package org.example.projectbdpbogacor.DBSource;

import org.example.projectbdpbogacor.Aset.AlertClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBS {

    protected static final String URL = ""; // Ganti 'dbmu' sesuai database kamu
    protected static final String USER = "postgres"; // Ganti user kamu
    protected static final String PASSWORD = "admin"; // Ganti password kamu


    public static Connection getUserConnection() {

        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Berhasil terhubung ke database PostgreSQL!");
            return conn;
        } catch (SQLException e) {
            System.out.println("❌ Gagal koneksi ke database: " + e.getMessage());
            AlertClass.ErrorAlert("❌Connection Error", "Database Connection Error", e.getMessage());
            return null;
        }
    }
}
