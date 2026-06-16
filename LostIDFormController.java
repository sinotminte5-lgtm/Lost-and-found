package com.lostid.controllers;

import com.lostid.model.DataService;
import com.lostid.model.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.IOException;

public class LostIDFormController {

    @FXML private TextField fullNameField;
    @FXML private TextField studentIdField;
    @FXML private TextField idDetailsField; // Matches FXML fx:id
    @FXML private TextArea locationDescArea; // Matches FXML fx:id (Note it is TextArea)

    private SceneManager sceneManager;
    private String username;

    public void setSceneManager(SceneManager sm) { this.sceneManager = sm; }

    public void setUsername(String username) {
        this.username = username;
        if (fullNameField != null) {
            fullNameField.setText(username);
        }
    }

    @FXML
    private void handleSubmit() throws IOException {
        // Validation check to prevent the crash you saw earlier
        if (studentIdField == null || idDetailsField == null || locationDescArea == null) {
            System.out.println("Critical Error: FXML fields are not linked to Controller variables.");
            return;
        }

        String studentId = studentIdField.getText().trim();
        String type = idDetailsField.getText().trim();
        String loc = locationDescArea.getText().trim();

        if (studentId.isEmpty() || type.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter the Student ID and ID Type.").showAndWait();
            return;
        }

        try {
            // Save the data to lost_reports.txt
            DataService.saveLostReport(username, studentId, type, loc);

            new Alert(Alert.AlertType.INFORMATION, "Lost report submitted!").showAndWait();
            sceneManager.loadScene(SceneManager.USER_DASHBOARD_VIEW, username);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void handleBack() throws IOException {
        sceneManager.loadScene(SceneManager.USER_DASHBOARD_VIEW, username);
    }
}