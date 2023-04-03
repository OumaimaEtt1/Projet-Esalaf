package com.example.esalaf;

import com.exemple.model.BaseDAO;
import com.exemple.model.DatabaseConnection;
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
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;


public class RegisterController {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label invalidRegister;

    public RegisterController() throws SQLException {
        super();
    }

    //fonction pour insérer les données de l'utilisateur dans la table "login"
    public void registerUser(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String email = emailField.getText();
        String password = passwordField.getText();

        String insertToRegister = String.format("INSERT INTO login (email, password) VALUES ('%s', '%s')", email, password);

        try {
            Statement statement = connectDB.createStatement();
            var i = statement.executeUpdate(insertToRegister);

        } catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }

    //fonction qui fait appel à registerUser(): insérer l'utilisateur dans DB pour ensuite le redériger vers Dashboard
    @FXML
    public void registerButton (ActionEvent event) throws IOException {
        registerUser();

        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isBlank() || password.isBlank()) {
            invalidRegister.setText("Veuillez saisir les informations correctement !");
        } else {
        Parent root = FXMLLoader.load(getClass().getResource("dashboard-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root , 1227 , 680);
        stage.setScene(scene);
        stage.resizableProperty().setValue(false);
        stage.show();
        }
    }

}
