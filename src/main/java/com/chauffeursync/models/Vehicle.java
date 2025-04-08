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
    private final int currentKilometer;

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
}
