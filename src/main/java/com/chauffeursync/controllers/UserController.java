package com.chauffeursync.controllers;

import com.chauffeursync.database.DatabaseManager;
import com.chauffeursync.models.Role;
import com.chauffeursync.models.User;
import com.chauffeursync.models.UserUpdateData;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserController {

    private User currentUser;

    public boolean login(String email, String password) {
        String hashedPassword = hashPassword(password);
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM User WHERE email = ? AND password = ?");
            stmt.setString(1, email);
            stmt.setString(2, hashedPassword);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                currentUser = new User(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        null, // Don’t store password
                        rs.getString("role_id")
                );
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean register(String name, String email, String password) {
        String roleId = getRoleIdByTitle("Chauffeur");
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

    public User getCurrentUser() {
        return currentUser;
    }

    public User getUserById(String id) {
        return User.findById(id);
    }

    public boolean updateUser(String id, UserUpdateData data) {
        return User.updateById(id, data);
    }

    public boolean deleteUser(String id) {
        return User.deleteById(id);
    }

    private boolean userExists(String email) {
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT 1 FROM User WHERE email = ?");
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashed = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashed) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getRoleIdByTitle(String title) {
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id FROM Role WHERE title = ?");
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
