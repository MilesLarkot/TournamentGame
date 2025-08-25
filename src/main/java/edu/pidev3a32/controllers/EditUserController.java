package edu.pidev3a32.controllers;

import edu.pidev3a32.entities.User;
import edu.pidev3a32.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;

import java.io.IOException;

import static edu.pidev3a32.tools.Toast.showToast;
import static edu.pidev3a32.tools.Toast.showWarning;

public class EditUserController {

    @FXML
    private PasswordField pfPassword;

    @FXML
    private TextField tfUsername;

    private UserService userService = new UserService();
    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;
        tfUsername.setText(user.getUsername());
        pfPassword.setText(user.getPassword());
    }

    @FXML
    void updateUser(ActionEvent event) {
        if (currentUser == null) {
            showWarning("No user selected!");
            return;
        }

        currentUser.setUsername(tfUsername.getText());
        currentUser.setPassword(pfPassword.getText());

        userService.updateEntity(currentUser.getId(), currentUser);
        showToast("User updated successfully!");

        goToUserList(event);
    }

    @FXML
    void goToUserList(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/BackOffice/UserList.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showWarning("Error loading User List: " + e.getMessage());
        }
    }
}
