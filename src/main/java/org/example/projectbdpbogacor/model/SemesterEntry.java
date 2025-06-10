package org.example.projectbdpbogacor.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SemesterEntry {
    private final IntegerProperty semesterId;
    private final StringProperty semesterName;

    public SemesterEntry(int semesterId, String semesterName) {
        this.semesterId = new SimpleIntegerProperty(semesterId);
        this.semesterName = new SimpleStringProperty(semesterName);
    }

    public IntegerProperty semesterIdProperty() {
        return semesterId;
    }

    public StringProperty semesterNameProperty() {
        return semesterName;
    }

    public int getSemesterId() {
        return semesterId.get();
    }

    public String getSemesterName() {
        return semesterName.get();
    }

    public void setSemesterId(int semesterId) {
        this.semesterId.set(semesterId);
    }

    public void setSemesterName(String semesterName) {
        this.semesterName.set(semesterName);
    }

    @Override
    public String toString() {
        return semesterName.get(); // Penting untuk menampilkan nama semester di ChoiceBox/ComboBox
    }
}