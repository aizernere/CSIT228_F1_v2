package com.example.csit228_f1_v2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.cert.PolicyNode;

public class LoginApplication extends Application {
    private BackgroundMusicPlayer musicPlayer;
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws IOException {
        // Main window
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("loginPage.fxml"));
        AnchorPane mainPane = mainLoader.load();

        FXMLLoader serverLoader = new FXMLLoader(getClass().getResource("serverWindow.fxml"));
        AnchorPane serverPane = serverLoader.load();
        ServerWindowController serverController = serverLoader.getController();
        serverController.initialize(serverPane);

        mainPane.getChildren().add(serverPane);


        Scene scene = new Scene(mainPane, 1280, 720);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setMaximized(false);

        // start position for mini window
        double centerX = scene.getWidth() / 2;
        double centerY = scene.getHeight()*2 / 3;
        System.out.println(centerX);
        System.out.println(centerY);

        double centerMiniX = serverPane.getMaxWidth()/2;
        double centerMiniY = serverPane.getMaxHeight()/2;
        System.out.println(centerMiniX);
        System.out.println(centerMiniY);

        double miniX=centerX-centerMiniX;
        double miniY=centerY-centerMiniY;

        serverPane.setLayoutX(miniX);
        serverPane.setLayoutY(miniY);



        //start music
        musicPlayer = new BackgroundMusicPlayer("/sound/01.wav");
        musicPlayer.play();
        serverController.listViewServer.getSelectionModel().selectFirst();

        stage.show();

        serverController.listViewServer.setOnKeyPressed(event -> {
            String selectedServer = serverController.listViewServer.getSelectionModel().getSelectedItem().toString();
            if (event.getCode() == KeyCode.ENTER) {
                if (selectedServer.equals("Test Server")) {
                    ServerWindowController.TestServer();
                } else {
                    try {
                        mainPane.getChildren().get(1).setVisible(false);
                        gotoValkyrie(mainPane);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
    public static void gotoValkyrie(AnchorPane mainPane) throws IOException {
        // Mini window
        FXMLLoader miniLoader = new FXMLLoader(LoginApplication.class.getResource("loginWindow.fxml"));
        AnchorPane miniPane = miniLoader.load();
        LoginWindowController miniController = miniLoader.getController();
        miniController.initialize(miniPane);



        double centerX = mainPane.getScene().getWidth() / 2;
        double centerY = mainPane.getScene().getHeight()*2 / 3;
        System.out.println(centerX);
        System.out.println(centerY);

        double centerMiniX = miniPane.getMaxWidth()/2;
        double centerMiniY = miniPane.getMaxHeight()/2;
        System.out.println(centerMiniX);
        System.out.println(centerMiniY);

        double miniX=centerX-centerMiniX;
        double miniY=centerY-centerMiniY;

        miniPane.setLayoutX(miniX);
        miniPane.setLayoutY(miniY);

        if(mainPane.getChildren().size()==3){
            mainPane.getChildren().get(2).setVisible(true);
            miniController.focus();
        }else{
            mainPane.getChildren().add(miniPane);
            miniController.focus();
        }

        miniController.btnExit.setOnAction(event -> {
            mainPane.getChildren().get(2).setVisible(false);
            mainPane.getChildren().get(1).setVisible(true);
        });
    }


}
