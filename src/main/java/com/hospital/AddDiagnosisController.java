package com.hospital;

import Project.ConnectionProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.beans.property.ReadOnlyStringWrapper;

public class AddDiagnosisController {

    // FXML Components
    @FXML private TextField patientIdField;
    @FXML private Label idNotFoundLabel;
    @FXML private TableView<ObservableList> patientTable; // Will hold patient details
    @FXML private TextField symptomsField;
    @FXML private TextField diagnosisField;
    @FXML private TextField medicinesField;
    @FXML private CheckBox wardCheckBox;
    @FXML private Label wardTypeLabel;
    @FXML private ComboBox<String> wardComboBox;

    // State variable from Swing: flag=1 initially, then changed upon search failure
    private boolean patientExists = false; 

    /**
     * Initial setup when the form loads.
     * We hide the ward fields and populate the ComboBox.
     */
    @FXML
    public void initialize() {
        // Swing initialization: jLabel2.setVisible(false); jLabel7.setVisible(false); jComboBox1.setVisible(false);
        idNotFoundLabel.setVisible(false);
        wardTypeLabel.setVisible(false);
        wardComboBox.setVisible(false);
        wardComboBox.setItems(FXCollections.observableArrayList("General", "Single", "Duo"));
        
        // Initialize TableView columns based on the patient table schema
        initializePatientTable();
    }
    
    /**
     * Sets up the columns for the TableView.
     */
    private void initializePatientTable() {
        String[] columnNames = {"Patient ID", "Name", "Contact", "Age", "Gender", "Blood Group", "Address", "Disease"};
        
        for (int i = 0; i < columnNames.length; i++) {
            final int columnIndex = i;
            TableColumn<ObservableList, String> column = new TableColumn<>(columnNames[i]);
            column.setCellValueFactory(param -> {
                if (param.getValue().size() > columnIndex) {
                    return new ReadOnlyStringWrapper(param.getValue().get(columnIndex).toString());
                }
                return new ReadOnlyStringWrapper("");
            });
            patientTable.getColumns().add(column);
        }
        patientTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Handles the visibility toggle of the Ward fields (jCheckBox1ActionPerformed in Swing)
     */
    @FXML
    private void onWardCheckBoxChange(ActionEvent event) {
        if (wardCheckBox.isSelected()) {
            wardTypeLabel.setVisible(true);
            wardComboBox.setVisible(true);
        } else {
            wardTypeLabel.setVisible(false);
            wardComboBox.setVisible(false);
        }
    }

    /**
     * Handles the Search button click (jButton1ActionPerformed in Swing)
     */
    @FXML
    private void onSearchClick(ActionEvent event) {
        String patientId = patientIdField.getText();
        patientTable.getItems().clear(); // Clear previous data
        idNotFoundLabel.setVisible(false);
        patientExists = false; // Reset flag

        if (patientId.isEmpty()) {
            showAlert(AlertType.WARNING, "Input Error", "Please enter a Patient ID.");
            return;
        }

        // --- SECURE SEARCH QUERY ---
        String sql = "SELECT * FROM patient WHERE patientId = ?";

        try (Connection con = ConnectionProvider.getCon();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, patientId);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                patientExists = true;
                idNotFoundLabel.setVisible(false);
                patientIdField.setEditable(false);
                
                // Populate TableView with data from the ResultSet
                ObservableList<ObservableList> data = FXCollections.observableArrayList();
                ResultSetMetaData rsmd = rs.getMetaData();
                int numColumns = rsmd.getColumnCount();
                
                // Add the single row data from the result set
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= numColumns; i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
                patientTable.setItems(data);

            } else {
                // Swing logic: jLabel2.setVisible(false);
                idNotFoundLabel.setVisible(true); // Label is "PatientId Does Not Exists !"
                patientIdField.setEditable(true);
            }

        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Connection or Query Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the Save button click (jButton2ActionPerformed in Swing)
     */
    @FXML
    private void onSaveClick(ActionEvent event) {
        if (!patientExists) {
            showAlert(AlertType.ERROR, "Error", "Please search for a valid patient first.");
            return;
        }

        // Get form data
        String patientId = patientIdField.getText();
        String symptom = symptomsField.getText();
        String diagnosis = diagnosisField.getText();
        String medicines = medicinesField.getText();
        
        String wardReq = wardCheckBox.isSelected() ? "YES" : "NO";
        String typeWard = wardCheckBox.isSelected() ? wardComboBox.getValue() : "";

        // --- SECURE INSERT QUERY to patientreport table ---
        String sql = "INSERT INTO patientreport (patientId, symptoms, diagnosis, medicines, wardReq, typeWard) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectionProvider.getCon();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, patientId);
            pst.setString(2, symptom);
            pst.setString(3, diagnosis);
            pst.setString(4, medicines);
            pst.setString(5, wardReq);
            pst.setString(6, typeWard);

            pst.executeUpdate();
            showAlert(AlertType.INFORMATION, "Success", "Diagnosis information successfully recorded.");
            
            // Swing logic: setVisible(false); new addDiagnosisInformation().setVisible(true);
            // JavaFX equivalent: Close current window and clear form fields (or close)
            onCloseClick(event); // Close the current window

        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Failed to insert diagnosis: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the Close button click (jButton3ActionPerformed in Swing)
     */
    @FXML
    private void onCloseClick(ActionEvent event) {
        // Get the current window (Stage) and close it
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}