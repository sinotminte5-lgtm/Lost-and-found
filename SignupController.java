package com.lostid.controllers;

import com.lostid.model.DataService;
import com.lostid.model.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;

public class SignupController {
    @FXML private TextField fullNameField, departmentField, sectionField, otherField;
    @FXML private PasswordField passwordField; // Linked to new FXML field

    private SceneManager sceneManager;
    public void setSceneManager(SceneManager sm) { this.sceneManager = sm; }

    @FXML
    private void handleSignup() throws IOException {
        String name = fullNameField.getText().trim();
        String pass = passwordField.getText().trim();

        if (name.isEmpty() || pass.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Full Name and Password are required!").showAndWait();
            return;
        }

        DataService.saveUser(name, departmentField.getText(), sectionField.getText(), otherField.getText(), pass);

        new Alert(Alert.AlertType.INFORMATION, "Registration successful!").showAndWait();
        sceneManager.loadScene(SceneManager.LOGIN_VIEW, null);
    }

    @FXML private void handleBack() throws IOException { sceneManager.loadScene(SceneManager.LOGIN_VIEW, null); }
}