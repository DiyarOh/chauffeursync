package com.chauffeursync.screens;

import com.chauffeursync.controllers.DashboardController;
import com.chauffeursync.controllers.UserController;
import com.chauffeursync.interfaces.Screen;
import com.chauffeursync.manager.ScreenManager;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public abstract class DashboardScreen extends AbstractScreen {

    protected final UserController userController;

    public DashboardScreen(ScreenManager manager) {
        super(manager);
        this.userController = manager.getUserController();
    }

    protected abstract String getFxmlPath();
    protected abstract String getCssFilename();

    @Override
    public void show() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(getFxmlPath()));
            loader.setController(new DashboardController(manager));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            String css = getCssFilename();
            if (css != null) {
                URL cssUrl = getClass().getResource(css);
                if (cssUrl != null) {
                    scene.getStylesheets().add(cssUrl.toExternalForm());
                } else {
                    System.err.println("CSS niet gevonden: " + css);
                }
            }

            manager.getPrimaryStage().setTitle(getTitle());
            Stage stage = getStage(scene);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Stage getStage(Scene scene) {
        Stage stage = manager.getPrimaryStage();
        stage.setTitle(getTitle());
        stage.setScene(scene);
        stage.setWidth(1000);
        stage.setHeight(700);
        stage.setResizable(false);

        Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        return stage;
    }

    protected String getTitle() {
        return "Dashboard - ChauffeurSync";
    }
}

