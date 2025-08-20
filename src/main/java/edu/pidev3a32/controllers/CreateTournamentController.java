package edu.pidev3a32.controllers;

import edu.pidev3a32.entities.Tournament;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.time.LocalDate;

import edu.pidev3a32.services.TournamentService;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import static edu.pidev3a32.tools.Toast.showToast;
import static edu.pidev3a32.tools.Toast.showWarning;

public class CreateTournamentController {

    @FXML
    private Button btnCreate;

    @FXML
    private DatePicker dpEnd;

    @FXML
    private DatePicker dpStart;

    @FXML
    private TextField tfName;

    TournamentService tournamentService = new TournamentService();
    private ValidationSupport validationSupport;

    @FXML
    public void initialize() {
        validationSupport = new ValidationSupport();
        validationSupport.registerValidator(tfName, true, Validator.createEmptyValidator("Tournament name required."));
        validationSupport.registerValidator(dpStart, true,
                Validator.createEmptyValidator("Start date is required"));
        validationSupport.registerValidator(dpEnd, true,
                Validator.createEmptyValidator("End date is required"));
    }

    @FXML
    void createTournament(ActionEvent event) {

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


            Tournament t = new Tournament(name, start, end);
            try {
                tournamentService.addEntity(t);
                showToast("Tournament created successfully!");
                clearForm();
            } catch (SQLException ex) {
                showWarning("Error creating tournament: " + ex.getMessage());
            }
    }

    private void clearForm() {
        tfName.clear();
        dpStart.setValue(null);
        dpEnd.setValue(null);
    }

}
