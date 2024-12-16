package org.example.frontend;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class RegisterPage extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Register New User");

        // Layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);

        // Username field
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        GridPane.setConstraints(usernameLabel, 0, 0);
        GridPane.setConstraints(usernameField, 1, 0);

        // Password field
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        GridPane.setConstraints(passwordLabel, 0, 1);
        GridPane.setConstraints(passwordField, 1, 1);

        // Email field
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter email");
        GridPane.setConstraints(emailLabel, 0, 2);
        GridPane.setConstraints(emailField, 1, 2);

        // Register button
        Button registerButton = new Button("Register");
        GridPane.setConstraints(registerButton, 1, 3);
        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String email = emailField.getText();

            if (!username.isEmpty() && !password.isEmpty() && !email.isEmpty()) {
                boolean success = AuthenticationService.register(username, password, email);
                if (success) {
                    showAlert("Registration successful!", Alert.AlertType.INFORMATION);
                    stage.close();
                    new LoginPage().start(new Stage());
                } else {
                    showAlert("Registration failed. Try again.", Alert.AlertType.ERROR);
                }
            } else {
                showAlert("All fields are required!", Alert.AlertType.WARNING);
            }
        });

        grid.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, emailLabel, emailField, registerButton);

        // Scene setup
        Scene scene = new Scene(grid, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Registration");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
