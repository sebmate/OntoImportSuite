
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ValidityReport;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import java.io.*;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.mindswap.pellet.jena.PelletReasonerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import javax.swing.text.Document;

/**
 *
 * @author sebmate
 */
public class Exporter implements Runnable {

    OntoExportForm myOntoExportForm;
    private DatabaseConnection conn;
    OntModel model = null;
    int suffixCounter = 0;
    String randomPrefix = "export"; // (new Date()).getTime();
    String OntXML = "";
    boolean MSSQL4GTDS = false; // <======== !!!!!!!!!!!!!!!!!!!!!!!!!!
    private int sqlErrors = 0;
    private int sqlOKs = 0;

    public Exporter(OntoExportForm testB) {
        this.myOntoExportForm = testB;
    }
    private OntoExportConfiguration exportConfig;

    void setConfig(OntoExportConfiguration config) {
        exportConfig = config;
    }
    String mappingSystemNameSpace = "http://www.uk-erlangen.de/OntoMappingSystem#";

    public void run() {

        File file = new File("SQL-Errors.txt");
        FileWriter writer = null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyy HH:mm:ss");
        String uhrzeit = sdf.format(new Date());
        try {
            writer = new FileWriter(file, false);
            writer.write("================================================================= \n");
            writer.write("Export started at " + uhrzeit + " \n\n");
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Exporter.class.getName()).log(Level.SEVERE, null, ex);
        }


        String inputFileName = exportConfig.ontologyFile;

        //create an input stream for the input file
        FileInputStream inputStream = null;

        try {
            inputStream = new FileInputStream(inputFileName);
        } catch (FileNotFoundException e) {
            System.err.println("'" + inputFileName + "' is an invalid input file.");
            return;
        }

        // Create the appropriate Jena model:

        switch (exportConfig.inferencingLevel) {
            case 0: // None
                myOntoExportForm.consoleWrite("Setting up Jena model without inferencing ...");
                model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
                break;
            case 1: // RDFS
                myOntoExportForm.consoleWrite("Setting up Jena model with inferencing level 'RDFS' ...");
                model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF);
                break;
            case 2: // OWL-DL
                myOntoExportForm.consoleWrite("Setting up Jena model with inferencing level 'OWL-DL' ...");
                Reasoner reasoner = PelletReasonerFactory.theInstance().create();
                Model infModel = ModelFactory.createInfModel(reasoner, ModelFactory.createDefaultModel());
                model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM, infModel);

                /*
                 * OntModelSpec spec = new
                 * OntModelSpec(OntModelSpec.OWL_DL_MEM);
                 * com.hp.hpl.jena.reasoner.Reasoner reasoner =
                 * PelletReasonerFactory.theInstance().create();
                 * spec.setReasoner(reasoner); Model infModel =
                 * ModelFactory.createInfModel(reasoner,
                 * ModelFactory.createDefaultModel()); model =
                 * ModelFactory.createOntologyModel(spec, infModel);
                 */

                break;
        }

        myOntoExportForm.consoleWrite("Loading Ontology ...");

        // Load the facts into the model:
        model.read(inputStream, "http://www.uk-erlangen.de/mappingNamespace#");

        // Validate the file:
        myOntoExportForm.consoleWrite("Validating Ontology ...");

        ValidityReport validityReport = model.validate();
        if (validityReport != null && !validityReport.isValid()) {
            Iterator i = validityReport.getReports();
            while (i.hasNext()) {
                System.err.println(
                        ((ValidityReport.Report) i.next()).getDescription());
            }
            return;
        }

        myOntoExportForm.consoleWrite("Setting up i2b2 database connection ...");
        conn = new DatabaseConnection("ExportConnection.properties", exportConfig.jdbc);
        conn.initConnention();

        if (MSSQL4GTDS == false) {

            conn.executeSQL("CREATE OR REPLACE FUNCTION SAFE_TO_NUMBER(TXT IN VARCHAR2) RETURN NUMBER IS BEGIN RETURN TO_NUMBER(TXT); EXCEPTION WHEN VALUE_ERROR THEN RETURN NULL; END SAFE_TO_NUMBER; /");
            myOntoExportForm.sqlConsoleWrite("CREATE OR REPLACE FUNCTION SAFE_TO_NUMBER(TXT IN VARCHAR2) RETURN NUMBER IS \n"
                    + "BEGIN\n"
                    + "  RETURN TO_NUMBER(TXT);\n"
                    + "EXCEPTION WHEN VALUE_ERROR THEN\n"
                    + "  RETURN NULL;\n"
                    + "END SAFE_TO_NUMBER;\n"
                    + "/\n");
            myOntoExportForm.consoleWrite("  " + conn.getUpdateCount() + " row(s) inserted.");
            handleSQLError(conn.getLastSQL(), conn.getLastSQLError());


            conn.executeSQL("EXECUTE DBMS_ERRLOG.CREATE_ERROR_LOG('OBSERVATION_FACT', 'ONTOEXPORT_ERROR_LOG', NULL, NULL, TRUE)");
            myOntoExportForm.sqlConsoleWrite(conn.getLastSQL() + ";");
            myOntoExportForm.consoleWrite("  " + conn.getUpdateCount() + " row(s) inserted.");
            handleSQLError(conn.getLastSQL(), conn.getLastSQLError());

            conn.executeSQL("TRUNCATE TABLE ONTOEXPORT_ERROR_LOG");
            myOntoExportForm.sqlConsoleWrite(conn.getLastSQL() + ";");
            myOntoExportForm.consoleWrite("  " + conn.getUpdateCount() + " row(s) inserted.");
            handleSQLError(conn.getLastSQL(), conn.getLastSQLError());

        }

        i2b2OntologyEntry rootEntry = new i2b2OntologyEntry(conn);

        if (exportConfig.createOntology == true) {

            myOntoExportForm.consoleWrite("Truncating i2b2 ontology database table ...");

            rootEntry.emptyTable();
            myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() + ";");
            myOntoExportForm.consoleWrite("  " + conn.getUpdateCount() + " row(s) inserted.");
            handleSQLError(conn.getLastSQL(), conn.getLastSQLError());

            if (MSSQL4GTDS == false) {
                rootEntry.commit();
            }


            myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() + ";");
            myOntoExportForm.consoleWrite("  " + conn.getUpdateCount() + " row(s) inserted.");
            handleSQLError(conn.getLastSQL(), conn.getLastSQLError());

            rootEntry.C_FULLNAME = "\\i2b2\\";
            rootEntry.C_DIMCODE = "\\i2b2\\";
            rootEntry.C_NAME = "i2b2";
            rootEntry.C_TOOLTIP = "i2b2";
            rootEntry.C_HLEVEL = 0;
            rootEntry.C_VISUALATTRIBUTES = "CA ";
            rootEntry.insertInto();
            myOntoExportForm.consoleWrite("  " + conn.getUpdateCount() + " row(s) inserted.");
            handleSQLError(conn.getLastSQL(), conn.getLastSQLError());
            myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() + ";");
            OntClass artefact = model.getOntClass(exportConfig.rootClass);

            getHierarchicalClass(artefact, 1, "\\i2b2\\");

            myOntoExportForm.consoleWrite("Commiting i2b2 ontology database table changes ...");
            if (MSSQL4GTDS == false) {
                rootEntry.commit();
            }
            myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() + ";");


            myOntoExportForm.exportFinished();
            myOntoExportForm.consoleWrite("i2b2 Ontology creation successfully finished!");


            if (MSSQL4GTDS == false) {
                myOntoExportForm.consoleWrite("Creating CONCEPT_DIMENSION ...");

                //conn.executeSQL("TRUNCATE TABLE " + conn.getI2b2schema() + "CONCEPT_DIMENSION");
                conn.executeSQL("DELETE FROM " + conn.getI2b2schema() + "CONCEPT_DIMENSION");


                myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() + ";");
                myOntoExportForm.consoleWrite("  " + conn.getUpdateCount() + " row(s) inserted.");
                handleSQLError(conn.getLastSQL(), conn.getLastSQLError());

                if (MSSQL4GTDS == false) {
                    conn.executeSQL("COMMIT");
                }
                myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() + ";");
                myOntoExportForm.consoleWrite("  " + conn.getUpdateCount() + " row(s) inserted.");
                handleSQLError(conn.getLastSQL(), conn.getLastSQLError());

                conn.executeSQL("INSERT INTO " + conn.getI2b2schema() + "CONCEPT_DIMENSION (CONCEPT_CD, CONCEPT_PATH, NAME_CHAR, CONCEPT_BLOB, UPDATE_DATE, DOWNLOAD_DATE, IMPORT_DATE, SOURCESYSTEM_CD, UPLOAD_ID) SELECT DISTINCT C_BASECODE, C_FULLNAME, C_NAME, null, null, null, null, null, null FROM " + conn.getI2b2schema() + "I2B2 WHERE C_VISUALATTRIBUTES LIKE 'LA%'");
                myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() + ";");
                myOntoExportForm.consoleWrite("i2b2 " + conn.getI2b2schema() + "CONCEPT_DIMENSION creation successfully finished!");
                myOntoExportForm.consoleWrite("  " + conn.getUpdateCount() + " row(s) inserted.");
                handleSQLError(conn.getLastSQL(), conn.getLastSQLError());

            }

        }


        if (exportConfig.loadFacts == true) {
            FileWriter fstream = null;
            try {
                // Process all StringNodes first:
                myOntoExportForm.consoleWrite("Processing StringNodes ...");
                ResultSet stringNodes;
                stringNodes = runQuery(" select ?subject ?stringValue "
                        + "WHERE { "
                        + "?subject omsys:hasStringValue ?stringValue . "
                        + "}", model);
                while (stringNodes.hasNext()) {

                    QuerySolution soln = stringNodes.nextSolution();

                    RDFNode subject = soln.get("?subject");
                    RDFNode stringValue = soln.get("?stringValue");

                    System.out.println("Found StringNode: " + simplifyURI(subject.toString()) + " = " + stringValue.toString().replace("\"", ""));

                    // mark as "processed":

                    OntClass processed = model.getOntClass("http://www.uk-erlangen.de/OntoMappingSystem#ProcessedItem");
                    Individual sNode = model.getIndividual(subject.toString());
                    sNode.addOntClass(processed);

                }
                myOntoExportForm.consoleWrite("Processing intermediate result nodes ...");

                //conn.executeSQL("TRUNCATE TABLE ONTOEXPORTTEMP");
                //myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() + ";");
                /*
                 * conn.executeSQL("DROP TABLE " + conn.getI2b2schema() +
                 * "ONTOEXPORTTEMP");
                 * myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() +
                 * ";"); conn.executeSQL("CREATE TABLE " + conn.getI2b2schema()
                 * + "ONTOEXPORTTEMP (DOCUMENTID NUMBER(10,0), PATIENTID
                 * NUMBER(10,0), STARTDATE TIMESTAMP (6), ENDDATE TIMESTAMP (6),
                 * VALUE VARCHAR2(2000 BYTE), NODENAME VARCHAR2(100 BYTE))");
                 * myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() +
                 * ";");
                 */
                if (MSSQL4GTDS == false) {
                    conn.executeSQL("CREATE TABLE " + conn.getI2b2schema() + "ONTOEXPORTTEMP (DOCUMENTID NUMBER(10,0), PATIENTID NUMBER(10,0), STARTDATE TIMESTAMP (6), ENDDATE TIMESTAMP (6), VALUE VARCHAR2(2000 BYTE), NODENAME VARCHAR2(100 BYTE))");
                    myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() + ";");
                    myOntoExportForm.consoleWrite("  " + conn.getUpdateCount() + " row(s) inserted.");
                    handleSQLError(conn.getLastSQL(), conn.getLastSQLError());


                    conn.executeSQL("TRUNCATE TABLE " + conn.getI2b2schema() + "ONTOEXPORTTEMP");
                    myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() + ";");
                    myOntoExportForm.consoleWrite("  " + conn.getUpdateCount() + " row(s) inserted.");
                    handleSQLError(conn.getLastSQL(), conn.getLastSQLError());

                    conn.executeSQL("DELETE FROM " + conn.getI2b2schema() + "ONTOEXPORTTEMP");
                    myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() + ";");
                    myOntoExportForm.consoleWrite("  " + conn.getUpdateCount() + " row(s) inserted.");
                    handleSQLError(conn.getLastSQL(), conn.getLastSQLError());

                }
                ResultSet response;
                do {

                    // fetch the next unprocessed node:

                    response = runQuery(" select ?subject ?operator ?operand1 ?operand2"
                            + "?outputTransformation ?selectFilter ?dateItem ?dateEndValue ?dateStartValue "
                            + "?stringValue1 ?stringValue2 "
                            + "WHERE { "
                            + "?subject a omsys:UnprocessedItem . "
                            + "?subject omsys:hasCommandType ?operator . "
                            + "?subject omsys:hasOperand1 ?operand1 . "
                            + "?subject omsys:hasOperand2 ?operand2 . "
                            + "?operand1 a omsys:ProcessedItem . "
                            + "?operand2 a omsys:ProcessedItem . "
                            + "?operator omsys:hasOutputTransformation ?outputTransformation . "
                            + "OPTIONAL { ?operator omsys:hasSelectFilter ?selectFilter . } "
                            + "?operator omsys:hasDateEndValue ?dateEndValue . "
                            + "?operator omsys:hasDateStartValue ?dateStartValue . "
                            + "OPTIONAL {?operand1 omsys:hasStringValue ?stringValue1 . } "
                            + "OPTIONAL {?operand2 omsys:hasStringValue ?stringValue2 . } "
                            + "} LIMIT 1", model);

                    if (response.hasNext()) {

                        QuerySolution soln = response.nextSolution();

                        RDFNode name = soln.get("?subject");
                        //RDFNode opr = soln.get("?operator");
                        RDFNode op1 = soln.get("?operand1");
                        RDFNode op2 = soln.get("?operand2");

                        System.out.println("Found processable node: " + name.toString());

                        RDFNode trafo = soln.get("?outputTransformation");
                        if (trafo == null) {
                            System.out.println("trafo is NULL!");
                        }

                        RDFNode filter = soln.get("?selectFilter");
                        if (filter == null) {
                            System.out.println("filter is NULL!");
                        }

                        RDFNode dateEnd = soln.get("?dateEndValue");
                        if (dateEnd == null) {
                            System.out.println("dateEnd is NULL!");
                        }

                        RDFNode dateStart = soln.get("?dateStartValue");
                        if (dateStart == null) {
                            System.out.println("dateStart is NULL!");
                        }


                        RDFNode stringValue1 = soln.get("?stringValue1");
                        if (stringValue1 != null) {
                            System.out.println("StringValue1: " + stringValue1);
                        }

                        RDFNode stringValue2 = soln.get("?stringValue2");
                        if (stringValue2 != null) {
                            System.out.println("StringValue2: " + stringValue2);
                        }



                        if (name != null) {

                            myOntoExportForm.consoleWrite("Processing node: " + simplifyURI(name.toString()));
                            myOntoExportForm.sqlConsoleWrite("/* Processing node: " + simplifyURI(name.toString()) + " */");

                            SQLQueryFieldLoader fields1 = new SQLQueryFieldLoader(model);
                            SQLQueryFieldLoader fields2 = new SQLQueryFieldLoader(model);
                            SQLQueryFieldLoader fields3 = new SQLQueryFieldLoader(model);

                            if (stringValue1 != null) {
                                fields2.getOperandFields(op2);
                                fields1.superDuperStringTrick(stringValue1, fields2);
                                fields1.getOperandFields(op1);
                            } else {
                                fields1.getOperandFields(op1);
                            }
                            if (stringValue2 != null) {
                                fields1.getOperandFields(op1);
                                fields2.superDuperStringTrick(stringValue2, fields1);
                                fields2.getOperandFields(op2);
                            } else {
                                fields2.getOperandFields(op2);
                            }


                            fields3.getOperandFields(name);

                            OntClass processed = model.getOntClass("http://www.uk-erlangen.de/OntoMappingSystem#ProcessedItem");
                            OntClass unprocessed = model.getOntClass("http://www.uk-erlangen.de/OntoMappingSystem#UnprocessedItem");

                            System.out.println("Subject: " + name.toString());
                            Individual subject = model.getIndividual(name.toString());
                            
                            if (subject == null) System.out.println("Subject is null");
                            
                            if (processed == null) System.out.println("processed is null");
                            if (unprocessed == null) System.out.println("unprocessed is null");
                            
                            subject.addOntClass(processed);
                            subject.removeOntClass(unprocessed);

                            // assign the temp table to the individual:
                            subject = model.getIndividual(name.toString());
                            Individual object = model.getIndividual("http://www.uk-erlangen.de/OntoMappingSystem#OntoExportTempTable");

                            Property predicate = model.getProperty("http://www.uk-erlangen.de/OntoMappingSystem#hasSourceTable");
                            //myOntoExportForm.consoleWrite(model.createStatement(subject, predicate, object).toString());

                            model.add(model.createStatement(subject, predicate, object));

                            // update hasSelectFilter property:
                            DatatypeProperty predicate2 = model.getDatatypeProperty("http://www.uk-erlangen.de/OntoMappingSystem#hasSelectFilter");
                            model.add(model.createStatement(subject, predicate2, "NodeName = '" + simplifyURI(name.toString()) + "'"));

                            String op1FetchSQL = fields1.getTransformedFetchSQL();
                            String op2FetchSQL = fields2.getTransformedFetchSQL();
                            String mySQL = fields3.getSQL();


                            //System.out.println(mySQL);
                            // replace the operand-fetch subselects:


                            mySQL = mySQL.replace("$OPERANDFETCHSQL1$", " " + op1FetchSQL + " ");
                            mySQL = mySQL.replace("$OPERANDFETCHSQL2$", " " + op2FetchSQL + " ");

                            // replace all variables in the generic SQL statement:

                            mySQL = mySQL.replace("$NODENAME$", "'" + simplifyURI(name.toString()) + "'");
                            mySQL = mySQL.replace("$OUTPUT$", simplifyURI(trafo.toString()));

                            // System.out.println(dateStart.toString());
                            // System.out.println(dateEnd.toString());

                            mySQL = mySQL.replace("$DATESTARTVALUE$", simplifyURI(dateStart.toString()));
                            mySQL = mySQL.replace("$DATEENDVALUE$", simplifyURI(dateEnd.toString()));

                            String whereClause = "";

                            if (filter != null) {
                                whereClause = simplifyURI(filter.toString());
                                if (!whereClause.equals("")) {
                                    whereClause = " WHERE " + whereClause + " ";
                                }
                            }

                            mySQL = mySQL.replace("$RESULTFILTER$", whereClause);
                            mySQL = "INSERT INTO " + conn.getI2b2schema() + "OntoExportTemp(NodeName, DocumentID, PatientID, StartDate, EndDate, Value) " + mySQL;

                            conn.executeSQL(mySQL);
                            myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() + ";");
                            myOntoExportForm.consoleWrite("  " + conn.getUpdateCount() + " row(s) inserted.");
                            handleSQLError(conn.getLastSQL(), conn.getLastSQLError());

                            if (MSSQL4GTDS == false) {

                                mySQL = "COMMIT";
                                conn.executeSQL(mySQL);
                                myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() + ";");
                                myOntoExportForm.consoleWrite("  " + conn.getUpdateCount() + " row(s) inserted.");
                                handleSQLError(conn.getLastSQL(), conn.getLastSQLError());
                            }
                        }

                    } else {
                        myOntoExportForm.consoleWrite("No more intermediate nodes found!");
                        break;
                    }

                } while (true);
                // ------------- load data into i2b2 -------------

                myOntoExportForm.consoleWrite("Loading data into i2b2 ...");

                fstream = new FileWriter("mappingtable.sql");
                BufferedWriter mappingtable = new BufferedWriter(fstream);
                Hashtable existingColumns = new Hashtable();
                int idCntr = 0;


                mappingtable.write("DROP TABLE MAPPINGTABLE;\n");
                mappingtable.write("CREATE TABLE MAPPINGTABLE (ID NUMBER, CONCEPT_CODE VARCHAR2(200 BYTE), SRC_TABLE VARCHAR2(200 BYTE), ORIGINAL_WHERE VARCHAR2(2000 BYTE));\n");

                String i2b2sql = "";
                if (MSSQL4GTDS == false) {

                    i2b2sql = "INSERT INTO " + conn.getI2b2schema() + "OBSERVATION_FACT ("
                            + "ENCOUNTER_NUM, "
                            + "PATIENT_NUM, "
                            + "CONCEPT_CD, "
                            + "PROVIDER_ID, "
                            + "START_DATE, "
                            + "MODIFIER_CD, "
                            + "VALTYPE_CD, "
                            + "TVAL_CHAR, "
                            + "NVAL_NUM, "
                            + "VALUEFLAG_CD, "
                            + "QUANTITY_NUM, "
                            + "UNITS_CD, "
                            + "END_DATE, "
                            + "LOCATION_CD, "
                            + "CONFIDENCE_NUM, "
                            + "OBSERVATION_BLOB, "
                            + "UPDATE_DATE, "
                            + "DOWNLOAD_DATE, "
                            + "IMPORT_DATE, "
                            + "SOURCESYSTEM_CD, "
                            + "UPLOAD_ID,"
                            + "INSTANCE_NUM"
                            + ") SELECT DISTINCT "
                            + "$ENCOUNTER_NUM$, "
                            + "$PATIENT_NUM$, "
                            + "$CONCEPT_CD$, "
                            + "$PROVIDER_ID$, "
                            + "$START_DATE$, "
                            + "$MODIFIER_CD$, "
                            + "$VALTYPE_CD$, "
                            + "$TVAL_CHAR$, "
                            + "$NVAL_NUM$, "
                            + "$VALUEFLAG_CD$, "
                            + "$QUANTITY_NUM$, "
                            + "$UNITS_CD$, "
                            + "$END_DATE$, "
                            + "$LOCATION_CD$, "
                            + "$CONFIDENCE_NUM$, "
                            + "$OBSERVATION_BLOB$, "
                            + "$UPDATE_DATE$, "
                            + "$DOWNLOAD_DATE$, "
                            + "$IMPORT_DATE$, "
                            + "$SOURCESYSTEM_CD$, "
                            + "$UPLOAD_ID$, 1 "
                            + " FROM ( $SUBSELECT$ ) LOG ERRORS INTO ONTOEXPORT_ERROR_LOG REJECT LIMIT UNLIMITED";

                } else {

                    i2b2sql = "INSERT INTO " + conn.getI2b2schema() + "OBSERVATION_FACT ("
                            + "DocumentID, "
                            + "ENCOUNTER_NUM, "
                            + "PATIENT_NUM, "
                            + "CONCEPT_CD, "
                            + "PROVIDER_ID, "
                            + "START_DATE, "
                            + "MODIFIER_CD, "
                            + "VALTYPE_CD, "
                            + "TVAL_CHAR, "
                            + "NVAL_NUM, "
                            + "VALUEFLAG_CD, "
                            + "QUANTITY_NUM, "
                            + "UNITS_CD, "
                            + "END_DATE, "
                            + "LOCATION_CD, "
                            + "CONFIDENCE_NUM, "
                            + "OBSERVATION_BLOB, "
                            + "UPDATE_DATE, "
                            + "DOWNLOAD_DATE, "
                            + "IMPORT_DATE, "
                            + "SOURCESYSTEM_CD, "
                            + "UPLOAD_ID, INSTANCE_NUM"
                            + ") SELECT DISTINCT "
                            + "$ENCOUNTER_NUM$, "
                            + "$PATIENT_NUM$, "
                            + "$CONCEPT_CD$, "
                            + "$PROVIDER_ID$, "
                            + "$START_DATE$, "
                            + "$MODIFIER_CD$, "
                            + "$VALTYPE_CD$, "
                            + "$TVAL_CHAR$, "
                            + "$NVAL_NUM$, "
                            + "$VALUEFLAG_CD$, "
                            + "$QUANTITY_NUM$, "
                            + "$UNITS_CD$, "
                            + "$END_DATE$, "
                            + "$LOCATION_CD$, "
                            + "$CONFIDENCE_NUM$, "
                            + "$OBSERVATION_BLOB$, "
                            + "$UPDATE_DATE$, "
                            + "$DOWNLOAD_DATE$, "
                            + "$IMPORT_DATE$, "
                            + "$SOURCESYSTEM_CD$, "
                            + "$UPLOAD_ID$, 1 "
                            + " FROM ( $SUBSELECT$ ) AS DATA ";
                }
                if (MSSQL4GTDS == false) {

                    conn.executeSQL("TRUNCATE TABLE " + conn.getI2b2schema() + "OBSERVATION_FACT");
                    myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() + ";");
                    myOntoExportForm.consoleWrite("  " + conn.getUpdateCount() + " row(s) inserted.");
                    handleSQLError(conn.getLastSQL(), conn.getLastSQLError());

                    conn.executeSQL("DELETE FROM " + conn.getI2b2schema() + "OBSERVATION_FACT");
                    myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() + ";");
                    myOntoExportForm.consoleWrite("  " + conn.getUpdateCount() + " row(s) inserted.");
                    handleSQLError(conn.getLastSQL(), conn.getLastSQLError());

                    conn.executeSQL("COMMIT");
                    myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() + ";");
                    myOntoExportForm.consoleWrite("  " + conn.getUpdateCount() + " row(s) inserted.");
                    handleSQLError(conn.getLastSQL(), conn.getLastSQLError());

                }
                ResultSet response2;
                response2 = runQuery(" select ?destclass ?importnode ?ccprefix ?ccsuffix ?datatype"
                        + " ?dateItem ?sourceTable"
                        + " WHERE "
                        + "{ "
                        + "?destclass omsys:hasImport ?importnode . "
                        + "?destclass rdfs:subClassOf mdrsys:Exported . "
                        + "?destclass omsys:hasConceptCodePrefix ?ccprefix . "
                        + "?destclass omsys:hasConceptCodeSuffix ?ccsuffix . "
                        + "OPTIONAL { ?destclass mdrsys:hasDataType ?datatype . }"
                        + "OPTIONAL { ?importnode omsys:hasDate ?dateItem ."
                        + "           ?dateItem omsys:hasSourceTable ?sourceTable  } "
                        + "}", model); // add the query string
                if (!response2.hasNext()) {
                    myOntoExportForm.consoleWrite("-- ERROR -- No data to load found! ...");
                }
                int ModifierCnt = 0;
                while (response2.hasNext()) {

                    QuerySolution soln = response2.nextSolution();

                    RDFNode importnode = soln.get("?importnode");
                    RDFNode destclass = soln.get("?destclass");

                    myOntoExportForm.consoleWrite("Importing Data: " + simplifyURI(destclass.toString()) + " : " + simplifyURI(importnode.toString()));

                    myOntoExportForm.sqlConsoleWrite("/* Importing Data: " + simplifyURI(destclass.toString()) + " : " + simplifyURI(importnode.toString()) + " */");

                    SQLQueryFieldLoader fields4 = new SQLQueryFieldLoader(model);
                    fields4.getOperandFields(importnode);
                    String subselectSQL = fields4.getFetchSQL();

                    if (MSSQL4GTDS == true) {
                        subselectSQL = subselectSQL.replace("$DOCMENTID$", fields4.getDocumentIDColumn());
                    }

                    subselectSQL = subselectSQL.replace("$PATIENTID$", fields4.getPatientIDColumn());
                    subselectSQL = subselectSQL.replace("$DOCUMENTID$", fields4.getDocumentIDColumn());
                    subselectSQL = subselectSQL.replace("$DATESTARTVALUE$", fields4.getDateStartValueColumn());
                    subselectSQL = subselectSQL.replace("$DATEENDVALUE$", fields4.getDateEndValueColumn());
                    subselectSQL = subselectSQL.replace("$VALUE$", fields4.getValueColumn());
                    subselectSQL = subselectSQL.replace("$SOURCETABLE$", fields4.getSourceTableName());
                    subselectSQL = subselectSQL.replace("$SOURCEFILTER$", fields4.getSourceFilter());

                    System.out.println(fields4.getSourceFilter());

                    String[] whereFields = fields4.getSourceFilter().split("AND");

                    idCntr++;
                    mappingtable.write("INSERT INTO MAPPINGTABLE (ID, CONCEPT_CODE, SRC_TABLE, ORIGINAL_WHERE) VALUES (" + idCntr + ", '" + soln.get("?ccprefix") + ":" + soln.get("?ccsuffix") + "', '" + fields4.getSourceTableName() + "', '" + fields4.getSourceFilter().replaceAll("'", "#") + "'  );\n");

                    for (int aa = 0; aa < whereFields.length; aa++) {
                        System.out.println(whereFields[aa].trim());
                        if (whereFields[aa].trim().indexOf("!=") < 0 && whereFields[aa].trim().indexOf("=") >= 0 && whereFields[aa].trim().indexOf(" OR ") < 0) {

                            String[] entry = whereFields[aa].trim().split("=");
                            if (existingColumns.get(entry[0]) == null) {
                                mappingtable.write("ALTER TABLE MAPPINGTABLE ADD (" + entry[0].trim() + " VARCHAR2(200 BYTE));\n");
                                //mappingtable.write("ALTER TABLE MAPPINGTABLE ADD (" + entry[0].trim() + "_CMP VARCHAR2(200 BYTE));\n");
                                existingColumns.put(entry[0], true);
                            }
                            mappingtable.write("UPDATE MAPPINGTABLE SET " + entry[0].trim() + " = '" + entry[1].trim().replaceAll("'", "") + "' WHERE CONCEPT_CODE = '" + soln.get("?ccprefix") + ":" + soln.get("?ccsuffix") + "' AND SRC_TABLE =  '" + fields4.getSourceTableName() + "' AND ID = '" + idCntr + "';");
                        } else {
                            mappingtable.write("// Did not include contraint: " + whereFields[aa].trim() + "\n");
                        }
                    }


                    String mySQL = "";

                    if (MSSQL4GTDS == true) {
                        mySQL = i2b2sql.replace("$ENCOUNTER_NUM$", "DocumentID, PatientID");
                    } else {
                        mySQL = i2b2sql.replace("$ENCOUNTER_NUM$", "PatientID");
                    }

                    mySQL = mySQL.replace("$PATIENT_NUM$", "PatientID");
                    mySQL = mySQL.replace("$CONCEPT_CD$", "'" + soln.get("?ccprefix") + ":" + soln.get("?ccsuffix") + "'");
                    mySQL = mySQL.replace("$PROVIDER_ID$", "'@'");
                    mySQL = mySQL.replace("$SUBSELECT$", subselectSQL);

                    if (MSSQL4GTDS == false) {
                        mySQL = mySQL.replace("$START_DATE$", "CASE WHEN DateStartValue IS NOT NULL THEN DateStartValue ELSE TO_DATE('19000101', 'YYYYMMDD') END");
                    } else {
                        mySQL = mySQL.replace("$START_DATE$", "CASE WHEN DateStartValue IS NOT NULL THEN DateStartValue ELSE '19000101' END");
                    }

                    mySQL = mySQL.replace("$MODIFIER_CD$", ModifierCnt + "");
                    ModifierCnt++; // we do not want to violate the OBSERVATION_FACT_PK

                    String valtype_cd = "null";

                    if (soln.get("?datatype") != null) {
                        String datatype = simplifyURI(soln.get("?datatype").toString());
                        if (datatype.equals("Float") || datatype.equals("PosFloat") || datatype.equals("Integer") || datatype.equals("PosInteger")) {
                            valtype_cd = "'N'";
                            mySQL = mySQL.replace("$TVAL_CHAR$", "'E'"); // we assume "equal"
                        }
                        if (datatype.equals("String")) {
                            valtype_cd = "'T'";
                        }
                    }

                    mySQL = mySQL.replace("$VALTYPE_CD$", valtype_cd);

                    if (valtype_cd.equals("'T'")) {
                        mySQL = mySQL.replace("$TVAL_CHAR$", fields4.getValueColumn().toString());
                    } else {
                        mySQL = mySQL.replace("$TVAL_CHAR$", "null");
                    }

                    if (valtype_cd.equals("'N'")) {
                        //mySQL = mySQL.replace("$NVAL_NUM$", "SAFE_TO_NUMBER(TRIM(REPLACE(" + fields4.getValueColumn().toString() + ", '.', ',')))");
                        mySQL = mySQL.replace("$NVAL_NUM$", "SAFE_TO_NUMBER(TRIM(REPLACE(Value, '.', ',')))");

                    } else {
                        mySQL = mySQL.replace("$NVAL_NUM$", "null");
                    }

                    mySQL = mySQL.replace("$VALUEFLAG_CD$", "null");
                    mySQL = mySQL.replace("$QUANTITY_NUM$", "null");
                    mySQL = mySQL.replace("$UNITS_CD$", "null");
                    //mySQL = mySQL.replace("$END_DATE$", fields4.getDateEndValueColumn());
                    mySQL = mySQL.replace("$END_DATE$", "DateENDValue");
                    mySQL = mySQL.replace("$LOCATION_CD$", "null");
                    mySQL = mySQL.replace("$CONFIDENCE_NUM$", "null");
                    mySQL = mySQL.replace("$OBSERVATION_BLOB$", "null");
                    mySQL = mySQL.replace("$UPDATE_DATE$", "null");
                    mySQL = mySQL.replace("$DOWNLOAD_DATE$", "null");
                    mySQL = mySQL.replace("$IMPORT_DATE$", "null");
                    mySQL = mySQL.replace("$SOURCESYSTEM_CD$", "null");
                    mySQL = mySQL.replace("$UPLOAD_ID$", "null");


                    conn.executeSQL(mySQL);
                    myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() + ";");
                    myOntoExportForm.consoleWrite("  " + conn.getUpdateCount() + " row(s) inserted.");
                    handleSQLError(conn.getLastSQL(), conn.getLastSQLError());

                    if (MSSQL4GTDS == false) {

                        mySQL = "COMMIT";
                        conn.executeSQL(mySQL);
                        myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() + ";");
                        myOntoExportForm.consoleWrite("  " + conn.getUpdateCount() + " row(s) inserted.");
                        handleSQLError(conn.getLastSQL(), conn.getLastSQLError());
                    }

                    //System.out.println(mySQL);
                }
                if (MSSQL4GTDS == false) {
                    myOntoExportForm.consoleWrite("Commiting i2b2 observation fact database table changes ...");
                    conn.executeSQL("COMMIT");
                    myOntoExportForm.consoleWrite("  " + conn.getUpdateCount() + " row(s) inserted.");
                    handleSQLError(conn.getLastSQL(), conn.getLastSQLError());
                }
                myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() + ";");
                if (MSSQL4GTDS == false) {
                    myOntoExportForm.consoleWrite("Building PATIENT_DIMENSION ...");


                    //conn.executeSQL("TRUNCATE TABLE " + conn.getI2b2schema() + "PATIENT_DIMENSION");
                    conn.executeSQL("DELETE FROM " + conn.getI2b2schema() + "PATIENT_DIMENSION");
                    myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() + ";");
                    handleSQLError(conn.getLastSQL(), conn.getLastSQLError());

                    if (MSSQL4GTDS == false) {

                        conn.executeSQL("COMMIT");
                        myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() + ";");
                    }

                    String genPatientDimension = "INSERT INTO " + conn.getI2b2schema() + "PATIENT_DIMENSION (PATIENT_NUM, VITAL_STATUS_CD, BIRTH_DATE,"
                            + "DEATH_DATE, SEX_CD, AGE_IN_YEARS_NUM, LANGUAGE_CD, RACE_CD,"
                            + "MARITAL_STATUS_CD, RELIGION_CD, ZIP_CD, STATECITYZIP_PATH,"
                            + "PATIENT_BLOB, UPDATE_DATE, DOWNLOAD_DATE, IMPORT_DATE,"
                            + "SOURCESYSTEM_CD, UPLOAD_ID) "
                            + "SELECT DISTINCT PATIENT_NUM, "
                            + "'N',"
                            + "null,"
                            + "null,"
                            + "null," // Geschlecht
                            + "null," // Alter
                            + "null, null, null, null,"
                            + "null,"
                            + "null, null, null, null, null, null, null "
                            + "FROM " + conn.getI2b2schema() + "OBSERVATION_FACT";

                    conn.executeSQL(genPatientDimension);
                    myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() + ";");
                    myOntoExportForm.consoleWrite("  " + conn.getUpdateCount() + " row(s) inserted.");
                    handleSQLError(conn.getLastSQL(), conn.getLastSQLError());



                    /*
                     * myOntoExportForm.consoleWrite("Calculating TOTAL_NUM
                     * column ...");
                     *
                     * conn.executeSQL("CREATE TABLE " + conn.getI2b2schema() +
                     * "I2B2_2 AS (SELECT * FROM " + conn.getI2b2schema() +
                     * "I2B2)");
                     * myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() +
                     * ";"); myOntoExportForm.consoleWrite(" " +
                     * conn.getUpdateCount() + " row(s) inserted.");
                     * handleSQLError(conn.getLastSQL(),
                     * conn.getLastSQLError()); conn.executeSQL("UPDATE " +
                     * conn.getI2b2schema() + "I2B2_2 SET C_TOTALNUM = 0");
                     * myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() +
                     * ";"); myOntoExportForm.consoleWrite(" " +
                     * conn.getUpdateCount() + " row(s) inserted.");
                     * handleSQLError(conn.getLastSQL(),
                     * conn.getLastSQLError()); conn.executeSQL("DROP TABLE " +
                     * conn.getI2b2schema() + "I2B2");
                     * myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() +
                     * ";"); myOntoExportForm.consoleWrite(" " +
                     * conn.getUpdateCount() + " row(s) inserted.");
                     * handleSQLError(conn.getLastSQL(),
                     * conn.getLastSQLError()); conn.executeSQL("CREATE TABLE "
                     * + conn.getI2b2schema() + "LEAFTABLE AS SELECT DISTINCT "
                     * + conn.getI2b2schema() + "I2B2_2.C_FULLNAME FULLNAME,
                     * COUNT(OBSERVATION_FACT.PATIENT_NUM) TOTALNUM FROM " +
                     * conn.getI2b2schema() + "I2B2_2, " + conn.getI2b2schema()
                     * + "OBSERVATION_FACT, " + conn.getI2b2schema() +
                     * "CONCEPT_DIMENSION WHERE C_VISUALATTRIBUTES LIKE 'LA%'
                     * AND " + conn.getI2b2schema() +
                     * "OBSERVATION_FACT.CONCEPT_CD = " + conn.getI2b2schema() +
                     * "CONCEPT_DIMENSION.CONCEPT_CD AND " +
                     * conn.getI2b2schema() + "CONCEPT_DIMENSION.CONCEPT_PATH =
                     * " + conn.getI2b2schema() + "I2B2_2.C_FULLNAME GROUP BY "
                     * + conn.getI2b2schema() + "I2B2_2.C_FULLNAME ORDER BY
                     * TOTALNUM");
                     * myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() +
                     * ";"); myOntoExportForm.consoleWrite(" " +
                     * conn.getUpdateCount() + " row(s) inserted.");
                     * handleSQLError(conn.getLastSQL(),
                     * conn.getLastSQLError()); conn.executeSQL("CREATE TABLE "
                     * + conn.getI2b2schema() + "I2B2 AS SELECT " +
                     * conn.getI2b2schema() + "I2B2_2.C_HLEVEL, " +
                     * conn.getI2b2schema() + "I2B2_2.C_FULLNAME, " +
                     * conn.getI2b2schema() + "I2B2_2.C_NAME, " +
                     * conn.getI2b2schema() + "I2B2_2.C_SYNONYM_CD, " +
                     * conn.getI2b2schema() + "I2B2_2.C_VISUALATTRIBUTES, " +
                     * conn.getI2b2schema() + "LEAFTABLE.TOTALNUM C_TOTALNUM, "
                     * + conn.getI2b2schema() + "I2B2_2.C_BASECODE, " +
                     * conn.getI2b2schema() + "I2B2_2.C_METADATAXML, " +
                     * conn.getI2b2schema() + "I2B2_2.C_FACTTABLECOLUMN, " +
                     * conn.getI2b2schema() + "I2B2_2.C_TABLENAME, " +
                     * conn.getI2b2schema() + "I2B2_2.C_COLUMNNAME, " +
                     * conn.getI2b2schema() + "I2B2_2.C_COLUMNDATATYPE, " +
                     * conn.getI2b2schema() + "I2B2_2.C_OPERATOR, " +
                     * conn.getI2b2schema() + "I2B2_2.C_DIMCODE, " +
                     * conn.getI2b2schema() + "I2B2_2.C_COMMENT, " +
                     * conn.getI2b2schema() + "I2B2_2.C_TOOLTIP, " +
                     * conn.getI2b2schema() + "I2B2_2.UPDATE_DATE, " +
                     * conn.getI2b2schema() + "I2B2_2.DOWNLOAD_DATE, " +
                     * conn.getI2b2schema() + "I2B2_2.IMPORT_DATE, " +
                     * conn.getI2b2schema() + "I2B2_2.SOURCESYSTEM_CD, " +
                     * conn.getI2b2schema() + "I2B2_2.VALUETYPE_CD , " +
                     * conn.getI2b2schema() + "I2B2_2.M_APPLIED_PATH, " +
                     * conn.getI2b2schema() + "I2B2_2.M_EXCLUSION_CD, " +
                     * conn.getI2b2schema() + "I2B2_2.C_PATH, " +
                     * conn.getI2b2schema() + "I2B2_2.C_SYMBOL FROM " +
                     * conn.getI2b2schema() + "I2B2_2 LEFT OUTER JOIN " +
                     * conn.getI2b2schema() + "LEAFTABLE ON " +
                     * conn.getI2b2schema() + "LEAFTABLE.FULLNAME = " +
                     * conn.getI2b2schema() + "I2B2_2.C_FULLNAME");
                     * myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() +
                     * ";"); myOntoExportForm.consoleWrite(" " +
                     * conn.getUpdateCount() + " row(s) inserted.");
                     * handleSQLError(conn.getLastSQL(),
                     * conn.getLastSQLError()); conn.executeSQL("DROP TABLE " +
                     * conn.getI2b2schema() + "I2B2_2");
                     * myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() +
                     * ";"); myOntoExportForm.consoleWrite(" " +
                     * conn.getUpdateCount() + " row(s) inserted.");
                     * handleSQLError(conn.getLastSQL(),
                     * conn.getLastSQLError()); conn.executeSQL("DROP TABLE " +
                     * conn.getI2b2schema() + "LEAFTABLE");
                     * myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() +
                     * ";"); myOntoExportForm.consoleWrite(" " +
                     * conn.getUpdateCount() + " row(s) inserted.");
                     * handleSQLError(conn.getLastSQL(),
                     * conn.getLastSQLError());
                     */

                    mappingtable.close();

                }
            } catch (IOException ex) {
                System.out.println(ex.getStackTrace());
                Logger.getLogger(Exporter.class.getName()).log(Level.SEVERE, null, ex);
                
            } finally {
               
            }
        }

        if (MSSQL4GTDS == false) {
            conn.executeSQL("COMMIT");
            myOntoExportForm.sqlConsoleWrite(rootEntry.getLastSQL() + ";");
            handleSQLError(conn.getLastSQL(), conn.getLastSQLError());

        } else {

            myOntoExportForm.sqlConsoleFixToMSSQL();
        }


        myOntoExportForm.consoleWrite("Done!");

        conn.closeConnection();


    }

    ResultSet runQuery(String queryRequest, Model model) {

        StringBuilder queryStr = new StringBuilder();
        queryStr.append("PREFIX mdrsys" + ": <" + "http://www.uk-erlangen.de/MDR-System#" + "> ");
        queryStr.append("PREFIX omsys" + ": <" + "http://www.uk-erlangen.de/OntoMappingSystem#" + "> ");
        //queryStr.append("PREFIX mapp" + ": <" + "http://www.uk-erlangen.de/UKER-DPKK-Mapping#" + "> ");
        queryStr.append("PREFIX rdfs" + ": <" + "http://www.w3.org/2000/01/rdf-schema#" + "> ");
        queryStr.append("PREFIX rdf" + ": <" + "http://www.w3.org/1999/02/22-rdf-syntax-ns#" + "> ");
        //queryStr.append("PREFIX soa" + ": <" + "http://www.uk-erlangen.de/Soarian#" + "> ");
        //queryStr.append("PREFIX ont" + ": <" + "http://www.uk-erlangen.de/DPKK-Datensatz#" + "> ");

        queryStr.append(queryRequest);

        //System.out.println("SPARQL query is " + queryStr);

        Query query = QueryFactory.create(queryStr.toString());
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        ResultSet response = qexec.execSelect();
        return response;
    }

    private void getHierarchicalClass(OntClass artefact, int level, String ClassesTree) {

        String newClassesTree = null;
        String predicateName = "";
        String objectName = "";
        String niceName = "";
        String description = "";

        if (artefact != null) {

            for (Iterator i = artefact.listSubClasses(); i.hasNext();) {

                // Get the class:
                OntClass c = (OntClass) i.next();
                Boolean doExport = true;

                for (Iterator i2 = c.listSuperClasses(); i2.hasNext();) {
                    OntClass c2 = (OntClass) i2.next();
                    System.out.println("Super class: " + c2);
                    if (simplifyURI(c2.toString()).equals("Unexported")) {
                        doExport = false;
                    }
                }

                System.out.println(c);

                if (doExport == true) {
                    // build i2b2 ontology entry for this class:
                    newClassesTree = createI2B2OntologyEntry((OntResource) c, ClassesTree, level);
                    // build i2b2 ontology entry for all of its intances:
                    ExtendedIterator iIndividuals = c.listInstances();
                    while (iIndividuals.hasNext()) {
                        Individual indiv = (Individual) iIndividuals.next();
                        System.out.println(indiv);
                        createI2B2OntologyEntry((OntResource) indiv, newClassesTree, level + 1);

                    }
                    // Process all subclasses:
                    getHierarchicalClass(c, level + 1, newClassesTree);
                }
            }

        } else {
            System.out.println("Artefact is NULL!");
        }
    }

    private String simplifyURI(String string) {
        if (string.indexOf('^') != -1) {
            string = string.substring(0, string.indexOf('^'));
        }
        if (string.indexOf('#') != -1) {
            string = string.substring(string.indexOf('#') + 1);
        }
        return string;
    }

    /**
     * Print information about the individual
     *
     * @param i The individual to output
     * @param writer The writer to which to output
     */
    public static void printIndividual(Individual i) {
        //print the local name of the individual (to keep it terse)

        System.out.println("Individual: " + i.getLocalName());

        //print the statements about this individual
        StmtIterator iProperties = i.listProperties();
        while (iProperties.hasNext()) {
            Statement s = (Statement) iProperties.next();
            System.out.println("  " + s.getPredicate().getLocalName()
                    + " : " + s.getObject().toString());
        }
        iProperties.close();
        System.out.println();
    }

    String createI2B2OntologyEntry(OntResource ontResource, String ClassesTree, int level) {

        String conceptCodeSuffix = "" + suffixCounter;
        suffixCounter++;
        String conceptCodePrefix = "" + randomPrefix;
        String niceName = simplifyURI(ontResource.toString());
        //niceName = niceName.replace("_", " ");

        // Upload the entry to the database:
        i2b2OntologyEntry entry = new i2b2OntologyEntry(conn);


        // entry.C_NAME = niceName.replace("_", " ");
        // entry.C_TOOLTIP = niceName.replace("_", " "); // must be filled out!
        entry.C_NAME = niceName;
        entry.C_TOOLTIP = niceName; // must be filled out!


        //print the statements about this individual
        StmtIterator iProperties = ontResource.listProperties();
        while (iProperties.hasNext()) {
            Statement s = (Statement) iProperties.next();

            String predicate = s.getPredicate().getLocalName();
            String subject = s.getObject().toString();
            String ssubject = simplifyURI(s.getObject().toString());

            if (predicate.equals("hasNiceName") && !ssubject.replace("\n", "").equals("")) {
                entry.C_NAME = ssubject.replace("\n", "");
                // niceName = ssubject.replace("\n", "");
            }

            if (predicate.equals("hasDataType") && !ssubject.replace("\n", "").equals("")) {

                //System.out.println("hasDataType " + ssubject);

                if (ssubject.equals("Float") || ssubject.equals("PosFloat")) {
                    entry.set_xml_DataType(ssubject.replace("\n", ""));
                    entry.set_xml_Oktousevalues("Y");
                    entry.C_COLUMNDATATYPE = "T";
                    entry.C_METADATAXML = entry.getXML();
                    System.out.println(entry.C_METADATAXML);

                }
                if (ssubject.equals("Integer") || ssubject.equals("PosInteger")) {
                    entry.set_xml_DataType(ssubject.replace("\n", ""));
                    entry.set_xml_Oktousevalues("Y");
                    entry.C_COLUMNDATATYPE = "T";
                    entry.C_METADATAXML = entry.getXML();
                    System.out.println(entry.C_METADATAXML);
                }
                if (ssubject.equals("String")) {
                    entry.C_COLUMNDATATYPE = "T";
                    entry.C_METADATAXML = entry.getXML();
                    System.out.println(entry.C_METADATAXML);
                }
            }

            if (predicate.equals("hasDescription") && !ssubject.replace("\n", "").equals("")) {
                entry.C_TOOLTIP = ssubject.replace("\n", "");
            }

            if (predicate.equals("hasConceptCodeSuffix") && !ssubject.replace("\n", "").equals("")) {
                conceptCodeSuffix = ssubject.replace("\n", "");
            }

            if (predicate.equals("hasConceptCodePrefix") && !ssubject.replace("\n", "").equals("")) {
                conceptCodePrefix = ssubject.replace("\n", "");
            }
        }

        String newClassesTree = ClassesTree + niceName + "\\";

        //System.out.println(entry.C_BASECODE);

        iProperties.close();
        //System.out.println();

        entry.C_FULLNAME = newClassesTree;
        entry.C_DIMCODE = newClassesTree;
        entry.C_HLEVEL = level;

        if (MSSQL4GTDS == false) {
            entry.UPDATE_DATE = "sysdate";
        } else {
            entry.UPDATE_DATE = "SYSDATETIME()";
        }

        OntClass rsrc = (OntClass) ontResource;

        // if (ontResource.isIndividual()) {
        if (!rsrc.hasSubClass()) {

            entry.C_VISUALATTRIBUTES = "LAE";
            entry.C_BASECODE = conceptCodePrefix + ":" + conceptCodeSuffix;

            System.out.println(entry.C_BASECODE);

            model.add(model.createStatement(ontResource, model.getProperty(mappingSystemNameSpace + "hasConceptCodeSuffix"),
                    model.createLiteral(conceptCodeSuffix)));
            model.add(model.createStatement(ontResource, model.getProperty(mappingSystemNameSpace + "hasConceptCodePrefix"),
                    model.createLiteral(conceptCodePrefix)));



        } else {
            entry.C_VISUALATTRIBUTES = "FAE";

        }


        //System.out.println("Creating Entry for " + ontResource.getLocalName() + "   " + entry.C_VISUALATTRIBUTES);
        myOntoExportForm.consoleWrite(newClassesTree);
        entry.insertInto();

        myOntoExportForm.sqlConsoleWrite(entry.getLastSQL() + ";");
        myOntoExportForm.consoleWrite("  " + conn.getUpdateCount() + " row(s) inserted.");
        handleSQLError(conn.getLastSQL(), conn.getLastSQLError());

        return newClassesTree;

    }

    private void handleSQLError(String lastSQL, String lastSQLError) {

        if (!lastSQLError.equals("")) {
            if (exportConfig.stopOnError) {
                JOptionPane.showMessageDialog(null, "The last SQL statement (see lower console) has caused an SQL error:\n\n" + lastSQLError, "SQL database error", JOptionPane.ERROR_MESSAGE);
            }


            File file = new File("SQL-Errors.txt");
            FileWriter writer = null;
            try {
                writer = new FileWriter(file, true);
                writer.write(lastSQLError + "\n\n");
                writer.write(lastSQL + "\n\n");
                writer.write("--------------------------------------------------------------\n\n");

                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Exporter.class.getName()).log(Level.SEVERE, null, ex);
            }


            sqlErrors++;
        } else {
            sqlOKs++;
        }

        myOntoExportForm.setDatabaseStatus("Database status: " + sqlOKs + " commands OK, " + sqlErrors + " error(s)");
    }
}
