package project.javaa.project2048;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecordManager {
    private final Properties props = new Properties();
    private int GRID_SIZE;
    private final String recordFilename;
    private File userGameFolder;

    public RecordManager() {
        GRID_SIZE=GameSettings.getGridSize();
        recordFilename = "game2048_"+"record";
    }

    protected void saveRecord(Tile[][] gameGrid, Integer score, Integer round,Long time) {
        if(GameSettings.isGuest()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("警告");
            alert.setHeaderText(null);
            alert.setContentText("游客模式下无法存入存档");
            alert.showAndWait();
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        GRID_SIZE=GameSettings.getGridSize();
        props.setProperty("score", score.toString());
        props.setProperty("round", round.toString());
        props.setProperty("grid_size", Integer.toString(GRID_SIZE));
        props.setProperty("isGuest", Boolean.toString(GameSettings.isGuest()));
        props.setProperty("time", time.toString());
        props.setProperty("userName",GameSettings.getPlayerName());
        props.setProperty("userMode", GameSettings.getMode().toString());
        props.setProperty("userCSS", GameSettings.getUserCSS());
        for(int i=0;i<GRID_SIZE;i++) {
            for(int j=0;j<GRID_SIZE;j++) {
                props.setProperty("Location_" + j + "_" + i, gameGrid[j][i] != null ? gameGrid[j][i].getValue().toString() : "0");
            }
        }
        try {
            var userDataPath = Path.of(GameSettings.LOCAL.getGameFolder().toString(), GameSettings.getPlayerName());
            userGameFolder = Files.createDirectories(userDataPath).toFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        var filePath = new File(userGameFolder, recordFilename + ".properties");
        try (FileOutputStream file = new FileOutputStream(filePath)) {
            props.store(file, "Game Session");
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] digest = new byte[0];
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            try (DigestInputStream dis = new DigestInputStream(fileInputStream, md)) {
                while (dis.read() != -1) ;
                digest = md.digest();
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        var hashFilePath = new File(userGameFolder, recordFilename + ".hash");
        try (FileOutputStream file = new FileOutputStream(hashFilePath)) {
            file.write(digest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected boolean checkHash(File recordFile){
        if (recordFile == null) {
            return false;
        }
        try (FileInputStream fileInputStream = new FileInputStream(recordFile)) {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = new byte[0];
            try (DigestInputStream dis = new DigestInputStream(fileInputStream, md)) {
                while (dis.read() != -1) ;
                digest = md.digest();
            }
            var hashFilePath = new File(recordFile.getParent(), recordFile.getName().replace(".properties", ".hash"));
            try (FileInputStream file = new FileInputStream(hashFilePath)) {
                byte[] hash = file.readAllBytes();
                return Arrays.equals(digest, hash);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }
    private boolean isValidProperties(Properties props) {
        String[] requiredKeys = {"score", "round", "grid_size", "isGuest", "time", "userName", "userMode", "userCSS"};
        for (String key : requiredKeys) {
            if (!props.containsKey(key)) {
                return false;
            }
        }
        return true;
    }
    protected boolean loadRecord(Tile[][] gameGrid, SimpleIntegerProperty score, SimpleIntegerProperty round,StringProperty time) {
        if(GameSettings.isGuest()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("警告");
            alert.setHeaderText(null);
            alert.setContentText("游客模式下无法读取存档");
            alert.showAndWait();
            return false;
        }
        try {
            var userDataPath = Path.of(GameSettings.LOCAL.getGameFolder().toString(), GameSettings.getPlayerName());
            userGameFolder = Files.createDirectories(userDataPath).toFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (var reader = new FileReader(new File(userGameFolder, recordFilename + ".properties"))) {
            props.clear();
            props.load(reader);
            if (!isValidProperties(props)) {
                return false;
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger(RecordManager.class.getName()).log(Level.INFO, "Previous game record not found.");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("警告");
            alert.setHeaderText(null);
            alert.setContentText("存档不存在");
            alert.showAndWait();
            return false;
        } catch (IOException ex) {
            Logger.getLogger(RecordManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        File recordFile = new File(userGameFolder, recordFilename + ".properties");
        boolean hashFlag = checkHash(recordFile);
        if (!hashFlag) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("警告");
            alert.setHeaderText(null);
            alert.setContentText("请注意，存档可能被篡改");
            alert.showAndWait();
        }
        if(props.isEmpty()) {
            return false;
        }
        if(props.getProperty("grid_size") == null || Integer.parseInt(props.getProperty("grid_size")) != GRID_SIZE) {
            return false;
        }
        if(Boolean.parseBoolean(props.getProperty("isGuest")) || !Objects.equals(props.getProperty("userName"), GameSettings.getPlayerName())) {
            return false;
        }
        GameSettings.LOCAL.restore(props);
        for(int i=0;i<GRID_SIZE;i++) {
            for(int j=0;j<GRID_SIZE;j++) {
                var val = props.getProperty("Location_" + j + "_" + i);
                if (!val.equals("0")) {
                    var tile = Tile.newTile(Integer.parseInt(val));
                    tile.setLocation(new Location(i,j));
                    gameGrid[j][i] = tile;
                }else{
                    gameGrid[j][i]=null;
                }
            }
        }
        time.set(props.getProperty("time"));
        score.set(Integer.parseInt(props.getProperty("score")));
        round.set(Integer.parseInt(props.getProperty("round")));
        System.out.println("loadRecord: sc"+score+" ro"+round);
        return true;
    }
}
