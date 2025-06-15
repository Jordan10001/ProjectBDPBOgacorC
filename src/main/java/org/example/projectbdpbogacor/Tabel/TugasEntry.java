// ProjectBDPBOgacor/src/main/java/org/example/projectbdpbogacor/model/TugasEntry.java
package org.example.projectbdpbogacor.Tabel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class TugasEntry {
    private final IntegerProperty tugasId;
    private final StringProperty keterangan;
    private final StringProperty deadline;
    private final StringProperty tanggalDirelease;
    private final StringProperty namaMapel;
    private final StringProperty namaKelas;
    private final String kelasWaliId;
    private final int kelasId;

    // Constructor for Guru (includes IDs)
    public TugasEntry(int tugasId, String keterangan,
                      String deadline, String tanggalDirelease,
                      String namaMapel, String namaKelas,
                      String kelasWaliId, int kelasId) {
        this.tugasId = new SimpleIntegerProperty(tugasId);
        this.keterangan = new SimpleStringProperty(keterangan);
        this.deadline = new SimpleStringProperty(deadline);
        this.tanggalDirelease = new SimpleStringProperty(tanggalDirelease);
        this.namaMapel = new SimpleStringProperty(namaMapel);
        this.namaKelas = new SimpleStringProperty(namaKelas);
        this.kelasWaliId = kelasWaliId;
        this.kelasId = kelasId;
    }

    // Constructor for Siswa (does not need IDs for display)
    public TugasEntry(String keterangan, String deadline, String tanggalDirelease, String namaMapel, String namaKelas) {
        this.tugasId = new SimpleIntegerProperty(0); // Default or unused
        this.keterangan = new SimpleStringProperty(keterangan);
        this.deadline = new SimpleStringProperty(deadline);
        this.tanggalDirelease = new SimpleStringProperty(tanggalDirelease);
        this.namaMapel = new SimpleStringProperty(namaMapel);
        this.namaKelas = new SimpleStringProperty(namaKelas);
        this.kelasWaliId = null; // Not needed
        this.kelasId = 0; // Not needed
    }

    public int getTugasId() {
        return tugasId.get();
    }

    public IntegerProperty tugasIdProperty() {
        return tugasId;
    }

    public String getKeterangan() {
        return keterangan.get();
    }

    public StringProperty keteranganProperty() {
        return keterangan;
    }

    public String getDeadline() {
        return deadline.get();
    }

    public StringProperty deadlineProperty() {
        return deadline;
    }

    public String getTanggalDirelease() {
        return tanggalDirelease.get();
    }

    public StringProperty tanggalDireleaseProperty() {
        return tanggalDirelease;
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