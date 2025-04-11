package com.chauffeursync.controllers;

import com.chauffeursync.enums.ScreenType;
import com.chauffeursync.manager.ScreenManager;
import com.chauffeursync.models.Shift;
import com.chauffeursync.models.User;
import com.chauffeursync.screens.ShiftDetailScreen;
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
        List<Shift> shifts = user.getShifts();

        for (Shift shift : shifts) {
            VBox shiftCard = new VBox();
            shiftCard.getStyleClass().add("shift-card");
            shiftCard.setSpacing(6);

            Label timeLabel = new Label(String.format("%s - %s", shift.getStartTime(), shift.getEndTime()));
            Label vehicleLabel = new Label(String.format("Voertuig: %s", shift.getVehicleId()));
            Label userLabel = new Label("Chauffeur: Deze gebruiker is verwijderd.");

            User shiftOwner = User.getUserById(shift.getUserId());
            if (shiftOwner != null) {
                userLabel = new Label("Chauffeur: " + shiftOwner.getName());
            }
            shiftCard.getChildren().addAll(timeLabel, vehicleLabel, userLabel);

            shiftCard.setOnMouseClicked(e -> {
                System.out.println("Shift " + shift.getId() + " geselecteerd");
                manager.switchTo(new ShiftDetailScreen(manager, shift));
            });

            shiftBox.getChildren().add(shiftCard);
        }

        if (shifts.isEmpty()) {
            Label emptyLabel = new Label("Je hebt momenteel geen ingeplande shifts.");
            emptyLabel.getStyleClass().add("empty-message");
            emptyLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666; -fx-padding: 20;");
            shiftBox.getChildren().add(emptyLabel);
        }
    }

    @FXML
    private void handleManageUsers() {
        manager.switchTo(ScreenType.MANAGE_USERS);
    }

    @FXML
    private void handleManageVehicles() {
        manager.switchTo(ScreenType.VEHICLE_LIST);
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
