package com.example.csit228_f1_v2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class CharacterWindowController {
    public Button btnLogout;
    @FXML
    private AnchorPane miniWindow;

    private double xOffset = 0;
    private double yOffset = 0;

    public void initialize(AnchorPane miniPane, int id) {
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

        Account myChar = new Account();
        int myCharID = myChar.findChar(id);
        Account myStats = myChar.showStats(myCharID);

    }

    public void logout() throws IOException {
        AnchorPane mainPane = (AnchorPane) miniWindow.getParent();
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
}
