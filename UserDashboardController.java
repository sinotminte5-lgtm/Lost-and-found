package com.lostid.controllers;

import com.lostid.model.SceneManager;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.io.IOException;

public class UserDashboardController {

    @FXML private Label welcomeLabel;

    @FXML private VBox lostIdCard;   // <-- New VBox for the Lost ID Card
    @FXML private VBox foundIdCard;  // <-- New VBox for the Found ID Card

    private SceneManager sceneManager;
    private String username;

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public void setUsername(String username) {
        this.username = username;
        if (welcomeLabel != null) {
            welcomeLabel.setText("Welcome, " + username);
        }
    }

    @FXML
    public void initialize() {
        // Apply hover effects to the new card VBoxes
        setupCardHover(lostIdCard);
        setupCardHover(foundIdCard);
    }

    /**
     * Applies subtle scale and shadow transitions on mouse hover/exit.
     */
    private void setupCardHover(VBox card) {
        // Hover Enter Effect
        card.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), card);
            st.setToX(1.03); // Slightly larger scale than admin dashboard (1.05)
            st.setToY(1.03);
            st.play();
            // Enhance the shadow for a lifted effect
            card.setEffect(new DropShadow(15, Color.gray(0.5)));
        });

        // Hover Exit Effect
        card.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), card);
            st.setToX(1);
            st.setToY(1);
            st.play();
            // Return to the initial subtle shadow
            card.setEffect(new DropShadow(5, Color.gray(0.3)));
        });
    }

    @FXML
    private void handleLostIDForm() throws IOException {
        sceneManager.loadScene(SceneManager.LOST_ID_FORM_VIEW, username);
    }

    @FXML
    private void handleFoundIDForm() throws IOException {
        sceneManager.loadScene(SceneManager.FOUND_ID_FORM_VIEW, username);
    }

    @FXML
    private void handleBack() throws IOException {
        sceneManager.loadScene(SceneManager.LOGIN_VIEW, null);
    }
}