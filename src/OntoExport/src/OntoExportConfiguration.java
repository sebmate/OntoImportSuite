
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
public class OntoExportConfiguration {

    public String ontologyFile = "";
    public boolean createOntology = false;
    public boolean loadFacts = false;
    public boolean createSQLFile = false;
    public boolean directi2b2Export = false;
    public boolean jdbc = false;
    public String rootClass;
    public int inferencingLevel;
    public int outputMode;
    private String propertiesFile = null;
    public int verbosity = 0;
    public boolean stopOnError = true;

    OntoExportConfiguration(String wantedPropertiesFile) {
        propertiesFile = wantedPropertiesFile;

        System.out.println("propertiesFile = " + propertiesFile);
    }

    public void loadConfiguration() {

        System.out.println("loadConfiguration() called");

        try {
            System.out.println("loadConfiguration called!");
            BufferedInputStream stream = null;
            Properties properties = new Properties();
            stream = new BufferedInputStream(new FileInputStream(propertiesFile));
            properties.load(stream);
            stream.close();


            ontologyFile = properties.getProperty("ontologyFile");
            rootClass = properties.getProperty("rootClass");
            createOntology = Boolean.parseBoolean(properties.getProperty("createOntology"));
            jdbc = Boolean.parseBoolean(properties.getProperty("jdbc"));
            loadFacts = Boolean.parseBoolean(properties.getProperty("loadFacts"));
            outputMode = Integer.parseInt(properties.getProperty("outputMode"));
            verbosity = Integer.parseInt(properties.getProperty("verbosity"));

            inferencingLevel = Integer.parseInt(properties.getProperty("inferencingLevel"));


        } catch (IOException ex) {
            Logger.getLogger(OntoExportConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveConfiguration() {
        try {
            System.out.println("saveConfiguration called!");
            Properties properties = new Properties();
            properties.setProperty("ontologyFile", ontologyFile);
            properties.setProperty("rootClass", rootClass);
            properties.setProperty("createOntology", new Boolean(createOntology).toString());
            properties.setProperty("loadFacts", new Boolean(loadFacts).toString());
            properties.setProperty("jdbc", new Boolean(jdbc).toString());
            properties.setProperty("inferencingLevel", Integer.toString(inferencingLevel));
            properties.setProperty("outputMode", Integer.toString(outputMode));
            properties.setProperty("verbosity", Integer.toString(verbosity));

            File propertiesFileFile = new File(propertiesFile);
            properties.store(new FileOutputStream(propertiesFileFile), "OntoExport Properties File");
        } catch (IOException ex) {
            Logger.getLogger(OntoExportConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
}
