package project.javaa.project2048;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.Arrays;

class GameState {
    private static GameState instance = null;
    final IntegerProperty gameScoreProperty = new SimpleIntegerProperty(0);
    final IntegerProperty gameBestProperty = new SimpleIntegerProperty(0);
    final IntegerProperty gameRoundProperty = new SimpleIntegerProperty(0);
    final IntegerProperty gameMovePoints = new SimpleIntegerProperty(0);
    final BooleanProperty gameWonProperty = new SimpleBooleanProperty(false);
    final BooleanProperty gameOverProperty = new SimpleBooleanProperty(false);
    final BooleanProperty gameAboutProperty = new SimpleBooleanProperty(false);
    final BooleanProperty gamePauseProperty = new SimpleBooleanProperty(false);
    final BooleanProperty gameTryAgainProperty = new SimpleBooleanProperty(false);
    final BooleanProperty gameSaveProperty = new SimpleBooleanProperty(false);
    final BooleanProperty gameRestoreProperty = new SimpleBooleanProperty(false);
    final BooleanProperty gameQuitProperty = new SimpleBooleanProperty(false);
    final BooleanProperty layerOnProperty = new SimpleBooleanProperty(false);
    final BooleanProperty resetGame = new SimpleBooleanProperty(false);
    final BooleanProperty clearGame = new SimpleBooleanProperty(false);
    final BooleanProperty restoreGame = new SimpleBooleanProperty(false);
    final BooleanProperty saveGame = new SimpleBooleanProperty(false);
    private ArrayList<int[][]> tableList = new ArrayList<>();

    private GameState() {
    }
    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }
    public void keepGoing() {
        layerOnProperty.set(false);
        gamePauseProperty.set(false);
        gameTryAgainProperty.set(false);
        gameSaveProperty.set(false);
        gameRestoreProperty.set(false);
        gameAboutProperty.set(false);
        gameQuitProperty.set(false);
    }
    public void addTable(int[][] table) {
        gameRoundProperty.add(1);
        tableList.add(table);
    }
    public void clearState() {
        Arrays.asList(clearGame, resetGame, restoreGame, saveGame, layerOnProperty, gameWonProperty, gameOverProperty,
                gameAboutProperty, gamePauseProperty, gameTryAgainProperty, gameSaveProperty, gameRestoreProperty,
                gameQuitProperty).forEach(a -> a.set(false));
        tableList.clear();
        gameScoreProperty.set(0);
        gameRoundProperty.set(0);
        clearGame.set(true);
    }

    public void resetGame() {
        resetGame.set(true);
    }
}
