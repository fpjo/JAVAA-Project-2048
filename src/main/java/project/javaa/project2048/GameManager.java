package project.javaa.project2048;

import javafx.animation.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.util.*;

public class GameManager extends Group {
    public static int FINAL_VALUE_TO_WIN = 2048;
    public static int GRID_SIZE = 4;

    private static final Duration ANIMATION_EXISTING_TILE = Duration.millis(65);
    private static final Duration ANIMATION_NEWLY_ADDED_TILE = Duration.millis(125);
    private static final Duration ANIMATION_MERGED_TILE = Duration.millis(80);

    private volatile boolean movingTiles = false;
    private final ArrayList<Location> freeLocations;
    private final Tile[][] gameGrid;
    private final Set<Tile> mergedToBeRemoved = new HashSet<>();
    private int roundsCnt;

    private final Board board;
    private final GameState state = GameState.getInstance();
    private Animation shakingAnimation;

    public GameManager(){
        GRID_SIZE = GameSettings.getGridSize();
        FINAL_VALUE_TO_WIN = GameSettings.getFinalValueToWin();
        this.gameGrid = new Tile[GRID_SIZE][GRID_SIZE];
        for(int i=0;i<GRID_SIZE;i++){
            for(int j=0;j<GRID_SIZE;j++){
                gameGrid[j][i] = null;
            }
        }
        this.freeLocations = new ArrayList<>();
        state.gameRoundProperty.set(0);
        board = new Board();
        board.setToolBar(createToolBar());
        board.setOperatorBar(createOperatorBar());
        this.getChildren().add(board);

        BooleanProperty trueProperty = new SimpleBooleanProperty(true);
        board.clearGameProperty().and(trueProperty).addListener((ov, b1, b2) -> initializeGameGrid());
        board.resetGameProperty().addListener((ov, b1, b2) -> {
            if(b2) {
                System.out.println("reset heard");
                startGame();
                board.resetGameProperty().set(false);
            }
        });
        board.restoreGameProperty().addListener((ov, b1, b2) -> {
            if(b2){
                doRestoreSession();
                board.restoreGameProperty().set(false);
            }
        });
        board.saveGameProperty().addListener((ov, b1, b2) -> {
            if(b2) {
                doSaveSession();
            }
        });
        startGame();
    }
    private void initializeGameGrid() {
        for(int i=0;i<GRID_SIZE;i++){
            for(int j=0;j<GRID_SIZE;j++){
                gameGrid[j][i] = null;
            }
        }
        updateFreeLocations();
    }
    private void updateRound(){
        int[][] table = new int[GRID_SIZE][GRID_SIZE];
        for(int i=0;i<GRID_SIZE;i++){
            for(int j=0;j<GRID_SIZE;j++){
                table[j][i]=(gameGrid[j][i]==null)?0:gameGrid[j][i].getValue();
            }
        }
        state.addTable(table);
    }
    private void startGame() {
        initializeGameGrid();
        Collections.shuffle(freeLocations);
        var loc0= freeLocations.getFirst();
        gameGrid[loc0.getY()][loc0.getX()]=new Tile(2,loc0);
        freeLocations.removeFirst();
        var loc1=freeLocations.getFirst();
        gameGrid[loc1.getY()][loc1.getX()]=new Tile(4,loc1);
        freeLocations.removeFirst();
        state.clearTableList();
        updateRound();
        redrawTilesInGameGrid();
        board.startGame();
    }

    private void redrawTilesInGameGrid() {
        synchronized (gameGrid){
            for(int i=0;i<GRID_SIZE;i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    if (gameGrid[j][i] != null) {
                        board.addTile(gameGrid[j][i]);
                    }
                }
            }
        }
    }

    private boolean boundaryCheck(int x, int y){
        return x>=0 && x<GRID_SIZE && y>=0 && y<GRID_SIZE;
    }

    private void moveTiles(Direction direction) {
        synchronized (gameGrid) {
            if (movingTiles) {
                return;
            }
        }

        board.setPoints(0);
        mergedToBeRemoved.clear();
        var parallelTransition = new ParallelTransition();
        int setXs=0, setYs=0, setXa=1, setYa=1;
        int xm=direction.getX(),ym=direction.getY();
        if(direction==Direction.RIGHT){setXs=GRID_SIZE-1;setXa=-1;}
        if(direction==Direction.DOWN){setYs=GRID_SIZE-1;setYa=-1;}
        int movedCnt=0;
        for(int x=setXs;x>=0 && x<GRID_SIZE;x+=setXa){
            for(int y=setYs;y>=0 && y<GRID_SIZE;y+=setYa){
                if(gameGrid[y][x]==null)continue;
                int xp=x,yp=y;
                var currentLocation = new Location(x, y);
                while(boundaryCheck(xp+xm,yp+ym) && gameGrid[yp+ym][xp+xm]==null){
                    xp+=xm;
                    yp+=ym;
                }
                var tile0=gameGrid[y][x];
                if(boundaryCheck(xp+xm,yp+ym)&& !gameGrid[yp + ym][xp + xm].isMerged() && Objects.equals(gameGrid[yp + ym][xp + xm].getValue(), gameGrid[y][x].getValue())){
                    var tileF=gameGrid[yp+ym][xp+xm];
                    tileF.merge(tile0);
                    tileF.toFront();
                    parallelTransition.getChildren().add(animateExistingTile(tile0, tileF.getLocation()));
                    parallelTransition.getChildren().add(animateMergedTile(tileF));
                    mergedToBeRemoved.add(tile0);
                    gameGrid[y][x]=null;
                    board.addPoints(tileF.getValue());
                    if(tileF.getValue()==FINAL_VALUE_TO_WIN){
                        board.setGameWin(true);
                    }
                    movedCnt++;
                }else if(x!=xp || y!=yp){
                    parallelTransition.getChildren().add(animateExistingTile(tile0, new Location(xp,yp)));
                    gameGrid[yp][xp]=tile0;
                    gameGrid[y][x]=null;
                    tile0.setLocation(new Location(xp,yp));
                    movedCnt++;
                }
            }
        }//待检验
        board.animateScore();
        if (!parallelTransition.getChildren().isEmpty()) {
            int finalMovedCnt = movedCnt;
            parallelTransition.setOnFinished(e -> {
                board.removeTiles(mergedToBeRemoved);
                for(int i=0;i<GRID_SIZE;i++){
                    for(int j=0;j<GRID_SIZE;j++){
                        if(gameGrid[j][i]==null)continue;
                        gameGrid[j][i].clearMerge();
                    }
                }
                updateRound();
                updateFreeLocations();
                if (freeLocations.isEmpty() && !mergeMovementsAvailable()) {
                    board.setGameOver(true);
                } else if (!freeLocations.isEmpty() && finalMovedCnt > 0) {
                    synchronized (gameGrid) {
                        movingTiles = false;
                    }
                    Collections.shuffle(freeLocations);
                    addAndAnimateRandomTile(freeLocations.getFirst());
                }
            });

            synchronized (gameGrid) {
                movingTiles = true;
            }
            parallelTransition.play();
        }

        if (movedCnt == 0) {
            if (shakingAnimation == null) {
                shakingAnimation = createShakeGamePaneAnimation();
            }

            if (!shakingAnimationPlaying) {
                shakingAnimation.play();
                shakingAnimationPlaying = true;
            }
        }
    }

    private void updateFreeLocations() {
        freeLocations.clear();
        for(int i=0;i<GRID_SIZE;i++){
            for(int j=0;j<GRID_SIZE;j++){
                if(gameGrid[j][i]==null){
                    freeLocations.add(new Location(i,j));
                }
            }
        }
    }

    private boolean shakingAnimationPlaying = false;
    private boolean shakingXYState = false;

    private boolean mergeMovementsAvailable() {
        for(int i=0;i<GRID_SIZE;i++){
            for(int j=0;j<GRID_SIZE;j++){
                if(gameGrid[j][i]==null) continue;
                if(j+1<GRID_SIZE && Objects.equals(gameGrid[j][i].getValue(), gameGrid[j + 1][i].getValue())) return true;
                if(i+1<GRID_SIZE && gameGrid[j][i].getValue().equals(gameGrid[j][i + 1].getValue())) return true;
            }
        }
        return false;
    }

    private void addAndAnimateRandomTile(Location randomLocation) {
        var tile = board.addRandomTile(randomLocation);
        gameGrid[randomLocation.getY()][randomLocation.getX()] = tile;

        animateNewlyAddedTile(tile).play();
    }

    private ScaleTransition animateNewlyAddedTile(Tile tile) {
        final var scaleTransition = new ScaleTransition(ANIMATION_NEWLY_ADDED_TILE, tile);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.setInterpolator(Interpolator.EASE_OUT);
        scaleTransition.setOnFinished(e -> {
            synchronized (gameGrid){
                updateFreeLocations();
            }
            // after last movement on full grid, check if there are available
            if (freeLocations.isEmpty() && !mergeMovementsAvailable()) {
                board.setGameOver(true);
            }
        });
        return scaleTransition;
    }

    private Animation createShakeGamePaneAnimation() {
        var shakingAnimation = new Timeline(new KeyFrame(Duration.seconds(0.05), (ae) -> {
            var parent = getParent();

            if (shakingXYState) {
                parent.setLayoutX(parent.getLayoutX() + 5);
                parent.setLayoutY(parent.getLayoutY() + 5);
            } else {
                parent.setLayoutX(parent.getLayoutX() - 5);
                parent.setLayoutY(parent.getLayoutY() - 5);
            }

            shakingXYState = !shakingXYState;
        }));

        shakingAnimation.setCycleCount(6);
        shakingAnimation.setAutoReverse(false);
        shakingAnimation.setOnFinished(event -> {
            shakingXYState = false;
            shakingAnimationPlaying = false;
        });

        return shakingAnimation;
    }

    private Timeline animateExistingTile(Tile tile, Location newLocation) {
        var timeline = new Timeline();
        var kvX = new KeyValue(tile.layoutXProperty(),
                newLocation.getLayoutX(Board.CELL_SIZE) - (tile.getMinHeight() / 2), Interpolator.EASE_OUT);
        var kvY = new KeyValue(tile.layoutYProperty(),
                newLocation.getLayoutY(Board.CELL_SIZE) - (tile.getMinHeight() / 2), Interpolator.EASE_OUT);

        var kfX = new KeyFrame(ANIMATION_EXISTING_TILE, kvX);
        var kfY = new KeyFrame(ANIMATION_EXISTING_TILE, kvY);

        timeline.getKeyFrames().add(kfX);
        timeline.getKeyFrames().add(kfY);

        return timeline;
    }

    private SequentialTransition animateMergedTile(Tile tile) {
        final var scale0 = new ScaleTransition(ANIMATION_MERGED_TILE, tile);
        scale0.setToX(1.2);
        scale0.setToY(1.2);
        scale0.setInterpolator(Interpolator.EASE_IN);

        final var scale1 = new ScaleTransition(ANIMATION_MERGED_TILE, tile);
        scale1.setToX(1.0);
        scale1.setToY(1.0);
        scale1.setInterpolator(Interpolator.EASE_OUT);

        return new SequentialTransition(scale0, scale1);
    }

    public void move(Direction direction) {
        if (!board.isLayerOn().get()) {
            moveTiles(direction);
        }
    }

    public void setScale(double scale) {
        this.setScaleX(scale);
        this.setScaleY(scale);
    }

    public void pauseGame() {
        board.pauseGame();
    }

    public void quitGame() {
        board.quitGame();
    }

    public void saveSession() {
        board.saveSession();
    }

    private void doSaveSession() {
        board.saveSession(gameGrid,roundsCnt);
    }

    public void restoreSession() {
        board.restoreSession();
    }

    private void doRestoreSession() {
        System.out.println("Start restoring session...");
        initializeGameGrid();
        System.out.println("Game grid initialized.");
        boolean flag= board.restoreSession(gameGrid);
        System.out.println("Game state restored: " + flag);
        if (flag) {
            state.clearTableList();
            updateRound();
            redrawTilesInGameGrid();
            updateFreeLocations();
            board.startGame();
        }
    }

    private HBox createOperatorBar(){
        var btnUp = createButtonItem("mUp", "Move Up", t -> move(Direction.UP));
        var btnDown = createButtonItem("mDown", "Move Down", t -> move(Direction.DOWN));
        var btnLeft = createButtonItem("mLeft", "Move Left", t -> move(Direction.LEFT));
        var btnRight = createButtonItem("mRight", "Move Right", t -> move(Direction.RIGHT));
        var operatorBar = new HBox(btnUp, btnDown, btnLeft, btnRight);
        operatorBar.setAlignment(Pos.CENTER);
        operatorBar.setPadding(new Insets(10.0));
        return operatorBar;
    }
    private HBox createToolBar() {
        var btnSave = createButtonItem("mSave", "Save Session", t -> saveSession());
        var btnRestore = createButtonItem("mRestore", "Restore Session", t -> restoreSession());
        var btnPause = createButtonItem("mPause", "Pause Game", t -> board.pauseGame());
        var btnReset = createButtonItem("mReplay", "Reset", t -> board.showTryAgainOverlay());
        var btnQuit = createButtonItem("mQuit", "Quit Game", t -> quitGame());


        var toolbar = new HBox(btnSave, btnRestore, btnPause, btnReset, btnQuit);
        toolbar.setAlignment(Pos.CENTER);
        toolbar.setPadding(new Insets(10.0));
        return toolbar;
    }

    private Button createButtonItem(String symbol, String text, EventHandler<ActionEvent> t) {
        var g = new Button();
        g.setPrefSize(40, 40);
        g.setId(symbol);
        g.setOnAction(t);
        g.setTooltip(new Tooltip(text));
        return g;
    }
}
