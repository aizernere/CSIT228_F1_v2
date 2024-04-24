package com.example.csit228_f1_v2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
        try(Connection c = MySQLConnection.getDatabase();
            Statement statement = c.createStatement()){
            String query = "SELECT * FROM tbluser where username='"+username+"'";
            ResultSet res = statement.executeQuery(query);

            while(res.next()){
                int id = res.getInt("userID");
                int userPass = res.getInt("password");
                int hash = password.hashCode();
                if(hash==userPass){
                    String name = res.getString("username");
                    String email = res.getString(4);
                    System.out.println("userID: " + id + "\nUsername: " + name + "\nEmail: " + email + "\n");
                    showCharacter(id);
                    return;
                }else{
                    System.out.println("Wrong username/password!");
                    return;
                }
            }
            if(!res.next()){
                System.out.println("Account not found!");
            }
        }catch (IOException | SQLException e){
            e.printStackTrace();
        }
    }

    private void showCharacter(int id) throws IOException {
        AnchorPane mainPane = (AnchorPane) miniWindow.getParent();
        if (!mainPane.getChildren().isEmpty()) {
            mainPane.getChildren().remove(1);
        }

        FXMLLoader charLoader = new FXMLLoader(CharacterWindowController.class.getResource("CharacterWindow.fxml"));
        AnchorPane charPane = charLoader.load();
        CharacterWindowController charController = charLoader.getController();
        charController.initialize(charPane, id);
        mainPane.getChildren().add(charPane);

        // start position for mini window
        Window window = new Window(mainPane.getScene(), charPane);
        window.setCenter();
    }

    public void selectServer() throws IOException {
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

    public void btnLogin(ActionEvent actionEvent) {
        String username = tfUser.getText();
        String password = pfPass.getText();
        login(username, password);
    }
}

