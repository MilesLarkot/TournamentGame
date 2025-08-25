package edu.pidev3a32.controllers;

import edu.pidev3a32.tools.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;

public class FrontNavbarController {
    @FXML
    void logOut(ActionEvent event) { switchScene(event, "/FrontOffice/SignIn.fxml");
    SessionManager.clear();}

    @FXML
    void goToHome(ActionEvent event) { switchScene(event, "/FrontOffice/Home.fxml"); }

    @FXML
    void goToTournaments(ActionEvent event) { switchScene(event, "/FrontOffice/TournamentList.fxml"); }

    @FXML
    void goToGames(ActionEvent event) { switchScene(event, "/FrontOffice/GameList.fxml"); }

    @FXML
    void goToAbout(ActionEvent event) { switchScene(event, "/FrontOffice/About.fxml"); }

    private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
