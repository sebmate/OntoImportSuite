
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.reasoner.ValidityReport;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sebmate
 */
public class SourceStatementCreator {

    private static class SourceAnnotatorException extends Exception {

        public SourceAnnotatorException(String string) {
            super(string);
        }
    }
    
    OntModel ontModel = null;
    String mappingSystemNameSpace = null;
    String rightNameSpace = null;
    Resource subject = null;
    OntProperty predicate = null;
    Resource object = null;

    public SourceStatementCreator(OntModel ontModel, String mappingSystemNameSpace, String rightNameSpace) {
        this.ontModel = ontModel;
        this.mappingSystemNameSpace = mappingSystemNameSpace;
        this.rightNameSpace = rightNameSpace;
    }

    void parseAndSetStatement(String statement) throws SourceAnnotatorException {

        // split the statement into SUBJECT, PREDICATE and OBJECT

        int firstSpacePos = statement.indexOf(" ");
        int secondSpacePos = statement.indexOf(" ", firstSpacePos + 1);
        int thirdSpacePos = statement.indexOf(" ", secondSpacePos + 1);
        if (thirdSpacePos == -1) {
            thirdSpacePos = statement.indexOf(".", secondSpacePos + 1);
        }

        if ((firstSpacePos == -1) || (secondSpacePos == -1) || (thirdSpacePos == -1) || (statement.indexOf("  ") != -1)) {
            throw new SourceAnnotatorException("Bad Syntax");
        }

        String subj = rightNameSpace + statement.substring(0, firstSpacePos);
        String pred = mappingSystemNameSpace + statement.substring(firstSpacePos + 1, secondSpacePos);
        String obj = rightNameSpace + statement.substring(secondSpacePos + 1, thirdSpacePos);

        subject = ontModel.getResource(subj);
        if (subject == null) {
            throw new SourceAnnotatorException("Resource " + subj + " not found! It does not exist in the source system ontology.");
        }
        predicate = ontModel.getObjectProperty(pred);
        if (predicate == null) {
            throw new SourceAnnotatorException("Predicate " + pred + " not found! It is not defined in the mapping system ontology.");
        }
        object = ontModel.getResource(obj);
        if (object == null) {
            throw new SourceAnnotatorException("Resource " + obj + " not found! It does not exist in the source system ontology.");
        }
    }

    void addStatement() throws SourceAnnotatorException {
        ontModel.add(ontModel.createStatement(subject, predicate, object));
    }

    OntModel getModel() {
        return ontModel;
    }

    public static void main(String[] args) {   // test method

        String inputFile = "/Users/sebmate/NetBeansProjects/QuickMapp/DPKK-Mapping.owl";
        //create an input stream for the input file
        FileInputStream inputStream = null;

        //JOptionPane.showMessageDialog(null,"This is an error message", "Error", JOptionPane.ERROR_MESSAGE);

        try {
            inputStream = new FileInputStream(inputFile);
        } catch (FileNotFoundException e) {
            System.err.println("'" + inputFile + "' is an invalid input file.");
        }

        System.out.println("Setting up Jena model with inferencing level 'none' ...");

        String mappingNameSpace = "http://www.uk-erlangen.de/DPKK-Mapping-Leer#";
        String mappingSystemNameSpace = "http://www.uk-erlangen.de/OntoMappingSystem#";
        String leftNameSpace = "http://www.uk-erlangen.de/DPKK-Datensatz-Erlangen#";
        String rightNameSpace = "http://www.uk-erlangen.de/Soarian#";

        String exp1 = "Value14369_1 hasDate Value14397_1 .";
        String exp2 = "Value14369_1 hasDate Value14397_1.";
        String exp3 = "Value14369_1 hasDate  Value14397_1.";

        OntModel myModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        SourceStatementCreator mySourceAnnotator = new SourceStatementCreator(myModel, mappingSystemNameSpace, rightNameSpace);

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

        try {
            mySourceAnnotator.parseAndSetStatement(exp1);
            mySourceAnnotator.addStatement();
            mySourceAnnotator.parseAndSetStatement(exp2);
            mySourceAnnotator.addStatement();
            mySourceAnnotator.parseAndSetStatement(exp3);
            mySourceAnnotator.addStatement();
        } catch (SourceAnnotatorException ex) {
            Logger.getLogger(SourceStatementCreator.class.getName()).log(Level.SEVERE, null, ex);
        }

        OntModel mappedModel = mySourceAnnotator.getModel();

        try {
            FileOutputStream outFile = new FileOutputStream("/Users/sebmate/Desktop/testmapping.owl");

            mappedModel.write(outFile);
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
