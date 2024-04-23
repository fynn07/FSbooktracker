package org.example.crud;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class MainConnection {
    //replace the db in the URL to your database
    public static final String URL = "jdbc:mysql://localhost:3306/java_jdbc_db";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";
    public Connection connect = null;
    public boolean is_tables_created;

    public MainConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            is_tables_created = this.createTables();
        } catch (ClassNotFoundException | SQLException var2) {
            var2.printStackTrace();
        }
    }
    public boolean createTables(){
        if(connect != null){
            String account_table_query = "CREATE TABLE IF NOT EXISTS accounts (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(100) NOT NULL," +
                    "email VARCHAR(100) NOT NULL," +
                    "username VARCHAR(100) NOT NULL," +
                    "password VARCHAR(100) NOT NULL)";

            String book_table_query = "CREATE TABLE IF NOT EXISTS books (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "user_id INT," +
                    "book_name VARCHAR(100) NOT NULL," +
                    "book_author VARCHAR(100) NOT NULL," +
                    "book_pages INT NOT NULL," +
                    "FOREIGN KEY(user_id) REFERENCES accounts(id))";

            try{
                Statement statement = connect.createStatement();
                statement.execute(account_table_query);
                statement.execute(book_table_query);
                return true;
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    //return indicators
    //0 = successful ; 1 = database not connect ; 2 = tables not created ; 3 = sql exception
    public int createAccount(String NAME, String EMAIL, String USERNAME, String PASSWORD){
        if(connect == null){
            return 1;
        }
        if(!is_tables_created){
            return 2;
        }

        try(PreparedStatement statement = connect.prepareStatement(
                "INSERT INTO accounts (name, email, username, password) VALUES (?,?,?,?)"
        )){
            statement.setString(1, NAME);
            statement.setString(2, EMAIL);
            statement.setString(3, USERNAME);
            statement.setString(4, PASSWORD);
            statement.executeUpdate();
            return 0;
        }catch (SQLException e){
            e.printStackTrace();
            return 3;
        }
    }

}
