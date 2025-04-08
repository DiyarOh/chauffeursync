package com.chauffeursync.screens;

import com.chauffeursync.controllers.DashboardController;
import com.chauffeursync.interfaces.Screen;
import com.chauffeursync.manager.ScreenManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class AdminDashboardScreen extends DashboardScreen {
    public AdminDashboardScreen(ScreenManager manager) {
        super(manager);
    }

    @Override
    protected String getFxmlPath() {
        return "/com/chauffeursync/fxml/admin_dashboard.fxml";
    }

    @Override
    protected String getCssFilename() {
        return "/com/chauffeursync/css/admin_dashboard.css";
    }

    @Override
    protected String getTitle() {
        return "Administrator Dashboard";
    }
}