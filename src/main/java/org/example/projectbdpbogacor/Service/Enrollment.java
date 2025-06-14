package org.example.projectbdpbogacor.Service;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class Enrollment {
    private final StringProperty usersUserId;        // Users_user_id (Part of composite PK, FK to Users)
    private final IntegerProperty kelasKelasId;      // Kelas_kelas_id (Part of composite PK, FK to Kelas)
    private final StringProperty kelasUsersUserId; // Kelas_Users_user_id (Part of composite PK, FK to Kelas)

    // Constructor
    public Enrollment(String usersUserId, int kelasKelasId, String kelasUsersUserId) {
        this.usersUserId = new SimpleStringProperty(usersUserId);
        this.kelasKelasId = new SimpleIntegerProperty(kelasKelasId);
        this.kelasUsersUserId = new SimpleStringProperty(kelasUsersUserId);
    }

    // Getters
    public String getUsersUserId() { return usersUserId.get(); }
    public int getKelasKelasId() { return kelasKelasId.get(); }
    public String getKelasUsersUserId() { return kelasUsersUserId.get(); }

    // Property Methods
    public StringProperty usersUserIdProperty() { return usersUserId; }
    public IntegerProperty kelasKelasIdProperty() { return kelasKelasId; }
    public StringProperty kelasUsersUserIdProperty() { return kelasUsersUserId; }

    // Setters (if needed, e.g., if re-assigning enrollment)
    public void setUsersUserId(String usersUserId) { this.usersUserId.set(usersUserId); }
    public void setKelasKelasId(int kelasKelasId) { this.kelasKelasId.set(kelasKelasId); }
    public void setKelasUsersUserId(String kelasUsersUserId) { this.kelasUsersUserId.set(kelasUsersUserId); }
}