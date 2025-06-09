// ProjectBDPBOgacor/src/main/java/org/example/projectbdpbogacor/model/NilaiEntry.java
package org.example.projectbdpbogacor.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;

public class NilaiEntry {
    private final IntegerProperty nilaiId; // Added for the ID
    private final StringProperty jenisNilai;
    private final StringProperty namaMapel;
    private final IntegerProperty nilai;

    // Updated constructor to include nilaiId
    public NilaiEntry(int nilaiId, String jenisNilai, String namaMapel, int nilai) {
        this.nilaiId = new SimpleIntegerProperty(nilaiId);
        this.jenisNilai = new SimpleStringProperty(jenisNilai);
        this.namaMapel = new SimpleStringProperty(namaMapel);
        this.nilai = new SimpleIntegerProperty(nilai);
    }

    // New constructor for display-only (e.g., Siswa view) if needed, without ID
    public NilaiEntry(String jenisNilai, String namaMapel, int nilai) {
        this.nilaiId = new SimpleIntegerProperty(0); // Default or unused
        this.jenisNilai = new SimpleStringProperty(jenisNilai);
        this.namaMapel = new SimpleStringProperty(namaMapel);
        this.nilai = new SimpleIntegerProperty(nilai);
    }

    public int getNilaiId() { // Getter for ID
        return nilaiId.get();
    }

    public IntegerProperty nilaiIdProperty() { // Property for ID
        return nilaiId;
    }

    public String getJenisNilai() {
        return jenisNilai.get();
    }

    public StringProperty jenisNilaiProperty() {
        return jenisNilai;
    }

    public String getNamaMapel() {
        return namaMapel.get();
    }

    public StringProperty namaMapelProperty() {
        return namaMapel;
    }

    public int getNilai() {
        return nilai.get();
    }

    public IntegerProperty nilaiProperty() {
        return nilai;
    }
}