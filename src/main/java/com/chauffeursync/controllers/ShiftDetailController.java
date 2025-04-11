package com.chauffeursync.controllers;

import com.chauffeursync.enums.ScreenType;
import com.chauffeursync.manager.ScreenManager;
import com.chauffeursync.models.KilometerEntry;
import com.chauffeursync.models.Shift;
import com.chauffeursync.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

import java.util.List;
import java.util.function.UnaryOperator;

public class ShiftDetailController {

    private final ScreenManager manager;
    private Shift shift;

    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Button backButton;
    @FXML private Button logoutButton;

    @FXML private Label startTimeLabel;
    @FXML private Label endTimeLabel;
    @FXML private Label vehicleIdLabel;

    @FXML private Spinner<Integer> startKmSpinner;
    @FXML private Spinner<Integer> endKmSpinner;
    @FXML private Button updateStartKmBtn;
    @FXML private Button updateEndKmBtn;

    @FXML private ComboBox<User> userSelector;
    @FXML private Button changeUserBtn;
    @FXML private Button deleteShiftBtn;
    @FXML private Label changeUser;
    @FXML private HBox changeUserBox;
    @FXML private HBox deleteShift;


    public ShiftDetailController(ScreenManager manager, Shift shift) {
        this.manager = manager;
        this.shift = shift;
    }

    @FXML
    public void initialize() {
        boolean isAdmin = manager.getUserController().getCurrentUser().isAdmin();

        changeUser.setVisible(isAdmin);
        changeUser.setManaged(isAdmin);
        changeUserBox.setVisible(isAdmin);
        changeUserBox.setManaged(isAdmin);
        deleteShift.setManaged(isAdmin);
        deleteShift.setVisible(isAdmin);
        deleteShiftBtn.setManaged(isAdmin);
        deleteShiftBtn.setVisible(isAdmin);

        if (manager.getUserController().getCurrentUser() != null) {
            nameLabel.setText(manager.getUserController().getCurrentUser().getName());
            emailLabel.setText(manager.getUserController().getCurrentUser().getEmail());
        }

        if (shift != null) {
            Shift latest = Shift.getById(shift.getId());
            if (latest != null) {
                shift = latest;

                startTimeLabel.setText(shift.getStartTime().toString());
                endTimeLabel.setText(shift.getEndTime().toString());
                vehicleIdLabel.setText(shift.getVehicleId());

                configureSpinner(startKmSpinner, shift.getStartKm() != null ? shift.getStartKm() : 0);
                configureSpinner(endKmSpinner, shift.getEndKm() != null ? shift.getEndKm() : 0);
            }

            List<User> users = User.getAllUsers();
            userSelector.getItems().addAll(users);

            userSelector.setConverter(new StringConverter<>() {
                @Override
                public String toString(User user) {
                    return user != null ? user.getName() + " (" + user.getEmail() + ")" : "";
                }

                @Override
                public User fromString(String string) {
                    return users.stream()
                            .filter(u -> (u.getName() + " (" + u.getEmail() + ")").equals(string))
                            .findFirst()
                            .orElse(null);
                }
            });

            userSelector.setValue(
                    users.stream()
                            .filter(u -> u.getId().equals(shift.getUserId()))
                            .findFirst()
                            .orElse(null)
            );
        }
    }


    @FXML
    private void handleBack() {
        if (manager.getUserController().getCurrentUser().isAdmin()) {
            manager.switchTo(ScreenType.ADMIN_DASHBOARD);
        } else {
            manager.switchTo(ScreenType.CHAUFFEUR_DASHBOARD);
        }
    }

    @FXML
    private void handleLogout() {
        manager.getUserController().logout();
    }

    @FXML
    private void handleUpdateStartKm() {
        System.out.println("Start km bijgewerkt: " + startKmSpinner.getValue());
        KilometerEntry.createEntry(shift.getId(), shift.getVehicleId(), startKmSpinner.getValue(), manager.getUserController().getCurrentUser().getId());
        shift.updateStartKm(startKmSpinner.getValue());
    }

    @FXML
    private void handleUpdateEndKm() {
        System.out.println("Eind km bijgewerkt: " + endKmSpinner.getValue());
        KilometerEntry.createEntry(shift.getId(), shift.getVehicleId(), endKmSpinner.getValue(), manager.getUserController().getCurrentUser().getId());
        shift.updateEndKm(endKmSpinner.getValue());
    }

    @FXML
    private void handleDeleteShift() {
        if (shift != null) {
            boolean success = shift.delete();
            if (success) {
                System.out.println("Shift verwijderd");
                if (manager.getUserController().isAdmin()) {
                    manager.switchTo(ScreenType.ADMIN_DASHBOARD);
                } else {
                    manager.switchTo(ScreenType.DASHBOARD);
                }
            } else {
                System.out.println("Verwijderen mislukt");
            }
        }
    }

    @FXML
    private void handleChangeUser() {
        User selectedUser = userSelector.getValue();
        if (selectedUser != null && !selectedUser.getId().equals(shift.getUserId())) {
            boolean updated = shift.updateUserId(selectedUser.getId());
            if (updated) {
                System.out.println("Gebruiker bijgewerkt");
                shift = Shift.getById(shift.getId());
                userSelector.setValue(selectedUser);
            } else {
                System.out.println("Bijwerken mislukt");
            }
        }
    }

    private void configureSpinner(Spinner<Integer> spinner, int initialValue) {
        // Clear any existing formatter (optional safety)
        spinner.getEditor().setTextFormatter(null);

        SpinnerValueFactory.IntegerSpinnerValueFactory factory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, initialValue);
        spinner.setValueFactory(factory);
        spinner.setEditable(true);

        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            return newText.matches("\\d*") ? change : null;
        };

        TextFormatter<Integer> formatter = new TextFormatter<>(
                factory.getConverter(),
                factory.getValue(),
                integerFilter
        );

        spinner.getEditor().setTextFormatter(formatter);

        formatter.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.equals(spinner.getValue())) {
                spinner.getValueFactory().setValue(newVal);
            }
        });
    }
}
