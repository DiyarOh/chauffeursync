package com.chauffeursync.models;

import com.chauffeursync.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Vehicle {

    private final String id;
    private final String licensePlate;
    private final String type;
    private final String status;
    private int currentKilometer;

    public Vehicle(String id, String licensePlate, String type, String status) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.type = type;
        this.status = status;
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
                        rs.getString("status")
                );
                vehicles.add(vehicle);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return vehicles;
    }

    public String getTitle() {
        return String.format("%s %s", licensePlate, type);
    }
}
