package edu.pidev3a32.controllers;

import edu.pidev3a32.entities.User;
import edu.pidev3a32.services.UserService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.util.List;

import static edu.pidev3a32.tools.Toast.showToast;
import static edu.pidev3a32.tools.Toast.showWarning;

public class BackUserListController {

    @FXML
    private ListView<User> lvUsers;

    private final UserService userService = new UserService();

    @FXML
    private void initialize() {
        loadUsers();

        lvUsers.setCellFactory(param -> {
            ListCell<User> cell = new ListCell<>() {
                @Override
                protected void updateItem(User user, boolean empty) {
                    super.updateItem(user, empty);

                    if (empty || user == null) {
                        setGraphic(null);
                        setOnMouseClicked(null);
                    } else {
                        Label username = new Label(user.getUsername());

                        ComboBox<String> roleBox = new ComboBox<>();
                        roleBox.setItems(FXCollections.observableArrayList("ADMIN", "USER"));
                        roleBox.setValue(user.getRole());

                        roleBox.setOnAction(e -> {
                            user.setRole(roleBox.getValue());
                            userService.updateEntity(user.getId(), user);
                            showToast("Role updated for " + user.getUsername());
                        });

                        Button deleteButton = new Button("Delete");
                        deleteButton.setStyle("-fx-background-color: #ffce00;");
                        deleteButton.setOnAction(e -> {
                            userService.deleteEntity(user);
                            loadUsers();
                            showToast("User deleted!");
                        });

                        HBox row = new HBox(15, username, roleBox, deleteButton);
                        setGraphic(row);

                        setOnMouseClicked(event -> {
                            if ( !isEmpty()) {
                                try {
                                    goToEditUser(user);
                                } catch (IOException ex) {
                                    showWarning("Error opening Edit User: " + ex.getMessage());
                                }
                            }
                        });
                    }
                }
            };
            return cell;
        });
    }

    private void loadUsers() {
        List<User> users = userService.getAllData();
        lvUsers.setItems(FXCollections.observableArrayList(users));
    }

    @FXML
    void goToCreateUser(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BackOffice/CreateUser.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showWarning("Error navigating to Create User: " + e.getMessage());
        }
    }

    private void goToEditUser(User selectedUser) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/BackOffice/EditUser.fxml"));
        Parent root = loader.load();

        EditUserController controller = loader.getController();
        controller.setUser(selectedUser);

        Stage stage = (Stage) lvUsers.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
