package org.example.projectbdpbogacor.Aset;


import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;

import java.util.Optional;

public class AlertClass {
    public static void ErrorAlert(String title,String header,String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void WarningAlert(String title,String header,String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void InformationAlert(String title,String header,String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static Optional<ButtonType> ConfirmationAlert(String title, String header, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
        return alert.showAndWait();
    }

    public static ChoiceDialog<String> ChoiceDialog(String title, String header, String message, String... choices) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices[0], choices);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(message);
        return dialog;
    }

}

