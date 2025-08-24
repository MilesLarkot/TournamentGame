package edu.pidev3a32.controllers;

import edu.pidev3a32.services.GameService;
import edu.pidev3a32.services.TournamentService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class BackHomeController {

    @FXML
    private Button btnAddGame;

    @FXML
    private Button btnAddTournament;

    @FXML
    private ListView<?> lvLogs;

    @FXML
    private Text txtOngoingTournaments;

    @FXML
    private Text txtTotalGames;

    @FXML
    private Text txtTotalOngoingGames;

    @FXML
    private Text txtTotalTournaments;

    @FXML
    void goToCreateGame(ActionEvent event) {
        navigateTo(event, "/BackOffice/CreateGame.fxml");
    }

    @FXML
    void goToCreateTournament(ActionEvent event) {
        navigateTo(event, "/BackOffice/CreateTournament.fxml");
    }

    private void navigateTo(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        TournamentService tournamentService = new TournamentService();
        GameService gameService = new GameService();

        int totalTournaments = tournamentService.getAllData().size();
        int ongoingTournaments = (int) tournamentService.getAllData().stream()
                .filter(t -> LocalDate.now().isAfter(t.getStartDate()) && LocalDate.now().isBefore(t.getEndDate()))
                .count();

        int totalGames = gameService.getAllData().size();
        int ongoingGames = (int) gameService.getAllData().stream()
                .filter(g -> g.getDate().equals(LocalDate.now()))
                .count();

        txtTotalTournaments.setText(String.valueOf(totalTournaments));
        txtOngoingTournaments.setText(String.valueOf(ongoingTournaments));
        txtTotalGames.setText(String.valueOf(totalGames));
        txtTotalOngoingGames.setText(String.valueOf(ongoingGames));
    }


}
