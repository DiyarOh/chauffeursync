package com.chauffeursync.models;

import com.chauffeursync.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Vehicle {

    private final String id;
    private String licensePlate;
    private String type;
    private String status;
    private int currentKilometer;

    public Vehicle(String id, String licensePlate, String type, String status) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.type = type;
        this.status = status;
    }
    public Vehicle(String id, String licensePlate, String type, String status, int currentKilometer) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.type = type;
        this.status = status;
        this.currentKilometer = currentKilometer;
    }

    public String getId() {
        return id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCurrentKilometer(int currentKilometer) {
        this.currentKilometer = currentKilometer;
    }


    public int getCurrentKilometer() {
        return currentKilometer;
    }

    public List<Shift> getShifts() {
        List<Shift> shifts = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Shift WHERE vehicle_id = ?");
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

    public List<VehicleReport> getReports() {
        List<VehicleReport> reports = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM VehicleReport WHERE vehicle_id = ?");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                reports.add(VehicleReport.fromResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reports;
    }

    public List<DamageReport> getDamageReports() {
        List<DamageReport> reports = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM DamageReport WHERE vehicle_id = ?");
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
    public static List<Vehicle> getAllVehicles(){
        List<Vehicle> vehicles = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Vehicle ");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Vehicle vehicle = new Vehicle(
                        rs.getString("id"),
                        rs.getString("license_plate"),
                        rs.getString("type"),
                        rs.getString("status"),
                        rs.getInt("current_kilometer")
                );
                vehicles.add(vehicle);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return vehicles;
    }

    public boolean delete() {
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Vehicle WHERE id = ?");
            stmt.setString(1, this.id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static Vehicle getById(String id) {
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Vehicle WHERE id = ?");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Vehicle(
                        rs.getString("id"),
                        rs.getString("license_plate"),
                        rs.getString("type"),
                        rs.getString("status")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean create(String userId) {
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO Vehicle (id, license_plate, type, status, current_kilometer) VALUES (?, ?, ?, ?, ?)"
            );
            stmt.setString(1, this.id);
            stmt.setString(2, this.licensePlate);
            stmt.setString(3, this.type);
            stmt.setString(4, this.status);
            stmt.setInt(5, this.currentKilometer);
            boolean success = stmt.executeUpdate() > 0;

            if (success) {
                KilometerEntry.createEntry(null, this.id, this.currentKilometer, userId);
            }
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update() {
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE Vehicle SET license_plate = ?, type = ?, status = ?, current_kilometer = ? WHERE id = ?"
            );
            stmt.setString(1, this.licensePlate);
            stmt.setString(2, this.type);
            stmt.setString(3, this.status);
            stmt.setInt(4, this.currentKilometer);
            stmt.setString(5, this.id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public String getTitle() {
        return String.format("%s %s", licensePlate, type);
    }
}
