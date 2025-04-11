package com.chauffeursync.screens;

import com.chauffeursync.manager.ScreenManager;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Screen;

public abstract class AbstractScreen {

    protected final ScreenManager manager;

    public AbstractScreen(ScreenManager manager) {
        this.manager = manager;
    }

    public abstract void show();

    protected Stage prepareStage(Scene scene, String title, int width, int height, boolean resizable) {
        Stage stage = manager.getPrimaryStage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setResizable(resizable);

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - width) / 2);
        stage.setY((screenBounds.getHeight() - height) / 2);

        return stage;
    }

    protected Stage prepareFixedStage(Scene scene, String title, int width, int height) {
        return prepareStage(scene, title, width, height, false);
    }
}