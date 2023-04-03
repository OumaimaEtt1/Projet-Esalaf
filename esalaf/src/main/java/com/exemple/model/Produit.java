package com.exemple.model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Produit {
    private String nom_produit;
    private int Id_produit;

    private Button checkbox;

    public String getNom_produit() {
        return nom_produit;
    }

    public void setNom_produit(String nom_produit) {
        this.nom_produit = nom_produit;
    }

    public int getId_produit() {
        return Id_produit;
    }

    public void setId_produit(int Id_produit) {
        Id_produit = Id_produit;
    }

    public Button getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(Button checkbox) {
        this.checkbox = checkbox;
    }

    public Produit(int Id_produit, String nom_produit, Button checkbox, int id) {
        this.nom_produit = nom_produit;
        this.Id_produit = Id_produit;
        this.checkbox = checkbox;

        checkbox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event)  {
                String req = "INSERT INTO credit (Id , Id_produit) values (?,?)";


                DatabaseConnection connectNow = new DatabaseConnection();
                Connection connectDB = connectNow.getConnection();


                try {
                    PreparedStatement prepare = connectDB.prepareStatement(req);
                    prepare.setInt(1 , id);
                    prepare.setInt(2 , Id_produit);
                    if(prepare.execute() == false){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Produit bien ajouté !");
                        alert.showAndWait();
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }

    @Override
    public String toString() {
        return "Produit{" +
                "nom_produit='" + nom_produit + '\'' +
                ", Id_produit=" + Id_produit +
                ", checkbox=" + checkbox +
                '}';
    }

    public Produit(String nom_produit, int Id_produit) {
        this.nom_produit = nom_produit;
        Id_produit = Id_produit;
    }

}
