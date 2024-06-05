package project.javaa.project2048;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Objects;

public class SettingPane extends VBox {
    private Button saveButton;
    public SettingPane(Stage stage) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.getStyleClass().add("game-root");
        Label title = new Label("Settings");
        title.getStyleClass().addAll("game-title","game-label");

        setGameMode();
        setGridSize();
        setFinalValueToWin();
        setTimeLimit();
        setStepLimit();
        setCSSSetting();
        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.addRow(0,gameMode);
        gridPane.addRow(1,gridSize);
        gridPane.addRow(2,finalValueToWin);
        gridPane.addRow(3,timeLimit);
        gridPane.addRow(4,stepLimit);
        gridPane.addRow(5,cssSetting);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.getStyleClass().add("game-root");
        gridPane.setPrefSize(300, 200);

        saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            if(Objects.equals(gameModeOption.getValue(), "TIME LIMITED") && minutes.getValue() == 0 && seconds.getValue() == 0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Time Limit Error");
                alert.setContentText("Time limit cannot be 0");
                ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                alert.getButtonTypes().setAll(buttonTypeOk);
                alert.showAndWait();
                return;
            }
            int stepLimitValue;
            try {
                stepLimitValue = Integer.parseInt(stepLimitOption.getValue());
            }catch (NumberFormatException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Step Limit Error");
                alert.setContentText("Step limit must be a Integer");
                ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                alert.getButtonTypes().setAll(buttonTypeOk);
                alert.showAndWait();
                return;
            }
            if(Objects.equals(gameModeOption.getValue(), "STEP LIMITED") && stepLimitValue<= 0){
                System.out.println(stepLimitOption.getValue()+" "+stepLimitValue);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Step Limit Error");
                alert.setContentText("Step limit must > 0");
                ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                alert.getButtonTypes().setAll(buttonTypeOk);
                alert.showAndWait();
                return;
            }
            switch(gameModeOption.getValue()){
                case "NORMAL" -> GameSettings.setMode(Mode.NORMAL);
                case "TIME LIMITED" -> GameSettings.setMode(Mode.TIMELIMITED);
                case "STEP LIMITED" -> GameSettings.setMode(Mode.STEPLIMITED);
            }
            GameSettings.setGridSize(Integer.parseInt(gridSizeOption.getValue()));
            GameSettings.setFinalValueToWin(finalValueToWinOption.getValue());
            if(!timeLimit.isDisable())GameSettings.setTimeLimitInSeconds(minutes.getValue() * 60 + seconds.getValue());
            if(!stepLimitOption.isDisable())GameSettings.setStepLimit(stepLimitValue);
            GameSettings.setGridSize(Integer.parseInt(gridSizeOption.getValue()));
            GameSettings.setUserCSS(cssSettingOption.getValue()+".css");
            System.out.println(GameSettings.LOCAL);
            stage.close();
        });
        this.getChildren().addAll(title, gridPane,saveButton);
    }
    void setHBox(HBox hBox, Label label, ComboBox<?> comboBox){
        label.getStyleClass().add("option-label");
        comboBox.getStyleClass().add("option-comboBox");
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(hBox, Priority.ALWAYS);
    }
    private ComboBox<String> gameModeOption;
    private Label gameModeLabel;
    private HBox gameMode;
    void setGameMode(){
        gameModeOption = new ComboBox<>();
        gameModeOption.getItems().addAll("NORMAL", "TIME LIMITED", "STEP LIMITED");
        gameModeOption.setValue("NORMAL");
        gameModeOption.setVisibleRowCount(3);

        gameModeLabel = new Label("游戏模式");
        gameMode = new HBox(gameModeLabel, gameModeOption);
        setHBox(gameMode, gameModeLabel, gameModeOption);
    }
    private ComboBox<String> gridSizeOption;
    private Label gridSizeLabel;
    private HBox gridSize;
    void setGridSize(){
        gridSizeOption = new ComboBox<>();
        gridSizeOption.getItems().addAll("4", "5", "6", "7", "8", "9", "10");
        gridSizeOption.setValue("4");
        gridSizeOption.setVisibleRowCount(3);
        gridSizeLabel = new Label("棋盘边长");
        gridSize = new HBox(gridSizeLabel, gridSizeOption);
        setHBox(gridSize, gridSizeLabel, gridSizeOption);
    }
    private ComboBox<Integer> finalValueToWinOption;
    private Label finalValueToWinLabel;
    private HBox finalValueToWin;
    void setFinalValueToWin(){
        finalValueToWinOption = new ComboBox<>();
        int val=16;
        for(int i=0;i<15;i++){
            finalValueToWinOption.getItems().add(val);
            val*=2;
        }
        finalValueToWinOption.setValue(2048);
        finalValueToWinOption.setVisibleRowCount(3);
        finalValueToWinLabel = new Label("胜利条件");
        finalValueToWin = new HBox(finalValueToWinLabel, finalValueToWinOption);
        setHBox(finalValueToWin, finalValueToWinLabel, finalValueToWinOption);
    }
    private HBox timeLimit;
    private Label minutesLabel;
    private ComboBox<Integer> minutes;
    private Label secondsLabel;
    private ComboBox<Integer> seconds;
    void setTimeLimit(){
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
        seconds.setVisibleRowCount(5);
        minutesLabel = new Label("Min:");
        secondsLabel = new Label("Sec:");
        minutesLabel.getStyleClass().add("option-label");
        secondsLabel.getStyleClass().add("option-label");

        timeLimit = new HBox(minutesLabel, minutes, secondsLabel, seconds);
        timeLimit.disableProperty().bind(gameModeOption.valueProperty().isEqualTo("TIME LIMITED").not());
        timeLimit.setSpacing(10);
        timeLimit.setAlignment(Pos.CENTER);
        HBox.setHgrow(timeLimit, Priority.ALWAYS);
    }
    private ComboBox<String> stepLimitOption;
    private Label stepLimitLabel;
    private HBox stepLimit;
    void setStepLimit(){
        stepLimitOption = new ComboBox<>();
        stepLimitOption.setEditable(true);
        stepLimitOption.getItems().addAll("100", "200", "300", "400", "500");
        stepLimitOption.setValue("100");
        stepLimitLabel= new Label("步数限制");
        stepLimit = new HBox(stepLimitLabel, stepLimitOption);
        setHBox(stepLimit, stepLimitLabel, stepLimitOption);
        stepLimit.disableProperty().bind(gameModeOption.valueProperty().isEqualTo("STEP LIMITED").not());
    }
    private ComboBox<String> cssSettingOption;
    private Label cssSettingLabel;
    private HBox cssSetting;
    void setCSSSetting(){
        cssSettingOption = new ComboBox<>();
        cssSettingOption.getItems().addAll("Default","CustomizedStyle");
        cssSettingOption.setValue("Default");
        cssSettingOption.setVisibleRowCount(3);
        cssSettingLabel = new Label("皮肤设置");
        cssSetting = new HBox(cssSettingLabel, cssSettingOption);
        setHBox(cssSetting, cssSettingLabel, cssSettingOption);
    }
    public void display(Stage stage) {
        stage.setScene(new Scene(this, 300, 200));
        stage.showAndWait();
    }
}
