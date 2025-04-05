package com.chauffeursync.models;

import java.sql.ResultSet;

public class DamageReport {
    private final String id;
    private final String userId;
    private final String vehicleId;
    private final String description;

    public DamageReport(String id, String userId, String vehicleId, String description) {
        this.id = id;
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.description = description;
    }

    public static DamageReport fromResultSet(ResultSet rs) throws Exception {
        return new DamageReport(
                rs.getString("id"),
                rs.getString("user_id"),
                rs.getString("vehicle_id"),
                rs.getString("description")
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

    public String getDescription() {
        return description;
    }
}
