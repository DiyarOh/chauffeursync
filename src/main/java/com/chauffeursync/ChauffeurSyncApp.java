package com.chauffeursync;

import com.chauffeursync.database.DatabaseManager;
import com.chauffeursync.manager.ScreenManager;
import com.chauffeursync.enums.ScreenType;
import javafx.application.Application;
import javafx.stage.Stage;

public class ChauffeurSyncApp extends Application {

    @Override
    public void start(Stage stage) {
        DatabaseManager.initializeIfNeeded();
        ScreenManager screenManager = new ScreenManager(stage);
        screenManager.switchTo(ScreenType.START);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
