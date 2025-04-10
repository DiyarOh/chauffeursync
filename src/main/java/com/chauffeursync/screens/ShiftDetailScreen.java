package com.chauffeursync.screens;

import com.chauffeursync.controllers.ShiftDetailController;
import com.chauffeursync.manager.ScreenManager;
import com.chauffeursync.models.Shift;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;

public class ShiftDetailScreen extends AbstractScreen {

    private final Shift shift;

    public ShiftDetailScreen(ScreenManager manager, Shift shift) {
        super(manager);
        this.shift = shift;
    }

    @Override
    public void show() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/chauffeursync/fxml/shift_detail.fxml"));

            ShiftDetailController controller = new ShiftDetailController(manager, shift);
            loader.setController(controller);

            Parent root = loader.load();
            Scene scene = new Scene(root, 1000, 700);

            String cssPath = "/com/chauffeursync/css/base.css";
            URL cssUrl = getClass().getResource(cssPath);
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            manager.getPrimaryStage().setTitle("Shift Detail - ChauffeurSync");
            manager.getPrimaryStage().setScene(scene);
            manager.getPrimaryStage().setResizable(false);
            manager.getPrimaryStage().show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
