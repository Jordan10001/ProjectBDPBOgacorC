package org.example.projectbdpbogacor.Service;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Role {
    private final StringProperty roleId;   // role_id (VARCHAR(1))
    private final StringProperty roleName; // role_name (VARCHAR(50))

    // Constructor
    public Role(String roleId, String roleName) {
        this.roleId = new SimpleStringProperty(roleId);
        this.roleName = new SimpleStringProperty(roleName);
    }

    // Getters
    public String getRoleId() { return roleId.get(); }
    public String getRoleName() { return roleName.get(); }

    // Property Methods
    public StringProperty roleIdProperty() { return roleId; }
    public StringProperty roleNameProperty() { return roleName; }

    // Setters (if needed, though role might be mostly static)
    public void setRoleName(String roleName) { this.roleName.set(roleName); }
}