package org.example.projectbdpbogacor.Service;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime; // Untuk kolom TIMESTAMP

public class Semester {
    private final IntegerProperty semesterId;      // semester_id (SERIAL PRIMARY KEY)
    private final StringProperty tahunAjaran;      // tahun_ajaran (VARCHAR(20))
    private final StringProperty namaSemester;     // semester (VARCHAR(20)) - renamed to namaSemester for clarity
    private final ObjectProperty<LocalDateTime> tahun; // tahun (TIMESTAMP)

    // Constructor
    public Semester(int semesterId, String tahunAjaran, String namaSemester, LocalDateTime tahun) {
        this.semesterId = new SimpleIntegerProperty(semesterId);
        this.tahunAjaran = new SimpleStringProperty(tahunAjaran);
        this.namaSemester = new SimpleStringProperty(namaSemester);
        this.tahun = new SimpleObjectProperty<>(tahun);
    }

    // Getters
    public int getSemesterId() { return semesterId.get(); }
    public String getTahunAjaran() { return tahunAjaran.get(); }
    public String getNamaSemester() { return namaSemester.get(); }
    public LocalDateTime getTahun() { return tahun.get(); }

    // Property Methods
    public IntegerProperty semesterIdProperty() { return semesterId; }
    public StringProperty tahunAjaranProperty() { return tahunAjaran; }
    public StringProperty namaSemesterProperty() { return namaSemester; }
    public ObjectProperty<LocalDateTime> tahunProperty() { return tahun; }

    // Setters
    public void setTahunAjaran(String tahunAjaran) { this.tahunAjaran.set(tahunAjaran); }
    public void setNamaSemester(String namaSemester) { this.namaSemester.set(namaSemester); }
    public void setTahun(LocalDateTime tahun) { this.tahun.set(tahun); }
}