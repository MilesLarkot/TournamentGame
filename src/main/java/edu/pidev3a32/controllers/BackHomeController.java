package edu.pidev3a32.controllers;

import edu.pidev3a32.entities.Logger;
import edu.pidev3a32.services.GameService;
import edu.pidev3a32.services.LoggerService;
import edu.pidev3a32.services.TournamentService;
import javafx.collections.FXCollections;
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
import java.time.format.DateTimeFormatter;

public class BackHomeController {

    @FXML
    private ListView<Logger> lvLogs;

    @FXML
    private Text txtOngoingTournaments;

    @FXML
    private Text txtTotalGames;

    @FXML
    private Text txtTotalOngoingGames;

    @FXML
    private Text txtTotalTournaments;

    private final LoggerService loggerService = new LoggerService();

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

        loadLogs();
    }

    private void loadLogs() {
        var logs = loggerService.getLogs();
        lvLogs.setItems(FXCollections.observableArrayList(logs));

        lvLogs.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Logger log, boolean empty) {
                super.updateItem(log, empty);
                if (empty || log == null) {
                    setText(null);
                } else {
                    String time = log.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    setText("[" + time + "] " + log.getAction());
                }
            }
        });

        lvLogs.setOnMouseClicked(event -> {
                Logger selectedLog = lvLogs.getSelectionModel().getSelectedItem();
                if (selectedLog != null) {
                    try {
                        goToLogDetails(selectedLog, event);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

        });
    }

    private void goToLogDetails(Logger log, javafx.scene.input.MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/BackOffice/LogDetails.fxml"));
        Parent root = loader.load();

        LogDetailsController controller = loader.getController();
        controller.setLog(log);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
