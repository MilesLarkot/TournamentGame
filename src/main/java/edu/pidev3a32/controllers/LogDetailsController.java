package edu.pidev3a32.controllers;

import edu.pidev3a32.entities.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LogDetailsController {

    @FXML
    private Label lblUser;

    @FXML
    private Label lblAction;

    @FXML
    private Label lblDetails;

    @FXML
    private Label lblTimestamp;

    public void setLog(Logger log) {
        lblUser.setText("User ID: " + log.getUserId());
        lblAction.setText("Action: " + log.getAction());
        lblDetails.setText("Details: " + log.getDetails());
        lblTimestamp.setText("Time: " + log.getTimestamp().toString());
    }
}
