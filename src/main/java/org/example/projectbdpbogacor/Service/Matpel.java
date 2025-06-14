package org.example.projectbdpbogacor.Service;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class Matpel {
    private final IntegerProperty mapelId;     // mapel_id (SERIAL PRIMARY KEY)
    private final StringProperty namaMapel;    // nama_mapel
    private final StringProperty category;     // category

    // Constructor
    public Matpel(int mapelId, String namaMapel, String category) {
        this.mapelId = new SimpleIntegerProperty(mapelId);
        this.namaMapel = new SimpleStringProperty(namaMapel);
        this.category = new SimpleStringProperty(category);
    }

    // Getters
    public int getMapelId() { return mapelId.get(); }
    public String getNamaMapel() { return namaMapel.get(); }
    public String getCategory() { return category.get(); }

    // Property Methods
    public IntegerProperty mapelIdProperty() { return mapelId; }
    public StringProperty namaMapelProperty() { return namaMapel; }
    public StringProperty categoryProperty() { return category; }

    // Setters
    public void setNamaMapel(String namaMapel) { this.namaMapel.set(namaMapel); }
    public void setCategory(String category) { this.category.set(category); }
}