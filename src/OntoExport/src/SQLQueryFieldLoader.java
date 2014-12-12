
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;

/**
 *
 * @author sebmate
 */
class SQLQueryFieldLoader {

    /**
     * @return the SourceFilter
     */
    public String getSourceFilter() {
        return SourceFilter;
    }

    /**
     * @param SourceFilter the SourceFilter to set
     */
    public void setSourceFilter(String SourceFilter) {
        this.SourceFilter = SourceFilter;
    }

    /**
     * @return the dateEndValueColumn
     */
    public String getDateEndValueColumn() {
        return dateEndValueColumn;
    }

    /**
     * @param dateEndValueColumn the dateEndValueColumn to set
     */
    public void setDateEndValueColumn(String dateEndValueColumn) {
        this.dateEndValueColumn = dateEndValueColumn;
    }

    /**
     * @return the dateStartValueColumn
     */
    public String getDateStartValueColumn() {
        return dateStartValueColumn;
    }

    /**
     * @param dateStartValueColumn the dateStartValueColumn to set
     */
    public void setDateStartValueColumn(String dateStartValueColumn) {
        this.dateStartValueColumn = dateStartValueColumn;
    }

    /**
     * @return the documentIDColumn
     */
    public String getDocumentIDColumn() {
        return documentIDColumn;
    }

    /**
     * @param documentIDColumn the documentIDColumn to set
     */
    public void setDocumentIDColumn(String documentIDColumn) {
        this.documentIDColumn = documentIDColumn;
    }

    /**
     * @return the patientIDColumn
     */
    public String getPatientIDColumn() {
        return patientIDColumn;
    }

    /**
     * @param patientIDColumn the patientIDColumn to set
     */
    public void setPatientIDColumn(String patientIDColumn) {
        this.patientIDColumn = patientIDColumn;
    }

    /**
     * @return the sourceTableName
     */
    public String getSourceTableName() {
        return sourceTableName;
    }

    /**
     * @param sourceTableName the sourceTableName to set
     */
    public void setSourceTableName(String sourceTableName) {
        this.sourceTableName = sourceTableName;
    }

    /**
     * @return the valueColumn
     */
    public String getValueColumn() {
        return valueColumn;
    }

    /**
     * @param valueColumn the valueColumn to set
     */
    public void setValueColumn(String valueColumn) {
        this.valueColumn = valueColumn;
    }

    /**
     * @return the SQL
     */
    public String getSQL() {
        return SQL;
    }

    /**
     * @param SQL the SQL to set
     */
    public void setSQL(String SQL) {
        this.SQL = SQL;
    }

    /**
     * @return the model
     */
    public OntModel getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(OntModel model) {
        this.model = model;
    }

    /**
     * @return the FetchSQL
     */
    public String getFetchSQL() {

        return FetchSQL;
    }

    public String getTransformedFetchSQL() {

        FetchSQL = FetchSQL.replace("$PATIENTID$", getPatientIDColumn());
        FetchSQL = FetchSQL.replace("$DOCUMENTID$", getDocumentIDColumn());
        FetchSQL = FetchSQL.replace("$DATESTARTVALUE$", getDateStartValueColumn());
        FetchSQL = FetchSQL.replace("$DATEENDVALUE$", getDateEndValueColumn());
        FetchSQL = FetchSQL.replace("$VALUE$", getValueColumn());
        FetchSQL = FetchSQL.replace("$SOURCETABLE$", getSourceTableName());
        FetchSQL = FetchSQL.replace("$SOURCEFILTER$", getSourceFilter());

        return FetchSQL;
    }

    /**
     * @param FetchSQL the FetchSQL to set
     */
    public void setFetchSQL(String FetchSQL) {
        this.FetchSQL = FetchSQL;
    }
    private OntModel model;
    private String SourceFilter = "";
    private String dateEndValueColumn = "";
    private String dateStartValueColumn = "";
    private String documentIDColumn = "";
    private String patientIDColumn = "";
    private String sourceTableName = "";
    private String valueColumn = "";
    private String SQL = "";
    private String FetchSQL = "";
    private String dateTrafo = "";
    // properties for subselects:
    private RDFNode hasDate = null;
    private boolean specialFetch;
    private String stringNode = "";  // stores a String Value, for example "1000"
    private String StringFetchSQL = "";

    SQLQueryFieldLoader(OntModel model) {
        this.model = model;
    }

    void superDuperStringTrick(RDFNode stringValue, SQLQueryFieldLoader otherNode) {

        //System.out.println("Got a StringNode: " + stringValue);
        stringNode = stringValue.toString();

        StringFetchSQL = otherNode.getFetchSQL();

        StringFetchSQL = StringFetchSQL.replace("$PATIENTID$", otherNode.getPatientIDColumn());
        StringFetchSQL = StringFetchSQL.replace("$DOCUMENTID$", otherNode.getDocumentIDColumn());
        StringFetchSQL = StringFetchSQL.replace("$DATESTARTVALUE$", otherNode.getDateStartValueColumn());
        StringFetchSQL = StringFetchSQL.replace("$DATEENDVALUE$", otherNode.getDateEndValueColumn());
        StringFetchSQL = StringFetchSQL.replace("$VALUE$", stringValue.toString().replace("\"", "'"));
        StringFetchSQL = StringFetchSQL.replace("$SOURCETABLE$", otherNode.getSourceTableName());
        StringFetchSQL = StringFetchSQL.replace("$SOURCEFILTER$", otherNode.getSourceFilter());

        //System.out.println("New tricky SQL subquery is: " + StringFetchSQL);

    }

    void getOperandFields(RDFNode node) {

        System.out.println("\ngetOperandFields() called for node: " + node);

        String nameSpace = "";

        // get hasSelectFilter for Operand:

        if (stringNode.equals("")) {

            ResultSet result;
            result = runQuery(" PREFIX p1: <" + getURIPrefix(node.toString()) + "#>  select ?dbr WHERE "
                    + "{ p1:" + simplifyURI(node.toString())
                    + " omsys:hasSelectFilter ?dbr }", getModel()); // add the query string

            while (result.hasNext()) {
                QuerySolution solution = result.nextSolution();
                Literal restriction = solution.getLiteral("?dbr");
                //System.out.println("     hasSelectFilter: " + restriction.toString());
                if (restriction != null) {
                    if (!getSourceFilter().equals("")) {
                        setSourceFilter(getSourceFilter() + " AND ");
                    }
                    setSourceFilter(getSourceFilter() + restriction + " ");
                }
            }

            ResultSet result2;

            result2 = runQuery(" PREFIX p1: <" + getURIPrefix(node.toString()) + "#>  select ?sourceTable "
                    + "?dateEndValueColumn ?dateStartValueColumn ?documentIDColumn ?patientIDColumn "
                    + "?sourceTableName ?valueColumn ?SQL ?fetchSQL ?dateItem ?dateTrafo "
                    + "WHERE "
                    + "{ p1:" + simplifyURI(node.toString()) + " omsys:hasSourceTable ?sourceTable ."
                    + " ?sourceTable omsys:hasDateEndValueColumn ?dateEndValueColumn."
                    + " ?sourceTable omsys:hasDateStartValueColumn ?dateStartValueColumn."
                    + " ?sourceTable omsys:hasDocumentIDColumn ?documentIDColumn."
                    + " ?sourceTable omsys:hasPatientIDColumn ?patientIDColumn."
                    + " ?sourceTable omsys:hasSourceTableName ?sourceTableName."
                    + " ?sourceTable omsys:hasValueColumn ?valueColumn."
                    + " ?sourceTable omsys:hasDatabaseConnection ?dbConn."
                    + " ?sourceTable omsys:hasAccessSQL ?SQL."
                    + " ?sourceTable omsys:hasOperandFetchSQL ?fetchSQL."
                    + " ?sourceTable omsys:hasDateTransformation ?dateTrafo."
                    + " OPTIONAL { p1:" + simplifyURI(node.toString()) + " omsys:hasDate ?dateItem. } "
                    + " }", getModel()); // add the query string */



            if (!result2.hasNext()) {
                System.out.println("ERROR: Could not get properties for database connection!");
            }


            while (result2.hasNext()) {

                //System.out.println("     Found the properties for database connection");
                QuerySolution solution2 = result2.nextSolution();

                Literal dateEndValueColumnLit = solution2.getLiteral("?dateEndValueColumn");
                Literal dateStartValueColumnLit = solution2.getLiteral("?dateStartValueColumn");
                Literal documentIDColumnLit = solution2.getLiteral("?documentIDColumn");
                Literal patientIDColumnLit = solution2.getLiteral("?patientIDColumn");
                Literal sourceTableNameLit = solution2.getLiteral("?sourceTableName");
                Literal valueColumnLit = solution2.getLiteral("?valueColumn");
                Literal SQLLit = solution2.getLiteral("?SQL");
                Literal fetchSQLLit = solution2.getLiteral("?fetchSQL");
                Literal dateTrafoLit = solution2.getLiteral("?dateTrafo");

                // process all properties related for subselects:

                RDFNode dateItem = solution2.get("?dateItem");
                if (dateItem != null) {
                    //System.out.println("  " + node + " hasDate " + dateItem.toString() + " .");
                    setHasDate(dateItem);
                }

                // ... TODO: check for other properties inside the source system ontology

                setDateEndValueColumn(simplifyURI(dateEndValueColumnLit.toString()));
                setDateStartValueColumn(simplifyURI(dateStartValueColumnLit.toString()));
                setDocumentIDColumn(simplifyURI(documentIDColumnLit.toString()));
                setPatientIDColumn(simplifyURI(patientIDColumnLit.toString()));
                setSourceTableName(simplifyURI(sourceTableNameLit.toString()));
                setValueColumn(simplifyURI(valueColumnLit.toString()));
                setSQL(simplifyURI(SQLLit.toString()));
                setFetchSQL(simplifyURI(fetchSQLLit.toString()));
                setDateTrafo(simplifyURI(dateTrafoLit.toString()));
            }

        } else {

            setDateEndValueColumn("");
            setDateStartValueColumn("");
            setDocumentIDColumn("");
            setPatientIDColumn("");
            setSourceTableName("");
            setValueColumn("");
            setSQL("");
            setFetchSQL("");
            setDateTrafo("");

        }

        String outerSelect = "SELECT DISTINCT S1.PatientID PatientID, S1.DocumentID DocumentID, S1.DateStartValue DateStartValue, S1.DateEndValue DateEndValue, S1.Value Value FROM";
        String innerSelect = "( " + getFetchSQL() + " ) S1 ";
        String joinStuff = "";

        if (getHasDate() != null) {

            joinStuff += "LEFT OUTER JOIN ( " + getFetchSQL() + " ) S2 ON S1.DocumentID = S2.DocumentID";
            SQLQueryFieldLoader subSelector = new SQLQueryFieldLoader(getModel());

            if (!node.equals(getHasDate())) { // No loop detected:  Field42 hasDate Field43

                subSelector.getOperandFields(getHasDate());

                String paramTrafo = subSelector.getDateTrafo();
                if (!paramTrafo.equals("")) {
                    paramTrafo = paramTrafo.replace("$VALUE$", "S2.Value");
                } else {
                    paramTrafo = "S2.Value";
                }
                outerSelect = outerSelect.replace("S1.DateStartValue", paramTrafo);
                joinStuff = joinStuff.replace("$PATIENTID$", subSelector.getPatientIDColumn());
                joinStuff = joinStuff.replace("$DOCUMENTID$", subSelector.getDocumentIDColumn());
                joinStuff = joinStuff.replace("$DATESTARTVALUE$", subSelector.getDateStartValueColumn());
                joinStuff = joinStuff.replace("$DATEENDVALUE$", subSelector.getDateEndValueColumn());
                joinStuff = joinStuff.replace("$VALUE$", subSelector.getValueColumn());
                joinStuff = joinStuff.replace("$SOURCETABLE$", subSelector.getSourceTableName());
                joinStuff = joinStuff.replace("$SOURCEFILTER$", subSelector.getSourceFilter());

                // Until the other properties (see TODO (2) below) are implemented, set the DateEndValue
                // to the same as the DateStartValue:
                outerSelect = outerSelect.replace("S1.DateEndValue", paramTrafo) + "";

            } else {

                // Loop detected:  Field42 hasDate Field42

                String paramTrafo = getDateTrafo();
                if (!paramTrafo.equals("")) {
                    paramTrafo = paramTrafo.replace("$VALUE$", "S2.Value");
                } else {
                    paramTrafo = "S2.Value";
                }
                outerSelect = outerSelect.replace("S1.DateStartValue", paramTrafo);
                joinStuff = joinStuff.replace("$PATIENTID$", getPatientIDColumn());
                joinStuff = joinStuff.replace("$DOCUMENTID$", getDocumentIDColumn());
                joinStuff = joinStuff.replace("$DATESTARTVALUE$", getDateStartValueColumn());
                joinStuff = joinStuff.replace("$DATEENDVALUE$", getDateEndValueColumn());
                joinStuff = joinStuff.replace("$VALUE$", getValueColumn());
                joinStuff = joinStuff.replace("$SOURCETABLE$", getSourceTableName());
                joinStuff = joinStuff.replace("$SOURCEFILTER$", getSourceFilter());

                // Until the other properties (see TODO (2) below) are implemented, set the DateEndValue
                // to the same as the DateStartValue:
                outerSelect = outerSelect.replace("S1.DateEndValue", paramTrafo) + "";

            }
        }

        // TODO (2): continue to build up the joinStuff for the other properties like hasDate ...

        if (!joinStuff.equals("")) {
            setFetchSQL(outerSelect + innerSelect + joinStuff + " /*DATUM!*/");
            setSpecialFetch(true);
            //System.out.println(getFetchSQL());
        }

        if (!stringNode.equals("")) {   // stores a string value, for example "1000"
            setFetchSQL(StringFetchSQL);
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

    private String getURIPrefix(String string) {
        if (string.indexOf('#') != -1) {
            string = string.substring(0, string.indexOf('#'));
        }
        return string;
    }

    ResultSet runQuery(String queryRequest, Model model) {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("PREFIX omsys" + ": <" + "http://www.uk-erlangen.de/OntoMappingSystem#" + "> ");
        queryStr.append("PREFIX rdfs" + ": <" + "http://www.w3.org/2000/01/rdf-schema#" + "> ");
        queryStr.append("PREFIX rdf" + ": <" + "http://www.w3.org/1999/02/22-rdf-syntax-ns#" + "> ");
        queryStr.append(queryRequest);

        Query query = QueryFactory.create(queryStr.toString());
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        ResultSet response = qexec.execSelect();
        return response;
    }

    /**
     * @return the hasDate
     */
    public RDFNode getHasDate() {
        return hasDate;
    }

    /**
     * @param hasDate the hasDate to set
     */
    public void setHasDate(RDFNode hasDate) {
        this.hasDate = hasDate;
    }

    /**
     * @return the dateTrafo
     */
    public String getDateTrafo() {
        return dateTrafo;
    }

    /**
     * @param dateTrafo the dateTrafo to set
     */
    public void setDateTrafo(String dateTrafo) {
        this.dateTrafo = dateTrafo;
    }

    /**
     * @return the specialFetch
     */
    public boolean isSpecialFetch() {
        return specialFetch;
    }

    /**
     * @param specialFetch the specialFetch to set
     */
    public void setSpecialFetch(boolean specialFetch) {
        this.specialFetch = specialFetch;
    }
}
