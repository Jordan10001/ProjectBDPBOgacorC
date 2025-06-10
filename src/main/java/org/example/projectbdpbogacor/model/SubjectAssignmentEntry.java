package org.example.projectbdpbogacor.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SubjectAssignmentEntry {
    private final StringProperty subjectName;
    private final StringProperty className;
    private final StringProperty teacherName;
    private final String teacherId; // Store actual IDs for deletion
    private final int subjectId;
    private final String kelasWaliId;
    private final int kelasId;

    public SubjectAssignmentEntry(String subjectName, String className,
                                  String teacherName,
                                  String teacherId, int subjectId,
                                  String kelasWaliId, int kelasId) {
        this.subjectName = new SimpleStringProperty(subjectName);
        this.className = new SimpleStringProperty(className);
        this.teacherName = new SimpleStringProperty(teacherName);
        this.teacherId = teacherId;
        this.subjectId = subjectId;
        this.kelasWaliId = kelasWaliId;
        this.kelasId = kelasId;
    }

    public String getSubjectName() { return subjectName.get(); }
    public StringProperty subjectNameProperty() { return subjectName; }
    public String getClassName() { return className.get(); }
    public StringProperty classNameProperty() { return className; }
    public String getTeacherName() { return teacherName.get(); }
    public StringProperty teacherNameProperty() { return teacherName; }

    public String getTeacherId() { return teacherId; }
    public int getSubjectId() { return subjectId; }
    public String getKelasWaliId() { return kelasWaliId; }
    public int getKelasId() { return kelasId; }
}
