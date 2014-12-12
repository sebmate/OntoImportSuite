import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class EditableTree extends JFrame {

  private DefaultMutableTreeNode m_rootNode = new DefaultMutableTreeNode("AA");
  private DefaultTreeModel m_model = new DefaultTreeModel(m_rootNode);

  private JTree m_tree = new JTree(m_model);

  private JButton m_addButton = new JButton("Add Node");

  private JButton m_delButton = new JButton("Delete Node");

  private JButton m_searchButton = new JButton("Search Node");

  private JButton m_searchAndDeleteButton = new JButton("Search and Delete Node");

  private JTextField m_searchText;

  public EditableTree() {
    DefaultMutableTreeNode forums = new DefaultMutableTreeNode("A");
    forums.add(new DefaultMutableTreeNode("B"));
    DefaultMutableTreeNode articles = new DefaultMutableTreeNode("E");
    articles.add(new DefaultMutableTreeNode("F"));
    DefaultMutableTreeNode examples = new DefaultMutableTreeNode("G");
    examples.add(new DefaultMutableTreeNode("H"));

    m_rootNode.add(forums);
    m_rootNode.add(articles);
    m_rootNode.add(examples);

    m_tree.setEditable(true);
    m_tree.setSelectionRow(0);

    JScrollPane scrollPane = new JScrollPane(m_tree);
    getContentPane().add(scrollPane, BorderLayout.CENTER);

    JPanel panel = new JPanel();

    m_addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) m_tree
            .getLastSelectedPathComponent();
        if (selNode == null) {
          return;
        }
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("New Node");
        m_model.insertNodeInto(newNode, selNode, selNode.getChildCount());

        TreeNode[] nodes = m_model.getPathToRoot(newNode);
        TreePath path = new TreePath(nodes);
        m_tree.scrollPathToVisible(path);
        m_tree.setSelectionPath(path);
        m_tree.startEditingAtPath(path);
      }
    });
    panel.add(m_addButton);

    m_delButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) m_tree
            .getLastSelectedPathComponent();
        removeNode(selNode);
      }
    });
    panel.add(m_delButton);

    JPanel searchPanel = new JPanel();
    searchPanel.setBorder(BorderFactory.createEtchedBorder());

    m_searchText = new JTextField(10);
    searchPanel.add(m_searchText);

    m_searchButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DefaultMutableTreeNode node = searchNode(m_searchText.getText());
        if (node != null) {
          TreeNode[] nodes = m_model.getPathToRoot(node);
          TreePath path = new TreePath(nodes);
          m_tree.scrollPathToVisible(path);
          m_tree.setSelectionPath(path);
        } else {
          System.out.println("Node with string " + m_searchText.getText() + " not found");
        }
      }
    });
    searchPanel.add(m_searchButton);

    m_searchAndDeleteButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DefaultMutableTreeNode node = searchNode(m_searchText.getText());
        if (node != null) {
          removeNode(node);
        } else {
          System.out.println("Node with string " + m_searchText.getText() + " not found");
        }
      }
    });
    searchPanel.add(m_searchAndDeleteButton);
    panel.add(searchPanel);
    getContentPane().add(panel, BorderLayout.SOUTH);
    setSize(700, 400);
    setVisible(true);
  }

  public DefaultMutableTreeNode searchNode(String nodeStr) {
    DefaultMutableTreeNode node = null;
    Enumeration e = m_rootNode.breadthFirstEnumeration();
    while (e.hasMoreElements()) {
      node = (DefaultMutableTreeNode) e.nextElement();
      if (nodeStr.equals(node.getUserObject().toString())) {
        return node;
      }
    }
    return null;
  }

  public void removeNode(DefaultMutableTreeNode selNode) {
    if (selNode == null) {
      return;
    }
    MutableTreeNode parent = (MutableTreeNode) (selNode.getParent());
    if (parent == null) {
      return;
    }
    MutableTreeNode toBeSelNode = getSibling(selNode);
    if (toBeSelNode == null) {
      toBeSelNode = parent;
    }
    TreeNode[] nodes = m_model.getPathToRoot(toBeSelNode);
    TreePath path = new TreePath(nodes);
    m_tree.scrollPathToVisible(path);
    m_tree.setSelectionPath(path);
    m_model.removeNodeFromParent(selNode);
  }

  private MutableTreeNode getSibling(DefaultMutableTreeNode selNode) {
    MutableTreeNode sibling = (MutableTreeNode) selNode.getPreviousSibling();
    if (sibling == null) {
      sibling = (MutableTreeNode) selNode.getNextSibling();
    }
    return sibling;
  }

  public static void main(String[] arg) {
    EditableTree editableTree = new EditableTree();
  }
  
  
  
}