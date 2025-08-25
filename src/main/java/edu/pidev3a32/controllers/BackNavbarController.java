package edu.pidev3a32.controllers;

import edu.pidev3a32.tools.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static edu.pidev3a32.tools.Toast.showWarning;

public class BackNavbarController {

    @FXML
    void goToGames(ActionEvent event) {
        navigateTo(event, "/BackOffice/GameList.fxml");
    }

    @FXML
    void goToHome(ActionEvent event) {
        navigateTo(event, "/BackOffice/Home.fxml");
    }

    @FXML
    void logout(ActionEvent event) {
        navigateTo(event, "/FrontOffice/SignIn.fxml");
        SessionManager.clear();
    }

    @FXML
    void goToTournaments(ActionEvent event) {
        navigateTo(event, "/BackOffice/TournamentList.fxml");
    }

    @FXML
    void goToUsers(ActionEvent event) {
        navigateTo(event, "/BackOffice/UserList.fxml");
    }

    private void navigateTo(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showWarning("Error navigating to page: " + e.getMessage());
        }
    }
}
