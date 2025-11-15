package com.hospital;

// We need your original ConnectionProvider
import Project.ConnectionProvider; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddNewPatientController {

    // These @FXML tags link to the fx:id components in the FXML file
    @FXML private TextField patientIdField;
    @FXML private TextField nameField;
    @FXML private TextField contactField;
    @FXML private TextField ageField;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private TextField bloodGroupField;
    @FXML private TextField addressField;
    @FXML private TextField diseaseField;

    /**
     * This method is called automatically when the FXML file is loaded.
     * We use it to populate the ComboBox.
     */
    @FXML
    public void initialize() {
        genderComboBox.setItems(FXCollections.observableArrayList("Male", "Female", "Others"));
    }

    /**
     * Called when the "Save" button is clicked.
     */
    @FXML
    private void onSaveClick(ActionEvent event) {
        // Get all data from the form
        String patientId = patientIdField.getText();
        String name = nameField.getText();
        String contactNumber = contactField.getText();
        String age = ageField.getText();
        String gender = genderComboBox.getValue();
        String bloodGroup = bloodGroupField.getText();
        String address = addressField.getText();
        String anyMajorDisease = diseaseField.getText();

        // Validate input (simple check)
        if (patientId.isEmpty() || name.isEmpty() || contactNumber.isEmpty() || age.isEmpty() || gender == null) {
            showAlert(AlertType.ERROR, "Form Error", "Please fill in all required fields.");
            return;
        }

        // --- THIS IS THE SECURE WAY TO DO DATABASE QUERIES ---
        // The SQL query uses '?' as placeholders
        String sql = "INSERT INTO patient VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        // try-with-resources ensures the connection and statement are
        // always closed, even if an error occurs.
        try (Connection con = ConnectionProvider.getCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            // We set the value for each '?' placeholder
            // This prevents SQL Injection!
            pstmt.setString(1, patientId);
            pstmt.setString(2, name);
            pstmt.setString(3, contactNumber);
            pstmt.setString(4, age);
            pstmt.setString(5, gender);
            pstmt.setString(6, bloodGroup);
            pstmt.setString(7, address);
            pstmt.setString(8, anyMajorDisease);

            // Execute the update
            pstmt.executeUpdate();

            // Show success and clear the form
            showAlert(AlertType.INFORMATION, "Success", "Patient record successfully saved.");
            clearForm();

        } catch (SQLException e) {
            // Show a detailed error message if something goes wrong
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Failed to save patient: " + e.getMessage());
        } catch (Exception e) {
            // Catch any other errors (like ConnectionProvider failing)
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Called when the "Close" button is clicked.
     */
    @FXML
    private void onCloseClick(ActionEvent event) {
        // Get the current window (Stage) and close it
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * A helper method to clear all fields after a successful save.
     */
    private void clearForm() {
        patientIdField.clear();
        nameField.clear();
        contactField.clear();
        ageField.clear();
        genderComboBox.getSelectionModel().clearSelection();
        bloodGroupField.clear();
        addressField.clear();
        diseaseField.clear();
    }

    /**
     * A helper method to show alerts easily.
     */
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}