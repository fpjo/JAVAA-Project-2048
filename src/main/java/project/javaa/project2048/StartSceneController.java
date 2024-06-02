package project.javaa.project2048;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class StartSceneController {

    @FXML
    private Button Btn_Start;

    @FXML
    private Label Label_Title;

    @FXML
    void StartBtnReleased(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("WELCOME TO JAVA PROJECT 2048");
        alert.showAndWait();
    }

}
