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

public class MainController{
    //start connection
    MainConnection connection = new MainConnection();

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

    //MAIN PAGE REDIRECT
    @FXML
    protected void executeMainPageRedirect(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main_page.fxml"));

        Scene newScene = new Scene(loader.load());
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(newScene);
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
