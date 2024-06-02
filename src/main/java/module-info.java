module project.javaa.project2048 {
    requires javafx.controls;
    requires javafx.fxml;


    opens project.javaa.project2048 to javafx.fxml;
    exports project.javaa.project2048;
    exports project.javaa.project2048.test;
    opens project.javaa.project2048.test to javafx.fxml;
    exports project.javaa.project2048.view;
    opens project.javaa.project2048.view to javafx.fxml;
    exports project.javaa.project2048.module;
    opens project.javaa.project2048.module to javafx.fxml;
}