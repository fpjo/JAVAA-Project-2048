package project.javaa.project2048;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public class StartPane extends VBox {
    private Button startButton;
    private Button exitButton;
    private Button settingButton;
    private Button loginButton;
    private Button loadButton;
    private Label versionLabel;
    private Label userStatusLabel;
    private HBox bottomBox;
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
        setLoadButton(primaryStage);
        setLoginButton(primaryStage);

        versionLabel = new Label("Version: " + Game2048.VERSION);
        userStatusLabel = new Label((GameSettings.isGuest()? "Guest" : "User:" + GameSettings.getPlayerName()));
        versionLabel.getStyleClass().add("version-label");
        userStatusLabel.getStyleClass().add("user-status-label");
        bottomBox = new HBox(versionLabel, new Region(), userStatusLabel);
        HBox.setHgrow(bottomBox.getChildren().get(1), Priority.ALWAYS);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(10, 10, 10, 10));

        this.getChildren().addAll(title, startButton, loginButton, loadButton, settingButton, exitButton, bottomBox);
    }

    private void setLoginButton(Stage primaryStage) {
        loginButton = new Button("Login");
        loginButton.getStyleClass().add("login-button");
//        loginButton.setOnAction(e -> {
//            System.out.println("Login button clicked");
//            Stage stage = new Stage();
//            LoginPane loginPane = new LoginPane(stage);
//            Scene loginScene = new Scene(loginPane, 400, 600);
//            URL cssUrl = getClass().getResource(GameSettings.getUserCSS());
//            if (cssUrl == null) {
//                throw new RuntimeException("Cannot find CSS file: " + GameSettings.getUserCSS());
//            }
//            loginScene.getStylesheets().add(cssUrl.toExternalForm());
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.setTitle("Login");
//            stage.setScene(loginScene);
//            stage.showAndWait();
//            stage.close();
//        });
    }

    private void setStartButton(Stage primaryStage) {
        startButton = new Button("Start");
        startButton.getStyleClass().add("start-button");
        startButton.setOnAction(e -> {
            System.out.println("Start button clicked");
            GamePane gamePane = new GamePane(primaryStage);
            Scene gameScene = new Scene(gamePane, 400, 600);
            gameScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(GameSettings.getUserCSS())).toExternalForm());
            primaryStage.setScene(gameScene);
        });
    }
    private void setExitButton(Stage primaryStage) {
        exitButton = new Button("Exit");
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
    private void setLoadButton(Stage primaryStage) {
        loadButton = new Button("Load");
        loadButton.getStyleClass().add("load-button");
        loadButton.disableProperty().bind(GameSettings.isGuestProperty().not());
//        loadRecordButton.setOnAction(e -> {
//            System.out.println("Load Record button clicked");
//            RecordPane recordPane = new RecordPane(primaryStage);
//            Scene recordScene = new Scene(recordPane, 400, 600);
//            recordScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(GameSettings.LOCAL.getCss())).toExternalForm());
//            primaryStage.setScene(recordScene);
//        });
    }
}