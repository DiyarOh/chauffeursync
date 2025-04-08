package com.chauffeursync.controllers;
import com.chauffeursync.enums.ScreenType;
import com.chauffeursync.manager.ScreenManager;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    private final ScreenManager screenManager;
    private final UserController userController;

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    public RegisterController(ScreenManager screenManager) {
        this.screenManager = screenManager;
        this.userController = screenManager.getUserController();
    }

    @FXML
    public void handleRegister() {
        boolean success = userController.register(
                nameField.getText(),
                emailField.getText(),
                passwordField.getText()
        );

        if (success) {
            screenManager.switchTo(ScreenType.LOGIN);
        } else {
            System.out.println("Registratie mislukt");
        }
    }
    
    @FXML
    private void handleBack() {
        screenManager.switchTo(ScreenType.START);
    }
}
