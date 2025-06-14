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
import org.example.projectbdpbogacor.model.*;
import org.example.projectbdpbogacor.Service.*; // Import all Service classes

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
    private ChoiceBox<String> filterbyRoleChoiceBox; // Existing filter by role
    @FXML
    private ChoiceBox<String> filterYearsChoiceBox; // NEW: Filter by years
    @FXML
    private Button searchUserButton; // NEW: Search button for user selection
    @FXML
    private ChoiceBox<String> selectUserForEditDeleteChoiceBox; // NEW: This is the actual user selection ChoiceBox for edit/delete
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
    private TextField editNomerHpField; // Changed from newPhoneNumberField for consistency with edit
    @FXML
    private ChoiceBox<String> editRoleChoiceBox;
    @FXML
    private Button deleteUserButton;
    @FXML
    private Button updateUserButton;


    // Manage Schedules & Subjects (This section will now be for schedules only, subjects moved to a new tab)
    @FXML
    private ChoiceBox<String> scheduleDayChoiceBox; // Changed from TextField to ChoiceBox for days
    @FXML
    private TextField scheduleStartTimeField;
    @FXML
    private TextField scheduleEndTimeField;
    @FXML
    private ChoiceBox<String> scheduleSubjectChoiceBox;
    @FXML
    private ChoiceBox<String> scheduleClassChoiceBox; // Displays "Nama Kelas - Nama Wali"


    // Assign Students to Classes (now integrated into Manage Students in Class)
    @FXML
    private ChoiceBox<String> assignStudentToClassChoiceBox; // Display: "Student Name (NIS/NIP)"
    @FXML
    private Button assignStudentToClassButton;

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
    @FXML
    private TableView<Pengumuman> announcementTable; // Table to view announcements // Changed to Pengumuman service class
    @FXML
    private TableColumn<Pengumuman, String> announcementWaktuColumn; // Column for announcement time // Changed to Pengumuman service class
    @FXML
    private TableColumn<Pengumuman, String> announcementContentColumn; // Column for announcement content // Changed to Pengumuman service class
    @FXML
    private Button createAnnouncementButton; // Assuming you have a button to create
    @FXML
    private Button updateAnnouncementButton; // Assuming you have a button to update
    @FXML
    private Button deleteAnnouncementButton; // Assuming you have a button to delete


    // Manage Class
    @FXML
    private TextField newClassNameField;
    @FXML
    private TextField newClassDescriptionField;
    @FXML
    private ChoiceBox<String> newClassWaliKelasChoiceBox; // Displays "Wali Kelas Name (ID)"
    @FXML
    private ChoiceBox<String> newClassSemesterChoiceBox; // Displays "Tahun Ajaran - Semester"
    @FXML
    private ChoiceBox<String> editClassChoiceBox; // For selecting a class to edit/delete
    @FXML
    private Button updateClassButton;
    @FXML
    private Button deleteClassButton;


    // View All Users Table (separate from Edit/Delete user section)
    @FXML
    private TableView<Users> allUsersTableView; // Changed to Users service class
    @FXML
    private TableColumn<Users, String> tableUserIdColumn; // Changed to Users service class
    @FXML
    private TableColumn<Users, String> tableUsernameColumn; // Changed to Users service class
    @FXML
    private TableColumn<Users, String> tableNisNipColumn; // Changed to Users service class
    @FXML
    private TableColumn<Users, String> tableNamaColumn; // Changed to Users service class
    @FXML
    private TableColumn<Users, String> tableGenderColumn; // Changed to Users service class
    @FXML
    private TableColumn<Users, String> tableAlamatColumn; // Changed to Users service class
    @FXML
    private TableColumn<Users, String> tableEmailColumn; // Changed to Users service class
    @FXML
    private TableColumn<Users, String> tableNomerHpColumn; // Changed to Users service class
    @FXML
    private TableColumn<Users, String> tableRoleColumn; // Changed to Users service class
    @FXML // Renamed to separate from edit/delete filter
    private ChoiceBox<String> allUsersFilterRoleChoiceBox;
    @FXML // Renamed to separate from edit/delete filter
    private TextField allUsersFilterNameField;


    // NEW FXML elements for "Manage Subjects" Tab
    @FXML
    private TextField newSubjectNameField;
    @FXML
    private ChoiceBox newCategoryChoiceBox; // Category for the new subject
    @FXML
    private Button addSubjectButton; // Assuming a button for adding subject
    @FXML
    private ChoiceBox<String> assignTeacherSubjectChoiceBox; // Subject to assign
    @FXML
    private ChoiceBox<String> assignTeacherClassChoiceBox; // Class for the assignment
    @FXML
    private ChoiceBox<String> assignTeacherGuruChoiceBox; // Teacher to assign
    @FXML
    private Button assignTeacherButton; // Assuming a button for assigning teacher
    @FXML
    private TableView<SubjectAssignmentEntry> subjectAssignmentTable; // Table to view assignments
    @FXML
    private TableColumn<SubjectAssignmentEntry, String> assignmentSubjectColumn;
    @FXML
    private TableColumn<SubjectAssignmentEntry, String> assignmentClassColumn;
    @FXML
    private TableColumn<SubjectAssignmentEntry, String> assignmentTeacherColumn;
    @FXML
    private Button deleteAssignmentButton;


    private String loggedInUserId;

    // Helper data structures for ChoiceBoxes
    private ObservableList<Pair<String, String>> classData = FXCollections.observableArrayList(); // Pair<Display, ID> where ID is "kelas_id-wali_user_id"
    private ObservableList<Pair<String, Integer>> subjectData = FXCollections.observableArrayList(); // Pair<Display, ID>
    private ObservableList<Pair<String, String>> studentData = FXCollections.observableArrayList(); // Pair<Display, ID>
    // This will now hold UserID -> DisplayName for selectUserForEditDeleteChoiceBox
    private Map<String, String> userDisplayMap = new HashMap<>();
    private ObservableList<Pair<String, String>> waliKelasData = FXCollections.observableArrayList(); // Pair<Display, WaliID> for new class
    private ObservableList<Pair<String, Integer>> semesterData = FXCollections.observableArrayList(); // Pair<Display, SemesterID> for new class
    private ObservableList<Pair<String, String>> guruData = FXCollections.observableArrayList(); // Pair<Display, GuruID> for assigning teachers
    private Map<String, String> roleNameToIdMap = new HashMap<>(); // Map role names to role IDs
    private Map<String, String> roleIdToNameMap = new HashMap<>(); // Map ID to role names
    // New map for editClassChoiceBox: Display -> Kelas_id-Wali_user_id
    private Map<String, String> editableClassesMap = new HashMap<>();


    @FXML
    void initialize() {
        loggedInUserId = HelloApplication.getInstance().getLoggedInUserId();
        loadAdminName();

        // Initialize ChoiceBoxes for new user
        newGenderChoiceBox.getItems().addAll("L", "P"); // L: Laki-laki, P: Perempuan
        newGenderChoiceBox.setValue("L"); // Default


        newCategoryChoiceBox.getItems().addAll("General", "Science"); // L: Laki-laki, P: Perempuan
        newCategoryChoiceBox.setValue("General");

        loadRolesForChoiceBox(); // Load roles into the newRoleChoiceBox
        newRoleChoiceBox.setValue("Siswa"); // Default to student for adding users


        // Add listener to newRoleChoiceBox to auto-fill newUserIdField
        newRoleChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                newUserIdField.setText(generateUserIdPrefix(newValue));
            }
        });


        // Initialize ChoiceBoxes for schedules and assignments
        scheduleDayChoiceBox.getItems().addAll("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"); // Initialize days
        scheduleDayChoiceBox.setValue("Senin"); // Default day
        loadSubjectsForChoiceBox();
        loadClassesForChoiceBox();
        loadStudentsForChoiceBox(); // Now also for assignStudentToClassChoiceBox

        // Initialize edit/delete user fields
        editGenderChoiceBox.getItems().addAll("L", "P");
        loadRolesForEditUserFilter(); // Load roles for the new filter choice box
        filterbyRoleChoiceBox.setValue("All Roles"); // Set default value for role filter
        loadYearsForFilter(); // NEW: Load years for the filter
        filterYearsChoiceBox.setValue("All Years"); // NEW: Set default for years filter

        // No direct call to loadUsersForEditDelete here. It will be called by searchUserButton.
        editRoleChoiceBox.getItems().addAll(roleNameToIdMap.keySet()); // Populate edit role choice box

        // Listener for the NEW selectUserForEditDeleteChoiceBox
        selectUserForEditDeleteChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                handleUserSelectionForEditDelete();
            } else {
                clearEditUserFields();
            }
        });

        // Add listeners for the new filter choice boxes
        filterbyRoleChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> filterUsersAndRefreshSearch());
        filterYearsChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> filterUsersAndRefreshSearch());

        // Listener for the search button



        // Initialize Student in Class Table
        initStudentInClassTable();
        loadClassesForStudentFilter(); // Load classes for the filter dropdown
        // Listener for Student in Class Table selection
        studentInClassTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            boolean isSelected = newValue != null;
            deleteStudentFromClassButton.setDisable(!isSelected);
            editStudentInClassButton.setDisable(!isSelected);
        });
        deleteStudentFromClassButton.setDisable(true); // Disable initially
        editStudentInClassButton.setDisable(true); // Disable initially


        // Initialize elements for Manage Class
        loadWaliKelasForChoiceBox();
        loadSemestersForChoiceBox();
        loadClassesForEdit(); // Load classes for editing
        editClassChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                handleClassSelectionForEdit();
                updateClassButton.setDisable(false); // Enable update
                deleteClassButton.setDisable(false); // Enable delete
            } else {
                clearClassEditFields();
                updateClassButton.setDisable(true); // Disable update
                deleteClassButton.setDisable(true); // Disable delete
            }
        });
        updateClassButton.setDisable(true); // Disable initially
        deleteClassButton.setDisable(true); // Disable initially


        // Initialize all users table
        initAllUsersTable();
        allUsersFilterRoleChoiceBox.getItems().addAll("All Roles"); // Add "All Roles" option
        allUsersFilterRoleChoiceBox.getItems().addAll(roleNameToIdMap.keySet()); // Add actual roles
        allUsersFilterRoleChoiceBox.setValue("All Roles"); // Default selection

        // NEW: Initialize elements for "Manage Subjects" Tab
        initSubjectAssignmentTable();
        loadGuruForChoiceBox(); // Load Guru for assignment
        // These will be refreshed when the tab is selected
        // loadSubjectsForAssignmentChoiceBox(); // Re-use existing loadSubjectsForChoiceBox
        // loadClassesForAssignmentChoiceBox(); // Re-use existing loadClassesForChoiceBox

        // Listener for Subject Assignment Table selection
        subjectAssignmentTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            boolean isSelected = newValue != null;
            deleteAssignmentButton.setDisable(!isSelected);
        });
        deleteAssignmentButton.setDisable(true); // Disable initially


        // Initialize announcement table
        initAnnouncementTable();
        // Add listener for announcement table selection
        announcementTable.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            if (newSelection != null) {
                announcementTextArea.setText(newSelection.getPengumumanContent()); // Populate text area with selected announcement content // Use getPengumumanContent
                updateAnnouncementButton.setDisable(false); // Enable update
                deleteAnnouncementButton.setDisable(false); // Enable delete
                createAnnouncementButton.setDisable(true); // Disable create
            } else {
                announcementTextArea.clear(); // Clear text area if no selection
                updateAnnouncementButton.setDisable(true); // Disable update
                deleteAnnouncementButton.setDisable(true); // Disable delete
                createAnnouncementButton.setDisable(false); // Enable create
            }
        });
        updateAnnouncementButton.setDisable(true); // Disable initially
        deleteAnnouncementButton.setDisable(true); // Disable initially


        // Add listeners to tabs to load data when selected
        adminTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null) {
                switch (newTab.getText()) {
                    case "Manage Schedules": // Renamed from "Manage Schedules & Subjects"
                        loadSubjectsForChoiceBox(); // Refresh subjects
                        loadClassesForChoiceBox();  // Refresh classes
                        break;
                    case "Manage Users":
                        // Ensure filters and search are applied when tab is selected
                        filterUsersAndRefreshSearch();
                        selectUserForEditDeleteChoiceBox.getSelectionModel().clearSelection(); // Clear selection
                        clearEditUserFields(); // Clear selected user fields when switching to this tab
                        updateUserButton.setDisable(true); // Ensure disabled on tab change
                        deleteUserButton.setDisable(true); // Ensure disabled on tab change
                        break;
                    case "Manage Students in Class": // This tab now includes student assignment
                        loadStudentsForChoiceBox(); // Refresh students
                        loadClassesForStudentFilter(); // Refresh classes for filter and assignment
                        studentInClassTableView.getItems().clear(); // Clear table until a class is selected
                        deleteStudentFromClassButton.setDisable(true); // Ensure disabled on tab change
                        editStudentInClassButton.setDisable(true); // Ensure disabled on tab change
                        break;
                    case "Manage Classes":
                        loadWaliKelasForChoiceBox(); // Refresh wali kelas list
                        loadSemestersForChoiceBox(); // Refresh semester list
                        loadClassesForEdit(); // Refresh classes for edit/delete
                        clearClassEditFields();
                        updateClassButton.setDisable(true); // Ensure disabled on tab change
                        deleteClassButton.setDisable(true); // Ensure disabled on tab change
                        break;
                    case "View All Users":
                        loadAllUsersToTable(allUsersFilterRoleChoiceBox.getValue(), allUsersFilterNameField.getText()); // Load all users to the table with current filters
                        break;
                    case "Manage Subjects": // NEW: Handle "Manage Subjects" tab selection
                        loadSubjectsForChoiceBox(); // Populate assignTeacherSubjectChoiceBox
                        loadClassesForChoiceBox(); // Populate assignTeacherClassChoiceBox
                        loadGuruForChoiceBox(); // Populate assignTeacherGuruChoiceBox
                        loadSubjectAssignments(); // Load existing assignments
                        deleteAssignmentButton.setDisable(true); // Ensure disabled on tab change
                        break;
                    case "Announcements": // Handle "Announcements" tab selection
                        loadAnnouncements(); // Load announcements when the tab is selected
                        updateAnnouncementButton.setDisable(true); // Ensure disabled on tab change
                        deleteAnnouncementButton.setDisable(true); // Ensure disabled on tab change
                        createAnnouncementButton.setDisable(false); // Ensure enabled on tab change
                        break;
                }
            }
        });

        // Add listeners for filter fields for the "View All Users" table
        allUsersFilterRoleChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> handleFilterAllUsersTable());
        allUsersFilterNameField.textProperty().addListener((observable, oldValue, newValue) -> handleFilterAllUsersTable());

        // Load all users initially for "View All Users" table
        loadAllUsersToTable(allUsersFilterRoleChoiceBox.getValue(), allUsersFilterNameField.getText());
        loadAnnouncements(); // Load announcements initially

        // Set initial state for Manage Users (Edit/Delete) buttons
        updateUserButton.setDisable(true);
        deleteUserButton.setDisable(true);
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
    // NEW: Load roles specifically for the edit user filter choice box
    private void loadRolesForEditUserFilter() {
        filterbyRoleChoiceBox.getItems().clear();
        filterbyRoleChoiceBox.getItems().add("All Roles");
        filterbyRoleChoiceBox.getItems().addAll(roleNameToIdMap.keySet());
    }

    // NEW: Load years for the filterYearsChoiceBox
    private void loadYearsForFilter() {
        filterYearsChoiceBox.getItems().clear();
        filterYearsChoiceBox.getItems().add("All Years"); // Option to show all years

        Set<String> years = new HashSet<>();
        String sql = "SELECT user_id FROM Users";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String userId = rs.getString("user_id");
                if (userId != null && userId.length() >= 3) {
                    years.add(userId.substring(1, 3)); // Extract the two digits after the role prefix
                }
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load years for filter", e.getMessage());
            e.printStackTrace();
        }
        // Sort years for consistent display
        SortedSet<String> sortedYears = new TreeSet<>(years);
        filterYearsChoiceBox.getItems().addAll(sortedYears);
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
                filterUsersAndRefreshSearch(); // Refresh list of users for edit/delete
                loadStudentsForChoiceBox(); // Refresh student lists if user role changed to/from student
                loadWaliKelasForChoiceBox(); // Refresh wali kelas for new class
                loadGuruForChoiceBox(); // Refresh guru for subject assignment
                loadAllUsersToTable(allUsersFilterRoleChoiceBox.getValue(), allUsersFilterNameField.getText()); // Refresh all users table
                loadSubjectAssignments(); // Refresh subject assignments
                loadYearsForFilter(); // Refresh years filter
            } else {
                AlertClass.ErrorAlert("Failed", "User Not Added", "Failed to add user to the database.");
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to add user", e.getMessage());
            e.printStackTrace();
        }
    }

    // This method is now called by the search button or filter changes
    private void loadUsersForEditDelete(String roleFilter, String yearFilter) {
        userDisplayMap.clear(); // Clear the map that stores user ID to display name
        selectUserForEditDeleteChoiceBox.getItems().clear(); // Clear the ChoiceBox for user selection

        StringBuilder sqlBuilder = new StringBuilder("SELECT user_id, nama, NIS_NIP, Role_role_id FROM Users WHERE 1=1 ");

        if (roleFilter != null && !roleFilter.equals("All Roles")) {
            String roleId = roleNameToIdMap.get(roleFilter);
            if (roleId != null) {
                sqlBuilder.append("AND Role_role_id = ? ");
            }
        }
        if (yearFilter != null && !yearFilter.equals("All Years")) {
            sqlBuilder.append("AND SUBSTRING(user_id, 2, 2) = ? "); // Filter by the 2nd and 3rd characters for year ID
        }

        sqlBuilder.append("ORDER BY user_id"); // Order by user_id to group by year

        try (Connection con = DBS.getConnection()) {
            PreparedStatement stmt = con.prepareStatement(sqlBuilder.toString());

            int paramIndex = 1;
            if (roleFilter != null && !roleFilter.equals("All Roles")) {
                String roleId = roleNameToIdMap.get(roleFilter);
                if (roleId != null) {
                    stmt.setString(paramIndex++, roleId);
                }
            }
            if (yearFilter != null && !yearFilter.equals("All Years")) {
                stmt.setString(paramIndex++, yearFilter);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String userId = rs.getString("user_id");
                String nama = rs.getString("nama");
                String nisNip = rs.getString("NIS_NIP");
                String display = userId + " - " + nama + " (NIS/NIP: " + nisNip + ")";
                userDisplayMap.put(userId, display); // Store actual user_id to display string
                selectUserForEditDeleteChoiceBox.getItems().add(display);
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load users for edit/delete", e.getMessage());
            e.printStackTrace();
        }
    }

    // New method to trigger loading users based on filters and search input
    @FXML
    void filterUsersAndRefreshSearch() {
        String selectedRole = filterbyRoleChoiceBox.getValue();
        String selectedYear = filterYearsChoiceBox.getValue();
        loadUsersForEditDelete(selectedRole, selectedYear);
        clearEditUserFields(); // Clear fields when filters or search change
        selectUserForEditDeleteChoiceBox.getSelectionModel().clearSelection(); // Clear selection
        updateUserButton.setDisable(true); // Disable update
        deleteUserButton.setDisable(true); // Disable delete
    }

    @FXML
    void handleUserSelectionForEditDelete() {
        String selectedUserDisplay = selectUserForEditDeleteChoiceBox.getValue();
        if (selectedUserDisplay == null || selectedUserDisplay.isEmpty()) {
            clearEditUserFields();
            updateUserButton.setDisable(true);
            deleteUserButton.setDisable(true);
            return;
        }

        // Extract the actual user_id from the display format "User ID - Name (NIS/NIP)"
        String userIdToEdit = selectedUserDisplay.substring(0, selectedUserDisplay.indexOf(" - ")).trim();


        if (userIdToEdit == null || userIdToEdit.isEmpty()) {
            clearEditUserFields();
            updateUserButton.setDisable(true);
            deleteUserButton.setDisable(true);
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
                editNomerHpField.setText(rs.getString("nomer_hp"));
                String roleId = rs.getString("Role_role_id");
                editRoleChoiceBox.setValue(roleIdToNameMap.get(roleId));
                updateUserButton.setDisable(false); // Enable update
                deleteUserButton.setDisable(false); // Enable delete
            } else {
                clearEditUserFields();
                updateUserButton.setDisable(true);
                deleteUserButton.setDisable(true);
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load user details", e.getMessage());
            e.printStackTrace();
            clearEditUserFields();
            updateUserButton.setDisable(true);
            deleteUserButton.setDisable(true);
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
        editNomerHpField.clear();
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
        String nomerHp = editNomerHpField.getText();
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
                sql = "UPDATE Users SET username = ?, password = ?, NIS_NIP = ?, nama = ?, gender = ?, ?, email = ?, nomer_hp = ?, Role_role_id = ? WHERE user_id = ?";
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
                filterUsersAndRefreshSearch(); // Refresh the choice box with current filters
                loadStudentsForChoiceBox(); // Refresh student lists if user role changed to/from student
                loadWaliKelasForChoiceBox(); // Refresh wali kelas list if user role changed to/from wali kelas
                loadGuruForChoiceBox(); // Refresh guru list if user role changed to/from guru
                loadClassesForChoiceBox(); // Refresh classes if wali kelas info changed
                loadAllUsersToTable(allUsersFilterRoleChoiceBox.getValue(), allUsersFilterNameField.getText()); // Refresh all users table
                loadSubjectAssignments(); // Refresh subject assignments if a guru was updated
                updateUserButton.setDisable(true); // Disable update after success
                deleteUserButton.setDisable(true); // Disable delete after success
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
        String selectedUserDisplay = selectUserForEditDeleteChoiceBox.getValue();
        if (selectedUserDisplay == null || selectedUserDisplay.isEmpty()) {
            AlertClass.WarningAlert("Selection Error", "No User Selected", "Please select a user to delete.");
            return;
        }

        // Extract the actual user_id from the display format "User ID - Name (NIS/NIP)"
        String userIdToDelete = selectedUserDisplay.substring(0, selectedUserDisplay.indexOf(" - ")).trim();

        if (userIdToDelete == null || userIdToDelete.isEmpty()) {
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
                // Delete from Enrollment first if the user is a student
                // The ON DELETE CASCADE on Enrollment_Users_FK should handle this,
                // but explicit deletion here ensures better control and immediate feedback.
                String deleteEnrollmentSql = "DELETE FROM Enrollment WHERE Users_user_id = ?";
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
                    filterUsersAndRefreshSearch(); // Refresh the choice box with current filters
                    loadStudentsForChoiceBox(); // Refresh student lists if a student was deleted
                    loadWaliKelasForChoiceBox(); // Refresh wali kelas list if a wali was deleted
                    loadGuruForChoiceBox(); // Refresh guru for subject assignment if a guru was deleted
                    loadClassesForChoiceBox(); // Refresh classes if wali kelas deleted
                    loadAllUsersToTable(allUsersFilterRoleChoiceBox.getValue(), allUsersFilterNameField.getText()); // Refresh all users table
                    loadSubjectAssignments(); // Refresh subject assignments
                    updateUserButton.setDisable(true); // Disable update after success
                    deleteUserButton.setDisable(true); // Disable delete after success
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
        assignTeacherSubjectChoiceBox.getItems().clear(); // For new tab
        String sql = "SELECT mapel_id, nama_mapel FROM Matpel";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int mapelId = rs.getInt("mapel_id");
                String namaMapel = rs.getString("nama_mapel");
                subjectData.add(new Pair<>(namaMapel, mapelId));
                scheduleSubjectChoiceBox.getItems().add(namaMapel);
                assignTeacherSubjectChoiceBox.getItems().add(namaMapel); // For new tab
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load subjects", e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadClassesForChoiceBox() {
        classData.clear();
        scheduleClassChoiceBox.getItems().clear();
        // assignClassChoiceBox.getItems().clear(); // Removed as it's merged
        studentClassFilterChoiceBox.getItems().clear(); // Also clear for student filter
        assignTeacherClassChoiceBox.getItems().clear(); // For new tab

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
                // assignClassChoiceBox.getItems().add(display); // Removed as it's merged
                studentClassFilterChoiceBox.getItems().add(display); // Populate for student filter
                assignTeacherClassChoiceBox.getItems().add(display); // For new tab
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load classes", e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadStudentsForChoiceBox() {
        studentData.clear();
        assignStudentToClassChoiceBox.getItems().clear(); // Updated for merged tab
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
                assignStudentToClassChoiceBox.getItems().add(display); // Updated for merged tab
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load students", e.getMessage());
            e.printStackTrace();
        }
    }

    // NEW: Load Guru for the assignment choice box
    private void loadGuruForChoiceBox() {
        guruData.clear();
        assignTeacherGuruChoiceBox.getItems().clear();
        String sql = "SELECT user_id, nama FROM Users WHERE Role_role_id = 'G'"; // Only Guru
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String userId = rs.getString("user_id");
                String nama = rs.getString("nama");
                String display = nama + " (" + userId + ")";
                guruData.add(new Pair<>(display, userId));
                assignTeacherGuruChoiceBox.getItems().add(display);
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load teachers (Guru)", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleAddSchedule() {
        String hari = scheduleDayChoiceBox.getValue(); // Get day from ChoiceBox
        String jamMulai = scheduleStartTimeField.getText();
        String jamSelesai = scheduleEndTimeField.getText();
        String selectedSubjectDisplay = scheduleSubjectChoiceBox.getValue();
        String selectedClassDisplay = scheduleClassChoiceBox.getValue();

        if (hari == null || hari.isEmpty() || jamMulai.isEmpty() || jamSelesai.isEmpty() ||
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
                scheduleDayChoiceBox.getSelectionModel().clearSelection(); // Clear selected day
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

    // Moved to the new "Manage Subjects" tab
    @FXML
    void handleAddSubject() {
        String namaMapel = newSubjectNameField.getText();
        String category = (String) newCategoryChoiceBox.getValue();

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
                newSubjectNameField.clear();
                loadSubjectsForChoiceBox(); // Refresh subjects in choice box for all tabs
                loadSubjectAssignments(); // Refresh assignments table
            } else {
                AlertClass.ErrorAlert("Failed", "Subject Not Added", "Failed to add subject to the database.");
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to add subject", e.getMessage());
            e.printStackTrace();
        }
    }

    // NEW: Assign Teacher to Subject
    @FXML
    void handleAssignTeacherToSubject() {
        String selectedSubjectDisplay = assignTeacherSubjectChoiceBox.getValue();
        String selectedClassDisplay = assignTeacherClassChoiceBox.getValue();
        String selectedGuruDisplay = assignTeacherGuruChoiceBox.getValue();

        if (selectedSubjectDisplay == null || selectedClassDisplay == null || selectedGuruDisplay == null) {
            AlertClass.WarningAlert("Input Error", "Missing Information", "Please select a subject, class, and teacher.");
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

        String guruUserId = guruData.stream()
                .filter(p -> p.getKey().equals(selectedGuruDisplay))
                .map(Pair::getValue)
                .findFirst()
                .orElse(null);

        if (mapelId == null || combinedClassId == null || guruUserId == null) {
            AlertClass.ErrorAlert("Selection Error", "Invalid Selection", "Could not retrieve IDs for selected subject, class, or teacher.");
            return;
        }

        String[] ids = combinedClassId.split("-");
        int kelasId = Integer.parseInt(ids[0]);
        String waliKelasId = ids[1]; // This is the Kelas.Users_user_id (Wali Kelas's ID)

        try (Connection con = DBS.getConnection()) {
            // Check for existing assignment to prevent duplicates
            String checkSql = "SELECT COUNT(*) FROM Detail_Pengajar WHERE Users_user_id = ? AND Matpel_mapel_id = ? AND Kelas_Users_user_id = ? AND Kelas_kelas_id = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkSql);
            checkStmt.setString(1, guruUserId);
            checkStmt.setInt(2, mapelId);
            checkStmt.setString(3, waliKelasId);
            checkStmt.setInt(4, kelasId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                AlertClass.WarningAlert("Duplicate Assignment", "Teacher Already Assigned", "This teacher is already assigned to this subject in this class.");
                return;
            }

            String sql = "INSERT INTO Detail_Pengajar (Users_user_id, Matpel_mapel_id, Kelas_Users_user_id, Kelas_kelas_id) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, guruUserId);
            stmt.setInt(2, mapelId);
            stmt.setString(3, waliKelasId);
            stmt.setInt(4, kelasId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                AlertClass.InformationAlert("Success", "Assignment Added", "Teacher assigned to subject in class successfully.");
                assignTeacherSubjectChoiceBox.getSelectionModel().clearSelection();
                assignTeacherClassChoiceBox.getSelectionModel().clearSelection();
                assignTeacherGuruChoiceBox.getSelectionModel().clearSelection();
                loadSubjectAssignments(); // Refresh the table
            } else {
                AlertClass.ErrorAlert("Failed", "Assignment Failed", "Failed to assign teacher to subject in class.");
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to assign teacher", e.getMessage());
            e.printStackTrace();
        }
    }

    // NEW: Initialize Subject Assignment Table
    private void initSubjectAssignmentTable() {
        assignmentSubjectColumn.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        assignmentClassColumn.setCellValueFactory(new PropertyValueFactory<>("className"));
        assignmentTeacherColumn.setCellValueFactory(new PropertyValueFactory<>("teacherName"));
    }

    // NEW: Load Subject Assignments
    @FXML
    void loadSubjectAssignments() {
        ObservableList<SubjectAssignmentEntry> assignmentList = FXCollections.observableArrayList();
        String sql = "SELECT m.nama_mapel, k.nama_kelas, u_guru.nama AS guru_name, dp.Users_user_id AS guru_id, dp.Matpel_mapel_id, dp.Kelas_Users_user_id AS kelas_wali_id, dp.Kelas_kelas_id " +
                "FROM Detail_Pengajar dp " +
                "JOIN Matpel m ON dp.Matpel_mapel_id = m.mapel_id " +
                "JOIN Kelas k ON dp.Kelas_kelas_id = k.kelas_id AND dp.Kelas_Users_user_id = k.Users_user_id " +
                "JOIN Users u_guru ON dp.Users_user_id = u_guru.user_id " +
                "ORDER BY m.nama_mapel, k.nama_kelas, u_guru.nama";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                assignmentList.add(new SubjectAssignmentEntry(
                        rs.getString("nama_mapel"),
                        rs.getString("nama_kelas"),
                        rs.getString("guru_name"),
                        rs.getString("guru_id"),
                        rs.getInt("Matpel_mapel_id"),
                        rs.getString("kelas_wali_id"),
                        rs.getInt("Kelas_kelas_id")
                ));
            }
            subjectAssignmentTable.setItems(assignmentList);
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load subject assignments", e.getMessage());
            e.printStackTrace();
        }
    }

    // NEW: Delete Subject Assignment
    @FXML
    void handleDeleteSubjectAssignment() {
        SubjectAssignmentEntry selectedAssignment = subjectAssignmentTable.getSelectionModel().getSelectedItem();
        if (selectedAssignment == null) {
            AlertClass.WarningAlert("Selection Error", "No Assignment Selected", "Please select an assignment to delete.");
            return;
        }

        Optional<ButtonType> result = AlertClass.ConfirmationAlert(
                "Confirm Deletion",
                "Delete Subject Assignment",
                "Are you sure you want to delete the assignment of " + selectedAssignment.getTeacherName() +
                        " to " + selectedAssignment.getSubjectName() + " in " + selectedAssignment.getClassName() + "?"
        );

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try (Connection con = DBS.getConnection()) {
                String sql = "DELETE FROM Detail_Pengajar WHERE Users_user_id = ? AND Matpel_mapel_id = ? AND Kelas_Users_user_id = ? AND Kelas_kelas_id = ?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, selectedAssignment.getTeacherId());
                stmt.setInt(2, selectedAssignment.getSubjectId());
                stmt.setString(3, selectedAssignment.getKelasWaliId());
                stmt.setInt(4, selectedAssignment.getKelasId());

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    AlertClass.InformationAlert("Success", "Assignment Deleted", "Subject assignment has been deleted.");
                    loadSubjectAssignments(); // Refresh the table
                    deleteAssignmentButton.setDisable(true); // Disable delete after success
                } else {
                    AlertClass.ErrorAlert("Failed", "Deletion Failed", "Failed to delete subject assignment. It might not exist.");
                }
            } catch (SQLException e) {
                AlertClass.ErrorAlert("Database Error", "Failed to delete assignment", e.getMessage());
                e.printStackTrace();
            }
        }
    }


    // MERGED: Assign Student to Class & Manage Students in Class
    @FXML
    void handleAssignStudentToClass() {
        String selectedStudentDisplay = assignStudentToClassChoiceBox.getValue();
        String selectedClassDisplay = studentClassFilterChoiceBox.getValue(); // Use the class from the filter choice box

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
            String checkSql = "SELECT COUNT(*) FROM Enrollment WHERE Users_user_id = ? AND Kelas_kelas_id = ? AND Kelas_Users_user_id = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkSql);
            checkStmt.setString(1, studentUserId);
            checkStmt.setInt(2, kelasId);
            checkStmt.setString(3, waliUserId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                AlertClass.WarningAlert("Duplicate Entry", "Student Already Assigned", "This student is already assigned to this class.");
                return;
            }

            // Changed table name from Student_Class_Enrollment to Enrollment
            String insertSql = "INSERT INTO Enrollment (Users_user_id, Kelas_kelas_id, Kelas_Users_user_id) VALUES (?, ?, ?)";
            PreparedStatement insertStmt = con.prepareStatement(insertSql);
            insertStmt.setString(1, studentUserId);
            insertStmt.setInt(2, kelasId);
            insertStmt.setString(3, waliUserId);

            int rowsAffected = insertStmt.executeUpdate();
            if (rowsAffected > 0) {
                AlertClass.InformationAlert("Success", "Student Assigned", "Student assigned to class successfully.");
                assignStudentToClassChoiceBox.getSelectionModel().clearSelection(); // Clear the assignment student choicebox
                loadStudentsInSelectedClass(); // Refresh the table of students in the selected class
            } else {
                AlertClass.ErrorAlert("Failed", "Assignment Failed", "Failed to assign student to class.");
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Assignment Failed", e.getMessage());
            e.printStackTrace();
        }
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
            // Ensure the prefix is only added once at creation
            String rolePrefix = (userInfo.getKey() != null) ? "[" + userInfo.getKey().toUpperCase() + "] " : "";
            String namePrefix = (userInfo.getValue() != null) ? userInfo.getValue() + ": " : "";

            // Construct the final content by prepending the role and name once.
            // If the user somehow typed the prefix in the textarea, it will still be there.
            // This method assumes the user inputs raw content.
            String finalAnnouncementContent = rolePrefix + namePrefix + announcementContent;

            String sql = "INSERT INTO Pengumuman (pengumuman, Users_user_id, waktu) VALUES (?, ?, NOW())";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, finalAnnouncementContent);
            stmt.setString(2, loggedInUserId);

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
        announcementWaktuColumn.setCellValueFactory(new PropertyValueFactory<>("waktu")); // Property name from Pengumuman service
        announcementContentColumn.setCellValueFactory(new PropertyValueFactory<>("pengumumanContent")); // Property name from Pengumuman service
    }

    private void loadAnnouncements() {
        ObservableList<Pengumuman> announcementList = FXCollections.observableArrayList(); // Changed to Pengumuman service
        String sql = "SELECT pengumuman_id, pengumuman, waktu, Users_user_id FROM Pengumuman ORDER BY waktu DESC"; // Fetch Users_user_id
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Timestamp timestamp = rs.getTimestamp("waktu");


                String originalContent = rs.getString("pengumuman");
                String userIdOfPoster = rs.getString("Users_user_id"); // Get the user ID of the poster

                // Check if content already contains the prefix
                // A simple heuristic: check for "[ROLE] Name:" at the beginning
                boolean hasPrefix = originalContent.matches("^\\[.+\\]\\s*[^:]+:\\s*");
                String displayContent = originalContent;

                if (!hasPrefix) {
                    // If no prefix, try to get the poster's info and prepend it
                    try {
                        Pair<String, String> posterInfo = getUserRoleAndName(userIdOfPoster);
                        if (posterInfo.getKey() != null && posterInfo.getValue() != null) {
                            displayContent = "[" + posterInfo.getKey().toUpperCase() + "] " + posterInfo.getValue() + ": " + originalContent;
                        }
                    } catch (SQLException e) {
                        System.err.println("Error fetching poster info for announcement ID " + rs.getInt("pengumuman_id") + ": " + e.getMessage());
                        // Fallback to original content if poster info can't be fetched
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

        // Pattern to identify the prefix: "[ROLE] Name: "
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^\\[.+\\]\\s*[^:]+:\\s*");
        java.util.regex.Matcher matcher = pattern.matcher(originalFullContent);

        String currentPrefix = "";
        if (matcher.find()) {
            currentPrefix = matcher.group(0); // Capture the existing prefix
            // Now, strip the prefix from the original content to get the actual "raw" message
            // This ensures that the user's input in the textarea is treated as the new raw message
            // and the prefix is re-added based on the current user if necessary.
            // This also handles the case where the user might have copied the prefix into the textarea.
            originalFullContent = originalFullContent.substring(currentPrefix.length());
        }

        // If the user's input `updatedContentRaw` starts with the `currentPrefix`,
        // it means they might have copied the prefixed text back into the textarea.
        // In that case, we should strip the prefix from their input before re-adding.
        String contentWithoutPotentialPrefix = updatedContentRaw;
        java.util.regex.Matcher updatedContentMatcher = pattern.matcher(updatedContentRaw);
        if (updatedContentMatcher.find() && updatedContentMatcher.group(0).equals(currentPrefix)) {
            contentWithoutPotentialPrefix = updatedContentRaw.substring(currentPrefix.length());
        }

        // Reconstruct the final content with the correct prefix and the new raw content.
        // Get the current user's info to form the prefix for the update.
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

        // Combine the current poster's prefix with the actual content typed by the user.
        finalContentToSave = rolePrefix + namePrefix + contentWithoutPotentialPrefix;


        int pengumumanId = selectedAnnouncement.getPengumumanId(); // Property name from Pengumuman service

        try (Connection con = DBS.getConnection()) {
            String sql = "UPDATE Pengumuman SET pengumuman = ?, waktu = NOW() WHERE pengumuman_id = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, finalContentToSave); // Use the content with preserved/new prefix
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

        // Changed table name from Student_Class_Enrollment to Enrollment
        String sql = "SELECT u.user_id, u.nama, u.NIS_NIP FROM Users u " +
                "JOIN Enrollment sce ON u.user_id = sce.Users_user_id " +
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
                // Changed table name from Student_Class_Enrollment to Enrollment
                String sql = "DELETE FROM Enrollment WHERE Users_user_id = ? AND Kelas_kelas_id = ? AND Kelas_Users_user_id = ?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, selectedStudent.getUserId());
                stmt.setInt(2, kelasId);
                stmt.setString(3, waliUserId);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    AlertClass.InformationAlert("Success", "Student Removed", "'" + selectedStudent.getStudentName() + "' has been removed from the class.");
                    loadStudentsInSelectedClass(); // Refresh table
                    deleteStudentFromClassButton.setDisable(true); // Disable after success
                    editStudentInClassButton.setDisable(true); // Disable after success
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
        // Adjust the display format to match what loadUsersForEditDelete creates for selection
        // The format is now "USER_ID - Full Name (NIS/NIP: XXX)"
        String userIdToSelect = selectedStudent.getUserId();
        String userDisplay = userDisplayMap.get(userIdToSelect); // Retrieve the full display string from the map
        if (userDisplay != null) {
            selectUserForEditDeleteChoiceBox.setValue(userDisplay);
            handleUserSelectionForEditDelete(); // Load details for the selected user
        } else {
            AlertClass.ErrorAlert("Error", "User Not Found", "Could not find selected student in the user list for editing.");
        }
    }

    // NEW: Class Management Methods (Create, Update, Delete)
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
                newClassWaliKelasChoiceBox.getItems().add(display);
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

    private void loadClassesForEdit() {
        editableClassesMap.clear();
        editClassChoiceBox.getItems().clear();

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
                String combinedId = kelasId + "-" + waliId; // Store for lookup
                editableClassesMap.put(display, combinedId);
                editClassChoiceBox.getItems().add(display);
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load classes for editing", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleClassSelectionForEdit() {
        String selectedClassDisplay = editClassChoiceBox.getValue();
        if (selectedClassDisplay == null || selectedClassDisplay.isEmpty()) {
            clearClassEditFields();
            updateClassButton.setDisable(true);
            deleteClassButton.setDisable(true);
            return;
        }

        String combinedId = editableClassesMap.get(selectedClassDisplay);
        if (combinedId == null) {
            clearClassEditFields();
            updateClassButton.setDisable(true);
            deleteClassButton.setDisable(true);
            return;
        }
        String[] ids = combinedId.split("-");
        int kelasId = Integer.parseInt(ids[0]);
        String waliUserId = ids[1];

        String sql = "SELECT nama_kelas, keterangan, Users_user_id, Semester_semester_id FROM Kelas WHERE kelas_id = ? AND Users_user_id = ?";
        try (Connection con = DBS.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, kelasId);
            stmt.setString(2, waliUserId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                newClassNameField.setText(rs.getString("nama_kelas"));
                newClassDescriptionField.setText(rs.getString("keterangan"));

                String currentWaliId = rs.getString("Users_user_id");
                waliKelasData.stream()
                        .filter(p -> p.getValue().equals(currentWaliId))
                        .findFirst()
                        .ifPresent(p -> newClassWaliKelasChoiceBox.setValue(p.getKey()));

                int currentSemesterId = rs.getInt("Semester_semester_id");
                semesterData.stream()
                        .filter(p -> p.getValue().equals(currentSemesterId))
                        .findFirst()
                        .ifPresent(p -> newClassSemesterChoiceBox.setValue(p.getKey()));
                updateClassButton.setDisable(false);
                deleteClassButton.setDisable(false);
            } else {
                clearClassEditFields();
                updateClassButton.setDisable(true);
                deleteClassButton.setDisable(true);
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to load class details for editing", e.getMessage());
            e.printStackTrace();
            clearClassEditFields();
            updateClassButton.setDisable(true);
            deleteClassButton.setDisable(true);
        }
    }

    private void clearClassEditFields() {
        newClassNameField.clear();
        newClassDescriptionField.clear();
        newClassWaliKelasChoiceBox.getSelectionModel().clearSelection();
        newClassSemesterChoiceBox.getSelectionModel().clearSelection();
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
                loadClassesForEdit(); // Refresh for edit/delete
            } else {
                AlertClass.ErrorAlert("Failed", "Class Not Created", "Failed to create class in the database.");
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to create class", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleUpdateClass() {
        String selectedClassDisplay = editClassChoiceBox.getValue();
        if (selectedClassDisplay == null || selectedClassDisplay.isEmpty()) {
            AlertClass.WarningAlert("Selection Error", "No Class Selected", "Please select a class to update.");
            return;
        }

        String className = newClassNameField.getText();
        String classDescription = newClassDescriptionField.getText();
        String selectedWaliKelasDisplay = newClassWaliKelasChoiceBox.getValue();
        String selectedSemesterDisplay = newClassSemesterChoiceBox.getValue();

        if (className.isEmpty() || classDescription.isEmpty() || selectedWaliKelasDisplay == null || selectedSemesterDisplay == null) {
            AlertClass.WarningAlert("Input Error", "Missing Information", "Please fill in all class details for update.");
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

        String combinedId = editableClassesMap.get(selectedClassDisplay);
        if (combinedId == null) {
            AlertClass.ErrorAlert("Retrieval Error", "Invalid Class Selection", "Could not retrieve class ID for update.");
            return;
        }
        String[] ids = combinedId.split("-");
        int kelasIdToUpdate = Integer.parseInt(ids[0]);
        String currentWaliIdForKelasPK = ids[1]; // The original wali_id which is part of the PK

        try (Connection con = DBS.getConnection()) {
            String sql = "UPDATE Kelas SET nama_kelas = ?, keterangan = ?, Users_user_id = ?, Semester_semester_id = ? WHERE kelas_id = ? AND Users_user_id = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, className);
            stmt.setString(2, classDescription);
            stmt.setString(3, waliKelasUserId);
            stmt.setInt(4, semesterId);
            stmt.setInt(5, kelasIdToUpdate);
            stmt.setString(6, currentWaliIdForKelasPK);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                AlertClass.InformationAlert("Success", "Class Updated", "Class '" + className + "' has been updated successfully.");
                clearClassEditFields();
                loadClassesForEdit(); // Refresh the choice box
                loadClassesForChoiceBox(); // Refresh class list in other sections
                loadSubjectAssignments(); // Refresh assignments if class info changed
                updateClassButton.setDisable(true); // Disable after success
                deleteClassButton.setDisable(true); // Disable after success
            } else {
                AlertClass.ErrorAlert("Failed", "Class Not Updated", "Failed to update class. Class ID or Wali Kelas might not exist, or there are unhandled dependencies (e.g., if you tried to change the Wali Kelas and FKs prevent it).");
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Failed to update class", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleDeleteClass() {
        String selectedClassDisplay = editClassChoiceBox.getValue();
        if (selectedClassDisplay == null || selectedClassDisplay.isEmpty()) {
            AlertClass.WarningAlert("Selection Error", "No Class Selected", "Please select a class to delete.");
            return;
        }

        Optional<ButtonType> result = AlertClass.ConfirmationAlert(
                "Confirm Deletion",
                "Delete Class: " + selectedClassDisplay,
                "Are you sure you want to delete this class? This will also delete all associated schedules, materials, tasks, exams, and student enrollments for this class. This action cannot be undone."
        );

        if (result.isPresent() && result.get() == ButtonType.OK) {
            String combinedId = editableClassesMap.get(selectedClassDisplay);
            if (combinedId == null) {
                AlertClass.ErrorAlert("Retrieval Error", "Invalid Class Selection", "Could not retrieve class ID for deletion.");
                return;
            }
            String[] ids = combinedId.split("-");
            int kelasIdToDelete = Integer.parseInt(ids[0]);
            String waliUserIdToDelete = ids[1];

            try (Connection con = DBS.getConnection()) {
                // Delete from dependent tables first due to ON DELETE NO ACTION
                // Order matters: Enrollment, Jadwal, Materi, Tugas, Ujian, Detail_Pengajar
                String[] dependentTables = {
                        "Enrollment", "Jadwal", "Materi", "Tugas", "Ujian", "Detail_Pengajar"
                };

                for (String tableName : dependentTables) {
                    try {
                        String deleteSql = "DELETE FROM " + tableName + " WHERE Kelas_kelas_id = ? AND Kelas_Users_user_id = ?";
                        PreparedStatement delStmt = con.prepareStatement(deleteSql);
                        delStmt.setInt(1, kelasIdToDelete);
                        delStmt.setString(2, waliUserIdToDelete);
                        delStmt.executeUpdate();
                    } catch (SQLException e) {
                        System.err.println("Warning: Could not delete from " + tableName + " for class " + kelasIdToDelete + " (Wali: " + waliUserIdToDelete + "): " + e.getMessage());
                        // Continue even if one table fails, unless it's critical for Kelas table deletion
                    }
                }

                // Finally, delete the class itself
                String sql = "DELETE FROM Kelas WHERE kelas_id = ? AND Users_user_id = ?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setInt(1, kelasIdToDelete);
                stmt.setString(2, waliUserIdToDelete);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    AlertClass.InformationAlert("Success", "Class Deleted", "Class '" + selectedClassDisplay + "' and all its associated data have been deleted.");
                    clearClassEditFields();
                    loadClassesForEdit(); // Refresh the choice box
                    loadClassesForChoiceBox(); // Refresh class list in other sections
                    loadSubjectAssignments(); // Refresh subject assignments
                    updateClassButton.setDisable(true); // Disable after success
                    deleteClassButton.setDisable(true); // Disable after success
                } else {
                    AlertClass.ErrorAlert("Failed", "Class Not Deleted", "Failed to delete class. Class might not exist or there are remaining dependencies.");
                }
            } catch (SQLException e) {
                AlertClass.ErrorAlert("Database Error", "Failed to delete class", e.getMessage());
                e.printStackTrace();
            }
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

    private void loadAllUsersToTable(String roleFilter, String nameFilter) {
        ObservableList<Users> userList = FXCollections.observableArrayList(); // Changed to Users service class
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
                userList.add(new Users( // Using the new Users constructor
                        rs.getString("user_id"),
                        rs.getString("username"),
                        rs.getString("NIS_NIP"), // password, roleId not retrieved
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
    void handleFilterAllUsersTable() {
        String selectedRole = allUsersFilterRoleChoiceBox.getValue();
        String nameFilter = allUsersFilterNameField.getText();
        loadAllUsersToTable(selectedRole, nameFilter);
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

    // Helper class for ChoiceBox items (to store display text and associated ID)


    // New Model Class for StudentEntry in TableView


}