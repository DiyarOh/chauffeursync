package com.chauffeursync.models;

import com.chauffeursync.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Shift {
    private final String id;
    private final String userId;
    private final String vehicleId;
    private final String startTime;
    private final String endTime;

    public Shift(String id, String userId, String vehicleId, String startTime, String endTime) {
        this.id = id;
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static Shift fromResultSet(ResultSet rs) throws Exception {
        return new Shift(
                rs.getString("id"),
                rs.getString("user_id"),
                rs.getString("vehicle_id"),
                rs.getString("start_time"),
                rs.getString("end_time")
        );
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public static List<Shift> getAllShifts() {
        List<Shift> shifts = new ArrayList<Shift>();
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Shift ");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Shift shift = new Shift(
                        rs.getString("id"),
                        rs.getString("user_id"),
                        rs.getString("vehicle_id"),
                        rs.getString("start_time"),
                        rs.getString("end_time")
                        );
                shifts.add(shift);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return shifts;
    }

    public static List<Shift> getUserShifts(String id) {
        List<Shift> shifts = new ArrayList<Shift>();
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Shift WHERE user_id=?");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Shift shift = new Shift(
                        rs.getString("id"),
                        rs.getString("user_id"),
                        rs.getString("vehicle_id"),
                        rs.getString("start_time"),
                        rs.getString("end_time")
                );
                shifts.add(shift);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return shifts;
    }

    public static List<Shift> getVehicleShifts(String id) {
        List<Shift> shifts = new ArrayList<Shift>();
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Shift WHERE vehicle_id=?");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Shift shift = new Shift(
                        rs.getString("id"),
                        rs.getString("user_id"),
                        rs.getString("vehicle_id"),
                        rs.getString("start_time"),
                        rs.getString("end_time")
                );
                shifts.add(shift);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return shifts;
    }
}
