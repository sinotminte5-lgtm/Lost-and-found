package com.lostid.controllers;

import com.lostid.model.DataService;
import com.lostid.model.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.io.IOException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    private SceneManager sceneManager;

    public void setSceneManager(SceneManager sceneManager) { this.sceneManager = sceneManager; }

    @FXML
    private void handleLogin() throws IOException {
        String user = usernameField.getText().trim();
        String pass = passwordField.getText().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Enter both username and password.").showAndWait();
            return;
        }

        String authResult = DataService.authenticate(user, pass);
        System.out.println("Login Result: " + authResult); // CHECK YOUR CONSOLE FOR THIS

        if (authResult.equals("ADMIN")) {
            sceneManager.loadScene(SceneManager.ADMIN_DASHBOARD_VIEW, "Admin");
        } else if (authResult.equals("USER")) {
            sceneManager.loadScene(SceneManager.USER_DASHBOARD_VIEW, user);
        } else {
            new Alert(Alert.AlertType.ERROR, "Invalid Credentials!").showAndWait();
        }
    }

    @FXML private void handleSignup() throws IOException { sceneManager.loadScene(SceneManager.SIGNUP_VIEW, null); }
}