package com.chauffeursync.controllers;

import com.chauffeursync.enums.ScreenType;
import com.chauffeursync.manager.ScreenManager;
import com.chauffeursync.models.KilometerEntry;
import com.chauffeursync.models.Vehicle;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.UUID;

public class VehicleDetailController {

    private final ScreenManager manager;
    private final Vehicle vehicle;

    @FXML private Label nameLabel;
    @FXML private Label emailLabel;

    @FXML private TextField licensePlateField;
    @FXML private TextField typeField;
    @FXML private TextField statusField;
    @FXML private TextField kmField;
    @FXML private Button saveBtn;
    @FXML private Button backBtn;

    public VehicleDetailController(ScreenManager manager, Vehicle vehicle) {
        this.manager = manager;
        this.vehicle = vehicle;
    }

    @FXML
    public void initialize() {
        if (manager.getUserController().getCurrentUser() != null) {
            nameLabel.setText(manager.getUserController().getCurrentUser().getName());
            emailLabel.setText(manager.getUserController().getCurrentUser().getEmail());
        }

        if (vehicle != null) {
            licensePlateField.setText(vehicle.getLicensePlate());
            typeField.setText(vehicle.getType());
            statusField.setText(vehicle.getStatus());
            kmField.setText(String.valueOf(vehicle.getCurrentKilometer()));
        }
    }

    @FXML
    private void handleBack() {
        manager.switchTo(ScreenType.VEHICLE_LIST);
    }

    @FXML
    private void handleSave() {
        String license = licensePlateField.getText().trim();
        String type = typeField.getText().trim();
        String status = statusField.getText().trim();
        int km = Integer.parseInt(kmField.getText().trim());

        if (vehicle == null) {
            Vehicle newVehicle = new Vehicle(UUID.randomUUID().toString(), license, type, status, km);
            // Create new
            manager.getVehicleController().createVehicle(newVehicle);
        } else {
            // Edit existing
            vehicle.setLicensePlate(license);
            vehicle.setType(type);
            vehicle.setStatus(status);
            KilometerEntry.createEntry(null, vehicle.getId(), km, manager.getUserController().getCurrentUser().getId());
            vehicle.setCurrentKilometer(km);
            manager.getVehicleController().updateVehicle(vehicle);
        }

        manager.switchTo(ScreenType.VEHICLE_LIST);
    }
}
