package com.chauffeursync.controllers;
import com.chauffeursync.enums.ScreenType;
import com.chauffeursync.manager.ScreenManager;
import com.chauffeursync.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

public class ManageUserController {

    private final ScreenManager manager;
    private final UserController userController;
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private VBox userBox;

    public ManageUserController(ScreenManager screenManager) {
        this.manager = screenManager;
        this.userController = screenManager.getUserController();
    }

    @FXML
    public void initialize() {
        User user = manager.getUserController().getCurrentUser();
        if (user != null) {
            nameLabel.setText(user.getName());
            emailLabel.setText(user.getEmail());
            loadUsers();
        }
    }
    
    @FXML
    private void handleBack() {
        manager.switchTo(ScreenType.START);
    }

    @FXML
    public void handleLogout() {
        manager.getUserController().logout();
        manager.switchTo(com.chauffeursync.enums.ScreenType.START);
    }

    private void loadUsers() {
        userBox.getChildren().clear();

        List<User> users = User.getAllUsers();

        for (User user : users) {
            System.out.println(user.getName());
            VBox userCard = new VBox();
            userCard.getStyleClass().add("user-card");
            userCard.setSpacing(6);

            Label userNameLabel = new Label(String.format("Naam: %s", user.getName()));
            Label emailLabel = new Label(String.format("Email: %s", user.getEmail()));
            Label roleLabel = new Label(String.format("Rol: %s", user.getRole().toString()));

            userCard.getChildren().addAll(userNameLabel, emailLabel, roleLabel);
            userCard.getStyleClass().add("user-card");

            userCard.setOnMouseClicked(e -> {
                System.out.println("User " + user.getId() + " geselecteerd");
                // Hier later een detailview openen
            });

            userBox.getChildren().add(userCard);
        }
    }
}
