package org.example.crud;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MainController{
    //start connection
    MainConnection connection = new MainConnection();
    public static int user_connected;

    //main page text-box fields
    @FXML
    private TextField bookNameField;

    @FXML
    private TextField bookAuthorField;

    @FXML
    private TextField bookPagesField;

    //signup text-box fields
    @FXML
    private TextField nameSignupField;

    @FXML
    private TextField emailSignupField;

    @FXML
    private TextField usernameSignupField;

    @FXML
    private TextField passwordSignupField;

    //login text-box fields
    @FXML
    private TextField usernameLoginField;

    @FXML
    private TextField passwordLoginField;

    //MAIN PAGE
    @FXML
    protected void executeMainPageRedirect(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main_page.fxml"));

        Scene newScene = new Scene(loader.load());
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(newScene);
    }

    @FXML
    protected void doAddBook() throws SQLException{
        String BOOKNAME = bookNameField.getText();
        String BOOKAUTHOR = bookAuthorField.getText();
        String BOOKPAGES = bookPagesField.getText();

        int status = connection.addBook(BOOKNAME, BOOKAUTHOR, BOOKPAGES);
        if(status == 0){
            System.out.println("Book Added Successfully");
            //Do FXML DOM MANIPULATION
            handleLibraryDOM();
        }
        if(status == 1){
            System.out.println("Database Not Connected");
        }
        if(status == 2){
            System.out.println("Internal Error");
        }

        return;

    }

    protected void handleLibraryDOM() {
        List<Book> books = connection.getBooksResults();
        for(Book book : books){
            System.out.println("Book Name: " + book.getBookName() + " Book Author: " + book.getBookAuthor());
        }
        return;
    }

    //LOGIN PAGE CONTROLLERS
    @FXML
    protected void onSignupRedirectClick(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("signup_page.fxml"));

        Scene newScene = new Scene(loader.load());
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(newScene);

    }

    @FXML
    protected void onLoginClick(ActionEvent event){
        String USERNAME = usernameLoginField.getText();
        String PASSWORD = passwordLoginField.getText();

        if(connection.loginAccount(USERNAME, PASSWORD)){
            System.out.println("Login Successful");
            try {
                executeMainPageRedirect(event);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else{
            System.out.println("Account not Found");
        }
    }


    //SIGNUP PAGE CONTROLLERS
    @FXML
    protected void onLoginRedirectClick(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login_page.fxml"));

        Scene newScene = new Scene(loader.load());

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(newScene);
    }

    @FXML
    protected void onSignupClick(ActionEvent event){
        String NAME = nameSignupField.getText();
        String EMAIL = emailSignupField.getText();
        String USERNAME = usernameSignupField.getText();
        String PASSWORD = passwordSignupField.getText();

        int status = connection.createAccount(NAME, EMAIL, USERNAME, PASSWORD);
        if(status == 0){
            System.out.println("Account Successfully Created");
            try {
                onLoginRedirectClick(event);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if(status == 1){
            System.out.println("Database not Connected");
        }
        if(status == 2){
            System.out.println("Tables not Found");
        }
        if(status == 3){
            System.out.println("Internal Error");
        }
    }
}
