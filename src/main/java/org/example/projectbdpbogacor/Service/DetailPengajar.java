package org.example.projectbdpbogacor.Service;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class DetailPengajar {
    private final StringProperty usersUserId;       // Users_user_id (Part of composite PK, FK to Users)
    private final StringProperty kelasUsersUserId; // Kelas_Users_user_id (Part of composite PK, FK to Kelas)
    private final IntegerProperty matpelMapelId;    // Matpel_mapel_id (Part of composite PK, FK to Matpel)
    private final IntegerProperty kelasKelasId;     // Kelas_kelas_id (Part of composite PK, FK to Kelas)

    // Constructor
    public DetailPengajar(String usersUserId, String kelasUsersUserId,
                          int matpelMapelId, int kelasKelasId) {
        this.usersUserId = new SimpleStringProperty(usersUserId);
        this.kelasUsersUserId = new SimpleStringProperty(kelasUsersUserId);
        this.matpelMapelId = new SimpleIntegerProperty(matpelMapelId);
        this.kelasKelasId = new SimpleIntegerProperty(kelasKelasId);
    }

    // Getters
    public String getUsersUserId() { return usersUserId.get(); }
    public String getKelasUsersUserId() { return kelasUsersUserId.get(); }
    public int getMatpelMapelId() { return matpelMapelId.get(); }
    public int getKelasKelasId() { return kelasKelasId.get(); }

    // Property Methods
    public StringProperty usersUserIdProperty() { return usersUserId; }
    public StringProperty kelasUsersUserIdProperty() { return kelasUsersUserId; }
    public IntegerProperty matpelMapelIdProperty() { return matpelMapelId; }
    public IntegerProperty kelasKelasIdProperty() { return kelasKelasId; }

    // Setters (if needed)
    public void setUsersUserId(String usersUserId) { this.usersUserId.set(usersUserId); }
    public void setKelasUsersUserId(String kelasUsersUserId) { this.kelasUsersUserId.set(kelasUsersUserId); }
    public void setMatpelMapelId(int matpelMapelId) { this.matpelMapelId.set(matpelMapelId); }
    public void setKelasKelasId(int kelasKelasId) { this.kelasKelasId.set(kelasKelasId); }
}