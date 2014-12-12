
public class i2b2OntologyEntry {

    public int C_HLEVEL = 0;
    public String C_FULLNAME = "";
    public String C_NAME = "";
    public String C_SYNONYM_CD = "N";
    public String C_VISUALATTRIBUTES = "FAE";
    public String C_TOTALNUM = "NULL";
    public String C_BASECODE = "";
    public String C_METADATAXML = "";
    public String C_FACTTABLECOLUMN = "concept_cd";
    public String C_TABLENAME = "concept_dimension";
    public String C_COLUMNNAME = "concept_path";
    public String C_COLUMNDATATYPE = "T";
    public String C_OPERATOR = "LIKE";
    public String C_DIMCODE = "";
    public String C_COMMENT = "";
    public String C_TOOLTIP = "";
    public String UPDATE_DATE = "sysdate";
    public String DOWNLOAD_DATE = "";
    public String IMPORT_DATE = "";
    public String SOURCESYSTEM_CD = "";
    public String VALUETYPE_CD = "";
    String xmlHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ValueMetadata><Version>3.2</Version><CreationDateTime>2010-11-17T20:21:04.486+01:00</CreationDateTime>      <Loinc>Unspecified</Loinc><UnitValues><NormalUnits>Unspecified</NormalUnits></UnitValues>";
    public String xml_DataType = "";
    public String xml_Flagstouse = "";
    public String xml_Oktousevalues = "";
    public String xml_MaxStringLength = "";
    public String xml_LowofLowValue = "";
    public String xml_HighofLowValue = "";
    public String xml_LowofHighValue = "";
    public String xml_HighofHighValue = "";
    public String xml_LowofToxicValue = "";
    public String xml_HighofToxicValue = "";
    //public String xml_EnumValues = "";
    public String xml_Val = ""; // Valdescription
    //public String xml_UnitValues = "";
    public String xml_NormalUnits = "";
    public String xml_EqualUnits = "";
    public String xml_ExcludingUnits = "";
    public String xml_Units = "";
    public String xml_ConvertingUnits = "";
    private DatabaseConnection myConn = null;
    // New in i2b2 1.6:   
    public String M_APPLIED_PATH = "@";
    public String M_EXCLUSION_CD = "";
    public String C_PATH = "";
    public String C_SYMBOL = "";

    public void set_xml_DataType(String param) {
        xml_DataType = "<DataType>" + param + "</DataType>";

    }

    public void set_xml_Flagstouse(String param) {
        xml_Flagstouse = "<Flagstouse>" + param + "</Flagstouse>";

    }

    public void set_xml_Oktousevalues(String param) {
        xml_Oktousevalues = "<Oktousevalues>" + param + "</Oktousevalues>";

    }

    public void set_xml_MaxStringLength(String param) {
        xml_MaxStringLength = "<MaxStringLength>" + param + "</MaxStringLength>";

    }

    public void set_xml_LowofLowValue(String param) {
        xml_LowofLowValue = "<LowofLowValue>" + param + "</LowofLowValue>";

    }

    public void set_xml_HighofLowValue(String param) {
        xml_HighofLowValue = "<HighofLowValue>" + param + "</HighofLowValue>";

    }

    public void set_xml_LowofHighValue(String param) {
        xml_LowofHighValue = "<LowofHighValue>" + param + "</LowofHighValue>";

    }

    public void set_xml_HighofHighValue(String param) {
        xml_HighofHighValue = "<HighofHighValue>" + param + "</HighofHighValue>";

    }

    public void set_xml_LowofToxicValue(String param) {
        xml_LowofToxicValue = "<LowofToxicValue>" + param + "</LowofToxicValue>";

    }

    public void set_xml_HighofToxicValue(String param) {
        xml_LowofToxicValue = "<HighofToxicValue>" + param + "</HighofToxicValue>";

    }

    public void add_xml_Val(String param) {
        xml_Val += "<Val>" + param + "</Val>";

    }

    public void set_xml_NormalUnits(String param) {
        xml_NormalUnits = "<NormalUnits>" + param + "</NormalUnits>";

    }

    public void add_xml_EqualUnits(String param) {
        xml_EqualUnits += "<EqualUnits>" + param + "</EqualUnits>";

    }

    public void add_xml_ExcludingUnits(String param) {
        xml_ExcludingUnits += "<ExcludingUnits>" + param + "</ExcludingUnits>";

    }

    public void add_xml_ConvertingUnits(String unit, String factor) {
        xml_ConvertingUnits += "<ConvertingUnits><Units>" + unit + "</Units><MultiplyingFactor>" + factor + "</MultiplyingFactor></ConvertingUnits>";

    }

    public String getXML() {

        String XML = xmlHead;

        if (!xml_DataType.equals("")) {
            XML += xml_DataType;
        }
        if (!xml_Flagstouse.equals("")) {
            XML += xml_Flagstouse;
        }
        if (!xml_Oktousevalues.equals("")) {
            XML += xml_Oktousevalues;
        }
        if (!xml_MaxStringLength.equals("")) {
            XML += xml_MaxStringLength;
        }
        if (!xml_LowofLowValue.equals("")) {
            XML += xml_LowofLowValue;
        }
        if (!xml_HighofLowValue.equals("")) {
            XML += xml_HighofLowValue;
        }
        if (!xml_LowofHighValue.equals("")) {
            XML += xml_LowofHighValue;
        }
        if (!xml_HighofHighValue.equals("")) {
            XML += xml_HighofHighValue;
        }
        if (!xml_LowofToxicValue.equals("")) {
            XML += xml_LowofToxicValue;
        }
        if (!xml_HighofToxicValue.equals("")) {
            XML += xml_HighofToxicValue;
        }
        if (!xml_Val.equals("")) {
            XML += "<EnumValues>" + xml_Val + "<EnumValues>";
        }


        //|| !xml_EqualUnits.equals("") || !xml_Units.equals("") || !xml_MultiplyingFactor.equals(""))

        if (!xml_NormalUnits.equals("")) {
            XML += "<UnitValues>" + xml_NormalUnits;

            if (!xml_EqualUnits.equals("")) {
                XML += xml_EqualUnits;
            }

            if (!xml_ExcludingUnits.equals("")) {
                XML += xml_ExcludingUnits;
            }

            if (!xml_ConvertingUnits.equals("")) {
                XML += xml_ConvertingUnits;
            }
            XML += "<UnitValues>";
        }

        XML += "</ValueMetadata>";

        return XML;
    }


    i2b2OntologyEntry(DatabaseConnection connection) {

        /*
         * if (connection != null) { System.out.println("Connection angegeben");
         * }
         */

        myConn = connection;
    }

    i2b2OntologyEntry() {
        System.out.println("Keine connection angegeben");
    }

    void insertInto() {

        /*
         * C_FULLNAME = C_FULLNAME.replace("Ä", "Ae"); C_FULLNAME =
         * C_FULLNAME.replace("Ö", "Oe"); C_FULLNAME = C_FULLNAME.replace("Ü",
         * "Ue"); C_FULLNAME = C_FULLNAME.replace("ä", "ae"); C_FULLNAME =
         * C_FULLNAME.replace("ö", "oe"); C_FULLNAME = C_FULLNAME.replace("ü",
         * "ue"); C_FULLNAME = C_FULLNAME.replace("ß", "ss");
         *
         * C_NAME = C_NAME.replace("Ä", "Ae"); C_NAME = C_NAME.replace("Ö",
         * "Oe"); C_NAME = C_NAME.replace("Ü", "Ue"); C_NAME =
         * C_NAME.replace("ä", "ae"); C_NAME = C_NAME.replace("ö", "oe"); C_NAME
         * = C_NAME.replace("ü", "ue"); C_NAME = C_NAME.replace("ß", "ss");
         *
         * C_TOOLTIP = C_TOOLTIP.replace("Ä", "Ae"); C_TOOLTIP =
         * C_TOOLTIP.replace("Ö", "Oe"); C_TOOLTIP = C_TOOLTIP.replace("Ü",
         * "Ue"); C_TOOLTIP = C_TOOLTIP.replace("ä", "ae"); C_TOOLTIP =
         * C_TOOLTIP.replace("ö", "oe"); C_TOOLTIP = C_TOOLTIP.replace("ü",
         * "ue"); C_TOOLTIP = C_TOOLTIP.replace("ß", "ss");
         *
         * C_DIMCODE = C_DIMCODE.replace("Ä", "Ae"); C_DIMCODE =
         * C_DIMCODE.replace("Ö", "Oe"); C_DIMCODE = C_DIMCODE.replace("Ü",
         * "Ue"); C_DIMCODE = C_DIMCODE.replace("ä", "ae"); C_DIMCODE =
         * C_DIMCODE.replace("ö", "oe"); C_DIMCODE = C_DIMCODE.replace("ü",
         * "ue"); C_DIMCODE = C_DIMCODE.replace("ß", "ss");
         *
         *
         */

        C_NAME = C_NAME.replaceAll("'", "''");

        String sqlCommand = "INSERT INTO " + myConn.getI2b2schema() + "I2B2("
                + "C_HLEVEL, C_FULLNAME, C_NAME, C_SYNONYM_CD, "
                + "C_VISUALATTRIBUTES, C_TOTALNUM, C_BASECODE, C_METADATAXML, "
                + "C_FACTTABLECOLUMN, C_TABLENAME, C_COLUMNNAME, "
                + "C_COLUMNDATATYPE, C_OPERATOR, C_DIMCODE, C_COMMENT, "
                + "C_TOOLTIP, UPDATE_DATE, DOWNLOAD_DATE, IMPORT_DATE, "
                + "VALUETYPE_CD, M_APPLIED_PATH, M_EXCLUSION_CD, C_PATH, C_SYMBOL) VALUES ("
                + C_HLEVEL + ", '" + C_FULLNAME + "', '" + C_NAME + "', '" + C_SYNONYM_CD + "', '"
                + C_VISUALATTRIBUTES + "', " + C_TOTALNUM + ", '" + C_BASECODE + "', '" + C_METADATAXML + "', '"
                + C_FACTTABLECOLUMN + "', '" + C_TABLENAME + "', '" + C_COLUMNNAME + "', '"
                + C_COLUMNDATATYPE + "', '" + C_OPERATOR + "', '" + C_DIMCODE + "', '" + C_COMMENT + "', '"
                + C_TOOLTIP + "', " + UPDATE_DATE + ", '" + DOWNLOAD_DATE + "', '" + IMPORT_DATE + "', '" + VALUETYPE_CD + "', '"
                + M_APPLIED_PATH + "', '"
                + M_EXCLUSION_CD + "', '"
                + C_PATH + "', '"
                + C_SYMBOL
                + "')";

        myConn.executeSQL(sqlCommand);
    }

    void emptyTable() {
        //String sqlCommand = "TRUNCATE TABLE I2B2";
        //myConn.executeSQL(sqlCommand);

        String sqlCommand = "DELETE FROM " + myConn.getI2b2schema() + "I2B2";
        myConn.executeSQL(sqlCommand);
    }

    void commit() {
        String sqlCommand = "COMMIT";
        myConn.executeSQL(sqlCommand);
    }

    String getLastSQL() {

        return myConn.getLastSQL();

    }


}
