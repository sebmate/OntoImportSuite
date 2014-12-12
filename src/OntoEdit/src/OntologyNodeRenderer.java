
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class OntologyNodeRenderer extends DefaultTreeCellRenderer {

    private boolean isSelected;

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);



        if (leaf) {

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            OntologyNode nodeInfo = (OntologyNode) node.getUserObject();


            if (nodeInfo.getNodeType() == 1 & nodeInfo.isHighlighted()) {
                this.setIcon(new ImageIcon("icons" + File.separator + "individual2.png"));
            } else if (nodeInfo.getNodeType() == 1 & !nodeInfo.isHighlighted()) {
                this.setIcon(new ImageIcon("icons" + File.separator + "individual.png"));
            } else if (nodeInfo.getNodeType() == 0 & nodeInfo.isHighlighted()) {
                this.setIcon(new ImageIcon("icons" + File.separator + "class2.png"));
            } else if (nodeInfo.getNodeType() == 0 & !nodeInfo.isHighlighted()) {
                this.setIcon(new ImageIcon("icons" + File.separator + "class.png"));
            }



            Color col = nodeInfo.getColor();
            this.setBackgroundNonSelectionColor(col);


        } else {
            this.setIcon(new ImageIcon("icons" + File.separator + "class.png"));

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

            //if (node.getUserObject() instanceof DefaultMutableTreeNode) {

            //OntologyNode nodeInfo = (OntologyNode) node.getUserObject();
            //Color col = nodeInfo.getColor();
            //this.setBackgroundNonSelectionColor(col);
            //}
        }




        return this;
    }
}
