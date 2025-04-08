package com.chauffeursync.controllers;

import com.chauffeursync.enums.ScreenType;
import com.chauffeursync.manager.ScreenManager;

import com.chauffeursync.models.Role;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    private final ScreenManager screenManager;
    private final UserController userController;

    public LoginController(ScreenManager screenManager) {
        this.screenManager = screenManager;
        this.userController = screenManager.getUserController();
    }

    @FXML
    public void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        boolean success = userController.login(email, password);

        if (success) {
            String name = userController.getCurrentUser().getName();
            Role role = userController.getCurrentUser().getRole();
            System.out.println("Ingelogd als: " + name + " (" + role.getTitle() + ")");

            switch (role.getTitle()) {
                case "Administrator" -> screenManager.switchTo(ScreenType.ADMIN_DASHBOARD);
                case "Chauffeur"     -> screenManager.switchTo(ScreenType.CHAUFFEUR_DASHBOARD);
                case "Boekhouder"    -> screenManager.switchTo(ScreenType.BOEKHOUDER_DASHBOARD);
                default              -> System.out.println("Onbekende rol: " + role);
            }
        } else {
            System.out.println("Ongeldige inloggegevens");
            // TODO: Geef foutmelding weer in de UI
        }
    }

    @FXML
    private void handleBack() {
        screenManager.switchTo(ScreenType.START);
    }
}
