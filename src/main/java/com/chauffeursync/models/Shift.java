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
    private Integer startKm;
    private Integer endKm;

    public Shift(String id, String userId, String vehicleId, String startTime, String endTime) {
        this.id = id;
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static Shift fromResultSet(ResultSet rs) throws Exception {
        Shift shift = new Shift(
                rs.getString("id"),
                rs.getString("user_id"),
                rs.getString("vehicle_id"),
                rs.getString("start_time"),
                rs.getString("end_time")
        );
        shift.startKm = rs.getObject("start_km") != null ? rs.getInt("start_km") : null;
        shift.endKm = rs.getObject("end_km") != null ? rs.getInt("end_km") : null;
        return shift;
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

    public boolean updateStartKm(int km) {
        return updateKmValue("start_km", km);
    }

    public boolean updateEndKm(int km) {
        return updateKmValue("end_km", km);
    }

    private boolean updateKmValue(String column, int km) {
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE Shift SET " + column + " = ? WHERE id = ?");
            stmt.setInt(1, km);
            stmt.setString(2, this.id);
            int updated = stmt.executeUpdate();
            if (updated > 0) {
                if (column.equals("start_km")) this.startKm = km;
                if (column.equals("end_km")) this.endKm = km;
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<KilometerEntry> getKilometerEntries() {
        List<KilometerEntry> entries = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM KilometerEntry WHERE shift_id = ?"
            );
            stmt.setString(1, this.id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                entries.add(KilometerEntry.fromResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entries;
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
