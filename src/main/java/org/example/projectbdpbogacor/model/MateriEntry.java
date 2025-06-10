// ProjectBDPBOgacor/src/main/java/org/example/projectbdpbogacor/model/MateriEntry.java
package org.example.projectbdpbogacor.model;

//UNTUK ENTITY MATERI

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class MateriEntry {
    private final IntegerProperty materiId;
    private final StringProperty namaMateri;
    private final StringProperty namaMapel;
    private final StringProperty namaKelas;
    private final String kelasWaliId;
    private final int kelasId;

    // Constructor for Guru (includes IDs)
    public MateriEntry(int materiId, String namaMateri, String namaMapel,
                       String namaKelas, String kelasWaliId, int kelasId) {
        this.materiId = new SimpleIntegerProperty(materiId);
        this.namaMateri = new SimpleStringProperty(namaMateri);
        this.namaMapel = new SimpleStringProperty(namaMapel);
        this.namaKelas = new SimpleStringProperty(namaKelas);
        this.kelasWaliId = kelasWaliId;
        this.kelasId = kelasId;
    }

    // Constructor for Siswa (does not need IDs as they are not displayed/managed)
    public MateriEntry(String namaMateri, String namaMapel, String namaKelas) {
        this.materiId = new SimpleIntegerProperty(0); // Default or unused for display-only
        this.namaMateri = new SimpleStringProperty(namaMateri);
        this.namaMapel = new SimpleStringProperty(namaMapel);
        this.namaKelas = new SimpleStringProperty(namaKelas);
        this.kelasWaliId = null; // Not needed for display-only
        this.kelasId = 0; // Not needed for display-only
    }

    public int getMateriId() {
        return materiId.get();
    }

    public IntegerProperty materiIdProperty() {
        return materiId;
    }

    public String getNamaMateri() {
        return namaMateri.get();
    }

    public StringProperty namaMateriProperty() {
        return namaMateri;
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