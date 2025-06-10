package org.example.projectbdpbogacor.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StudentEntry {
    private final StringProperty studentName;
    private final StringProperty nisNip;
    private final StringProperty userId;

    public StudentEntry(String studentName, String nisNip, String userId) {
        this.studentName = new SimpleStringProperty(studentName);
        this.nisNip = new SimpleStringProperty(nisNip);
        this.userId = new SimpleStringProperty(userId);
    }

    public String getStudentName() {
        return studentName.get();
    }

    public StringProperty studentNameProperty() {
        return studentName;
    }

    public String getNisNip() {
        return nisNip.get();
    }

    public StringProperty nisNipProperty() {
        return nisNip;
    }

    public String getUserId() {
        return userId.get();
    }

    public StringProperty userIdProperty() {
        return userId;
    }
}
