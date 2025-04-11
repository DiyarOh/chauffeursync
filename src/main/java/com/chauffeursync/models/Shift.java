package com.chauffeursync.models;

import com.chauffeursync.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Shift {
    private String id;
    private String userId;
    private final String vehicleId;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private Integer startKm;
    private Integer endKm;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DEFAULT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter WITH_SECONDS_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Shift(String id, String userId, String vehicleId, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Shift(String userId, String vehicleId, LocalDateTime startTime, LocalDateTime endTime) {
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static Shift fromResultSet(ResultSet rs) throws Exception {
        String startStr = rs.getString("start_time");
        String endStr = rs.getString("end_time");

        LocalDateTime startTime = parseFlexibleDateTime(startStr);
        LocalDateTime endTime = parseFlexibleDateTime(endStr);

        Shift shift = new Shift(
                rs.getString("id"),
                rs.getString("user_id"),
                rs.getString("vehicle_id"),
                startTime,
                endTime
        );

        shift.startKm = rs.getObject("start_km") != null ? rs.getInt("start_km") : null;
        shift.endKm = rs.getObject("end_km") != null ? rs.getInt("end_km") : null;

        return shift;
    }

    public void save() {
        String id = UUID.randomUUID().toString();

        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO Shift (id, user_id, vehicle_id, start_time, end_time) VALUES (?, ?, ?, ?, ?)"
            );
            stmt.setString(1, id);
            stmt.setString(2, userId);
            stmt.setString(3, vehicleId);
            stmt.setString(4, startTime.format(DEFAULT_FORMAT));
            stmt.setString(5, endTime.format(DEFAULT_FORMAT));
            stmt.executeUpdate();

            System.out.println("Shift " + id + " saved");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public boolean updateStartKm(int km) {
        return updateKmValue("start_km", km);
    }

    public boolean updateEndKm(int km) {
        return updateKmValue("end_km", km);
    }

    public Integer getStartKm() {
        return startKm;
    }

    public Integer getEndKm() {
        return endKm;
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
        return getShiftsByQuery("SELECT * FROM Shift");
    }

    public static List<Shift> getUserShifts(String userId) {
        return getShiftsByQuery("SELECT * FROM Shift WHERE user_id = ?", userId);
    }

    public static List<Shift> getVehicleShifts(String vehicleId) {
        return getShiftsByQuery("SELECT * FROM Shift WHERE vehicle_id = ?", vehicleId);
    }

    public static Shift getById(String shiftId) {
        List<Shift> result = getShiftsByQuery("SELECT * FROM Shift WHERE id = ?", shiftId);
        return result.isEmpty() ? null : result.getFirst();
    }

    private static List<Shift> getShiftsByQuery(String query, String... params) {
        List<Shift> shifts = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                stmt.setString(i + 1, params[i]);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                shifts.add(fromResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shifts;
    }

    private static LocalDateTime parseFlexibleDateTime(String input) {
        try {
            return LocalDateTime.parse(input, DEFAULT_FORMAT);
        } catch (Exception e) {
            try {
                return LocalDateTime.parse(input, WITH_SECONDS_FORMAT);
            } catch (Exception ex) {
                System.err.println("Kon datum/tijd niet parsen: " + input);
                return null; // or throw an exception if nulls are not allowed
            }
        }
    }

    private void setId(String id) {
        this.id = id;
    }

    public boolean delete() {
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Shift WHERE id = ?");
            stmt.setString(1, this.id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean updateUserId(String newUserId) {
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE Shift SET user_id = ? WHERE id = ?");
            stmt.setString(1, newUserId);
            stmt.setString(2, this.id);
            int updated = stmt.executeUpdate();
            if (updated > 0) {
                this.userId = newUserId;
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
