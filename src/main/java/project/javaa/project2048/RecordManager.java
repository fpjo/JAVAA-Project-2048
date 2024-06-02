package project.javaa.project2048;

import javafx.beans.property.StringProperty;
import project.javaa.project2048.module.GameSettings;
import project.javaa.project2048.view.Tile;

import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

public class RecordManager {
    public final String propertiesFilename = null;
    private final Properties props = new Properties();
    private final GridOperator gridOperator;

    public RecordManager(GameSettings userSettings) {
        this.propertiesFilename = "game2048_" + userSettings.getGridSize() + "_record.properties";
    }

    protected void saveSession(Tile[][] gameGrid, Integer score, Long time) {
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"));
        String RecordFILENAME = "game2048_" + formattedDateTime +"_record.properties";
        try {
            FileOutputStream file = new FileOutputStream(propertiesFilename);
        }
        props.setProperty("score", score.toString());
        props.setProperty("time", time.toString());
        UserSettings.LOCAL.store(props, propertiesFilename);
    }

    protected int restoreSession(Map<Location, Tile> gameGrid, StringProperty time) {
        UserSettings.LOCAL.restore(props, propertiesFilename);

        gridOperator.traverseGrid((x, y) -> {
            var val = props.getProperty("Location_" + x + "_" + y);
            if (!val.equals("0")) {
                var tile = Tile.newTile(Integer.parseInt(val));
                var location = new Location(x, y);
                tile.setLocation(location);
                gameGrid.put(location, tile);
            }
            return 0;
        });

        time.set(props.getProperty("time"));

        var score = props.getProperty("score");
        if (score != null) {
            return Integer.parseInt(score);
        }
        return 0;
    }
}
