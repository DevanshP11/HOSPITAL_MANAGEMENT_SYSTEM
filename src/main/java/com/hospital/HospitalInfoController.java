package com.hospital;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class HospitalInfoController {

    // FXML TextAreas that will be populated with content
    @FXML private TextArea dosArea;
    @FXML private TextArea dontsArea;
    @FXML private TextArea servicesArea;

    /**
     * This method runs when the form is loaded. It replaces the static 
     * text definition in the initComponents() area of the Swing file.
     */
    @FXML
    public void initialize() {
        populateContent();
    }

    private void populateContent() {
        // CONTENT FOR 'DOs' (jTextArea4)
        dosArea.setText(
            "1. Follow Hospital Policies\n\n" +
            "2. Check-In and Register\n\n" +
            "3. Wash Your Hands\n\n" +
            "4. Respect Privacy\n\n" +
            "5. Use Quiet Voices\n\n" +
            "6. Be Patient and Supportive\n\n" +
            "7. Follow Infection Control Measures"
        );

        // CONTENT FOR 'DONTs' (jTextArea2)
        dontsArea.setText(
            "1. Don't Overstay \n\n" +
            "2. Don't Bring in Sick Children or Pets \n\n" +
            "3. Don't Use Mobile Devices in Restricted Areas\n\n" +
            "4. Don't Bring Outside Food or Drinks\n\n" +
            "5. Don't Touch Medical Equipment\n\n" +
            "6. Don't Ignore Safety Procedures\n\n" +
            "7. Don't Offer Medical Advice\n\n" +
            "8. Don't Share Personal Health Information"
        );

        // CONTENT FOR 'SERVICES' (jTextArea3)
        servicesArea.setText(
            "1. Emergency Services\n\n" +
            "2. Diagnostic Services\n\n" +
            "3. Laboratory Services\n\n" +
            "4. Pediatrics\n\n" +
            "5. Intensive Care Units (ICU)\n\n" +
            "6. Rehabilitation and Mental Health Services\n\n" +
            "7. 24 * 7 Ambulance Services"
        );
    }

    /**
     * Handles the Exit button click (jButton1ActionPerformed in Swing).
     */
    @FXML
    private void onCloseClick(ActionEvent event) {
        // Get the current window (Stage) and close it
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}