// ProjectBDPBOgacor/src/main/java/org/example/projectbdpbogacor/model/AbsensiEntry.java
package org.example.projectbdpbogacor.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AbsensiEntry {
    private final StringProperty tanggal;
    private final StringProperty status;
    private final StringProperty namaMapel;
    private final StringProperty namaKelas;
    private final StringProperty jamMulai;
    private final StringProperty jamSelesai;

    public AbsensiEntry(String tanggal, String status, String namaMapel, String namaKelas, String jamMulai, String jamSelesai) {
        this.tanggal = new SimpleStringProperty(tanggal);
        this.status = new SimpleStringProperty(status);
        this.namaMapel = new SimpleStringProperty(namaMapel);
        this.namaKelas = new SimpleStringProperty(namaKelas);
        this.jamMulai = new SimpleStringProperty(jamMulai);
        this.jamSelesai = new SimpleStringProperty(jamSelesai);
    }

    public String getTanggal() {
        return tanggal.get();
    }

    public StringProperty tanggalProperty() {
        return tanggal;
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
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

    public String getJamMulai() {
        return jamMulai.get();
    }

    public StringProperty jamMulaiProperty() {
        return jamMulai;
    }

    public String getJamSelesai() {
        return jamSelesai.get();
    }

    public StringProperty jamSelesaiProperty() {
        return jamSelesai;
    }
}