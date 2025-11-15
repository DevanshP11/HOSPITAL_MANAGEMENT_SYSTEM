package com.hospital;

import Project.ConnectionProvider;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import static javafx.scene.control.TableView.UNCONSTRAINED_RESIZE_POLICY; // The corrected import
import javafx.stage.Stage;


public class FullHistoryController {

    @FXML private TableView<ObservableList> historyTable;

    /**
     * Swing logic: formComponentShown - executed when the window opens.
     * In JavaFX, we use the initialize() method.
     */
    @FXML
    public void initialize() {
        loadFullHistory();
    }

    /**
     * Runs the robust query and populates the TableView dynamically.
     */
    private void loadFullHistory() {
        // FINAL ROBUST QUERY: LEFT JOIN shows ALL patients, and TRIM() handles hidden spaces in IDs.
        String sql = "SELECT * FROM patient LEFT JOIN patientreport ON TRIM(patient.patientId) = TRIM(patientreport.patientId)";

        try (Connection con = ConnectionProvider.getCon();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            // 1. Get metadata to define columns and data
            ResultSetMetaData rsmd = rs.getMetaData();
            final int columnCount = rsmd.getColumnCount();

            // Clear any existing columns/data
            historyTable.getColumns().clear();
            ObservableList<ObservableList> data = FXCollections.observableArrayList();

            // 2. Dynamically create columns based on the query result
            for (int i = 1; i <= columnCount; i++) {
                final int columnIndex = i - 1;
                // Use the column name from the ResultSet for the header
                TableColumn<ObservableList, String> column = new TableColumn<>(rsmd.getColumnLabel(i));
                
                // Set the value factory to pull the string from the corresponding list index
                column.setCellValueFactory(param -> {
                    if (param.getValue().size() > columnIndex) {
                        return new ReadOnlyStringWrapper(param.getValue().get(columnIndex).toString());
                    }
                    return new ReadOnlyStringWrapper("");
                });
                
                historyTable.getColumns().add(column);
            }

            // 3. Populate the data rows
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }

            historyTable.setItems(data);
            
            // Apply the modern resizing policy
            historyTable.setColumnResizePolicy(UNCONSTRAINED_RESIZE_POLICY);


        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Database Error", "Failed to load patient history: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the Close button click (jButton1ActionPerformed in Swing)
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