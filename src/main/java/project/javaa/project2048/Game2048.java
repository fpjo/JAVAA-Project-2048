package project.javaa.project2048;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Objects;

public class Game2048 extends Application {
    public static final String VERSION = "1.0.1";
    private StartPane startPane;
    private static Game2048 applicationInstance;
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("2048 Game");
        startPane = new StartPane(primaryStage);
        Scene startScene = new Scene(startPane, 400, 500);
        startScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("default.css")).toExternalForm());
        setGameBounds(primaryStage, startScene);
        primaryStage.setScene(startScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    private void setGameBounds(Stage primaryStage, Scene scene) {
        var margin = GameSettings.MARGIN;
        var gameBounds = startPane.getLayoutBounds();
        var visualBounds = Screen.getPrimary().getVisualBounds();
        double factor = Math.min(visualBounds.getWidth() / (gameBounds.getWidth() + margin),
                visualBounds.getHeight() / (gameBounds.getHeight() + margin));
        primaryStage.setTitle("Project2048");
        primaryStage.setMinWidth(gameBounds.getWidth() / 2d);
        primaryStage.setMinHeight(gameBounds.getHeight() / 2d);
        primaryStage.setWidth(((gameBounds.getWidth() + margin) * factor) / 1.5d);
        primaryStage.setHeight(((gameBounds.getHeight() + margin) * factor) / 1.5d);
    }
}
