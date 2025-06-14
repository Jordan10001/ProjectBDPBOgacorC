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
    private final StringProperty deskripsi;
    private final ObjectProperty<LocalDateTime> tanggalUpload; // Asumsi ada kolom ini di DB
    private final StringProperty fileUrl; // Asumsi ada kolom ini di DB
    private final StringProperty usersUserIdPoster; // Asumsi ada kolom user_id yang mengupload

    // Constructor (dengan asumsi kolom tambahan)
    public Materi(int materiId, String namaMateri, String kelasUsersUserId,
                  int matpelMapelId, int kelasKelasId, String deskripsi,
                  LocalDateTime tanggalUpload, String fileUrl, String usersUserIdPoster) {
        this.materiId = new SimpleIntegerProperty(materiId);
        this.namaMateri = new SimpleStringProperty(namaMateri);
        this.kelasUsersUserId = new SimpleStringProperty(kelasUsersUserId);
        this.matpelMapelId = new SimpleIntegerProperty(matpelMapelId);
        this.kelasKelasId = new SimpleIntegerProperty(kelasKelasId);
        this.deskripsi = new SimpleStringProperty(deskripsi);
        this.tanggalUpload = new SimpleObjectProperty<>(tanggalUpload);
        this.fileUrl = new SimpleStringProperty(fileUrl);
        this.usersUserIdPoster = new SimpleStringProperty(usersUserIdPoster);
    }

    // Constructor (minimal sesuai skema yang Anda berikan)
    public Materi(int materiId, String namaMateri, String kelasUsersUserId,
                  int matpelMapelId, int kelasKelasId) {
        this(materiId, namaMateri, kelasUsersUserId, matpelMapelId, kelasKelasId,
                "", null, "", ""); // Default values for missing fields
    }


    // Getters
    public int getMateriId() { return materiId.get(); }
    public String getNamaMateri() { return namaMateri.get(); }
    public String getKelasUsersUserId() { return kelasUsersUserId.get(); }
    public int getMatpelMapelId() { return matpelMapelId.get(); }
    public int getKelasKelasId() { return kelasKelasId.get(); }
    public String getDeskripsi() { return deskripsi.get(); }
    public LocalDateTime getTanggalUpload() { return tanggalUpload.get(); }
    public String getFileUrl() { return fileUrl.get(); }
    public String getUsersUserIdPoster() { return usersUserIdPoster.get(); }

    // Property Methods
    public IntegerProperty materiIdProperty() { return materiId; }
    public StringProperty namaMateriProperty() { return namaMateri; }
    public StringProperty kelasUsersUserIdProperty() { return kelasUsersUserId; }
    public IntegerProperty matpelMapelIdProperty() { return matpelMapelId; }
    public IntegerProperty kelasKelasIdProperty() { return kelasKelasId; }
    public StringProperty deskripsiProperty() { return deskripsi; }
    public ObjectProperty<LocalDateTime> tanggalUploadProperty() { return tanggalUpload; }
    public StringProperty fileUrlProperty() { return fileUrl; }
    public StringProperty usersUserIdPosterProperty() { return usersUserIdPoster; }

    // Setters
    public void setNamaMateri(String namaMateri) { this.namaMateri.set(namaMateri); }
    public void setKelasUsersUserId(String kelasUsersUserId) { this.kelasUsersUserId.set(kelasUsersUserId); }
    public void setMatpelMapelId(int matpelMapelId) { this.matpelMapelId.set(matpelMapelId); }
    public void setKelasKelasId(int kelasKelasId) { this.kelasKelasId.set(kelasKelasId); }
    public void setDeskripsi(String deskripsi) { this.deskripsi.set(deskripsi); }
    public void setTanggalUpload(LocalDateTime tanggalUpload) { this.tanggalUpload.set(tanggalUpload); }
    public void setFileUrl(String fileUrl) { this.fileUrl.set(fileUrl); }
    public void setUsersUserIdPoster(String usersUserIdPoster) { this.usersUserIdPoster.set(usersUserIdPoster); }
}