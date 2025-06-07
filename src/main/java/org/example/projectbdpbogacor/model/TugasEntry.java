// ProjectBDPBOgacor/src/main/java/org/example/projectbdpbogacor/model/TugasEntry.java
package org.example.projectbdpbogacor.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TugasEntry {
    private final StringProperty keterangan;
    private final StringProperty deadline;
    private final StringProperty tanggalDirelease;
    private final StringProperty namaMapel;
    private final StringProperty namaKelas;

    public TugasEntry(String keterangan, String deadline, String tanggalDirelease, String namaMapel, String namaKelas) {
        this.keterangan = new SimpleStringProperty(keterangan);
        this.deadline = new SimpleStringProperty(deadline);
        this.tanggalDirelease = new SimpleStringProperty(tanggalDirelease);
        this.namaMapel = new SimpleStringProperty(namaMapel);
        this.namaKelas = new SimpleStringProperty(namaKelas);
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
}