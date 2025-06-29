package org.example.projectbdpbogacor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.prefs.Preferences;

public class HelloApplication extends Application {

    private static final String LIGHT_MODE_CSS = "/org/example/projectbdpbogacor/Aset/light-mode.css";

    private Scene scene;
    private Stage primaryStage;

    private static HelloApplication instance; // Singleton instance
    private String loggedInUserRole;
    private String loggedInUserId;

    public HelloApplication() {
        instance = this; // Initialize the singleton instance
    }

    public static HelloApplication getInstance() {
        return instance;
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;

        // Load the initial login scene
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        scene = new Scene(fxmlLoader.load(), 1300, 700); // Initial scene size

        applyCurrentMode(); // Apply theme to the initial scene

        stage.setTitle("Login Aplikasi");
        stage.setScene(scene);
        stage.show();
    }


    private void applyCurrentMode() {
        if (scene != null) { // Ensure scene is not null before applying styles
            scene.getStylesheets().clear();
            String stylesheet = Objects.requireNonNull(getClass().getResource(LIGHT_MODE_CSS)).toExternalForm();
            scene.getStylesheets().add(stylesheet);
        }
    }


    public Scene getScene() {
        return scene;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public String getLoggedInUserRole() {
        return loggedInUserRole;
    }

    public void setLoggedInUserRole(String loggedInUserRole) {
        this.loggedInUserRole = loggedInUserRole;
    }

    public String getLoggedInUserId() {
        return loggedInUserId;
    }

    public void setLoggedInUserId(String loggedInUserId) {
        this.loggedInUserId = loggedInUserId;
    }

    // Method to change the scene
    public void changeScene(String fxmlPath, String title, int width, int height) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        scene.setRoot(loader.load()); // Set new root to the existing scene
        primaryStage.setTitle(title);
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
        primaryStage.centerOnScreen(); // Center the stage after resizing
        applyCurrentMode(); // Re-apply styles after changing root
    }

    public static void main(String[] args) {
        launch();
    }
}