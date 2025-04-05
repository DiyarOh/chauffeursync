package com.chauffeursync.screens;

import com.chauffeursync.controllers.RegisterController;
import com.chauffeursync.interfaces.Screen;
import com.chauffeursync.manager.ScreenManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class RegisterScreen implements Screen {

    private final ScreenManager manager;

    public RegisterScreen(ScreenManager manager) {
        this.manager = manager;
    }

    @Override
    public void show() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/chauffeursync/fxml/register.fxml"));
            loader.setController(new RegisterController(manager));
            Parent root = loader.load();

            manager.getPrimaryStage().setTitle("Registreren - ChauffeurSync");
            manager.getPrimaryStage().setScene(new Scene(root));
            manager.getPrimaryStage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
