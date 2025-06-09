package org.example.projectbdpbogacor.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserTableEntry {
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
