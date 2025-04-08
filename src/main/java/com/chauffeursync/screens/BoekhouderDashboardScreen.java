package com.chauffeursync.screens;

import com.chauffeursync.manager.ScreenManager;

public class BoekhouderDashboardScreen extends DashboardScreen {
    public BoekhouderDashboardScreen(ScreenManager manager) {
        super(manager);
    }

    @Override
    protected String getFxmlPath() {
        return "/com/chauffeursync/fxml/boekhouder_dashboard.fxml";
    }

    @Override
    protected String getCssFilename() {
        return "/com/chauffeursync/css/admin_dashboard.css";
    }

    @Override
    protected String getTitle() {
        return "Boekhouder Dashboard";
    }
}