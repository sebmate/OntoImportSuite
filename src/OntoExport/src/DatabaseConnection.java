
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
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
    private String i2b2schema = "";
    String server = "";
    String port = "";
    String SID = "";
    Connection conn = null;
    private String lastSQL = "";
    boolean enabled = false;
    private int updateCount = 0;
    private String lastSQLError = "";

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

            if (!prop.getProperty("db.i2b2schema").equalsIgnoreCase("")) {
                i2b2schema = prop.getProperty("db.i2b2schema") + ".";
            }
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

    int executeSQL(String sqlCommand) {
        lastSQL = sqlCommand;

        System.out.println(sqlCommand);

        if (enabled == false) {
            return 1;
        }

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlCommand);

            setUpdateCount(stmt.getUpdateCount());
            setLastSQLError("");

            rs.close();
            stmt.close();

            return 1;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
            setUpdateCount(0);
            setLastSQLError(ex.toString());
            return 0;
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

    /**
     * @return the i2b2schema
     */
    public String getI2b2schema() {
        return i2b2schema;
    }

    /**
     * @return the lastSQLError
     */
    public String getLastSQLError() {
        return lastSQLError;
    }

    /**
     * @param lastSQLError the lastSQLError to set
     */
    public void setLastSQLError(String lastSQLError) {
        this.lastSQLError = lastSQLError;
    }

    /**
     * @return the updateCount
     */
    public int getUpdateCount() {
        return updateCount;
    }

    /**
     * @param updateCount the updateCount to set
     */
    public void setUpdateCount(int updateCount) {
        this.updateCount = updateCount;
    }

    public static String getStackTrace(Throwable aThrowable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }

    /**
     * Defines a custom format for the stack trace as String.
     */
    public static String getCustomStackTrace(Throwable aThrowable) {
        //add the class name and any message passed to constructor
        final StringBuilder result = new StringBuilder("BOO-BOO: ");
        result.append(aThrowable.toString());
        final String NEW_LINE = System.getProperty("line.separator");
        result.append(NEW_LINE);

        //add each element of the stack trace
        for (StackTraceElement element : aThrowable.getStackTrace()) {
            result.append(element);
            result.append(NEW_LINE);
        }
        return result.toString();
    }
}
