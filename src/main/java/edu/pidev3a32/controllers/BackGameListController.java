package edu.pidev3a32.controllers;

import edu.pidev3a32.entities.Game;
import edu.pidev3a32.services.GameService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.Node;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static edu.pidev3a32.tools.Toast.showToast;
import static edu.pidev3a32.tools.Toast.showWarning;

public class BackGameListController {
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
                    Button deleteButton = new Button("Delete");

                    deleteButton.setOnAction(event -> {
                        gameService.deleteEntity(g);
                        loadGames();
                        showToast("Game deleted successfully!");
                    });

                    HBox row = new HBox(10, teams, date, score, deleteButton);
                    setGraphic(row);
                }
            }
        });

        lvGames.setOnMouseClicked(event -> {
            if (event.getTarget() instanceof ListCell) {
                Game selected = lvGames.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    try {
                        goToUpdateGame(selected);
                    } catch (IOException e) {
                        showWarning("Error navigating to Edit Game: " + e.getMessage());
                    }
                }
            }
        });

        tfSearch.textProperty().addListener((obs, oldVal, newVal) -> {
            filterList(newVal);
            showSuggestions(newVal);
        });
    }

    @FXML
    private void goToUpdateGame(Game selectedGame) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/BackOffice/EditGame.fxml"));
        Parent root = loader.load();

        EditGameController controller = loader.getController();
        controller.setGame(selectedGame);

        Stage stage = (Stage) lvGames.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void goToCreateGame(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BackOffice/CreateGame.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showWarning("Error navigating to Create Game: " + e.getMessage());
        }
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