package com.chauffeursync.screens;

import com.chauffeursync.enums.ScreenType;
import com.chauffeursync.manager.ScreenManager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class StartScreen extends AbstractScreen {

    public StartScreen(ScreenManager manager) {
        super(manager);
    }

    @Override
    public void show() {
        // Titel
        Text title = new Text("Welkom bij ChauffeurSync");
        title.setFont(Font.font("Segoe UI", 22));

        // Knoppen
        Button loginBtn = new Button("Inloggen");
        Button registerBtn = new Button("Registreren");

        loginBtn.getStyleClass().add("main-button");
        registerBtn.getStyleClass().add("main-button");

        loginBtn.setOnAction(e -> manager.switchTo(ScreenType.LOGIN));
        registerBtn.setOnAction(e -> manager.switchTo(ScreenType.REGISTER));

        // Layout
        VBox buttonBox = new VBox(15, loginBtn, registerBtn);
        buttonBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(30, title, buttonBox);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f7f9fc;");

        Scene scene = new Scene(root, 400, 300);

        // Voeg optioneel CSS toe
        String css = getClass().getResource("/com/chauffeursync/css/start_screen.css").toExternalForm();
        scene.getStylesheets().add(css);

        manager.getPrimaryStage().setTitle("Welkom - ChauffeurSync");
        manager.getPrimaryStage().setScene(scene);
        manager.getPrimaryStage().setResizable(false);
        manager.getPrimaryStage().show();
    }
}