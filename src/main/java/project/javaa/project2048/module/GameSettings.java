package project.javaa.project2048.module;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public enum GameSettings {

    LOCAL;

    public final static int MARGIN = 36;
    private static Mode mode = Mode.NORMAL;
    private final File GameFolder;
    private static BooleanProperty isGuest = new SimpleBooleanProperty(true);
    private static String playerName = null;
    private static String userCSS = "default.css";
    private static int timeLimitInSeconds = 0;
    private static int stepLimit = 0;
    private static int GRID_SIZE = 4;

    public static int getGridSize() {
        return GRID_SIZE;
    }

    public static void setGridSize(int gridSize) {
        GRID_SIZE = gridSize;
    }

    public static int getFinalValueToWin() {
        return FINAL_VALUE_TO_WIN;
    }

    public static void setFinalValueToWin(int finalValueToWin) {
        FINAL_VALUE_TO_WIN = finalValueToWin;
    }

    private static int FINAL_VALUE_TO_WIN = 2048;
    GameSettings() {
        var userHome = System.getProperty("user.home");
        var gameDataPath = Path.of(userHome, ".Project2048");
        try{
            Files.createDirectories(gameDataPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        GameFolder = gameDataPath.toFile();
    }

    public static Mode getMode() {
        return mode;
    }

    public static void setMode(Mode mode) {
        GameSettings.mode = mode;
    }

    public static boolean isGuest() {
        return isGuest.get();
    }

    public static BooleanProperty isGuestProperty() {
        return isGuest;
    }

    public static void setIsGuest(boolean isGuest) {
        GameSettings.isGuest.set(isGuest);
    }

    public static void setStepLimit(int stepLimit) {
        GameSettings.stepLimit = stepLimit;
    }

    public static int getStepLimit() {
        return stepLimit;
    }

    public File getGameFolder() {
        return GameFolder;
    }
    public static int getTimeLimitInSeconds() {
        return timeLimitInSeconds;
    }

    public static void setTimeLimitInSeconds(int timeLimitInSeconds) {
        GameSettings.timeLimitInSeconds = timeLimitInSeconds;
    }
    public static String getPlayerName() {
        return playerName;
    }

    public static void setPlayerName(String playerName) {
        GameSettings.playerName = playerName;
    }

    public static String getUserCSS() {
        return userCSS;
    }

    public static void setUserCSS(String userCSS) {
        GameSettings.userCSS = userCSS;
    }

    public void restore(Properties data) {
        data.forEach((key, value) -> {
            if(key.equals("isGuest")) {
                isGuest.set(Boolean.parseBoolean((String) value));
            } else if(key.equals("userName")) {
                playerName = (String) value;
            } else if(key.equals("userMode")) {
                mode = Mode.valueOf((String) value);
            } else if(key.equals("userCSS")) {
                userCSS = (String) value;
            }
        });
    }

}

