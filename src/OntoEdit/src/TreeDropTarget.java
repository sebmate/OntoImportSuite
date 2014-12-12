//TreeDropTarget.java
//A quick DropTarget that's looking for drops from draggable JTrees.
//

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.DnDConstants;
import java.net.URLDecoder;
import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

class TreeDropTarget implements DropTargetListener {

    DropTarget target;
    JTree targetTree;

    public TreeDropTarget(JTree tree) {
        targetTree = tree;
        target = new DropTarget(targetTree, this);
    }

    /*
     * Drop Event Handlers
     */
    private TreeNode getNodeForEvent(DropTargetDragEvent dtde) {
        Point p = dtde.getLocation();
        DropTargetContext dtc = dtde.getDropTargetContext();
        JTree tree = (JTree) dtc.getComponent();
        TreePath path = tree.getClosestPathForLocation(p.x, p.y);
        return (TreeNode) path.getLastPathComponent();
    }

    public void dragEnter(DropTargetDragEvent dtde) {

        TreeNode node = getNodeForEvent(dtde);

        //dtde.acceptDrag(DnDConstants.ACTION_MOVE);
        dtde.acceptDrag(dtde.getDropAction());

    }

    public void dragOver(DropTargetDragEvent dtde) {

        TreeNode node = getNodeForEvent(dtde);

        // dtde.acceptDrag(DnDConstants.ACTION_MOVE);
        dtde.acceptDrag(dtde.getDropAction());

        DropTargetContext dtc = dtde.getDropTargetContext();
        JTree tree = (JTree) dtc.getComponent();
        TreePath path = tree.getClosestPathForLocation(dtde.getLocation().x, dtde.getLocation().y);
        tree.setSelectionPath(path);

    }

    public void dragExit(DropTargetEvent dte) {
    }

    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    private String getOWLName(String element) {
        String returnString = element;
        returnString = returnString.replaceAll(" ", "_");
        returnString = returnString.replaceAll("Ä", "Ae");
        returnString = returnString.replaceAll("Ö", "Oe");
        returnString = returnString.replaceAll("Ü", "Ue");
        returnString = returnString.replaceAll("ä", "ae");
        returnString = returnString.replaceAll("ö", "oe");
        returnString = returnString.replaceAll("ü", "ue");
        returnString = returnString.replaceAll("ß", "ss");
        returnString = returnString.replaceAll("[^a-zA-Z0-9]", "_");
        return returnString;
    }

    public DefaultMutableTreeNode searchNode(String nodeStr, JTree treeBrowser) {

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeBrowser.getModel().getRoot();
        Enumeration e = node.breadthFirstEnumeration();
        while (e.hasMoreElements()) {
            node = (DefaultMutableTreeNode) e.nextElement();
            if (nodeStr.equals(node.getUserObject().toString())) {
                System.out.println("Found!");
                return node;
            }
        }
        return null;
    }

    public void drop(DropTargetDropEvent dtde) {

        Point pt = dtde.getLocation();
        DropTargetContext dtc = dtde.getDropTargetContext();
        JTree tree = (JTree) dtc.getComponent();
        TreePath parentpath = tree.getClosestPathForLocation(pt.x, pt.y);
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) parentpath.getLastPathComponent();
        Object object;

        //if (parent.isLeaf()) {
        //dtde.rejectDrop();
        //return;
        //}

        try {
            Transferable tr = dtde.getTransferable();
            DataFlavor[] flavors = tr.getTransferDataFlavors();

            for (int i = 0; i < flavors.length; i++) {

                if (tr.isDataFlavorSupported(flavors[i])) {

                    dtde.acceptDrop(dtde.getDropAction());

                    object = tr.getTransferData(flavors[i]);
                    Class c = object.getClass();

                    if (c.getName().equals("java.net.URL")) {

                        String nodeName = URLDecoder.decode(object.toString());
                        nodeName = nodeName.replaceAll("http://localhost:8080/", "");
                        nodeName = nodeName.replaceAll("#selected", "");
                        nodeName = nodeName.replaceAll("/#selected", "");


                        System.out.println("Dropped URL: " + nodeName);

                        String[] splits = nodeName.split("/");

                        String returnString2 = getOWLName(splits[0]);
                        String returnString3 = returnString2;


                        int uniqueCnt = 0;
                        while (searchNode(returnString3, tree) != null) {
                            uniqueCnt++;
                            System.out.println("Node already exists!");
                            returnString3 = returnString2 + "_" + uniqueCnt;
                        }
                        OntologyNode newOntoNode = new OntologyNode(returnString3);
                        newOntoNode.setNiceName(splits[0]);
                        newOntoNode.setNiceName(splits[1]);
                        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newOntoNode);
                        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                        model.insertNodeInto(newNode, parent, 0);


                        if (splits.length > 3) {

                            for (int ii = 2; ii < splits.length; ii = ii + 2) {

                                returnString2 = getOWLName(splits[ii]);
                                returnString3 = returnString2;
                                uniqueCnt = 0;

                                while (searchNode(returnString3, tree) != null) {
                                    uniqueCnt++;
                                    System.out.println("Node already exists!");
                                    returnString3 = returnString2 + "_" + uniqueCnt;
                                }

                                OntologyNode newOntoNode2 = new OntologyNode(returnString3);
                                newOntoNode2.setNiceName(splits[ii]);
                                newOntoNode2.setNiceName(splits[ii + 1]);
                                DefaultMutableTreeNode newNode2 = new DefaultMutableTreeNode(newOntoNode2);
                                DefaultTreeModel model2 = (DefaultTreeModel) tree.getModel();
                                model2.insertNodeInto(newNode2, newNode, 0);

                            }
                        }

                        dtde.dropComplete(true);

                    } else if (c.getName().equals("javax.swing.tree.TreePath")) {
                        TreePath p = (TreePath) tr.getTransferData(flavors[i]);
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) p.getLastPathComponent();
                        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                        model.insertNodeInto(node, parent, 0);
                        dtde.dropComplete(true);

                    } else {

                        System.out.println("Unknown object type: " + c.getName());
                    }

                    return;
                }
            }
            dtde.rejectDrop();

        } catch (Exception e) {
            e.printStackTrace();
            dtde.rejectDrop();
        }
    }
}
