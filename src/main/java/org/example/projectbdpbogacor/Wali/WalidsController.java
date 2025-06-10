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
    private Label attendanceClassNameLabel; // Changed from ChoiceBox to Label
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
    private Label raporClassNameLabel; // Changed from ChoiceBox to Label
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
    private String waliKelasId; // To store the wali_kelas_user_id (same as loggedInUserId)
    private int kelasId; // To store the kelas_id that this wali kelas manages
    private String kelasName; // To store the name of the class this wali kelas manages

    // Maps to store IDs corresponding to displayed names in ChoiceBoxes
    private Map<String, String> studentsMap = new HashMap<>(); // Display -> user_id
    private Map<String, Integer> semestersMap = new HashMap<>(); // Display -> semester_id

    @FXML
    void initialize() {
        loggedInUserId = HelloApplication.getInstance().getLoggedInUserId();
        loadWaliKelasNameAndClassInfo(); // Load wali kelas name and their class info

        // Initialize TableView columns
        initAbsensiTable();
        initNilaiUjianTable();

        // Load initial data for ChoiceBoxes
        loadSemesters(); // Semesters are still selected

        // Add listeners to tab and choice boxes
        attendanceStudentChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> loadAbsensiData());
        raporStudentChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> loadNilaiData());
        raporSemesterChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> loadNilaiData());

        // Add listeners to tabs to load data when selected
        waliTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null) {
                if (newTab.getText().equals("Student Attendance")) {
                    loadStudentsInClass(attendanceStudentChoiceBox); // Load students for the attendance tab
                    loadAbsensiData(); // Load attendance for the managed class
                } else if (newTab.getText().equals("Print Report Card")) {
                    loadStudentsInClass(raporStudentChoiceBox); // Load students for the report card tab
                    loadSemesters(); // Refresh semesters
                    if (!raporSemesterChoiceBox.getItems().isEmpty()) {
                        raporSemesterChoiceBox.setValue(raporSemesterChoiceBox.getItems().get(0)); // Select first semester by default
                    }
                    loadNilaiData(); // Load grades for the managed class
                }
            }
        });

        // Load initial data for the currently selected tab (which is usually the first one)
        if (waliTabPane.getSelectionModel().getSelectedItem().getText().equals("Student Attendance")) {
            loadStudentsInClass(attendanceStudentChoiceBox);
            loadAbsensiData();
        } else if (waliTabPane.getSelectionModel().getSelectedItem().getText().equals("Print Report Card")) {
            loadStudentsInClass(raporStudentChoiceBox);
            loadSemesters();
            if (!raporSemesterChoiceBox.getItems().isEmpty()) {
                raporSemesterChoiceBox.setValue(raporSemesterChoiceBox.getItems().get(0));
            }
            loadNilaiData();
        }
    }

    private void loadWaliKelasNameAndClassInfo() {
        String sql = "SELECT u.nama, k.kelas_id, k.nama_kelas " +
                "FROM Users u JOIN Kelas k ON u.user_id = k.Users_user_id " +
                "WHERE u.user_id = ?";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, loggedInUserId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                welcomeUserLabel.setText("Welcome, " + rs.getString("nama") + "!"); // Set welcome label
                this.waliKelasId = loggedInUserId; // Store wali_kelas_user_id
                this.kelasId = rs.getInt("kelas_id"); // Store kelas_id
                this.kelasName = rs.getString("nama_kelas"); // Store kelas_name

                attendanceClassNameLabel.setText("Kelas: " + kelasName); // Set class name for attendance tab
                raporClassNameLabel.setText("Kelas: " + kelasName); // Set class name for report card tab

            } else {
                AlertClass.WarningAlert("Class Not Found", "Wali Kelas has no assigned class", "Please ensure this Wali Kelas is assigned to a class.");
                welcomeUserLabel.setText("Welcome, Wali Kelas!");
                attendanceClassNameLabel.setText("Kelas: Not Assigned");
                raporClassNameLabel.setText("Kelas: Not Assigned");
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load user name or class info", e.getMessage());
            e.printStackTrace();
        }
    }

    // --- Common Helper Methods for ChoiceBoxes ---
    // This method is now simplified as it doesn't need to load multiple classes
    private void loadStudentsInClass(ChoiceBox<String> studentChoiceBoxToPopulate) {
        studentChoiceBoxToPopulate.getItems().clear();
        studentsMap.clear();

        if (this.kelasId == 0) { // Check if class info is loaded
            return;
        }

        // Query students enrolled in the managed class
        String sql = "SELECT u.user_id, u.nama, u.NIS_NIP FROM Users u " +
                "JOIN student_class_enrollment sce ON u.user_id = sce.Users_user_id " + // Corrected table name
                "WHERE sce.Kelas_kelas_id = ? AND sce.Kelas_Users_user_id = ?";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, this.kelasId); // Use the pre-loaded kelasId
            stmt.setString(2, this.waliKelasId); // Use the pre-loaded waliKelasId
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String userId = rs.getString("user_id");
                String nama = rs.getString("nama");
                String nisNip = rs.getString("NIS_NIP");
                String display = nama + " (NIS: " + nisNip + ")";
                studentsMap.put(display, userId);
                studentChoiceBoxToPopulate.getItems().add(display);
            }
            if (!studentChoiceBoxToPopulate.getItems().isEmpty()) {
                studentChoiceBoxToPopulate.setValue(studentChoiceBoxToPopulate.getItems().get(0)); // Select first student by default
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

    // Removed handleClassSelectionForAttendance as class is now fixed.
    // The student choice box selection listener will now trigger loadAbsensiData.

    @FXML
    void loadAbsensiData() {
        ObservableList<AbsensiWaliEntry> absensiList = FXCollections.observableArrayList();
        String selectedStudentDisplay = attendanceStudentChoiceBox.getValue();

        if (this.kelasId == 0 || selectedStudentDisplay == null) { // Ensure class and student are selected
            absensiTable.setItems(FXCollections.emptyObservableList());
            return;
        }

        String studentId = studentsMap.get(selectedStudentDisplay);
        if (studentId == null) {
            absensiTable.setItems(FXCollections.emptyObservableList());
            return;
        }

        String sql = "SELECT u.nama AS student_name, a.tanggal, a.status, m.nama_mapel, k.nama_kelas, j.jam_mulai, j.jam_selsai " +
                "FROM Absensi a " +
                "JOIN Users u ON a.Users_user_id = u.user_id " +
                "JOIN Jadwal j ON a.Jadwal_jadwal_id = j.jadwal_id " +
                "JOIN Matpel m ON j.Matpel_mapel_id = m.mapel_id " +
                "JOIN Kelas k ON j.Kelas_Users_user_id = k.Users_user_id AND j.Kelas_kelas_id = k.kelas_id " +
                "JOIN student_class_enrollment sce ON u.user_id = sce.Users_user_id AND k.kelas_id = sce.Kelas_kelas_id AND k.Users_user_id = sce.Kelas_Users_user_id " + // Corrected table name
                "WHERE k.kelas_id = ? AND k.Users_user_id = ? AND u.user_id = ?"; // Filter by managed class and selected student

        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, this.kelasId); // Use the pre-loaded kelasId
            stmt.setString(2, this.waliKelasId); // Use the pre-loaded waliKelasId
            stmt.setString(3, studentId); // Filter by selected student

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

    // Removed handleClassSelectionForRapor as class is now fixed.
    // The student and semester choice box selection listeners will now trigger loadNilaiData.

    @FXML
    void loadNilaiData() {
        ObservableList<NilaiEntry> nilaiList = FXCollections.observableArrayList();
        String selectedStudentDisplay = raporStudentChoiceBox.getValue();
        String selectedSemesterDisplay = raporSemesterChoiceBox.getValue();

        if (selectedStudentDisplay == null || selectedSemesterDisplay == null || this.kelasId == 0) { // Ensure class, student, and semester are selected
            nilaiUjianTable.setItems(FXCollections.emptyObservableList());
            return;
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
        String selectedStudentDisplay = raporStudentChoiceBox.getValue();
        String selectedSemesterDisplay = raporSemesterChoiceBox.getValue();

        if (selectedStudentDisplay == null || selectedSemesterDisplay == null || this.kelasId == 0) { // Ensure class, student, and semester are selected
            AlertClass.WarningAlert("Input Error", "Missing Information", "Please select a student and semester to print the report card.");
            return;
        }

        String studentUserId = studentsMap.get(selectedStudentDisplay);
        Integer semesterId = semestersMap.get(selectedSemesterDisplay);

        if (studentUserId == null || semesterId == null) {
            AlertClass.ErrorAlert("Selection Error", "Invalid Selection", "Could not retrieve IDs for selected student or semester.");
            return;
        }

        try {
            // 1. Fetch student biodata and class name
            Map<String, String> studentBiodata = getStudentBiodata(studentUserId);
            String className = this.kelasName; // Use the pre-loaded class name

            // 2. Fetch grades (already loaded in nilaiUjianTable, but we can re-fetch for robustness)
            ObservableList<NilaiEntry> grades = nilaiUjianTable.getItems(); // Use the already loaded data

            // 3. Calculate average grade and letter grade
            double averageGrade = calculateAverageGrade(grades); // Still calculate for potential future use or debugging
            String letterGrade = getGradeLetter(averageGrade); // Still calculate for potential future use or debugging

            // 4. Fetch attendance summary (e.g., total Hadir, Alpha, Ijin for the selected student and semester)
            Map<String, Integer> attendanceSummary = getAttendanceSummary(studentUserId, semesterId);

            // 5. Generate HTML content
            String htmlContent = generateReportCardHtml(studentBiodata, averageGrade, letterGrade, attendanceSummary, className, selectedSemesterDisplay, grades);

            // 6. Save to a temporary HTML file
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

            // 7. Open the file in the default browser
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
        String sql = "SELECT u.user_id, u.username, u.NIS_NIP, u.nama, u.gender, u.alamat, u.email, u.nomer_hp FROM Users u WHERE u.user_id = ?";
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

    private double calculateAverageGrade(ObservableList<NilaiEntry> grades) {
        // This calculation is for an overall average based on fixed weights.
        // It's still used to determine the individual subject average as well.
        double uts = 0;
        double uas = 0;
        double tugas1 = 0;
        double tugas2 = 0;
        double tugas3 = 0;
        double tugas4 = 0;

        for (NilaiEntry grade : grades) {
            switch (grade.getJenisNilai().toUpperCase()) {
                case "UTS":
                    uts = grade.getNilai();
                    break;
                case "UAS":
                    uas = grade.getNilai();
                    break;
                case "TUGAS 1":
                    tugas1 = grade.getNilai();
                    break;
                case "TUGAS 2":
                    tugas2 = grade.getNilai();
                    break;
                case "TUGAS 3":
                    tugas3 = grade.getNilai();
                    break;
                case "TUGAS 4":
                    tugas4 = grade.getNilai();
                    break;
            }
        }

        // (0.30 * UTS) + (0.30 * UAS) + (0.10 * Tugas 1) + (0.10 * Tugas 2) + (0.10 * Tugas 3) + (0.10 * Tugas 4)
        return (0.30 * uts) + (0.30 * uas) + (0.10 * tugas1) + (0.10 * tugas2) + (0.10 * tugas3) + (0.10 * tugas4);
    }

    private String getGradeLetter(double averageGrade) {
        if (averageGrade >= 90) {
            return "A";
        } else if (averageGrade >= 83) {
            return "B";
        } else if (averageGrade >= 75) {
            return "C";
        } else {//74=>
            return "D";
        }
    }


    private Map<String, Integer> getAttendanceSummary(String studentUserId, int semesterId) throws SQLException {
        Map<String, Integer> summary = new HashMap<>();
        summary.put("Hadir", 0);
        summary.put("Alpha", 0);
        summary.put("Ijin", 0);

        String semesterSql = "SELECT tahun_ajaran, semester, tahun FROM Semester WHERE semester_id = ?";
        LocalDateTime semesterStart = null;
        String semesterName = null;
        String tahunAjaran = null;

        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(semesterSql)) {
            stmt.setInt(1, semesterId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                semesterStart = rs.getTimestamp("tahun").toLocalDateTime();
                semesterName = rs.getString("semester");
                tahunAjaran = rs.getString("tahun_ajaran");
            } else {
                System.err.println("Semester with ID " + semesterId + " not found.");
                return summary;
            }
        }

        if (semesterStart == null) {
            return summary;
        }

        LocalDateTime semesterEnd;
        // Determine semester end date based on semester name and start year
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


        // Override semesterEnd for the example data for testing purposes
        // Since the Absensi data in sqlscriptuser.sql has '2024-07-08 08:00:00'
        // and the semester '2024/2025 - Ganjil' starts '2024-07-01 00:00:00'
        // we need to ensure the semester end date covers July 2024.
        // For '2024/2025 - Ganjil', the end date should be adjusted.
        if ("2024/2025".equals(tahunAjaran) && "Ganjil".equals(semesterName)) {
            semesterEnd = LocalDateTime.of(2024, 12, 31, 23, 59, 59);
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


    private String generateReportCardHtml(Map<String, String> biodata, double overallAverageGrade, String overallLetterGrade, Map<String, Integer> attendance, String className, String semesterName, ObservableList<NilaiEntry> grades) {
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
        html.append("            <p><strong>Student ID:</strong> ").append(biodata.get("user_id")).append("</p>\n");
        html.append("            <p><strong>NIS:</strong> ").append(biodata.get("NIS_NIP")).append("</p>\n");
        html.append("            <p><strong>Class:</strong> ").append(className).append("</p>\n"); // Use the class name
        html.append("            <p><strong>Gender:</strong> ").append(biodata.get("gender")).append("</p>\n");
        html.append("            <p><strong>Email:</strong> ").append(biodata.get("email")).append("</p>\n");
        html.append("            <p><strong>Phone:</strong> ").append(biodata.get("nomer_hp")).append("</p>\n");
        html.append("            <p><strong>Address:</strong> ").append(biodata.get("alamat")).append("</p>\n");
        html.append("        </div>\n");

        html.append("        <div class=\"grades-section\">\n");
        html.append("            <h3>Academic Grades:</h3>\n");

        // Table for individual subject grades, showing average and grade for each
        html.append("            <table>\n");
        html.append("                <thead>\n");
        html.append("                    <tr>\n");
        html.append("                        <th>Subject</th>\n");
        html.append("                        <th>Average Score</th>\n");
        html.append("                        <th>Grade</th>\n");
        html.append("                    </tr>\n");
        html.append("                </thead>\n");
        html.append("                <tbody>\n");

        // Group grades by subject to calculate average for each
        Map<String, Map<String, Double>> subjectGradesMap = new HashMap<>(); // SubjectName -> { "UTS": score, "TUGAS 1": score, ... }

        for (NilaiEntry grade : grades) {
            String subjectName = grade.getNamaMapel(); // Get the subject name
            String jenisNilai = grade.getJenisNilai(); // Get the type of grade (UTS, UAS, Tugas)
            double nilai = grade.getNilai(); // Get the score

            subjectGradesMap.computeIfAbsent(subjectName, k -> new HashMap<>()).put(jenisNilai, nilai);
        }

        // Iterate through each subject to calculate its average and grade
        for (Map.Entry<String, Map<String, Double>> entry : subjectGradesMap.entrySet()) {
            String subject = entry.getKey();
            Map<String, Double> typesAndScores = entry.getValue();

            double uts = typesAndScores.getOrDefault("UTS", 0.0);
            double uas = typesAndScores.getOrDefault("UAS", 0.0);
            double tugas1 = typesAndScores.getOrDefault("TUGAS 1", 0.0);
            double tugas2 = typesAndScores.getOrDefault("TUGAS 2", 0.0);
            double tugas3 = typesAndScores.getOrDefault("TUGAS 3", 0.0);
            double tugas4 = typesAndScores.getOrDefault("TUGAS 4", 0.0);

            // Calculate weighted average for the current subject
            double subjectAverage = (0.30 * uts) + (0.30 * uas) + (0.10 * tugas1) + (0.10 * tugas2) + (0.10 * tugas3) + (0.10 * tugas4);
            String subjectLetterGrade = getGradeLetter(subjectAverage);

            html.append("                    <tr>\n");
            html.append("                        <td>").append(subject).append("</td>\n");
            html.append("                        <td>").append(String.format("%.2f", subjectAverage)).append("</td>\n");
            html.append("                        <td>").append(subjectLetterGrade).append("</td>\n");
            html.append("                    </tr>\n");
        }
        html.append("                </tbody>\n");
        html.append("            </table>\n");
        html.append("        </div>\n");

        // Removed "Overall Academic Summary" section as requested.

        html.append("        <div class=\"attendance-section\">\n");
        html.append("            <h3>Attendance Summary:</h3>\n");
        html.append("            <p><strong>Hadir (Present):</strong> ").append(attendance.getOrDefault("Hadir", 0)).append(" days</p>\n");
        html.append("            <p><strong>Alpha (Absent without leave):</strong> ").append(attendance.getOrDefault("Alpha", 0)).append(" days</p>\n");
        html.append("            <p><strong>Ijin (Excused):</strong> ").append(attendance.getOrDefault("Ijin", 0)).append(" days</p>\n");
        html.append("        </div>\n");

        html.append("        <div class=\"footer\">\n");
        html.append("            <p>Generated on: ").append(java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("</p>\n");
        html.append("            <p>Wali Kelas: ").append(welcomeUserLabel.getText().replace("Welcome, ", "")).append("</p>\n"); // Use welcome label text
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