// ProjectBDPBOgacor/src/main/java/org/example/projectbdpbogacor/model/UjianEntry.java
package org.example.projectbdpbogacor.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UjianEntry {
    private final StringProperty jenisUjian;
    private final StringProperty tanggal;
    private final StringProperty namaMapel;
    private final StringProperty namaKelas;

    public UjianEntry(String jenisUjian, String tanggal, String namaMapel, String namaKelas) {
        this.jenisUjian = new SimpleStringProperty(jenisUjian);
        this.tanggal = new SimpleStringProperty(tanggal);
        this.namaMapel = new SimpleStringProperty(namaMapel);
        this.namaKelas = new SimpleStringProperty(namaKelas);
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
}