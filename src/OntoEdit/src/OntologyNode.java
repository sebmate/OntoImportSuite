
import java.io.Serializable;

import java.awt.Color;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author sebmate
 */
public class OntologyNode implements Serializable {

    int nodeType = 0; // 0=Class, 1=Individual
    private Boolean highlighted = false;
    private Color color = new Color(255, 255, 255);
    private String OWLName = "";
    private String nodeName = "";
    private String NiceName = "";
    private String DataType = "";
    private String Units = "";
    private String FlagsToUse = "";
    private String Description = "";
    private String Context = "";
    private String ConceptCodeSuffix = "";
    private String ConceptCodePrefix = "";
    private String LowOfLowValue = "";
    private String HighOfLowValue = "";
    private String LowOfHighValue = "";
    private String HighOfHighValue = "";
    private String LowOfToxicValue = "";
    private String HighOfToxicValue = "";
    private Boolean doNotExport = false;

    OntologyNode(String nodeName, int nodeType) {
        this.nodeName = nodeName;
        this.nodeType = nodeType;
    }

    OntologyNode(String nodeName) {
        this.nodeName = nodeName;
    }

    @Override
    public String toString() {
        return getNodeName();
    }

    public int getNodeType() {
        return nodeType;
    }

    /**
     * @param nodeType the nodeType to set
     */
    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * @return the nodeName
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * @param nodeName the nodeName to set
     */
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * @return the niceName
     */
    public String getNiceName() {
        return NiceName;
    }

    /**
     * @param niceName the niceName to set
     */
    public void setNiceName(String niceName) {
        this.NiceName = niceName;
    }

    /**
     * @return the hasDescription
     */
    public String getNodeDescription() {
        return getDescription();
    }

    /**
     * @param hasDescription the hasDescription to set
     */
    public void setNodeDescription(String nodeDescription) {
        this.setDescription(nodeDescription);
    }

    /**
     * @return the conceptCodeSuffix
     */
    public String getConceptCodeSuffix() {
        return ConceptCodeSuffix;
    }

    /**
     * @param conceptCodeSuffix the conceptCodeSuffix to set
     */
    public void setConceptCodeSuffix(String conceptCodeSuffix) {
        this.ConceptCodeSuffix = conceptCodeSuffix;
    }

    /**
     * @return the conceptCodePrefix
     */
    public String getConceptCodePrefix() {
        return ConceptCodePrefix;
    }

    /**
     * @param conceptCodePrefix the conceptCodePrefix to set
     */
    public void setConceptCodePrefix(String conceptCodePrefix) {
        this.ConceptCodePrefix = conceptCodePrefix;
    }

    /**
     * @return the LowOfLowValue
     */
    public String getLowOfLowValue() {
        return LowOfLowValue;
    }

    /**
     * @param LowOfLowValue the LowOfLowValue to set
     */
    public void setLowOfLowValue(String LowOfLowValue) {
        this.LowOfLowValue = LowOfLowValue;
    }

    /**
     * @return the HighOfLowValue
     */
    public String getHighOfLowValue() {
        return HighOfLowValue;
    }

    /**
     * @param HighOfLowValue the HighOfLowValue to set
     */
    public void setHighOfLowValue(String HighOfLowValue) {
        this.HighOfLowValue = HighOfLowValue;
    }

    /**
     * @return the LowOfHighValue
     */
    public String getLowOfHighValue() {
        return LowOfHighValue;
    }

    /**
     * @param LowOfHighValue the LowOfHighValue to set
     */
    public void setLowOfHighValue(String LowOfHighValue) {
        this.LowOfHighValue = LowOfHighValue;
    }

    /**
     * @return the HighOfHighValue
     */
    public String getHighOfHighValue() {
        return HighOfHighValue;
    }

    /**
     * @param HighOfHighValue the HighOfHighValue to set
     */
    public void setHighOfHighValue(String HighOfHighValue) {
        this.HighOfHighValue = HighOfHighValue;
    }

    /**
     * @return the LowOfToxicValue
     */
    public String getLowOfToxicValue() {
        return LowOfToxicValue;
    }

    /**
     * @param LowOfToxicValue the LowOfToxicValue to set
     */
    public void setLowOfToxicValue(String LowOfToxicValue) {
        this.LowOfToxicValue = LowOfToxicValue;
    }

    /**
     * @return the HighOfToxicValue
     */
    public String getHighOfToxicValue() {
        return HighOfToxicValue;
    }

    /**
     * @param HighOfToxicValue the HighOfToxicValue to set
     */
    public void setHighOfToxicValue(String HighOfToxicValue) {
        this.HighOfToxicValue = HighOfToxicValue;
    }

    /**
     * @return the Description
     */
    public String getDescription() {
        return Description;
    }

    /**
     * @param Description the Description to set
     */
    public void setDescription(String Description) {
        this.Description = Description;
    }

    /**
     * @return the DataType
     */
    public String getDataType() {
        return DataType;
    }

    /**
     * @param DataType the DataType to set
     */
    public void setDataType(String DataType) {
        if (DataType != null) {
            this.DataType = DataType;
        }
    }

    /**
     * @return the FlagsToUse
     */
    public String getFlagsToUse() {
        return FlagsToUse;
    }

    /**
     * @param FlagsToUse the FlagsToUse to set
     */
    public void setFlagsToUse(String FlagsToUse) {
        this.FlagsToUse = FlagsToUse;
    }

    /**
     * @return the Units
     */
    public String getUnits() {
        return Units;
    }

    /**
     * @param Units the Units to set
     */
    public void setUnits(String Units) {
        this.Units = Units;
    }

    /**
     * @return the Context
     */
    public String getContext() {
        return Context;
    }

    /**
     * @param Context the Context to set
     */
    public void setContext(String Context) {
        this.Context = Context;
    }

    /**
     * @return the doNotExport
     */
    public Boolean getDoNotExport() {
        return doNotExport;
    }

    /**
     * @param doNotExport the doNotExport to set
     */
    public void setDoNotExport(Boolean doNotExport) {
        this.doNotExport = doNotExport;
    }

    /**
     * @return the OWLName
     */
    public String getOWLName() {
        return OWLName;
    }

    /**
     * @param OWLName the OWLName to set
     */
    public void setOWLName(String OWLName) {
        this.OWLName = OWLName;
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }

    public Boolean isHighlighted() {
        return highlighted;
    }

    /**
     * @param highlighted the highlighted to set
     */
    public void setHighlighted(Boolean highlighted) {
        this.highlighted = highlighted;
    }
}
