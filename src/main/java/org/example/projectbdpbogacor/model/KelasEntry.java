// File: src/main/java/org/example/projectbdpbogacor/model/KelasEntry.java

// NTUK ENTITY KELAS

package org.example.projectbdpbogacor.model;

import javafx.beans.property.*;

public class KelasEntry {
    private final IntegerProperty classId;
    private final StringProperty className;
    private final IntegerProperty waliKelasId;
    private final StringProperty waliKelasName; // Untuk tampilan, join dengan Users
    private final IntegerProperty semesterId;
    private final StringProperty semesterName; // Untuk tampilan, join dengan Semester
    private final IntegerProperty tahunAjaran;

    public KelasEntry(int classId, String className, int waliKelasId, String waliKelasName, int semesterId, String semesterName, int tahunAjaran) {
        this.classId = new SimpleIntegerProperty(classId);
        this.className = new SimpleStringProperty(className);
        this.waliKelasId = new SimpleIntegerProperty(waliKelasId);
        this.waliKelasName = new SimpleStringProperty(waliKelasName);
        this.semesterId = new SimpleIntegerProperty(semesterId);
        this.semesterName = new SimpleStringProperty(semesterName);
        this.tahunAjaran = new SimpleIntegerProperty(tahunAjaran);
    }

    // Getters for properties
    public IntegerProperty classIdProperty() { return classId; }
    public StringProperty classNameProperty() { return className; }
    public IntegerProperty waliKelasIdProperty() { return waliKelasId; }
    public StringProperty waliKelasNameProperty() { return waliKelasName; }
    public IntegerProperty semesterIdProperty() { return semesterId; }
    public StringProperty semesterNameProperty() { return semesterName; }
    public IntegerProperty tahunAjaranProperty() { return tahunAjaran; }

    // Getters for actual values
    public int getClassId() { return classId.get(); }
    public String getClassName() { return className.get(); }
    public int getWaliKelasId() { return waliKelasId.get(); }
    public String getWaliKelasName() { return waliKelasName.get(); }
    public int getSemesterId() { return semesterId.get(); }
    public String getSemesterName() { return semesterName.get(); }
    public int getTahunAjaran() { return tahunAjaran.get(); }

    // Setters (jika diperlukan untuk edit di UI)
    public void setClassName(String className) { this.className.set(className); }
    public void setWaliKelasId(int waliKelasId) { this.waliKelasId.set(waliKelasId); }
    public void setWaliKelasName(String waliKelasName) { this.waliKelasName.set(waliKelasName); }
    public void setSemesterId(int semesterId) { this.semesterId.set(semesterId); }
    public void setSemesterName(String semesterName) { this.semesterName.set(semesterName); }
    public void setTahunAjaran(int tahunAjaran) { this.tahunAjaran.set(tahunAjaran); }

    @Override
    public String toString() {
        return className.get(); // Penting untuk menampilkan nama kelas di ChoiceBox/ComboBox
    }
}