package edu.pidev3a32.services;

import edu.pidev3a32.entities.Logger;
import edu.pidev3a32.entities.Tournament;
import edu.pidev3a32.interfaces.IService;
import edu.pidev3a32.entities.Game;
import edu.pidev3a32.tools.MyConnection;
import edu.pidev3a32.tools.SessionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static edu.pidev3a32.tools.Toast.showToast;
import static edu.pidev3a32.tools.Toast.showWarning;

public class GameService implements IService<Game> {
    private Connection cnx;

    public GameService() {
        cnx = MyConnection.getInstance().getCnx();
    }

    LoggerService loggerService = new LoggerService();

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
            loggerService.logAction(new Logger(SessionManager.getCurrentUser().getId(), "ADD_GAME", "created game id: " + g.getId()));
            showToast("Game added!");
        }
    }

    @Override
    public void deleteEntity(Game g) {
        String req = "DELETE FROM game WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, g.getId());
            ps.executeUpdate();
            loggerService.logAction(new Logger(SessionManager.getCurrentUser().getId(), "DELETE_GAME", "Deleted game id: " + g.getId()));
            showToast("Game deleted!");
        } catch (SQLException e) {
            showWarning("Error deleting game: " + e.getMessage());
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
            loggerService.logAction(new Logger(SessionManager.getCurrentUser().getId(), "UPDATE_GAME", "Updated game id: " + id));
            System.out.println("Game updated!");
        } catch (SQLException e) {
            System.out.println("Error updating game: " + e.getMessage());
        }
    }

    @Override
    public List<Game> getAllData() {
        List<Game> list = new ArrayList<>();
        String req = """
        SELECT g.id, g.date, g.team1, g.team2, g.score,
               t.id AS t_id, t.name AS t_name, t.start_date, t.end_date
        FROM game g
        LEFT JOIN tournament t ON g.tournament_id = t.id
    """;

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(req)) {

            while (rs.next()) {
                Tournament tournament = null;
                int tId = rs.getInt("t_id");
                if (!rs.wasNull()) {
                    tournament = new Tournament(
                            tId,
                            rs.getString("t_name"),
                            rs.getDate("start_date").toLocalDate(),
                            rs.getDate("end_date").toLocalDate()
                    );
                }

                // Build Game
                Game g = new Game(
                        rs.getInt("id"),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("team1"),
                        rs.getString("team2"),
                        rs.getString("score")
                );
                g.setTournament(tournament);

                list.add(g);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching games: " + e.getMessage());
        }
        return list;
    }

}
