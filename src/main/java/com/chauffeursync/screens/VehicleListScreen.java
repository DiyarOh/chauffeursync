package com.chauffeursync.screens;

import com.chauffeursync.controllers.VehicleListController;
import com.chauffeursync.manager.ScreenManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.net.URL;

public class VehicleListScreen extends AbstractScreen {

    public VehicleListScreen(ScreenManager manager) {
        super(manager);
    }

    @Override
    public void show() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/chauffeursync/fxml/vehicle_list.fxml"));
            loader.setController(new VehicleListController(manager));
            Parent root = loader.load();

            Scene scene = new Scene(root, 1000, 700);
            String cssPath = "/com/chauffeursync/css/base.css";
            URL cssUrl = getClass().getResource(cssPath);
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            manager.getPrimaryStage().setTitle("Voertuigen - ChauffeurSync");
            manager.getPrimaryStage().setScene(scene);
            manager.getPrimaryStage().setResizable(false);
            manager.getPrimaryStage().show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
