package com.circlehub.controller;

import com.circlehub.model.User;
import com.circlehub.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField displayNameField;
    @FXML private Label messageLabel;

    private final AuthService authService = new AuthService();

    @FXML
    public void onRegister() {
        try {
            User u = new User();
            u.setUsername(usernameField.getText().trim());
            u.setEmail(emailField.getText().trim());
            u.setDisplayName(displayNameField.getText().trim());
            boolean ok = authService.register(u, passwordField.getText()).isPresent();
            messageLabel.setText(ok ? "Account created" : "Registration failed");
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error: " + e.getMessage());
        }
    }
}
