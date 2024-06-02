package project.javaa.project2048.module;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public enum GameSettings {

    LOCAL;

    public final static int MARGIN = 36;
    private final File userGameFolder;
    private static boolean isGuest = true;
    private static String playerName = null;
    private static String userCSS = "default.css";
    private static final int GRID_SIZE = 4;
    private static final int FINAL_VALUE_TO_WIN = 2048;
    private

    GameSettings() {
        var userHome = System.getProperty("user.home");
        var gameDataPath = Path.of(userHome, ".Project2048");
        try{
            Files.createDirectories(gameDataPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        userGameFolder = gameDataPath.toFile();
    }
    public String getCss() {
        return userCSS;
    }
    public int getGridSize() {
        return GRID_SIZE;
    }
    public int getFinalValueToWin() {
        return FINAL_VALUE_TO_WIN;
    }
    public void store(Properties data, String fileName) {
        try {
            data.store(new FileWriter(new File(userGameFolder, fileName)), fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

