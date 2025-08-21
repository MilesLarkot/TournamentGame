package edu.pidev3a32.controllers;

import edu.pidev3a32.entities.Game;
import edu.pidev3a32.entities.Tournament;
import edu.pidev3a32.services.TournamentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class FrontTournamentListController {

    @FXML
    private ListView<Tournament> lvTournaments;

    @FXML
    private TextField tfSearch;

    private TournamentService tournamentService = new TournamentService();

    private ContextMenu suggestions = new ContextMenu();

    @FXML
    private void initialize() {
        loadTournaments();

        lvTournaments.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Tournament t, boolean empty) {
                super.updateItem(t, empty);

                if (empty || t == null) {
                    setGraphic(null);
                } else {
                    Label nameLabel = new Label(t.getName());
                    Label startLabel = new Label(t.getStartDate().toString());
                    Label endLabel = new Label(t.getEndDate().toString());


                    HBox row = new HBox(10, nameLabel, startLabel, endLabel);
                    row.setFillHeight(true);

                    nameLabel.setMaxWidth(Double.MAX_VALUE);
                    startLabel.setMaxWidth(Double.MAX_VALUE);
                    endLabel.setMaxWidth(Double.MAX_VALUE);
                    HBox.setHgrow(nameLabel, javafx.scene.layout.Priority.ALWAYS);
                    HBox.setHgrow(startLabel, javafx.scene.layout.Priority.ALWAYS);
                    HBox.setHgrow(endLabel, javafx.scene.layout.Priority.ALWAYS);

                    setGraphic(row);
                }
            }
        });



        tfSearch.textProperty().addListener((obs, oldVal, newVal) -> {
            filterList(newVal);
            showSuggestions(newVal);
        });

        lvTournaments.setOnMouseClicked(event -> {
            Tournament selected = lvTournaments.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    goToTournamentDetails(selected);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @FXML
    private void goToTournamentDetails(Tournament selectedTournament) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontOffice/TournamentDetails.fxml"));
        Parent root = loader.load();

        FrontTournamentDetailsController controller = loader.getController();
        controller.setTournament(selectedTournament);

        Stage stage = (Stage) lvTournaments.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    private void loadTournaments() {
        List<Tournament> tournaments = tournamentService.getAllData();
        lvTournaments.setItems(FXCollections.observableArrayList(tournaments));
    }


    private void filterList(String filter) {
        if (filter == null || filter.isEmpty()) {
            lvTournaments.setItems(FXCollections.observableArrayList(tournamentService.getAllData()));
            return;
        }
        ObservableList<Tournament> filtered = FXCollections.observableArrayList();
        for (Tournament t : tournamentService.getAllData()) {
            if (t.getName().toLowerCase().contains(filter.toLowerCase())) {
                filtered.add(t);
            }
        }
        lvTournaments.setItems(filtered);
    }


    private void showSuggestions(String input) {
        suggestions.getItems().clear();

        if (input == null || input.isEmpty()) {
            suggestions.hide();
            return;
        }

        for (Tournament t : tournamentService.getAllData()) {
            if (t.getName().toLowerCase().startsWith(input.toLowerCase())) {
                MenuItem item = new MenuItem(t.getName());
                item.setOnAction(e -> {
                    tfSearch.setText(t.getName());
                    filterList(t.getName());
                    suggestions.hide();
                });
                suggestions.getItems().add(item);
            }
        }

        if (!suggestions.getItems().isEmpty()) {
            suggestions.show(tfSearch, Side.BOTTOM, 0, 0);
        } else {
            suggestions.hide();
        }
    }

}
