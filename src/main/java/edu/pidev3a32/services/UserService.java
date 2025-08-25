package edu.pidev3a32.services;

import edu.pidev3a32.entities.Logger;
import edu.pidev3a32.entities.User;
import edu.pidev3a32.interfaces.IService;
import edu.pidev3a32.tools.MyConnection;
import edu.pidev3a32.tools.SessionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static edu.pidev3a32.tools.Toast.showToast;
import static edu.pidev3a32.tools.Toast.showWarning;

public class UserService implements IService<User> {

    private final Connection cnx;

    LoggerService loggerService =  new LoggerService();

    public UserService() {
        cnx = MyConnection.getInstance().getCnx();
    }

    @Override
    public void addEntity(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.executeUpdate();
            loggerService.logAction(new Logger(SessionManager.getCurrentUser().getId(), "CREATE_USER", "Created user id: " + user.getId()));
            showToast("User created!");
        }
    }

    @Override
    public void deleteEntity(User user) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, user.getId());
            ps.executeUpdate();
            loggerService.logAction(new Logger(SessionManager.getCurrentUser().getId(), "DELETE_USER", "Deleted user id: " + user.getId()));
            showToast("User deleted!");
        } catch (SQLException e) {
            showWarning(e.getMessage());
        }
    }

    @Override
    public void updateEntity(int id, User user) {
        String sql = "UPDATE users SET username = ?, password = ?, role = ? WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.setInt(4, id);
            ps.executeUpdate();
            loggerService.logAction(new Logger(SessionManager.getCurrentUser().getId(), "UPDATE_USER", "Updated user id: " + user.getId()));
            showToast("User updated!");
        } catch (SQLException e) {
            showWarning(e.getMessage());
        }
    }

    @Override
    public List<User> getAllData() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User authenticate(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("role"));
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
        }
        return null;
    }

}
