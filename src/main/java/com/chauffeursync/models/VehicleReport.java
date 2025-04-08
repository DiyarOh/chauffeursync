package com.chauffeursync.models;

import java.sql.ResultSet;

public class VehicleReport {
    private final String id;
    private final String vehicleId;
    private final String period;
    private final int totalKilometers;
    private final String generatedAt;

    public VehicleReport(String id, String vehicleId, String period, int totalKilometers, String generatedAt) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.period = period;
        this.totalKilometers = totalKilometers;
        this.generatedAt = generatedAt;
    }

    public static VehicleReport fromResultSet(ResultSet rs) throws Exception {
        return new VehicleReport(
                rs.getString("id"),
                rs.getString("vehicle_id"),
                rs.getString("period"),
                rs.getInt("total_kilometers"),
                rs.getString("generated_at")
        );
    }

    public String getId() { return id; }
    public String getVehicleId() { return vehicleId; }
    public String getPeriod() { return period; }
    public int getTotalKilometers() { return totalKilometers; }
    public String getGeneratedAt() { return generatedAt; }
}
