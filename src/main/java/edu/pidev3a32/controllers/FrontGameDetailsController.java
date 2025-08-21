package edu.pidev3a32.controllers;

import edu.pidev3a32.entities.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class FrontGameDetailsController {

    @FXML
    private Text txtTitle;

    @FXML
    private Text txtDate;

    @FXML
    private Text txtTournament;

    @FXML
    private Label lblTeam1;

    @FXML
    private Text txtScore;

    @FXML
    private Label lblTeam2;

    private Game game;


    @FXML
    void goToFrontOfficeGameList(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontOffice/GameList.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    void goToFrontOfficeTournamentList(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontOffice/TournamentList.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }


    public void setGame(Game game) {
        this.game = game;

        txtTitle.setText(game.getTeam1() + " vs " + game.getTeam2());
        txtDate.setText("Date: " + game.getDate().toString());
        txtTournament.setText("Tournament: " + game.getTournament().getName());
        lblTeam1.setText(game.getTeam1());
        lblTeam2.setText(game.getTeam2());
        txtScore.setText(game.getScore());
    }

}
