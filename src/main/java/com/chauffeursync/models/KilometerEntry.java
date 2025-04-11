package com.chauffeursync.models;

import com.chauffeursync.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public class KilometerEntry {
    private final String id;
    private final String shiftId;
    private final String vehicleId;
    private final String userId;
    private final int km;
    private final String timestamp;
    private Boolean confirmedByDriver;
    private Boolean confirmedByAdmin;

    public KilometerEntry(String id, String shiftId, String vehicleId, int km, String userId,
                          String timestamp, Boolean confirmedByDriver, Boolean confirmedByAdmin) {
        this.id = id;
        this.shiftId = shiftId;
        this.vehicleId = vehicleId;
        this.km = km;
        this.userId = userId;
        this.timestamp = timestamp;
        this.confirmedByDriver = confirmedByDriver;
        this.confirmedByAdmin = confirmedByAdmin;
    }

    public KilometerEntry(String shiftId, String vehicleId, int km, String userId) {
        this.id = UUID.randomUUID().toString();
        this.shiftId = shiftId;
        this.vehicleId = vehicleId;
        this.km = km;
        this.userId = userId;
        this.timestamp = LocalDateTime.now().toString();
        this.confirmedByDriver = false;
        this.confirmedByAdmin = false;
    }

    public static KilometerEntry fromResultSet(ResultSet rs) throws Exception {
        return new KilometerEntry(
                rs.getString("id"),
                rs.getString("shift_id"),
                rs.getString("vehicle_id"),
                rs.getInt("km"),
                rs.getString("user_id"),
                rs.getString("timestamp"),
                rs.getObject("confirmed_by_driver", Boolean.class),
                rs.getObject("confirmed_by_admin", Boolean.class)
        );
    }

    public static KilometerEntry createEntry(String shiftId, String vehicleId, int km, String userId) {
        String id = UUID.randomUUID().toString();
        String now = Timestamp.valueOf(java.time.LocalDateTime.now()).toString();

        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            // Insert into KilometerEntry
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO KilometerEntry (id, shift_id, vehicle_id, km, user_id,  timestamp, confirmed_by_driver, confirmed_by_admin) " +
                            "VALUES (?, ?, ?, ?, ?, ?, NULL, NULL)"
            );
            stmt.setString(1, id);
            stmt.setString(2, shiftId);
            stmt.setString(3, vehicleId);
            stmt.setInt(4, km);
            stmt.setString(5, userId);
            stmt.setString(6, now);
            stmt.executeUpdate();

            // Check current_kilometer in Vehicle
            PreparedStatement selectStmt = conn.prepareStatement(
                    "SELECT current_kilometer FROM Vehicle WHERE id = ?"
            );
            selectStmt.setString(1, vehicleId);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                int currentKm = rs.getInt("current_kilometer");
                if (currentKm != km) {
                    PreparedStatement updateStmt = conn.prepareStatement(
                            "UPDATE Vehicle SET current_kilometer = ? WHERE id = ?"
                    );
                    updateStmt.setInt(1, km);
                    updateStmt.setString(2, vehicleId);
                    updateStmt.executeUpdate();
                }
            }

            conn.commit(); // Commit both insert and update

            return new KilometerEntry(id, shiftId, vehicleId, km, userId,  now, null, null);

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
