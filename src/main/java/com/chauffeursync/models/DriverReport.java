package com.chauffeursync.models;

import java.sql.ResultSet;

public class DriverReport {
    private final String id;
    private final String userId;
    private final String period;
    private final float totalHours;

    public DriverReport(String id, String userId, String period, float totalHours) {
        this.id = id;
        this.userId = userId;
        this.period = period;
        this.totalHours = totalHours;
    }

    public static DriverReport fromResultSet(ResultSet rs) throws Exception {
        return new DriverReport(
                rs.getString("id"),
                rs.getString("user_id"),
                rs.getString("period"),
                rs.getFloat("total_hours")
        );
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getPeriod() {
        return period;
    }

    public float getTotalHours() {
        return totalHours;
    }
}
