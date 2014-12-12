
import java.awt.Color;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author sebmate
 */
public class OntologyNode {

    String nodeName = "Blubb";
    int nodeType = 0; // 0=Class, 1=Individual
    private Boolean highlighted = false;

    private Color color = new Color(255, 255, 255);

    OntologyNode(String nodeName, int nodeType) {
        this.nodeName = nodeName;
        this.nodeType = nodeType;
    }

    @Override
    public String toString() {
        return nodeName;
    }

    public int getNodeType() {
        return nodeType;
    }

    /**
     * @return the highlighted
     */
    public Boolean isHighlighted() {
        return highlighted;
    }

    /**
     * @param highlighted the highlighted to set
     */
    public void setHighlighted(Boolean highlighted) {
        this.highlighted = highlighted;
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
}
