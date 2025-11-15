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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    // These @FXML tags link the variables to the
    // components we defined in the LoginView.fxml file.
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    /**
     * This method is called when the "Login" button is clicked.
     * The name "onLoginButtonClick" matches the onAction="#onLoginButtonClick"
     * in the FXML file.
     */
    @FXML
    private void onLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.equals("hms") && password.equals("admin")) {
            // Login success
            try {
                // Get the stage (the window) from the event
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                
                // Load the new FXML for the home screen
                // We will create "HomeView.fxml" in our next step!
                Parent homeRoot = FXMLLoader.load(getClass().getResource("HomeView.fxml"));
                Scene homeScene = new Scene(homeRoot);
                
                // Set the stage to show the new scene
                stage.setScene(homeScene);
                stage.setTitle("Hospital Management - Home");

            } catch (IOException e) {
                e.printStackTrace();
                showError("Failed to load the home page.");
            }
        } else {
            // Login failed - show an error alert
            showError("Incorrect Username or Password");
        }
    }

    /**
     * This method is called when the "Close" button is clicked.
     */
    @FXML
    private void onCloseButtonClick(ActionEvent event) {
        // This is the JavaFX way to create a confirmation dialog
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Close");
        alert.setHeaderText(null);
        alert.setContentText("Do you really want to Close Application?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User clicked OK
            System.exit(0);
        }
    }

    /**
     * A helper method to show error messages easily.
     */
    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}