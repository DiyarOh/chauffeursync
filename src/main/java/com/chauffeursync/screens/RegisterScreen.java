package com.chauffeursync.screens;

import com.chauffeursync.controllers.RegisterController;
import com.chauffeursync.interfaces.Screen;
import com.chauffeursync.manager.ScreenManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;

public class RegisterScreen extends AbstractScreen {

    public RegisterScreen(ScreenManager manager) {
        super(manager);
    }

    @Override
    public void show() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/chauffeursync/fxml/register.fxml"));
            loader.setController(new RegisterController(manager));
            Parent root = loader.load();
            Scene scene = new Scene(root, 400, 300);

            String cssPath = "/com/chauffeursync/css/register_screen.css";
            URL cssUrl = getClass().getResource(cssPath);
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            manager.getPrimaryStage().setTitle("Registreren - ChauffeurSync");
            manager.getPrimaryStage().setScene(scene);
            manager.getPrimaryStage().setResizable(false);
            manager.getPrimaryStage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
