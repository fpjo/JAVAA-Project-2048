package project.javaa.project2048;

import javafx.beans.property.StringProperty;

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

public class RecordManager {
    private final Properties props = new Properties();
    private final int GRID_SIZE;
    private File userGameFolder;

    public RecordManager() {
        GRID_SIZE = GameSettings.LOCAL.getGridSize();
    }

    protected void saveRecord(Tile[][] gameGrid, Integer score, Integer round, StringProperty time) {
        if(GameSettings.isGuest()) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"));
        String recordFilename = "game2048_" + formattedDateTime +"_record.";
        props.setProperty("score", score.toString());
        props.setProperty("round", round.toString());
        props.setProperty("grid_size", Integer.toString(GRID_SIZE));
        props.setProperty("isGuest", Boolean.toString(GameSettings.isGuest()));
        props.setProperty("time", time.get());
        props.setProperty("userName",GameSettings.getPlayerName());
        props.setProperty("userMode", GameSettings.getMode().toString());
        props.setProperty("userCSS", GameSettings.getCss());
        for(int i=0;i<GRID_SIZE;i++) {
            for(int j=0;j<GRID_SIZE;j++) {
                props.setProperty("Location_" + i + "_" + j, gameGrid[i][j] != null ? gameGrid[i][j].getValue().toString() : "0");
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
                while (dis.read() != -1) ; //empty loop to clear the data
                digest = md.digest();
                System.out.println("SHA-256 hash: " + Arrays.toString(digest));
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
    protected boolean loadRecord(File recordFile, File recordHash) {
        if (GameSettings.isGuest()) {
            return false;
        }
        if (recordFile == null || recordHash == null) {
            return false;
        }
        try (FileInputStream fileInputStream = new FileInputStream(recordFile)) {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = new byte[0];
            try (DigestInputStream dis = new DigestInputStream(fileInputStream, md)) {
                while (dis.read() != -1) ; //empty loop to clear the data
                digest = md.digest();
                System.out.println("SHA-256 hash: " + Arrays.toString(digest));
            }
            try (FileInputStream file = new FileInputStream(recordHash)) {
                byte[] hash = file.readAllBytes();
                if (!Arrays.equals(digest, hash)) {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            props.clear();
            props.load(Files.newBufferedReader(recordFile.toPath()));
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return true;
    }

    protected boolean restoreRecord(Tile[][] gameGrid, Integer score, Integer round, StringProperty time) {
        if(props.isEmpty()) {
            return false;
        }
        if(props.getProperty("grid_size") == null || Integer.parseInt(props.getProperty("grid_size")) != GRID_SIZE) {
            return false;
        }
        if(!Boolean.parseBoolean(props.getProperty("isGuest")) && !Objects.equals(props.getProperty("userName"), GameSettings.getPlayerName())) {
            return false;
        }
        GameSettings.LOCAL.restore(props);
        for(int i=0;i<GRID_SIZE;i++) {
            for(int j=0;j<GRID_SIZE;j++) {
                var val = props.getProperty("Location_" + i + "_" + j);
                if (!val.equals("0")) {
                    var tile = Tile.newTile(Integer.parseInt(val));
                    tile.setLocation(new Location(i,j));
                    gameGrid[i][j] = tile;
                }
            }
        }
        time.set(props.getProperty("time"));
        score = Integer.parseInt(props.getProperty("score"));
        round = Integer.parseInt(props.getProperty("round"));
        return true;
    }
}
