package com.hospital;

import Project.ConnectionProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class UpdatePatientController {

    @FXML private TextField patientIdField;
    @FXML private TextField nameField;
    @FXML private TextField contactField;
    @FXML private TextField ageField;
    // Note: The original Swing code used a TextField for Gender (jTextField5), 
    // but the schema is best handled with a ComboBox for consistency with AddNewPatient.
    // I am assuming the FXML/Schema supports a ComboBox for Gender, as per our previous plan.
    @FXML private ComboBox<String> genderComboBox;
    @FXML private TextField bloodGroupField;
    @FXML private TextField addressField;
    @FXML private TextField diseaseField;

    private boolean patientFound = false;

    @FXML
    public void initialize() {
        genderComboBox.getItems().addAll("Male", "Female", "Others");
        clearFields(false); // Clear all non-ID fields on load
        patientIdField.setEditable(true);
    }

    /**
     * Handles the Search button click (jButton1ActionPerformed in Swing).
     * Retrieves patient data from the database.
     */
    @FXML
    private void onSearchClick(ActionEvent event) {
        String patientId = patientIdField.getText();
        
        if (patientId.isEmpty()) {
            showAlert(AlertType.WARNING, "Input Required", "Please enter a Patient ID to search.");
            return;
        }

        // --- SECURE SELECT QUERY ---
        String sql = "SELECT * FROM patient WHERE patientId = ?";

        try (Connection con = ConnectionProvider.getCon();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setString(1, patientId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // Patient found: populate fields
                patientFound = true;
                patientIdField.setEditable(false); // Swing logic
                
                // Populate fields using column index (1-based index from Swing code)
                nameField.setText(rs.getString(2));
                contactField.setText(rs.getString(3));
                ageField.setText(rs.getString(4));
                
                // Gender (rs.getString(5)) is handled by ComboBox
                genderComboBox.getSelectionModel().select(rs.getString(5)); 
                
                bloodGroupField.setText(rs.getString(6));
                addressField.setText(rs.getString(7));
                diseaseField.setText(rs.getString(8));
                
                showAlert(AlertType.INFORMATION, "Success", "Patient record loaded.");
            } else {
                // Patient not found
                patientFound = false;
                clearFields(false);
                showAlert(AlertType.ERROR, "Not Found", "Patient ID does not exist.");
            }

        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Connection or Query Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the Update button click (jButton2ActionPerformed in Swing).
     * Updates the existing patient record.
     */
    @FXML
    private void onUpdateClick(ActionEvent event) {
        if (!patientFound) {
            showAlert(AlertType.ERROR, "Error", "Please search for a valid patient record first.");
            return;
        }

        String patientId = patientIdField.getText();
        String name = nameField.getText();
        String contactNumber = contactField.getText();
        String age = ageField.getText();
        String gender = genderComboBox.getValue(); // Get value from ComboBox
        String bloodGroup = bloodGroupField.getText();
        String address = addressField.getText();
        String anyMajorDisease = diseaseField.getText();

        // --- SECURE UPDATE QUERY (Fixes SQL Injection vulnerability) ---
        // Sets columns in order: name, contact, age, gender, bloodGroup, address, disease, THEN patientId (for WHERE clause)
        String sql = "UPDATE patient SET name=?, contactNumber=?, age=?, gender=?, bloodGroup=?, address=?, anyMajorDisease=? WHERE patientId=?";

        try (Connection con = ConnectionProvider.getCon();
             PreparedStatement pst = con.prepareStatement(sql)) {

            // Set parameters
            pst.setString(1, name);
            pst.setString(2, contactNumber);
            pst.setString(3, age);
            pst.setString(4, gender);
            pst.setString(5, bloodGroup);
            pst.setString(6, address);
            pst.setString(7, anyMajorDisease);
            pst.setString(8, patientId); // The WHERE clause parameter

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                showAlert(AlertType.INFORMATION, "Success", "Patient record successfully updated.");
            } else {
                showAlert(AlertType.WARNING, "Update Failed", "No changes made.");
            }
            
            // Swing logic: reload the form (setVisible(false); new updatePatientRecord().setVisible(true);)
            // JavaFX equivalent: Clear fields for next entry.
            clearFields(true);

        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Failed to update record: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the Close button click (jButton3ActionPerformed in Swing).
     */
    @FXML
    private void onCloseClick(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
    
    // --- Helper Methods ---

    private void clearFields(boolean clearId) {
        if(clearId) patientIdField.clear();
        nameField.clear();
        contactField.clear();
        ageField.clear();
        genderComboBox.getSelectionModel().clearSelection();
        bloodGroupField.clear();
        addressField.clear();
        diseaseField.clear();
        patientFound = false;
        patientIdField.setEditable(true);
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}