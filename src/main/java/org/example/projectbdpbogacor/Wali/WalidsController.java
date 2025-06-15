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
import org.example.projectbdpbogacor.Service.*;
import org.example.projectbdpbogacor.Tabel.AbsensiWaliEntry; // Use the specific model for Wali
import org.example.projectbdpbogacor.Tabel.NilaiEntry;
import org.example.projectbdpbogacor.Tabel.RaporEntry; // Import RaporEntry

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.awt.Desktop; // Required for opening file
import java.time.LocalDateTime; // For accurate time handling


public class WalidsController {

    @FXML
    private Label welcomeUserLabel;
    @FXML
    private TabPane waliTabPane;

    // Student Attendance
    @FXML
    private Label attendanceClassNameLabel;
    @FXML
    private ChoiceBox<String> attendanceStudentChoiceBox;
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
    private Label raporClassNameLabel;
    @FXML
    private ChoiceBox<String> raporStudentChoiceBox;
    @FXML
    private ChoiceBox<String> raporSemesterChoiceBox;
    @FXML
    private TableView<NilaiEntry> nilaiUjianTable;
    @FXML
    private TableColumn<NilaiEntry, String> jenisNilaiColumn;
    @FXML
    private TableColumn<NilaiEntry, String> namaMapelNilaiColumn;
    @FXML
    private TableColumn<NilaiEntry, Integer> nilaiColumn;

    // NEW: Student List Tab
    @FXML
    private Label studentListClassNameLabel; // NEW FXML element for class name in student list
    @FXML
    private TableView<Users> studentListTable; // UBAH: Menggunakan Service.Users
    @FXML
    private TableColumn<Users, String> studentListNameColumn; // UBAH: Tipe kolom mengikuti Users
    @FXML
    private TableColumn<Users, String> studentListGenderColumn; // UBAH
    @FXML
    private TableColumn<Users, String> studentListNISNIPColumn; // UBAH
    @FXML
    private TableColumn<Users, String> studentListAddressColumn; // UBAH
    @FXML
    private TableColumn<Users, String> studentListPhoneNumberColumn; // UBAH
    @FXML
    private TableColumn<Users, String> studentListEmailColumn; // UBAH

    private String loggedInUserId;
    private String waliKelasId;
    private int kelasId;
    private String kelasName;

    // Maps to store IDs corresponding to displayed names in ChoiceBoxes
    private Map<String, String> studentsMap = new HashMap<>();
    private Map<String, Integer> semestersMap = new HashMap<>();

    @FXML
    void initialize() {
        loggedInUserId = HelloApplication.getInstance().getLoggedInUserId();
        loadWaliKelasNameAndClassInfo();

        initAbsensiTable();
        initNilaiUjianTable();
        initStudentListTable(); // Panggil inisialisasi tabel siswa

        loadSemesters(); // Muat data semester

        attendanceStudentChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> loadAbsensiData());
        raporStudentChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> loadNilaiData());
        raporSemesterChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> loadNilaiData());

        waliTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null) {
                if (newTab.getText().equals("Student Attendance")) {
                    loadStudentsInClass(attendanceStudentChoiceBox);
                    loadAbsensiData();
                } else if (newTab.getText().equals("Print Report Card")) {
                    loadStudentsInClass(raporStudentChoiceBox);
                    loadSemesters();
                    if (!raporSemesterChoiceBox.getItems().isEmpty()) {
                        raporSemesterChoiceBox.setValue(raporSemesterChoiceBox.getItems().get(0));
                    }
                    loadNilaiData();
                } else if (newTab.getText().equals("Student List")) {
                    loadStudentsInMyClass(); // Panggil method untuk memuat daftar siswa
                }
            }
        });

        // Load initial data for the currently selected tab
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
        } else if (waliTabPane.getSelectionModel().getSelectedItem().getText().equals("Student List")) {
            loadStudentsInMyClass();
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
                welcomeUserLabel.setText("Welcome, " + rs.getString("nama") + "!");
                this.waliKelasId = loggedInUserId;
                this.kelasId = rs.getInt("kelas_id");
                this.kelasName = rs.getString("nama_kelas");

                attendanceClassNameLabel.setText("Kelas: " + kelasName);
                raporClassNameLabel.setText("Kelas: " + kelasName);
                studentListClassNameLabel.setText("Kelas: " + kelasName); // Set class name for student list tab

            } else {
                AlertClass.WarningAlert("Class Not Found", "Wali Kelas has no assigned class", "Please ensure this Wali Kelas is assigned to a class.");
                welcomeUserLabel.setText("Welcome, Wali Kelas!");
                attendanceClassNameLabel.setText("Kelas: Not Assigned");
                raporClassNameLabel.setText("Kelas: Not Assigned");
                studentListClassNameLabel.setText("Kelas: Not Assigned"); // Set default if no class found
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load user name or class info", e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadStudentsInClass(ChoiceBox<String> studentChoiceBoxToPopulate) {
        studentChoiceBoxToPopulate.getItems().clear();
        studentsMap.clear();

        if (this.kelasId == 0) {
            return;
        }

        String sql = "SELECT u.user_id, u.nama, u.NIS_NIP FROM Users u " +
                "JOIN student_class_enrollment sce ON u.user_id = sce.Users_user_id " + // UBAH: Enrollment menjadi student_class_enrollment
                "WHERE sce.Kelas_kelas_id = ? AND sce.Kelas_Users_user_id = ?";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, this.kelasId);
            stmt.setString(2, this.waliKelasId);
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
                studentChoiceBoxToPopulate.setValue(studentChoiceBoxToPopulate.getItems().get(0));
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load students", e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadSemesters() {
        semestersMap.clear();
        raporSemesterChoiceBox.getItems().clear();

        String sql = "SELECT semester_id, tahun_ajaran, semester, tahun FROM Semester"; // Tambahkan kolom tahun
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int semesterId = rs.getInt("semester_id");
                String tahunAjaran = rs.getString("tahun_ajaran");
                String semesterName = rs.getString("semester");
                LocalDateTime tahun = rs.getTimestamp("tahun").toLocalDateTime(); // Ambil data tahun sebagai LocalDateTime

                // GUNAKAN SERVICE.SEMESTER UNTUK MENGISI CHOICEBOX
                Semester semesterObj = new Semester(semesterId, tahunAjaran, semesterName, tahun); // Inisialisasi objek Semester
                String display = semesterObj.getTahunAjaran() + " - " + semesterObj.getNamaSemester(); // Gunakan getter dari objek Semester
                semestersMap.put(display, semesterObj.getSemesterId()); // Gunakan getter dari objek Semester
                raporSemesterChoiceBox.getItems().add(display);
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load semesters", e.getMessage());
            e.printStackTrace();
        }
    }

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
    void loadAbsensiData() {
        ObservableList<AbsensiWaliEntry> absensiList = FXCollections.observableArrayList();
        String selectedStudentDisplay = attendanceStudentChoiceBox.getValue();

        if (this.kelasId == 0 || selectedStudentDisplay == null) {
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
                "JOIN student_class_enrollment sce ON u.user_id = sce.Users_user_id AND k.kelas_id = sce.Kelas_kelas_id AND k.Users_user_id = sce.Kelas_Users_user_id " + // UBAH: Enrollment menjadi student_class_enrollment
                "WHERE k.kelas_id = ? AND k.Users_user_id = ? AND u.user_id = ?";

        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, this.kelasId);
            stmt.setString(2, this.waliKelasId);
            stmt.setString(3, studentId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Users user = new Users(rs.getString("student_name"));
                Absensi absensi = new Absensi(
                        rs.getTimestamp("tanggal").toLocalDateTime(),
                        rs.getString("status"));
                Matpel matpel = new Matpel(rs.getString("nama_mapel"));
                Kelas kelas = new Kelas(rs.getString("nama_kelas"));
                Jadwal jadwal = new Jadwal(
                        rs.getString("jam_mulai"),
                        rs.getString("jam_selsai"));
                absensiList.add(new AbsensiWaliEntry(
                        user.getNama(),
                        absensi.getTanggal().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        absensi.getStatus(),
                        matpel.getNamaMapel(),
                        kelas.getNamaKelas(),
                        jadwal.getJamMulai(),
                        jadwal.getJamSelesai()
                ));
            }
            absensiTable.setItems(absensiList);
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load attendance data", e.getMessage());
            e.printStackTrace();
        }
    }

    private void initNilaiUjianTable() {
        jenisNilaiColumn.setCellValueFactory(new PropertyValueFactory<>("jenisNilai"));
        namaMapelNilaiColumn.setCellValueFactory(new PropertyValueFactory<>("namaMapel"));
        nilaiColumn.setCellValueFactory(new PropertyValueFactory<>("nilai"));
    }

    @FXML
    void loadNilaiData() {
        ObservableList<NilaiEntry> nilaiList = FXCollections.observableArrayList();
        String selectedStudentDisplay = raporStudentChoiceBox.getValue();
        String selectedSemesterDisplay = raporSemesterChoiceBox.getValue();

        if (selectedStudentDisplay == null || selectedSemesterDisplay == null || this.kelasId == 0) {
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
                Nilai nilai = new Nilai(
                        rs.getInt("nilai"),
                        rs.getString("jenis_nilai"));
                Matpel matpel = new Matpel(rs.getString("nama_mapel"));
                nilaiList.add(new NilaiEntry(
                        nilai.getJenisNilai(),
                        matpel.getNamaMapel(),
                        nilai.getNilaiAngka()
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

        if (selectedStudentDisplay == null || selectedSemesterDisplay == null || this.kelasId == 0) {
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
            Map<String, String> studentBiodata = getStudentBiodata(studentUserId);
            String className = this.kelasName;
            String homeroomTeacherName = welcomeUserLabel.getText().replace("Welcome, ", "").replace("!", ""); // Extract name from label

            ObservableList<NilaiEntry> grades = nilaiUjianTable.getItems();
            Map<String, Double> subjectAverageGrades = calculateSubjectAverageGrades(grades); // Calculate average per subject

            Map<String, Integer> attendanceSummary = getAttendanceSummary(studentUserId, semesterId);

            // Extract semester name and academic year from selectedSemesterDisplay
            String semesterName = selectedSemesterDisplay.split(" - ")[1];
            String academicYear = selectedSemesterDisplay.split(" - ")[0];

            // Create RaporEntry object using the constructor
            RaporEntry rapor = new RaporEntry(
                    studentBiodata.get("user_id"),
                    studentBiodata.get("nama"),
                    studentBiodata.get("NIS_NIP"),
                    className,
                    studentBiodata.get("gender"),
                    studentBiodata.get("alamat"),
                    studentBiodata.get("email"),
                    studentBiodata.get("nomer_hp"),
                    homeroomTeacherName,
                    semesterName,
                    academicYear,
                    subjectAverageGrades, // Pass the calculated subject average grades
                    attendanceSummary.getOrDefault("Hadir", 0),
                    attendanceSummary.getOrDefault("Sakit", 0),
                    attendanceSummary.getOrDefault("Ijin", 0),
                    attendanceSummary.getOrDefault("Alpha", 0)
            );

            // Generate HTML content using the RaporEntry object
            String htmlContent = generateReportCardHtml(rapor);

            File raporDir = new File("Rapor");
            if (!raporDir.exists()) {
                raporDir.mkdirs();
            }

            File outputFile = new File(raporDir, "rapor_" + studentUserId + "_" + semesterId + ".html");
            try (FileWriter writer = new FileWriter(outputFile)) {
                writer.write(htmlContent);
            }

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
                // Data ini tidak ditampilkan di TableView, jadi langsung diolah string-nya atau dimasukkan ke Map
                biodata.put("gender", rs.getString("gender").equals("L") ? "Laki-laki" : "Perempuan");
                biodata.put("alamat", rs.getString("alamat"));
                biodata.put("email", rs.getString("email"));
                biodata.put("nomer_hp", rs.getString("nomer_hp"));
            }
        }
        return biodata;
    }

    private Map<String, Double> calculateSubjectAverageGrades(ObservableList<NilaiEntry> grades) {
        Map<String, Map<String, Double>> subjectGradesMap = new HashMap<>();

        for (NilaiEntry grade : grades) {
            String subjectName = grade.getNamaMapel();
            String jenisNilai = grade.getJenisNilai();
            double nilai = grade.getNilai();

            subjectGradesMap.computeIfAbsent(subjectName, k -> new HashMap<>()).put(jenisNilai, nilai);
        }

        Map<String, Double> subjectAverageGrades = new HashMap<>();
        for (Map.Entry<String, Map<String, Double>> entry : subjectGradesMap.entrySet()) {
            String subject = entry.getKey();
            Map<String, Double> typesAndScores = entry.getValue();

            double uts = typesAndScores.getOrDefault("UTS", 0.0);
            double uas = typesAndScores.getOrDefault("UAS", 0.0);
            double tugas1 = typesAndScores.getOrDefault("TUGAS 1", 0.0);
            double tugas2 = typesAndScores.getOrDefault("TUGAS 2", 0.0);
            double tugas3 = typesAndScores.getOrDefault("TUGAS 3", 0.0);
            double tugas4 = typesAndScores.getOrDefault("TUGAS 4", 0.0);

            // Calculate weighted average
            double subjectAverage = (0.30 * uts) + (0.30 * uas) + (0.10 * tugas1) + (0.10 * tugas2) + (0.10 * tugas3) + (0.10 * tugas4);
            subjectAverageGrades.put(subject, subjectAverage);
        }
        return subjectAverageGrades;
    }

    private String getGradeLetter(double averageGrade) {
        if (averageGrade >= 90) {
            return "A";
        } else if (averageGrade >= 83) {
            return "B";
        } else if (averageGrade >= 75) {
            return "C";
        } else {
            return "D";
        }
    }

    private Map<String, Integer> getAttendanceSummary(String studentUserId, int semesterId) throws SQLException {
        Map<String, Integer> summary = new HashMap<>();
        summary.put("Hadir", 0);
        summary.put("Alpha", 0);
        summary.put("Ijin", 0);
        summary.put("Sakit", 0); // Include Sakit (Sick)

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
        if ("Ganjil".equalsIgnoreCase(semesterName)) {
            semesterEnd = LocalDateTime.of(semesterStart.getYear(), 12, 31, 23, 59, 59);
        } else if ("Genap".equalsIgnoreCase(semesterName)) {
            int endYear = semesterStart.getYear();
            if (tahunAjaran != null && tahunAjaran.contains("/")) {
                try {
                    String[] years = tahunAjaran.split("/");
                    if (years.length == 2) {
                        endYear = Integer.parseInt(years[1]);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing tahun_ajaran: " + e.getMessage());
                }
            }
            semesterEnd = LocalDateTime.of(endYear, 6, 30, 23, 59, 59);
        } else {
            semesterEnd = semesterStart.plusMonths(6).minusDays(1).withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        }

        // Override semesterEnd for the example data for testing purposes
        // This block might be for testing purposes and can be removed in production
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


    private String generateReportCardHtml(RaporEntry rapor) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"en\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("    <title>Report Card - ").append(rapor.getStudentName()).append("</title>\n");
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
        html.append("        <h2>").append(rapor.getAcademicYear()).append(" - ").append(rapor.getSemester()).append("</h2>\n");

        html.append("        <div class=\"info-section\">\n");
        html.append("            <h3>Student Information:</h3>\n");
        html.append("            <p><strong>Name:</strong> ").append(rapor.getStudentName()).append("</p>\n");
        html.append("            <p><strong>Student ID:</strong> ").append(rapor.getStudentId()).append("</p>\n");
        html.append("            <p><strong>NIS:</strong> ").append(rapor.getStudentNIS()).append("</p>\n");
        html.append("            <p><strong>Class:</strong> ").append(rapor.getStudentClass()).append("</p>\n");
        html.append("            <p><strong>Gender:</strong> ").append(rapor.getStudentGender()).append("</p>\n");
        html.append("            <p><strong>Email:</strong> ").append(rapor.getStudentEmail()).append("</p>\n");
        html.append("            <p><strong>Phone:</strong> ").append(rapor.getStudentPhone()).append("</p>\n");
        html.append("            <p><strong>Address:</strong> ").append(rapor.getStudentAddress()).append("</p>\n");
        html.append("        </div>\n");

        html.append("        <div class=\"grades-section\">\n");
        html.append("            <h3>Academic Grades:</h3>\n");

        html.append("            <table>\n");
        html.append("                <thead>\n");
        html.append("                    <tr>\n");
        html.append("                        <th>Subject</th>\n");
        html.append("                        <th>Average Score</th>\n");
        html.append("                        <th>Grade</th>\n");
        html.append("                    </tr>\n");
        html.append("                </thead>\n");
        html.append("                <tbody>\n");

        for (Map.Entry<String, Double> entry : rapor.getSubjectAverageGrades().entrySet()) {
            String subject = entry.getKey();
            Double averageScore = entry.getValue();
            String subjectLetterGrade = getGradeLetter(averageScore);

            html.append("                    <tr>\n");
            html.append("                        <td>").append(subject).append("</td>\n");
            html.append("                        <td>").append(String.format("%.2f", averageScore)).append("</td>\n");
            html.append("                        <td>").append(subjectLetterGrade).append("</td>\n");
            html.append("                    </tr>\n");
        }
        html.append("                </tbody>\n");
        html.append("            </table>\n");
        html.append("        </div>\n");

        html.append("        <div class=\"attendance-section\">\n");
        html.append("            <h3>Attendance Summary:</h3>\n");
        html.append("            <p><strong>Hadir (Present):</strong> ").append(rapor.getTotalPresent()).append(" days</p>\n");
        html.append("            <p><strong>Alpha (Absent without leave):</strong> ").append(rapor.getTotalAbsent()).append(" days</p>\n");
        html.append("            <p><strong>Ijin (Excused):</strong> ").append(rapor.getTotalPermission()).append(" days</p>\n");
        html.append("            <p><strong>Sakit (Sick):</strong> ").append(rapor.getTotalSick()).append(" days</p>\n");
        html.append("        </div>\n");

        html.append("        <div class=\"footer\">\n");
        html.append("            <p>Generated on: ").append(java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("</p>\n");
        html.append("            <p>Wali Kelas: ").append(rapor.getHomeroomTeacherName()).append("</p>\n");
        html.append("        </div>\n");

        html.append("    </div>\n");
        html.append("</body>\n");
        html.append("</html>");

        return html.toString();
    }

    // NEW: Student List Methods
    private void initStudentListTable() {
        // UBAH: PropertyValueFactory menggunakan nama properti dari Service.Users
        studentListNameColumn.setCellValueFactory(new PropertyValueFactory<>("nama"));
        studentListGenderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        studentListNISNIPColumn.setCellValueFactory(new PropertyValueFactory<>("nisNip"));
        studentListAddressColumn.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        studentListPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("nomerHp"));
        studentListEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void loadStudentsInMyClass() {
        // UBAH: ObservableList menggunakan Service.Users
        ObservableList<Users> studentList = FXCollections.observableArrayList();

        if (this.kelasId == 0) {
            studentListTable.setItems(FXCollections.emptyObservableList());
            return;
        }

        String sql = "SELECT u.user_id, u.username, u.NIS_NIP, u.nama, u.gender, u.alamat, u.email, u.nomer_hp, r.role_name " +
                "FROM Users u " +
                "JOIN student_class_enrollment sce ON u.user_id = sce.Users_user_id " + // UBAH: Enrollment menjadi student_class_enrollment
                "JOIN Role r ON u.Role_role_id = r.role_id " +
                "WHERE sce.Kelas_kelas_id = ? AND sce.Kelas_Users_user_id = ? AND u.Role_role_id = 'S'";

        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, this.kelasId);
            stmt.setString(2, this.waliKelasId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // UBAH: Membuat objek Service.Users
                studentList.add(new Users(
                        rs.getString("user_id"),
                        rs.getString("username"),
                        rs.getString("NIS_NIP"),
                        rs.getString("nama"),
                        rs.getString("gender").equals("L") ? "Laki-laki" : "Perempuan", // Sesuaikan agar sesuai dengan string display
                        rs.getString("alamat"),
                        rs.getString("email"),
                        rs.getString("nomer_hp"),
                        rs.getString("role_name")
                ));
            }
            studentListTable.setItems(studentList);
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load student list for class", e.getMessage());
            e.printStackTrace();
            studentListTable.setItems(FXCollections.emptyObservableList());
        }
    }


    @FXML
    void handleLogout() {
        try {
            HelloApplication.getInstance().changeScene("login-view.fxml", "Login Aplikasi", 1300, 700);
        } catch (IOException e) {
            AlertClass.ErrorAlert("Error", "Logout Failed", "Could not return to login screen.");
            e.printStackTrace();
        }
    }
}