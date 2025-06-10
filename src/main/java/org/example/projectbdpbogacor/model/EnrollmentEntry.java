package org.example.projectbdpbogacor.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EnrollmentEntry {
    private final StringProperty userId; // ID pengguna yang terdaftar (siswa)
    private final StringProperty namaSiswa; // Nama siswa yang terdaftar
    private final StringProperty kelasId; // ID kelas tempat siswa terdaftar (bagian dari PK komposit Kelas)
    private final StringProperty namaKelas; // Nama kelas tempat siswa terdaftar

    public EnrollmentEntry(String userId, String namaSiswa, String kelasId, String namaKelas) {
        this.userId = new SimpleStringProperty(userId);
        this.namaSiswa = new SimpleStringProperty(namaSiswa);
        this.kelasId = new SimpleStringProperty(kelasId);
        this.namaKelas = new SimpleStringProperty(namaKelas);
    }

    // Getters for JavaFX TableView
    public String getUserId() { return userId.get(); }
    public StringProperty userIdProperty() { return userId; }

    public String getNamaSiswa() { return namaSiswa.get(); }
    public StringProperty namaSiswaProperty() { return namaSiswa; }

    public String getKelasId() { return kelasId.get(); }
    public StringProperty kelasIdProperty() { return kelasId; }

    public String getNamaKelas() { return namaKelas.get(); }
    public StringProperty namaKelasProperty() { return namaKelas; }
}