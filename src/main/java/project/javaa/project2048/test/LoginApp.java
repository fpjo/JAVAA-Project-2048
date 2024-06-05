package project.javaa.project2048.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Email label and text field
        Label emailLabel = new Label("Email Address:");
        TextField emailField = new TextField();
        grid.add(emailLabel, 0, 0);
        grid.add(emailField, 1, 0);

        // Password label and password field
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);

        // Forgot password link
        Hyperlink forgotPasswordLink = new Hyperlink("Forgot password?");
        grid.add(forgotPasswordLink, 2, 1);

        // Captcha label and text field
        Label captchaLabel = new Label("Characters in Image:");
        TextField captchaField = new TextField();
        grid.add(captchaLabel, 0, 2);
        grid.add(captchaField, 1, 2);

        // Captcha image
        Image captchaImage = new Image("file:/path/to/captcha.png"); // replace with the actual path to your captcha image
        ImageView captchaImageView = new ImageView(captchaImage);
        grid.add(captchaImageView, 2, 2);

        // Sign in button
        Button signInButton = new Button("Sign In");
        grid.add(signInButton, 1, 3);

        // Create account link
        Hyperlink createAccountLink = new Hyperlink("Create an account");
        grid.add(createAccountLink, 1, 4);

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
