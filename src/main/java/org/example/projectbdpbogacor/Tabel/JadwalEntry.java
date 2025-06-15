// ProjectBDPBOgacor/src/main/java/org/example/projectbdpbogacor/model/JadwalEntry.java
package org.example.projectbdpbogacor.Tabel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class JadwalEntry {
    private final StringProperty hari;
    private final StringProperty jamMulai;
    private final StringProperty jamSelesai;
    private final StringProperty namaMapel;
    private final StringProperty namaKelas;
    private final StringProperty namaPengajar; // Added for Guru's name

    public JadwalEntry(String hari, String jamMulai, String jamSelesai,
                       String namaMapel, String namaKelas,
                       String namaPengajar) {
        this.hari = new SimpleStringProperty(hari);
        this.jamMulai = new SimpleStringProperty(jamMulai);
        this.jamSelesai = new SimpleStringProperty(jamSelesai);
        this.namaMapel = new SimpleStringProperty(namaMapel);
        this.namaKelas = new SimpleStringProperty(namaKelas);
        this.namaPengajar = new SimpleStringProperty(namaPengajar);
    }

    public String getHari() {
        return hari.get();
    }

    public StringProperty hariProperty() {
        return hari;
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

    public String getNamaPengajar() {
        return namaPengajar.get();
    }

    public StringProperty namaPengajarProperty() {
        return namaPengajar;
    }
}