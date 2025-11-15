package com.hospital;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is the main class that launches the JavaFX application.
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Load our LoginView.fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
        Parent root = loader.load();
        
        // Create a new scene and set the stage (window)
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Hospital Management System - Login");
        stage.show(); // Show the window
    }

    public static void main(String[] args) {
        launch(args);
    }
}