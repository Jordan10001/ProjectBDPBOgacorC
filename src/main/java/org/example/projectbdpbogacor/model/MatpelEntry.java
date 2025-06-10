
//UNDER ENTITY MATPEL

package org.example.projectbdpbogacor.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MatpelEntry {
    private final IntegerProperty matpelId;
    private final StringProperty matpelName;
    private final StringProperty matpelCategory; // Tambahan, jika ada kategori

    public MatpelEntry(int matpelId, String matpelName, String matpelCategory) {
        this.matpelId = new SimpleIntegerProperty(matpelId);
        this.matpelName = new SimpleStringProperty(matpelName);
        this.matpelCategory = new SimpleStringProperty(matpelCategory);
    }

    public IntegerProperty matpelIdProperty() {
        return matpelId;
    }

    public StringProperty matpelNameProperty() {
        return matpelName;
    }

    public StringProperty matpelCategoryProperty() {
        return matpelCategory;
    }

    public int getMatpelId() {
        return matpelId.get();
    }

    public String getMatpelName() {
        return matpelName.get();
    }

    public String getMatpelCategory() {
        return matpelCategory.get();
    }

    public void setMatpelId(int matpelId) {
        this.matpelId.set(matpelId);
    }

    public void setMatpelName(String matpelName) {
        this.matpelName.set(matpelName);
    }

    public void setMatpelCategory(String matpelCategory) {
        this.matpelCategory.set(matpelCategory);
    }

    @Override
    public String toString() {
        return matpelName.get(); // Penting untuk menampilkan nama mata pelajaran di ChoiceBox/ComboBox
    }
}