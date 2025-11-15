package Project;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * This class provides a static method to get a connection
 * to our hospital_db database.
 * * This is the ONLY file in the project that knows the
 * database name or password.
 */
public class ConnectionProvider {

    public static Connection getCon() {
        try {
            // 1. Define your connection details
            String dbName = "hospital_db"; // <-- Your new DB name
            String jdbcUrl = "jdbc:mysql://localhost:3306/" + dbName;
            String username = "root";
            
            // --------------------------------------------------------
            // !! IMPORTANT: Change this to your real MySQL password !!
            String password = "Dev@1234"; 
            // --------------------------------------------------------

            // 2. Create and return the connection
            Connection con = DriverManager.getConnection(jdbcUrl, username, password);
            return con;

        } catch (Exception e) {
            // If the connection fails, print the error and return null
            e.printStackTrace();
            return null;
        }
    }
}