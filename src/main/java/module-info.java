module org.example.projectbdpbogacor {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.projectbdpbogacor to javafx.fxml;
    exports org.example.projectbdpbogacor;
}