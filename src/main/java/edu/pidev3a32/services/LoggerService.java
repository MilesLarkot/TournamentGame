package edu.pidev3a32.services;

import edu.pidev3a32.entities.Logger;
import edu.pidev3a32.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoggerService {

    private final Connection conn;

    public LoggerService() {
        conn = MyConnection.getInstance().getCnx();
    }

    public void logAction(Logger logger) {
        String sql = "INSERT INTO logger (user_id, action, details, timestamp) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, logger.getUserId());
            stmt.setString(2, logger.getAction());
            stmt.setString(3, logger.getDetails());
            stmt.setTimestamp(4, Timestamp.valueOf(logger.getTimestamp()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Logger> getLogs() {
        List<Logger> logs = new ArrayList<>();
        String sql = "SELECT * FROM logger ORDER BY timestamp DESC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                logs.add(new Logger(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("action"),
                        rs.getString("details"),
                        rs.getTimestamp("timestamp").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }
}
