package edu.pidev3a32.services;

import edu.pidev3a32.entities.Game;
import edu.pidev3a32.entities.Tournament;
import edu.pidev3a32.interfaces.IService;
import edu.pidev3a32.tools.MyConnection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TournamentService implements IService<Tournament> {

    private final Connection cnx;

    public TournamentService() {
        cnx = MyConnection.getInstance().getCnx();
    }

    private String parseTeamName(String json) {
        json = json.trim();

        if (json.startsWith("{") && json.endsWith("}")) {
            json = json.substring(1, json.length() - 1);
        }

        String[] parts = json.split(":", 2);
        if (parts.length == 2) {
            return parts[1].trim().replaceAll("^\"|\"$", "");
        }
        return null;
    }

    public String fetchTeamName() {
        try {
            URL url = new URL("https://team-name-generator-two.vercel.app/api/teams");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder content = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            conn.disconnect();

            return parseTeamName(content.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void addEntity(Tournament t) throws SQLException {
        String req = "INSERT INTO tournament(name, start_date, end_date) VALUES (?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, t.getName());
            ps.setDate(2, Date.valueOf(t.getStartDate()));
            ps.setDate(3, Date.valueOf(t.getEndDate()));
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                int tournamentId = keys.getInt(1);

                insertTeams(tournamentId, t.getTeams());
            }
        }
    }

    @Override
    public void deleteEntity(Tournament t) {
        String deleteTeams = "DELETE FROM tournament_team WHERE tournament_id=?";
        String deleteTournament = "DELETE FROM tournament WHERE id=?";
        try {
            // First delete dependent rows
            try (PreparedStatement ps = cnx.prepareStatement(deleteTeams)) {
                ps.setInt(1, t.getId());
                ps.executeUpdate();
            }
            // Then delete the tournament itself
            try (PreparedStatement ps = cnx.prepareStatement(deleteTournament)) {
                ps.setInt(1, t.getId());
                ps.executeUpdate();
            }
            System.out.println("Tournament and teams deleted!");
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

            try (PreparedStatement delPs = cnx.prepareStatement("DELETE FROM tournament_team WHERE tournament_id=?")) {
                delPs.setInt(1, id);
                delPs.executeUpdate();
            }

            insertTeams(id, t.getTeams());

            System.out.println("Tournament updated with teams!");
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

                String teamReq = "SELECT team_name FROM tournament_team WHERE tournament_id = ?";
                try (PreparedStatement ps = cnx.prepareStatement(teamReq)) {
                    ps.setInt(1, t.getId());
                    ResultSet teamRs = ps.executeQuery();
                    while (teamRs.next()) {
                        t.addTeam(teamRs.getString("team_name"));
                    }
                }

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

    private void insertTeams(int tournamentId, List<String> teams) throws SQLException {
        if (teams == null || teams.isEmpty()) return;

        String teamReq = "INSERT INTO tournament_team(tournament_id, team_name) VALUES (?, ?)";
        try (PreparedStatement teamPs = cnx.prepareStatement(teamReq)) {
            for (String team : teams) {
                teamPs.setInt(1, tournamentId);
                teamPs.setString(2, team);
                teamPs.executeUpdate();
            }
        }
    }
}
