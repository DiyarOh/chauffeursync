package com.chauffeursync.models;

import com.chauffeursync.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Garage {

    private final String id;
    private final String title;
    private final String address;

    public Garage(String id, String title, String address) {
        this.id = id;
        this.title = title;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public List<MaintenanceTask> getMaintenanceTasks() {
        List<MaintenanceTask> tasks = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM MaintenanceTask WHERE garage_id = ?");
            stmt.setString(1, this.id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tasks.add(MaintenanceTask.fromResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public static Garage fromResultSet(ResultSet rs) throws Exception {
        return new Garage(
                rs.getString("id"),
                rs.getString("title"),
                rs.getString("address")
        );
    }
}
