package org.example.projectbdpbogacor.Service;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDateTime;

public class Materi {
    private final IntegerProperty materiId;         // materi_id (PRIMARY KEY)
    private final StringProperty namaMateri;       // nama_materi
    private final StringProperty kelasUsersUserId; // Kelas_Users_user_id (Part of FK to Kelas)
    private final IntegerProperty matpelMapelId;    // Matpel_mapel_id (FK to Matpel)
    private final IntegerProperty kelasKelasId;     // Kelas_kelas_id (Part of FK to Kelas)
    // Asumsi ada kolom tanggal_upload atau sejenisnya di Materi jika ingin mereplikasi MateriEntry
    // Menambahkan field tambahan yang umum ada di tabel materi, jika tidak ada di skema Anda bisa abaikan.

    // Constructor (dengan asumsi kolom tambahan)
    public Materi(int materiId, String namaMateri, String kelasUsersUserId,
                  int matpelMapelId, int kelasKelasId) {
        this.materiId = new SimpleIntegerProperty(materiId);
        this.namaMateri = new SimpleStringProperty(namaMateri);
        this.kelasUsersUserId = new SimpleStringProperty(kelasUsersUserId);
        this.matpelMapelId = new SimpleIntegerProperty(matpelMapelId);
        this.kelasKelasId = new SimpleIntegerProperty(kelasKelasId);
    }

    public Materi(int materiId, String namaMateri, String kelasUsersUserId,
                   int kelasKelasId) {
        this.materiId = new SimpleIntegerProperty(materiId);
        this.namaMateri = new SimpleStringProperty(namaMateri);
        this.kelasUsersUserId = new SimpleStringProperty(kelasUsersUserId);
        this.matpelMapelId = new SimpleIntegerProperty();
        this.kelasKelasId = new SimpleIntegerProperty(kelasKelasId);
    }

    // Constructor (minimal sesuai skema yang Anda berikan)
    public Materi(String namaMateri) {
        this.materiId = new SimpleIntegerProperty();
        this.namaMateri = new SimpleStringProperty(namaMateri);
        this.kelasUsersUserId = new SimpleStringProperty();
        this.matpelMapelId = new SimpleIntegerProperty();
        this.kelasKelasId = new SimpleIntegerProperty();
    }


    // Getters
    public int getMateriId() { return materiId.get(); }
    public String getNamaMateri() { return namaMateri.get(); }
    public String getKelasUsersUserId() { return kelasUsersUserId.get(); }
    public int getMatpelMapelId() { return matpelMapelId.get(); }
    public int getKelasKelasId() { return kelasKelasId.get(); }


    // Property Methods
    public IntegerProperty materiIdProperty() { return materiId; }
    public StringProperty namaMateriProperty() { return namaMateri; }
    public StringProperty kelasUsersUserIdProperty() { return kelasUsersUserId; }
    public IntegerProperty matpelMapelIdProperty() { return matpelMapelId; }
    public IntegerProperty kelasKelasIdProperty() { return kelasKelasId; }


    // Setters
    public void setNamaMateri(String namaMateri) { this.namaMateri.set(namaMateri); }
    public void setKelasUsersUserId(String kelasUsersUserId) { this.kelasUsersUserId.set(kelasUsersUserId); }
    public void setMatpelMapelId(int matpelMapelId) { this.matpelMapelId.set(matpelMapelId); }
    public void setKelasKelasId(int kelasKelasId) { this.kelasKelasId.set(kelasKelasId); }

}