package com.chauffeursync.models;

import java.sql.ResultSet;

public class MaintenanceTask {
    private final String id;
    private final String vehicleId;
    private final String garageId;
    private final String scheduledDate;
    private final String status;
    private final String actionTaken;
    private final String updatedAt;

    public MaintenanceTask(String id, String vehicleId, String garageId, String scheduledDate,
                           String status, String actionTaken, String updatedAt) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.garageId = garageId;
        this.scheduledDate = scheduledDate;
        this.status = status;
        this.actionTaken = actionTaken;
        this.updatedAt = updatedAt;
    }

    public static MaintenanceTask fromResultSet(ResultSet rs) throws Exception {
        return new MaintenanceTask(
                rs.getString("id"),
                rs.getString("vehicle_id"),
                rs.getString("garage_id"),
                rs.getString("scheduled_date"),
                rs.getString("status"),
                rs.getString("action_taken"),
                rs.getString("updated_at")
        );
    }
}
