package org.example.projectbdpbogacor.Siswa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.projectbdpbogacor.Aset.AlertClass;
import org.example.projectbdpbogacor.DBSource.DBS;
import org.example.projectbdpbogacor.HelloApplication;
import org.example.projectbdpbogacor.model.AbsensiEntry;
import org.example.projectbdpbogacor.model.JadwalEntry;
import org.example.projectbdpbogacor.model.MateriEntry;
import org.example.projectbdpbogacor.model.NilaiEntry;
import org.example.projectbdpbogacor.model.TugasEntry;
import org.example.projectbdpbogacor.model.UjianEntry;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class SiswadsController {

    @FXML
    private Label welcomeUserLabel;
    @FXML
    private TabPane siswaTabPane;

    // Biodata
    @FXML
    private Label userIdLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label nisNipLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label genderLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label phoneLabel;

    // Jadwal Kelas Table
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
    @FXML
    private TableColumn<JadwalEntry, String> namaPengajarJadwalColumn; // For teacher's name

    // Nilai Ujian Table
    @FXML
    private TableView<NilaiEntry> nilaiUjianTable;
    @FXML
    private TableColumn<NilaiEntry, String> jenisNilaiColumn;
    @FXML
    private TableColumn<NilaiEntry, String> namaMapelNilaiColumn;
    @FXML
    private TableColumn<NilaiEntry, Integer> nilaiColumn;

    // Tugas Table
    @FXML
    private TableView<TugasEntry> tugasTable;
    @FXML
    private TableColumn<TugasEntry, String> keteranganTugasColumn;
    @FXML
    private TableColumn<TugasEntry, String> deadlineTugasColumn;
    @FXML
    private TableColumn<TugasEntry, String> tanggalRilisTugasColumn;
    @FXML
    private TableColumn<TugasEntry, String> mapelTugasColumn;
    @FXML
    private TableColumn<TugasEntry, String> kelasTugasColumn;

    // Materi Table
    @FXML
    private TableView<MateriEntry> materiTable;
    @FXML
    private TableColumn<MateriEntry, String> namaMateriColumn;
    @FXML
    private TableColumn<MateriEntry, String> mapelMateriColumn;
    @FXML
    private TableColumn<MateriEntry, String> kelasMateriColumn;

    // Ujian Table
    @FXML
    private TableView<UjianEntry> ujianTable;
    @FXML
    private TableColumn<UjianEntry, String> jenisUjianColumn;
    @FXML
    private TableColumn<UjianEntry, String> tanggalUjianColumn;
    @FXML
    private TableColumn<UjianEntry, String> mapelUjianColumn;
    @FXML
    private TableColumn<UjianEntry, String> kelasUjianColumn;

    // Absensi Table
    @FXML
    private TableView<AbsensiEntry> absensiTable;
    @FXML
    private TableColumn<AbsensiEntry, String> tanggalAbsensiColumn;
    @FXML
    private TableColumn<AbsensiEntry, String> statusAbsensiColumn;
    @FXML
    private TableColumn<AbsensiEntry, String> mapelAbsensiColumn;
    @FXML
    private TableColumn<AbsensiEntry, String> kelasAbsensiColumn;
    @FXML
    private TableColumn<AbsensiEntry, String> jamMulaiAbsensiColumn;
    @FXML
    private TableColumn<AbsensiEntry, String> jamSelesaiAbsensiColumn;


    // Feedback
    @FXML
    private TextArea feedbackTextArea;

    // Announcements
    @FXML
    private TextArea announcementDisplayArea;

    private String loggedInUserId;

    @FXML
    void initialize() {
        loggedInUserId = HelloApplication.getInstance().getLoggedInUserId();
        loadStudentName();

        // Initialize TableViews
        initJadwalKelasTable();
        initNilaiUjianTable();
        initTugasTable();
        initMateriTable();
        initUjianTable();
        initAbsensiTable();

        // Load data when tabs are selected
        siswaTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null) {
                switch (newTab.getText()) {
                    case "Biodata":
                        loadBiodata();
                        break;
                    case "Jadwal Kelas":
                        loadJadwalKelas();
                        break;
                    case "Nilai Ujian":
                        loadNilaiUjian();
                        break;
                    case "Tugas":
                        loadTugas();
                        break;
                    case "Materi":
                        loadMateri();
                        break;
                    case "Ujian":
                        loadUjian();
                        break;
                    case "Absensi":
                        loadAbsensi();
                        break;
                    case "Pengumuman": // Add this case
                        loadAnnouncements();
                        break;
                }
            }
        });

        // Load biodata initially
        loadBiodata();
    }

    private void loadStudentName() {
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

    private void loadBiodata() {
        String sql = "SELECT user_id, username, NIS_NIP, nama, gender, alamat, email, nomer_hp FROM Users WHERE user_id = ?";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, loggedInUserId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                userIdLabel.setText(rs.getString("user_id"));
                usernameLabel.setText(rs.getString("username"));
                nisNipLabel.setText(rs.getString("NIS_NIP"));
                nameLabel.setText(rs.getString("nama"));
                genderLabel.setText(rs.getString("gender").equals("L") ? "Laki-laki" : "Perempuan");
                addressLabel.setText(rs.getString("alamat"));
                emailLabel.setText(rs.getString("email"));
                phoneLabel.setText(rs.getString("nomer_hp"));
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load biodata", e.getMessage());
            e.printStackTrace();
        }
    }

    private void initJadwalKelasTable() {
        hariColumn.setCellValueFactory(new PropertyValueFactory<>("hari"));
        jamMulaiColumn.setCellValueFactory(new PropertyValueFactory<>("jamMulai"));
        jamSelesaiColumn.setCellValueFactory(new PropertyValueFactory<>("jamSelesai"));
        namaMapelColumn.setCellValueFactory(new PropertyValueFactory<>("namaMapel"));
        namaKelasJadwalColumn.setCellValueFactory(new PropertyValueFactory<>("namaKelas"));
        namaPengajarJadwalColumn.setCellValueFactory(new PropertyValueFactory<>("namaPengajar"));
    }

    private void loadJadwalKelas() {
        ObservableList<JadwalEntry> jadwalList = FXCollections.observableArrayList();
        // Assuming there's a Student_Class_Enrollment table to link students to classes
        // Or that a student's class can be determined by their user_id in the Kelas table (less likely for students)
        // For simplicity, let's assume a student is linked to a class via a hypothetical enrollment table
        // For now, I'll provide a generic query that assumes student's class_id is known or can be found.
        // A more robust solution would involve a join with a dedicated student-class enrollment table.

        // SQL query to get the class_id(s) for the loggedInUserId (Siswa)
        // THIS IS A CRITICAL ASSUMPTION: You need a way to link students to their classes.
        // If your database has a 'Student_Class_Enrollment' table, use it.
        // For example:
        // SELECT ske.Kelas_Users_user_id, ske.Kelas_kelas_id FROM Student_Class_Enrollment ske WHERE ske.Users_user_id = ?
        // If there's no such table, you might need to add it or define how students are assigned to classes.
        // For the example, I'm assuming a direct link or that all classes are shown (which is not ideal).
        // I will use a simplified query that might need adjustment based on your actual student-class assignment logic.

        String sql = "SELECT j.hari, j.jam_mulai, j.jam_selsai, m.nama_mapel, k.nama_kelas, u.nama AS nama_pengajar " +
                "FROM Jadwal j " +
                "JOIN Matpel m ON j.Matpel_mapel_id = m.mapel_id " +
                "JOIN Kelas k ON j.Kelas_Users_user_id = k.Users_user_id AND j.Kelas_kelas_id = k.kelas_id " +
                "JOIN Users u ON k.Users_user_id = u.user_id " + // Joining with Users to get teacher's name
                "WHERE EXISTS (SELECT 1 FROM Student_Class_Enrollment sce WHERE sce.Users_user_id = ? AND sce.Kelas_kelas_id = k.kelas_id AND sce.Kelas_Users_user_id = k.Users_user_id)";

        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, loggedInUserId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                jadwalList.add(new JadwalEntry(
                        rs.getString("hari"),
                        rs.getString("jam_mulai"),
                        rs.getString("jam_selsai"),
                        rs.getString("nama_mapel"),
                        rs.getString("nama_kelas"),
                        rs.getString("nama_pengajar")
                ));
            }
            jadwalKelasTable.setItems(jadwalList);
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load class schedule", e.getMessage());
            e.printStackTrace();
        }
    }


    private void initNilaiUjianTable() {
        jenisNilaiColumn.setCellValueFactory(new PropertyValueFactory<>("jenisNilai"));
        namaMapelNilaiColumn.setCellValueFactory(new PropertyValueFactory<>("namaMapel"));
        nilaiColumn.setCellValueFactory(new PropertyValueFactory<>("nilai"));
    }

    private void loadNilaiUjian() {
        ObservableList<NilaiEntry> nilaiList = FXCollections.observableArrayList();
        String sql = "SELECT n.jenis_nilai, m.nama_mapel, n.nilai " +
                "FROM Nilai n " +
                "JOIN Matpel m ON n.Matpel_mapel_id = m.mapel_id " +
                "JOIN Rapor r ON n.Rapor_rapor_id = r.rapor_id " +
                "WHERE r.Users_user_id = ?";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, loggedInUserId);
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

    private void initTugasTable() {
        keteranganTugasColumn.setCellValueFactory(new PropertyValueFactory<>("keterangan"));
        deadlineTugasColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        tanggalRilisTugasColumn.setCellValueFactory(new PropertyValueFactory<>("tanggalDirelease"));
        mapelTugasColumn.setCellValueFactory(new PropertyValueFactory<>("namaMapel"));
        kelasTugasColumn.setCellValueFactory(new PropertyValueFactory<>("namaKelas"));
    }

    private void loadTugas() {
        ObservableList<TugasEntry> tugasList = FXCollections.observableArrayList();
        String sql = "SELECT t.keterangan, t.deadline, t.tanggal_direlease, m.nama_mapel, k.nama_kelas " +
                "FROM Tugas t " +
                "JOIN Matpel m ON t.Matpel_mapel_id = m.mapel_id " +
                "JOIN Kelas k ON t.Kelas_Users_user_id = k.Users_user_id AND t.Kelas_kelas_id = k.kelas_id " +
                "WHERE EXISTS (SELECT 1 FROM Student_Class_Enrollment sce WHERE sce.Users_user_id = ? AND sce.Kelas_kelas_id = k.kelas_id AND sce.Kelas_Users_user_id = k.Users_user_id)";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, loggedInUserId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tugasList.add(new TugasEntry(
                        rs.getString("keterangan"),
                        rs.getTimestamp("deadline").toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        rs.getTimestamp("tanggal_direlease").toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        rs.getString("nama_mapel"),
                        rs.getString("nama_kelas")
                ));
            }
            tugasTable.setItems(tugasList);
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load assignments", e.getMessage());
            e.printStackTrace();
        }
    }

    private void initMateriTable() {
        namaMateriColumn.setCellValueFactory(new PropertyValueFactory<>("namaMateri"));
        mapelMateriColumn.setCellValueFactory(new PropertyValueFactory<>("namaMapel"));
        kelasMateriColumn.setCellValueFactory(new PropertyValueFactory<>("namaKelas"));
    }

    private void loadMateri() {
        ObservableList<MateriEntry> materiList = FXCollections.observableArrayList();
        String sql = "SELECT mt.nama_materi, m.nama_mapel, k.nama_kelas " +
                "FROM Materi mt " +
                "JOIN Matpel m ON mt.Matpel_mapel_id = m.mapel_id " +
                "JOIN Kelas k ON mt.Kelas_Users_user_id = k.Users_user_id AND mt.Kelas_kelas_id = k.kelas_id " +
                "WHERE EXISTS (SELECT 1 FROM Student_Class_Enrollment sce WHERE sce.Users_user_id = ? AND sce.Kelas_kelas_id = k.kelas_id AND sce.Kelas_Users_user_id = k.Users_user_id)";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, loggedInUserId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                materiList.add(new MateriEntry(
                        rs.getString("nama_materi"),
                        rs.getString("nama_mapel"),
                        rs.getString("nama_kelas")
                ));
            }
            materiTable.setItems(materiList);
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load materials", e.getMessage());
            e.printStackTrace();
        }
    }

    private void initUjianTable() {
        jenisUjianColumn.setCellValueFactory(new PropertyValueFactory<>("jenisUjian"));
        tanggalUjianColumn.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        mapelUjianColumn.setCellValueFactory(new PropertyValueFactory<>("namaMapel"));
        kelasUjianColumn.setCellValueFactory(new PropertyValueFactory<>("namaKelas"));
    }

    private void loadUjian() {
        ObservableList<UjianEntry> ujianList = FXCollections.observableArrayList();
        String sql = "SELECT u.jenis_ujian, u.tanggal, m.nama_mapel, k.nama_kelas " +
                "FROM Ujian u " +
                "JOIN Matpel m ON u.Matpel_mapel_id = m.mapel_id " +
                "JOIN Kelas k ON u.Kelas_Users_user_id = k.Users_user_id AND u.Kelas_kelas_id = k.kelas_id " +
                "WHERE EXISTS (SELECT 1 FROM Student_Class_Enrollment sce WHERE sce.Users_user_id = ? AND sce.Kelas_kelas_id = k.kelas_id AND sce.Kelas_Users_user_id = k.Users_user_id)";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, loggedInUserId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ujianList.add(new UjianEntry(
                        rs.getString("jenis_ujian"),
                        rs.getTimestamp("tanggal").toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        rs.getString("nama_mapel"),
                        rs.getString("nama_kelas")
                ));
            }
            ujianTable.setItems(ujianList);
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load exams", e.getMessage());
            e.printStackTrace();
        }
    }

    private void initAbsensiTable() {
        tanggalAbsensiColumn.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        statusAbsensiColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        mapelAbsensiColumn.setCellValueFactory(new PropertyValueFactory<>("namaMapel"));
        kelasAbsensiColumn.setCellValueFactory(new PropertyValueFactory<>("namaKelas"));
        jamMulaiAbsensiColumn.setCellValueFactory(new PropertyValueFactory<>("jamMulai"));
        jamSelesaiAbsensiColumn.setCellValueFactory(new PropertyValueFactory<>("jamSelesai"));
    }

    private void loadAbsensi() {
        ObservableList<AbsensiEntry> absensiList = FXCollections.observableArrayList();
        String sql = "SELECT a.tanggal, a.status, m.nama_mapel, k.nama_kelas, j.jam_mulai, j.jam_selsai " +
                "FROM Absensi a " +
                "JOIN Users u ON a.Users_user_id = u.user_id " +
                "JOIN Jadwal j ON a.Jadwal_jadwal_id = j.jadwal_id " +
                "JOIN Matpel m ON j.Matpel_mapel_id = m.mapel_id " +
                "JOIN Kelas k ON j.Kelas_Users_user_id = k.Users_user_id AND j.Kelas_kelas_id = k.kelas_id " +
                "WHERE u.user_id = ?";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, loggedInUserId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                absensiList.add(new AbsensiEntry(
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
            AlertClass.ErrorAlert("Database Error", "Failed to load attendance", e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    void handleSubmitFeedback() {
        String feedbackContent = feedbackTextArea.getText();
        if (feedbackContent.isEmpty()) {
            AlertClass.WarningAlert("Input Error", "Feedback Empty", "Please enter your feedback.");
            return;
        }

        String sql = "INSERT INTO Feedback (feedback, Users_user_id) VALUES (?, ?)";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, feedbackContent);
            stmt.setString(2, loggedInUserId);
            stmt.executeUpdate();
            AlertClass.InformationAlert("Success", "Feedback Submitted", "Your feedback has been successfully submitted.");
            feedbackTextArea.clear();
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to submit feedback", "An error occurred while submitting feedback: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- Announcements Methods ---
    private void loadAnnouncements() {
        StringBuilder announcements = new StringBuilder();
        String sql = "SELECT pengumuman, waktu FROM Pengumuman ORDER BY waktu DESC"; // Order by time to show newest first
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (!rs.isBeforeFirst()) { // Check if there are any rows
                announcements.append("Tidak ada pengumuman saat ini.");
            } else {
                while (rs.next()) {
                    // Format the timestamp to a more readable string
                    String waktu = rs.getTimestamp("waktu").toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    announcements.append("Tanggal: ").append(waktu).append("\n");
                    announcements.append("Pengumuman: ").append(rs.getString("pengumuman")).append("\n");
                    announcements.append("------------------------------------\n");
                }
            }
            announcementDisplayArea.setText(announcements.toString());
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load announcements", e.getMessage());
            e.printStackTrace();
            announcementDisplayArea.setText("Gagal memuat pengumuman.");
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