package project.javaa.project2048.view;

import javafx.animation.*;
import javafx.util.Duration;
import project.javaa.project2048.*;
import project.javaa.project2048.GameSettings;
import project.javaa.project2048.module.NumberTable;

import java.util.Objects;

public class GameGrid {
    public static Tile[][] gameGrid;
    public static int gridSize;
    public static int finalValueToWin;
    public static int score = 0;

    private volatile boolean movingTiles = false;
    private static NumberTable numberTable;
    private Animation shakingAnimation;
    GameGrid(){
        gridSize = GameSettings.LOCAL.getGridSize();
        finalValueToWin = GameSettings.LOCAL.getFinalValueToWin();
        numberTable = new NumberTable(gridSize,finalValueToWin);
        gameGrid = new Tile[gridSize][gridSize];
    }
    private void startGame(Board board){
        score = 0;
        numberTable.init();
        for (int y = 0; y < gridSize; y++) {
            for (int x = 0; x < gridSize; x++) {
                if (numberTable.getValue(x, y) != 0) {
                    gameGrid[y][x] = new Tile(numberTable.getValue(x, y));
                    gameGrid[y][x].setLocation(new Location(x, y));
                    gameGrid[y][x].setTranslateX(x * Board.CELL_SIZE);
                    gameGrid[y][x].setTranslateY(y * Board.CELL_SIZE);
                    board.getChildren().add(gameGrid[y][x]);
                }
            }
        }
    }
    private void moveTiles(Direction direction){

    }
    private void redrawTiles(){

    }
    private void changeTile(){

    }
    private ScaleTransition animateNewlyAddedTile(Tile tile) {
        final var scaleTransition = new ScaleTransition(ANIMATION_NEWLY_ADDED_TILE, tile);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.setInterpolator(Interpolator.EASE_OUT);
        scaleTransition.setOnFinished(e -> {
            // after last movement on full grid, check if there are movements available
            if (this.gameGrid.values().parallelStream().noneMatch(Objects::isNull) && mergeMovementsAvailable() == 0) {
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
    public void setScale(double scale) {
        this.setScaleX(scale);
        this.setScaleY(scale);
    }
}
