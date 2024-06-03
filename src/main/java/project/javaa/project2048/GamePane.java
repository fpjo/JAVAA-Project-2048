package project.javaa.project2048;

import javafx.geometry.Bounds;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GamePane extends StackPane {
    private final GameManager gameManager;
    private final Bounds gameBounds;
    public GamePane(Stage primaryStage) {


        // 初始化游戏界面 (可以在此添加更多的游戏初始化逻辑)
        initGameBoard();
    }

    private void initGameBoard() {
        // 示例：创建一个4x4的游戏板
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                Tile tile = new Tile();
                this.add(tile, col, row);
            }
        }
    }
}
