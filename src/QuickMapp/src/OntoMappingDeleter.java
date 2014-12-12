
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.ValidityReport;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OntoMappingDeleter extends OntoMappingParser {

    private ArrayList deleteNodes;

    public OntoMappingDeleter(OntModel ontModel, String mappingSystemNS, String mappingNS) {
        this.ontModel = ontModel;
        this.mappingSystemNS = mappingSystemNS;
        this.mappingNS = mappingNS;
        touchedNodes = new ArrayList();
    }

    void deleteMapping(String destClass, String startClass) {

        deleteNodes = new ArrayList();
        System.out.println("Ontology tree deleting requested for: " + startClass);

        parseOntology(startClass);  // parse the ontology in order to fill the "touchedNodes" array

        Resource destClassR = ontModel.getResource(destClass);
        Property hasImport = ontModel.getProperty(mappingSystemNS, "hasImport");
        Resource startClassR = ontModel.getResource(startClass);

        Statement imp = ontModel.createStatement(destClassR, hasImport, startClassR);

        System.out.println("Trying to delete: " + imp);
        ontModel.remove(imp);
               

        // iterate over all touchedNodes and collect all made statements:

        for (int i = 0; i < touchedNodes.size(); i++) {
            System.out.println("Marking for deletion: " + (String) touchedNodes.get(i));
            Individual myIndividual = ontModel.getIndividual((String) touchedNodes.get(i));
            for (StmtIterator si = myIndividual.listProperties(); si.hasNext();) {
                Statement stmt = (Statement) si.next();
                deleteNodes.add(stmt);
                System.out.println(stmt);
            }
        }

        // now delete all statements:

        for (int i = 0; i < deleteNodes.size(); i++) {
            Statement delSt = (Statement) deleteNodes.get(i);
            System.out.println("Deleting statement: " + delSt);
            ontModel.remove(delSt);
        }

    }

    public static void main(String[] args) throws IOException {   // test method

        String inputFile = "/Users/sebmate/NetBeansProjects/QuickMapp/DPKK-Mapping.owl";

        //create an input stream for the input file
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(inputFile);
        } catch (FileNotFoundException e) {
            System.err.println("'" + inputFile + "' is an invalid input file.");
        }

        OntModel myModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        System.out.println("Setting up Jena model with inferencing level 'none' ...");

        // Load the facts into the ontModel:
        myModel.read(inputStream, "");

        // Validate the file:
        System.out.println("Validating OWL file ...");
        ValidityReport validityReport = myModel.validate();

        if (validityReport != null && !validityReport.isValid()) {
            Iterator i = validityReport.getReports();
            while (i.hasNext()) {
                System.err.println(((ValidityReport.Report) i.next()).getDescription());
            }
        }

        OntoMappingDeleter myOntoMappingDeleter = new OntoMappingDeleter(myModel, "http://www.uk-erlangen.de/OntoMappingSystem#", "");
        myOntoMappingDeleter.deleteMapping("http://www.uk-erlangen.de/DPKK-Datensatz-Erlangen#A13-A14_PSA-Wert", "http://www.uk-erlangen.de/DPKK-Mapping#Result_Of_A13-A14_PSA-Wert_Operation_IF1");

        try {
            FileOutputStream outFile = new FileOutputStream("/Users/sebmate/Desktop/testmapping.owl");
            myModel.write(outFile, "TURTLE");
            //myModel.write(outFile);
            try {
                outFile.close();
            } catch (IOException ex) {
                Logger.getLogger(QuickMappForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(QuickMappForm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
