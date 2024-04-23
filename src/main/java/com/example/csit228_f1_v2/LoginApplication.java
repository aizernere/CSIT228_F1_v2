package com.example.csit228_f1_v2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

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

        // Mini window
        FXMLLoader miniLoader = new FXMLLoader(getClass().getResource("loginWindow.fxml"));
        AnchorPane miniPane = miniLoader.load();
        LoginWindowController miniController = miniLoader.getController();
        miniController.initialize(miniPane);

        mainPane.getChildren().add(miniPane);

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

        double centerMiniX = miniPane.getMaxWidth()/2;
        double centerMiniY = miniPane.getMaxHeight()/2;
        System.out.println(centerMiniX);
        System.out.println(centerMiniY);

        double miniX=centerX-centerMiniX;
        double miniY=centerY-centerMiniY;

        miniPane.setLayoutX(miniX);
        miniPane.setLayoutY(miniY);

        //start music
        musicPlayer = new BackgroundMusicPlayer("/sound/01.wav");
        musicPlayer.play();
        miniController.focus();
        stage.show();
    }
}
