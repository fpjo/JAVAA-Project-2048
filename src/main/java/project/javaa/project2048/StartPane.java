package project.javaa.project2048;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;

public class StartPane extends VBox {
    private Button startButton;
    private Button exitButton;
    private Button settingButton;
    private Button loadRecordButton;
    private Button aboutButton;
    //    private SettingPane settingPane;
    public StartPane(Stage primaryStage) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.getStyleClass().add("game-root");

        Text title = new Text("Game 2048");
        title.getStyleClass().add("game-title");

        setStartButton(primaryStage);
        setExitButton(primaryStage);
        setSettingButton(primaryStage);
        setLoadRecordButton(primaryStage);
        aboutButton = new Button("About");

        this.getChildren().addAll(title, startButton, exitButton,settingButton,loadRecordButton,aboutButton);
    }
    private void setStartButton(Stage primaryStage) {
        startButton = new Button("Start");
        startButton.getStyleClass().add("start-button");
        startButton.setOnAction(e -> {
            System.out.println("Start button clicked");
//            GamePane gamePane = new GamePane(primaryStage);
//            Scene gameScene = new Scene(gamePane, 400, 600);
//            gameScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(GameSettings.getUserCSS())).toExternalForm());
//            primaryStage.setScene(gameScene);
        });
    }
    private void setExitButton(Stage primaryStage) {
        exitButton = new Button("Exit ");
        exitButton.getStyleClass().add("exit-button");
        exitButton.setOnAction(e -> {
            System.out.println("Exit button clicked");
            primaryStage.close();
        });
    }
    private void setSettingButton(Stage primaryStage) {
        settingButton = new Button("Setting");
        settingButton.getStyleClass().add("start-button");
        settingButton.setOnAction(e -> {
            System.out.println("Setting button clicked");
            Stage stage = new Stage();
            SettingPane settingPane = new SettingPane(stage);
            Scene settingScene = new Scene(settingPane, 400, 600);
            URL cssUrl = getClass().getResource(GameSettings.getUserCSS());
            if (cssUrl == null) {
                throw new RuntimeException("Cannot find CSS file: " + GameSettings.getUserCSS());
            }
            settingScene.getStylesheets().add(cssUrl.toExternalForm());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Settings");
            stage.setScene(settingScene);
            stage.showAndWait();
            stage.close();
        });
    }
    private void setLoadRecordButton(Stage primaryStage) {
        loadRecordButton = new Button("Load Record");
        loadRecordButton.getStyleClass().add("load-button");
        loadRecordButton.visibleProperty().bind(GameSettings.isGuestProperty().not());
//        loadRecordButton.setOnAction(e -> {
//            System.out.println("Load Record button clicked");
//            RecordPane recordPane = new RecordPane(primaryStage);
//            Scene recordScene = new Scene(recordPane, 400, 600);
//            recordScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(GameSettings.LOCAL.getCss())).toExternalForm());
//            primaryStage.setScene(recordScene);
//        });
    }
}