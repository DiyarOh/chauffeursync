package com.chauffeursync.manager;

import com.chauffeursync.controllers.UserController;
import com.chauffeursync.controllers.VehicleController;
import com.chauffeursync.enums.ScreenType;
import com.chauffeursync.screens.*;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class ScreenManager {

    private final Stage primaryStage;
    private final Map<ScreenType, AbstractScreen> screenRegistry = new HashMap<>();

    private final UserController userController = new UserController();
    private final VehicleController vehicleController = new VehicleController();

    public ScreenManager(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Registreer alle schermen hier
        register(ScreenType.START, new StartScreen(this));
        register(ScreenType.LOGIN, new LoginScreen(this));
        register(ScreenType.REGISTER, new RegisterScreen(this));
        register(ScreenType.ADMIN_DASHBOARD, new AdminDashboardScreen(this));
        register(ScreenType.CHAUFFEUR_DASHBOARD, new ChauffeurDashboardScreen(this));
        register(ScreenType.BOEKHOUDER_DASHBOARD, new BoekhouderDashboardScreen(this));
        register(ScreenType.MANAGE_USERS, new ManageUserScreen(this));
        register(ScreenType.VEHICLE_LIST, new VehicleListScreen(this));
        register(ScreenType.VEHICLE_DETAIL, new VehicleDetailScreen(this));
        vehicleController.setScreenManager(this);
    }

    private void register(ScreenType type, AbstractScreen screen) {
        screenRegistry.put(type, screen);
    }

    public void switchTo(ScreenType type) {
        AbstractScreen screen = screenRegistry.get(type);
        if (screen != null) {
            screen.show();
        } else {
            System.out.println("Scherm niet gevonden: " + type);
        }
    }

    public void switchTo(AbstractScreen screen) {
        if (screen != null) {
            screen.show();
        } else {
            System.out.println("Geen scherm meegegeven.");
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public UserController getUserController() {
        return userController;
    }

    public VehicleController getVehicleController() {
        return vehicleController;
    }
}
