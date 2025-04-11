package com.chauffeursync.screens;

import com.chauffeursync.controllers.VehicleDetailController;
import com.chauffeursync.manager.ScreenManager;
import com.chauffeursync.models.Vehicle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.net.URL;

public class VehicleDetailScreen extends AbstractScreen {
    private final Vehicle vehicle;

    public VehicleDetailScreen(ScreenManager manager) {
        super(manager);
        this.vehicle = manager.getVehicleController().getCurrentVehicle();
    }

    public VehicleDetailScreen(ScreenManager manager, Vehicle vehicle) {
        super(manager);
        this.vehicle = vehicle;
    }

    @Override
    public void show() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/chauffeursync/fxml/vehicle_detail.fxml"));
            loader.setController(new VehicleDetailController(manager, vehicle));
            Parent root = loader.load();

            Scene scene = new Scene(root, 600, 400);
            URL cssUrl = getClass().getResource("/com/chauffeursync/css/base.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            manager.getPrimaryStage().setTitle("Voertuig " + (vehicle == null ? "Aanmaken" : "Bewerken"));
            manager.getPrimaryStage().setScene(scene);
            manager.getPrimaryStage().setResizable(false);
            manager.getPrimaryStage().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}