package edu.pidev3a32.controllers;

import edu.pidev3a32.entities.Game;
import edu.pidev3a32.services.GameService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.util.List;

public class FrontGameListController {

    @FXML
    private ListView<Game> lvGames;

    @FXML
    private TextField tfSearch;

    private GameService gameService = new GameService();
    private ContextMenu suggestions = new ContextMenu();

    @FXML
    private void initialize() {
        loadGames();

        lvGames.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Game g, boolean empty) {
                super.updateItem(g, empty);

                if (empty || g == null) {
                    setGraphic(null);
                } else {
                    Label teams = new Label(g.getTeam1() + " vs " + g.getTeam2());
                    Label date = new Label(g.getDate().toString());
                    Label score = new Label(g.getScore());

                    HBox row = new HBox(10, teams, date, score);
                    setGraphic(row);
                }
            }
        });

        tfSearch.textProperty().addListener((obs, oldVal, newVal) -> {
            filterList(newVal);
            showSuggestions(newVal);
        });
    }

    private void loadGames() {
        List<Game> games = gameService.getAllData();
        lvGames.setItems(FXCollections.observableArrayList(games));
    }

    private void filterList(String filter) {
        if (filter == null || filter.isEmpty()) {
            loadGames();
            return;
        }
        ObservableList<Game> filtered = FXCollections.observableArrayList();
        for (Game g : gameService.getAllData()) {
            if (g.getTeam1().toLowerCase().contains(filter.toLowerCase())
                    || g.getTeam2().toLowerCase().contains(filter.toLowerCase())
                    || g.getScore().toLowerCase().contains(filter.toLowerCase())) {
                filtered.add(g);
            }
        }
        lvGames.setItems(filtered);
    }

    private void showSuggestions(String input) {
        suggestions.getItems().clear();

        if (input == null || input.isEmpty()) {
            suggestions.hide();
            return;
        }

        for (Game g : gameService.getAllData()) {
            String suggestion = g.getTeam1() + " vs " + g.getTeam2();
            if (suggestion.toLowerCase().startsWith(input.toLowerCase())) {
                MenuItem item = new MenuItem(suggestion);
                item.setOnAction(e -> {
                    tfSearch.setText(suggestion);
                    filterList(suggestion);
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
