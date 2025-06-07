module org.example.projectbdpbogacor {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.prefs;

    opens org.example.projectbdpbogacor to javafx.fxml;
    opens org.example.projectbdpbogacor.Admin to javafx.fxml;
    opens org.example.projectbdpbogacor.Kepala to javafx.fxml;
    opens org.example.projectbdpbogacor.Guru to javafx.fxml;
    opens org.example.projectbdpbogacor.Wali to javafx.fxml;
    opens org.example.projectbdpbogacor.Siswa to javafx.fxml;

    exports org.example.projectbdpbogacor;
    exports org.example.projectbdpbogacor.Admin; // Export if you have classes in these packages that need to be accessed
    exports org.example.projectbdpbogacor.Kepala;
    exports org.example.projectbdpbogacor.Guru;
    exports org.example.projectbdpbogacor.Wali;
    exports org.example.projectbdpbogacor.Siswa;
}