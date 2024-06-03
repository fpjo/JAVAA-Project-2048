package project.javaa.project2048;

import javafx.animation.*;
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
import project.javaa.project2048.module.GameSettings;
import project.javaa.project2048.view.Tile;

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
    private final ArrayList<int[][]> numberTable;
    private int roundsCnt;

    private final Board board;
    private Animation shakingAnimation;

    public GameManager(){ this(GameSettings.LOCAL); }

    public GameManager(GameSettings settings){
        GRID_SIZE = settings.getGridSize();
        this.gameGrid = new Tile[GRID_SIZE][GRID_SIZE];
        this.freeLocations = new ArrayList<>();
        this.roundsCnt=0;
        this.numberTable = new ArrayList<>();
        board = new Board(settings);
        board.setToolBar(createToolBar());
        this.getChildren().add(board);

        var trueProperty = new SimpleBooleanProperty(true);
        board.clearGameProperty().and(trueProperty).addListener((ov, b1, b2) -> initializeGameGrid());
        board.resetGameProperty().and(trueProperty).addListener((ov, b1, b2) -> startGame());
//        board.restoreGameProperty().and(trueProperty).addListener((ov, b1, b2) -> doRestoreSession());
//        board.saveGameProperty().and(trueProperty).addListener((ov, b1, b2) -> doSaveSession());

        initializeGameGrid();
        startGame();
    }
    private void initializeGameGrid() {
        for(int i=0;i<GRID_SIZE;i++){
            for(int j=0;j<GRID_SIZE;j++){
                gameGrid[i][j] = null;
            }
        }
        updateFreeLocations();
    }
    private void updateRound(){
        roundsCnt++;
        recordNumberTable();
    }
    private void recordNumberTable(){
        numberTable.add(new int[GRID_SIZE][GRID_SIZE]);
        for(int i=0;i<GRID_SIZE;i++){
            for(int j=0;j<GRID_SIZE;j++){
                numberTable.get(roundsCnt)[i][j]=gameGrid[i][j]==null?0:gameGrid[i][j].getValue();
            }
        }
    }
    private void startGame() {
        Collections.shuffle(freeLocations);
        var loc0= freeLocations.getFirst();
        gameGrid[loc0.getY()][loc0.getX()]=new Tile(2,loc0);
        freeLocations.removeFirst();
        var loc1=freeLocations.getFirst();
        gameGrid[loc1.getY()][loc1.getX()]=new Tile(4,loc1);
        freeLocations.removeFirst();
        updateRound();
        redrawTilesInGameGrid();
        board.startGame();
    }

    /**
     * Redraws all tiles in the <code>gameGrid</code> object
     */
    private void redrawTilesInGameGrid() {
        for(int i=0;i<GRID_SIZE;i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (gameGrid[i][j] != null) {
                    board.addTile(gameGrid[i][j]);
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
                int xp=x,yp=y;
                var currentLocation = new Location(x, y);
                while(boundaryCheck(x+xm,y+ym)&&gameGrid[yp+ym][xp+xm]==null){
                    xp+=xm;
                    yp+=ym;
                }
                var tile0=gameGrid[x][y];
                if(boundaryCheck(xp+xm,yp+ym)&& !gameGrid[x + xm][y + ym].isMerged() && Objects.equals(gameGrid[y + ym][x + xm].getValue(), gameGrid[y][x].getValue())){
                    var tileF=gameGrid[x+xm][y+ym];
                    tileF.merge(tile0);
                    tileF.toFront();
                    parallelTransition.getChildren().add(animateExistingTile(tile0, tileF.getLocation()));
                    parallelTransition.getChildren().add(animateMergedTile(tileF));
                    gameGrid[x+xm][y+ym]=tile0;
                    gameGrid[x][y]=null;
                    mergedToBeRemoved.add(tile0);
                    board.addPoints(tileF.getValue());
                    movedCnt++;
                }else if(x!=xp && y!=yp){
                    parallelTransition.getChildren().add(animateExistingTile(tile0, new Location(xp,yp)));
                    gameGrid[xp][yp]=tile0;
                    gameGrid[x][y]=null;
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
                        if(gameGrid[i][j]!=null){
                            gameGrid[i][j].clearMerge();
                        }
                    }
                }
                roundsCnt++;
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
            // no tiles got moved
            // shake the game pane
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
                if(gameGrid[i][j]==null){
                    freeLocations.add(new Location(j,i));
                }
            }
        }
    }

    private boolean shakingAnimationPlaying = false;
    private boolean shakingXYState = false;

    private boolean mergeMovementsAvailable() {
        for(int i=0;i<GRID_SIZE;i++){
            for(int j=0;j<GRID_SIZE;j++){
                if(gameGrid[i][j]==null) continue;
                if(j+1<GRID_SIZE && Objects.equals(gameGrid[i][j].getValue(), gameGrid[i][j + 1].getValue())) return true;
                if(i+1<GRID_SIZE && gameGrid[i][j].getValue().equals(gameGrid[i + 1][j].getValue())) return true;
            }
        }
        return false;
    }

    /**
     * Adds a tile of random value to a random location with a proper animation
     *
     */
    private void addAndAnimateRandomTile(Location randomLocation) {
        var tile = board.addRandomTile(randomLocation);
        gameGrid[randomLocation.getY()][randomLocation.getX()] = tile;

        animateNewlyAddedTile(tile).play();
    }

    /**
     * Animation that creates a fade in effect when a tile is added to the game by
     * increasing the tile scale from 0 to 100%
     *
     * @param tile to be animated
     * @return a scale transition
     */
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

    /**
     * Animation that moves the tile from its previous location to a new location
     *
     * @param tile        to be animated
     * @param newLocation new location of the tile
     * @return a timeline
     */
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

    /**
     * Animation that creates a pop effect when two tiles merge by increasing the
     * tile scale to 120% at the middle, and then going back to 100%
     *
     * @param tile to be animated
     * @return a sequential transition
     */
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

    /**
     * Move the tiles according user input if overlay is not on
     *
     */
    public void move(Direction direction) {
        if (!board.isLayerOn().get()) {
            moveTiles(direction);
        }
    }

    /**
     * Set gameManager scale to adjust overall game size
     *
     */
    public void setScale(double scale) {
        this.setScaleX(scale);
        this.setScaleY(scale);
    }

    /**
     * Pauses the game time, covers the grid
     */
    public void pauseGame() {
        board.pauseGame();
    }

    /**
     * Quit the game with confirmation
     */
    public void quitGame() {
        board.quitGame();
    }

    /**
     * Ask to save the game from a properties file with confirmation
     */
//    public void saveSession() {
//        board.saveSession();
//    }

    /**
     * Save the game to a properties file, without confirmation
     */
//    private void doSaveSession() {
//        board.saveSession(numberTable.get(roundsCnt));
//    }

    /**
     * Ask to restore the game from a properties file with confirmation
     */
//    public void restoreSession() {
//        board.restoreSession();
//    }

    /**
     * Restore the game from a properties file, without confirmation
     */
//    private void doRestoreSession() {
//        initializeGameGrid();
//        if (board.restoreSession(gameGrid)) {
//            redrawTilesInGameGrid();
//        }
//    }

    /**
     * Save actual record to a properties file
     */
    public void saveRecord() {
        board.saveRecord();
    }

    private HBox createToolBar() {
//        var btnSave = createButtonItem("mSave", "Save Session", t -> saveSession());
//        var btnLogin = createButtonItem("mLogin", "Login", t -> board.login());
//        var btnRestore = createButtonItem("mRestore", "Restore Session", t -> restoreSession());
        var btnPause = createButtonItem("mPause", "Pause Game", t -> board.pauseGame());
        var btnReset = createButtonItem("mReset", "Reset", t -> board.showTryAgainOverlay());
        var btnSettings = createButtonItem("mSettings", "Settings", t -> board.aboutGame()); //about to modify
        var btnQuit = createButtonItem("mQuit", "Quit Game", t -> quitGame());


//        var toolbar = new HBox(btnSave, btnLogin, btnRestore, btnPause, btnReset, btnSettings, btnQuit);
        var toolbar = new HBox(btnPause, btnReset, btnSettings, btnQuit);
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