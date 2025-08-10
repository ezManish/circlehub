package com.circlehub.controller;

import com.circlehub.service.AuthService;
import com.circlehub.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Optional;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private final AuthService authService = new AuthService();

    @FXML
    public void onLogin() {
        String email = emailField.getText().trim();
        String pass = passwordField.getText();
        try {
            Optional<User> u = authService.login(email, pass);
            if (u.isPresent()) {
                messageLabel.setText("Login successful — open dashboard");
            } else {
                messageLabel.setText("Invalid credentials");
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Login failed: " + e.getMessage());
        }
    }

    @FXML public void onOpenRegister() {
        // TODO: load register.fxml
    }
}
