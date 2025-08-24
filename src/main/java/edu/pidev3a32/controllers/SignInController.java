package edu.pidev3a32.controllers;

import edu.pidev3a32.entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static edu.pidev3a32.tools.Toast.showWarning;

public class SignInController {

    @FXML
    private TextField tfUsername;

    @FXML
    private PasswordField pfPassword;

    @FXML
    private Button btnLogin;


    private static final List<User> users = new ArrayList<>();

    static {
        users.add(new User("admin", "admin123", "ADMIN"));
        users.add(new User("john", "user123", "USER"));
    }

    @FXML
    void signIn(ActionEvent event) {
        String username = tfUsername.getText();
        String password = pfPassword.getText();

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                try {
                    if ("ADMIN".equals(user.getRole())) {
                        goToScene("/BackOffice/Home.fxml", event);
                    } else {
                        goToScene("/FrontOffice/Home.fxml", event);
                    }
                } catch (IOException e) {
                    showWarning("Error loading scene: " + e.getMessage());
                }
                return;
            }
        }
        showWarning("Invalid credentials!");
    }

    private void goToScene(String fxml, ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
