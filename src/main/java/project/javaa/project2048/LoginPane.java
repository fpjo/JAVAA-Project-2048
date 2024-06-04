package project.javaa.project2048;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginPane extends VBox {
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button registerButton;
//    private Button resetPwdButton;

    public LoginPane(Stage stage) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.getStyleClass().add("game-root");
        Text title = new Text("Login");
        title.getStyleClass().add("game-title");

        setUsernameTextField(stage);
        setPasswordTextField(stage);
//        setResetPwdButton(stage);
        setLoginButton(stage);
        setRegisterButton(stage);

        this.getChildren().addAll(title, usernameField, passwordField, loginButton, registerButton);
    }

    private void setLoginButton(Stage stage) {
        loginButton = new Button("Login");
        loginButton.getStyleClass().add("login-button");
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
        });
    }

    private void setRegisterButton(Stage stage) {
        registerButton = new Button("Register");
        registerButton.getStyleClass().add("register-button");
        registerButton.setOnAction(e -> {

        });
    }

//    private void setResetPwdButton(Stage stage) {}

    private void setUsernameTextField(Stage stage) {
        usernameField = new TextField("UserName");
        usernameField.getStyleClass().add("username-field");
        usernameField.setOnKeyPressed(e -> {

        });
    }

    private void setPasswordTextField(Stage stage) {
        passwordField = new PasswordField();
        passwordField.getStyleClass().add("password-field");
        passwordField.setOnKeyPressed(e -> {

        });
    }
}
