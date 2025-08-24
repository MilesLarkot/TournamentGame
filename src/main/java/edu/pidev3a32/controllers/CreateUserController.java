package edu.pidev3a32.controllers;

import edu.pidev3a32.entities.User;
import edu.pidev3a32.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.sql.SQLException;

import static edu.pidev3a32.tools.Toast.showToast;
import static edu.pidev3a32.tools.Toast.showWarning;

public class CreateUserController {

    @FXML
    private PasswordField pfPassword;

    @FXML
    private TextField tfUsername;

    private final UserService userService = new UserService();

    @FXML
    void addUser(ActionEvent event) {
        String username = tfUsername.getText();
        String password = pfPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showWarning("Username and password cannot be empty!");
            return;
        }

        try {
            User user = new User(username, password, "USER");

            userService.addEntity(user);
            showToast("User added successfully!");

            goToUserList(event);

        } catch (SQLException e) {
            showWarning("Error adding user: " + e.getMessage());
        }
    }

    @FXML
    void goToUserList(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BackOffice/UserList.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showWarning("Error navigating to User List: " + e.getMessage());
        }
    }
}
