package com.example.csit228_f1_v2;

import java.sql.*;

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


    public int UpdateData(String username, String password) {
        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "UPDATE users SET name=? WHERE id=?"
            )){
            String new_name = "Malt John Vianney Solon";
            int id = 4;
            statement.setString(1, new_name);
            statement.setInt(2, id);
            int rowsUpdated = statement.executeUpdate();
            System.out.println("Rows Updated: " + rowsUpdated);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 1;
    }

    public void DeleteData(){
        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "DELETE FROM users WHERE id>?"
            )){
            int starting_id = 2;
            statement.setInt(1, starting_id);
            int rowsDeleted = statement.executeUpdate();
            System.out.println("Rows Updated: " + rowsDeleted);
        }catch (SQLException e){
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
}
