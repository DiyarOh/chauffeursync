package com.chauffeursync.controllers;

import com.chauffeursync.enums.ScreenType;
import com.chauffeursync.manager.ScreenManager;

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
            System.out.println("Ingelogd als: " + userController.getCurrentUser().getName());
            screenManager.switchTo(ScreenType.DASHBOARD);
        } else {
            System.out.println("Ongeldige inloggegevens");
            // Later: show error in de UI
        }
    }
}
