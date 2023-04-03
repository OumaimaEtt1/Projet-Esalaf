package com.example.esalaf;

import com.exemple.model.Client;
import com.exemple.model.Credit;
import com.exemple.model.DatabaseConnection;
import com.mysql.cj.x.protobuf.MysqlxPrepare;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.FloatStringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private AnchorPane clientsView;
    @FXML
    private AnchorPane creditsView;
    @FXML
    private AnchorPane homeView;
    @FXML
    private Button homebtn;
    @FXML
    private Button creditbtn;
    @FXML
    private Button clientbtn;

    //Boutons view Crédits
    @FXML
    private TextField creditID;
    @FXML
    private TextField creditRecu;
    @FXML
    private Label creditRestant;

    @FXML
    private Label nbrcredits;

    @FXML
    private Label creditspayes;
    @FXML
    private Label creditNom;
    @FXML
    private Label creditTel;
    @FXML
    private Button creditUpdate;
    @FXML
    private TableView<Credit> creditTableview;
    @FXML
    private TableColumn<Credit, Integer> ID_credit;
    @FXML
    private TableColumn<Credit, Float> montant_recu;
    @FXML
    private TableColumn<Credit, Float> montant_restant;
    @FXML
    private TableColumn<Credit, String> Nom;

    //Boutons view Clients
    @FXML
    private TextField clientNom;
    @FXML
    private TextField clientTel;
    @FXML
    private TextField clientMontant;
    @FXML
    private TableView<Client> clientTableview;
    @FXML
    private TableColumn<Client, String> clientNom_col;
    @FXML
    private TableColumn<Client, String> clientTel_col;
    @FXML
    private TableColumn<Client, String> clientMontant_col;

    @FXML
    private TableColumn <Client, Integer> id_client;
    @FXML
    private Button clientSave;
    @FXML
    private Button clientUpdate;
    @FXML
    private Button clientClear;
    @FXML
    private Button clientDelete;
    @FXML
    private TextField clientSearch;
    private Connection connect;
    private Statement statement;
    private PreparedStatement prepare;
    private ResultSet result;

/////////////////////////// Switch Views ////////////////////////////////////////

    //Changer views selon le choix séléctionné (visibilty true/false)
    public void SwitchViews(ActionEvent event){
        if (event.getSource() == homebtn){
            homeView.setVisible(true);
            clientsView.setVisible(false);
            creditsView.setVisible(false);
        }else if (event.getSource() == clientbtn){
            homeView.setVisible(false);
            clientsView.setVisible(true);
            creditsView.setVisible(false);
            ClientShowListData();
            addClientsListData();

        }else if (event.getSource() == creditbtn){
            homeView.setVisible(false);
            clientsView.setVisible(false);
            creditsView.setVisible(true);
            ClientShowListData();
            CreditShowListData();
        }

    }
//////////////////////// Déconnexion /////////////////////////////////////////

    //Fonction de Logout() pour se déconnecter
    @FXML
    public void Logout(ActionEvent event ) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root , 791, 419);
        stage.setScene(scene);
        stage.resizableProperty().setValue(false);
        stage.show();
    }
    
    @FXML
    public void afficherReste (ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("dashboard-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root , 1227 , 680);
        stage.setScene(scene);
        stage.resizableProperty().setValue(false);
        stage.show();
    }
//////////////////////////// Clear ///////////////////////////////////

    //Supprimer le texte des champs saisis
    public void Clear() {
        clientNom.setText("");
        clientTel.setText("");
        clientMontant.setText("");
    }
//////////////////////// Sélectionner un élément du tableau ////////////////

    //cliquer sur un client et l'afficher dans les TextFields
    public void ClientSelect() {
        Client client = clientTableview.getSelectionModel().getSelectedItem();
        int num = clientTableview.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }
        clientNom.setText(client.getNom());
        clientTel.setText(client.getTelephone());
        float Montant_Credit = client.getMontant_credit();
        String Montant_CreditStr = String.format("%.2f", Montant_Credit);
        clientMontant.setText(Montant_CreditStr);
    }
////////////////////////// Delete //////////////////////////////////
    public void Delete() throws SQLException {

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String deleteInfo = "DELETE FROM client WHERE Nom = '"
                + clientNom.getText() + "'";

        prepare = connectDB.prepareStatement(deleteInfo);
        prepare.executeUpdate();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Message");
        alert.setHeaderText(null);
        alert.setContentText("Successfully Deleted!");
        alert.showAndWait();
        Clear();
        ClientShowListData();
    }
///////////////////////////////  Update  ////////////////////////////////////
    public void Update() throws SQLException {

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String updateInfo = "UPDATE client SET Nom = '"
                + clientNom.getText() + "', Telephone = '"
                + clientTel.getText() + "', Montant_credit = '"
                + clientMontant.getText()
                + "' WHERE Nom = '"
                + clientNom.getText() + "'";

        prepare = connectDB.prepareStatement(updateInfo);
        prepare.executeUpdate();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Message");
        alert.setHeaderText(null);
        alert.setContentText("Successfully Updated!");
        alert.showAndWait();
        Clear();
        ClientShowListData();

    }

/////////////////////////////  Clients  ///////////////////////////////////////////////
    public ObservableList<Client> addClientsListData(){

        ObservableList<Client> listData = FXCollections.observableArrayList();
        String sql = "SELECT Id,Nom, Telephone, Montant_credit FROM client";

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        try {
            prepare = connectDB.prepareStatement(sql);
            result = prepare.executeQuery();
            Client client;

            while (result.next()) {
                client = new Client
                        (result.getString("Nom"),
                        result.getString("Telephone"),
                        result.getFloat("Montant_credit"),
                        result.getInt("Id"));
                listData.add(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }
////////////////////// Afficher les clients ajoutés dans le tableau //////////////////////

    //Fonction pour afficher les informations saisies dans le tableau
    public void ClientShowListData() {

        clientNom_col.setCellValueFactory(new PropertyValueFactory<>("Nom"));
        clientTel_col.setCellValueFactory(new PropertyValueFactory<>("Telephone"));
        clientMontant_col.setCellValueFactory(new PropertyValueFactory<>("Montant_credit"));
        id_client.setCellValueFactory(new PropertyValueFactory<Client , Integer>("Id"));
        clientTableview.setItems(addClientsListData());
    }

///////////////////////// Enregistrer les clients dans la base de données ///////////////////////

    //Fonction pour insérer les informations saisies dans DB
    public void Saveclient() throws SQLException {
        String sql = "INSERT INTO client "
                + "(Nom, Telephone, Montant_credit) "
                + "VALUES(?,?,?)";

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        try {
            Alert alert;
            if (clientNom.getText().isEmpty()
                    || clientTel.getText().isEmpty()
                    || clientMontant.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }
            else {
                prepare = connectDB.prepareStatement(sql);
                prepare.setString(1, clientNom.getText());
                prepare.setString(2, clientTel.getText());
                prepare.setString(3, clientMontant.getText());
                prepare.executeUpdate();

                addClientsListData();
                ClientShowListData();
                Clear();

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

///////////////////////////  Credits  //////////////////////////////////////////////////
    public ObservableList<Credit> addCreditsListData(){

        ObservableList<Credit> listData = FXCollections.observableArrayList();
        String sql = "SELECT credit.ID_credit, client.nom, credit.montant_recu, (client.montant_credit - credit.montant_recu) as montant_restant FROM credit INNER JOIN client ON credit.id = client.Id;";

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        try {
            prepare = connectDB.prepareStatement(sql);
            result = prepare.executeQuery();
            Credit credit;

            while (result.next()) {
                credit = new Credit
                        (result.getInt("ID_credit"),
                                result.getFloat("montant_recu"),
                                result.getFloat("montant_restant"),
                                result.getString("Nom"));
                listData.add(credit);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }
///////////////////////// Afficher les crédits dans le tableau ////////////////////////////
    public void CreditShowListData() {
        ID_credit.setCellValueFactory(new PropertyValueFactory<Credit, Integer>("ID_credit"));
        Nom.setCellValueFactory(new PropertyValueFactory<Credit, String>("Nom"));
        montant_recu.setCellValueFactory(new PropertyValueFactory<Credit, Float>("montant_recu"));
        montant_restant.setCellValueFactory(new PropertyValueFactory<Credit, Float>("montant_restant"));
        creditTableview.setItems(addCreditsListData());

        creditTableview.setEditable(true);
        montant_recu.setEditable(true);


        montant_recu.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));

    }
///////////////////// Editer le montant reçu ///////////////////////////////////////////

    //Modifier le montant en cliquant sur la colonne "montant reçu"
    @FXML
    public void edit_recu (TableColumn.CellEditEvent cellEditEvent) throws SQLException {

        Credit c = creditTableview.getSelectionModel().getSelectedItem();
        System.out.println(c.toString());
        c.setMontant_recu((Float) cellEditEvent.getNewValue());
        String sql = "UPDATE credit SET montant_recu = ? WHERE ID_credit = ?;";

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        prepare = connectDB.prepareStatement(sql);
        prepare.setFloat(1, c.getMontant_recu());
        prepare.setInt(2, c.getID_credit());

        prepare.executeUpdate();
    }
/////////////////////// Déclarer les produits ////////////////////////////////////////
    @FXML
    public void DeclarerProduit(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("produits.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root , 916 , 561);

        int id_client = clientTableview.getSelectionModel().getSelectedItem().getId();

        ProduitsController produitsController = loader.getController();
        produitsController.setId_client(id_client);
        produitsController.ProduitShowListData();

        System.out.println(id_client);

        stage.setScene(scene);
        stage.resizableProperty().setValue(false);
        stage.show();

    }

    ////////////////////   Crédits disponibles  ////////////////////////////////////
    public void CreditsAvailable() {

        String sql = "SELECT COUNT(ID_credit) FROM credit";

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        int countData = 0;
        try {
            statement = connectDB.createStatement();
            result = statement.executeQuery(sql);

            while (result.next()) {
                countData = result.getInt("COUNT(ID_credit)");
            }
            nbrcredits.setText(String.valueOf(countData));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
///////////////////// Calculer le nombre de crédits payés  /////////////////////////////////////////////////////
    public void CreditsPayes() {

        String sql = "SELECT COUNT(ID_credit) FROM credit WHERE montant_recu = 0;";

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        int countData = 0;
        try {
            statement = connectDB.createStatement();
            result = statement.executeQuery(sql);

            while (result.next()) {
                countData = result.getInt("COUNT(ID_credit)");
            }
            creditspayes.setText(String.valueOf(countData));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



////////////////// initialize /////////////////////////////////////////////////////
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Client> Clients = addClientsListData();
        ClientShowListData();
        CreditShowListData();
        CreditsAvailable();
        CreditsPayes();

    }


}
