package project.javaa.project2048.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StyledTextApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        // 创建 Text 节点
        Text title = new Text("2048");

        // 设置简单样式进行测试
        title.setStyle("-fx-fill: #CC00FF"); // 仅设置颜色
//        title.setFill(Color.web("#CC00FF"));
        // 创建布局并添加 Text 节点
        VBox layout = new VBox();
        layout.getChildren().add(title);

        // 创建场景
        Scene scene = new Scene(layout, 300, 200);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Styled Text App");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
