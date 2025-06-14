package org.example.projectbdpbogacor.Service;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class Kelas {
    private final StringProperty usersUserId;     // Users_user_id (Part of composite PK, FK)
    private final IntegerProperty kelasId;        // kelas_id (Part of composite PK)
    private final StringProperty namaKelas;       // nama_kelas
    private final StringProperty keterangan;      // keterangan
    private final IntegerProperty semesterSemesterId; // Semester_semester_id (FK)

    // Constructor
    public Kelas(String usersUserId, int kelasId, String namaKelas,
                 String keterangan, int semesterSemesterId) {
        this.usersUserId = new SimpleStringProperty(usersUserId);
        this.kelasId = new SimpleIntegerProperty(kelasId);
        this.namaKelas = new SimpleStringProperty(namaKelas);
        this.keterangan = new SimpleStringProperty(keterangan);
        this.semesterSemesterId = new SimpleIntegerProperty(semesterSemesterId);
    }

    // Getters
    public String getUsersUserId() { return usersUserId.get(); }
    public int getKelasId() { return kelasId.get(); }
    public String getNamaKelas() { return namaKelas.get(); }
    public String getKeterangan() { return keterangan.get(); }
    public int getSemesterSemesterId() { return semesterSemesterId.get(); }

    // Property Methods
    public StringProperty usersUserIdProperty() { return usersUserId; }
    public IntegerProperty kelasIdProperty() { return kelasId; }
    public StringProperty namaKelasProperty() { return namaKelas; }
    public StringProperty keteranganProperty() { return keterangan; }
    public IntegerProperty semesterSemesterIdProperty() { return semesterSemesterId; }

    // Setters
    public void setNamaKelas(String namaKelas) { this.namaKelas.set(namaKelas); }
    public void setKeterangan(String keterangan) { this.keterangan.set(keterangan); }
    public void setSemesterSemesterId(int semesterSemesterId) { this.semesterSemesterId.set(semesterSemesterId); }
    public void setUsersUserId(String usersUserId) { this.usersUserId.set(usersUserId); } // If changing wali kelas
}