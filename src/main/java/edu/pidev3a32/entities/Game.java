package edu.pidev3a32.entities;

import java.time.LocalDate;

public class Game {
    private int id;
    private LocalDate date;
    private String team1;
    private String team2;
    private String score;
    private Tournament tournament;

    public Game(int id, LocalDate date, String team1, String team2, String score) {
        this.id = id;
        this.date = date;
        this.team1 = team1;
        this.team2 = team2;
        this.score = score;
    }

    public Game(LocalDate date, String team1, String team2, String score) {
        this.date = date;
        this.team1 = team1;
        this.team2 = team2;
        this.score = score;
    }

    public Game(LocalDate date, String team1, String team2, Tournament tournament) {
        this.date = date;
        this.team1 = team1;
        this.team2 = team2;
        this.tournament = tournament;
        this.score = "";
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    @Override
    public String toString() {
        return "Game{" +
                "team1='" + team1 + '\'' +
                ", id=" + id +
                ", date=" + date +
                ", team2='" + team2 + '\'' +
                ", score='" + score + '\'' +
                ", tournament=" + tournament.getId() +
                '}';
    }

    public int getId() {
        return id;
    }
}