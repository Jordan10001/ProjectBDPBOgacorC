package org.example.projectbdpbogacor.Service;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class Jadwal {
    private final IntegerProperty jadwalId;          // jadwal_id (PRIMARY KEY)
    private final StringProperty hari;               // hari
    private final StringProperty jamMulai;           // jam_mulai
    private final StringProperty jamSelesai;         // jam_selsai
    private final StringProperty kelasUsersUserId;   // Kelas_Users_user_id (Part of FK to Kelas)
    private final IntegerProperty matpelMapelId;     // Matpel_mapel_id (FK)
    private final IntegerProperty kelasKelasId;      // Kelas_kelas_id (Part of FK to Kelas)

    // Constructor
    public Jadwal(int jadwalId, String hari, String jamMulai, String jamSelesai,
                  String kelasUsersUserId, int matpelMapelId, int kelasKelasId) {
        this.jadwalId = new SimpleIntegerProperty(jadwalId);
        this.hari = new SimpleStringProperty(hari);
        this.jamMulai = new SimpleStringProperty(jamMulai);
        this.jamSelesai = new SimpleStringProperty(jamSelesai);
        this.kelasUsersUserId = new SimpleStringProperty(kelasUsersUserId);
        this.matpelMapelId = new SimpleIntegerProperty(matpelMapelId);
        this.kelasKelasId = new SimpleIntegerProperty(kelasKelasId);
    }

    public Jadwal(int jadwalId, String hari, String jamMulai, String jamSelesai,
                   int matpelMapelId, int kelasKelasId) {
        this.jadwalId = new SimpleIntegerProperty(jadwalId);
        this.hari = new SimpleStringProperty(hari);
        this.jamMulai = new SimpleStringProperty(jamMulai);
        this.jamSelesai = new SimpleStringProperty(jamSelesai);
        this.kelasUsersUserId = new SimpleStringProperty("");
        this.matpelMapelId = new SimpleIntegerProperty(matpelMapelId);
        this.kelasKelasId = new SimpleIntegerProperty(kelasKelasId);
    }

    public Jadwal( String hari, String jamMulai, String jamSelesai,
                  int matpelMapelId, int kelasKelasId) {
        this.jadwalId = new SimpleIntegerProperty();
        this.hari = new SimpleStringProperty(hari);
        this.jamMulai = new SimpleStringProperty(jamMulai);
        this.jamSelesai = new SimpleStringProperty(jamSelesai);
        this.kelasUsersUserId = new SimpleStringProperty("");
        this.matpelMapelId = new SimpleIntegerProperty(matpelMapelId);
        this.kelasKelasId = new SimpleIntegerProperty(kelasKelasId);
    }

    public Jadwal( String hari, String jamMulai, String jamSelesai) {
        this.jadwalId = new SimpleIntegerProperty();
        this.hari = new SimpleStringProperty(hari);
        this.jamMulai = new SimpleStringProperty(jamMulai);
        this.jamSelesai = new SimpleStringProperty(jamSelesai);
        this.kelasUsersUserId = new SimpleStringProperty();
        this.matpelMapelId = new SimpleIntegerProperty();
        this.kelasKelasId = new SimpleIntegerProperty();
    }

    public Jadwal( String jamMulai, String jamSelesai) {
        this.jadwalId = new SimpleIntegerProperty();
        this.hari = new SimpleStringProperty();
        this.jamMulai = new SimpleStringProperty(jamMulai);
        this.jamSelesai = new SimpleStringProperty(jamSelesai);
        this.kelasUsersUserId = new SimpleStringProperty();
        this.matpelMapelId = new SimpleIntegerProperty();
        this.kelasKelasId = new SimpleIntegerProperty();
    }

    // Getters
    public int getJadwalId() { return jadwalId.get(); }
    public String getHari() { return hari.get(); }
    public String getJamMulai() { return jamMulai.get(); }
    public String getJamSelesai() { return jamSelesai.get(); }
    public String getKelasUsersUserId() { return kelasUsersUserId.get(); }
    public int getMatpelMapelId() { return matpelMapelId.get(); }
    public int getKelasKelasId() { return kelasKelasId.get(); }

    // Property Methods
    public IntegerProperty jadwalIdProperty() { return jadwalId; }
    public StringProperty hariProperty() { return hari; }
    public StringProperty jamMulaiProperty() { return jamMulai; }
    public StringProperty jamSelesaiProperty() { return jamSelesai; }
    public StringProperty kelasUsersUserIdProperty() { return kelasUsersUserId; }
    public IntegerProperty matpelMapelIdProperty() { return matpelMapelId; }
    public IntegerProperty kelasKelasIdProperty() { return kelasKelasId; }

    // Setters
    public void setHari(String hari) { this.hari.set(hari); }
    public void setJamMulai(String jamMulai) { this.jamMulai.set(jamMulai); }
    public void setJamSelesai(String jamSelesai) { this.jamSelesai.set(jamSelesai); }
    public void setKelasUsersUserId(String kelasUsersUserId) { this.kelasUsersUserId.set(kelasUsersUserId); }
    public void setMatpelMapelId(int matpelMapelId) { this.matpelMapelId.set(matpelMapelId); }
    public void setKelasKelasId(int kelasKelasId) { this.kelasKelasId.set(kelasKelasId); }
}