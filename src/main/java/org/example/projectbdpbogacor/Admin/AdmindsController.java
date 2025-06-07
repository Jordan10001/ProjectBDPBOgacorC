// ProjectBDPBOgacor/src/main/java/org/example/projectbdpbogacor/Admin/AdmindsController.java
package org.example.projectbdpbogacor.Admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.projectbdpbogacor.Aset.AlertClass;
import org.example.projectbdpbogacor.Aset.HashGenerator;
import org.example.projectbdpbogacor.DBSource.DBS;
import org.example.projectbdpbogacor.HelloApplication;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AdmindsController {

    @FXML
    private Label welcomeUserLabel;
    @FXML
    private TabPane adminTabPane;

    // Manage Users (Add User)
    @FXML
    private TextField newUserIdField; // New FXML ID for user_id input
    @FXML
    private TextField newUsernameField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private TextField newNisNipField;
    @FXML
    private TextField newNameField;
    @FXML
    private ChoiceBox<String> newGenderChoiceBox;
    @FXML
    private TextField newAddressField;
    @FXML
    private TextField newEmailField;
    @FXML
    private TextField newPhoneNumberField;
    @FXML
    private ChoiceBox<String> newRoleChoiceBox; // New FXML ID for role selection

    // Manage Users (Edit/Delete)
    @FXML
    private ChoiceBox<String> editUserChoiceBox; // Displays "Name (ID)"
    @FXML
    private TextField editUserIdField;
    @FXML
    private TextField editUsernameField;
    @FXML
    private PasswordField editPasswordField;
    @FXML
    private TextField editNisNipField;
    @FXML
    private TextField editNameField;
    @FXML
    private ChoiceBox<String> editGenderChoiceBox;
    @FXML
    private TextField editAddressField;
    @FXML
    private TextField editEmailField;
    @FXML
    private TextField editPhoneNumberField;
    @FXML
    private ChoiceBox<String> editRoleChoiceBox;
    @FXML
    private Button deleteUserButton;
    @FXML
    private Button updateUserButton;


    // Manage Schedules & Subjects
    @FXML
    private TextField scheduleDayField;
    @FXML
    private TextField scheduleStartTimeField;
    @FXML
    private TextField scheduleEndTimeField;
    @FXML
    private ChoiceBox<String> scheduleSubjectChoiceBox;
    @FXML
    private ChoiceBox<String> scheduleClassChoiceBox; // Displays "Nama Kelas - Nama Wali"
    @FXML
    private TextField newSubjectNameField;
    @FXML
    private TextField newSubjectCategoryField;

    // Assign Students to Classes
    @FXML
    private ChoiceBox<String> assignStudentChoiceBox; // Displays "Student Name (NIS/NIP)"
    @FXML
    private ChoiceBox<String> assignClassChoiceBox; // Displays "Nama Kelas - Nama Wali"

    // Manage Students in Class
    @FXML
    private ChoiceBox<String> studentClassFilterChoiceBox; // Filter students by class
    @FXML
    private TableView<StudentEntry> studentInClassTableView;
    @FXML
    private TableColumn<StudentEntry, String> studentNameInClassColumn;
    @FXML
    private TableColumn<StudentEntry, String> nisNipInClassColumn;
    @FXML
    private TableColumn<StudentEntry, String> studentUserIdInClassColumn; // To store user_id
    @FXML
    private Button deleteStudentFromClassButton;
    @FXML
    private Button editStudentInClassButton;

    // Announcements
    @FXML
    private TextArea announcementTextArea;

    // NEW: Create Class
    @FXML
    private TextField newClassNameField;
    @FXML
    private TextField newClassDescriptionField;
    @FXML
    private ChoiceBox<String> newClassWaliKelasChoiceBox; // Displays "Wali Kelas Name (ID)"
    @FXML
    private ChoiceBox<String> newClassSemesterChoiceBox; // Displays "Tahun Ajaran - Semester"

    // NEW: View All Users Table
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

    // Helper data structures for ChoiceBoxes
    private ObservableList<Pair<String, String>> classData = FXCollections.observableArrayList(); // Pair<Display, ID> where ID is "kelas_id-wali_user_id"
    private ObservableList<Pair<String, Integer>> subjectData = FXCollections.observableArrayList(); // Pair<Display, ID>
    private ObservableList<Pair<String, String>> studentData = FXCollections.observableArrayList(); // Pair<Display, ID>
    private ObservableList<Pair<String, String>> allUsersData = FXCollections.observableArrayList(); // Pair<Display, UserID> for edit/delete
    private ObservableList<Pair<String, String>> waliKelasData = FXCollections.observableArrayList(); // Pair<Display, WaliID> for new class
    private ObservableList<Pair<String, Integer>> semesterData = FXCollections.observableArrayList(); // Pair<Display, SemesterID> for new class
    private Map<String, String> roleNameToIdMap = new HashMap<>(); // Map role names to role IDs
    private Map<String, String> roleIdToNameMap = new HashMap<>(); // Map ID to role names


    @FXML
    void initialize() {
        loggedInUserId = HelloApplication.getInstance().getLoggedInUserId();
        loadAdminName();

        // Initialize ChoiceBoxes for new user
        newGenderChoiceBox.getItems().addAll("L", "P"); // L: Laki-laki, P: Perempuan
        newGenderChoiceBox.setValue("L"); // Default

        loadRolesForChoiceBox(); // Load roles into the newRoleChoiceBox
        newRoleChoiceBox.setValue("Siswa"); // Default to student for adding users

        // Add listener to newRoleChoiceBox to auto-fill newUserIdField
        newRoleChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                newUserIdField.setText(generateUserIdPrefix(newValue));
            }
        });


        // Initialize ChoiceBoxes for schedules and assignments
        loadSubjectsForChoiceBox();
        loadClassesForChoiceBox();
        loadStudentsForChoiceBox();

        // Initialize edit/delete user fields
        editGenderChoiceBox.getItems().addAll("L", "P");
        loadUsersForEditDelete(); // Load users for the edit/delete dropdown
        editRoleChoiceBox.getItems().addAll(roleNameToIdMap.keySet()); // Populate edit role choice box

        // Initialize Student in Class Table
        initStudentInClassTable();
        loadClassesForStudentFilter(); // Load classes for the filter dropdown

        // NEW: Initialize elements for Create Class
        loadWaliKelasForChoiceBox();
        loadSemestersForChoiceBox();

        // NEW: Initialize all users table
        initAllUsersTable();

        // Add listeners to tabs to load data when selected
        adminTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null) {
                switch (newTab.getText()) {
                    case "Manage Schedules & Subjects":
                        loadSubjectsForChoiceBox(); // Refresh subjects
                        loadClassesForChoiceBox();  // Refresh classes
                        break;
                    case "Assign Students to Classes":
                        loadStudentsForChoiceBox(); // Refresh students
                        loadClassesForChoiceBox();  // Refresh classes
                        break;
                    case "Manage Users": // Assuming this is the new tab for edit/delete
                        loadUsersForEditDelete();
                        // Clear selected user fields when switching to this tab
                        editUserChoiceBox.getSelectionModel().clearSelection();
                        clearEditUserFields();
                        break;
                    case "Manage Students in Class":
                        loadClassesForStudentFilter();
                        studentInClassTableView.getItems().clear(); // Clear table until a class is selected
                        break;
                    case "Create Class":
                        loadWaliKelasForChoiceBox(); // Refresh wali kelas list
                        loadSemestersForChoiceBox(); // Refresh semester list
                        break;
                    case "View All Users":
                        loadAllUsersToTable(); // Load all users to the table
                        break;
                }
            }
        });
    }

    private void loadAdminName() {
        String sql = "SELECT nama FROM Users WHERE user_id = ?";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, loggedInUserId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                welcomeUserLabel.setText("Welcome, " + rs.getString("nama") + "!");
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load admin name", e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadRolesForChoiceBox() {
        roleNameToIdMap.clear(); // Clear existing data
        roleIdToNameMap.clear();
        newRoleChoiceBox.getItems().clear(); // Clear existing items

        String sql = "SELECT role_id, role_name FROM Role"; // Query to get all roles
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String roleId = rs.getString("role_id"); // Get role ID
                String roleName = rs.getString("role_name"); // Get role name
                roleNameToIdMap.put(roleName, roleId); // Map name to ID
                roleIdToNameMap.put(roleId, roleName); // Map ID to name
                newRoleChoiceBox.getItems().add(roleName); // Add name to ChoiceBox
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load roles", e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method to generate user ID prefix based on role
    private String generateUserIdPrefix(String roleName) {
        String prefix = "";
        switch (roleName) {
            case "Admin":
                prefix = "A";
                break;
            case "Kepala Sekolah":
                prefix = "K";
                break;
            case "Guru":
                prefix = "G";
                break;
            case "Wali Kelas":
                prefix = "W";
                break;
            case "Siswa":
                prefix = "S";
                break;
            default:
                prefix = ""; // No prefix or handle unknown role
        }
        return prefix;
    }

    @FXML
    void handleAddUser() { // Renamed from handleAddStudent to handleAddUser
        String userId = newUserIdField.getText(); // Get user ID from the new field
        String username = newUsernameField.getText();
        String password = newPasswordField.getText();
        String nisNip = newNisNipField.getText();
        String nama = newNameField.getText();
        String gender = newGenderChoiceBox.getValue();
        String alamat = newAddressField.getText();
        String email = newEmailField.getText();
        String nomerHp = newPhoneNumberField.getText();
        String selectedRoleName = newRoleChoiceBox.getValue(); // Get selected role name

        if (userId.isEmpty() || username.isEmpty() || password.isEmpty() || nisNip.isEmpty() || nama.isEmpty() ||
                gender == null || alamat.isEmpty() || email.isEmpty() || nomerHp.isEmpty() || selectedRoleName == null) {
            AlertClass.WarningAlert("Input Error", "Missing Information", "Please fill in all user details and select a role.");
            return;
        }
        if (password.length() != 8) {
            AlertClass.WarningAlert("Input Error", "Password Length Error", "Password must be exactly 8 characters long.");
            return;
        }

        String roleId = roleNameToIdMap.get(selectedRoleName); // Get the role ID from the map
        if (roleId == null) {
            AlertClass.ErrorAlert("Role Error", "Invalid Role", "Selected role is not recognized.");
            return;
        }

        try (Connection con = DBS.getConnection()) {
            // Check if user_id already exists
            String checkIdSql = "SELECT COUNT(*) FROM Users WHERE user_id = ?";
            PreparedStatement checkIdStmt = con.prepareStatement(checkIdSql);
            checkIdStmt.setString(1, userId);
            ResultSet rs = checkIdStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                AlertClass.WarningAlert("Input Error", "Duplicate User ID", "The entered User ID already exists. Please use a unique ID.");
                return;
            }

            String hashedPassword = HashGenerator.hash(password);

            String sql = "INSERT INTO Users (user_id, username, password, NIS_NIP, nama, gender, alamat, email, nomer_hp, Role_role_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, userId);
            stmt.setString(2, username);
            stmt.setString(3, hashedPassword);
            stmt.setString(4, nisNip);
            stmt.setString(5, nama);
            stmt.setString(6, gender);
            stmt.setString(7, alamat);
            stmt.setString(8, email);
            stmt.setString(9, nomerHp);
            stmt.setString(10, roleId); // Use the retrieved roleId

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                AlertClass.InformationAlert("Success", "User Added", "New user '" + nama + "' has been added with User ID: " + userId + " and Role: " + selectedRoleName);
                // Clear fields
                newUserIdField.clear(); // Clear the new user ID field
                newUsernameField.clear();
                newPasswordField.clear();
                newNisNipField.clear();
                newNameField.clear();
                newAddressField.clear();
                newEmailField.clear();
                newPhoneNumberField.clear();
                newRoleChoiceBox.setValue("Siswa"); // Reset to default
                loadUsersForEditDelete(); // Refresh list of users for edit/delete
                loadStudentsForChoiceBox(); // Refresh students for assignment
                loadWaliKelasForChoiceBox(); // Refresh wali kelas for new class
                loadAllUsersToTable(); // Refresh all users table
            } else {
                AlertClass.ErrorAlert("Failed", "User Not Added", "Failed to add user to the database.");
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to add user", e.getMessage());
            e.printStackTrace();
        }
    }

    // --- Manage Users (Edit/Delete) Methods ---
    private void loadUsersForEditDelete() {
        allUsersData.clear();
        editUserChoiceBox.getItems().clear();

        String sql = "SELECT user_id, nama FROM Users ORDER BY nama";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String userId = rs.getString("user_id");
                String nama = rs.getString("nama");
                String display = nama + " (" + userId + ")";
                allUsersData.add(new Pair<>(display, userId));
                editUserChoiceBox.getItems().add(display);
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load users for edit/delete", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleUserSelectionForEditDelete() {
        String selectedUserDisplay = editUserChoiceBox.getValue();
        if (selectedUserDisplay == null || selectedUserDisplay.isEmpty()) {
            clearEditUserFields();
            return;
        }

        String userIdToEdit = allUsersData.stream()
                .filter(p -> p.getKey().equals(selectedUserDisplay))
                .map(Pair::getValue)
                .findFirst()
                .orElse(null);

        if (userIdToEdit == null) {
            clearEditUserFields();
            return;
        }

        String sql = "SELECT user_id, username, password, NIS_NIP, nama, gender, alamat, email, nomer_hp, Role_role_id FROM Users WHERE user_id = ?";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, userIdToEdit);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                editUserIdField.setText(rs.getString("user_id"));
                editUsernameField.setText(rs.getString("username"));
                editPasswordField.setText(""); // Don't display actual password
                editNisNipField.setText(rs.getString("NIS_NIP"));
                editNameField.setText(rs.getString("nama"));
                editGenderChoiceBox.setValue(rs.getString("gender"));
                editAddressField.setText(rs.getString("alamat"));
                editEmailField.setText(rs.getString("email"));
                editPhoneNumberField.setText(rs.getString("nomer_hp"));
                String roleId = rs.getString("Role_role_id");
                editRoleChoiceBox.setValue(roleIdToNameMap.get(roleId));
            } else {
                clearEditUserFields();
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load user details", e.getMessage());
            e.printStackTrace();
            clearEditUserFields();
        }
    }

    private void clearEditUserFields() {
        editUserIdField.clear();
        editUsernameField.clear();
        editPasswordField.clear();
        editNisNipField.clear();
        editNameField.clear();
        editGenderChoiceBox.getSelectionModel().clearSelection();
        editAddressField.clear();
        editEmailField.clear();
        editPhoneNumberField.clear();
        editRoleChoiceBox.getSelectionModel().clearSelection();
    }

    @FXML
    void handleUpdateUser() {
        String userId = editUserIdField.getText();
        String username = editUsernameField.getText();
        String password = editPasswordField.getText(); // This will be empty if not re-entered
        String nisNip = editNisNipField.getText();
        String nama = editNameField.getText();
        String gender = editGenderChoiceBox.getValue();
        String alamat = editAddressField.getText();
        String email = editEmailField.getText();
        String nomerHp = editPhoneNumberField.getText();
        String selectedRoleName = editRoleChoiceBox.getValue();

        if (userId.isEmpty() || username.isEmpty() || nisNip.isEmpty() || nama.isEmpty() ||
                gender == null || alamat.isEmpty() || email.isEmpty() || nomerHp.isEmpty() || selectedRoleName == null) {
            AlertClass.WarningAlert("Input Error", "Missing Information", "Please fill in all user details for update.");
            return;
        }

        String roleId = roleNameToIdMap.get(selectedRoleName);
        if (roleId == null) {
            AlertClass.ErrorAlert("Role Error", "Invalid Role", "Selected role is not recognized.");
            return;
        }

        try (Connection con = DBS.getConnection()) {
            String sql;
            PreparedStatement stmt;

            if (!password.isEmpty()) { // If password field is not empty, hash and update password
                if (password.length() != 8) {
                    AlertClass.WarningAlert("Input Error", "Password Length Error", "Password must be exactly 8 characters long if changing.");
                    return;
                }
                String hashedPassword = HashGenerator.hash(password);
                sql = "UPDATE Users SET username = ?, password = ?, NIS_NIP = ?, nama = ?, gender = ?, alamat = ?, email = ?, nomer_hp = ?, Role_role_id = ? WHERE user_id = ?";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, hashedPassword);
                stmt.setString(3, nisNip);
                stmt.setString(4, nama);
                stmt.setString(5, gender);
                stmt.setString(6, alamat);
                stmt.setString(7, email);
                stmt.setString(8, nomerHp);
                stmt.setString(9, roleId);
                stmt.setString(10, userId);
            } else { // Otherwise, update without changing password
                sql = "UPDATE Users SET username = ?, NIS_NIP = ?, nama = ?, gender = ?, alamat = ?, email = ?, nomer_hp = ?, Role_role_id = ? WHERE user_id = ?";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, nisNip);
                stmt.setString(3, nama);
                stmt.setString(4, gender);
                stmt.setString(5, alamat);
                stmt.setString(6, email);
                stmt.setString(7, nomerHp);
                stmt.setString(8, roleId);
                stmt.setString(9, userId);
            }

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                AlertClass.InformationAlert("Success", "User Updated", "User '" + nama + "' has been updated successfully.");
                clearEditUserFields();
                loadUsersForEditDelete(); // Refresh the choice box
                loadStudentsForChoiceBox(); // Refresh student lists if user role changed to/from student
                loadWaliKelasForChoiceBox(); // Refresh wali kelas list if user role changed to/from wali kelas
                loadClassesForChoiceBox(); // Refresh classes if wali kelas info changed
                loadAllUsersToTable(); // Refresh all users table
            } else {
                AlertClass.ErrorAlert("Failed", "User Not Updated", "Failed to update user. User ID might not exist.");
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to update user", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleDeleteUser() {
        String selectedUserDisplay = editUserChoiceBox.getValue();
        if (selectedUserDisplay == null || selectedUserDisplay.isEmpty()) {
            AlertClass.WarningAlert("Selection Error", "No User Selected", "Please select a user to delete.");
            return;
        }

        String userIdToDelete = allUsersData.stream()
                .filter(p -> p.getKey().equals(selectedUserDisplay))
                .map(Pair::getValue)
                .findFirst()
                .orElse(null);

        if (userIdToDelete == null) {
            AlertClass.ErrorAlert("Retrieval Error", "Invalid User", "Could not retrieve user ID for deletion.");
            return;
        }

        Optional<ButtonType> result = AlertClass.ConfirmationAlert(
                "Confirm Deletion",
                "Delete User: " + selectedUserDisplay,
                "Are you sure you want to delete this user? This action cannot be undone."
        );

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try (Connection con = DBS.getConnection()) {
                // Delete from Student_Class_Enrollment first if the user is a student
                // The ON DELETE CASCADE on Student_Class_Enrollment_Users_FK should handle this,
                // but explicit deletion here ensures better control and immediate feedback.
                String deleteEnrollmentSql = "DELETE FROM Student_Class_Enrollment WHERE Users_user_id = ?";
                try (PreparedStatement delEnrollStmt = con.prepareStatement(deleteEnrollmentSql)) {
                    delEnrollStmt.setString(1, userIdToDelete);
                    delEnrollStmt.executeUpdate();
                }

                // Also delete from other tables that might have foreign key constraints
                // (e.g., Feedback, Pengumuman, Rapor, Absensi, Detail_Pengajar, Kelas where Users_user_id is a foreign key)
                // Need to consider the order of deletion to avoid FK violations.
                // Assuming ON DELETE NO ACTION is used, so manual deletion is required.
                // If a user is a Wali Kelas, related classes might need re-assignment or cascade deletion.
                // For simplicity, directly deleting related entries, assuming no complex re-assignment logic is needed for this admin page.

                // Delete related records in dependent tables first
                String[] dependentTables = {
                        "Feedback", "Pengumuman", "Rapor", "Absensi", "Detail_Pengajar", "Kelas"
                };

                for (String tableName : dependentTables) {
                    try {
                        String deleteSql = "DELETE FROM " + tableName + " WHERE Users_user_id = ?";
                        if (tableName.equals("Kelas")) {
                            // For Kelas, it's part of a composite primary key and foreign key.
                            // Need to be careful. If a Wali Kelas is deleted, their classes need handling.
                            // If a class's Wali Kelas is deleted, you might need to reassign the class or delete the class.
                            // For this implementation, let's assume cascade or manual re-assignment is outside scope
                            // and focus on simply trying to delete.
                            deleteSql = "DELETE FROM " + tableName + " WHERE Users_user_id = ?"; // Deletes classes where this user is the wali
                        }
                        try (PreparedStatement delStmt = con.prepareStatement(deleteSql)) {
                            delStmt.setString(1, userIdToDelete);
                            delStmt.executeUpdate();
                        }
                    } catch (SQLException e) {
                        System.err.println("Warning: Could not delete from " + tableName + " for user " + userIdToDelete + ": " + e.getMessage());
                        // Continue even if one table fails, unless it's critical for Users table deletion
                    }
                }

                // Finally, delete the user from the Users table
                String sql = "DELETE FROM Users WHERE user_id = ?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, userIdToDelete);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    AlertClass.InformationAlert("Success", "User Deleted", "User '" + selectedUserDisplay + "' has been deleted.");
                    clearEditUserFields();
                    loadUsersForEditDelete(); // Refresh the choice box
                    loadStudentsForChoiceBox(); // Refresh student lists if a student was deleted
                    loadWaliKelasForChoiceBox(); // Refresh wali kelas list if a wali was deleted
                    loadClassesForChoiceBox(); // Refresh classes if wali kelas deleted
                    loadAllUsersToTable(); // Refresh all users table
                } else {
                    AlertClass.ErrorAlert("Failed", "User Not Deleted", "Failed to delete user. User ID might not exist or there are other dependencies.");
                }
            } catch (SQLException e) {
                AlertClass.ErrorAlert("Database Error", "Failed to delete user", e.getMessage());
                e.printStackTrace();
            }
        }
    }


    private void loadSubjectsForChoiceBox() {
        subjectData.clear();
        scheduleSubjectChoiceBox.getItems().clear();
        String sql = "SELECT mapel_id, nama_mapel FROM Matpel";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int mapelId = rs.getInt("mapel_id");
                String namaMapel = rs.getString("nama_mapel");
                subjectData.add(new Pair<>(namaMapel, mapelId));
                scheduleSubjectChoiceBox.getItems().add(namaMapel);
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load subjects", e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadClassesForChoiceBox() {
        classData.clear();
        scheduleClassChoiceBox.getItems().clear();
        assignClassChoiceBox.getItems().clear();
        studentClassFilterChoiceBox.getItems().clear(); // Also clear for student filter

        // SQL to get class details and their wali kelas (homeroom teacher) name
        String sql = "SELECT k.kelas_id, k.nama_kelas, k.Users_user_id AS wali_id, u.nama AS wali_name " +
                "FROM Kelas k JOIN Users u ON k.Users_user_id = u.user_id";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int kelasId = rs.getInt("kelas_id");
                String namaKelas = rs.getString("nama_kelas");
                String waliId = rs.getString("wali_id");
                String waliName = rs.getString("wali_name");
                String display = namaKelas + " (Wali: " + waliName + ")";
                // Store a combined ID that includes both kelas_id and wali_id for lookup
                classData.add(new Pair<>(display, kelasId + "-" + waliId));
                scheduleClassChoiceBox.getItems().add(display);
                assignClassChoiceBox.getItems().add(display);
                studentClassFilterChoiceBox.getItems().add(display); // Populate for student filter
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load classes", e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadStudentsForChoiceBox() {
        studentData.clear();
        assignStudentChoiceBox.getItems().clear();
        String sql = "SELECT user_id, nama, NIS_NIP FROM Users WHERE Role_role_id = 'S'"; // Only students
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String userId = rs.getString("user_id");
                String nama = rs.getString("nama");
                String nisNip = rs.getString("NIS_NIP");
                String display = nama + " (NIS/NIP: " + nisNip + ")";
                studentData.add(new Pair<>(display, userId));
                assignStudentChoiceBox.getItems().add(display);
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load students", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleAddSchedule() {
        String hari = scheduleDayField.getText();
        String jamMulai = scheduleStartTimeField.getText();
        String jamSelesai = scheduleEndTimeField.getText();
        String selectedSubjectDisplay = scheduleSubjectChoiceBox.getValue();
        String selectedClassDisplay = scheduleClassChoiceBox.getValue();

        if (hari.isEmpty() || jamMulai.isEmpty() || jamSelesai.isEmpty() ||
                selectedSubjectDisplay == null || selectedClassDisplay == null) {
            AlertClass.WarningAlert("Input Error", "Missing Information", "Please fill in all schedule details.");
            return;
        }

        // Validate time format (simple check)
        if (!jamMulai.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$") || !jamSelesai.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")) {
            AlertClass.WarningAlert("Input Error", "Invalid Time Format", "Please enter time in HH:MM format (e.g., 08:00).");
            return;
        }

        Integer mapelId = subjectData.stream()
                .filter(p -> p.getKey().equals(selectedSubjectDisplay))
                .map(Pair::getValue)
                .findFirst()
                .orElse(null);

        String combinedClassId = classData.stream()
                .filter(p -> p.getKey().equals(selectedClassDisplay))
                .map(Pair::getValue)
                .findFirst()
                .orElse(null);

        if (mapelId == null || combinedClassId == null) {
            AlertClass.ErrorAlert("Selection Error", "Invalid Selection", "Could not retrieve ID for selected subject or class. Please try again.");
            return;
        }

        String[] ids = combinedClassId.split("-");
        int kelasId = Integer.parseInt(ids[0]);
        String waliUserId = ids[1];

        try (Connection con = DBS.getConnection()) {
            String sql = "INSERT INTO Jadwal (hari, jam_mulai, jam_selsai, Kelas_Users_user_id, Matpel_mapel_id, Kelas_kelas_id) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, hari);
            stmt.setString(2, jamMulai);
            stmt.setString(3, jamSelesai);
            stmt.setString(4, waliUserId); // Part of Kelas PK
            stmt.setInt(5, mapelId);
            stmt.setInt(6, kelasId); // Part of Kelas PK

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                AlertClass.InformationAlert("Success", "Schedule Added", "New schedule has been added successfully.");
                scheduleDayField.clear();
                scheduleStartTimeField.clear();
                scheduleEndTimeField.clear();
                scheduleSubjectChoiceBox.getSelectionModel().clearSelection();
                scheduleClassChoiceBox.getSelectionModel().clearSelection();
            } else {
                AlertClass.ErrorAlert("Failed", "Schedule Not Added", "Failed to add schedule to the database.");
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to add schedule", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleAddSubject() {
        String namaMapel = newSubjectNameField.getText();
        String category = newSubjectCategoryField.getText();

        if (namaMapel.isEmpty() || category.isEmpty()) {
            AlertClass.WarningAlert("Input Error", "Missing Information", "Please fill in both subject name and category.");
            return;
        }

        try (Connection con = DBS.getConnection()) {
            String sql = "INSERT INTO Matpel (nama_mapel, category) VALUES (?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, namaMapel);
            stmt.setString(2, category);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                AlertClass.InformationAlert("Success", "Subject Added", "New subject '" + namaMapel + "' has been added successfully.");
                newSubjectNameField.clear();
                newSubjectCategoryField.clear();
                loadSubjectsForChoiceBox(); // Refresh subjects in choice box
            } else {
                AlertClass.ErrorAlert("Failed", "Subject Not Added", "Failed to add subject to the database.");
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to add subject", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleAssignStudentToClass() {
        String selectedStudentDisplay = assignStudentChoiceBox.getValue();
        String selectedClassDisplay = assignClassChoiceBox.getValue();

        if (selectedStudentDisplay == null || selectedClassDisplay == null) {
            AlertClass.WarningAlert("Selection Error", "Missing Selection", "Please select both a student and a class.");
            return;
        }

        String studentUserId = studentData.stream()
                .filter(p -> p.getKey().equals(selectedStudentDisplay))
                .map(Pair::getValue)
                .findFirst()
                .orElse(null);

        String combinedClassId = classData.stream()
                .filter(p -> p.getKey().equals(selectedClassDisplay))
                .map(Pair::getValue)
                .findFirst()
                .orElse(null);


        if (studentUserId == null || combinedClassId == null) {
            AlertClass.ErrorAlert("Retrieval Error", "Invalid Selection", "Could not retrieve IDs for selected student or class.");
            return;
        }

        String[] ids = combinedClassId.split("-");
        int kelasId = Integer.parseInt(ids[0]);
        String waliUserId = ids[1];

        try (Connection con = DBS.getConnection()) {
            // Check if enrollment already exists
            String checkSql = "SELECT COUNT(*) FROM Student_Class_Enrollment WHERE Users_user_id = ? AND Kelas_kelas_id = ? AND Kelas_Users_user_id = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkSql);
            checkStmt.setString(1, studentUserId);
            checkStmt.setInt(2, kelasId);
            checkStmt.setString(3, waliUserId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                AlertClass.WarningAlert("Duplicate Entry", "Student Already Assigned", "This student is already assigned to this class.");
                return;
            }

            String insertSql = "INSERT INTO Student_Class_Enrollment (Users_user_id, Kelas_kelas_id, Kelas_Users_user_id) VALUES (?, ?, ?)";
            PreparedStatement insertStmt = con.prepareStatement(insertSql);
            insertStmt.setString(1, studentUserId);
            insertStmt.setInt(2, kelasId);
            insertStmt.setString(3, waliUserId);

            int rowsAffected = insertStmt.executeUpdate();
            if (rowsAffected > 0) {
                AlertClass.InformationAlert("Success", "Student Assigned", "Student assigned to class successfully.");
                assignStudentChoiceBox.getSelectionModel().clearSelection();
                assignClassChoiceBox.getSelectionModel().clearSelection();
                loadStudentsInSelectedClass(); // Refresh the table
            } else {
                AlertClass.ErrorAlert("Failed", "Assignment Failed", "Failed to assign student to class.");
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Assignment Failed", e.getMessage());
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
            String sql = "INSERT INTO Pengumuman (pengumuman, Users_user_id, waktu) VALUES (?, ?, NOW())"; // Added waktu column
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, announcementContent);
            stmt.setString(2, loggedInUserId); // Admin's user ID

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

    // --- Manage Students in Class Methods ---
    private void initStudentInClassTable() {
        studentNameInClassColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        nisNipInClassColumn.setCellValueFactory(new PropertyValueFactory<>("nisNip"));
        studentUserIdInClassColumn.setCellValueFactory(new PropertyValueFactory<>("userId")); // Hidden column for user_id
    }

    private void loadClassesForStudentFilter() {
        studentClassFilterChoiceBox.getItems().clear();
        // Re-use classData as it contains the necessary info
        for (Pair<String, String> classPair : classData) {
            studentClassFilterChoiceBox.getItems().add(classPair.getKey());
        }
    }

    @FXML
    void handleClassFilterSelection() {
        loadStudentsInSelectedClass();
    }

    private void loadStudentsInSelectedClass() {
        ObservableList<StudentEntry> studentsInClassList = FXCollections.observableArrayList();
        String selectedClassDisplay = studentClassFilterChoiceBox.getValue();

        if (selectedClassDisplay == null || selectedClassDisplay.isEmpty()) {
            studentInClassTableView.setItems(FXCollections.emptyObservableList());
            return;
        }

        int kelasId = -1;
        String waliUserId = null;
        for (Pair<String, String> p : classData) {
            if (p.getKey().equals(selectedClassDisplay)) {
                String[] ids = p.getValue().split("-");
                kelasId = Integer.parseInt(ids[0]);
                waliUserId = ids[1];
                break;
            }
        }

        if (kelasId == -1 || waliUserId == null) {
            AlertClass.ErrorAlert("Retrieval Error", "Invalid Class Selection", "Could not retrieve class ID for filtering.");
            return;
        }

        String sql = "SELECT u.user_id, u.nama, u.NIS_NIP FROM Users u " +
                "JOIN Student_Class_Enrollment sce ON u.user_id = sce.Users_user_id " +
                "WHERE sce.Kelas_kelas_id = ? AND sce.Kelas_Users_user_id = ? AND u.Role_role_id = 'S'";

        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, kelasId);
            stmt.setString(2, waliUserId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                studentsInClassList.add(new StudentEntry(
                        rs.getString("nama"),
                        rs.getString("NIS_NIP"),
                        rs.getString("user_id") // Pass user_id for deletion/editing
                ));
            }
            studentInClassTableView.setItems(studentsInClassList);
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load students in class", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleDeleteStudentFromClass() {
        StudentEntry selectedStudent = studentInClassTableView.getSelectionModel().getSelectedItem();
        String selectedClassDisplay = studentClassFilterChoiceBox.getValue();

        if (selectedStudent == null) {
            AlertClass.WarningAlert("Selection Error", "No Student Selected", "Please select a student to remove from the class.");
            return;
        }
        if (selectedClassDisplay == null || selectedClassDisplay.isEmpty()) {
            AlertClass.WarningAlert("Selection Error", "No Class Selected", "Please select a class filter.");
            return;
        }

        Optional<ButtonType> result = AlertClass.ConfirmationAlert(
                "Confirm Removal",
                "Remove Student from Class",
                "Are you sure you want to remove '" + selectedStudent.getStudentName() + "' from '" + selectedClassDisplay + "'?"
        );

        if (result.isPresent() && result.get() == ButtonType.OK) {
            int kelasId = -1;
            String waliUserId = null;
            for (Pair<String, String> p : classData) {
                if (p.getKey().equals(selectedClassDisplay)) {
                    String[] ids = p.getValue().split("-");
                    kelasId = Integer.parseInt(ids[0]);
                    waliUserId = ids[1];
                    break;
                }
            }

            if (kelasId == -1 || waliUserId == null) {
                AlertClass.ErrorAlert("Retrieval Error", "Invalid Class Data", "Could not retrieve class details for removal.");
                return;
            }

            try (Connection con = DBS.getConnection()) {
                String sql = "DELETE FROM Student_Class_Enrollment WHERE Users_user_id = ? AND Kelas_kelas_id = ? AND Kelas_Users_user_id = ?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, selectedStudent.getUserId());
                stmt.setInt(2, kelasId);
                stmt.setString(3, waliUserId);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    AlertClass.InformationAlert("Success", "Student Removed", "'" + selectedStudent.getStudentName() + "' has been removed from the class.");
                    loadStudentsInSelectedClass(); // Refresh table
                } else {
                    AlertClass.ErrorAlert("Failed", "Removal Failed", "Failed to remove student from class. Enrollment might not exist.");
                }
            } catch (SQLException e) {
                AlertClass.ErrorAlert("Database Error", "Removal Failed", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    void handleEditStudentInClass() {
        StudentEntry selectedStudent = studentInClassTableView.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            AlertClass.WarningAlert("Selection Error", "No Student Selected", "Please select a student to edit.");
            return;
        }

        // Switch to the "Manage Users" tab and select the user for editing
        adminTabPane.getSelectionModel().select(1); // Assuming "Manage Users" is the second tab (index 1)
        String userDisplayToSelect = selectedStudent.getStudentName() + " (" + selectedStudent.getUserId() + ")";
        editUserChoiceBox.setValue(userDisplayToSelect);
        handleUserSelectionForEditDelete(); // Load details for the selected user
    }

    // NEW: Class Creation Methods
    private void loadWaliKelasForChoiceBox() {
        waliKelasData.clear();
        newClassWaliKelasChoiceBox.getItems().clear(); // Clear existing items

        String sql = "SELECT user_id, nama FROM Users WHERE Role_role_id = 'W'"; // Filter for Wali Kelas role
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String userId = rs.getString("user_id");
                String nama = rs.getString("nama");
                String display = nama + " (" + userId + ")";
                waliKelasData.add(new Pair<>(display, userId));
                newClassWaliKelasChoiceBox.getItems().add(display); // Corrected line
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load Wali Kelas", e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadSemestersForChoiceBox() {
        semesterData.clear();
        newClassSemesterChoiceBox.getItems().clear();

        String sql = "SELECT semester_id, tahun_ajaran, semester FROM Semester";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int semesterId = rs.getInt("semester_id");
                String tahunAjaran = rs.getString("tahun_ajaran");
                String semesterName = rs.getString("semester");
                String display = tahunAjaran + " - " + semesterName;
                semesterData.add(new Pair<>(display, semesterId));
                newClassSemesterChoiceBox.getItems().add(display);
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load semesters", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleCreateClass() {
        String className = newClassNameField.getText();
        String classDescription = newClassDescriptionField.getText();
        String selectedWaliKelasDisplay = newClassWaliKelasChoiceBox.getValue();
        String selectedSemesterDisplay = newClassSemesterChoiceBox.getValue();

        if (className.isEmpty() || classDescription.isEmpty() || selectedWaliKelasDisplay == null || selectedSemesterDisplay == null) {
            AlertClass.WarningAlert("Input Error", "Missing Information", "Please fill in all class details.");
            return;
        }

        String waliKelasUserId = waliKelasData.stream()
                .filter(p -> p.getKey().equals(selectedWaliKelasDisplay))
                .map(Pair::getValue)
                .findFirst()
                .orElse(null);

        Integer semesterId = semesterData.stream()
                .filter(p -> p.getKey().equals(selectedSemesterDisplay))
                .map(Pair::getValue)
                .findFirst()
                .orElse(null);

        if (waliKelasUserId == null || semesterId == null) {
            AlertClass.ErrorAlert("Selection Error", "Invalid Selection", "Could not retrieve IDs for selected Wali Kelas or Semester.");
            return;
        }

        try (Connection con = DBS.getConnection()) {
            String sql = "INSERT INTO Kelas (nama_kelas, keterangan, Users_user_id, Semester_semester_id) VALUES (?, ?, ?, ?) RETURNING kelas_id";
            PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); // Request generated keys
            stmt.setString(1, className);
            stmt.setString(2, classDescription);
            stmt.setString(3, waliKelasUserId);
            stmt.setInt(4, semesterId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Retrieve the generated kelas_id
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int newKelasId = generatedKeys.getInt(1);
                    AlertClass.InformationAlert("Success", "Class Created", "New class '" + className + "' (ID: " + newKelasId + ") has been created successfully with Wali Kelas: " + selectedWaliKelasDisplay);
                } else {
                    AlertClass.InformationAlert("Success", "Class Created", "New class '" + className + "' has been created successfully.");
                }

                // Clear fields
                newClassNameField.clear();
                newClassDescriptionField.clear();
                newClassWaliKelasChoiceBox.getSelectionModel().clearSelection();
                newClassSemesterChoiceBox.getSelectionModel().clearSelection();
                loadClassesForChoiceBox(); // Refresh class list in other sections
            } else {
                AlertClass.ErrorAlert("Failed", "Class Not Created", "Failed to create class in the database.");
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to create class", e.getMessage());
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

    private void loadAllUsersToTable() {
        ObservableList<UserTableEntry> userList = FXCollections.observableArrayList();
        String sql = "SELECT u.user_id, u.username, u.NIS_NIP, u.nama, u.gender, u.alamat, u.email, u.nomer_hp, r.role_name " +
                "FROM Users u JOIN Role r ON u.Role_role_id = r.role_id ORDER BY u.nama";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
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
    void handleLogout() {
        try {
            HelloApplication.getInstance().changeScene("login-view.fxml", "Login Aplikasi", 1300, 600);
        } catch (IOException e) {
            AlertClass.ErrorAlert("Error", "Logout Failed", "Could not return to login screen.");
            e.printStackTrace();
        }
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

    // New Model Class for StudentEntry in TableView
    public static class StudentEntry {
        private final StringProperty studentName;
        private final StringProperty nisNip;
        private final StringProperty userId;

        public StudentEntry(String studentName, String nisNip, String userId) {
            this.studentName = new SimpleStringProperty(studentName);
            this.nisNip = new SimpleStringProperty(nisNip);
            this.userId = new SimpleStringProperty(userId);
        }

        public String getStudentName() {
            return studentName.get();
        }

        public StringProperty studentNameProperty() {
            return studentName;
        }

        public String getNisNip() {
            return nisNip.get();
        }

        public StringProperty nisNipProperty() {
            return nisNip;
        }

        public String getUserId() {
            return userId.get();
        }

        public StringProperty userIdProperty() {
            return userId;
        }
    }

    // NEW: Model Class for UserTableEntry (for "View All Users" table)
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