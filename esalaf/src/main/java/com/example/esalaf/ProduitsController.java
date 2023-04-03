package com.example.esalaf;

import com.exemple.model.Credit;
import com.exemple.model.DatabaseConnection;
import javafx.collections.transformation.FilteredList;
import com.exemple.model.Produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;


public class ProduitsController implements Initializable {

    @FXML
    private TableView<Produit> produitTableview;
    @FXML
    private TableColumn<Produit, Integer> Id_produit;
    @FXML
    private TableColumn<Produit, String> nom_produit;
    @FXML
    private TableColumn<Produit, Button> checkbox;

    @FXML
    private TextField search;

    public int getId_client() {
        return id_client;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    private int id_client;

///////////////////////////////// Back //////////////////////////////////////////////////
    @FXML
    public void Back (ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("dashboard-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root , 1227 , 680);
        stage.setScene(scene);
        stage.resizableProperty().setValue(false);
        stage.show();
    }

    //////////////////////// Ajouter le produits dans la base de données /////////////////////////
    public ObservableList<Produit> addProduitsListData(){

        ObservableList<Produit> listData = FXCollections.observableArrayList();
        String sql = "SELECT Id_produit, nom_produit FROM produit;";

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        try {
            PreparedStatement prepare = connectDB.prepareStatement(sql);
            ResultSet result = prepare.executeQuery();
            Produit produit = null;

            while (result.next()) {
                produit = new Produit
                        (
                                result.getInt("Id_produit"),
                                result.getString("nom_produit"),
                            new Button("Ajouter Produit"),
                                this.getId_client()
                        );

                listData.add(produit);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

 /////////////////////////// Afficher les produits dans le tableau ////////////////////////
    public void ProduitShowListData() {
        Id_produit.setCellValueFactory(new PropertyValueFactory<Produit, Integer>("Id_produit"));
        nom_produit.setCellValueFactory(new PropertyValueFactory<Produit, String>("nom_produit"));
        checkbox.setCellValueFactory(new PropertyValueFactory<Produit, Button >("checkbox"));

        produitTableview.setItems(addProduitsListData());

        System.out.println(getId_client());

    }


 /////////////////////////  Search /////////////////////////////////////////////////////////////
private ObservableList<Produit>addProduitsList;
    public void ProductSearch() {
        addProduitsList = addProduitsListData();

        FilteredList<Produit> filter = new FilteredList<>(addProduitsList, e -> true);

        search.textProperty().addListener((Observable, oldValue, newValue) -> {

            filter.setPredicate(predicateProductData -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if (predicateProductData.getNom_produit().toLowerCase().contains(searchKey)) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<Produit> sortList = new SortedList<>(filter);
        sortList.comparatorProperty().bind(produitTableview.comparatorProperty());
        produitTableview.setItems(sortList);
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ProduitShowListData();
    }
}
