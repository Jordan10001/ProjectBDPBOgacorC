// ProjectBDPBOgacor/src/main/java/org/example/projectbdpbogacor/Kepala/KepaladsController.java
package org.example.projectbdpbogacor.Kepala;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab; // Import Tab class
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ChoiceBox; // Import ChoiceBox
import javafx.scene.control.TextField; // Import TextField
import javafx.scene.control.TableView; // Import TableView
import javafx.scene.control.TableColumn; // Import TableColumn
import javafx.scene.control.cell.PropertyValueFactory; // Import PropertyValueFactory
import org.example.projectbdpbogacor.Aset.AlertClass;
import org.example.projectbdpbogacor.DBSource.DBS;
import org.example.projectbdpbogacor.HelloApplication;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class KepaladsController {

    @FXML
    private Label welcomeUserLabel;
    @FXML
    private TabPane kepalaTabPane;

    // Announcements
    @FXML
    private TextArea announcementTextArea;

    // View All Users (NEW FXML elements)
    @FXML
    private ChoiceBox<String> filterRoleChoiceBox;
    @FXML
    private TextField filterNameField;
    @FXML
    private TableView<UserTableEntry> allUsersTableView;
    @FXML
    private TableColumn<UserTableEntry, String> tableUserIdColumn;
    @FXML
    private TableColumn<UserTableEntry, String> tableUsernameColumn;
    @FXML
    private TableColumn<UserTableEntry, String> tableNisNipColumn;
    @FXML
    private TableColumn<UserTableEntry, String> tableNamaColumn;
    @FXML
    private TableColumn<UserTableEntry, String> tableGenderColumn;
    @FXML
    private TableColumn<UserTableEntry, String> tableAlamatColumn;
    @FXML
    private TableColumn<UserTableEntry, String> tableEmailColumn;
    @FXML
    private TableColumn<UserTableEntry, String> tableNomerHpColumn;
    @FXML
    private TableColumn<UserTableEntry, String> tableRoleColumn;

    private String loggedInUserId;
    private Map<String, String> roleNameToIdMap = new HashMap<>(); // Map role names to role IDs

    @FXML
    void initialize() {
        loggedInUserId = HelloApplication.getInstance().getLoggedInUserId();
        loadKepalaSekolahName();

        // Initialize "View All Users" components
        loadRolesForChoiceBox();
        filterRoleChoiceBox.getItems().addAll("All Roles");
        filterRoleChoiceBox.getItems().addAll(roleNameToIdMap.keySet());
        filterRoleChoiceBox.setValue("All Roles");
        initAllUsersTable();
        loadAllUsersToTable(filterRoleChoiceBox.getValue(), filterNameField.getText());

        // Add listeners for filter fields
        filterRoleChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> handleFilterUsers());
        filterNameField.textProperty().addListener((observable, oldValue, newValue) -> handleFilterUsers());

        // Add listener to tabs to load data when selected
        kepalaTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null) {
                if (newTab.getText().equals("View All Users")) {
                    loadAllUsersToTable(filterRoleChoiceBox.getValue(), filterNameField.getText());
                }
            }
        });
    }

    private void loadKepalaSekolahName() {
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

    @FXML
    void handleCreateAnnouncement() {
        String announcementContent = announcementTextArea.getText();

        if (announcementContent.isEmpty()) {
            AlertClass.WarningAlert("Input Error", "Announcement Empty", "Please enter the announcement content.");
            return;
        }

        try (Connection con = DBS.getConnection()) {
            // Updated SQL to include 'waktu' column and set it to NOW()
            String sql = "INSERT INTO Pengumuman (pengumuman, Users_user_id, waktu) VALUES (?, ?, NOW())";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, announcementContent);
            stmt.setString(2, loggedInUserId); // Kepala Sekolah's user ID

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                AlertClass.InformationAlert("Success", "Announcement Published", "Announcement has been published successfully.");
                announcementTextArea.clear();
            } else {
                AlertClass.ErrorAlert("Failed", "Announcement Not Published", "Failed to publish announcement.");
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to publish announcement", e.getMessage());
            e.printStackTrace();
        }
    }

    // NEW: View All Users Methods
    private void initAllUsersTable() {
        tableUserIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        tableUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        tableNisNipColumn.setCellValueFactory(new PropertyValueFactory<>("nisNip"));
        tableNamaColumn.setCellValueFactory(new PropertyValueFactory<>("nama"));
        tableGenderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        tableAlamatColumn.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        tableEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableNomerHpColumn.setCellValueFactory(new PropertyValueFactory<>("nomerHp"));
        tableRoleColumn.setCellValueFactory(new PropertyValueFactory<>("roleName")); // Display role name
    }

    private void loadRolesForChoiceBox() {
        roleNameToIdMap.clear(); // Clear existing data
        String sql = "SELECT role_id, role_name FROM Role"; // Query to get all roles
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String roleId = rs.getString("role_id"); // Get role ID
                String roleName = rs.getString("role_name"); // Get role name
                roleNameToIdMap.put(roleName, roleId); // Map name to ID
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load roles", e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadAllUsersToTable(String roleFilter, String nameFilter) {
        ObservableList<UserTableEntry> userList = FXCollections.observableArrayList();
        StringBuilder sqlBuilder = new StringBuilder("SELECT u.user_id, u.username, u.NIS_NIP, u.nama, u.gender, u.alamat, u.email, u.nomer_hp, r.role_name " +
                "FROM Users u JOIN Role r ON u.Role_role_id = r.role_id WHERE 1=1 "); // Start with 1=1 for easy WHERE clause appending

        if (roleFilter != null && !roleFilter.equals("All Roles")) {
            String roleId = roleNameToIdMap.get(roleFilter);
            if (roleId != null) {
                sqlBuilder.append("AND u.Role_role_id = ? ");
            }
        }
        if (nameFilter != null && !nameFilter.trim().isEmpty()) {
            sqlBuilder.append("AND (u.nama ILIKE ? OR u.username ILIKE ? OR u.NIS_NIP ILIKE ?) "); // Case-insensitive search
        }
        sqlBuilder.append("ORDER BY u.nama");

        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sqlBuilder.toString())) {
            int paramIndex = 1;
            if (roleFilter != null && !roleFilter.equals("All Roles")) {
                String roleId = roleNameToIdMap.get(roleFilter);
                if (roleId != null) {
                    stmt.setString(paramIndex++, roleId);
                }
            }
            if (nameFilter != null && !nameFilter.trim().isEmpty()) {
                stmt.setString(paramIndex++, "%" + nameFilter.trim() + "%");
                stmt.setString(paramIndex++, "%" + nameFilter.trim() + "%");
                stmt.setString(paramIndex++, "%" + nameFilter.trim() + "%");
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                userList.add(new UserTableEntry(
                        rs.getString("user_id"),
                        rs.getString("username"),
                        rs.getString("NIS_NIP"),
                        rs.getString("nama"),
                        rs.getString("gender").equals("L") ? "Laki-laki" : "Perempuan",
                        rs.getString("alamat"),
                        rs.getString("email"),
                        rs.getString("nomer_hp"),
                        rs.getString("role_name")
                ));
            }
            allUsersTableView.setItems(userList);
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load all users", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleFilterUsers() {
        String selectedRole = filterRoleChoiceBox.getValue();
        String nameFilter = filterNameField.getText();
        loadAllUsersToTable(selectedRole, nameFilter);
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

    // NEW: Model Class for UserTableEntry (copied from AdmindsController)
    public static class UserTableEntry {
        private final StringProperty userId;
        private final StringProperty username;
        private final StringProperty nisNip;
        private final StringProperty nama;
        private final StringProperty gender;
        private final StringProperty alamat;
        private final StringProperty email;
        private final StringProperty nomerHp;
        private final StringProperty roleName; // Full role name

        public UserTableEntry(String userId, String username, String nisNip, String nama, String gender, String alamat, String email, String nomerHp, String roleName) {
            this.userId = new SimpleStringProperty(userId);
            this.username = new SimpleStringProperty(username);
            this.nisNip = new SimpleStringProperty(nisNip);
            this.nama = new SimpleStringProperty(nama);
            this.gender = new SimpleStringProperty(gender);
            this.alamat = new SimpleStringProperty(alamat);
            this.email = new SimpleStringProperty(email);
            this.nomerHp = new SimpleStringProperty(nomerHp);
            this.roleName = new SimpleStringProperty(roleName);
        }

        public String getUserId() { return userId.get(); }
        public StringProperty userIdProperty() { return userId; }
        public String getUsername() { return username.get(); }
        public StringProperty usernameProperty() { return username; }
        public String getNisNip() { return nisNip.get(); }
        public StringProperty nisNipProperty() { return nisNip; }
        public String getNama() { return nama.get(); }
        public StringProperty namaProperty() { return nama; }
        public String getGender() { return gender.get(); }
        public StringProperty genderProperty() { return gender; }
        public String getAlamat() { return alamat.get(); }
        public StringProperty alamatProperty() { return alamat; }
        public String getEmail() { return email.get(); }
        public StringProperty emailProperty() { return email; }
        public String getNomerHp() { return nomerHp.get(); }
        public StringProperty nomerHpProperty() { return nomerHp; }
        public String getRoleName() { return roleName.get(); }
        public StringProperty roleNameProperty() { return roleName; }
    }
}