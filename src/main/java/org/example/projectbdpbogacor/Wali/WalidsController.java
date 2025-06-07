// ProjectBDPBOgacor/src/main/java/org/example/projectbdpbogacor/Wali/WalidsController.java
package org.example.projectbdpbogacor.Wali;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.projectbdpbogacor.Aset.AlertClass;
import org.example.projectbdpbogacor.DBSource.DBS;
import org.example.projectbdpbogacor.HelloApplication;
import org.example.projectbdpbogacor.model.AbsensiWaliEntry;
import org.example.projectbdpbogacor.model.NilaiEntry;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class WalidsController {

    @FXML
    private Label welcomeUserLabel;
    @FXML
    private TabPane waliTabPane;

    // Student Attendance
    @FXML
    private ChoiceBox<String> attendanceClassChoiceBox; // Display: "Nama Kelas"
    @FXML
    private ChoiceBox<String> attendanceStudentChoiceBox; // Display: "Student Name (NIS/NIP)"
    @FXML
    private TableView<AbsensiWaliEntry> absensiTable;
    @FXML
    private TableColumn<AbsensiWaliEntry, String> studentNameAbsensiColumn;
    @FXML
    private TableColumn<AbsensiWaliEntry, String> tanggalAbsensiColumn;
    @FXML
    private TableColumn<AbsensiWaliEntry, String> statusAbsensiColumn;
    @FXML
    private TableColumn<AbsensiWaliEntry, String> mapelAbsensiColumn;
    @FXML
    private TableColumn<AbsensiWaliEntry, String> kelasAbsensiColumn;
    @FXML
    private TableColumn<AbsensiWaliEntry, String> jamMulaiAbsensiColumn;
    @FXML
    private TableColumn<AbsensiWaliEntry, String> jamSelesaiAbsensiColumn;

    // Print Report Card
    @FXML
    private ChoiceBox<String> raporClassChoiceBox; // Display: "Nama Kelas"
    @FXML
    private ChoiceBox<String> raporStudentChoiceBox; // Display: "Student Name (NIS/NIP)"
    @FXML
    private ChoiceBox<String> raporSemesterChoiceBox; // Display: "Tahun Ajaran - Semester"
    @FXML
    private TableView<NilaiEntry> nilaiUjianTable;
    @FXML
    private TableColumn<NilaiEntry, String> jenisNilaiColumn;
    @FXML
    private TableColumn<NilaiEntry, String> namaMapelNilaiColumn;
    @FXML
    private TableColumn<NilaiEntry, Integer> nilaiColumn;

    private String loggedInUserId;

    // Maps to store IDs corresponding to displayed names in ChoiceBoxes
    private Map<String, String> classesMap = new HashMap<>(); // Display -> Kelas_Users_user_id-kelas_id
    private Map<String, String> studentsMap = new HashMap<>(); // Display -> user_id
    private Map<String, Integer> semestersMap = new HashMap<>(); // Display -> semester_id

    @FXML
    void initialize() {
        loggedInUserId = HelloApplication.getInstance().getLoggedInUserId();
        loadWaliKelasName();

        // Initialize TableView columns
        initAbsensiTable();
        initNilaiUjianTable();

        // Load initial data for ChoiceBoxes
        loadClassesForWaliKelas();
        loadSemesters();

        // Add listeners to tabs to load data when selected
        waliTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null) {
                if (newTab.getText().equals("Student Attendance")) {
                    loadClassesForWaliKelas(); // Refresh classes
                    // Students are loaded based on class selection
                } else if (newTab.getText().equals("Print Report Card")) {
                    loadClassesForWaliKelas(); // Refresh classes
                    loadSemesters(); // Refresh semesters
                    // Students are loaded based on class selection
                }
            }
        });
    }

    private void loadWaliKelasName() {
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

    // --- Common Helper Methods for ChoiceBoxes ---
    private void loadClassesForWaliKelas() {
        classesMap.clear();
        attendanceClassChoiceBox.getItems().clear();
        raporClassChoiceBox.getItems().clear();

        // Get classes where this user is the Wali Kelas (Kelas.Users_user_id)
        String sql = "SELECT kelas_id, nama_kelas FROM Kelas WHERE Users_user_id = ?";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, loggedInUserId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int kelasId = rs.getInt("kelas_id");
                String namaKelas = rs.getString("nama_kelas");
                String combinedId = loggedInUserId + "-" + kelasId; // Store as "WaliID-KelasID"
                classesMap.put(namaKelas, combinedId); // Store display name -> combined ID
                attendanceClassChoiceBox.getItems().add(namaKelas);
                raporClassChoiceBox.getItems().add(namaKelas);
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load classes", e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadStudentsInClass(String selectedClassDisplay, ChoiceBox<String> studentChoiceBoxToPopulate) {
        studentChoiceBoxToPopulate.getItems().clear();
        studentsMap.clear();

        if (selectedClassDisplay == null || selectedClassDisplay.isEmpty()) {
            return; // No class selected
        }

        String combinedId = classesMap.get(selectedClassDisplay);
        if (combinedId == null) {
            return; // Should not happen if data is consistent
        }
        String[] ids = combinedId.split("-");
        String waliIdFromKelas = ids[0]; // This is the Wali Kelas's user_id from Kelas PK
        int kelasId = Integer.parseInt(ids[1]);

        // Query students enrolled in the selected class
        String sql = "SELECT u.user_id, u.nama, u.NIS_NIP FROM Users u " +
                "JOIN Student_Class_Enrollment sce ON u.user_id = sce.Users_user_id " +
                "WHERE sce.Kelas_kelas_id = ? AND sce.Kelas_Users_user_id = ?";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, kelasId);
            stmt.setString(2, waliIdFromKelas);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String userId = rs.getString("user_id");
                String nama = rs.getString("nama");
                String nisNip = rs.getString("NIS_NIP");
                String display = nama + " (NIS: " + nisNip + ")";
                studentsMap.put(display, userId);
                studentChoiceBoxToPopulate.getItems().add(display);
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load students", e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadSemesters() {
        semestersMap.clear();
        raporSemesterChoiceBox.getItems().clear();

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
                raporSemesterChoiceBox.getItems().add(display);
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load semesters", e.getMessage());
            e.printStackTrace();
        }
    }

    // --- Student Attendance Methods ---
    private void initAbsensiTable() {
        studentNameAbsensiColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        tanggalAbsensiColumn.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        statusAbsensiColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        mapelAbsensiColumn.setCellValueFactory(new PropertyValueFactory<>("namaMapel"));
        kelasAbsensiColumn.setCellValueFactory(new PropertyValueFactory<>("namaKelas"));
        jamMulaiAbsensiColumn.setCellValueFactory(new PropertyValueFactory<>("jamMulai"));
        jamSelesaiAbsensiColumn.setCellValueFactory(new PropertyValueFactory<>("jamSelesai"));
    }

    @FXML
    void handleClassSelectionForAttendance() {
        String selectedClassDisplay = attendanceClassChoiceBox.getValue();
        loadStudentsInClass(selectedClassDisplay, attendanceStudentChoiceBox);
        loadAbsensiData(); // Load attendance for selected class
    }

    @FXML
    void loadAbsensiData() {
        ObservableList<AbsensiWaliEntry> absensiList = FXCollections.observableArrayList();
        String selectedClassDisplay = attendanceClassChoiceBox.getValue();
        String selectedStudentDisplay = attendanceStudentChoiceBox.getValue();

        String sql = "SELECT u.nama AS student_name, a.tanggal, a.status, m.nama_mapel, k.nama_kelas, j.jam_mulai, j.jam_selsai " +
                "FROM Absensi a " +
                "JOIN Users u ON a.Users_user_id = u.user_id " +
                "JOIN Jadwal j ON a.Jadwal_jadwal_id = j.jadwal_id " +
                "JOIN Matpel m ON j.Matpel_mapel_id = m.mapel_id " +
                "JOIN Kelas k ON j.Kelas_Users_user_id = k.Users_user_id AND j.Kelas_kelas_id = k.kelas_id " +
                "JOIN Student_Class_Enrollment sce ON u.user_id = sce.Users_user_id AND k.kelas_id = sce.Kelas_kelas_id AND k.Users_user_id = sce.Kelas_Users_user_id " +
                "WHERE k.Users_user_id = ?"; // Filter by Wali Kelas's user_id

        if (selectedClassDisplay != null && !selectedClassDisplay.isEmpty()) {
            String combinedClassId = classesMap.get(selectedClassDisplay);
            if (combinedClassId != null) {
                String[] ids = combinedClassId.split("-");
                String waliIdFromKelas = ids[0];
                int kelasId = Integer.parseInt(ids[1]);
                sql += " AND k.kelas_id = ? AND k.Users_user_id = ?";
            }
        }
        if (selectedStudentDisplay != null && !selectedStudentDisplay.isEmpty()) {
            String studentId = studentsMap.get(selectedStudentDisplay);
            if (studentId != null) {
                sql += " AND u.user_id = ?";
            }
        }

        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            int paramIndex = 1;
            stmt.setString(paramIndex++, loggedInUserId); // Wali Kelas filter

            if (selectedClassDisplay != null && !selectedClassDisplay.isEmpty()) {
                String combinedClassId = classesMap.get(selectedClassDisplay);
                if (combinedClassId != null) {
                    String[] ids = combinedClassId.split("-");
                    stmt.setInt(paramIndex++, Integer.parseInt(ids[1])); // Kelas ID
                    stmt.setString(paramIndex++, ids[0]); // Wali Kelas User ID for Kelas PK
                }
            }
            if (selectedStudentDisplay != null && !selectedStudentDisplay.isEmpty()) {
                String studentId = studentsMap.get(selectedStudentDisplay);
                if (studentId != null) {
                    stmt.setString(paramIndex++, studentId);
                }
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                absensiList.add(new AbsensiWaliEntry(
                        rs.getString("student_name"),
                        rs.getTimestamp("tanggal").toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        rs.getString("status"),
                        rs.getString("nama_mapel"),
                        rs.getString("nama_kelas"),
                        rs.getString("jam_mulai"),
                        rs.getString("jam_selsai")
                ));
            }
            absensiTable.setItems(absensiList);
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load attendance data", e.getMessage());
            e.printStackTrace();
        }
    }

    // --- Print Report Card Methods ---
    private void initNilaiUjianTable() {
        jenisNilaiColumn.setCellValueFactory(new PropertyValueFactory<>("jenisNilai"));
        namaMapelNilaiColumn.setCellValueFactory(new PropertyValueFactory<>("namaMapel"));
        nilaiColumn.setCellValueFactory(new PropertyValueFactory<>("nilai"));
    }

    @FXML
    void handleClassSelectionForRapor() {
        String selectedClassDisplay = raporClassChoiceBox.getValue();
        loadStudentsInClass(selectedClassDisplay, raporStudentChoiceBox);
        loadNilaiData(); // Load data when class changes
    }

    @FXML
    void loadNilaiData() {
        ObservableList<NilaiEntry> nilaiList = FXCollections.observableArrayList();
        String selectedStudentDisplay = raporStudentChoiceBox.getValue();
        String selectedSemesterDisplay = raporSemesterChoiceBox.getValue();

        if (selectedStudentDisplay == null || selectedSemesterDisplay == null) {
            nilaiUjianTable.setItems(FXCollections.emptyObservableList());
            return; // No student or semester selected
        }

        String studentUserId = studentsMap.get(selectedStudentDisplay);
        Integer semesterId = semestersMap.get(selectedSemesterDisplay);

        if (studentUserId == null || semesterId == null) {
            AlertClass.WarningAlert("Selection Error", "Invalid Selection", "Please select a valid student and semester.");
            return;
        }

        String sql = "SELECT n.jenis_nilai, m.nama_mapel, n.nilai " +
                "FROM Nilai n " +
                "JOIN Matpel m ON n.Matpel_mapel_id = m.mapel_id " +
                "JOIN Rapor r ON n.Rapor_rapor_id = r.rapor_id " +
                "WHERE r.Users_user_id = ? AND r.Semester_semester_id = ?";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, studentUserId);
            stmt.setInt(2, semesterId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                nilaiList.add(new NilaiEntry(
                        rs.getString("jenis_nilai"),
                        rs.getString("nama_mapel"),
                        rs.getInt("nilai")
                ));
            }
            nilaiUjianTable.setItems(nilaiList);
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load exam scores", e.getMessage());
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