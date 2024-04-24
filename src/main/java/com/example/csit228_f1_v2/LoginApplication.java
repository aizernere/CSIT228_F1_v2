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
        Window window = new Window(scene, serverPane);
        window.setCenter();

        //start DB
        Thread CRUD = new Thread(new Account());
        CRUD.start();

        //start music
        musicPlayer = new BackgroundMusicPlayer("/sound/01.wav");
        musicPlayer.play();
        serverController.listViewServer.getSelectionModel().selectFirst();
        stage.show();
    }
}
