package org.example.projectbdpbogacor.Service;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.ObjectProperty;

import java.time.LocalDateTime;

public class Tugas {
    private final IntegerProperty tugasId;          // tugas_id (PRIMARY KEY)
    private final StringProperty keterangan;       // keterangan (renamed from judulTugas/deskripsi)
    private final ObjectProperty<LocalDateTime> deadline; // deadline (TIMESTAMP)
    private final ObjectProperty<LocalDateTime> tanggalDirelease; // tanggal_direlease (TIMESTAMP)
    private final StringProperty kelasUsersUserId; // Kelas_Users_user_id (Part of FK to Kelas)
    private final IntegerProperty matpelMapelId;    // Matpel_mapel_id (FK to Matpel)
    private final IntegerProperty kelasKelasId;     // Kelas_kelas_id (Part of FK to Kelas)

    // Constructor
    public Tugas(int tugasId, String keterangan, LocalDateTime deadline,
                 LocalDateTime tanggalDirelease, String kelasUsersUserId,
                 int matpelMapelId, int kelasKelasId) {
        this.tugasId = new SimpleIntegerProperty(tugasId);
        this.keterangan = new SimpleStringProperty(keterangan);
        this.deadline = new SimpleObjectProperty<>(deadline);
        this.tanggalDirelease = new SimpleObjectProperty<>(tanggalDirelease);
        this.kelasUsersUserId = new SimpleStringProperty(kelasUsersUserId);
        this.matpelMapelId = new SimpleIntegerProperty(matpelMapelId);
        this.kelasKelasId = new SimpleIntegerProperty(kelasKelasId);
    }

    public Tugas(int tugasId, String keterangan, LocalDateTime deadline,
                 LocalDateTime tanggalDirelease, String kelasUsersUserId,
                 int kelasKelasId) {
        this.tugasId = new SimpleIntegerProperty(tugasId);
        this.keterangan = new SimpleStringProperty(keterangan);
        this.deadline = new SimpleObjectProperty<>(deadline);
        this.tanggalDirelease = new SimpleObjectProperty<>(tanggalDirelease);
        this.kelasUsersUserId = new SimpleStringProperty(kelasUsersUserId);
        this.matpelMapelId = new SimpleIntegerProperty();
        this.kelasKelasId = new SimpleIntegerProperty(kelasKelasId);
    }

    public Tugas( String keterangan, LocalDateTime deadline,
                 LocalDateTime tanggalDirelease) {
        this.tugasId = new SimpleIntegerProperty();
        this.keterangan = new SimpleStringProperty(keterangan);
        this.deadline = new SimpleObjectProperty<>(deadline);
        this.tanggalDirelease = new SimpleObjectProperty<>(tanggalDirelease);
        this.kelasUsersUserId = new SimpleStringProperty();
        this.matpelMapelId = new SimpleIntegerProperty();
        this.kelasKelasId = new SimpleIntegerProperty();
    }

    // Getters
    public int getTugasId() { return tugasId.get(); }
    public String getKeterangan() { return keterangan.get(); }
    public LocalDateTime getDeadline() { return deadline.get(); }
    public LocalDateTime getTanggalDirelease() { return tanggalDirelease.get(); }
    public String getKelasUsersUserId() { return kelasUsersUserId.get(); }
    public int getMatpelMapelId() { return matpelMapelId.get(); }
    public int getKelasKelasId() { return kelasKelasId.get(); }

    // Property Methods
    public IntegerProperty tugasIdProperty() { return tugasId; }
    public StringProperty keteranganProperty() { return keterangan; }
    public ObjectProperty<LocalDateTime> deadlineProperty() { return deadline; }
    public ObjectProperty<LocalDateTime> tanggalDireleaseProperty() { return tanggalDirelease; }
    public StringProperty kelasUsersUserIdProperty() { return kelasUsersUserId; }
    public IntegerProperty matpelMapelIdProperty() { return matpelMapelId; }
    public IntegerProperty kelasKelasIdProperty() { return kelasKelasId; }

    // Setters
    public void setKeterangan(String keterangan) { this.keterangan.set(keterangan); }
    public void setDeadline(LocalDateTime deadline) { this.deadline.set(deadline); }
    public void setTanggalDirelease(LocalDateTime tanggalDirelease) { this.tanggalDirelease.set(tanggalDirelease); }
    public void setKelasUsersUserId(String kelasUsersUserId) { this.kelasUsersUserId.set(kelasUsersUserId); }
    public void setMatpelMapelId(int matpelMapelId) { this.matpelMapelId.set(matpelMapelId); }
    public void setKelasKelasId(int kelasKelasId) { this.kelasKelasId.set(kelasKelasId); }
}