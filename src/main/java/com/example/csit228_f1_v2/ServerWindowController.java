package com.example.csit228_f1_v2;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

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

        listViewServer.setOnKeyPressed(event -> {
            String selectedServer = listViewServer.getSelectionModel().getSelectedItem().toString();
            if (event.getCode() == KeyCode.ENTER) {
                if (selectedServer.equals("Test Server")) {
                    TestServer();
                } else {

                    try {
                        gotoValkyrie((AnchorPane) miniPane.getParent());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });


    }

    public void close() {
        Platform.exit();
    }

    public static void TestServer() {
        System.out.println("Test Server Only");
    }
    public static void gotoValkyrie(AnchorPane mainPane) throws IOException {

        if (!mainPane.getChildren().isEmpty()) {
            mainPane.getChildren().remove(1);
        }

        // Mini window
        FXMLLoader miniLoader = new FXMLLoader(LoginApplication.class.getResource("loginWindow.fxml"));
        AnchorPane miniPane = miniLoader.load();
        LoginWindowController miniController = miniLoader.getController();
        miniController.initialize(miniPane);

        Window window = new Window(mainPane.getScene(), miniPane);
        window.setCenter();

        mainPane.getChildren().add(miniPane);
        miniController.focus();
    }

    public void openRegister() throws IOException {
        AnchorPane mainPane = (AnchorPane) miniWindow.getParent();
        if (!mainPane.getChildren().isEmpty()) {
            mainPane.getChildren().remove(1);
        }

        // Mini window
        FXMLLoader miniLoader = new FXMLLoader(RegisterWindowController.class.getResource("registerWindow.fxml"));
        AnchorPane miniPane = miniLoader.load();
        RegisterWindowController miniController = miniLoader.getController();
        miniController.initialize(miniPane);

        Window window = new Window(mainPane.getScene(), miniPane);
        window.setCenter();

        mainPane.getChildren().add(miniPane);
    }
}

