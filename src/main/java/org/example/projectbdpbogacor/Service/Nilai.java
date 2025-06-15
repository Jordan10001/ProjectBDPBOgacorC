package org.example.projectbdpbogacor.Service;

import javafx.beans.property.SimpleIntegerProperty; // Menggunakan IntegerProperty untuk nilai
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class Nilai {
    private final IntegerProperty nilaiId;          // nilai_id (PRIMARY KEY)
    private final IntegerProperty nilaiAngka;       // nilai (BIGINT) - di JavaFX pakai IntegerProperty atau LongProperty
    private final StringProperty jenisNilai;        // jenis_nilai
    private final IntegerProperty matpelMapelId;    // Matpel_mapel_id (FK)
    private final IntegerProperty raporRaporId;     // Rapor_rapor_id (FK)
    // Asumsi Nilai ini juga terkait dengan Users_user_id (siswa yang mendapat nilai)


    // Constructor
    public Nilai(int nilaiId, int nilaiAngka, String jenisNilai,
                 int matpelMapelId, int raporRaporId) {
        this.nilaiId = new SimpleIntegerProperty(nilaiId);
        this.nilaiAngka = new SimpleIntegerProperty(nilaiAngka);
        this.jenisNilai = new SimpleStringProperty(jenisNilai);
        this.matpelMapelId = new SimpleIntegerProperty(matpelMapelId);
        this.raporRaporId = new SimpleIntegerProperty(raporRaporId);
    }

    public Nilai( int nilaiAngka, String jenisNilai) {
        this.nilaiId = new SimpleIntegerProperty();
        this.nilaiAngka = new SimpleIntegerProperty(nilaiAngka);
        this.jenisNilai = new SimpleStringProperty(jenisNilai);
        this.matpelMapelId = new SimpleIntegerProperty();
        this.raporRaporId = new SimpleIntegerProperty();
    }

    // Getters
    public int getNilaiId() { return nilaiId.get(); }
    public int getNilaiAngka() { return nilaiAngka.get(); }
    public String getJenisNilai() { return jenisNilai.get(); }
    public int getMatpelMapelId() { return matpelMapelId.get(); }
    public int getRaporRaporId() { return raporRaporId.get(); }

    // Property Methods
    public IntegerProperty nilaiIdProperty() { return nilaiId; }
    public IntegerProperty nilaiAngkaProperty() { return nilaiAngka; }
    public StringProperty jenisNilaiProperty() { return jenisNilai; }
    public IntegerProperty matpelMapelIdProperty() { return matpelMapelId; }
    public IntegerProperty raporRaporIdProperty() { return raporRaporId; }

    // Setters
    public void setNilaiAngka(int nilaiAngka) { this.nilaiAngka.set(nilaiAngka); }
    public void setJenisNilai(String jenisNilai) { this.jenisNilai.set(jenisNilai); }
    public void setMatpelMapelId(int matpelMapelId) { this.matpelMapelId.set(matpelMapelId); }
    public void setRaporRaporId(int raporRaporId) { this.raporRaporId.set(raporRaporId); }
}