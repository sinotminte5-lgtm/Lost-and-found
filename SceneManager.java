package com.lostid.model;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import com.lostid.controllers.*;

public class SceneManager {
    public static final String LOGIN_VIEW = "/com/lostid/LoginView.fxml";
    public static final String SIGNUP_VIEW = "/com/lostid/SignupView.fxml";
    public static final String ADMIN_DASHBOARD_VIEW = "/com/lostid/AdminDashboardView.fxml";
    public static final String USER_DASHBOARD_VIEW = "/com/lostid/UserDashboardView.fxml";
    public static final String LOST_ID_FORM_VIEW = "/com/lostid/LostIDFormView.fxml";
    public static final String FOUND_ID_FORM_VIEW = "/com/lostid/FoundIDFormView.fxml";
    public static final String RECORDS_LIST_VIEW = "/com/lostid/RecordsListView.fxml";

    private final Stage stage;
    public SceneManager(Stage stage) { this.stage = stage; }

    public void loadScene(String path, Object data) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        Parent root = loader.load();
        injectDependencies(loader.getController(), data instanceof String ? (String) data : null);

        if (stage.getScene() == null) {
            stage.setScene(new Scene(root, 900, 600));
        } else {
            stage.getScene().setRoot(root);
        }
        stage.show();
    }

    public void loadListView(String type) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(RECORDS_LIST_VIEW));
        Parent root = loader.load();

        RecordsListController controller = loader.getController();
        if (controller != null) {
            controller.setSceneManager(this);
            controller.loadData(type);
        }

        stage.getScene().setRoot(root);
    }

    private void injectDependencies(Object controller, String user) {
        if (controller instanceof LoginController) ((LoginController) controller).setSceneManager(this);
        else if (controller instanceof SignupController) ((SignupController) controller).setSceneManager(this);
        else if (controller instanceof AdminDashboardController) ((AdminDashboardController) controller).setSceneManager(this);
        else if (controller instanceof UserDashboardController) {
            ((UserDashboardController) controller).setSceneManager(this);
            ((UserDashboardController) controller).setUsername(user);
        }
        else if (controller instanceof LostIDFormController) {
            ((LostIDFormController) controller).setSceneManager(this);
            ((LostIDFormController) controller).setUsername(user);
        }
        else if (controller instanceof FoundIDFormController) {
            ((FoundIDFormController) controller).setSceneManager(this);
            ((FoundIDFormController) controller).setUsername(user);
        }
    }
}