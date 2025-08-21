package edu.pidev3a32.controllers;

import edu.pidev3a32.entities.Game;
import edu.pidev3a32.entities.Tournament;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class FrontTournamentDetailsController {

    @FXML
    private ListView<Game> lvGames;

    @FXML
    private Text txtDateRange;

    @FXML
    private Text txtNbGames;

    @FXML
    private Text txtStatus;

    @FXML
    private Text txtTitle;

    private Tournament tournament;

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
        displayTournamentDetails();
    }

    private void displayTournamentDetails() {
        if (tournament == null) return;

        txtTitle.setText(tournament.getName());
        txtDateRange.setText(
                tournament.getStartDate() + " → " + tournament.getEndDate()
        );
        txtNbGames.setText("Games: " + tournament.getGames().size());
        txtStatus.setText(getTournamentStatus());

        lvGames.getItems().setAll(tournament.getGames());

        lvGames.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Game game, boolean empty) {
                super.updateItem(game, empty);
                if (empty || game == null) {
                    setText(null);
                } else {
                    setText(game.getTeam1() + " vs " + game.getTeam2()
                            + " | " + game.getDate()
                            + " | " + game.getScore());
                }
            }
        });
    }

    private String getTournamentStatus() {
        LocalDate now = LocalDate.now();
        if (now.isBefore(tournament.getStartDate())) return "Upcoming";
        if (now.isAfter(tournament.getEndDate())) return "Finished";
        return "Ongoing";
    }

    @FXML
    void goToFrontOfficeTournamentList(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontOffice/TournamentList.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}
