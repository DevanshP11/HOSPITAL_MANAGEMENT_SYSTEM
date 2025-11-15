package com.hospital;

import java.io.IOException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HomeController {

    /**
     * This method is called when the "Logout" button is clicked.
     */
    @FXML
    private void onLogoutClick(ActionEvent event) {
        // Show confirmation dialog
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Logout");
        alert.setHeaderText(null);
        alert.setContentText("Do you really want to Logout?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User clicked OK, let's go back to the Login screen.

            try {
                // Load the LoginView.fxml
                Parent loginRoot = FXMLLoader.load(getClass().getResource("LoginView.fxml"));
                Scene loginScene = new Scene(loginRoot);

                // Get the current stage (window) and set its scene to the login scene
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(loginScene);
                stage.setTitle("Hospital Management System - Login");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // --- Navigation Methods for other buttons ---
    // These all open new windows, just like your Swing app.

    @FXML
    private void onAddPatientClick(ActionEvent event) {
        // This will open "AddNewPatientView.fxml" in a new window
        // We will create that file in the next step!
        loadNewWindow("AddNewPatientView.fxml", "Add New Patient Record");
    }

    @FXML
    private void onAddDiagnosisClick(ActionEvent event) {
        loadNewWindow("AddDiagnosisView.fxml", "Add Diagnosis Information");
    }

    @FXML
    private void onFullHistoryClick(ActionEvent event) {
        loadNewWindow("FullHistoryView.fxml", "Full Patient History");
    }

    @FXML
    private void onUpdatePatientClick(ActionEvent event) {
        loadNewWindow("UpdatePatientView.fxml", "Update Patient Record");
    }

    @FXML
    private void onHospitalInfoClick(ActionEvent event) {
        loadNewWindow("HospitalInfoView.fxml", "Hospital Information");
    }

    
    /**
     * A helper method to open a new FXML file in a new window (Stage).
     * This mimics the "new MyFrame().setVisible(true)" behavior from Swing.
     *
     * @param fxmlFileName The name of the FXML file to load (e.g., "AddNewPatientView.fxml")
     * @param title        The title for the new window
     */
    private void loadNewWindow(String fxmlFileName, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent root = loader.load();
            
            // Create a new window (Stage)
            Stage newStage = new Stage();
            newStage.setTitle(title);
            newStage.setScene(new Scene(root));
            
            // This prevents you from clicking on the main menu
            // while this new window is open.
            newStage.initModality(Modality.APPLICATION_MODAL); 
            
            newStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            
            // Show an error if the FXML file couldn't be loaded
            // (This usually means we haven't created it yet!)
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Load Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to load the view: " + fxmlFileName 
                + "\nWe need to create this file next.");
            alert.showAndWait();
        }
    }
}