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

    @FXML
    void createTournament(ActionEvent event) {

            String name = tfName.getText();
            LocalDate start = dpStart.getValue();
            LocalDate end = dpEnd.getValue();

            if (name.isEmpty() || start == null || end == null) {
                System.out.println("Please fill all fields!");
                return;
            }

            Tournament t = new Tournament(name, start, end);
            try {
                tournamentService.addEntity(t);
                System.out.println("Tournament created successfully!");
            } catch (SQLException ex) {
                System.out.println("Error creating tournament: " + ex.getMessage());
            }

    }

}
