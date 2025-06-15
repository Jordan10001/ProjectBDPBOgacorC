package org.example.projectbdpbogacor.Service;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Users {
    private final StringProperty userId;
    private final StringProperty username;
    private final StringProperty password;
    private final StringProperty nisNip;
    private final StringProperty nama;
    private final StringProperty gender;
    private final StringProperty alamat;
    private final StringProperty email;
    private final StringProperty nomerHp;
    private final StringProperty roleRoleId;
  // Added for display purposes

    // Constructor with all parameters
    public Users(String userId, String username, String password, String nisNip,
                 String nama, String gender, String alamat, String email,
                 String nomerHp, String roleRoleId) {
        this.userId = new SimpleStringProperty(userId);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.nisNip = new SimpleStringProperty(nisNip);
        this.nama = new SimpleStringProperty(nama);
        this.gender = new SimpleStringProperty(gender);
        this.alamat = new SimpleStringProperty(alamat);
        this.email = new SimpleStringProperty(email);
        this.nomerHp = new SimpleStringProperty(nomerHp);
        this.roleRoleId = new SimpleStringProperty(roleRoleId);
        // Initialize here
    }


    // Overloaded Constructor with all parameters including roleName (for UI display)

    // NEW: Constructor for displaying user data in tables (e.g., Admin/Kepala dashboard)
    // Does not include password or roleRoleId directly, as they are not retrieved for display in this context.
    public Users(String userId, String username, String nisNip, String nama,
                  String gender, String alamat, String email, String nomerHp, String roleName) {
        this.userId = new SimpleStringProperty(userId);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(""); // Not retrieved, set to empty
        this.nisNip = new SimpleStringProperty(nisNip);
        this.nama = new SimpleStringProperty(nama);
        this.gender = new SimpleStringProperty(gender);
        this.alamat = new SimpleStringProperty(alamat);
        this.email = new SimpleStringProperty(email);
        this.nomerHp = new SimpleStringProperty(nomerHp);
        this.roleRoleId = new SimpleStringProperty(roleName); // Not directly retrieved, set to empty

    }

    public Users(String userId, String username, String nisNip, String nama,
                 String gender, String alamat, String email, String nomerHp) {
        this.userId = new SimpleStringProperty(userId);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(""); // Not retrieved, set to empty
        this.nisNip = new SimpleStringProperty(nisNip);
        this.nama = new SimpleStringProperty(nama);
        this.gender = new SimpleStringProperty(gender);
        this.alamat = new SimpleStringProperty(alamat);
        this.email = new SimpleStringProperty(email);
        this.nomerHp = new SimpleStringProperty(nomerHp);
        this.roleRoleId = new SimpleStringProperty(""); // Not directly retrieved, set to empty

    }


    public Users(String userId,  String nisNip, String nama) {
        this.userId = new SimpleStringProperty(userId);
        this.username = new SimpleStringProperty("");
        this.password = new SimpleStringProperty(""); // Not retrieved, set to empty
        this.nisNip = new SimpleStringProperty(nisNip);
        this.nama = new SimpleStringProperty(nama);
        this.gender = new SimpleStringProperty("");
        this.alamat = new SimpleStringProperty("");
        this.email = new SimpleStringProperty("");
        this.nomerHp = new SimpleStringProperty("");
        this.roleRoleId = new SimpleStringProperty(""); // Not directly retrieved, set to empty

    }

    public Users(String userId,   String nama) {
        this.userId = new SimpleStringProperty(userId);
        this.username = new SimpleStringProperty("");
        this.password = new SimpleStringProperty(""); // Not retrieved, set to empty
        this.nisNip = new SimpleStringProperty("");
        this.nama = new SimpleStringProperty(nama);
        this.gender = new SimpleStringProperty("");
        this.alamat = new SimpleStringProperty("");
        this.email = new SimpleStringProperty("");
        this.nomerHp = new SimpleStringProperty("");
        this.roleRoleId = new SimpleStringProperty(""); // Not directly retrieved, set to empty

    }

    public Users( String nama) {
        this.userId = new SimpleStringProperty("");
        this.username = new SimpleStringProperty("");
        this.password = new SimpleStringProperty("");
        this.nisNip = new SimpleStringProperty("");
        this.nama = new SimpleStringProperty(nama);
        this.gender = new SimpleStringProperty("");
        this.alamat = new SimpleStringProperty("");
        this.email = new SimpleStringProperty("");
        this.nomerHp = new SimpleStringProperty("");
        this.roleRoleId = new SimpleStringProperty("");
        // Initialize here
    }


    // Getters for String values (using .get())
    public String getUserId() { return userId.get(); }
    public String getUsername() { return username.get(); }
    public String getPassword() { return password.get(); }
    public String getNisNip() { return nisNip.get(); }
    public String getNama() { return nama.get(); }
    public String getGender() { return gender.get(); }
    public String getAlamat() { return alamat.get(); }
    public String getEmail() { return email.get(); }
    public String getNomerHp() { return nomerHp.get(); }
    public String getRoleRoleId() { return roleRoleId.get(); }
     // Getter for roleName

    // Property methods (important for JavaFX TableView and Binding)
    public StringProperty userIdProperty() { return userId; }
    public StringProperty usernameProperty() { return username; }
    public StringProperty passwordProperty() { return password; }
    public StringProperty nisNipProperty() { return nisNip; }
    public StringProperty namaProperty() { return nama; }
    public StringProperty genderProperty() { return gender; }
    public StringProperty alamatProperty() { return alamat; }
    public StringProperty emailProperty() { return email; }
    public StringProperty nomerHpProperty() { return nomerHp; }
    public StringProperty roleRoleIdProperty() { return roleRoleId; }
    // Property method for roleName

    // Setters (if you need to modify values after object creation,
    // which is common for mutable data in JavaFX models)
    public void setUserId(String userId) { this.userId.set(userId); }
    public void setUsername(String username) { this.username.set(username); }
    public void setPassword(String password) { this.password.set(password); }
    public void setNisNip(String nisNip) { this.nisNip.set(nisNip); }
    public void setNama(String nama) { this.nama.set(nama); }
    public void setGender(String gender) { this.gender.set(gender); }
    public void setAlamat(String alamat) { this.alamat.set(alamat); }
    public void setEmail(String email) { this.email.set(email); }
    public void setNomerHp(String nomerHp) { this.nomerHp.set(nomerHp); }
    public void setRoleRoleId(String roleRoleId) { this.roleRoleId.set(roleRoleId); }

}