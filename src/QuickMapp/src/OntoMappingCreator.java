
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.reasoner.ValidityReport;
import com.hp.hpl.jena.vocabulary.RDFS;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
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
class OntoMappingCreator {

    public static class ParserException extends Exception {

        public ParserException(String string) {
            super(string);
        }
    }
    private String myExpression;
    private String mappingNameSpace;
    private String mappingSystemNameSpace;
    private String leftNameSpace;
    private String rightNameSpace;
    private OntModel ontModel = null;
    private String targetRessource;
    private String expression;
    private int indents = 0;
    private int operationCnt = 0;

    public OntoMappingCreator(OntModel model, String mappingNameSpace, String mappingSystemNameSpace, String leftNameSpace, String rightNameSpace) {
        this.myExpression = expression;
        this.mappingSystemNameSpace = mappingSystemNameSpace;
        this.mappingNameSpace = mappingNameSpace;
        this.leftNameSpace = leftNameSpace;
        this.rightNameSpace = rightNameSpace;
        this.ontModel = model;
    }

    void addExpression(String expression) throws ParserException {

        targetRessource = ltrim(rtrim(expression.substring(0, expression.indexOf(":"))));
        expression = ltrim(rtrim(expression.substring(expression.indexOf(":") + 1)));

        System.out.println("");
        System.out.println("Adding expression: " + expression);
        System.out.println("Target is: " + leftNameSpace + targetRessource);

        OntProperty predicate = ontModel.createObjectProperty(mappingSystemNameSpace + "hasImport");
        if (predicate == null) {
            System.out.println("Error: Predicate is NULL!");
        }


        Individual object;

        /*
        // -- We need to enumerate all "hasImport" Statements. Find out which "number" is free: --

        // Iterate through all statements (statement = OBJECT PREDICATE OBJECT)
        String predicateName = "";
        String objectName = "";
        int tryEnum = 0;
        String Enumerator = "";

        do {

        tryEnum++;
        Enumerator = "" + tryEnum;

        StmtIterator iProperties = subject.listProperties();
        while (iProperties.hasNext()) {
        Statement s = (Statement) iProperties.next();
        predicateName = s.getPredicate().getLocalName();  // PREDICATE
        objectName = s.getObject().toString();            // OBJECT

        if (objectName.indexOf('^') != -1) {
        objectName = objectName.substring(0, objectName.indexOf('^'));
        }
        // Ouput infos, depending on the verbosity setting:
        if (predicateName.equals("hasEnumeratedImport") && objectName.equals(Enumerator)) {
        Enumerator = "";
        }
        System.out.println("XXX: " + predicateName + " " + objectName);

        }
        iProperties.close();

        } while (Enumerator.equals(""));

        System.out.println("hasEnumeratedImport = " + Enumerator);

        DatatypeProperty hasEnumeratedImport = ontModel.getDatatypeProperty(mappingSystemNameSpace + "hasEnumeratedImport");
        ontModel.add(ontModel.createStatement(subject, hasEnumeratedImport, Enumerator));

        // -- end of enumeration --
         */


        if (expression.indexOf(" ") == -1) {                                // it is a leaf, not an expression
            object = ontModel.getIndividual(rightNameSpace + expression);
        } else {                                                            // it is an expression
            String rootNode = processExpression(expression);
            object = ontModel.getIndividual(mappingNameSpace + rootNode);

        }

        if (object == null) {
            System.out.println("Error: Object is NULL!");
        }

        OntClass subject = ontModel.getOntClass(leftNameSpace + targetRessource);
        if (subject != null) {
            ontModel.add(ontModel.createStatement(subject, predicate, object));
        } else {
            Individual subject2 = ontModel.getIndividual(leftNameSpace + targetRessource);
            if (subject2 == null) {
                System.out.println("Error: Subject is NULL!");
            }

            ontModel.add(ontModel.createStatement(subject2, predicate, object));
        }

    }

    OntModel getModel() {
        return ontModel;
    }

    private void addOperationStatement(String sub, String pred, String obj, String expressionCommand, String targetRessource) throws ParserException {


        OntClass c = ontModel.getOntClass(mappingSystemNameSpace + "UnprocessedItem");
        Individual subject = c.createIndividual(sub);

        // say that the operation node has the operand obj:
        OntProperty predicate = ontModel.createObjectProperty(pred);


        if (obj.contains("\"")) { // the operand is a String, encapsulated in quotations marks

            // Introduce a new dummy node which holds the String value

            // DatatypeProperty hasOperandX = ontModel.getObjectProperty(pred);

            OntClass c2 = ontModel.getOntClass(mappingSystemNameSpace + "UnprocessedItem");
            // ontModel.add(ontModel.createStatement(c, RDFS.subClassOf, opn));
            // sub = targetRessource + "_Import_" + Enumerator + "_" + expressionCommand + "-Operation_" + operationCounter;

            int thisCounter = 0;
            Individual test;
            do {
                thisCounter++;
                sub = targetRessource + "_StringNode_" + thisCounter;
                test = ontModel.getIndividual(mappingNameSpace + sub);
            } while (test != null);
            System.out.println(mappingNameSpace + sub);
            Individual subject2 = c2.createIndividual(mappingNameSpace + sub);

            DatatypeProperty hasStringValue = ontModel.getDatatypeProperty(mappingSystemNameSpace + "hasStringValue");
            Statement statement = ontModel.createStatement(subject2, hasStringValue, obj);
            //System.out.println(statement);

            System.out.println(targetRessource);
            ontModel.add(statement);

            ontModel.add(ontModel.createStatement(subject, predicate, subject2));
            ontModel.add(statement);

        } else { // the operand is a variable, not a string
            Individual object = ontModel.getIndividual(obj);
            if (object == null) {
                throw new ParserException("Operand " + obj + " does not exist");
            }
            // add the statement to the model:
            ontModel.add(ontModel.createStatement(subject, predicate, object));
        }

        OntClass op = ontModel.getOntClass(mappingSystemNameSpace + "OperationCommand");
        Individual opIndiv = op.createIndividual(mappingSystemNameSpace + expressionCommand);
        OntProperty predicate2 = ontModel.createObjectProperty(mappingSystemNameSpace + "hasCommandType");
        ontModel.add(ontModel.createStatement(subject, predicate2, opIndiv));

    }

    private String processExpression(String exp) throws ParserException {

        String Enumerator = "1";

        indents++;
        String expressionCommand = "";
        String expressionParameters = "";
        String bracketContents = "";
        int openBrackets = 0;
        int charPos = 0;
        boolean inAString = false;
        String newOperand = "";

        String sub = "", pred = "", obj = "";
        boolean processable = true;

        operationCnt++;
        int operationCounter = operationCnt;
        int operandCounter = 0;

        // Split expressionCommand from the expressionParameters:

        expressionCommand = exp.substring(0, exp.indexOf(" "));
        expressionParameters = exp.substring(exp.indexOf(" ") + 1);

        System.out.println("Current expression = " + exp);


        OntClass c = ontModel.getOntClass(mappingSystemNameSpace + "UnprocessedItem");
        // ontModel.add(ontModel.createStatement(c, RDFS.subClassOf, opn));
        // sub = targetRessource + "_Import_" + Enumerator + "_" + expressionCommand + "-Operation_" + operationCounter;

        int thisCounter = 0;
        Individual test;

        do {

            thisCounter++;
            sub = "Result_Of_" + targetRessource + "_Operation_" + expressionCommand + thisCounter;
            test = ontModel.getIndividual(mappingNameSpace + sub);

        } while (test != null);

        Individual subject = c.createIndividual(mappingNameSpace + sub);

        DatatypeProperty hasEnumeratedImport = ontModel.getDatatypeProperty(mappingSystemNameSpace + "isResultOfExpression");
        ontModel.add(ontModel.createStatement(subject, hasEnumeratedImport, exp));

        Individual tempTable = ontModel.getIndividual(mappingSystemNameSpace + "OntoExportTempTable");

        if (tempTable == null) {
            throw new ParserException("tempTable is NULL!");
        }

        ObjectProperty hasSourceTable = ontModel.getObjectProperty(mappingSystemNameSpace + "hasSourceTable");

        if (hasSourceTable == null) {
            throw new ParserException("hasSourceTable is NULL!");
        }


        ontModel.add(ontModel.createStatement(subject, hasSourceTable, tempTable));

        //indent(indents);
        //System.out.println("Command: " + expressionCommand);


        // Split all the expressionParameters:
        while (charPos < expressionParameters.length()) {

            if (expressionParameters.charAt(charPos) == '(') { // the bracketContents starts with a bracket
                openBrackets = 1;
                bracketContents = "";
                while (openBrackets > 0) {           // iterate over the complete bracket to get its contents
                    charPos++;
                    if (expressionParameters.charAt(charPos) == '(') {
                        openBrackets++;
                    } else if (expressionParameters.charAt(charPos) == ')') {
                        openBrackets--;
                    }
                    if (openBrackets > 0) {
                        bracketContents = bracketContents + expressionParameters.charAt(charPos);
                    }
                }

                // Operand is an expression:

                String operandCommand = processExpression(bracketContents);

                operandCounter++;

                //sub = mappingNameSpace + targetRessource + "_Import_" + Enumerator + "_" + expressionCommand + "-Operation_" + operationCounter;
                pred = mappingSystemNameSpace + "hasOperand" + operandCounter;
                obj = mappingNameSpace + operandCommand;

                addOperationStatement(mappingNameSpace + sub, pred, obj, expressionCommand, targetRessource);
                processable = false;


            } else {  // the bracketContents starts with no bracket

                if (expressionParameters.charAt(charPos) == '"') {
                    inAString = !inAString;
                }
                if (inAString == true) {
                    newOperand += expressionParameters.charAt(charPos);
                }
                if ((inAString == false) && (expressionParameters.charAt(charPos) != ' ')) {
                    newOperand += expressionParameters.charAt(charPos);
                }
                if (((inAString == false) && (expressionParameters.charAt(charPos) == ' ')) || (charPos == expressionParameters.length() - 1)) {
                    if (ltrim(rtrim(newOperand)).isEmpty() == false) {

                        operandCounter++;

                        //sub = mappingNameSpace + targetRessource + "_Import_" + Enumerator + "_" + expressionCommand + "-Operation_" + operationCounter;

                        if (newOperand.startsWith("\"")) {  // the operand is a String, encapsulated in quotations marks
                            pred = mappingSystemNameSpace + "hasOperand" + operandCounter;
                            obj = newOperand;
                        } else { // the operand is a variable, not a String
                            pred = mappingSystemNameSpace + "hasOperand" + operandCounter;
                            obj = rightNameSpace + newOperand;
                        }

                        System.out.println("Object is " + newOperand);

                        addOperationStatement(mappingNameSpace + sub, pred, obj, expressionCommand, targetRessource);

                        if (processable != false) {
                            processable = true;
                        }

                    }
                    newOperand = "";
                }
            }
            charPos++;
        }

        // the operation node is an individual of either the classes "Processable" or "NonProcessable" (stored in processable):

        /*
        String processableString = "UnprocessableItem";
        if (processable == true) {
        processableString = "ProcessableItem";
        }
        OntClass c2 = ontModel.getOntClass(mappingSystemNameSpace + processableString);
        Individual subject2 = c2.createIndividual(mappingNameSpace + sub);
         */

        indents--;
        //operationCnt--;
        return sub; //targetRessource + "_Import_" + Enumerator + "_" + expressionCommand + "-Operation_" + operationCounter;

    }

    /* remove leading whitespace */
    public static String ltrim(String source) {
        return source.replaceAll("^\\s+", "");
    }

    /* remove trailing whitespace */
    public static String rtrim(String source) {
        return source.replaceAll("\\s+$", "");
    }

    public static void main(String[] args) {   // test method

        String inputFile = "UKER-DPKK-Mapping.owl";
        //create an input stream for the input file
        FileInputStream inputStream = null;

        try {
            inputStream = new FileInputStream(inputFile);
        } catch (FileNotFoundException e) {
            System.err.println("'" + inputFile + "' is an invalid input file.");
        }

        System.out.println("Setting up Jena model with inferencing level 'none' ...");

        String mappingNameSpace = "http://www.uk-erlangen.de/UKER-DPKK-Mapping#";
        String mappingSystemNameSpace = "http://www.uk-erlangen.de/OntoMappingSystem#";

        String leftNameSpace = "http://www.dpkk.de/Datensatz#";
        String rightNameSpace = "http://www.uk-erlangen.de/Soarian#";

        //String exp = "A13-A14_PSA-Wert : IF (GREATER Value14397_1 Value14398_1) (IF (GREATER Value14397_1 Value14399_1) (IF (GREATER Value14397_1 Value14400_1) Value14369_1))";
        String exp1 = "A13-A14_PSA-Wert : ADD Value14712_14369_1 \"1000\"";
        String exp2 = "A13-A14_PSA-Wert : ADD Value14712_14369_1 \"1000\"";

        OntModel myModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

        OntoMappingCreator myMappingCreator = new OntoMappingCreator(myModel, mappingNameSpace, mappingSystemNameSpace, leftNameSpace, rightNameSpace);

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
            //myMappingCreator.addExpression(exp);
            myMappingCreator.addExpression(exp1);
            myMappingCreator.addExpression(exp2);
        } catch (ParserException ex) {
            Logger.getLogger(OntoMappingCreator.class.getName()).log(Level.SEVERE, null, ex);
        }

        OntModel mappedModel = myMappingCreator.getModel();

        try {
            FileOutputStream outFile = new FileOutputStream("/Users/sebmate/Desktop/testmapping.owl");
            //mappedModel.write(outFile, "TURTLE");
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
