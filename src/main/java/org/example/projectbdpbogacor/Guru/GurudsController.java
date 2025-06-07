// ProjectBDPBOgacor/src/main/java/org/example/projectbdpbogacor/Guru/GurudsController.java
package org.example.projectbdpbogacor.Guru;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.projectbdpbogacor.Aset.AlertClass;
import org.example.projectbdpbogacor.DBSource.DBS;
import org.example.projectbdpbogacor.HelloApplication;
import org.example.projectbdpbogacor.model.JadwalEntry;
import org.example.projectbdpbogacor.model.NilaiEntry;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class GurudsController {

    @FXML
    private Label welcomeUserLabel;
    @FXML
    private TabPane guruTabPane;

    // Class Schedule
    @FXML
    private ChoiceBox<String> scheduleClassChoiceBox; // Display: "Nama Kelas (Wali: Name)"
    @FXML
    private ChoiceBox<String> scheduleSubjectChoiceBox; // Display: "Nama Mapel"
    @FXML
    private TableView<JadwalEntry> jadwalKelasTable;
    @FXML
    private TableColumn<JadwalEntry, String> hariColumn;
    @FXML
    private TableColumn<JadwalEntry, String> jamMulaiColumn;
    @FXML
    private TableColumn<JadwalEntry, String> jamSelesaiColumn;
    @FXML
    private TableColumn<JadwalEntry, String> namaMapelColumn;
    @FXML
    private TableColumn<JadwalEntry, String> namaKelasJadwalColumn;

    // Input Grades
    @FXML
    private ChoiceBox<String> gradeClassChoiceBox; // Display: "Nama Kelas (Wali: Name)"
    @FXML
    private ChoiceBox<String> gradeSubjectChoiceBox; // Display: "Nama Mapel"
    @FXML
    private ChoiceBox<String> gradeStudentChoiceBox; // Display: "Student Name (NIS/NIP)"
    @FXML
    private ChoiceBox<String> gradeSemesterChoiceBox; // Display: "Tahun Ajaran - Semester"
    @FXML
    private TextField gradeTypeField;
    @FXML
    private TextField scoreField;
    @FXML
    private TableView<NilaiEntry> existingGradesTable;
    @FXML
    private TableColumn<NilaiEntry, String> existingJenisNilaiColumn;
    @FXML
    private TableColumn<NilaiEntry, String> existingNamaMapelColumn;
    @FXML
    private TableColumn<NilaiEntry, Integer> existingNilaiColumn;

    private String loggedInUserId;

    // Maps to store IDs corresponding to displayed names in ChoiceBoxes
    private Map<String, String> classesMap = new HashMap<>(); // Display -> Kelas_Users_user_id-kelas_id
    private Map<String, Integer> subjectsMap = new HashMap<>(); // Display -> mapel_id
    private Map<String, String> studentsMap = new HashMap<>(); // Display -> user_id
    private Map<String, Integer> semestersMap = new HashMap<>(); // Display -> semester_id


    @FXML
    void initialize() {
        loggedInUserId = HelloApplication.getInstance().getLoggedInUserId();
        loadGuruName();

        // Initialize TableView columns
        initJadwalKelasTable();
        initExistingGradesTable();

        // Load initial data for ChoiceBoxes
        loadClassesTaughtByGuru();
        loadSubjectsTaughtByGuru();
        loadSemesters();

        // Add listeners to tabs to load data when selected
        guruTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null) {
                if (newTab.getText().equals("Class Schedule")) {
                    loadClassesTaughtByGuru();
                    loadSubjectsTaughtByGuru();
                    loadJadwalKelas(); // Load schedule for selected teacher/class/subject
                } else if (newTab.getText().equals("Input Grades")) {
                    loadClassesTaughtByGuru();
                    loadSubjectsTaughtByGuru();
                    loadSemesters();
                    // Students are loaded based on class/subject selection
                }
            }
        });
    }

    private void loadGuruName() {
        String sql = "SELECT nama FROM Users WHERE user_id = ?";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, loggedInUserId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                welcomeUserLabel.setText("Welcome, " + rs.getString("nama") + "!");
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load user name", e.getMessage());
            e.printStackTrace();
        }
    }

    // --- Class Schedule Methods ---
    private void initJadwalKelasTable() {
        hariColumn.setCellValueFactory(new PropertyValueFactory<>("hari"));
        jamMulaiColumn.setCellValueFactory(new PropertyValueFactory<>("jamMulai"));
        jamSelesaiColumn.setCellValueFactory(new PropertyValueFactory<>("jamSelesai"));
        namaMapelColumn.setCellValueFactory(new PropertyValueFactory<>("namaMapel"));
        namaKelasJadwalColumn.setCellValueFactory(new PropertyValueFactory<>("namaKelas"));
    }

    private void loadClassesTaughtByGuru() {
        classesMap.clear();
        scheduleClassChoiceBox.getItems().clear();
        gradeClassChoiceBox.getItems().clear();

        // Get classes associated with this teacher via Detail_Pengajar
        String sql = "SELECT DISTINCT k.kelas_id, k.nama_kelas, k.Users_user_id AS wali_id, u_wali.nama AS wali_name " +
                "FROM Kelas k " +
                "JOIN Detail_Pengajar dp ON k.kelas_id = dp.Kelas_kelas_id AND k.Users_user_id = dp.Kelas_Users_user_id " +
                "JOIN Users u_wali ON k.Users_user_id = u_wali.user_id " + // To get Wali Kelas name
                "WHERE dp.Users_user_id = ?"; // Filter by current Guru's user_id

        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, loggedInUserId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int kelasId = rs.getInt("kelas_id");
                String namaKelas = rs.getString("nama_kelas");
                String waliId = rs.getString("wali_id");
                String waliName = rs.getString("wali_name");
                String display = namaKelas + " (Wali: " + waliName + ")";
                String combinedId = kelasId + "-" + waliId; // Store for lookup
                classesMap.put(display, combinedId);
                scheduleClassChoiceBox.getItems().add(display);
                gradeClassChoiceBox.getItems().add(display);
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load classes for teacher", e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadSubjectsTaughtByGuru() {
        subjectsMap.clear();
        scheduleSubjectChoiceBox.getItems().clear();
        gradeSubjectChoiceBox.getItems().clear();

        // Get subjects associated with this teacher via Detail_Pengajar
        String sql = "SELECT DISTINCT m.mapel_id, m.nama_mapel FROM Matpel m " +
                "JOIN Detail_Pengajar dp ON m.mapel_id = dp.Matpel_mapel_id " +
                "WHERE dp.Users_user_id = ?"; // Filter by current Guru's user_id

        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, loggedInUserId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int mapelId = rs.getInt("mapel_id");
                String namaMapel = rs.getString("nama_mapel");
                subjectsMap.put(namaMapel, mapelId);
                scheduleSubjectChoiceBox.getItems().add(namaMapel);
                gradeSubjectChoiceBox.getItems().add(namaMapel);
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load subjects for teacher", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void loadJadwalKelas() {
        ObservableList<JadwalEntry> jadwalList = FXCollections.observableArrayList();
        String selectedClassDisplay = scheduleClassChoiceBox.getValue();
        String selectedSubjectDisplay = scheduleSubjectChoiceBox.getValue();

        // Base query for schedules taught by this guru
        String sql = "SELECT j.hari, j.jam_mulai, j.jam_selsai, m.nama_mapel, k.nama_kelas " +
                "FROM Jadwal j " +
                "JOIN Matpel m ON j.Matpel_mapel_id = m.mapel_id " +
                "JOIN Kelas k ON j.Kelas_Users_user_id = k.Users_user_id AND j.Kelas_kelas_id = k.kelas_id " +
                "JOIN Detail_Pengajar dp ON dp.Matpel_mapel_id = m.mapel_id AND dp.Kelas_Users_user_id = k.Users_user_id AND dp.Kelas_kelas_id = k.kelas_id " +
                "WHERE dp.Users_user_id = ?"; // Filter by current Guru's user_id

        if (selectedClassDisplay != null && !selectedClassDisplay.isEmpty()) {
            String combinedClassId = classesMap.get(selectedClassDisplay);
            if (combinedClassId != null) {
                String[] ids = combinedClassId.split("-");
                sql += " AND k.kelas_id = ? AND k.Users_user_id = ?"; // Filter by selected class
            }
        }
        if (selectedSubjectDisplay != null && !selectedSubjectDisplay.isEmpty()) {
            Integer mapelId = subjectsMap.get(selectedSubjectDisplay);
            if (mapelId != null) {
                sql += " AND m.mapel_id = ?"; // Filter by selected subject
            }
        }

        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            int paramIndex = 1;
            stmt.setString(paramIndex++, loggedInUserId);

            if (selectedClassDisplay != null && !selectedClassDisplay.isEmpty()) {
                String combinedClassId = classesMap.get(selectedClassDisplay);
                if (combinedClassId != null) {
                    String[] ids = combinedClassId.split("-");
                    stmt.setInt(paramIndex++, Integer.parseInt(ids[0])); // kelas_id
                    stmt.setString(paramIndex++, ids[1]); // Kelas_Users_user_id (wali_id)
                }
            }
            if (selectedSubjectDisplay != null && !selectedSubjectDisplay.isEmpty()) {
                Integer mapelId = subjectsMap.get(selectedSubjectDisplay);
                if (mapelId != null) {
                    stmt.setInt(paramIndex++, mapelId);
                }
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                jadwalList.add(new JadwalEntry(
                        rs.getString("hari"),
                        rs.getString("jam_mulai"),
                        rs.getString("jam_selsai"),
                        rs.getString("nama_mapel"),
                        rs.getString("nama_kelas"),
                        "" // Guru's own schedule, no need for another pengajar name
                ));
            }
            jadwalKelasTable.setItems(jadwalList);
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load class schedule", e.getMessage());
            e.printStackTrace();
        }
    }

    // --- Input Grades Methods ---
    private void initExistingGradesTable() {
        existingJenisNilaiColumn.setCellValueFactory(new PropertyValueFactory<>("jenisNilai"));
        existingNamaMapelColumn.setCellValueFactory(new PropertyValueFactory<>("namaMapel"));
        existingNilaiColumn.setCellValueFactory(new PropertyValueFactory<>("nilai"));
    }

    private void loadSemesters() {
        semestersMap.clear();
        gradeSemesterChoiceBox.getItems().clear();

        String sql = "SELECT semester_id, tahun_ajaran, semester FROM Semester";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int semesterId = rs.getInt("semester_id");
                String tahunAjaran = rs.getString("tahun_ajaran");
                String semesterName = rs.getString("semester");
                String display = tahunAjaran + " - " + semesterName;
                semestersMap.put(display, semesterId);
                gradeSemesterChoiceBox.getItems().add(display);
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load semesters", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleGradeClassSelection() {
        String selectedClassDisplay = gradeClassChoiceBox.getValue();
        studentsMap.clear();
        gradeStudentChoiceBox.getItems().clear();

        if (selectedClassDisplay == null || selectedClassDisplay.isEmpty()) {
            loadExistingGrades(); // Clear existing grades if no class selected
            return;
        }

        String combinedClassId = classesMap.get(selectedClassDisplay);
        if (combinedClassId == null) {
            loadExistingGrades(); // Clear existing grades
            return;
        }
        String[] ids = combinedClassId.split("-");
        int kelasId = Integer.parseInt(ids[0]);
        String waliUserId = ids[1];

        // Load students enrolled in the selected class
        String sql = "SELECT u.user_id, u.nama, u.NIS_NIP FROM Users u " +
                "JOIN Student_Class_Enrollment sce ON u.user_id = sce.Users_user_id " +
                "WHERE sce.Kelas_kelas_id = ? AND sce.Kelas_Users_user_id = ? AND u.Role_role_id = 'S'";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, kelasId);
            stmt.setString(2, waliUserId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String userId = rs.getString("user_id");
                String nama = rs.getString("nama");
                String nisNip = rs.getString("NIS_NIP");
                String display = nama + " (NIS: " + nisNip + ")";
                studentsMap.put(display, userId);
                gradeStudentChoiceBox.getItems().add(display);
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load students for class", e.getMessage());
            e.printStackTrace();
        }
        loadExistingGrades(); // Refresh existing grades based on new selections
    }

    @FXML
    void handleGradeSubjectSelection() {
        loadExistingGrades(); // Refresh existing grades based on new selections
    }


    @FXML
    void handleSubmitGrade() {
        String selectedStudentDisplay = gradeStudentChoiceBox.getValue();
        String selectedSubjectDisplay = gradeSubjectChoiceBox.getValue();
        String selectedSemesterDisplay = gradeSemesterChoiceBox.getValue();
        String jenisNilai = gradeTypeField.getText();
        String scoreText = scoreField.getText();

        if (selectedStudentDisplay == null || selectedSubjectDisplay == null || selectedSemesterDisplay == null ||
                jenisNilai.isEmpty() || scoreText.isEmpty()) {
            AlertClass.WarningAlert("Input Error", "Missing Information", "Please fill in all grade details.");
            return;
        }

        int score;
        try {
            score = Integer.parseInt(scoreText);
            if (score < 0 || score > 100) {
                AlertClass.WarningAlert("Input Error", "Invalid Score", "Score must be between 0 and 100.");
                return;
            }
        } catch (NumberFormatException e) {
            AlertClass.WarningAlert("Input Error", "Invalid Score", "Please enter a valid number for the score.");
            return;
        }

        String studentUserId = studentsMap.get(selectedStudentDisplay);
        Integer mapelId = subjectsMap.get(selectedSubjectDisplay);
        Integer semesterId = semestersMap.get(selectedSemesterDisplay);

        if (studentUserId == null || mapelId == null || semesterId == null) {
            AlertClass.ErrorAlert("Selection Error", "Invalid Selection", "Could not retrieve IDs for selected student, subject, or semester.");
            return;
        }

        try (Connection con = DBS.getConnection()) {
            // 1. Check if Rapor entry exists for this student and semester, create if not
            String raporIdSql = "SELECT rapor_id FROM Rapor WHERE Users_user_id = ? AND Semester_semester_id = ?";
            int raporId = -1;
            try (PreparedStatement raporStmt = con.prepareStatement(raporIdSql)) {
                raporStmt.setString(1, studentUserId);
                raporStmt.setInt(2, semesterId);
                ResultSet rs = raporStmt.executeQuery();
                if (rs.next()) {
                    raporId = rs.getInt("rapor_id");
                } else {
                    // Create new Rapor entry
                    String insertRaporSql = "INSERT INTO Rapor (Users_user_id, Semester_semester_id) VALUES (?, ?) RETURNING rapor_id";
                    try (PreparedStatement insertRaporStmt = con.prepareStatement(insertRaporSql)) {
                        insertRaporStmt.setString(1, studentUserId);
                        insertRaporStmt.setInt(2, semesterId);
                        ResultSet newRaporRs = insertRaporStmt.executeQuery();
                        if (newRaporRs.next()) {
                            raporId = newRaporRs.getInt("rapor_id");
                        }
                    }
                }
            }

            if (raporId == -1) {
                AlertClass.ErrorAlert("Database Error", "Failed to create or retrieve Rapor entry", "Could not associate grade with a report card.");
                return;
            }

            // 2. Insert the grade
            String insertNilaiSql = "INSERT INTO Nilai (nilai, jenis_nilai, Matpel_mapel_id, Rapor_rapor_id) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(insertNilaiSql);
            stmt.setInt(1, score);
            stmt.setString(2, jenisNilai);
            stmt.setInt(3, mapelId);
            stmt.setInt(4, raporId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                AlertClass.InformationAlert("Success", "Grade Submitted", "Grade for " + selectedStudentDisplay + " submitted successfully.");
                gradeTypeField.clear();
                scoreField.clear();
                loadExistingGrades(); // Refresh existing grades table
            } else {
                AlertClass.ErrorAlert("Failed", "Grade Not Submitted", "Failed to submit grade to the database.");
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to submit grade", e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadExistingGrades() {
        ObservableList<NilaiEntry> existingGrades = FXCollections.observableArrayList();
        String selectedStudentDisplay = gradeStudentChoiceBox.getValue();
        String selectedSubjectDisplay = gradeSubjectChoiceBox.getValue();
        String selectedSemesterDisplay = gradeSemesterChoiceBox.getValue();

        if (selectedStudentDisplay == null || selectedSubjectDisplay == null || selectedSemesterDisplay == null) {
            existingGradesTable.setItems(FXCollections.emptyObservableList());
            return;
        }

        String studentUserId = studentsMap.get(selectedStudentDisplay);
        Integer mapelId = subjectsMap.get(selectedSubjectDisplay);
        Integer semesterId = semestersMap.get(selectedSemesterDisplay);

        if (studentUserId == null || mapelId == null || semesterId == null) {
            existingGradesTable.setItems(FXCollections.emptyObservableList());
            return;
        }

        String sql = "SELECT n.jenis_nilai, m.nama_mapel, n.nilai " +
                "FROM Nilai n " +
                "JOIN Matpel m ON n.Matpel_mapel_id = m.mapel_id " +
                "JOIN Rapor r ON n.Rapor_rapor_id = r.rapor_id " +
                "WHERE r.Users_user_id = ? AND r.Semester_semester_id = ? AND n.Matpel_mapel_id = ?";

        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, studentUserId);
            stmt.setInt(2, semesterId);
            stmt.setInt(3, mapelId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                existingGrades.add(new NilaiEntry(
                        rs.getString("jenis_nilai"),
                        rs.getString("nama_mapel"),
                        rs.getInt("nilai")
                ));
            }
            existingGradesTable.setItems(existingGrades);
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load existing grades", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleLogout() {
        try {
            HelloApplication.getInstance().changeScene("login-view.fxml", "Login Aplikasi", 1300, 600);
        } catch (IOException e) {
            AlertClass.ErrorAlert("Error", "Logout Failed", "Could not return to login screen.");
            e.printStackTrace();
        }
    }
}