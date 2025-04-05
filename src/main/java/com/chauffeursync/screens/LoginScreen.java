package com.chauffeursync.screens;

import com.chauffeursync.controllers.LoginController;
import com.chauffeursync.interfaces.Screen;
import com.chauffeursync.manager.ScreenManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class LoginScreen implements Screen {

    private final ScreenManager manager;

    public LoginScreen(ScreenManager manager) {
        this.manager = manager;
    }

    @Override
    public void show() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/chauffeursync/fxml/login.fxml"));
            loader.setController(new LoginController(manager));
            Parent root = loader.load();

            manager.getPrimaryStage().setTitle("Inloggen - ChauffeurSync");
            manager.getPrimaryStage().setScene(new Scene(root));
            manager.getPrimaryStage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
