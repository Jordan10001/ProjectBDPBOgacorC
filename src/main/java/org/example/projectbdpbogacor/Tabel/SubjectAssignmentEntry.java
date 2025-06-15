package org.example.projectbdpbogacor.Tabel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SubjectAssignmentEntry {
    private final StringProperty subjectName;
    private final StringProperty className;
    private final StringProperty teacherName;
    private final StringProperty teacherId; // Store actual IDs for deletion
    private final IntegerProperty subjectId;
    private final StringProperty kelasWaliId;
    private final IntegerProperty kelasId;

    public SubjectAssignmentEntry(String subjectName, String className,
                                  String teacherName,
                                  String teacherId, int subjectId,
                                  String kelasWaliId, int kelasId) {
        this.subjectName = new SimpleStringProperty(subjectName);
        this.className = new SimpleStringProperty(className);
        this.teacherName = new SimpleStringProperty(teacherName);
        this.teacherId = new SimpleStringProperty(teacherId);
        this.subjectId = new SimpleIntegerProperty(subjectId);
        this.kelasWaliId = new SimpleStringProperty(kelasWaliId);
        this.kelasId = new SimpleIntegerProperty(kelasId);
    }

    public String getSubjectName() { return subjectName.get(); }
    public StringProperty subjectNameProperty() { return subjectName; }
    public String getClassName() { return className.get(); }
    public StringProperty classNameProperty() { return className; }
    public String getTeacherName() { return teacherName.get(); }
    public StringProperty teacherNameProperty() { return teacherName; }
    public String getTeacherId() {return teacherId.get(); }
    public StringProperty teacherIdProperty() { return teacherId; }
    public int getSubjectId() {return subjectId.get();}
    public IntegerProperty subjectIdProperty() { return subjectId; }
    public String getKelasWaliId() { return kelasWaliId.get(); }
    public StringProperty kelasWaliIdProperty() { return kelasWaliId; }
    public int getKelasId() { return kelasId.get(); }
    public IntegerProperty kelasIdProperty() { return kelasId; }
}
