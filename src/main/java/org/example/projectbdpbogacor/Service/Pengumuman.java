package org.example.projectbdpbogacor.Service;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.ObjectProperty;

import java.time.LocalDateTime;

public class Pengumuman {
    private final IntegerProperty pengumumanId;     // pengumuman_id (PRIMARY KEY)
    private final StringProperty pengumumanContent; // pengumuman (VARCHAR(255))
    private final StringProperty usersUserId;       // Users_user_id (Foreign Key)
    private final ObjectProperty<LocalDateTime> waktu; // waktu (TIMESTAMP)

    // Constructor
    public Pengumuman(int pengumumanId, String pengumumanContent, String usersUserId, LocalDateTime waktu) {
        this.pengumumanId = new SimpleIntegerProperty(pengumumanId);
        this.pengumumanContent = new SimpleStringProperty(pengumumanContent);
        this.usersUserId = new SimpleStringProperty(usersUserId);
        this.waktu = new SimpleObjectProperty<>(waktu);
    }

    // Constructor fo pengumuman guru
    public Pengumuman(int pengumumanId, String pengumumanContent,  LocalDateTime waktu) {
        this.pengumumanId = new SimpleIntegerProperty(pengumumanId);
        this.pengumumanContent = new SimpleStringProperty(pengumumanContent);
        this.usersUserId = new SimpleStringProperty("");
        this.waktu = new SimpleObjectProperty<>(waktu);
    }

    // Getters
    public int getPengumumanId() { return pengumumanId.get(); }
    public String getPengumumanContent() { return pengumumanContent.get(); }
    public String getUsersUserId() { return usersUserId.get(); }
    public LocalDateTime getWaktu() { return waktu.get(); }

    // Property Methods
    public IntegerProperty pengumumanIdProperty() { return pengumumanId; }
    public StringProperty pengumumanContentProperty() { return pengumumanContent; }
    public StringProperty usersUserIdProperty() { return usersUserId; }
    public ObjectProperty<LocalDateTime> waktuProperty() { return waktu; }

    // Setters
    public void setPengumumanContent(String pengumumanContent) { this.pengumumanContent.set(pengumumanContent); }
    public void setUsersUserId(String usersUserId) { this.usersUserId.set(usersUserId); }
    public void setWaktu(LocalDateTime waktu) { this.waktu.set(waktu); }
}