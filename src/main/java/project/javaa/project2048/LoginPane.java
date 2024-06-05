package project.javaa.project2048;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginPane extends VBox {
    private TextField usernameField;
    private PasswordField passwordField;
    private Button signInButton;
    private Button registerButton;
    private GridPane grid;
    private static final UserManager userManager=new UserManager();

    public LoginPane(Stage stage) {
        this.setAlignment(Pos.CENTER);
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        grid.getStyleClass().add("grid-pane");

        Label title = new Label("Sign in to Continue");
        title.getStyleClass().add("title");
        grid.add(title, 1, 0);

        Label userNameLabel = new Label("Username:");
        usernameField = new TextField();
        grid.add(userNameLabel, 0, 1);
        grid.add(usernameField, 1, 1);

        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        grid.add(passwordLabel, 0, 2);
        grid.add(passwordField, 1, 2);
        setSignInButton(stage);
        setRegisterButton(stage);

        this.getChildren().addAll(grid);
    }

    private void setSignInButton(Stage stage) {
        signInButton = new Button("Sign In");
        grid.add(signInButton, 1, 3);
        signInButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if(userManager.login(username, password)){
                GameSettings.setPlayerName(username);
                GameSettings.setIsGuest(false);
                stage.close();
            }else{
                usernameField.clear();
                passwordField.clear();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Login Error");
                alert.setContentText("Username or password is incorrect");
                ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                alert.getButtonTypes().setAll(buttonTypeOk);
                alert.showAndWait();
            }
        });
    }

    private void setRegisterButton(Stage stage) {
        registerButton = new Button("Register");
        grid.add(registerButton, 1, 4);
        registerButton.setOnAction(e -> {
            if(userManager.register(usernameField.getText(), passwordField.getText())) {
                usernameField.clear();
                passwordField.clear();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registration Successful");
                alert.setHeaderText(null);
                alert.setContentText("Registration successful. Please log in again.");
                alert.showAndWait();
            }else{
                usernameField.clear();
                passwordField.clear();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Registration Error");
                alert.setContentText("Username already exists");
                ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                alert.getButtonTypes().setAll(buttonTypeOk);
                alert.showAndWait();
            }
        });
    }
}
