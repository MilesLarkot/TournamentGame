package edu.pidev3a32.controllers;

import edu.pidev3a32.entities.Game;
import edu.pidev3a32.entities.Tournament;
import edu.pidev3a32.services.GameService;
import edu.pidev3a32.services.TournamentService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import static edu.pidev3a32.tools.Toast.showToast;
import static edu.pidev3a32.tools.Toast.showWarning;

public class EditGameController {

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnUpdateGame;

    @FXML
    private ComboBox<String> cbTeam1;

    @FXML
    private ComboBox<String> cbTeam2;

    @FXML
    private ComboBox<Tournament> cbTournament;

    @FXML
    private DatePicker dpDate;

    private final TournamentService tournamentService = new TournamentService();
    private final GameService gameService = new GameService();
    private Game currentGame;

    @FXML
    public void initialize() {
        cbTournament.getItems().addAll(tournamentService.getAllData());

        cbTournament.setCellFactory(param -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Tournament item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        cbTournament.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Tournament tournament) {
                return tournament == null ? "" : tournament.getName();
            }

            @Override
            public Tournament fromString(String string) {
                return cbTournament.getItems().stream()
                        .filter(t -> t.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        cbTournament.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cbTeam1.getItems().setAll(newVal.getTeams());
                cbTeam2.getItems().setAll(newVal.getTeams());
            } else {
                cbTeam1.getItems().clear();
                cbTeam2.getItems().clear();
            }
        });
    }

    public void setGame(Game game) {
        this.currentGame = game;

        dpDate.setValue(game.getDate());
        cbTournament.setValue(game.getTournament());

        Tournament t = game.getTournament();
        if (t != null) {
            cbTeam1.getItems().setAll(t.getTeams());
            cbTeam2.getItems().setAll(t.getTeams());
        }

        cbTeam1.setValue(game.getTeam1());
        cbTeam2.setValue(game.getTeam2());
    }

    @FXML
    void goToGameList(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BackOffice/GameList.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showWarning("Error navigating to Game List: " + e.getMessage());
        }
    }

    @FXML
    void submit(ActionEvent event) {
        Tournament tournament = cbTournament.getValue();
        String team1 = cbTeam1.getValue();
        String team2 = cbTeam2.getValue();
        LocalDate date = dpDate.getValue();

        if (tournament == null || team1 == null || team2 == null || date == null) {
            showWarning("Please fill all fields!");
            return;
        }

        if (team1.equals(team2)) {
            showWarning("A game cannot have the same team twice!");
            return;
        }

        currentGame.setDate(date);
        currentGame.setTeam1(team1);
        currentGame.setTeam2(team2);
        currentGame.setTournament(tournament);

        gameService.updateEntity(currentGame.getId(), currentGame);
        showToast("Game updated successfully!");
        goToGameList(event);
    }
}
