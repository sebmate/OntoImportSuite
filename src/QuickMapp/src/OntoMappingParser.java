
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.ValidityReport;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OntoMappingParser {

    OntModel ontModel;
    String mappingString;
    String mappingSystemNS;
    String mappingNS;
    List touchedNodes;

    public OntoMappingParser() {
    }

    public OntoMappingParser(OntModel ontModel, String mappingSystemNS, String mappingNS) {
        this.ontModel = ontModel;
        this.mappingSystemNS = mappingSystemNS;
        this.mappingNS = mappingNS;

        touchedNodes = new ArrayList();
    }

    public String getMappingCode(String startClass) {

        mappingString = "";
        /*if (startClass.indexOf(" ") == -1) { // it is a leaf, not an expression
         mappingString = startClass;
         } else { // it is an expression*/
        parseOntology(startClass);
        //}
        return mappingString;

    }

    public void parseOntology(String startClass) {

        System.out.println("Getting mapping code for " + startClass);

        List proptertiesList = new ArrayList();
        Individual myIndividual = ontModel.getIndividual(startClass);

        if (myIndividual != null) {

            // iterate through all properties to get the count of the "hasOperand..." statments:
            StmtIterator iProperties = null;
            iProperties = myIndividual.listProperties();
            int numOperands = 0;
            while (iProperties.hasNext()) {
                Statement s = (Statement) iProperties.next();
                String predicateName = s.getPredicate().getLocalName();  // PREDICATE
                String objectName = s.getObject().toString();            // OBJECT
                //System.out.println("Predicate: " + predicateName);
                //System.out.println("Object:" + objectName);
                if (predicateName.startsWith("hasOperand")) {
                    numOperands++;
                }
            }
            iProperties.close();

            // get the predicates for the "hasOperand..." statements:
            Property hasCommandType, hasStringValue;

            hasCommandType = ontModel.getProperty(mappingSystemNS + "hasCommandType");
            RDFNode s2 = myIndividual.getPropertyValue(hasCommandType);
            hasStringValue = ontModel.getProperty(mappingSystemNS + "hasStringValue");

            if (s2 != null) { // it is not a leaf, but has command
                String commandType = s2.toString();
                commandType = commandType.substring(commandType.indexOf("#") + 1);
                mappingString += commandType + " ";
                Property hasOperand, hasDataOperand;

                for (int i = 1; i <= numOperands; i++) {

                    RDFNode s3 = null;

                    hasOperand = ontModel.getProperty(mappingSystemNS + "hasOperand" + i);
                    RDFNode s = myIndividual.getPropertyValue(hasOperand);

                    if (s != null) {
                        Individual ind = ontModel.getIndividual(s.toString());
                        System.out.println("- " + ind);
                        if (ind != null) {
                            s3 = ind.getPropertyValue(hasCommandType);
                        }
                    }

                    if (s3 != null) {
                        mappingString += "(";
                        parseOntology(s.toString());
                        mappingString += ")";
                    } else {

                        // Try to find a hasStringValue Property (then the node is a String):
                        RDFNode s4 = null;
                        Individual ind = ontModel.getIndividual(s.toString());
                        if (ind != null) {
                            s4 = ind.getPropertyValue(hasStringValue);
                        }

                        if (s4 != null) {  // the node is a String

                            String niceName = s4.toString();
                            mappingString += niceName;

                            System.out.println("Touching String node: " + ind.toString());
                            touchedNodes.add(ind.toString());

                        } else { // the node is a normal node
                            String niceName = s.toString();
                            niceName = niceName.substring(niceName.indexOf("#") + 1);
                            mappingString += niceName;
                        }
                    }

                    if (i < numOperands) {
                        mappingString += " ";
                    }
                }
            } else {

                String niceName = startClass;
                niceName = niceName.substring(niceName.indexOf("#") + 1);
                mappingString += niceName;
            }

            //-------------
            //System.out.println("Deleting Individual: " + myIndividual);
            System.out.println("Touching node: " + myIndividual.toString());
            touchedNodes.add(myIndividual.toString());

        } else {
            System.out.println("Class is null!");
        }
    }

    public static void main(String[] args) throws IOException {   // test method

        //String inputFile = "/Users/sebmate/NetBeansProjects/QuickMapp/DPKK-Mapping.owl";
        String inputFile = "MyProject-Mapping.owl";

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
                System.err.println(
                        ((ValidityReport.Report) i.next()).getDescription());
            }
        }

        OntoMappingParser myOntoMappingParser = new OntoMappingParser(myModel, "http://www.uk-erlangen.de/OntoMappingSystem#", "");

        String output = "";

        output = myOntoMappingParser.getMappingCode("http://www.example.org/MyProject/MappingOntology#Result_Of_Age_Operation_ADD1");
        System.out.println("Mapping-Code: " + output);
        output = myOntoMappingParser.getMappingCode("http://www.example.org/MyProject/MappingOntology#Result_Of_Age_Operation_ADD2");
        System.out.println("Mapping-Code: " + output);

        /*
         output = myOntoMappingParser.getMappingCode("http://www.uk-erlangen.de/DPKK-Mapping-Leer#Result_Of_A13-A14_PSA-Wert_Operation_IF4");
         System.out.println("Mapping-Code: " + output);

         output = myOntoMappingParser.getMappingCode("http://www.uk-erlangen.de/DPKK-Mapping-Leer#Result_Of_A13-A14_PSA-Wert_Operation_ADD1");
         System.out.println("Mapping-Code: " + output);
         */
    }
}
