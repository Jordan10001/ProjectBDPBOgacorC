package org.example.projectbdpbogacor.DBSource;

import org.example.projectbdpbogacor.Aset.AlertClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBS {

    protected static final String URL = "jdbc:postgresql://localhost:5432/ProjectBDPBO"; // Ganti 'dbmu' sesuai database kamu
//    protected static final String URL = "jdbc:postgresql://db.uqmjegbdazbmjrwcqont.supabase.co:5432/postgres"; // Ganti 'dbmu' sesuai database kamu
    protected static final String USER = "postgres"; // Ganti user kamu
    protected static final String PASSWORD = "admin"; // Ganti password kamu


    public static Connection getConnection() {

        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            return conn;
        } catch (SQLException e) {
            System.out.println("❌ Gagal koneksi ke database: " + e.getMessage());
            AlertClass.ErrorAlert("❌Connection Error", "Database Connection Error", e.getMessage());
            return null;
        }
    }

    public static void checkConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Connection is valid.");
            } else {
                System.out.println("❌ Connection is not valid.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error checking connection: " + e.getMessage());
        }
    }
}
