package com.chauffeursync.models;

import com.chauffeursync.database.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.security.MessageDigest;
import java.util.UUID;

public class User {

    private final String id;
    private final String name;
    private final String email;
    private final String password;
    private final String roleId;

    public User(String id, String email, String name, String password, String roleId) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.roleId = roleId;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getRoleId() {
        return roleId;
    }

    public boolean checkPassword(String inputPassword) {
        String hashedInput = hashPassword(inputPassword);
        return this.password.equals(hashedInput);
    }


    public Role getRole() {
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Role WHERE id = ?");
            stmt.setString(1, roleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Role(rs.getString("id"), rs.getString("title"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Shift> getShifts() {
        List<Shift> shifts = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Shift WHERE user_id = ?");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                shifts.add(Shift.fromResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shifts;
    }

    public List<DriverReport> getReports() {
        List<DriverReport> reports = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM DriverReport WHERE user_id = ?");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                reports.add(DriverReport.fromResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reports;
    }

    public List<DamageReport> getDamageReports() {
        List<DamageReport> reports = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM DamageReport WHERE user_id = ?");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                reports.add(DamageReport.fromResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reports;
    }

    public static User insertUser(String name, String email, String plainPassword, String roleId) {
        String id = UUID.randomUUID().toString();
        String hashedPassword = hashPassword(plainPassword);

        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO User (id, name, email, password, role_id) VALUES (?, ?, ?, ?, ?)"
            );
            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setString(3, email);
            stmt.setString(4, hashedPassword);
            stmt.setString(5, roleId);
            stmt.executeUpdate();

            return new User(id, email, name, null, roleId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashed = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashed) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
