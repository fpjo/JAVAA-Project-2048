package project.javaa.project2048.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.javaa.project2048.SettingPane;
import project.javaa.project2048.module.GameSettings;

import java.util.Objects;

public class StartPane extends VBox {
    private Button startButton;
    private Button exitButton;
    private Button settingButton;
    private Button loadRecordButton;
    private Button aboutButton;
    public StartPane(Stage primaryStage) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.getStyleClass().add("game-root");

        Text title = new Text("Game 2048");
        title.getStyleClass().add("game-title");

        settingButton = new Button("Setting");
        loadRecordButton = new Button("Load Record");
        aboutButton = new Button("About");

        setStartButton(primaryStage);
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
        settingButton.getStyleClass().add("setting-button");
        settingButton.setOnAction(e -> {
            System.out.println("Setting button clicked");
            SettingPane settingPane = new SettingPane(primaryStage);
            Scene settingScene = new Scene(settingPane, 400, 600);
            settingScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(GameSettings.getUserCSS())).toExternalForm());
            settingPane.display();
            primaryStage.setScene(settingScene);
        });
    }
    private void serLoadRecordButton(Stage primaryStage) {
        loadRecordButton = new Button("Load Record");
        loadRecordButton.getStyleClass().add("load-record-button");
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

