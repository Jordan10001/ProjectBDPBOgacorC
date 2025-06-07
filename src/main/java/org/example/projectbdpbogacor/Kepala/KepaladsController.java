// ProjectBDPBOgacor/src/main/java/org/example/projectbdpbogacor/Kepala/KepaladsController.java
package org.example.projectbdpbogacor.Kepala;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import org.example.projectbdpbogacor.Aset.AlertClass;
import org.example.projectbdpbogacor.DBSource.DBS;
import org.example.projectbdpbogacor.HelloApplication;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class KepaladsController {

    @FXML
    private Label welcomeUserLabel;
    @FXML
    private TabPane kepalaTabPane;

    // Announcements
    @FXML
    private TextArea announcementTextArea;

    private String loggedInUserId;

    @FXML
    void initialize() {
        loggedInUserId = HelloApplication.getInstance().getLoggedInUserId();
        loadKepalaSekolahName();
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
            String sql = "INSERT INTO Pengumuman (pengumuman, Users_user_id) VALUES (?, ?)";
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