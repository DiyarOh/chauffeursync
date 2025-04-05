package com.chauffeursync.screens;

import com.chauffeursync.controllers.UserController;
import com.chauffeursync.enums.ScreenType;
import com.chauffeursync.interfaces.Screen;
import com.chauffeursync.manager.ScreenManager;
import com.chauffeursync.models.User;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DashboardScreen implements Screen {

    private final ScreenManager manager;
    private final UserController userController;

    public DashboardScreen(ScreenManager manager) {
        this.manager = manager;
        this.userController = manager.getUserController();
    }

    @Override
    public void show() {
        User user = userController.getCurrentUser();

        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 30; -fx-alignment: center;");

        Label welcomeLabel = new Label("Welkom, " + user.getName());

        Button logoutButton = new Button("Uitloggen");
        logoutButton.setOnAction(e -> {
            userController.logout();
            manager.switchTo(ScreenType.START);
        });

        root.getChildren().addAll(welcomeLabel, logoutButton);

        manager.getPrimaryStage().setTitle("Dashboard - ChauffeurSync");
        manager.getPrimaryStage().setScene(new Scene(root, 400, 200));
        manager.getPrimaryStage().show();
    }
}

