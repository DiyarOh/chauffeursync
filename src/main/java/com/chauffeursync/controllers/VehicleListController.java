package com.chauffeursync.controllers;

import com.chauffeursync.enums.ScreenType;
import com.chauffeursync.manager.ScreenManager;
import com.chauffeursync.models.Vehicle;
import com.chauffeursync.screens.VehicleDetailScreen;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class VehicleListController {

    @FXML private VBox vehicleBox;
    @FXML private Label nameLabel;
    @FXML private Label emailLabel;

    private final ScreenManager manager;

    public VehicleListController(ScreenManager manager) {
        this.manager = manager;
    }

    @FXML
    public void initialize() {
        vehicleBox.getChildren().clear();

        List<Vehicle> vehicles = Vehicle.getAllVehicles();

        for (Vehicle vehicle : vehicles) {
            VBox card = new VBox(5);
            card.getStyleClass().add("list-card");

            Label title = new Label("Titel: " + vehicle.getTitle());
            Label plate = new Label("Kenteken: " + vehicle.getLicensePlate());
            Label type = new Label("Type: " + vehicle.getType());

            Button deleteBtn = new Button("Verwijderen");
            deleteBtn.setOnAction(e -> {
                if (vehicle.delete()) {
                    initialize();
                }
            });
            deleteBtn.getStyleClass().add("actie-knop-verwijder");

            Button editBtn = new Button("Bewerken");
            editBtn.setOnAction(e -> manager.switchTo(new VehicleDetailScreen(manager, vehicle)));
            editBtn.getStyleClass().add("actie-knop");

            HBox actions = new HBox(10, editBtn, deleteBtn);
            card.getChildren().addAll(title, plate, type, actions);
            vehicleBox.getChildren().add(card);
        }

    }

    @FXML
    private void handleBack() {
        if (manager.getUserController().getCurrentUser().isAdmin()) {
            manager.switchTo(ScreenType.ADMIN_DASHBOARD);
        } else {
            manager.switchTo(ScreenType.DASHBOARD);
        }
    }

    @FXML
    private void handleCreateVehicle() {
        manager.switchTo(new VehicleDetailScreen(manager, null)); // null = create mode
    }

}
