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
    private String password = null;
    private final String roleId;

    public User(String id, String email, String name, String password, String roleId) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.roleId = roleId;
    }
    public User(String id, String email, String name, String roleId) {
        this.id = id;
        this.email = email;
        this.name = name;
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

    public boolean isAdmin() {
        return getRole().getTitle().equals("Administrator");
    }

    public boolean checkPassword(String inputPassword) {
        String hashedInput = hashPassword(inputPassword);
        String passwordHash = getHashedPassword();

        if (passwordHash == null || passwordHash.isEmpty()) {
            return false;
        }
        System.out.println(hashedInput);
        return passwordHash.equals(hashedInput);
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
        return isAdmin() ? Shift.getAllShifts() : Shift.getUserShifts(id);
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
            System.out.println(e.getMessage());
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

    private String getHashedPassword() {
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT password FROM User WHERE id = ?");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("password");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
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

    public static User findById(String id) {
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM User WHERE id = ?");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("id"),
                        rs.getString("email"),
                        rs.getString("name"),
                        null,
                        rs.getString("role_id")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM User");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(new User(
                        rs.getString("id"),
                        rs.getString("email"),
                        rs.getString("name"),
                        null,
                        rs.getString("role_id")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public static boolean updateEmailById(String id, String email) {
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE User SET email = ? WHERE id = ?"
            );
            stmt.setString(1, email);
            stmt.setString(2, id);
            int updated = stmt.executeUpdate();
            return updated > 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean updateRoleById(String id, String roleId) {
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE User SET role_id = ? WHERE id = ?"
            );
            stmt.setString(1, roleId);
            stmt.setString(2, id);
            int updated = stmt.executeUpdate();
            return updated > 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean updatePasswordById(String id, String password) {
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE User SET password = ? WHERE id = ?"
            );
            stmt.setString(1, hashPassword(password));
            stmt.setString(2, id);
            int updated = stmt.executeUpdate();
            return updated > 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean deleteById(String id) {
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM User WHERE id = ?");
            stmt.setString(1, id);
            int deleted = stmt.executeUpdate();
            return deleted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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

    public static User getUserById(String id){
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM User WHERE id = ?");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("id"),
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("role_id")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<User> getAllUsers(){
        List<User> users = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM User ");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User(
                        rs.getString("id"),
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("role_id")
                );
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public static User findUser(String email) {
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM User WHERE email = ?");
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getString("id"),
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("role_id")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
