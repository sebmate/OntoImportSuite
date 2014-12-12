
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author sebmate
 */
public class DatabaseConnection {

    String driver = "";
    String url = "";
    String user = "";
    String password = "";
    String server = "";
    String port = "";
    String SID = "";
    Connection conn = null;
    private String lastSQL = "";
    boolean enabled = false;

    DatabaseConnection(String configurationFile, boolean enabled) {

        this.enabled = enabled;

        FileInputStream in = null;
        try {
            in = new FileInputStream(configurationFile);
            Properties prop = new Properties();
            prop.load(in);
            in.close();
            // Login-Daten fuer die Datenbank einlesen:
            driver = prop.getProperty("db.driver");
            url = prop.getProperty("db.urlprefix") + prop.getProperty("db.server") + ":" + prop.getProperty("db.port") + ":" + prop.getProperty("db.SID");
            user = prop.getProperty("db.username");
            password = prop.getProperty("db.password");
            server = prop.getProperty("db.password");
            port = prop.getProperty("db.port");
            SID = prop.getProperty("db.SID");

        } catch (IOException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    void initConnention() {


        if (!enabled) {
            return;
        }

        System.out.println("Creating connection with: " + url + ", " + user + ", " + password);

        try {
            // Treiber laden
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
            } catch (ClassNotFoundException e) {
                System.out.println("Fehler: oracle.jdbc.driver.OracleDriver nicht gefunden!");
                System.out.println(e);
                System.exit(1);
            }
            conn = DriverManager.getConnection(url, user, password);

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void executeSQL(String sqlCommand) {
        lastSQL = sqlCommand;


        if (enabled == false) {
            return;
        }

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlCommand);
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getLastSQL() {
        return lastSQL;
    }

    public void closeConnection() {
        if (enabled == false) {
            return;
        }

        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
