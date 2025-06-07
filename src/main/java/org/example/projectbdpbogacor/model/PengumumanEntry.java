package org.example.projectbdpbogacor.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PengumumanEntry {
    private final StringProperty waktu;
    private final StringProperty pengumuman;

    public PengumumanEntry(String waktu, String pengumuman) {
        this.waktu = new SimpleStringProperty(waktu);
        this.pengumuman = new SimpleStringProperty(pengumuman);
    }

    public String getWaktu() {
        return waktu.get();
    }

    public StringProperty waktuProperty() {
        return waktu;
    }

    public String getPengumuman() {
        return pengumuman.get();
    }

    public StringProperty pengumumanProperty() {
        return pengumuman;
    }
}