// ProjectBDPBOgacor/src/main/java/org/example/projectbdpbogacor/Kepala/KepaladsController.java
package org.example.projectbdpbogacor.Kepala;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.projectbdpbogacor.Aset.AlertClass;
import org.example.projectbdpbogacor.DBSource.DBS;
import org.example.projectbdpbogacor.HelloApplication;
import org.example.projectbdpbogacor.model.Pair;
// import org.example.projectbdpbogacor.model.PengumumanEntry; // REMOVED
// import org.example.projectbdpbogacor.model.UserTableEntry; // REMOVED
import org.example.projectbdpbogacor.Service.Pengumuman; // ADDED
import org.example.projectbdpbogacor.Service.Users; // ADDED

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class KepaladsController {

    @FXML
    private Label welcomeUserLabel;
    @FXML
    private TabPane kepalaTabPane;

    // Announcements
    @FXML
    private TextArea announcementTextArea;
    @FXML
    private TableView<Pengumuman> announcementTable; // Changed to Pengumuman service
    @FXML
    private TableColumn<Pengumuman, String> announcementWaktuColumn; // Column for announcement time
    @FXML
    private TableColumn<Pengumuman, String> announcementContentColumn; // Column for announcement content
    @FXML
    private Button createAnnouncementButton;
    @FXML
    private Button updateAnnouncementButton;
    @FXML
    private Button deleteAnnouncementButton;


    // View All Users
    @FXML
    private ChoiceBox<String> filterRoleChoiceBox;
    @FXML
    private TextField filterNameField;
    @FXML
    private TableView<Users> allUsersTableView; // Changed to Users service
    @FXML
    private TableColumn<Users, String> tableUserIdColumn; // Changed to Users service
    @FXML
    private TableColumn<Users, String> tableUsernameColumn; // Changed to Users service
    @FXML
    private TableColumn<Users, String> tableNisNipColumn; // Changed to Users service
    @FXML
    private TableColumn<Users, String> tableNamaColumn; // Changed to Users service
    @FXML
    private TableColumn<Users, String> tableGenderColumn; // Changed to Users service
    @FXML
    private TableColumn<Users, String> tableAlamatColumn; // Changed to Users service
    @FXML
    private TableColumn<Users, String> tableEmailColumn; // Changed to Users service
    @FXML
    private TableColumn<Users, String> tableNomerHpColumn; // Changed to Users service
    @FXML
    private TableColumn<Users, String> tableRoleColumn; // Changed to Users service

    private String loggedInUserId;
    private Map<String, String> roleNameToIdMap = new HashMap<>();

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
        initAnnouncementTable();
        loadAnnouncements();

        // Add listener for announcement table selection
        announcementTable.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            if (newSelection != null) {
                announcementTextArea.setText(newSelection.getPengumumanContent()); // Changed from getPengumuman to getPengumumanContent
                updateAnnouncementButton.setDisable(false);
                deleteAnnouncementButton.setDisable(false);
                createAnnouncementButton.setDisable(true);
            } else {
                announcementTextArea.clear();
                updateAnnouncementButton.setDisable(true);
                deleteAnnouncementButton.setDisable(true);
                createAnnouncementButton.setDisable(false);
            }
        });
        updateAnnouncementButton.setDisable(true);
        deleteAnnouncementButton.setDisable(true);


        // Add listeners to tabs to load data when selected
        kepalaTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null) {
                if (newTab.getText().equals("View All Users")) {
                    loadAllUsersToTable(filterRoleChoiceBox.getValue(), filterNameField.getText());
                } else if (newTab.getText().equals("Announcements")) {
                    loadAnnouncements();
                    updateAnnouncementButton.setDisable(true);
                    deleteAnnouncementButton.setDisable(true);
                    createAnnouncementButton.setDisable(false);
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
        tableRoleColumn.setCellValueFactory(new PropertyValueFactory<>("roleName"));
    }

    private void loadRolesForChoiceBox() {
        roleNameToIdMap.clear();
        String sql = "SELECT role_id, role_name FROM Role";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String roleId = rs.getString("role_id");
                String roleName = rs.getString("role_name");
                roleNameToIdMap.put(roleName, roleId);
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load roles", e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadAllUsersToTable(String roleFilter, String nameFilter) {
        ObservableList<Users> userList = FXCollections.observableArrayList(); // Changed to Users service
        StringBuilder sqlBuilder = new StringBuilder("SELECT u.user_id, u.username, u.NIS_NIP, u.nama, u.gender, u.alamat, u.email, u.nomer_hp, r.role_name " +
                "FROM Users u JOIN Role r ON u.Role_role_id = r.role_id WHERE 1=1 ");

        if (roleFilter != null && !roleFilter.equals("All Roles")) {
            String roleId = roleNameToIdMap.get(roleFilter);
            if (roleId != null) {
                sqlBuilder.append("AND u.Role_role_id = ? ");
            }
        }
        if (nameFilter != null && !nameFilter.trim().isEmpty()) {
            sqlBuilder.append("AND (u.nama ILIKE ? OR u.username ILIKE ? OR u.NIS_NIP ILIKE ?) ");
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
                userList.add(new Users( // Using the new Users constructor
                        rs.getString("user_id"),
                        rs.getString("username"),
                        rs.getString("NIS_NIP"),
                        rs.getString("nama"),
                        rs.getString("gender").equals("L") ? "L" : "P",
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
            stmt.setString(2, loggedInUserId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                AlertClass.InformationAlert("Success", "Announcement Published", "Announcement has been published successfully.");
                announcementTextArea.clear();
                loadAnnouncements();
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
        announcementWaktuColumn.setCellValueFactory(new PropertyValueFactory<>("waktu")); // Property name from Pengumuman service
        announcementContentColumn.setCellValueFactory(new PropertyValueFactory<>("pengumumanContent")); // Property name from Pengumuman service
    }

    private void loadAnnouncements() {
        ObservableList<Pengumuman> announcementList = FXCollections.observableArrayList(); // Changed to Pengumuman service
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

                announcementList.add(new Pengumuman( // Using Pengumuman service constructor
                        rs.getInt("pengumuman_id"),
                        displayContent, // Use processed content for the pengumumanContent field
                        userIdOfPoster, // Pass the user ID of the poster
                        timestamp != null ? timestamp.toLocalDateTime() : null // Pass LocalDateTime object
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
        Pengumuman selectedAnnouncement = announcementTable.getSelectionModel().getSelectedItem(); // Changed to Pengumuman service
        if (selectedAnnouncement == null) {
            AlertClass.WarningAlert("Selection Error", "No Announcement Selected", "Please select an announcement to update.");
            return;
        }

        String updatedContentRaw = announcementTextArea.getText();
        if (updatedContentRaw.isEmpty()) {
            AlertClass.WarningAlert("Input Error", "Announcement Content Empty", "Please enter content for the announcement.");
            return;
        }

        String originalFullContent = selectedAnnouncement.getPengumumanContent(); // Changed to getPengumumanContent
        String finalContentToSave;

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^\\[.+\\]\\s*[^:]+:\\s*");
        java.util.regex.Matcher matcher = pattern.matcher(originalFullContent);

        String currentPrefix = "";
        if (matcher.find()) {
            currentPrefix = matcher.group(0);
            originalFullContent = originalFullContent.substring(currentPrefix.length());
        }

        String contentWithoutPotentialPrefix = updatedContentRaw;
        java.util.regex.Matcher updatedContentMatcher = pattern.matcher(updatedContentRaw);
        if (updatedContentMatcher.find() && updatedContentMatcher.group(0).equals(currentPrefix)) {
            contentWithoutPotentialPrefix = updatedContentRaw.substring(currentPrefix.length());
        }

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

        finalContentToSave = rolePrefix + namePrefix + contentWithoutPotentialPrefix;


        int pengumumanId = selectedAnnouncement.getPengumumanId(); // Property name from Pengumuman service

        try (Connection con = DBS.getConnection()) {
            String sql = "UPDATE Pengumuman SET pengumuman = ?, waktu = NOW() WHERE pengumuman_id = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, finalContentToSave);
            stmt.setInt(2, pengumumanId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                AlertClass.InformationAlert("Success", "Announcement Updated", "Announcement updated successfully.");
                announcementTextArea.clear();
                loadAnnouncements();
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
        Pengumuman selectedAnnouncement = announcementTable.getSelectionModel().getSelectedItem(); // Changed to Pengumuman service
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
            int pengumumanId = selectedAnnouncement.getPengumumanId(); // Property name from Pengumuman service

            try (Connection con = DBS.getConnection()) {
                String sql = "DELETE FROM Pengumuman WHERE pengumuman_id = ?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setInt(1, pengumumanId);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    AlertClass.InformationAlert("Success", "Announcement Deleted", "Announcement deleted successfully.");
                    announcementTextArea.clear();
                    loadAnnouncements();
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

}