// ProjectBDPBOgacor/src/main/java/org/example/projectbdpbogacor/model/UjianEntry.java
package org.example.projectbdpbogacor.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class UjianEntry {
    private final IntegerProperty ujianId;
    private final StringProperty jenisUjian;
    private final StringProperty tanggal;
    private final StringProperty namaMapel;
    private final StringProperty namaKelas;
    private final String kelasWaliId;
    private final int kelasId;

    // Constructor for Guru (includes IDs)
    public UjianEntry(int ujianId, String jenisUjian,
                      String tanggal, String namaMapel,
                      String namaKelas, String kelasWaliId, int kelasId) {
        this.ujianId = new SimpleIntegerProperty(ujianId);
        this.jenisUjian = new SimpleStringProperty(jenisUjian);
        this.tanggal = new SimpleStringProperty(tanggal);
        this.namaMapel = new SimpleStringProperty(namaMapel);
        this.namaKelas = new SimpleStringProperty(namaKelas);
        this.kelasWaliId = kelasWaliId;
        this.kelasId = kelasId;
    }

    // Constructor for Siswa (does not need IDs for display)
    public UjianEntry(String jenisUjian, String tanggal, String namaMapel, String namaKelas) {
        this.ujianId = new SimpleIntegerProperty(0); // Default or unused
        this.jenisUjian = new SimpleStringProperty(jenisUjian);
        this.tanggal = new SimpleStringProperty(tanggal);
        this.namaMapel = new SimpleStringProperty(namaMapel);
        this.namaKelas = new SimpleStringProperty(namaKelas);
        this.kelasWaliId = null; // Not needed
        this.kelasId = 0; // Not needed
    }

    public int getUjianId() {
        return ujianId.get();
    }

    public IntegerProperty ujianIdProperty() {
        return ujianId;
    }

    public String getJenisUjian() {
        return jenisUjian.get();
    }

    public StringProperty jenisUjianProperty() {
        return jenisUjian;
    }

    public String getTanggal() {
        return tanggal.get();
    }

    public StringProperty tanggalProperty() {
        return tanggal;
    }

    public String getNamaMapel() {
        return namaMapel.get();
    }

    public StringProperty namaMapelProperty() {
        return namaMapel;
    }

    public String getNamaKelas() {
        return namaKelas.get();
    }

    public StringProperty namaKelasProperty() {
        return namaKelas;
    }

    public String getKelasWaliId() {
        return kelasWaliId;
    }

    public int getKelasId() {
        return kelasId;
    }
}