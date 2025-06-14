package org.example.projectbdpbogacor.Service;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.ObjectProperty;

import java.time.LocalDateTime; // Untuk kolom TIMESTAMP

public class Absensi {
    private final IntegerProperty absensiId;      // absensi_id (PRIMARY KEY)
    private final ObjectProperty<LocalDateTime> tanggal; // tanggal (TIMESTAMP)
    private final StringProperty status;         // status (VARCHAR(50))
    private final StringProperty usersUserId;    // Users_user_id (Foreign Key)
    private final IntegerProperty jadwalJadwalId; // Jadwal_jadwal_id (Foreign Key)

    // Constructor
    public Absensi(int absensiId, LocalDateTime tanggal, String status,
                   String usersUserId, int jadwalJadwalId) {
        this.absensiId = new SimpleIntegerProperty(absensiId);
        this.tanggal = new SimpleObjectProperty<>(tanggal);
        this.status = new SimpleStringProperty(status);
        this.usersUserId = new SimpleStringProperty(usersUserId);
        this.jadwalJadwalId = new SimpleIntegerProperty(jadwalJadwalId);
    }

    // Getters
    public int getAbsensiId() { return absensiId.get(); }
    public LocalDateTime getTanggal() { return tanggal.get(); }
    public String getStatus() { return status.get(); }
    public String getUsersUserId() { return usersUserId.get(); }
    public int getJadwalJadwalId() { return jadwalJadwalId.get(); }

    // Property Methods
    public IntegerProperty absensiIdProperty() { return absensiId; }
    public ObjectProperty<LocalDateTime> tanggalProperty() { return tanggal; }
    public StringProperty statusProperty() { return status; }
    public StringProperty usersUserIdProperty() { return usersUserId; }
    public IntegerProperty jadwalJadwalIdProperty() { return jadwalJadwalId; }

    // Setters
    public void setTanggal(LocalDateTime tanggal) { this.tanggal.set(tanggal); }
    public void setStatus(String status) { this.status.set(status); }
    public void setUsersUserId(String usersUserId) { this.usersUserId.set(usersUserId); }
    public void setJadwalJadwalId(int jadwalJadwalId) { this.jadwalJadwalId.set(jadwalJadwalId); }
}