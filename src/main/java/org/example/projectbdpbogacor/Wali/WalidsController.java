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
import org.example.projectbdpbogacor.model.AbsensiWaliEntry; // Use the specific model for Wali
import org.example.projectbdpbogacor.model.NilaiEntry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.awt.Desktop; // Required for opening file
import java.time.LocalDateTime; // For accurate time handling
import java.time.temporal.TemporalAdjusters; // For end of month/year


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

        // Add listeners to tab and choice boxes
        attendanceClassChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> handleClassSelectionForAttendance());
        attendanceStudentChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> loadAbsensiData());

        raporClassChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> handleClassSelectionForRapor());
        raporStudentChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> loadNilaiData());
        raporSemesterChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> loadNilaiData());

        // Add listeners to tabs to load data when selected
        waliTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null) {
                if (newTab.getText().equals("Student Attendance")) {
                    loadClassesForWaliKelas(); // Refresh classes
                    if (!attendanceClassChoiceBox.getItems().isEmpty()) {
                        attendanceClassChoiceBox.setValue(attendanceClassChoiceBox.getItems().get(0)); // Select first class by default
                    }
                } else if (newTab.getText().equals("Print Report Card")) {
                    loadClassesForWaliKelas(); // Refresh classes
                    loadSemesters(); // Refresh semesters
                    if (!raporClassChoiceBox.getItems().isEmpty()) {
                        raporClassChoiceBox.setValue(raporClassChoiceBox.getItems().get(0)); // Select first class by default
                    }
                    if (!raporSemesterChoiceBox.getItems().isEmpty()) {
                        raporSemesterChoiceBox.setValue(raporSemesterChoiceBox.getItems().get(0)); // Select first semester by default
                    }
                }
            }
        });

        // Load initial data for the currently selected tab (which is usually the first one)
        if (waliTabPane.getSelectionModel().getSelectedItem().getText().equals("Student Attendance")) {
            loadClassesForWaliKelas();
            if (!attendanceClassChoiceBox.getItems().isEmpty()) {
                attendanceClassChoiceBox.setValue(attendanceClassChoiceBox.getItems().get(0));
            }
        } else if (waliTabPane.getSelectionModel().getSelectedItem().getText().equals("Print Report Card")) {
            loadClassesForWaliKelas();
            loadSemesters();
            if (!raporClassChoiceBox.getItems().isEmpty()) {
                raporClassChoiceBox.setValue(raporClassChoiceBox.getItems().get(0));
            }
            if (!raporSemesterChoiceBox.getItems().isEmpty()) {
                raporSemesterChoiceBox.setValue(raporSemesterChoiceBox.getItems().get(0));
            }
        }
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
                // Store as "WaliID-KelasID" since Kelas has a composite primary key
                String combinedId = loggedInUserId + "-" + kelasId;
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
    void handlePrintReportCard() {
        String selectedClassDisplay = raporClassChoiceBox.getValue();
        String selectedStudentDisplay = raporStudentChoiceBox.getValue();
        String selectedSemesterDisplay = raporSemesterChoiceBox.getValue();

        if (selectedClassDisplay == null || selectedStudentDisplay == null || selectedSemesterDisplay == null) {
            AlertClass.WarningAlert("Input Error", "Missing Information", "Please select a class, student, and semester to print the report card.");
            return;
        }

        String studentUserId = studentsMap.get(selectedStudentDisplay);
        Integer semesterId = semestersMap.get(selectedSemesterDisplay);

        if (studentUserId == null || semesterId == null) {
            AlertClass.ErrorAlert("Selection Error", "Invalid Selection", "Could not retrieve IDs for selected student or semester.");
            return;
        }

        try {
            // 1. Fetch student biodata
            Map<String, String> studentBiodata = getStudentBiodata(studentUserId);

            // 2. Fetch grades (already loaded in nilaiUjianTable, but we can re-fetch for robustness)
            ObservableList<NilaiEntry> grades = nilaiUjianTable.getItems(); // Use the already loaded data

            // 3. Fetch attendance summary (e.g., total Hadir, Alpha, Ijin for the selected student and semester)
            Map<String, Integer> attendanceSummary = getAttendanceSummary(studentUserId, semesterId);

            // 4. Generate HTML content
            String htmlContent = generateReportCardHtml(studentBiodata, grades, attendanceSummary, selectedClassDisplay, selectedSemesterDisplay);

            // 5. Save to a temporary HTML file
            // Create the "rapor" directory if it doesn't exist
            File raporDir = new File("Rapor");
            if (!raporDir.exists()) {
                raporDir.mkdirs(); // Creates the directory and any necessary but nonexistent parent directories.
            }

            // Save the file inside the "rapor" directory
            File outputFile = new File(raporDir, "rapor_" + studentUserId + "_" + semesterId + ".html");
            try (FileWriter writer = new FileWriter(outputFile)) {
                writer.write(htmlContent);
            }

            // 6. Open the file in the default browser
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(outputFile.toURI());
                AlertClass.InformationAlert("Report Card Generated", "Report card saved and opened.", "The report card for " + studentBiodata.get("nama") + " has been generated and opened in your browser.");
            } else {
                AlertClass.InformationAlert("Report Card Generated", "Report card saved.", "The report card for " + studentBiodata.get("nama") + " has been generated at: " + outputFile.getAbsolutePath() + "\nYou can open it manually.");
            }

        } catch (IOException e) {
            AlertClass.ErrorAlert("File Error", "Failed to generate report card file", e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to retrieve data for report card", e.getMessage());
            e.printStackTrace();
        }
    }

    private Map<String, String> getStudentBiodata(String userId) throws SQLException {
        Map<String, String> biodata = new HashMap<>();
        // Fetch both user_id and NIS_NIP
        String sql = "SELECT user_id, username, NIS_NIP, nama, gender, alamat, email, nomer_hp FROM Users WHERE user_id = ?";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                biodata.put("user_id", rs.getString("user_id"));
                biodata.put("username", rs.getString("username"));
                biodata.put("NIS_NIP", rs.getString("NIS_NIP"));
                biodata.put("nama", rs.getString("nama"));
                biodata.put("gender", rs.getString("gender").equals("L") ? "Laki-laki" : "Perempuan");
                biodata.put("alamat", rs.getString("alamat"));
                biodata.put("email", rs.getString("email"));
                biodata.put("nomer_hp", rs.getString("nomer_hp"));
            }
        }
        return biodata;
    }

    private Map<String, Integer> getAttendanceSummary(String studentUserId, int semesterId) throws SQLException {
        Map<String, Integer> summary = new HashMap<>();
        summary.put("Hadir", 0);
        summary.put("Alpha", 0);
        summary.put("Ijin", 0);

        // Fetch the semester's start date
        String semesterDateSql = "SELECT tahun_ajaran, semester, tahun FROM Semester WHERE semester_id = ?";
        LocalDateTime semesterStart = null;
        String semesterName = null;
        String tahunAjaran = null;

        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(semesterDateSql)) {
            stmt.setInt(1, semesterId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                semesterStart = rs.getTimestamp("tahun").toLocalDateTime();
                semesterName = rs.getString("semester");
                tahunAjaran = rs.getString("tahun_ajaran");
            } else {
                System.err.println("Semester with ID " + semesterId + " not found.");
                return summary; // No semester found, return empty summary
            }
        }

        if (semesterStart == null) {
            return summary;
        }

        // Determine semester end date based on semester name and start year
        LocalDateTime semesterEnd;
        if ("Ganjil".equalsIgnoreCase(semesterName)) {
            // Ganjil semester typically ends in December of the same year
            semesterEnd = LocalDateTime.of(semesterStart.getYear(), 12, 31, 23, 59, 59);
        } else if ("Genap".equalsIgnoreCase(semesterName)) {
            // Genap semester typically starts in Jan-Feb of the next year (if tahun_ajaran spans two years)
            // and ends around June-July of that next year.
            // For simplicity, let's assume it ends in June of the year it's primarily in.
            // A more robust solution might involve storing explicit start/end dates for semesters.
            int endYear = semesterStart.getYear();
            if (tahunAjaran != null && tahunAjaran.contains("/")) {
                try {
                    String[] years = tahunAjaran.split("/");
                    if (years.length == 2) {
                        endYear = Integer.parseInt(years[1]); // End year of the academic year
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing tahun_ajaran: " + e.getMessage());
                }
            }
            semesterEnd = LocalDateTime.of(endYear, 6, 30, 23, 59, 59); // Assuming end of June
        } else {
            // Fallback: assume 6 months duration if semester name is not Ganjil/Genap
            semesterEnd = semesterStart.plusMonths(6).minusDays(1).withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        }

        System.out.println("Calculating attendance for student: " + studentUserId +
                " from " + semesterStart.format(DateTimeFormatter.ISO_LOCAL_DATE) +
                " to " + semesterEnd.format(DateTimeFormatter.ISO_LOCAL_DATE));

        String sql = "SELECT a.status, COUNT(a.status) AS count " +
                "FROM Absensi a " +
                "WHERE a.Users_user_id = ? " +
                "AND a.tanggal >= ? AND a.tanggal <= ? " +
                "GROUP BY a.status";

        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, studentUserId);
            stmt.setTimestamp(2, Timestamp.valueOf(semesterStart));
            stmt.setTimestamp(3, Timestamp.valueOf(semesterEnd));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                summary.put(rs.getString("status"), rs.getInt("count"));
            }
        }
        return summary;
    }


    private String generateReportCardHtml(Map<String, String> biodata, ObservableList<NilaiEntry> grades, Map<String, Integer> attendance, String className, String semesterName) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"en\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("    <title>Report Card - ").append(biodata.get("nama")).append("</title>\n");
        html.append("    <style>\n");
        html.append("        body { font-family: Arial, sans-serif; margin: 20px; }\n");
        html.append("        .container { width: 800px; margin: 0 auto; border: 1px solid #ccc; padding: 20px; box-shadow: 2px 2px 8px rgba(0,0,0,0.1); }\n");
        html.append("        h1, h2 { text-align: center; color: #333; }\n");
        html.append("        .info-section, .grades-section, .attendance-section { margin-bottom: 20px; }\n");
        html.append("        .info-section p { margin: 5px 0; }\n");
        html.append("        table { width: 100%; border-collapse: collapse; margin-top: 10px; }\n");
        html.append("        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n");
        html.append("        th { background-color: #f2f2f2; }\n");
        html.append("        .footer { text-align: right; margin-top: 30px; font-size: 0.9em; color: #555; }\n");
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("    <div class=\"container\">\n");
        html.append("        <h1>School Report Card</h1>\n");
        html.append("        <h2>").append(semesterName).append("</h2>\n");

        html.append("        <div class=\"info-section\">\n");
        html.append("            <h3>Student Information:</h3>\n");
        html.append("            <p><strong>Name:</strong> ").append(biodata.get("nama")).append("</p>\n");
        // Displaying both Student ID (user_id) and NIS/NIP
        html.append("            <p><strong>Student ID:</strong> ").append(biodata.get("user_id")).append("</p>\n");
        html.append("            <p><strong>NIS:</strong> ").append(biodata.get("NIS_NIP")).append("</p>\n");
        html.append("            <p><strong>Class:</strong> ").append(className).append("</p>\n");
        html.append("            <p><strong>Gender:</strong> ").append(biodata.get("gender")).append("</p>\n");
        html.append("            <p><strong>Email:</strong> ").append(biodata.get("email")).append("</p>\n");
        html.append("            <p><strong>Phone:</strong> ").append(biodata.get("nomer_hp")).append("</p>\n");
        html.append("            <p><strong>Address:</strong> ").append(biodata.get("alamat")).append("</p>\n");
        html.append("        </div>\n");

        html.append("        <div class=\"grades-section\">\n");
        html.append("            <h3>Academic Grades:</h3>\n");
        html.append("            <table>\n");
        html.append("                <thead>\n");
        html.append("                    <tr>\n");
        html.append("                        <th>Subject</th>\n");
        html.append("                        <th>Type of Grade</th>\n");
        html.append("                        <th>Score</th>\n");
        html.append("                    </tr>\n");
        html.append("                </thead>\n");
        html.append("                <tbody>\n");
        if (grades != null && !grades.isEmpty()) {
            for (NilaiEntry grade : grades) {
                html.append("                    <tr>\n");
                html.append("                        <td>").append(grade.getNamaMapel()).append("</td>\n");
                html.append("                        <td>").append(grade.getJenisNilai()).append("</td>\n");
                html.append("                        <td>").append(grade.getNilai()).append("</td>\n");
                html.append("                    </tr>\n");
            }
        } else {
            html.append("                    <tr><td colspan=\"3\">No grades available for this semester.</td></tr>\n");
        }
        html.append("                </tbody>\n");
        html.append("            </table>\n");
        html.append("        </div>\n");

        html.append("        <div class=\"attendance-section\">\n");
        html.append("            <h3>Attendance Summary:</h3>\n");
        html.append("            <p><strong>Hadir (Present):</strong> ").append(attendance.getOrDefault("Hadir", 0)).append(" days</p>\n");
        html.append("            <p><strong>Alpha (Absent without leave):</strong> ").append(attendance.getOrDefault("Alpha", 0)).append(" days</p>\n");
        html.append("            <p><strong>Ijin (Excused):</strong> ").append(attendance.getOrDefault("Ijin", 0)).append(" days</p>\n");
        html.append("        </div>\n");

        html.append("        <div class=\"footer\">\n");
        html.append("            <p>Generated on: ").append(java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("</p>\n");
        html.append("            <p>Wali Kelas: ").append(welcomeUserLabel.getText().replace("Welcome, ", "")).append("</p>\n"); // Get Wali Kelas name from welcome label
        html.append("        </div>\n");

        html.append("    </div>\n");
        html.append("</body>\n");
        html.append("</html>");

        return html.toString();
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