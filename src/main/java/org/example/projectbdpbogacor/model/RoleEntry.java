package org.example.projectbdpbogacor.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RoleEntry {
    private final IntegerProperty roleId;
    private final StringProperty roleName;

    public RoleEntry(int roleId, String roleName) {
        this.roleId = new SimpleIntegerProperty(roleId);
        this.roleName = new SimpleStringProperty(roleName);
    }

    public IntegerProperty roleIdProperty() {
        return roleId;
    }

    public StringProperty roleNameProperty() {
        return roleName;
    }

    public int getRoleId() {
        return roleId.get();
    }

    public String getRoleName() {
        return roleName.get();
    }

    public void setRoleId(int roleId) {
        this.roleId.set(roleId);
    }

    public void setRoleName(String roleName) {
        this.roleName.set(roleName);
    }

    @Override
    public String toString() {
        return roleName.get(); // Penting untuk menampilkan nama peran di ChoiceBox/ComboBox
    }
}