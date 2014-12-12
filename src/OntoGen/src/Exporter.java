/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.RDFS;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author matesn
 */
public class Exporter implements Runnable {

    ExporterConfig myConfig;
    OntoGenForm myOntoGenForm;
    private int rowCnt = 0;

    public void setConfig(ExporterConfig myConfig) {
        this.myConfig = myConfig;
    }

    Exporter(OntoGenForm aThis) {
        this.myOntoGenForm = aThis;
    }

    private String getOWLClassName(String string) {
        String className = string.replaceAll("[^\\p{L}\\p{N}-]", "_");
        return className;
    }

    @Override
    public void run() {

        File outSQLFile = new File("Output.sql");
        try {
            FileWriter outWriter = new FileWriter(outSQLFile, false);


            List fileContents = new ArrayList();

            myOntoGenForm.logWrite("Loading CSV file ...\n");

            try {
                File file = new File("Input.csv");
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                DataInputStream dis = null;
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                dis = new DataInputStream(bis);
                while (dis.available() != 0) {
                    fileContents.add(dis.readLine());
                }
                fis.close();
                bis.close();
                dis.close();
            } catch (Exception ex) {
                Logger.getLogger(OntoGenForm.class.getName()).log(Level.SEVERE, null, ex);
            }

            String headlines[] = fileContents.get(0).toString().split(";");
            String lineTypes[] = fileContents.get(1).toString().split(";");

            int DocumentIdColumn = -1;
            int GlobalDateColumn = -1;
            int PatientIDColumn = -1;

            myOntoGenForm.logWrite("Parsing the first two rows ...\n");

            for (int i = 0; i < lineTypes.length; i++) {
                if (lineTypes[i].equalsIgnoreCase("DocumentId")) {
                    DocumentIdColumn = i;
                    myOntoGenForm.logWrite("   DocumentID is in column " + DocumentIdColumn + "\n");
                }
                if (lineTypes[i].equalsIgnoreCase("GlobalDate")) {
                    GlobalDateColumn = i;
                    myOntoGenForm.logWrite("   GlobalDate is in column " + GlobalDateColumn + "\n");
                }
                if (lineTypes[i].equalsIgnoreCase("PatientID")) {
                    PatientIDColumn = i;
                    myOntoGenForm.logWrite("   PatientID is in column " + PatientIDColumn + "\n");
                }
            }

            if (DocumentIdColumn == -1) {
                myOntoGenForm.logWrite("ERROR: Missing DocumentId entry, aborting!" + "\n");
                return;
            }
            if (GlobalDateColumn == -1) {
                myOntoGenForm.logWrite("ERROR: Missing GlobalDate entry, aborting!" + "\n");
                return;
            }
            if (PatientIDColumn == -1) {
                myOntoGenForm.logWrite("ERROR: Missing PatientID entry, aborting!" + "\n");
                return;
            }

            myOntoGenForm.logWrite("Setting up Jena model and JDBC database connection ..." + "\n");

            DatabaseConnection conn = new DatabaseConnection("ExportConnection.properties", myConfig.getJDBC());
            conn.initConnention();

            OntModel leftModel = null;
            OntModel rightModel = null;
            OntModel mappingModel = null;

            leftModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
            rightModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
            mappingModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

            OntModel mdrSystem;
            mdrSystem = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(System.getProperty("user.dir") + File.separatorChar + "MDR-System.owl");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(OntoGenForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            mdrSystem.read(inputStream, "");
            leftModel.addSubModel(mdrSystem);

            OntModel omsys;
            omsys = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
            FileInputStream inputStream2 = null;
            try {
                inputStream2 = new FileInputStream(System.getProperty("user.dir") + File.separatorChar + "OntoMappingSystem.owl");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(OntoGenForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            omsys.read(inputStream2, "");
            rightModel.addSubModel(mdrSystem);
            rightModel.addSubModel(omsys);


            mappingModel.addSubModel(leftModel);
            mappingModel.addSubModel(rightModel);
            mappingModel.addSubModel(mdrSystem);
            mappingModel.addSubModel(omsys);

            mappingModel.setNsPrefix("", myConfig.getMappingNamespace());

            leftModel.setNsPrefix("", myConfig.getLeftNamespace());
            rightModel.setNsPrefix("", myConfig.getRightNamespace());

            Ontology mappingOnt = mappingModel.createOntology(myConfig.getMappingNamespace());
            Ontology leftOnt = leftModel.createOntology(myConfig.getLeftNamespace());
            Ontology rightOnt = rightModel.createOntology(myConfig.getRightNamespace());

            mappingOnt.addImport(mappingModel.createResource("file:" + myConfig.getProjectName() + "-Target.owl"));
            mappingOnt.addImport(mappingModel.createResource("file:" + myConfig.getProjectName() + "-Source.owl"));
            mappingOnt.addImport(mappingModel.createResource("file:OntoMappingSystem.owl"));
            mappingOnt.addImport(mappingModel.createResource("file:MDR-System.owl"));

            leftOnt.addImport(leftModel.createResource("file:OntoMappingSystem.owl"));
            leftOnt.addImport(leftModel.createResource("file:MDR-System.owl"));

            rightOnt.addImport(rightModel.createResource("file:OntoMappingSystem.owl"));
            rightOnt.addImport(rightModel.createResource("file:MDR-System.owl"));

            myOntoGenForm.logWrite("Preparing mapping ontology and creating individuals for SourceTable and DatabaseConnection ..." + "\n");

            OntClass leftRoot = leftModel.createClass(myConfig.getLeftNamespace() + getOWLClassName(myConfig.getProjectName()) + "-Root");
            leftModel.add(leftModel.createStatement(leftRoot, RDFS.subClassOf,
                    leftModel.getOntClass("http://www.uk-erlangen.de/MDR-System#MDR-Dataelement")));

            OntClass rightRoot = rightModel.createClass(myConfig.getRightNamespace() + getOWLClassName(myConfig.getProjectName()) + "-Table");



            OntClass tableClass = rightModel.getOntClass("http://www.uk-erlangen.de/OntoMappingSystem#SourceTable");

            tableClass.createIndividual(myConfig.getRightNamespace() + myConfig.getProjectName() + "-Table");
            Individual tableInd = rightModel.getIndividual(myConfig.getRightNamespace() + myConfig.getProjectName() + "-Table");

            OntClass dbConn = rightModel.getOntClass("http://www.uk-erlangen.de/OntoMappingSystem#DatabaseConnection");
            dbConn.createIndividual(myConfig.getRightNamespace() + myConfig.getProjectName() + "-Connection");
            Individual connInd = rightModel.getIndividual(myConfig.getRightNamespace() + myConfig.getProjectName() + "-Table");


            // Create all properties for the Table Individual

            rightModel.add(rightModel.createStatement(tableInd,
                    rightModel.getObjectProperty("http://www.uk-erlangen.de/OntoMappingSystem#hasDatabaseConnection"),
                    connInd));

            rightModel.add(rightModel.createStatement(tableInd,
                    rightModel.getDatatypeProperty("http://www.uk-erlangen.de/OntoMappingSystem#hasDateTransformation"),
                    rightModel.createLiteral("($VALUE$)")));

            rightModel.add(rightModel.createStatement(tableInd,
                    rightModel.getDatatypeProperty("http://www.uk-erlangen.de/OntoMappingSystem#hasValueColumn"),
                    rightModel.createLiteral("Value")));

            String AccessSQL = "SELECT DISTINCT $NODENAME$ NodeName, (CASE WHEN OP1.DocumentID IS NULL AND OP2.DocumentID IS NOT NULL THEN OP2.DocumentID ELSE OP1.DocumentID END) DocumentID, (CASE WHEN OP1.PatientID IS NULL AND OP2.PatientID IS NOT NULL THEN OP2.PatientID ELSE OP1.PatientID END) DocumentID, $DATESTARTVALUE$ DateStartValue, $DATEENDVALUE$ DateEndValue, $OUTPUT$ Value FROM ($OPERANDFETCHSQL1$) OP1 FULL OUTER JOIN ($OPERANDFETCHSQL2$) OP2 ON OP1.DocumentID = OP2.DocumentID $RESULTFILTER$";

            rightModel.add(mappingModel.createStatement(tableInd,
                    mappingModel.getDatatypeProperty("http://www.uk-erlangen.de/OntoMappingSystem#hasAccessSQL"),
                    mappingModel.createLiteral(AccessSQL)));

            rightModel.add(mappingModel.createStatement(tableInd,
                    mappingModel.getDatatypeProperty("http://www.uk-erlangen.de/OntoMappingSystem#hasDateEndValueColumn"),
                    mappingModel.createLiteral("DateStartValue")));

            rightModel.add(mappingModel.createStatement(tableInd,
                    mappingModel.getDatatypeProperty("http://www.uk-erlangen.de/OntoMappingSystem#hasDateStartValueColumn"),
                    mappingModel.createLiteral("DateEndValue")));

            rightModel.add(mappingModel.createStatement(tableInd,
                    mappingModel.getDatatypeProperty("http://www.uk-erlangen.de/OntoMappingSystem#hasDocumentIDColumn"),
                    mappingModel.createLiteral("DocumentID")));

            rightModel.add(mappingModel.createStatement(tableInd,
                    mappingModel.getDatatypeProperty("http://www.uk-erlangen.de/OntoMappingSystem#hasPatientIDColumn"),
                    mappingModel.createLiteral("PatientID")));

            rightModel.add(mappingModel.createStatement(tableInd,
                    mappingModel.getDatatypeProperty("http://www.uk-erlangen.de/OntoMappingSystem#hasValueColumn"),
                    mappingModel.createLiteral("Value")));

            rightModel.add(mappingModel.createStatement(tableInd,
                    mappingModel.getDatatypeProperty("http://www.uk-erlangen.de/OntoMappingSystem#hasSourceTableName"),
                    //mappingModel.createLiteral("OBSERVATION_FACT")));
                    mappingModel.createLiteral(myConfig.getProjectName())));



            String OperandFetchSQL = "SELECT DISTINCT $PATIENTID$ PatientID, $DOCUMENTID$ DocumentID, $DATESTARTVALUE$ DateStartValue, $DATEENDVALUE$ DateEndValue, $VALUE$ Value FROM $SOURCETABLE$ WHERE $SOURCEFILTER$";

            rightModel.add(mappingModel.createStatement(tableInd,
                    mappingModel.getDatatypeProperty("http://www.uk-erlangen.de/OntoMappingSystem#hasOperandFetchSQL"),
                    mappingModel.createLiteral(OperandFetchSQL)));

            // Create all properties for the Connection Individual

            rightModel.add(mappingModel.createStatement(connInd,
                    mappingModel.getDatatypeProperty("http://www.uk-erlangen.de/OntoMappingSystem#hasHostName"),
                    mappingModel.createLiteral(conn.server)));

            rightModel.add(mappingModel.createStatement(connInd,
                    mappingModel.getDatatypeProperty("http://www.uk-erlangen.de/OntoMappingSystem#hasPassword"),
                    mappingModel.createLiteral(conn.user)));

            rightModel.add(mappingModel.createStatement(connInd,
                    mappingModel.getDatatypeProperty("http://www.uk-erlangen.de/OntoMappingSystem#hasPassword"),
                    mappingModel.createLiteral(conn.password)));

            rightModel.add(mappingModel.createStatement(connInd,
                    mappingModel.getDatatypeProperty("http://www.uk-erlangen.de/OntoMappingSystem#hasPort"),
                    mappingModel.createLiteral(conn.port)));

            rightModel.add(mappingModel.createStatement(connInd,
                    mappingModel.getDatatypeProperty("http://www.uk-erlangen.de/OntoMappingSystem#hasSID"),
                    mappingModel.createLiteral(conn.SID)));


            myOntoGenForm.logWrite("Aggregating column values ..." + "\n");

            for (int i = 0; i < headlines.length; i++) {
                if (i != DocumentIdColumn && i != GlobalDateColumn && i != PatientIDColumn) {
                    String className = myConfig.getLeftNamespace() + getOWLClassName(headlines[i]);
                    OntClass c = leftModel.createClass(className);
                    leftModel.add(leftModel.createStatement(c, RDFS.subClassOf, leftRoot));
                    leftModel.add(leftModel.createStatement(c, RDFS.subClassOf, mdrSystem.getOntClass("http://www.uk-erlangen.de/MDR-System#Exported")));

                    //Individual colInd = rightRoot.createIndividual(myConfig.getRightNamespace() + getOWLClassName(headlines[i]));

                    OntClass attributeRight = rightModel.createClass(myConfig.getRightNamespace() + getOWLClassName(headlines[i]));
                    rightModel.add(leftModel.createStatement(attributeRight, RDFS.subClassOf, rightRoot));

                    if (i < lineTypes.length) {
                        if (!lineTypes[i].equals("")) {

                            if (lineTypes[i].equalsIgnoreCase("INTEGER")) {
                                ObjectProperty prop = leftModel.getObjectProperty("http://www.uk-erlangen.de/MDR-System#hasDataType");
                                Individual ind = leftModel.getIndividual("http://www.uk-erlangen.de/MDR-System#Integer");
                                leftModel.add(leftModel.createStatement(c, prop, ind));
                            }
                            if (lineTypes[i].equalsIgnoreCase("POSINTEGER")) {
                                ObjectProperty prop = leftModel.getObjectProperty("http://www.uk-erlangen.de/MDR-System#hasDataType");
                                Individual ind = leftModel.getIndividual("http://www.uk-erlangen.de/MDR-System#PosInteger");
                                leftModel.add(leftModel.createStatement(c, prop, ind));
                            }
                            if (lineTypes[i].equalsIgnoreCase("FLOAT")) {
                                ObjectProperty prop = leftModel.getObjectProperty("http://www.uk-erlangen.de/MDR-System#hasDataType");
                                Individual ind = leftModel.getIndividual("http://www.uk-erlangen.de/MDR-System#Float");
                                leftModel.add(leftModel.createStatement(c, prop, ind));
                            }
                            if (lineTypes[i].equalsIgnoreCase("POSFLOAT")) {
                                ObjectProperty prop = leftModel.getObjectProperty("http://www.uk-erlangen.de/MDR-System#hasDataType");
                                Individual ind = leftModel.getIndividual("http://www.uk-erlangen.de/MDR-System#PosFloat");
                                leftModel.add(leftModel.createStatement(c, prop, ind));
                            }
                            if (lineTypes[i].equalsIgnoreCase("STRING")) {
                                ObjectProperty prop = leftModel.getObjectProperty("http://www.uk-erlangen.de/MDR-System#hasDataType");
                                Individual ind = leftModel.getIndividual("http://www.uk-erlangen.de/MDR-System#String");
                                leftModel.add(leftModel.createStatement(c, prop, ind));
                            }

                            String valueName = myConfig.getRightNamespace() + getOWLClassName(headlines[i]) + "-Value";
                            attributeRight.createIndividual(valueName);
                            Individual ind = rightModel.getIndividual(valueName);
                            ObjectProperty hasImport = omsys.getObjectProperty("http://www.uk-erlangen.de/OntoMappingSystem#hasImport");
                            DatatypeProperty hasNiceName = mdrSystem.getDatatypeProperty("http://www.uk-erlangen.de/MDR-System#hasNiceName");
                            mappingModel.add(mappingModel.createStatement(c, hasImport, ind));

                            rightModel.add(rightModel.createStatement(ind,
                                    omsys.getDatatypeProperty("http://www.uk-erlangen.de/OntoMappingSystem#hasSelectFilter"),
                                    rightModel.createLiteral("Attribute = '" + headlines[i]) + "'"));

                            ind.addOntClass(omsys.getOntClass("http://www.uk-erlangen.de/OntoMappingSystem#ProcessedItem"));

                            rightModel.add(rightModel.createStatement(ind,
                                    omsys.getObjectProperty("http://www.uk-erlangen.de/OntoMappingSystem#hasSourceTable"),
                                    tableInd));
                        }
                    }

                    if (i >= lineTypes.length || lineTypes[i].equals("")) {

                        // aggregate all values:

                        for (int q = 2; q < fileContents.size(); q++) {
                            String curLine[] = fileContents.get(q).toString().split(";");
                            if (i < curLine.length) {
                                if (!curLine[i].equals("")) {

                                    String valueName = getOWLClassName(headlines[i]) + "-" + getOWLClassName(curLine[i]);

                                    String className2 = myConfig.getLeftNamespace() + valueName;
                                    OntClass c2 = leftModel.createClass(className2);
                                    leftModel.add(leftModel.createStatement(c2, RDFS.subClassOf, c));
                                    leftModel.add(leftModel.createStatement(c2, RDFS.subClassOf, mdrSystem.getOntClass("http://www.uk-erlangen.de/MDR-System#Exported")));


                                    attributeRight.createIndividual(myConfig.getRightNamespace() + valueName);

                                    //c2 = mappingModel.getOntClass(className2);
                                    ObjectProperty hasImport = omsys.getObjectProperty("http://www.uk-erlangen.de/OntoMappingSystem#hasImport");
                                    DatatypeProperty hasNiceName = mdrSystem.getDatatypeProperty("http://www.uk-erlangen.de/MDR-System#hasNiceName");

                                    Individual ind = rightModel.getIndividual(myConfig.getRightNamespace() + valueName);

                                    Literal niceName = leftModel.createLiteral(headlines[i] + " = " + curLine[i]);
                                    mappingModel.add(mappingModel.createStatement(c2, hasImport, ind));
                                    leftModel.add(leftModel.createStatement(c2, hasNiceName, niceName));


                                    rightModel.add(rightModel.createStatement(ind,
                                            omsys.getDatatypeProperty("http://www.uk-erlangen.de/OntoMappingSystem#hasSelectFilter"),
                                            rightModel.createLiteral("Value = '" + curLine[i] + "'")));

                                    rightModel.add(rightModel.createStatement(ind,
                                            omsys.getDatatypeProperty("http://www.uk-erlangen.de/OntoMappingSystem#hasSelectFilter"),
                                            rightModel.createLiteral("Attribute = '" + headlines[i] + "'")));

                                    ind.addOntClass(omsys.getOntClass("http://www.uk-erlangen.de/OntoMappingSystem#ProcessedItem"));
                                    ind.addOntClass(mdrSystem.getOntClass("http://www.uk-erlangen.de/MDR-System#Exported"));

                                    rightModel.add(rightModel.createStatement(ind,
                                            rightModel.getObjectProperty("http://www.uk-erlangen.de/OntoMappingSystem#hasSourceTable"),
                                            tableInd));

                                }
                            }
                        }
                    }

                    DatatypeProperty prop2 = leftModel.getDatatypeProperty("http://www.uk-erlangen.de/MDR-System#hasNiceName");
                    Literal lit = leftModel.createLiteral(headlines[i]);
                    leftModel.add(leftModel.createStatement(c, prop2, lit));
                }
            }

            if (myConfig.getNewOntologies()) {


                try {
                    FileOutputStream outFile = new FileOutputStream(myConfig.getProjectName() + "-Target.owl");
                    leftModel.write(outFile, "RDF/XML-ABBREV", null);
                    //leftModel.write(outFile, "TURTLE", myConfig.getLeftNamespace());
                    try {
                        outFile.close();
                    } catch (IOException ex) {
                        Logger.getLogger(OntoGenForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(OntoGenForm.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    FileOutputStream outFile = new FileOutputStream(myConfig.getProjectName() + "-Mapping.owl");
                    mappingModel.write(outFile, "RDF/XML-ABBREV", null);
                    //mappingModel.write(outFile, "TURTLE", myConfig.getMappingNamespace());
                    try {
                        outFile.close();
                    } catch (IOException ex) {
                        Logger.getLogger(OntoGenForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(OntoGenForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                myOntoGenForm.logWrite("Created ontologies: " + myConfig.getProjectName() + "-Target.owl, " + myConfig.getProjectName() + "-Mapping.owl" + "\n");
            }

            try {
                FileOutputStream outFile = new FileOutputStream(myConfig.getProjectName() + "-Source.owl");
                rightModel.write(outFile, "RDF/XML-ABBREV", null);
                //rightModel.write(outFile, "TURTLE", myConfig.getRightNamespace());
                try {
                    outFile.close();
                } catch (IOException ex) {
                    Logger.getLogger(OntoGenForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(OntoGenForm.class.getName()).log(Level.SEVERE, null, ex);
            }


            myOntoGenForm.logWrite("Created ontology: " + myConfig.getProjectName() + "-Source.owl " + "\n");


            myOntoGenForm.logWrite("\nConverting CSV file to EAV format: 0% ... ");

            List sqlStatements = new ArrayList();



            String tableName = myConfig.getProjectName();
            String insertHead = "INSERT INTO " + tableName + "(DocumentID, PatientID, DateStartValue, DateEndValue, Attribute, Value) VALUES (";

            conn.executeSQL("DROP TABLE " + tableName);
            myOntoGenForm.consoleWrite(conn.getLastSQL() + ";" + "\n");
            outWriter.write(conn.getLastSQL() + ";" + "\n");


            conn.executeSQL("CREATE TABLE " + tableName + " (DocumentID NUMBER, PatientID NUMBER,  DateStartValue DATE, DateEndValue DATE, Attribute VARCHAR2(1000 BYTE), Value VARCHAR2(1000 BYTE))");
            myOntoGenForm.consoleWrite(conn.getLastSQL() + ";" + "\n");
            outWriter.write(conn.getLastSQL() + ";" + "\n");

            for (int i = 2; i < fileContents.size(); i++) {


                rowCnt++;

                if ((rowCnt % 5) == 0) {
                    myOntoGenForm.logWrite((int) ((new Double(i) / fileContents.size()) * 100) + "% ... ");
                }

                String currentlLine = fileContents.get(i).toString();
                String lineAttributes[] = currentlLine.split(";");

                //System.out.println(currentlLine);

                for (int a = 0; a < lineAttributes.length; a++) {

                    if (!lineAttributes[a].equalsIgnoreCase("")) {
                        if (a != DocumentIdColumn && a != GlobalDateColumn && a != PatientIDColumn) {


                            if (lineAttributes[DocumentIdColumn] == null) {
                                System.out.println("lineAttributes[DocumentIdColumn] is null");
                                System.out.println("DocumentIdColumn = " + DocumentIdColumn);
                            }
                            if (lineAttributes[GlobalDateColumn] == null) {
                                System.out.println("lineAttributes[GlobalDateColumn] is null");
                                System.out.println("DocumentIdColumn = " + DocumentIdColumn);
                            }
                            if (myConfig.getDateFormat() == null) {
                                System.out.println("myConfig.getDateFormat() is null");
                            }
                            if (headlines[a] == null) {
                                System.out.println("headlines[a] is null");
                                System.out.println("a = " + a);
                            }
                            if (lineAttributes[a] == null) {
                                System.out.println("lineAttributes[a] is null");
                                System.out.println("a = " + a);
                            }


                            String insertTail = lineAttributes[DocumentIdColumn] + ", "
                                    + lineAttributes[PatientIDColumn] + ", "
                                    + "TO_DATE('" + lineAttributes[GlobalDateColumn] + "', '" + myConfig.getDateFormat() + "'), "
                                    + "TO_DATE('" + lineAttributes[GlobalDateColumn] + "', '" + myConfig.getDateFormat() + "'), "
                                    + "'" + headlines[a] + "', "
                                    + "'" + lineAttributes[a] + "')";

                            String insertSQL = insertHead + insertTail;
                            conn.executeSQL(insertSQL);
                            myOntoGenForm.consoleWrite(conn.getLastSQL() + ";" + "\n");
                            outWriter.write(conn.getLastSQL() + ";" + "\n");
                        }
                    }
                }
            }

            myOntoGenForm.logWrite("100%\n");
            myOntoGenForm.logWrite("\n");

            conn.executeSQL("COMMIT");
            myOntoGenForm.consoleWrite(conn.getLastSQL() + ";" + "\n");
            outWriter.write(conn.getLastSQL() + ";" + "\n");
            conn.closeConnection();

            if (myConfig.getCreateProperties()) {
                try {
                    Properties properties = new Properties();
                    properties.setProperty("inputFile", myConfig.getProjectName() + "-Target.owl");
                    File propertiesFileFile = new File("OntoEdit.properties");
                    properties.store(new FileOutputStream(propertiesFileFile), "OntoEdit Properties File");
                } catch (IOException ex) {
                    Logger.getLogger(OntoGenForm.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    Properties properties = new Properties();
                    properties.setProperty("inputFile", myConfig.getProjectName() + "-Mapping.owl");
                    properties.setProperty("leftNameSpace", myConfig.getLeftNamespace());
                    properties.setProperty("leftStartClass", getOWLClassName(myConfig.getProjectName()) + "-Root");
                    properties.setProperty("rightNameSpace", myConfig.getRightNamespace());
                    properties.setProperty("rightStartClass", getOWLClassName(myConfig.getProjectName()) + "-Table");
                    properties.setProperty("mappingNameSpace", myConfig.getMappingNamespace());
                    properties.setProperty("mappingSystemNameSpace", "http://www.uk-erlangen.de/OntoMappingSystem#");
                    File propertiesFileFile = new File("QuickMapp.properties");
                    properties.store(new FileOutputStream(propertiesFileFile), "QuickMapp Properties File");
                } catch (IOException ex) {
                    Logger.getLogger(OntoGenForm.class.getName()).log(Level.SEVERE, null, ex);
                }


                try {
                    Properties properties = new Properties();
                    properties.setProperty("ontologyFile", myConfig.getProjectName() + "-Mapping.owl");
                    properties.setProperty("rootClass", myConfig.getLeftNamespace() + getOWLClassName(myConfig.getProjectName()) + "-Root");
                    properties.setProperty("createOntology", "true");
                    properties.setProperty("loadFacts", "true");
                    properties.setProperty("jdbc", "true");
                    properties.setProperty("inferencingLevel", "0");
                    properties.setProperty("outputMode", "0");
                    properties.setProperty("verbosity", "5");

                    File propertiesFileFile = new File("OntoExport.properties");
                    properties.store(new FileOutputStream(propertiesFileFile), "OntoExport Properties File");
                } catch (IOException ex) {
                    Logger.getLogger(OntoGenForm.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            outWriter.close();
            myOntoGenForm.logWrite("Done! File 'Output.sql' created.");

        } catch (IOException ex) {
            Logger.getLogger(Exporter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
