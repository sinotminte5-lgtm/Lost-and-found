package com.lostid.controllers;

import com.lostid.model.DataService;
import com.lostid.model.SceneManager;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AdminDashboardController {
    @FXML private Label totalUsersLabel, lostIdLabel, foundIdLabel, matchLabel, currentListTitle;
    @FXML private VBox listDataContainer, totalUsersCard, totalLostCard, totalFoundCard, matchingIDsCard;
    @FXML private Button btnDashboard, btnUsers, btnLost, btnFound;
    @FXML private TextField searchField;

    private SceneManager sceneManager;
    private String currentCategory = "Users";

    public void setSceneManager(SceneManager sm) { this.sceneManager = sm; }

    @FXML
    public void initialize() {
        refreshStats();
        loadData();
        setActiveButton(btnUsers);

        // Real-time search listener
        searchField.textProperty().addListener((obs, oldVal, newVal) -> loadData());

        // Card Click Actions
        totalUsersCard.setOnMouseClicked(e -> showUsersList());
        totalLostCard.setOnMouseClicked(e -> showLostList());
        totalFoundCard.setOnMouseClicked(e -> showFoundList());
        matchingIDsCard.setOnMouseClicked(e -> showMatches());
    }

    private void refreshStats() {
        totalUsersLabel.setText(String.valueOf(DataService.getTotalUsers()));
        lostIdLabel.setText(String.valueOf(DataService.getActiveLost()));
        foundIdLabel.setText(String.valueOf(DataService.getPendingFound()));
        matchLabel.setText(String.valueOf(DataService.getDetailedMatches().size()));
    }

    @FXML public void showDashboard() { currentCategory = "Found"; loadData(); setActiveButton(btnDashboard); }
    @FXML public void showUsersList() { currentCategory = "Users"; loadData(); setActiveButton(btnUsers); }
    @FXML public void showLostList() { currentCategory = "Lost"; loadData(); setActiveButton(btnLost); }
    @FXML public void showFoundList() { currentCategory = "Found"; loadData(); setActiveButton(btnFound); }
    @FXML public void showMatches() { currentCategory = "Matches"; loadData(); }

    private void loadData() {
        currentListTitle.setText(currentCategory + " List");
        listDataContainer.getChildren().clear();
        String filter = searchField.getText().toLowerCase().trim();

        try {
            if (currentCategory.equals("Matches")) {
                for (String match : DataService.getDetailedMatches()) {
                    if (match.toLowerCase().contains(filter)) {
                        listDataContainer.getChildren().add(createStyledRow("🤝 " + match, null, false));
                    }
                }
            } else {
                List<String[]> data = currentCategory.equals("Users") ? DataService.getAllUsers() :
                        currentCategory.equals("Lost") ? DataService.getAllLost() : DataService.getAllFound();

                for (String[] row : data) {
                    // Combine all row text for a thorough search
                    String combinedRowText = String.join(" ", row).toLowerCase();

                    if (filter.isEmpty() || combinedRowText.contains(filter)) {
                        String display;
                        if (currentCategory.equals("Users")) {
                            // Name | ID | Email
                            String email = (row.length > 3) ? row[3] : "No Email";
                            display = String.format("👤 %-15s | ID: %-8s | Email: %s", row[0], row[1], email);
                        } else {
                            display = "ID: " + row[1] + " | Reported by: " + row[0];
                        }
                        listDataContainer.getChildren().add(createStyledRow(display, row[1], true));
                    }
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private HBox createStyledRow(String text, String id, boolean canDelete) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPrefHeight(50);
        row.setStyle("-fx-background-color: #FBFBFB; -fx-padding: 0 15; -fx-background-radius: 8; -fx-border-color: #EDEDED;");

        Label lbl = new Label(text);
        lbl.setStyle("-fx-text-fill: #343A40; -fx-font-family: 'Consolas', 'Monospaced'; -fx-font-size: 13px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        row.getChildren().addAll(lbl, spacer);

        if (canDelete) {
            Button del = new Button("Delete");
            del.setStyle("-fx-background-color: #EF5350; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand;");
            del.setOnAction(e -> confirmAndDelete(id));
            row.getChildren().add(del);
        }
        return row;
    }

    private void confirmAndDelete(String id) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete record " + id + "?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                DataService.deleteRecord(currentCategory, id);
                refreshStats();
                loadData();
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    private void setActiveButton(Button active) {
        btnDashboard.setStyle("-fx-background-color: transparent; -fx-text-fill: #6C757D;");
        btnUsers.setStyle("-fx-background-color: transparent; -fx-text-fill: #6C757D;");
        btnLost.setStyle("-fx-background-color: transparent; -fx-text-fill: #6C757D;");
        btnFound.setStyle("-fx-background-color: transparent; -fx-text-fill: #6C757D;");
        active.setStyle("-fx-background-color: #E7F0FD; -fx-text-fill: #4285F4; -fx-font-weight: bold; -fx-background-radius: 8;");
    }

    @FXML public void handleLogout() throws IOException { sceneManager.loadScene(SceneManager.LOGIN_VIEW, null); }

    @FXML
    public void handleBack() {
        // 1. Clear the search field
        searchField.clear();

        // 2. Reset the category to the default (Dashboard/Found Summary)
        currentCategory = "Found";

        // 3. Refresh the UI
        refreshStats();
        loadData();

        // 4. Reset the sidebar button styling to "Dashboard"
        setActiveButton(btnDashboard);
    }
}