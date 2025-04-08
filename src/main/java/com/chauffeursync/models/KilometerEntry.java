package com.chauffeursync.models;

import com.chauffeursync.database.DatabaseManager;

import java.sql.*;
import java.util.UUID;

public class KilometerEntry {
    private final String id;
    private final String shiftId;
    private final String vehicleId;
    private final int km;
    private final String timestamp;
    private Boolean confirmedByDriver;
    private Boolean confirmedByAdmin;

    public KilometerEntry(String id, String shiftId, String vehicleId, int km,
                          String timestamp, Boolean confirmedByDriver, Boolean confirmedByAdmin) {
        this.id = id;
        this.shiftId = shiftId;
        this.vehicleId = vehicleId;
        this.km = km;
        this.timestamp = timestamp;
        this.confirmedByDriver = confirmedByDriver;
        this.confirmedByAdmin = confirmedByAdmin;
    }

    public static KilometerEntry fromResultSet(ResultSet rs) throws Exception {
        return new KilometerEntry(
                rs.getString("id"),
                rs.getString("shift_id"),
                rs.getString("vehicle_id"),
                rs.getInt("km"),
                rs.getString("timestamp"),
                rs.getObject("confirmed_by_driver", Boolean.class),
                rs.getObject("confirmed_by_admin", Boolean.class)
        );
    }

    public static KilometerEntry createEntry(String shiftId, String vehicleId, int km) {
        String id = UUID.randomUUID().toString();
        String now = Timestamp.valueOf(java.time.LocalDateTime.now()).toString();

        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO KilometerEntry (id, shift_id, vehicle_id, km, timestamp, confirmed_by_driver, confirmed_by_admin) " +
                            "VALUES (?, ?, ?, ?, ?, NULL, NULL)"
            );
            stmt.setString(1, id);
            stmt.setString(2, shiftId);
            stmt.setString(3, vehicleId);
            stmt.setInt(4, km);
            stmt.setString(5, now);
            stmt.executeUpdate();

            return new KilometerEntry(id, shiftId, vehicleId, km, now, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean driverApprove() {
        return updateApproval("confirmed_by_driver", true);
    }

    public boolean adminApprove(boolean approve) {
        return updateApproval("confirmed_by_admin", approve);
    }

    private boolean updateApproval(String field, Boolean value) {
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE KilometerEntry SET " + field + " = ? WHERE id = ?"
            );
            stmt.setObject(1, value);
            stmt.setString(2, id);
            int updated = stmt.executeUpdate();
            if (updated > 0) {
                if (field.equals("confirmed_by_driver")) this.confirmedByDriver = value;
                if (field.equals("confirmed_by_admin")) this.confirmedByAdmin = value;
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
