package edu.pidev3a32.services;

import edu.pidev3a32.interfaces.IService;
import edu.pidev3a32.entities.Game;
import edu.pidev3a32.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameService implements IService<Game> {
    private Connection cnx;

    public GameService() {
        cnx = MyConnection.getInstance().getCnx();
    }

    @Override
    public void addEntity(Game g) throws SQLException {
        String req = "INSERT INTO game(date, team1, team2, score, tournament_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setDate(1, Date.valueOf(g.getDate()));
            ps.setString(2, g.getTeam1());
            ps.setString(3, g.getTeam2());
            ps.setString(4, g.getScore());

            if (g.getTournament() != null) {
                ps.setInt(5, g.getTournament().getId());
            } else {
                ps.setNull(5, Types.INTEGER);
            }

            ps.executeUpdate();
            System.out.println("Game added!");
        }
    }

    @Override
    public void deleteEntity(Game g) {
        String req = "DELETE FROM game WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, g.getId());
            ps.executeUpdate();
            System.out.println("Game deleted!");
        } catch (SQLException e) {
            System.out.println("Error deleting game: " + e.getMessage());
        }
    }

    @Override
    public void updateEntity(int id, Game g) {
        String req = "UPDATE game SET date=?, team1=?, team2=?, score=?, tournament_id=? WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setDate(1, Date.valueOf(g.getDate()));
            ps.setString(2, g.getTeam1());
            ps.setString(3, g.getTeam2());
            ps.setString(4, g.getScore());

            if (g.getTournament() != null) {
                ps.setInt(5, g.getTournament().getId());
            } else {
                ps.setNull(5, Types.INTEGER);
            }

            ps.setInt(6, id);
            ps.executeUpdate();
            System.out.println("Game updated!");
        } catch (SQLException e) {
            System.out.println("Error updating game: " + e.getMessage());
        }
    }

    @Override
    public List<Game> getAllData() {
        List<Game> list = new ArrayList<>();
        String req = "SELECT * FROM game";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                Game g = new Game(
                        rs.getInt("id"),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("team1"),
                        rs.getString("team2"),
                        rs.getString("score")
                );
                list.add(g);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching games: " + e.getMessage());
        }
        return list;
    }
}
