package edu.pidev3a32.controllers;

import edu.pidev3a32.entities.Tournament;
import edu.pidev3a32.entities.Game;
import edu.pidev3a32.services.TournamentService;
import edu.pidev3a32.services.GameService;
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
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import static edu.pidev3a32.tools.Toast.showToast;
import static edu.pidev3a32.tools.Toast.showWarning;

public class CreateGameController {

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnCreateGame;

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
    private final ValidationSupport validationSupport = new ValidationSupport();

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

        cbTournament.setOnAction(e -> {
            Tournament selected = cbTournament.getValue();
            if (selected != null) {
                cbTeam1.getItems().clear();
                cbTeam2.getItems().clear();
                cbTeam1.getItems().addAll(selected.getTeams());
                cbTeam2.getItems().addAll(selected.getTeams());
            }
        });

        validationSupport.registerValidator(cbTournament,
                Validator.createEmptyValidator("Tournament is required"));

        validationSupport.registerValidator(cbTeam1,
                Validator.createEmptyValidator("Team 1 is required"));

        validationSupport.registerValidator(cbTeam2,
                Validator.createEmptyValidator("Team 2 is required"));

        validationSupport.registerValidator(cbTeam2, (control, value) -> {
            String team1 = cbTeam1.getValue();
            String team2 = cbTeam2.getValue();
            if (team1 != null && team2 != null && team1.equals(team2)) {
                return org.controlsfx.validation.ValidationResult.fromError(control,
                        "A game cannot have the same team twice!");
            }
            return null;
        });

        validationSupport.registerValidator(dpDate, (control, value) -> {
            if (value == null) {
                return org.controlsfx.validation.ValidationResult.fromError(control,
                        "Date is required");
            }
            if (!(value instanceof LocalDate)) {
                return org.controlsfx.validation.ValidationResult.fromError(control,
                        "Invalid date format");
            }
            LocalDate date = (LocalDate) value;
            Tournament t = cbTournament.getValue();
            if (t != null) {
                if (date.isBefore(t.getStartDate()) || date.isAfter(t.getEndDate())) {
                    return org.controlsfx.validation.ValidationResult.fromError(control,
                            "Date must be between " + t.getStartDate() + " and " + t.getEndDate());
                }
            }
            return null;
        });
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
            e.printStackTrace();
            showWarning("Error navigating to Game List: " + e.getMessage());
        }
    }

    @FXML
    void submit(ActionEvent event) {
        if (validationSupport.isInvalid()) {
            showWarning("Please fix validation errors before submitting.");
            validationSupport.getValidationResult().getErrors().forEach(error ->
                    showWarning(error.getText()));
            return;
        }

        try {
            Tournament tournament = cbTournament.getValue();
            String team1 = cbTeam1.getValue();
            String team2 = cbTeam2.getValue();
            LocalDate date = dpDate.getValue();

            Game g = new Game(date, team1, team2, tournament);
            gameService.addEntity(g);
            showToast("Game created successfully!");
            goToGameList(event);
        } catch (SQLException e) {
            showWarning("Error creating game: " + e.getMessage());
        }
    }
}