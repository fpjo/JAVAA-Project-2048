package project.javaa.project2048;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.javaa.project2048.module.GameSettings;
import project.javaa.project2048.module.Mode;

public class SettingPane extends VBox {
    private ComboBox<String> gameMode;
    private ComboBox<String> gridSize;
    private HBox timeLimit;
    private ComboBox<Integer> stepLimit;
    private Label minutesLabel;
    private ComboBox<Integer> minutes;
    private Label secondsLabel;
    private ComboBox<Integer> seconds;
    private ComboBox<String> cssSetting;
    private Button saveButton;
    public SettingPane(Stage stage) {
        gameMode = new ComboBox<>();
        gameMode.getItems().addAll("NORMAL", "TIME LIMITED", "STEP LIMITED");
        gameMode.setValue("NORMAL");
        gameMode.setVisibleRowCount(3);

        gridSize = new ComboBox<>();
        gridSize.getItems().addAll("4", "5", "6", "7", "8", "9", "10");
        gridSize.setValue("4");
        gridSize.setVisibleRowCount(3);
        minutes = new ComboBox<>();
        for(int i = 0;i<60;i++){
            minutes.getItems().add(i);
        }
        minutes.setValue(1);
        minutes.setVisibleRowCount(5);

        seconds = new ComboBox<>();
        for (int i = 0; i < 60; i++) {
            seconds.getItems().add(i);
        }
        seconds.setValue(0);

        minutesLabel = new Label("Minutes");
        secondsLabel = new Label("Seconds");

        timeLimit = new HBox(minutesLabel, minutes, secondsLabel, seconds);
        timeLimit.setDisable(true);

        stepLimit = new ComboBox<>();
        stepLimit.setEditable(true);
        stepLimit.getItems().addAll(100, 200, 300, 400, 500);
        stepLimit.setValue(100);
        stepLimit.setDisable(true);

        cssSetting = new ComboBox<>();
        cssSetting.getItems().addAll("Default");
        cssSetting.setValue("Default");

        saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            if(minutes.getValue() == 0 && seconds.getValue() == 0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Time Limit Error");
                alert.setContentText("Time limit cannot be 0");
                ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                alert.getButtonTypes().setAll(buttonTypeOk);
                alert.showAndWait();
                return;
            }
            switch(gameMode.getValue()){
                case "NORMAL" -> GameSettings.setMode(Mode.NORMAL);
                case "TIME LIMITED" -> GameSettings.setMode(Mode.TIMELIMITED);
                case "STEP LIMITED" -> GameSettings.setMode(Mode.STEPLIMITED);
            }
            GameSettings.setGridSize(Integer.parseInt(gridSize.getValue()));
            if(!timeLimit.isDisable())GameSettings.setTimeLimitInSeconds(minutes.getValue() * 60 + seconds.getValue());
            if(!stepLimit.isDisable())GameSettings.setStepLimit(stepLimit.getValue());
            GameSettings.setGridSize(Integer.parseInt(gridSize.getValue()));
            GameSettings.setUserCSS(cssSetting.getValue());
            stage.close();
        });

        gameMode.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.equals("Time Limit")) {
                timeLimit.setDisable(false);
            } else if(newVal.equals("Step Limit")){
                stepLimit.setDisable(false);

            } else {
                timeLimit.setDisable(true);
                stepLimit.setDisable(true);
            }
        });
        this.getChildren().addAll(gameMode, gridSize, timeLimit, stepLimit, cssSetting, saveButton);
    }

    public void display() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Settings");
        stage.setScene(new Scene(this, 300, 200));
        stage.showAndWait();
    }
}
