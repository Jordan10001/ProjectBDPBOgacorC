// ProjectBDPBOgacor/src/main/java/org/example/projectbdpbogacor/model/MateriEntry.java
package org.example.projectbdpbogacor.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MateriEntry {
    private final StringProperty namaMateri;
    private final StringProperty namaMapel;
    private final StringProperty namaKelas;

    public MateriEntry(String namaMateri, String namaMapel, String namaKelas) {
        this.namaMateri = new SimpleStringProperty(namaMateri);
        this.namaMapel = new SimpleStringProperty(namaMapel);
        this.namaKelas = new SimpleStringProperty(namaKelas);
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
}