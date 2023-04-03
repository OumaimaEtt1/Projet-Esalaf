module com.example.esalaf {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires mysql.connector.j;


    opens com.example.esalaf to javafx.fxml;
    exports com.example.esalaf;
    exports com.exemple.model;

}