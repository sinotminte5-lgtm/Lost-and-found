package com.lostid.controllers;

import com.lostid.model.DataService;
import com.lostid.model.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.IOException;

public class FoundIDFormController {

    // Removed studentNameField as we now use the logged-in 'username'
    @FXML private TextField studentIdField;
    @FXML private TextArea descArea;

    private SceneManager sceneManager;
    private String username; // This represents the person currently logged in

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @FXML
    private void handleSubmit() throws IOException {
        String foundId = studentIdField.getText().trim();
        String description = descArea.getText().trim();

        // 1. Validation
        if (foundId.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter the Student ID number found on the card.", ButtonType.OK).showAndWait();
            return;
        }

        if (description.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please specify where you found the ID.", ButtonType.OK).showAndWait();
            return;
        }

        try {
            // 2. THE FIX: 'username' is the person who found it. 'foundId' is the ID number.
            // DataService.saveFoundReport(FinderName, IDNumber, Location/Desc)
            DataService.saveFoundReport(username, foundId, description);

            new Alert(Alert.AlertType.INFORMATION, "Report submitted! Thank you for finding ID: " + foundId, ButtonType.OK).showAndWait();

            // 3. Return to the User Dashboard
            sceneManager.loadScene(SceneManager.USER_DASHBOARD_VIEW, username);

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error saving report: " + e.getMessage()).showAndWait();
        }
    }

    // handleUploadImage removed as per your request

    @FXML
    private void handleBack() throws IOException {
        sceneManager.loadScene(SceneManager.USER_DASHBOARD_VIEW, username);
    }
}