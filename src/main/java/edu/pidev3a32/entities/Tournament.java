package edu.pidev3a32.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Tournament {
    private int id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Game> games;
    private List<String> teams;

    public Tournament(int id, String name, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.games = new ArrayList<>();
        this.teams = new ArrayList<>();
    }

    public Tournament(String name, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.games = new ArrayList<>();
        this.teams = new ArrayList<>();
    }



    public void addGame(Game game) {
        games.add(game);
        game.setTournament(this);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }


    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public List<Game> getGames() { return games; }

    public List<String> getTeams() {
        return teams;
    }

    public void setTeams(List<String> teams) {
        this.teams = teams;
    }

    public void addTeam(String team) {
        this.teams.add(team);
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    @Override
    public String toString() {
        return "Tournament{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", games=" + games +
                ", teams=" + teams +
                '}';
    }
}