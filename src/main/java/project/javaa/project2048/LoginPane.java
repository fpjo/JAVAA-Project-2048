package project.javaa.project2048;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
        Label title = new Label("Sign in to Continue");
        title.getStyleClass().add("game-title");
        Label usernameLabel = new Label("Username");
        usernameLabel.getStyleClass().add("game-label");
        Label passwordLabel = new Label("Password");
        passwordLabel.getStyleClass().add("game-label");
        setUsernameTextField(stage);
        setPasswordTextField(stage);
        setLoginButton(stage);
        setRegisterButton(stage);
        loginButton.setAlignment(Pos.CENTER);

        VBox VContainer = new VBox();
        VContainer.setAlignment(Pos.CENTER_LEFT);
        VContainer.setSpacing(10);
        VContainer.setStyle("-fx-background-color: rgba(211, 211, 211, 0.5);");
        VContainer.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, loginButton, registerButton);
//        setResetPwdButton(stage);

        this.getChildren().addAll(title, VContainer);
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
