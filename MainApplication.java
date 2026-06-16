package com.lostid;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

import com.lostid.controllers.LoginController;
import com.lostid.model.SceneManager;

public class MainApplication extends Application {


    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Lost & Found ID Management System");

        // Initialize the SceneManager with the primary stage
        SceneManager sceneManager = new SceneManager(primaryStage);

        // Load the initial login scene
        // Note: The resource path is now relative to the root of the resources folder
        sceneManager.loadScene("/com/lostid/LoginView.fxml", null);

        primaryStage.show();
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}