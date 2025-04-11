package com.chauffeursync.controllers;

import com.chauffeursync.manager.ScreenManager;
import com.chauffeursync.models.Vehicle;

import java.util.List;

public class VehicleController {
    public ScreenManager screenManager = null;

    Vehicle vehicle;

    public void setScreenManager(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    public List<Vehicle> getAllVehicles() {
        return Vehicle.getAllVehicles();
    }

    public Vehicle getById(String id) {
        return Vehicle.getById(id);
    }

    public boolean deleteVehicle(Vehicle vehicle) {
        return vehicle.delete();
    }

    public boolean createVehicle(Vehicle vehicle) {
        return vehicle.create(screenManager.getUserController().getCurrentUser().getId());
    }

    public boolean updateVehicle(Vehicle vehicle) {
        return vehicle.update();
    }

    public Vehicle getCurrentVehicle() {
        return vehicle;
    }
}
