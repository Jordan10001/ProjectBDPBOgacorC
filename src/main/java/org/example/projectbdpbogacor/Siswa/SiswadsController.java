// ProjectBDPBOgacor/src/main/java/org/example/projectbdpbogacor/Siswa/SiswadsController.java
package org.example.projectbdpbogacor.Siswa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.projectbdpbogacor.Aset.AlertClass;
import org.example.projectbdpbogacor.DBSource.DBS;
import org.example.projectbdpbogacor.HelloApplication;
import org.example.projectbdpbogacor.Service.*;
import org.example.projectbdpbogacor.Tabel.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

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
    private TableColumn<JadwalEntry, String> namaPengajarJadwalColumn;

    // Nilai Ujian Table
    @FXML
    private TableView<NilaiEntry> nilaiUjianTable;
    @FXML
    private TableColumn<NilaiEntry, String> jenisNilaiColumn;
    @FXML
    private TableColumn<NilaiEntry, String> namaMapelNilaiColumn;
    @FXML
    private TableColumn<NilaiEntry, Integer> existingNilaiColumn;

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


    // Announcements
    @FXML
    private TableView<Pengumuman> announcementTable;
    @FXML
    private TableColumn<Pengumuman, String> announcementWaktuColumn;
    @FXML
    private TableColumn<Pengumuman, String> announcementContentColumn;

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
        initAnnouncementTable();

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
                    case "Pengumuman":
                        loadAnnouncements();
                        break;
                }
            }
        });

        // Load biodata initially
        loadBiodata();
        // Load announcements initially if the announcement tab is the default or should be loaded on start
        loadAnnouncements();
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
                Users biodata = new Users(
                        rs.getString("user_id"),
                        rs.getString("username"),
                        rs.getString("NIS_NIP"),
                        rs.getString("nama"),
                        rs.getString("gender"),
                        rs.getString("alamat"),
                        rs.getString("email"),
                        rs.getString("nomer_hp"));

                userIdLabel.setText(biodata.getUserId());
                usernameLabel.setText(biodata.getUsername());
                nisNipLabel.setText(biodata.getNisNip());
                nameLabel.setText(biodata.getNama());
                genderLabel.setText(biodata.getGender());
                addressLabel.setText(biodata.getAlamat());
                emailLabel.setText(biodata.getEmail());
                phoneLabel.setText(biodata.getNomerHp());
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
        String sql = "SELECT j.hari, j.jam_mulai, j.jam_selsai, m.nama_mapel, k.nama_kelas, u.nama AS nama_pengajar " +
                "FROM Jadwal j " +
                "JOIN Matpel m ON j.Matpel_mapel_id = m.mapel_id " +
                "JOIN Kelas k ON j.Kelas_Users_user_id = k.Users_user_id AND j.Kelas_kelas_id = k.kelas_id " +
                "JOIN Users u ON k.Users_user_id = u.user_id " +
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
        existingNilaiColumn.setCellValueFactory(new PropertyValueFactory<>("nilai"));
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
                Nilai nilai = new Nilai(
                        rs.getInt("nilai"),
                        rs.getString("jenis_nilai"));
                Matpel matpel = new Matpel( rs.getString("nama_mapel"));
                nilaiList.add(new NilaiEntry(
                        nilai.getJenisNilai(),
                        matpel.getNamaMapel(),
                        nilai.getNilaiAngka()
                ));
            }
            nilaiUjianTable.setItems(nilaiList);
        }
        catch (SQLException e) {
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
                Tugas tugas = new Tugas(
                        rs.getString("keterangan"),
                        rs.getTimestamp("deadline").toLocalDateTime(),
                        rs.getTimestamp("tanggal_direlease").toLocalDateTime()
                );
                Matpel matpel = new Matpel(rs.getString("nama_mapel"));
                Kelas kelas = new Kelas(rs.getString("nama_kelas"));
                tugasList.add(new TugasEntry(
                        tugas.getKeterangan(),
                        tugas.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        tugas.getTanggalDirelease().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        matpel.getNamaMapel(),
                        kelas.getNamaKelas()
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
                Materi materi = new Materi(rs.getString("nama_materi"));
                Matpel matpel = new Matpel(rs.getString("nama_mapel"));
                Kelas kelas = new Kelas(rs.getString("nama_kelas"));
                materiList.add(new MateriEntry(
                        materi.getNamaMateri(),
                        matpel.getNamaMapel(),
                        kelas.getNamaKelas()
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
                Ujian ujian = new Ujian(
                        rs.getString("jenis_ujian"),
                        rs.getTimestamp("tanggal").toLocalDateTime()
                );
                Matpel matpel = new Matpel(rs.getString("nama_mapel"));
                Kelas kelas = new Kelas(rs.getString("nama_kelas"));
                ujianList.add(new UjianEntry(
                        ujian.getJenisUjian(),
                        ujian.getTanggal().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        matpel.getNamaMapel(),
                        kelas.getNamaKelas()
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
                Absensi absensi = new Absensi(
                        rs.getTimestamp("tanggal").toLocalDateTime(),
                        rs.getString("status")
                );
                Matpel matpel = new Matpel(rs.getString("nama_mapel"));
                Kelas kelas = new Kelas(rs.getString("nama_kelas"));
                Jadwal jadwal = new Jadwal(
                        rs.getString("jam_mulai"),
                        rs.getString("jam_selsai")
                );
                absensiList.add(new AbsensiEntry(
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
            AlertClass.ErrorAlert("Database Error", "Failed to load attendance", e.getMessage());
            e.printStackTrace();
        }
    }


    // --- Announcements Methods ---
    private void initAnnouncementTable() {
        announcementWaktuColumn.setCellValueFactory(new PropertyValueFactory<>("waktu"));
        announcementContentColumn.setCellValueFactory(new PropertyValueFactory<>("PengumumanContent"));
    }
    @FXML
    void loadAnnouncements() {
        ObservableList<Pengumuman> announcementList = FXCollections.observableArrayList();
        // Guru can see all announcements, not just their own
        String sql = "SELECT pengumuman_id, pengumuman, waktu, Users_user_id FROM Pengumuman ORDER BY waktu DESC";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            // No user_id filter here, as Guru can view all announcements
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Timestamp timestamp = rs.getTimestamp("waktu");

                String originalContent = rs.getString("pengumuman");
                String userIdOfPoster = rs.getString("Users_user_id");

                boolean hasPrefix = originalContent.matches("^\\[.+\\]\\s*[^:]+:\\s*");
                String displayContent = originalContent;

                if (!hasPrefix) {
                    try {
                        Pair<String, String> posterInfo = getUserRoleAndName(userIdOfPoster);
                        if (posterInfo.getKey() != null && posterInfo.getValue() != null) {
                            displayContent = "[" + posterInfo.getKey().toUpperCase() + "] " + posterInfo.getValue() + ": " + originalContent;
                        }
                    } catch (SQLException e) {
                        System.err.println("Error fetching poster info for announcement ID " + rs.getInt("pengumuman_id") + ": " + e.getMessage());
                        displayContent = originalContent;
                    }
                }

                announcementList.add(new Pengumuman(
                        rs.getInt("pengumuman_id"),
                        displayContent, // Use processed content for the pengumumanContent field
                        userIdOfPoster,
                        timestamp != null ? timestamp.toLocalDateTime() : null

                ));
            }
            announcementTable.setItems(announcementList);
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load announcements", e.getMessage());
            e.printStackTrace();
        }
    }

    private Pair<String, String> getUserRoleAndName(String userId) throws SQLException {
        String roleName = null;
        String userName = null;
        String sql = "SELECT u.nama, r.role_name FROM Users u JOIN Role r ON u.Role_role_id = r.role_id WHERE u.user_id = ?";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Users user = new Users(rs.getString("nama"));
                Role role = new Role(rs.getString("role_name"));
                userName = user.getNama();
                roleName = role.getRoleName();
            }
        }
        return new Pair<>(roleName, userName);
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