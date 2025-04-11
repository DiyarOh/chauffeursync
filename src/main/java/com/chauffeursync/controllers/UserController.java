package com.chauffeursync.controllers;

import com.chauffeursync.models.Role;
import com.chauffeursync.models.Shift;
import com.chauffeursync.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserController {

    private User currentUser;

    public boolean login(String email, String password) {
        User user = User.findUser(email);
        if (user != null && user.checkPassword(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public boolean register(String name, String email, String password) {
        String roleId = Role.getRoleIdByTitle("Chauffeur");
        if (roleId == null) {
            System.out.println("Rol 'Chauffeur' niet gevonden.");
            return false;
        }

        if (userExists(email)) {
            System.out.println("Gebruiker bestaat al.");
            return false;
        }

        User user = User.insertUser(name, email, password, roleId);
        return user != null;
    }

    public void logout() {
        currentUser = null;
    }

    public boolean isAdmin() {
        Role role = currentUser.getRole();
        return role != null && "Administrator".equalsIgnoreCase(role.getTitle());
    }

    public List<User> listUsers() {
        if (!isAdmin()) {
            System.out.println("Actie niet toegestaan: alleen beheerders kunnen gebruikerslijst ophalen.");
            return new ArrayList<>();
        }
        return User.findAll();
    }

    public void updateEmailAndPassword(String id, String newEmail, String newPassword) {
        if (!newEmail.isEmpty()) {
            User.updateEmailById(id, newEmail);
        }
        if (!newPassword.isEmpty()) {
            User.updatePasswordById(id, newPassword);
        }
    }

    public void updatePassword(String id, String newPassword) {
        if (!newPassword.isEmpty()) {
            User.updatePasswordById(id, newPassword);
        }
    }

    public void updateRole(String id, String newRole) {
        if (!newRole.isEmpty()) {
            User.updateRoleById(id, newRole);
        }
    }

    public void createShift (Shift shift) {
        shift.save();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public User getUserById(String id) {
        return User.findById(id);
    }

    public boolean deleteUser(String id) {
        return User.deleteById(id);
    }

    private boolean userExists(String email) {
        return User.findUser(email) != null;
    }
}
