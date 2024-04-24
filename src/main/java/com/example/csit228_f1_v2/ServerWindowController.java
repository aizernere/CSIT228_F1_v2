package com.example.csit228_f1_v2;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.io.IOException;

public class ServerWindowController {

    public Button btnExit;
    public Label serverValkyrie;
    public ListView listViewServer;
    @FXML
    private AnchorPane miniWindow;


    private double xOffset = 0;
    private double yOffset = 0;

    public void initialize(AnchorPane miniPane) {
        listViewServer.getItems().add("Valkyrie");
        listViewServer.getItems().add("Test Server");
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

    public void close() {
        Platform.exit();
    }

    static void TestServer() {
        System.out.println("Test Server Only");
    }

}

