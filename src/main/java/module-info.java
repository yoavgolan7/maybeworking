module com.example.maybeworking {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.maybeworking to javafx.fxml;
    exports com.example.maybeworking;
}