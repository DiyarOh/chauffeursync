package com.chauffeursync.models;

import java.sql.ResultSet;

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
}
