package org.example.projectbdpbogacor.Tabel;

import java.util.Map;

public class RaporEntry {
    // Informasi Siswa
    private String studentId;
    private String studentName;
    private String studentNIS;
    private String studentClass;
    private String studentGender;
    private String studentAddress;
    private String studentEmail;
    private String studentPhone;
    private String homeroomTeacherName; // Nama wali kelas

    // Informasi Semester dan Tahun Ajaran
    private String semester;
    private String academicYear;

    // Nilai Mata Pelajaran (Map: Nama Mata Pelajaran -> Rata-rata Nilai)
    private Map<String, Double> subjectAverageGrades;

    // Kehadiran (Jumlah Hari Hadir, Sakit, Izin, Alfa)
    private int totalPresent;
    private int totalSick;
    private int totalPermission;
    private int totalAbsent;

    // Tambahan: Catatan Wali Kelas (jika ada)
    private String homeroomTeacherNotes;

    // Mungkin deskripsi sikap atau ekstrakurikuler jika ada
    // private Map<String, String> attitudeGrades; // Contoh: "Sikap Spiritual" -> "Baik"
    // private List<String> extracurriculars; // Contoh: "Pramuka" -> "Baik"


    public RaporEntry(String studentId, String studentName, String studentNIS,
                      String studentClass, String studentGender,
                      String studentAddress, String studentEmail,
                      String studentPhone, String homeroomTeacherName,
                      String semester, String academicYear, Map<String, Double> subjectAverageGrades,
                      int totalPresent, int totalSick, int totalPermission, int totalAbsent) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentNIS = studentNIS;
        this.studentClass = studentClass;
        this.studentGender = studentGender;
        this.studentAddress = studentAddress;
        this.studentEmail = studentEmail;
        this.studentPhone = studentPhone;
        this.homeroomTeacherName = homeroomTeacherName;
        this.semester = semester;
        this.academicYear = academicYear;
        this.subjectAverageGrades = subjectAverageGrades;
        this.totalPresent = totalPresent;
        this.totalSick = totalSick;
        this.totalPermission = totalPermission;
        this.totalAbsent = totalAbsent;
    }

    // --- Getters ---
    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getStudentNIS() { return studentNIS; }
    public String getStudentClass() { return studentClass; }
    public String getStudentGender() { return studentGender; }
    public String getStudentAddress() { return studentAddress; }
    public String getStudentEmail() { return studentEmail; }
    public String getStudentPhone() { return studentPhone; }
    public String getHomeroomTeacherName() { return homeroomTeacherName; }
    public String getSemester() { return semester; }
    public String getAcademicYear() { return academicYear; }
    public Map<String, Double> getSubjectAverageGrades() { return subjectAverageGrades; }
    public int getTotalPresent() { return totalPresent; }
    public int getTotalSick() { return totalSick; }
    public int getTotalPermission() { return totalPermission; }
    public int getTotalAbsent() { return totalAbsent; }
    public String getHomeroomTeacherNotes() { return homeroomTeacherNotes; }

    // --- Setters (jika diperlukan untuk data yang diisi setelah konstruksi) ---
    public void setHomeroomTeacherNotes(String homeroomTeacherNotes) { this.homeroomTeacherNotes = homeroomTeacherNotes; }
    // Anda bisa menambahkan setter untuk field lain jika perlu diubah setelah RaporEntry dibuat.
}