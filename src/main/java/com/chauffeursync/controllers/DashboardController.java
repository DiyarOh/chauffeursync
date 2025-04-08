package com.chauffeursync.controllers;

import com.chauffeursync.manager.ScreenManager;
import com.chauffeursync.models.Shift;
import com.chauffeursync.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class DashboardController {

    private final ScreenManager manager;
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Button logoutButton;
    @FXML
    private Button manageUsersButton;
    @FXML
    private Button manageVehiclesButton;
    @FXML
    private Button reportButton;
    @FXML
    private VBox notificationsBox;
    @FXML
    private VBox shiftBox;

    public DashboardController(ScreenManager manager) {
        this.manager = manager;
    }

    @FXML
    public void initialize() {
        User user = manager.getUserController().getCurrentUser();
        if (user != null) {
            nameLabel.setText(user.getName());
            emailLabel.setText(user.getEmail());
            loadShifts(user);
        }
    }
    private void loadShifts(User user) {
        shiftBox.getChildren().clear();

        // Voor nu gebruiken we dummy shifts – later vervang ik dit met: user.getShifts()
        for (int i = 1; i <= 10; i++) {
            VBox shiftCard = new VBox();
            shiftCard.getStyleClass().add("shift-card");
            shiftCard.setSpacing(6);

            Label timeLabel = new Label("08:00 - 16:00");
            Label vehicleLabel = new Label("Voertuig: Mercedes Sprinter");
            Label userLabel = new Label("Chauffeur: " + user.getName());

            shiftCard.getChildren().addAll(timeLabel, vehicleLabel, userLabel);

            int finalI = i;
            shiftCard.setOnMouseClicked(e -> {
                System.out.println("Shift " + finalI + " geselecteerd");
                // Hier later een detailview openen
            });

            shiftBox.getChildren().add(shiftCard);
        }
    }

    @FXML
    private void handleManageUsers() {
        // TODO: Navigatie naar gebruikersbeheer
    }

    @FXML
    private void handleManageVehicles() {
        // TODO: Navigatie naar voertuigenbeheer
    }

    @FXML
    private void handleReports() {
        // TODO: Navigatie naar rapportagescherm
    }

    @FXML
    public void handleLogout() {
        manager.getUserController().logout();
        manager.switchTo(com.chauffeursync.enums.ScreenType.START);
    }
}