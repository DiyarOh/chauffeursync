package com.chauffeursync.screens;

import com.chauffeursync.enums.ScreenType;
import com.chauffeursync.interfaces.Screen;
import com.chauffeursync.manager.ScreenManager;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class StartScreen implements Screen {

    private final ScreenManager manager;

    public StartScreen(ScreenManager manager) {
        this.manager = manager;
    }

    @Override
    public void show() {
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 40; -fx-alignment: center;");

        Button loginBtn = new Button("Inloggen");
        loginBtn.setOnAction(e -> manager.switchTo(ScreenType.LOGIN));

        Button registerBtn = new Button("Registreren");
        registerBtn.setOnAction(e -> manager.switchTo(ScreenType.REGISTER));

        root.getChildren().addAll(loginBtn, registerBtn);

        manager.getPrimaryStage().setTitle("Welkom - ChauffeurSync");
        manager.getPrimaryStage().setScene(new Scene(root, 300, 200));
        manager.getPrimaryStage().show();
    }
}
