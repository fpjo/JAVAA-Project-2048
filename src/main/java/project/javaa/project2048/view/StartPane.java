package project.javaa.project2048.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.javaa.project2048.module.GameSettings;

import java.util.Objects;

public class StartPane extends VBox {
    public StartPane(Stage primaryStage) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.getStyleClass().add("game-root");

        Text title = new Text("Game 2048");
        title.getStyleClass().add("game-title");

        Button startButton = new Button("Start");
        startButton.getStyleClass().add("start-button");
        startButton.setOnAction(e -> {
            System.out.println("Start button clicked");
            GamePane gamePane = new GamePane(primaryStage);
            Scene gameScene = new Scene(gamePane, 400, 600);
            gameScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(GameSettings.LOCAL.getCss())).toExternalForm());
            primaryStage.setScene(gameScene);
        });

        Button exitButton = new Button("Exit ");
        exitButton.getStyleClass().add("exit-button");
        exitButton.setOnAction(e -> {
            System.out.println("Exit button clicked");
            primaryStage.close();
        });

        setStartListener(primaryStage, startButton);
        this.getChildren().addAll(title, startButton, exitButton);
    }
    private void setStartListener(Stage primaryStage, Button startButton) {

    }
}

