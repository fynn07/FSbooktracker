package org.example.crud;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MainConnection {
    //replace the db in the URL to your database
    public static final String URL = "jdbc:mysql://localhost:3306/java_jdbc_db";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";
    public boolean is_tables_created;
    public static int user_connected;
    public Connection connect = null;
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
                    "book_pages VARCHAR(10) NOT NULL," +
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

    public boolean loginAccount(String USERNAME, String PASSWORD){
        if(connect != null){
            try(PreparedStatement statement = connect.prepareStatement(
                    "SELECT * FROM accounts WHERE username = ? AND password = ?"
            )) {
                statement.setString(1, USERNAME);
                statement.setString(2, PASSWORD);

                ResultSet res = statement.executeQuery();

                if(res.next()){
                    user_connected = res.getInt("id");
                    return true;
                }
                return false;

            }catch (SQLException e){
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean deleteAccount(String USERNAME, String PASSWORD){
        if(connect != null){
            try (PreparedStatement bookStatement = connect.prepareStatement(
                    "DELETE FROM books WHERE user_id = (SELECT id FROM accounts WHERE username = ? AND password = ?)"
            )) {
                bookStatement.setString(1, USERNAME);
                bookStatement.setString(2, PASSWORD);
                bookStatement.executeUpdate();
            }catch (SQLException e){
                e.printStackTrace();
                return false;
            }

            try(PreparedStatement statement = connect.prepareStatement(
                    "DELETE FROM accounts WHERE username = ? AND password = ?"
            )){
                statement.setString(1, USERNAME);
                statement.setString(2, PASSWORD);
                return statement.executeUpdate() >= 1;

            }catch (SQLException e){
                e.printStackTrace();
            }
            return false;
        }
        return false;
    }

    public int addBook(String BOOKNAME, String BOOKAUTHOR, String BOOKPAGES){
        if(connect == null){
            return 1;
        }
        if(!is_tables_created){
            return 2;
        }
        int i = 1;

        try(PreparedStatement statement = connect.prepareStatement(
                "INSERT INTO books (user_id, book_name, book_author, book_pages) VALUES (?, ?, ?, ?)"
        )){
            statement.setInt(1, user_connected);
            statement.setString(2, BOOKNAME);
            statement.setString(3, BOOKAUTHOR);
            statement.setString(4, BOOKPAGES);
            statement.executeUpdate();
            return 0;
        }catch(SQLException e){
            e.printStackTrace();
            return 2;
        }
    }

    public List<Book> getBooksResults(){
        if(connect != null){
            List<Book> books = new ArrayList<>();

            try(PreparedStatement statement = connect.prepareStatement(
                    "SELECT * FROM books WHERE user_id = ?"
            )){
                statement.setString(1, String.valueOf(user_connected));
                ResultSet res = statement.executeQuery();
                while(res.next()){
                    String bookName = res.getString("book_name");
                    String bookAuthor = res.getString("book_author");
                    String bookPages = res.getString("book_pages");

                    Book book = new Book(bookName, bookAuthor, bookPages);
                    books.add(book);
                }
                return books;

            }catch(SQLException e){
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

}