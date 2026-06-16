package com.lostid.controllers;

import com.lostid.model.DataService;
import com.lostid.model.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.List;

public class RecordsListController {
    @FXML private Label pageTitle;
    @FXML private FlowPane container;
    private SceneManager sceneManager;

    public void setSceneManager(SceneManager sm) { this.sceneManager = sm; }

    public void loadData(String type) throws IOException {
        pageTitle.setText(type + " Records");
        container.getChildren().clear();

        List<String[]> records = type.equals("Users") ? DataService.getAllUsers() :
                type.equals("Lost") ? DataService.getAllLost() : DataService.getAllFound();

        for (String[] data : records) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lostid/DataCard.fxml"));
            VBox card = loader.load();

            Label title = (Label) card.lookup("#titleLabel");
            Label d1 = (Label) card.lookup("#detail1Label");

            if (type.equals("Users")) {
                title.setText(data[0]); // Name
                d1.setText("Dept: " + (data.length > 1 ? data[1] : "N/A"));
            } else {
                title.setText("ID: " + (data.length > 1 ? data[1] : "N/A"));
                d1.setText("Reporter: " + data[0]);
            }
            container.getChildren().add(card);
        }
    }

    @FXML private void handleBack() throws IOException {
        sceneManager.loadScene(SceneManager.ADMIN_DASHBOARD_VIEW, "Admin");
    }
}