package project.javaa.project2048.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StackPaneExample extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 创建一个 StackPane
        StackPane stackPane = new StackPane();

        // 创建第一个矩形
        Rectangle rect1 = new Rectangle(200, 200, Color.BLUE);

        // 创建第二个矩形
        Rectangle rect2 = new Rectangle(150, 150, Color.GREEN);

        // 创建一个文本
        Text text = new Text("Hello, StackPane!");
        text.setFill(Color.WHITE);

        // 将矩形和文本添加到 StackPane
        stackPane.getChildren().addAll(rect1, rect2, text);

        // 创建一个 Scene
        Scene scene = new Scene(stackPane, 300, 300);

        // 设置舞台
        primaryStage.setTitle("StackPane Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
