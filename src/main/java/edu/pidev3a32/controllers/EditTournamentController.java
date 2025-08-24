package edu.pidev3a32.controllers;

import edu.pidev3a32.entities.Tournament;
import edu.pidev3a32.services.TournamentService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import static edu.pidev3a32.tools.Toast.showToast;
import static edu.pidev3a32.tools.Toast.showWarning;

public class EditTournamentController {

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnGenerateTeamName;

    @FXML
    private Button btnAddTeam;

    @FXML
    private TextField tfName;

    @FXML
    private DatePicker dpStart;

    @FXML
    private DatePicker dpEnd;

    @FXML
    private TextField tfTeamName;

    @FXML
    private ListView<String> lvTeams;

    private final TournamentService tournamentService = new TournamentService();
    private ValidationSupport validationSupport;

    private Tournament currentTournament;

    @FXML
    public void initialize() {
        validationSupport = new ValidationSupport();
        validationSupport.registerValidator(tfName, true, Validator.createEmptyValidator("Tournament name required."));
        validationSupport.registerValidator(dpStart, true, Validator.createEmptyValidator("Start date is required"));
        validationSupport.registerValidator(dpEnd, true, Validator.createEmptyValidator("End date is required"));

        lvTeams.setCellFactory(param -> new ListCell<>() {
            private final Button btnDelete = new Button("Delete");
            private final Label lblName = new Label();
            private final HBox container = new HBox(10, lblName, btnDelete);

            {
                btnDelete.setOnAction(e -> {
                    String team = getItem();
                    if (team != null) {
                        lvTeams.getItems().remove(team);
                    }
                });
            }

            @Override
            protected void updateItem(String team, boolean empty) {
                super.updateItem(team, empty);
                if (empty || team == null) {
                    setGraphic(null);
                } else {
                    lblName.setText(team);
                    setGraphic(container);
                }
            }
        });
    }

    public void setTournament(Tournament t) {
        this.currentTournament = t;

        tfName.setText(t.getName());
        dpStart.setValue(t.getStartDate());
        dpEnd.setValue(t.getEndDate());

        lvTeams.getItems().clear();
        if (t.getTeams() != null) {
            lvTeams.getItems().addAll(t.getTeams());
        }
    }

    @FXML
    void generateTeamName(ActionEvent event) {
        String randomTeam = tournamentService.fetchTeamName();
        if (randomTeam != null && !randomTeam.isEmpty()) {
            tfTeamName.setText(randomTeam);
        } else {
            showWarning("Could not generate team name.");
        }
    }

    @FXML
    void addTeam(ActionEvent event) {
        String teamName = tfTeamName.getText().trim();
        if (teamName.isEmpty()) {
            showWarning("Team name cannot be empty!");
            return;
        }
        lvTeams.getItems().add(teamName);
        tfTeamName.clear();
    }

    @FXML
    void updateTournament(ActionEvent event) {
        if (validationSupport.isInvalid()) {
            showWarning("Please fix the validation errors before submitting!");
            return;
        }

        String name = tfName.getText();
        LocalDate start = dpStart.getValue();
        LocalDate end = dpEnd.getValue();

        if (end.isBefore(start)) {
            showWarning("End date cannot be before start date!");
            return;
        }

        currentTournament.setName(name);
        currentTournament.setStartDate(start);
        currentTournament.setEndDate(end);

        currentTournament.setTeams(new ArrayList<>(lvTeams.getItems()));

        tournamentService.updateEntity(currentTournament.getId(), currentTournament);

        showToast("Tournament updated successfully!");
        goToTournamentList(event);
    }

    @FXML
    void goToTournamentList(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BackOffice/TournamentList.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showWarning("Error navigating to Tournament List: " + e.getMessage());
        }
    }
}
