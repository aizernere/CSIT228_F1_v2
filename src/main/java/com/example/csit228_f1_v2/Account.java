package com.example.csit228_f1_v2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Account implements Runnable {

    @Override
    public void run() {
        try(Connection c = MySQLConnection.getDatabase();Statement statement = c.createStatement()) {
            String query = "CREATE TABLE IF NOT EXISTS users (" +
                    "userID INT PRIMARY KEY AUTO_INCREMENT," +
                    "username VARCHAR(50) NOT NULL," +
                    "password INT(255) NOT NULL," +
                    "email VARCHAR(100) NOT NULL)";
            statement.execute(query);
            System.out.println("Table [users] created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int ReadData(String username, String password) {

        return 1;
    }

    public int InsertData(String username, String password,  String email) {
        try(Connection c = MySQLConnection.getDatabase();
            PreparedStatement statement = c.prepareStatement(
                    "INSERT INTO users (username, password, email) VALUES (?,?,?)"
            )){
            int hash = password.hashCode();
            statement.setString(1, username);
            statement.setInt(2, hash);
            statement.setString(3,email);
            int rowsInserted = statement.executeUpdate();
            System.out.println("Rows Inserted: " + rowsInserted);
            return rowsInserted;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public int UpdateData(String username, String password) {
        return 1;
    }
}
