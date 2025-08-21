package edu.pidev3a32.services;

import edu.pidev3a32.entities.Game;
import edu.pidev3a32.entities.Tournament;
import edu.pidev3a32.interfaces.IService;
import edu.pidev3a32.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class TournamentService implements IService<Tournament> {

    private Connection cnx;

    public TournamentService() {
        cnx = MyConnection.getInstance().getCnx();
    }

    @Override
    public void addEntity(Tournament t) throws SQLException {
        String req = "INSERT INTO tournament(name, start_date, end_date) VALUES (?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, t.getName());
            ps.setDate(2, Date.valueOf(t.getStartDate()));
            ps.setDate(3, Date.valueOf(t.getEndDate()));
            ps.executeUpdate();
            System.out.println("Tournament added!");
        }
    }

    @Override
    public void deleteEntity(Tournament t) {
        String req = "DELETE FROM tournament WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, t.getId());
            ps.executeUpdate();
            System.out.println("Tournament deleted!");
        } catch (SQLException e) {
            System.out.println("Error deleting tournament: " + e.getMessage());
        }
    }

    @Override
    public void updateEntity(int id, Tournament t) {
        String req = "UPDATE tournament SET name=?, start_date=?, end_date=? WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, t.getName());
            ps.setDate(2, Date.valueOf(t.getStartDate()));
            ps.setDate(3, Date.valueOf(t.getEndDate()));
            ps.setInt(4, id);
            ps.executeUpdate();
            System.out.println("Tournament updated!");
        } catch (SQLException e) {
            System.out.println("Error updating tournament: " + e.getMessage());
        }
    }

    @Override
    public List<Tournament> getAllData() {
        List<Tournament> list = new ArrayList<>();
        String req = "SELECT * FROM tournament";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(req)) {

            while (rs.next()) {
                Tournament t = new Tournament(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate()
                );

                String gameReq = "SELECT * FROM game WHERE tournament_id = ?";
                try (PreparedStatement ps = cnx.prepareStatement(gameReq)) {
                    ps.setInt(1, t.getId());
                    ResultSet gameRs = ps.executeQuery();
                    while (gameRs.next()) {
                        Game g = new Game(
                                gameRs.getInt("id"),
                                gameRs.getDate("date").toLocalDate(),
                                gameRs.getString("team1"),
                                gameRs.getString("team2"),
                                gameRs.getString("score")
                        );
                        g.setTournament(t);
                        t.addGame(g);
                    }
                }
                list.add(t);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching tournaments: " + e.getMessage());
        }
        return list;
    }

}