// ProjectBDPBOgacor/src/main/java/org/example/projectbdpbogacor/model/PengumumanEntry.java
package org.example.projectbdpbogacor.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class PengumumanEntry {
    private final IntegerProperty pengumumanId; // Added for the ID
    private final StringProperty waktu;
    private final StringProperty pengumuman;

    public PengumumanEntry(int pengumumanId, String waktu, String pengumuman) { // Updated constructor
        this.pengumumanId = new SimpleIntegerProperty(pengumumanId);
        this.waktu = new SimpleStringProperty(waktu);
        this.pengumuman = new SimpleStringProperty(pengumuman);
    }

    public int getPengumumanId() { // Getter for ID
        return pengumumanId.get();
    }

    public IntegerProperty pengumumanIdProperty() { // Property for ID
        return pengumumanId;
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