package com.chauffeursync.controllers;
import com.chauffeursync.enums.ScreenType;
import com.chauffeursync.manager.ScreenManager;
import com.chauffeursync.models.Role;
import com.chauffeursync.models.User;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

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
        manager.switchTo(ScreenType.ADMIN_DASHBOARD);
    }

    @FXML
    public void handleLogout() {
        manager.getUserController().logout();
        manager.switchTo(com.chauffeursync.enums.ScreenType.START);
    }

    private void loadUsers() {
        userBox.getChildren().clear();

        List<User> users = User.getAllUsers();
        List<Role> availableRoles = Role.getAllRoles();

        for (User user : users) {
            VBox userCard = new VBox(8);
            userCard.getStyleClass().add("user-card");

            Label userNameLabel = new Label("Naam: " + user.getName());
            Label emailLabel = new Label("E-mail: " + user.getEmail());
            Label roleLabel = new Label("Rol: " + user.getRole().getTitle());

            // Action buttons
            Button deleteButton = new Button("Verwijderen");
            Button editButton = new Button("Bewerken");
            Button changeRoleButton = new Button("Rol wijzigen");

            deleteButton.getStyleClass().add("actie-knop");
            deleteButton.setOnAction(e -> {

            });
            editButton.getStyleClass().add("actie-knop");
            changeRoleButton.getStyleClass().add("actie-knop");

            HBox buttonBox = new HBox(10, deleteButton, editButton, changeRoleButton);
            buttonBox.getStyleClass().add("user-actions");

            // Edit user form (hidden)
            VBox editForm = new VBox(6);
            editForm.setManaged(false);
            editForm.setVisible(false);

            TextField emailField = new TextField(user.getEmail());
            PasswordField passwordField = new PasswordField();
            Button saveEditBtn = new Button("Opslaan");
            saveEditBtn.getStyleClass().add("actie-knop");

            saveEditBtn.setOnAction(e -> {
                String newEmail = emailField.getText().trim();
                String newPassword = passwordField.getText().trim();

                if (!newEmail.isEmpty() || !newPassword.isEmpty()) {
                    userController.updateEmailAndPassword(user.getId(), newEmail, newPassword);
                    System.out.println("Gebruiker bijgewerkt!");
                } else {
                    System.out.println("Beide velden zijn verplicht.");
                }
            });

            editForm.getChildren().addAll(
                    new Label("Nieuw e-mailadres:"), emailField,
                    new Label("Nieuw wachtwoord:"), passwordField,
                    saveEditBtn
            );

            // Confirm delete prompt
            HBox confirmDeleteBox = new HBox(10);
            confirmDeleteBox.setManaged(false);
            confirmDeleteBox.setVisible(false);
            confirmDeleteBox.getStyleClass().add("verwijder-box");

            Label confirmLabel = new Label("Weet je het zeker?");
            Button confirmYesBtn = new Button("Ja, verwijderen");
            Button confirmNoBtn = new Button("Annuleren");

            confirmYesBtn.getStyleClass().add("actie-knop-verwijder");
            confirmNoBtn.getStyleClass().add("actie-knop");

            confirmDeleteBox.getChildren().addAll(confirmLabel, confirmYesBtn, confirmNoBtn);

            deleteButton.setOnAction(e -> toggleVisibility(confirmDeleteBox));

            // Delete user
            confirmYesBtn.setOnAction(e -> {
                userController.deleteUser(user.getId());
                System.out.println("Gebruiker verwijderd: " + user.getId());
                loadUsers(); // herlaad lijst na verwijderen
            });

            // Cancel deletion
            confirmNoBtn.setOnAction(e -> toggleVisibility(confirmDeleteBox));

            // Change role form (hidden)
            VBox roleForm = new VBox(6);
            roleForm.setManaged(false);
            roleForm.setVisible(false);

            ComboBox<Role> roleSelector = new ComboBox<>();
            roleSelector.getItems().addAll(availableRoles);
            roleSelector.setValue(user.getRole());

            // This translates the Object name to be the getTitle so it's readable for the user
            roleSelector.setConverter(new StringConverter<>() {
                @Override
                public String toString(Role role) {
                    return role != null ? role.getTitle() : "";
                }

                // This reverses it, i don't use this, but it is required
                @Override
                public Role fromString(String string) {
                    return availableRoles.stream()
                            .filter(r -> r.getTitle().equals(string))
                            .findFirst()
                            .orElse(null);
                }
            });
            Button saveRoleBtn = new Button("Rol opslaan");
            saveRoleBtn.getStyleClass().add("actie-knop");

            saveRoleBtn.setOnAction(e -> {
                Role selectedRole = roleSelector.getValue();

                if (selectedRole != null && !selectedRole.equals(user.getRole())) {
                    userController.updateRole(user.getId(), selectedRole.getId());
                    System.out.println("Rol gewijzigd naar: " + selectedRole.getTitle());
                } else {
                    System.out.println("Geen wijziging uitgevoerd.");
                }
            });

            roleForm.getChildren().addAll(new Label("Selecteer rol:"), roleSelector, saveRoleBtn);

            // Toggle hidden
            editButton.setOnAction(e -> toggleVisibility(editForm));
            changeRoleButton.setOnAction(e -> toggleVisibility(roleForm));

            userCard.getChildren().addAll(
                    userNameLabel, emailLabel, roleLabel,
                    buttonBox, confirmDeleteBox, editForm, roleForm
            );

            userBox.getChildren().add(userCard);
        }
    }

    private void toggleVisibility(Node node) {
        boolean isVisible = node.isVisible();
        node.setVisible(!isVisible);
        node.setManaged(!isVisible);
    }

}
