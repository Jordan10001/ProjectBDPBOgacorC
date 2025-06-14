package org.example.projectbdpbogacor.Service;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class Rapor {
    private final IntegerProperty raporId;             // rapor_id (PRIMARY KEY)
    private final StringProperty usersUserId;         // Users_user_id (FK)
    private final IntegerProperty semesterSemesterId; // Semester_semester_id (FK)
    // Menambahkan field tambahan yang umum ada di tabel rapor, jika tidak ada di skema Anda bisa abaikan.


    // Constructor (dengan asumsi kolom tambahan)
    public Rapor(int raporId, String usersUserId, int semesterSemesterId ) {
        this.raporId = new SimpleIntegerProperty(raporId);
        this.usersUserId = new SimpleStringProperty(usersUserId);
        this.semesterSemesterId = new SimpleIntegerProperty(semesterSemesterId);
    }

    // Constructor (minimal sesuai skema yang Anda berikan)
//    public Rapor(int raporId, String usersUserId, int semesterSemesterId) {
//        this(raporId, usersUserId, semesterSemesterId, "", ""); // Default values for missing fields
//    }

    // Getters
    public int getRaporId() { return raporId.get(); }
    public String getUsersUserId() { return usersUserId.get(); }
    public int getSemesterSemesterId() { return semesterSemesterId.get(); }

    // Property Methods
    public IntegerProperty raporIdProperty() { return raporId; }
    public StringProperty usersUserIdProperty() { return usersUserId; }
    public IntegerProperty semesterSemesterIdProperty() { return semesterSemesterId; }

    // Setters
    public void setUsersUserId(String usersUserId) { this.usersUserId.set(usersUserId); }
    public void setSemesterSemesterId(int semesterSemesterId) { this.semesterSemesterId.set(semesterSemesterId); }
}