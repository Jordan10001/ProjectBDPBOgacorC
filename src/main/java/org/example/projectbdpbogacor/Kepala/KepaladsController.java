// ProjectBDPBOgacor/src/main/java/org/example/projectbdpbogacor/Kepala/KepaladsController.java
package org.example.projectbdpbogacor.Kepala;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory; // Import PropertyValueFactory
import org.example.projectbdpbogacor.Aset.AlertClass;
import org.example.projectbdpbogacor.DBSource.DBS;
import org.example.projectbdpbogacor.HelloApplication;
import org.example.projectbdpbogacor.model.PengumumanEntry;

import java.io.IOException;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class KepaladsController {

    @FXML
    private Label welcomeUserLabel;
    @FXML
    private TabPane kepalaTabPane;

    // Announcements
    @FXML
    private TextArea announcementTextArea;
    @FXML
    private TableView<PengumumanEntry> announcementTable; // Table to view announcements
    @FXML
    private TableColumn<PengumumanEntry, String> announcementWaktuColumn; // Column for announcement time
    @FXML
    private TableColumn<PengumumanEntry, String> announcementContentColumn; // Column for announcement content


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

        // Initialize announcement table
        initAnnouncementTable(); // Initialize new announcement table
        loadAnnouncements(); // Load announcements initially when the controller starts

        // Add listener for announcement table selection
        announcementTable.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            if (newSelection != null) {
                announcementTextArea.setText(newSelection.getPengumuman()); // Populate text area with selected announcement content
            } else {
                announcementTextArea.clear(); // Clear text area if no selection
            }
        });


        // Add listeners to tabs to load data when selected
        kepalaTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null) {
                if (newTab.getText().equals("View All Users")) {
                    loadAllUsersToTable(filterRoleChoiceBox.getValue(), filterNameField.getText());
                } else if (newTab.getText().equals("Announcements")) { // Added this case
                    loadAnnouncements();
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

    // Helper method to get user's role name and actual name
    private Pair<String, String> getUserRoleAndName(String userId) throws SQLException {
        String roleName = null;
        String userName = null;
        String sql = "SELECT u.nama, r.role_name FROM Users u JOIN Role r ON u.Role_role_id = r.role_id WHERE u.user_id = ?";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                userName = rs.getString("nama");
                roleName = rs.getString("role_name");
            }
        }
        return new Pair<>(roleName, userName);
    }

    @FXML
    void handleCreateAnnouncement() {
        String announcementContent = announcementTextArea.getText();

        if (announcementContent.isEmpty()) {
            AlertClass.WarningAlert("Input Error", "Announcement Empty", "Please enter the announcement content.");
            return;
        }

        try (Connection con = DBS.getConnection()) {
            Pair<String, String> userInfo = getUserRoleAndName(loggedInUserId);
            String rolePrefix = (userInfo.getKey() != null) ? "[" + userInfo.getKey().toUpperCase() + "] " : "";
            String namePrefix = (userInfo.getValue() != null) ? userInfo.getValue() + ": " : "";

            String finalAnnouncementContent = rolePrefix + namePrefix + announcementContent;

            String sql = "INSERT INTO Pengumuman (pengumuman, Users_user_id, waktu) VALUES (?, ?, NOW())";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, finalAnnouncementContent);
            stmt.setString(2, loggedInUserId); // Admin's user ID

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                AlertClass.InformationAlert("Success", "Announcement Published", "Announcement has been published successfully.");
                announcementTextArea.clear();
                loadAnnouncements(); // Refresh the table after creation
            } else {
                AlertClass.ErrorAlert("Failed", "Announcement Not Published", "Failed to publish announcement.");
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to publish announcement", e.getMessage());
            e.printStackTrace();
        }
    }

    // --- Announcements Methods ---
    private void initAnnouncementTable() {
        announcementWaktuColumn.setCellValueFactory(new PropertyValueFactory<>("waktu"));
        announcementContentColumn.setCellValueFactory(new PropertyValueFactory<>("pengumuman"));
    }

    private void loadAnnouncements() {
        ObservableList<PengumumanEntry> announcementList = FXCollections.observableArrayList();
        String sql = "SELECT pengumuman_id, pengumuman, waktu, Users_user_id FROM Pengumuman ORDER BY waktu DESC";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Timestamp timestamp = rs.getTimestamp("waktu");
                String waktuFormatted;
                if (timestamp != null) {
                    waktuFormatted = timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                } else {
                    waktuFormatted = "N/A";
                }

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

                announcementList.add(new PengumumanEntry(
                        rs.getInt("pengumuman_id"),
                        waktuFormatted,
                        displayContent
                ));
            }
            announcementTable.setItems(announcementList);
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load announcements", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleUpdateAnnouncement() {
        PengumumanEntry selectedAnnouncement = announcementTable.getSelectionModel().getSelectedItem();
        if (selectedAnnouncement == null) {
            AlertClass.WarningAlert("Selection Error", "No Announcement Selected", "Please select an announcement to update.");
            return;
        }

        String updatedContentRaw = announcementTextArea.getText();
        if (updatedContentRaw.isEmpty()) {
            AlertClass.WarningAlert("Input Error", "Announcement Content Empty", "Please enter content for the announcement.");
            return;
        }

        String originalFullContent = selectedAnnouncement.getPengumuman();
        String finalContentToSave;

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^\\[.+\\]\\s*[^:]+:\\s*");
        java.util.regex.Matcher matcher = pattern.matcher(originalFullContent);

        if (matcher.find()) {
            // If the original content has a prefix, keep it and append the new raw content
            String prefix = matcher.group(0);

            // Check if the user re-typed the prefix in the textarea. If so, remove it to avoid duplication.
            // This is a heuristic and might need refinement based on actual user behavior.
            String strippedUpdatedContent = updatedContentRaw;
            java.util.regex.Matcher updatedContentMatcher = pattern.matcher(updatedContentRaw);
            if (updatedContentMatcher.find() && updatedContentMatcher.group(0).equals(prefix)) {
                strippedUpdatedContent = updatedContentRaw.substring(prefix.length());
            }

            finalContentToSave = prefix + strippedUpdatedContent;
        } else {
            // If the original content doesn't have a prefix, create a new one using the current user's info
            Pair<String, String> userInfo = null;
            try {
                userInfo = getUserRoleAndName(loggedInUserId);
            } catch (SQLException e) {
                System.err.println("Error fetching user info for update: " + e.getMessage());
                AlertClass.ErrorAlert("Database Error", "Failed to get user info for update", "Could not retrieve current user's role and name.");
                return;
            }
            String rolePrefix = (userInfo.getKey() != null) ? "[" + userInfo.getKey().toUpperCase() + "] " : "";
            String namePrefix = (userInfo.getValue() != null) ? userInfo.getValue() + ": " : "";
            finalContentToSave = rolePrefix + namePrefix + updatedContentRaw;
        }


        int pengumumanId = selectedAnnouncement.getPengumumanId();

        try (Connection con = DBS.getConnection()) {
            String sql = "UPDATE Pengumuman SET pengumuman = ?, waktu = NOW() WHERE pengumuman_id = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, finalContentToSave);
            stmt.setInt(2, pengumumanId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                AlertClass.InformationAlert("Success", "Announcement Updated", "Announcement updated successfully.");
                announcementTextArea.clear();
                loadAnnouncements(); // Refresh the table
            } else {
                AlertClass.ErrorAlert("Failed", "Announcement Not Updated", "Failed to update announcement.");
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to update announcement", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleDeleteAnnouncement() {
        PengumumanEntry selectedAnnouncement = announcementTable.getSelectionModel().getSelectedItem();
        if (selectedAnnouncement == null) {
            AlertClass.WarningAlert("Selection Error", "No Announcement Selected", "Please select an announcement to delete.");
            return;
        }

        Optional<ButtonType> result = AlertClass.ConfirmationAlert(
                "Confirm Deletion",
                "Delete Announcement",
                "Are you sure you want to delete this announcement? This action cannot be undone."
        );

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Use the pengumuman_id directly from the selected object
            int pengumumanId = selectedAnnouncement.getPengumumanId();

            try (Connection con = DBS.getConnection()) {
                String sql = "DELETE FROM Pengumuman WHERE pengumuman_id = ?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setInt(1, pengumumanId);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    AlertClass.InformationAlert("Success", "Announcement Deleted", "Announcement deleted successfully.");
                    announcementTextArea.clear();
                    loadAnnouncements(); // Refresh the table
                } else {
                    AlertClass.ErrorAlert("Failed", "Announcement Not Deleted", "Failed to delete announcement.");
                }
            } catch (SQLException e) {
                AlertClass.ErrorAlert("Database Error", "Failed to delete announcement", e.getMessage());
                e.printStackTrace();
            }
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

    // Helper class for ChoiceBox items (to store display text and associated ID)
    private static class Pair<K, V> {
        private final K key;
        private final V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        @Override
        public String toString() {
            return key.toString(); // Display the key in ChoiceBox
        }
    }
}