package com.example.csit228_f1_v2;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class LoginWindowController {
    public Button btnLogin;
    public Button btnExit;
    public TextField tfUser;
    public PasswordField pfPass;
    @FXML
    private AnchorPane miniWindow;

    private double xOffset = 0;
    private double yOffset = 0;

    public void initialize(AnchorPane miniPane) {
        miniWindow = miniPane;
        miniWindow.setOnMousePressed(event -> {
            xOffset = event.getX();
            yOffset = event.getY();
            miniWindow.setOpacity(0.7);
        });

        miniWindow.setOnMouseReleased(event -> {
            miniWindow.setOpacity(1);
        });

        miniWindow.setOnMouseDragged(event -> {
            double newX = event.getSceneX() - xOffset;
            double newY = event.getSceneY() - yOffset;
            if (newX < 0) {
                newX = 0;
            } else if (newX > 1200) {
                newX = 1200;
            }

            if (newY < 0) {
                newY = 0;
            } else if (newY > 700) {
                newY = 700;
            }
            miniWindow.setLayoutX(newX);
            miniWindow.setLayoutY(newY);
        });

    }

    public void focus() {
        tfUser.requestFocus();
    }

    @FXML
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String username = tfUser.getText();
            String password = pfPass.getText();
            login(username, password);
        }
    }

    // Method to handle the login action
    private void login(String username, String password) {
        System.out.println("Logging in with username: " + username + " and password: " + password);
    }
}

