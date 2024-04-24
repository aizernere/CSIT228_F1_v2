package com.example.csit228_f1_v2;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class Window {
    private Scene scene;
    private AnchorPane pane;
    public Window (Scene scene, AnchorPane pane){
        this.scene = scene;
        this.pane = pane;
    }

    public void setCenter(){
        double centerX = scene.getWidth() / 2;
        double centerY = scene.getHeight()*2 / 3;
        System.out.println(centerX);
        System.out.println(centerY);

        double centerMiniX = pane.getMaxWidth()/2;
        double centerMiniY = pane.getMaxHeight()/2;
        System.out.println(centerMiniX);
        System.out.println(centerMiniY);

        double miniX=centerX-centerMiniX;
        double miniY=centerY-centerMiniY;

        pane.setLayoutX(miniX);
        pane.setLayoutY(miniY);
    }
}
