module project.javaa.project2048 {
    requires javafx.controls;
    requires javafx.fxml;


    opens project.javaa.project2048 to javafx.fxml;
    exports project.javaa.project2048;
}