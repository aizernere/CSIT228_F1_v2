package com.example.csit228_f1_v2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

public class CharacterWindowController {
    public Button btnLogout;
    public Label str;
    public Label agi;
    public Label vit;
    public Label intel;
    public Label dex;
    public Label luk;
    public Label lblUser;
    public Label lblLvl;
    public ImageView[] plusStats = new ImageView[6];
    public ImageView plusStr;
    public ImageView plusAgi;
    public ImageView plusVit;
    public ImageView plusInt;
    public ImageView plusDex;
    public ImageView plusLuk;
    public Label pts;
    static int myCharID;
    static Account myStats;
    @FXML
    private AnchorPane miniWindow;

    private double xOffset = 0;
    private double yOffset = 0;

    public void initialize(AnchorPane miniPane, int id) {
        plusStats[0] = plusStr;
        plusStats[1] = plusAgi;
        plusStats[2] = plusVit;
        plusStats[3] = plusInt;
        plusStats[4] = plusDex;
        plusStats[5] = plusLuk;
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
        myCharID = myChar.findChar(id);
        myStats = myChar.showStats(myCharID);
        str.setText(String.valueOf(myStats.getStr()));
        agi.setText(String.valueOf(myStats.getAgi()));
        vit.setText(String.valueOf(myStats.getVit()));
        intel.setText(String.valueOf(myStats.getStatInt()));
        dex.setText(String.valueOf(myStats.getDex()));
        luk.setText(String.valueOf(myStats.getLuk()));
        lblUser.setText(myStats.getUsername());
        lblLvl.setText(String.valueOf(myStats.getLvl()));
        pts.setText(String.valueOf(myStats.getPts()));
        if(myStats.getPts()==0){
            for(ImageView stat: Arrays.asList(plusStats)){
                stat.setVisible(false);
            }
        }

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

    public void levelup() {
        if(lblLvl.getText().equals("99")){
            return;
        }
        try(Connection c = MySQLConnection.getDatabase();
            PreparedStatement statement = c.prepareStatement(
                    "UPDATE tblchar SET lvl=lvl+1, pts = pts + 4 WHERE charID=?"
            )){

            statement.setInt(1, myCharID);
            int rowsUpdated = statement.executeUpdate();
            System.out.println("Rows Updated: " + rowsUpdated);

            //update GUI
            Account myChar = new Account();
            myStats = myChar.showStats(myCharID);
            lblLvl.setText(String.valueOf(myStats.getLvl()));
            pts.setText(String.valueOf(myStats.getPts()));
            if(myStats.getPts()>=2){
                for(ImageView stat: Arrays.asList(plusStats)){
                    stat.setVisible(true);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void maxLvl() {
        if(lblLvl.getText().equals("99")){
            return;
        }
        try(Connection c = MySQLConnection.getDatabase();
            PreparedStatement statement = c.prepareStatement(
                    "UPDATE tblchar SET lvl=99, pts = 404-str*2-agi*2-vit*2-statInt*2-dex*2-luk*2 WHERE charID=?"
            )){

            statement.setInt(1, myCharID);
            int rowsUpdated = statement.executeUpdate();
            System.out.println("Rows Updated: " + rowsUpdated);

            Account myChar = new Account();
            myStats = myChar.showStats(myCharID);
            lblLvl.setText(String.valueOf(myStats.getLvl()));
            pts.setText(String.valueOf(myStats.getPts()));
            for(ImageView stat: Arrays.asList(plusStats)){
                stat.setVisible(true);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void resetLvl() {
        if(lblLvl.getText().equals("1")){
            return;
        }
        try(Connection c = MySQLConnection.getDatabase();
            PreparedStatement statement = c.prepareStatement(
                    "UPDATE tblchar SET lvl=1, pts = 0, str = 1, agi = 1, vit = 1, statInt = 1, dex = 1, luk = 1 WHERE charID=?"
            )){
            lblLvl.setText("1");
            pts.setText("0");

            statement.setInt(1, myCharID);
            int rowsUpdated = statement.executeUpdate();
            System.out.println("Rows Updated: " + rowsUpdated);

            //update
            Account myChar = new Account();
            myStats = myChar.showStats(myCharID);
            str.setText(String.valueOf(myStats.getStr()));
            agi.setText(String.valueOf(myStats.getAgi()));
            vit.setText(String.valueOf(myStats.getVit()));
            intel.setText(String.valueOf(myStats.getStatInt()));
            dex.setText(String.valueOf(myStats.getDex()));
            luk.setText(String.valueOf(myStats.getLuk()));
            if(myStats.getPts()==0){
                for(ImageView stat: Arrays.asList(plusStats)){
                    stat.setVisible(false);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void strUp(MouseEvent mouseEvent) {
        str.setText(String.valueOf(Integer.valueOf(str.getText())+1));
        pts.setText(String.valueOf(Integer.valueOf(pts.getText())-2));
        if(pts.getText().equals("0")){
            for(ImageView stat: Arrays.asList(plusStats)){
                stat.setVisible(false);
            }
            Account update = new Account();
            int success = update.UpdateData(str, agi, vit, intel, dex, luk , myCharID);
            if(success==1){
                System.out.println("Stat changes are saved.");
            }
        }

    }

    public void agiUp(MouseEvent mouseEvent) {
        agi.setText(String.valueOf(Integer.valueOf(agi.getText())+1));
        pts.setText(String.valueOf(Integer.valueOf(pts.getText())-2));
        if(pts.getText().equals("0")){
            for(ImageView stat: Arrays.asList(plusStats)){
                stat.setVisible(false);
            }
            Account update = new Account();
            int success = update.UpdateData(str, agi, vit, intel, dex, luk , myCharID);
            if(success==1){
                System.out.println("Stat changes are saved.");
            }
        }
    }

    public void vitUp(MouseEvent mouseEvent) {
        vit.setText(String.valueOf(Integer.valueOf(vit.getText())+1));
        pts.setText(String.valueOf(Integer.valueOf(pts.getText())-2));
        if(pts.getText().equals("0")){
            for(ImageView stat: Arrays.asList(plusStats)){
                stat.setVisible(false);
            }
            Account update = new Account();
            int success = update.UpdateData(str, agi, vit, intel, dex, luk , myCharID);
            if(success==1){
                System.out.println("Stat changes are saved.");
            }
        }
    }

    public void intUp(MouseEvent mouseEvent) {
        intel.setText(String.valueOf(Integer.valueOf(intel.getText())+1));
        pts.setText(String.valueOf(Integer.valueOf(pts.getText())-2));
        if(pts.getText().equals("0")){
            for(ImageView stat: Arrays.asList(plusStats)){
                stat.setVisible(false);
            }
            Account update = new Account();
            int success = update.UpdateData(str, agi, vit, intel, dex, luk , myCharID);
            if(success==1){
                System.out.println("Stat changes are saved.");
            }
        }
    }

    public void dexUp(MouseEvent mouseEvent) {
        dex.setText(String.valueOf(Integer.valueOf(dex.getText())+1));
        pts.setText(String.valueOf(Integer.valueOf(pts.getText())-2));
        if(pts.getText().equals("0")){
            for(ImageView stat: Arrays.asList(plusStats)){
                stat.setVisible(false);
            }
            Account update = new Account();
            int success = update.UpdateData(str, agi, vit, intel, dex, luk , myCharID);
            if(success==1){
                System.out.println("Stat changes are saved.");
            }
        }
    }

    public void lukUp(MouseEvent mouseEvent) {
        luk.setText(String.valueOf(Integer.valueOf(luk.getText())+1));
        pts.setText(String.valueOf(Integer.valueOf(pts.getText())-2));
        if(pts.getText().equals("0")){
            for(ImageView stat: Arrays.asList(plusStats)){
                stat.setVisible(false);
            }
            Account update = new Account();
            int success = update.UpdateData(str, agi, vit, intel, dex, luk , myCharID);
            if(success==1){
                System.out.println("Stat changes are saved.");
            }
        }
    }

    public void deleteUserChar() throws IOException {
        Account delete = new Account();
        delete.DeleteData(myCharID);
        logout();
    }
}
