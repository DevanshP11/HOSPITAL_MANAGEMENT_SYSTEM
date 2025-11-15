package Project;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectionProvider {
    // The properties object will store the data from db.properties
    private static Properties dbProperties = new Properties();

    // Static block runs when the class is loaded, loading the configuration
    static {
        try (InputStream input = ConnectionProvider.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            
            if (input == null) {
                System.out.println("ERROR: db.properties file not found in the classpath!");
            } else {
                dbProperties.load(input);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getCon() {
        try {
            // Load the driver (only needed for older JDBC versions, but safe to keep)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Get connection details from the loaded properties
            String url = dbProperties.getProperty("db.url");
            String user = dbProperties.getProperty("db.user");
            String password = dbProperties.getProperty("db.password");
            
            // Establish the connection
            Connection con = DriverManager.getConnection(url, user, password);
            return con;
            
        } catch (Exception e) {
            System.out.println("Connection Failed! Ensure MySQL is running and db.properties is correct.");
            e.printStackTrace();
            return null;
        }
    }
}