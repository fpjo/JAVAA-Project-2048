package project.javaa.project2048.module;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public enum GameSettings {

    LOCAL;

    public final static int MARGIN = 36;
    private static Mode mode = Mode.NORMAL;
    private final File GameFolder;
    private static boolean isGuest = true;
    private static String playerName = null;
    private static String userCSS = "default.css";
    private static final int GRID_SIZE = 4;
    private static final int FINAL_VALUE_TO_WIN = 2048;
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
    public static String getCss() {
        return userCSS;
    }
    public int getGridSize() {
        return GRID_SIZE;
    }
    public int getFinalValueToWin() {
        return FINAL_VALUE_TO_WIN;
    }

    public File getGameFolder() {
        return GameFolder;
    }

    public static boolean isGuest() {
        return isGuest;
    }

    public static String getPlayerName() {
        return playerName;
    }
    public static Mode getMode() {
        return mode;
    }
    public static void setIsGuest(boolean isGuest) {
        GameSettings.isGuest = isGuest;
    }
    public static void setMode(Mode mode) {
        GameSettings.mode = mode;
    }
    public static void setPlayerName(String playerName) {
        GameSettings.playerName = playerName;
    }

    public static void setUserCSS(String userCSS) {
        GameSettings.userCSS = userCSS;
    }

    public void restore(Properties data) {
        data.forEach((key, value) -> {
            if(key.equals("isGuest")) {
                isGuest = Boolean.parseBoolean((String) value);
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

