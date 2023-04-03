package com.example.esalaf;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.exemple.model.LoginDAO;
import com.exemple.model.Login;

import java.io.IOException;
import java.sql.SQLException;


public class LoginController {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label invalidLogin;


    public  void login (ActionEvent event) throws SQLException, IOException {
        LoginDAO connected = new LoginDAO();
        Login login = connected.checklogin(new Login(emailField.getText(),passwordField.getText()));

        if (emailField.getText().isBlank() || passwordField.getText().isBlank()){
            invalidLogin.setText("Veuillez saisir les informations correctement ");
        } else if (login == null) {
            invalidLogin.setText("Votre adresse email ou mot de passe est incorrect");
        }
        else {
            Parent root = FXMLLoader.load(getClass().getResource("dashboard-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root , 1227 , 680);
            stage.setScene(scene);
            stage.resizableProperty().setValue(false);
            stage.show();
        }

    }
    public void register(ActionEvent event ) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("register-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root , 835 , 416);
        stage.setScene(scene);
        stage.resizableProperty().setValue(false);
        stage.show();
    }





}
