package com.example.csit228_f1_v2;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterWindowController {
    public Button btnBack;
    public TextField tfUser;
    public PasswordField pfPass;
    public PasswordField pfPassConfirm;
    public TextField tfEmail;

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
    public void back() throws IOException {
        AnchorPane mainPane = (AnchorPane) miniWindow.getParent();
        if (!mainPane.getChildren().isEmpty()) {
            mainPane.getChildren().remove(1);
        }

        FXMLLoader serverLoader = new FXMLLoader(ServerWindowController.class.getResource("serverWindow.fxml"));
        AnchorPane serverPane = serverLoader.load();
        ServerWindowController serverController = serverLoader.getController();
        serverController.initialize(serverPane);
        mainPane.getChildren().add(serverPane);

        // start position for mini window
        Window window = new Window(mainPane.getScene(), serverPane);
        window.setCenter();
        // select first item
        serverController.listViewServer.getSelectionModel().selectFirst();
    }


    public void submit() throws IOException {
        String username = tfUser.getText().toString();
        String password = pfPass.getText().toString();
        String confirm = pfPassConfirm.getText().toString();
        String email = tfEmail.getText().toString();
        if(password.equals(confirm)){
            register(username,password,email);
        }else{
            System.out.println("Passwords do not match!");
        }


    }

    private void register(String username, String password,  String email) throws IOException {
        Account newAccount = new Account();
        int success = newAccount.InsertData(username, password, email);
        if(success==1){
            System.out.println("Successfully registered!");
            back();
        }else{
            System.out.println("There is something wrong in the server");
        }
    }
}
