// ProjectBDPBOgacor/src/main/java/org/example/projectbdpbogacor/model/NilaiEntry.java
package org.example.projectbdpbogacor.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;

public class NilaiEntry {
    private final StringProperty jenisNilai;
    private final StringProperty namaMapel;
    private final IntegerProperty nilai;

    public NilaiEntry(String jenisNilai, String namaMapel, int nilai) {
        this.jenisNilai = new SimpleStringProperty(jenisNilai);
        this.namaMapel = new SimpleStringProperty(namaMapel);
        this.nilai = new SimpleIntegerProperty(nilai);
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