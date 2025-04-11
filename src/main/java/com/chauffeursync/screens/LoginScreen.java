package com.chauffeursync.screens;

import com.chauffeursync.controllers.LoginController;
import com.chauffeursync.manager.ScreenManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;

public class LoginScreen extends AbstractScreen {

    public LoginScreen(ScreenManager manager) {
        super(manager);
    }

    @Override
    public void show() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/chauffeursync/fxml/login.fxml"));
            loader.setController(new LoginController(manager));
            Parent root = loader.load();

            Scene scene = new Scene(root, 400, 300);

            String cssPath = "/com/chauffeursync/css/login_screen.css";
            URL cssUrl = getClass().getResource(cssPath);
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            manager.getPrimaryStage().setTitle("Inloggen - ChauffeurSync");
            manager.getPrimaryStage().setScene(scene);
            manager.getPrimaryStage().setResizable(false);
            manager.getPrimaryStage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
