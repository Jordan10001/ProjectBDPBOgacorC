package org.example.projectbdpbogacor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import org.example.projectbdpbogacor.Aset.AlertClass;
import org.example.projectbdpbogacor.Aset.HashGenerator;
import org.example.projectbdpbogacor.DBSource.DBS;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ChoiceBox<String> selectRole;

    @FXML
    private ToggleButton darkModeToggle;

    private String userId;

    // Ensure the database connection is established
    @FXML
    void initialize() {
        selectRole.getItems().addAll("Admin", "Kepala Sekolah", "Guru", "Wali Kelas", "Siswa");
        selectRole.setValue("Choice Role");

        HelloApplication app = HelloApplication.getInstance();
        if (app != null) {
            darkModeToggle.setSelected(app.isDarkMode());
            darkModeToggle.setText(app.isDarkMode() ? "Light Mode" : "Dark Mode");
        }

        DBS.checkConnection();
    }

    @FXML
    void handleModeToggle(ActionEvent event) {
        HelloApplication app = HelloApplication.getInstance();
        if (app != null) {
            app.toggleMode();
            darkModeToggle.setText(app.isDarkMode() ? "Light Mode" : "Dark Mode"); // Update button text
        }
    }

    // Helper method to convert full role name to single-character role ID
    private String getRoleIdFromRoleName(String roleName) {
        switch (roleName) {
            case "Admin":
                return "A";
            case "Kepala Sekolah":
                return "K";
            case "Guru":
                return "G";
            case "Wali Kelas":
                return "W";
            case "Siswa":
                return "S";
            default:
                return null; // Or throw an IllegalArgumentException
        }
    }

    boolean verifyCredentials(String username, String password, String roleName) throws SQLException {
        String roleId = getRoleIdFromRoleName(roleName); // Get the single-character role ID
        if (roleId == null) {
            AlertClass.ErrorAlert("Login Error", "Invalid Role", "The selected role is not recognized.");
            return false;
        }

        try (Connection con = DBS.getConnection()) {
            if (con == null) {
                AlertClass.ErrorAlert("Connection Error", "Database Connection Failed", "Could not establish a connection to the database.");
                return false;
            }
            String sql = "SELECT user_id, password FROM \"User\" WHERE Username = ? AND Role_role_id = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, roleId); // Use the single-character role ID here
            System.out.println("Executing query for user");

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String dbPasswordHash = rs.getString("password");
                if (HashGenerator.verify(password, dbPasswordHash)) {
                    this.userId = rs.getString("user_id");
                    return true;
                }
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Query Failed", "An error occurred while querying the database: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @FXML
    void onLoginClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = selectRole.getValue();

        if (username.isEmpty()) {
            AlertClass.WarningAlert("Input Error", "Username Empty", "Please enter your username.");
            return;
        }
        if (password.isEmpty()) {
            AlertClass.WarningAlert("Input Error", "Password Empty", "Please enter your password.");
            return;
        }
        if (role == null || role.equals("Choice Role")) {
            AlertClass.WarningAlert("Input Error", "Role Not Selected", "Please select a role.");
            return;
        }

        try {
            if (verifyCredentials(username, password, role)) { // Pass the full role name
                HelloApplication app = HelloApplication.getInstance();
                app.setLoggedInUserId(this.userId);
                app.setLoggedInUserRole(role); // Store the full role name in HelloApplication for display/logic

                String fxmlFile = "";
                String title = "";
                int sceneWidth = 1300;
                int sceneHeight = 600;

                switch (role) {
                    case "Admin":
                        fxmlFile = "/org/example/projectbdpbogacor/admin-dashboard-view.fxml";
                        title = "Admin Dashboard";
                        break;
                    case "Kepala Sekolah":
                        fxmlFile = "/org/example/projectbdpbogacor/kepala-dashboard-view.fxml";
                        title = "Kepala Sekolah Dashboard";
                        break;
                    case "Guru":
                        fxmlFile = "/org/example/projectbdpbogacor/guru-dashboard-view.fxml";
                        title = "Guru Dashboard";
                        break;
                    case "Wali Kelas":
                        fxmlFile = "/org/example/projectbdpbogacor/wali-dashboard-view.fxml";
                        title = "Wali Kelas Dashboard";
                        break;
                    case "Siswa":
                        fxmlFile = "/org/example/projectbdpbogacor/siswa-dashboard-view.fxml";
                        title = "Siswa Dashboard";
                        break;
                    default:
                        AlertClass.ErrorAlert("Login Error", "Invalid Role", "The selected role does not have a defined dashboard.");
                        return;
                }
                app.changeScene(fxmlFile, title, sceneWidth, sceneHeight);
            } else {
                AlertClass.ErrorAlert("Login Failed", "Invalid Credentials", "Please check your username and password.");
            }
        } catch (SQLException e) {
            AlertClass.ErrorAlert("Database Error", "Login Failed", "An error occurred during login: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            AlertClass.ErrorAlert("Error Loading View", "Could not load dashboard", "An error occurred while loading the dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }
}