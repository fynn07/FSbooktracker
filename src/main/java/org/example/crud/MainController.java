package org.example.crud;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MainController{
    //start connection
    MainConnection connection = new MainConnection();

    public static int user_connected;

    @FXML
    private VBox libraryContainer;

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

    //update password fields
    @FXML
    private TextField changeUsernameField;

    @FXML
    private TextField changePasswordField;

    @FXML
    private TextField newPasswordField;

    //MAIN PAGE
    @FXML
    protected void executeMainPageRedirect(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main_page.fxml"));
        Scene newScene = new Scene(loader.load());
        MainController controller = loader.getController();
        controller.handleLibraryDOM();
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

    @FXML
    public void deleteBook(ActionEvent event){
        Button deleteButton = (Button) event.getSource();
        HBox hbox = (HBox) deleteButton.getParent();
        Label bookIDLabel = (Label) hbox.getChildren().getFirst(); // Assuming the bookID label is the first child
        String bookID = bookIDLabel.getText();

        if(connection.deleteBookinDB(bookID)){
            System.out.println("Book Deleted");
        }else{
            System.out.println("Book Not Deleted");
        }
        handleLibraryDOM();

    }

    @FXML
    protected void handleLibraryDOM() {
        List<Book> books = connection.getBooksResults();
        libraryContainer.getChildren().clear();
        for(Book book : books){
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER);
            hbox.setSpacing(10);

            // Create Labels and Button
            Label bookID = new Label(book.getID());
            bookID.setStyle("-fx-opacity: 0");

            Label bookNameLabel = new Label(book.getBookName());
            bookNameLabel.setStyle("-fx-min-width: 180;");

            Label authorNameLabel = new Label(book.getBookAuthor());
            authorNameLabel.setStyle("-fx-min-width: 100;");

            Label pageCountLabel = new Label(book.getBookPages());
            pageCountLabel.setStyle("-fx-min-width: 50;");

            Button deleteButton = new Button("Delete");
            deleteButton.setId("deleteButton");
            deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");

            // Your function implementation here
            // For example, you can call a method to handle the delete operation
            deleteButton.setOnAction(this::deleteBook);

            hbox.getChildren().addAll(bookID, bookNameLabel, authorNameLabel, pageCountLabel, deleteButton);
            libraryContainer.getChildren().add(hbox);
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
    protected void onForgetRedirectClick(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("change_password.fxml"));
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

    @FXML
    protected void onDeleteClick(){
        String USERNAME = usernameLoginField.getText();
        String PASSWORD = passwordLoginField.getText();
        if(connection.deleteAccount(USERNAME, PASSWORD)){
            System.out.println("Account Successfully Deleted");
        }
        else{
            System.out.println("Account Not Found");
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

    //change password
    @FXML
    protected void onChangePassword(ActionEvent event) throws Exception {
        String USERNAME = changeUsernameField.getText();
        String PASSWORD = changePasswordField.getText();
        String NEWPASSWORD = newPasswordField.getText();

        if(connection.changePassword(USERNAME, PASSWORD, NEWPASSWORD)){
            System.out.println("Password Successfully Changed");
            onLoginRedirectClick(event);
        }
        else{
            System.out.println("Account Does not Exist");
        }
    }
}