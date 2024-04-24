package com.example.csit228_f1_v2;

import javafx.scene.control.Label;

import java.sql.*;

import static com.example.csit228_f1_v2.CharacterWindowController.myStats;

public class Account implements Runnable {
    private String username;
    private int lvl;
    private int str;
    private int agi;
    private int vit;
    private int statInt;
    private int dex;
    private int luk;
    private int pts;


    public Account() {

    }

    public Account(String username, int lvl, int str, int agi, int vit,int statInt, int dex, int luk, int pts) {
        this.username = username;
        this.lvl = lvl;
        this.str = str;
        this.agi = agi;
        this.vit = vit;
        this.statInt = statInt;
        this.dex = dex;
        this.luk = luk;
        this.pts = pts;
    }

    @Override
    public void run() {
        try(Connection c = MySQLConnection.getDatabase();Statement statement = c.createStatement()) {
            String query = "CREATE TABLE IF NOT EXISTS tbluser (" +
                    "userID INT PRIMARY KEY AUTO_INCREMENT," +
                    "username VARCHAR(50) NOT NULL," +
                    "password INT(255) NOT NULL," +
                    "email VARCHAR(100) NOT NULL)";
            statement.execute(query);
            System.out.println("Table [user] created successfully!");
            query = "CREATE TABLE IF NOT EXISTS tblchar (" +
                    "charID INT PRIMARY KEY AUTO_INCREMENT," +
                    "userID INT," +
                    "FOREIGN KEY (userID) REFERENCES tbluser(userID)," +
                    "lvl INT(3) NOT NULL," +
                    "str INT(3) NOT NULL," +
                    "agi INT(3) NOT NULL," +
                    "vit INT(3) NOT NULL," +
                    "statInt INT(3) NOT NULL," +
                    "dex INT(3) NOT NULL," +
                    "luk INT(3) NOT NULL," +
                    "pts INT(3) NOT NULL)";

            statement.execute(query);
            System.out.println("Table [char] created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int findChar(int id) {
        int charID = 0;
        try(Connection c = MySQLConnection.getDatabase();
            Statement statement = c.createStatement()){
            String query = "SELECT charID FROM tblchar where userID='"+id+"'";
            ResultSet res = statement.executeQuery(query);
            while(res.next()){
                charID = res.getInt("charID");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return charID;
    }

    public int InsertData(String username, String password,  String email) {
        try (Connection c = MySQLConnection.getDatabase();
             PreparedStatement userStatement = c.prepareStatement(
                     "INSERT INTO tbluser (username, password, email) VALUES (?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS);
             PreparedStatement charStatement = c.prepareStatement(
                     "INSERT INTO tblchar (userID, lvl, str, agi, vit, statInt, dex, luk, pts) VALUES (?, 1, 1, 1, 1, 1, 1, 1, 0)")) {
            c.setAutoCommit(false);
            // Insert a new user into tbluser
            int hash = password.hashCode();
            userStatement.setString(1, username);
            userStatement.setInt(2, hash);
            userStatement.setString(3, email);
            int rowsInserted = userStatement.executeUpdate();

            int userID = -1;
            try (ResultSet generatedKeys = userStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    userID = generatedKeys.getInt(1);
                }
            }

            if (userID != -1) {
                charStatement.setInt(1, userID);
                int charRowsInserted = charStatement.executeUpdate();
                System.out.println("Character created in tblchar: " + charRowsInserted);
                c.commit();
                return charRowsInserted;
            } else {
                System.out.println("Failed to retrieve userID");
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }



    public void DeleteData(int myCharID){
        int myUserID = -1; // Initialize to an invalid value

        try (Connection c = MySQLConnection.getDatabase();
             PreparedStatement charStatement = c.prepareStatement(
                     "SELECT userID FROM tblchar WHERE charID=?")) {

            charStatement.setInt(1, myCharID);
            ResultSet resultSet = charStatement.executeQuery();
            if (resultSet.next()) {
                myUserID = resultSet.getInt("userID");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        try (Connection c = MySQLConnection.getDatabase();
             PreparedStatement charStatement = c.prepareStatement(
                     "DELETE FROM tblchar WHERE charID=?");
             PreparedStatement userStatement = c.prepareStatement(
                     "DELETE FROM tbluser WHERE userID=?")) {


            c.setAutoCommit(false);

            charStatement.setInt(1, myCharID);
            int charRowsDeleted = charStatement.executeUpdate();
            System.out.println("Character Rows Deleted: " + charRowsDeleted);
            if(charRowsDeleted==1){
                userStatement.setInt(1, myUserID);
                int userRowsDeleted = userStatement.executeUpdate();
                System.out.println("User Rows Deleted: " + userRowsDeleted);
                c.commit();
                System.out.println("Successfully deleted account.");
            }




        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public int getLvl() {
        return lvl;
    }

    public int getStr() {
        return str;
    }

    public int getAgi() {
        return agi;
    }

    public int getVit() {
        return vit;
    }

    public int getStatInt() {
        return statInt;
    }

    public int getDex() {
        return dex;
    }

    public int getLuk() {
        return luk;
    }

    public int getPts() {
        return pts;
    }

    public Account showStats(int myCharID) {
        try(Connection c = MySQLConnection.getDatabase();
            Statement statement = c.createStatement()){
            String query = "SELECT * FROM tblchar, tbluser where charID='"+myCharID+"'and tbluser.userID = tblchar.userID";
            ResultSet res = statement.executeQuery(query);
            while(res.next()){
                String username = res.getString("username");
                int lvl = res.getInt("lvl");
                int str = res.getInt("str");
                int agi = res.getInt("agi");
                int vit = res.getInt("vit");
                int statInt = res.getInt("statInt");
                int dex = res.getInt("dex");
                int luk = res.getInt("luk");
                int pts = res.getInt("pts");

                return new Account(username, lvl, str, agi, vit, statInt, dex, luk, pts);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return new Account();
    }

    public int UpdateData(Label str, Label agi, Label vit, Label intel, Label dex, Label luk, int myCharID) {
        int rowsUpdated = 0;
        try(Connection c = MySQLConnection.getDatabase();
            PreparedStatement statement = c.prepareStatement(
                    "UPDATE tblchar SET pts = 0, str = ?, agi = ?, vit=?, statInt=?, dex=? , luk = ? WHERE charID=?"
            )){

            statement.setInt(1, Integer.parseInt(str.getText()));
            statement.setInt(2, Integer.parseInt(agi.getText()));
            statement.setInt(3, Integer.parseInt(vit.getText()));
            statement.setInt(4, Integer.parseInt(intel.getText()));
            statement.setInt(5, Integer.parseInt(dex.getText()));
            statement.setInt(6, Integer.parseInt(luk.getText()));
            statement.setInt(7, myCharID);
            rowsUpdated = statement.executeUpdate();
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
        }catch (SQLException e){
            e.printStackTrace();
        }
        return rowsUpdated;
    }
}
