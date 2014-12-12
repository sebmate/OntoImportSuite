
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.ValidityReport;
import com.hp.hpl.jena.vocabulary.RDFS;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import java.awt.dnd.DnDConstants;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.JTree;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class OntoEditForm extends javax.swing.JFrame {

    private TreeDragSource ds;
    private TreeDropTarget dt;
    private JTree tree;
    private DefaultMutableTreeNode rootNode; // = new DefaultMutableTreeNode("");
    private DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
    private final OntModel ontModel;
    private String NsPrefixURI = "";
    private OntModel outModel;
    private String inputFile;
    private DefaultMutableTreeNode lastSelectedNode = null;
    private String lastInterestingNode = "";

    /**
     * Creates new form OntoEditForm
     */
    public OntoEditForm() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OntoEditForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(OntoEditForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(OntoEditForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(OntoEditForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        initComponents();
        tree = new AutoScrollingJTree();
        jScrollPane1.setViewportView(tree);
        ds = new TreeDragSource(tree, DnDConstants.ACTION_COPY_OR_MOVE);
        dt = new TreeDropTarget(tree);
        
                
        OntologyNodeRenderer myRenderer = new OntologyNodeRenderer();
        tree.setCellRenderer(myRenderer);
        setVisible(true);

        tree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {

            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                treeValueChanged(evt);
            }
        });

        hasFlagsToUse.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"", "High/Low", "Abnormal"}));
        tree.setShowsRootHandles(false);
        rootNode = new DefaultMutableTreeNode(new OntologyNode(" Please use the four buttons on the right! "));
        treeModel = new DefaultTreeModel(rootNode);

        tree.setModel(treeModel);

        //rootNode.setUserObject(new OntologyNode(simplifyURI("http://www.uk-erlangen.de/MDR-System#MDR-Dataelement")));
        //OntologyNode ontoNode = new OntologyNode("Test"); // this object stores als the MDR entries
        //DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(ontoNode);
        //rootNode.add(newNode);

        System.out.println("Setting up Jena model with inferencing level 'none' ...");
        ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

        loadButton.setEnabled(true);
        saveButton.setEnabled(true);
        newButton.setEnabled(true);
        fileButton.setEnabled(true);
        fileName.setText("No file selected!");

        BufferedInputStream stream = null;
        Properties properties = new Properties();
        try {
            stream = new BufferedInputStream(new FileInputStream("OntoEdit.properties"));
            properties.load(stream);
            stream.close();
            inputFile = properties.getProperty("inputFile");

            if (inputFile.length() > 36) {
                fileName.setText("... " + inputFile.substring(inputFile.length() - 36));
            } else {
                fileName.setText(inputFile);
            }
            fileName.setToolTipText(inputFile);

        } catch (IOException ex) {
            Logger.getLogger(OntoEditForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        newProjectWindow = new javax.swing.JFrame();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        namespaceInput = new javax.swing.JTextField();
        contextInput = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        descriptionInput = new javax.swing.JTextArea();
        createProject = new javax.swing.JButton();
        leftPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        newNode = new javax.swing.JButton();
        deleteNode = new javax.swing.JButton();
        duplicateNode = new javax.swing.JButton();
        renameNode = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        doNotExport = new javax.swing.JCheckBox();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jumpTo = new javax.swing.JCheckBox();
        rightPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        hasContext = new javax.swing.JComboBox();
        classNameLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        hasDataType = new javax.swing.JComboBox();
        hasFlagsToUse = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        hasLowOfLowValue = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        hasHighOfLowValue = new javax.swing.JTextField();
        hasLowOfHighValue = new javax.swing.JTextField();
        hasHighOfHighValue = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        hasDescription = new javax.swing.JTextPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        hasNiceName = new javax.swing.JTextPane();
        hasUnits = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        fileButton = new javax.swing.JButton();
        loadButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        hasConceptCodePrefix = new javax.swing.JTextField();
        hasConceptCodeSuffix = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        hasLowOfToxicValue = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        hasHighOfToxicValue = new javax.swing.JTextField();
        newButton = new javax.swing.JButton();
        fileName = new javax.swing.JLabel();
        assignContextToSubconceptsButton = new javax.swing.JButton();
        AutoPrefixButton = new javax.swing.JButton();
        AutoSuffixButton = new javax.swing.JButton();
        thisConcept = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        parentalConcepts = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        charsPerWord = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        AutoDatatypeButton = new javax.swing.JButton();
        autoFollow = new javax.swing.JCheckBox();
        recursive = new javax.swing.JCheckBox();
        jButton7 = new javax.swing.JButton();
        AutoSuffixButton1 = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        NthWord1 = new javax.swing.JTextField();
        replaceNodeNames = new javax.swing.JCheckBox();
        jSeparator2 = new javax.swing.JSeparator();

        newProjectWindow.setTitle("New Project");
        newProjectWindow.setAlwaysOnTop(true);
        newProjectWindow.setLocationByPlatform(true);
        newProjectWindow.setMinimumSize(new java.awt.Dimension(565, 218));
        newProjectWindow.setResizable(false);

        jLabel18.setText("Project namespace:");

        jLabel19.setText("Context element:");

        jLabel20.setText("Context description:");

        namespaceInput.setText("http://www.uk-erlangen.de/Test/MyProject#");

        contextInput.setText("MyContext-Element");
        contextInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contextInputActionPerformed(evt);
            }
        });

        descriptionInput.setColumns(20);
        descriptionInput.setLineWrap(true);
        descriptionInput.setRows(5);
        descriptionInput.setText("All elements in this context have documented as a simple example.");
        descriptionInput.setEnabled(false);
        jScrollPane4.setViewportView(descriptionInput);

        createProject.setText("Create");
        createProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createProjectActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout newProjectWindowLayout = new org.jdesktop.layout.GroupLayout(newProjectWindow.getContentPane());
        newProjectWindow.getContentPane().setLayout(newProjectWindowLayout);
        newProjectWindowLayout.setHorizontalGroup(
            newProjectWindowLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(newProjectWindowLayout.createSequentialGroup()
                .addContainerGap()
                .add(newProjectWindowLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(newProjectWindowLayout.createSequentialGroup()
                        .add(newProjectWindowLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel20)
                            .add(jLabel18)
                            .add(jLabel19))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(newProjectWindowLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, namespaceInput, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, contextInput, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, createProject))
                .addContainerGap())
        );
        newProjectWindowLayout.setVerticalGroup(
            newProjectWindowLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(newProjectWindowLayout.createSequentialGroup()
                .addContainerGap()
                .add(newProjectWindowLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel18)
                    .add(namespaceInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(newProjectWindowLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel19)
                    .add(contextInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(newProjectWindowLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel20)
                    .add(jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE))
                .add(18, 18, 18)
                .add(createProject)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("OntoEdit");
        setLocationByPlatform(true);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.X_AXIS));

        newNode.setText("Add");
        newNode.setToolTipText("Add new node");
        newNode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newNodeActionPerformed(evt);
            }
        });

        deleteNode.setText("Del");
        deleteNode.setToolTipText("delete selected node");
        deleteNode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteNodeActionPerformed(evt);
            }
        });

        duplicateNode.setText("Dup");
        duplicateNode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                duplicateNodeActionPerformed(evt);
            }
        });

        renameNode.setText("Ren");
        renameNode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renameNodeActionPerformed(evt);
            }
        });

        doNotExport.setText("Do not export this concept to i2b2");
        doNotExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doNotExportActionPerformed(evt);
            }
        });

        jButton5.setText("Move to ...");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Remember");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jumpTo.setSelected(true);
        jumpTo.setText("Jump to modifications");

        org.jdesktop.layout.GroupLayout leftPanelLayout = new org.jdesktop.layout.GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(leftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(leftPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1)
                    .add(leftPanelLayout.createSequentialGroup()
                        .add(jButton5)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton6)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jumpTo))
                    .add(leftPanelLayout.createSequentialGroup()
                        .add(newNode)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(deleteNode)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(duplicateNode)
                        .add(leftPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(leftPanelLayout.createSequentialGroup()
                                .add(49, 49, 49)
                                .add(jLabel22))
                            .add(leftPanelLayout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(renameNode)
                                .add(6, 6, 6)
                                .add(doNotExport)))))
                .addContainerGap())
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, leftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(leftPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(newNode)
                    .add(jLabel22)
                    .add(deleteNode)
                    .add(duplicateNode)
                    .add(renameNode)
                    .add(doNotExport))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(leftPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton5)
                    .add(jButton6)
                    .add(jumpTo))
                .addContainerGap())
        );

        getContentPane().add(leftPanel);

        rightPanel.setMinimumSize(new java.awt.Dimension(500, 0));
        rightPanel.setPreferredSize(new java.awt.Dimension(500, 541));

        jLabel1.setText("Description:");

        jLabel2.setText("Nice name:");

        jLabel3.setText("Context:");

        hasContext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hasContextActionPerformed(evt);
            }
        });

        classNameLabel.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        classNameLabel.setText("No Concept selected!");
        classNameLabel.setPreferredSize(new java.awt.Dimension(400, 22));
        classNameLabel.setRequestFocusEnabled(false);

        jLabel5.setText("Data type:");

        jLabel6.setText("Flag type:");

        hasDataType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hasDataTypeActionPerformed(evt);
            }
        });

        hasFlagsToUse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hasFlagsToUseActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));

        jLabel7.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jLabel7.setText("very low");

        hasLowOfLowValue.setMaximumSize(new java.awt.Dimension(25, 0));
        hasLowOfLowValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hasLowOfLowValueActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jLabel8.setText("low");

        jLabel9.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jLabel9.setText("medium");

        jLabel10.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jLabel10.setText("very high");

        jLabel11.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jLabel11.setText("high");

        hasHighOfLowValue.setMaximumSize(new java.awt.Dimension(25, 0));
        hasHighOfLowValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hasHighOfLowValueActionPerformed(evt);
            }
        });

        hasLowOfHighValue.setMaximumSize(new java.awt.Dimension(25, 0));
        hasLowOfHighValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hasLowOfHighValueActionPerformed(evt);
            }
        });

        hasHighOfHighValue.setMaximumSize(new java.awt.Dimension(25, 0));
        hasHighOfHighValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hasHighOfHighValueActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(4, 4, 4)
                .add(jLabel7)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(hasLowOfLowValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel8)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(hasHighOfLowValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel9)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(hasLowOfHighValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel11)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(hasHighOfHighValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel10)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jLabel7)
                .add(hasLowOfLowValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jLabel8)
                .add(hasHighOfLowValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jLabel9)
                .add(hasLowOfHighValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jLabel11)
                .add(hasHighOfHighValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jLabel10))
        );

        jLabel13.setText("Units:");

        jScrollPane2.setViewportView(hasDescription);

        jScrollPane3.setViewportView(hasNiceName);

        hasUnits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hasUnitsActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Arial", 3, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setText("OntoEdit");

        fileButton.setText("File");
        fileButton.setToolTipText("select the default file");
        fileButton.setEnabled(false);
        fileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileButtonActionPerformed(evt);
            }
        });

        loadButton.setText("Load");
        loadButton.setToolTipText("load the selected file");
        loadButton.setEnabled(false);
        loadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadButtonActionPerformed(evt);
            }
        });

        saveButton.setText("Save");
        saveButton.setToolTipText("save everything to the selected file");
        saveButton.setEnabled(false);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        jLabel14.setText("Concept Code Prefix:");

        jLabel16.setText("Toxic range:");

        hasLowOfToxicValue.setMaximumSize(new java.awt.Dimension(25, 0));
        hasLowOfToxicValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hasLowOfToxicValueActionPerformed(evt);
            }
        });

        jLabel17.setText("-");

        hasHighOfToxicValue.setMaximumSize(new java.awt.Dimension(25, 0));
        hasHighOfToxicValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hasHighOfToxicValueActionPerformed(evt);
            }
        });

        newButton.setText("New");
        newButton.setToolTipText("create a new project");
        newButton.setEnabled(false);
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });

        fileName.setText("Setting up Jena model ... please wait!");
        fileName.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        assignContextToSubconceptsButton.setText("Assign to all subconcepts");
        assignContextToSubconceptsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                assignContextToSubconceptsButtonActionPerformed(evt);
            }
        });

        AutoPrefixButton.setText("Assign to all subconcepts");
        AutoPrefixButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AutoPrefixButtonActionPerformed(evt);
            }
        });

        AutoSuffixButton.setText("Auto create for all subconcepts");
        AutoSuffixButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AutoSuffixButtonActionPerformed(evt);
            }
        });

        thisConcept.setText("This Concept");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "isValueOf", "isAttributeOf", "isComponentOf", "isComposedOf", "isElementOf", "isCollectionOf", "isSubsetOf", "isSupersetOf", "describesExistenceOf", "describesNonExistenceOf" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        parentalConcepts.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel15.setText("Concept Code Suffix:");

        charsPerWord.setText("4");

        jLabel21.setText("characters/word");

        jLabel23.setText("Length:");

        jButton1.setText("Capitalize");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Remove colons");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/form2.png"))); // NOI18N
        jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/form.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        AutoDatatypeButton.setText("Assign to all subconcepts");
        AutoDatatypeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AutoDatatypeButtonActionPerformed(evt);
            }
        });

        autoFollow.setText("Auto follow");

        recursive.setText("recursive");

        jButton7.setText("Enhance");
        jButton7.setEnabled(false);

        AutoSuffixButton1.setText("Use Nth term for all subconcepts");
        AutoSuffixButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AutoSuffixButton1ActionPerformed(evt);
            }
        });

        jLabel24.setText("N=");

        NthWord1.setText("0");
        NthWord1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NthWord1ActionPerformed(evt);
            }
        });

        replaceNodeNames.setText("Replace node names with nice names during load");
        replaceNodeNames.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                replaceNodeNamesActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout rightPanelLayout = new org.jdesktop.layout.GroupLayout(rightPanel);
        rightPanel.setLayout(rightPanelLayout);
        rightPanelLayout.setHorizontalGroup(
            rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(rightPanelLayout.createSequentialGroup()
                .add(rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jSeparator2)
                    .add(rightPanelLayout.createSequentialGroup()
                        .add(rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(replaceNodeNames)
                            .add(rightPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .add(rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jLabel3)
                                    .add(jLabel1)
                                    .add(rightPanelLayout.createSequentialGroup()
                                        .add(jLabel2)
                                        .add(73, 73, 73)
                                        .add(rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                            .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane2)
                                            .add(org.jdesktop.layout.GroupLayout.LEADING, rightPanelLayout.createSequentialGroup()
                                                .add(jButton1)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(jButton2)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(recursive)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .add(jButton7))
                                            .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane3)
                                            .add(org.jdesktop.layout.GroupLayout.LEADING, rightPanelLayout.createSequentialGroup()
                                                .add(hasContext, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 189, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(assignContextToSubconceptsButton))))
                                    .add(classNameLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 435, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(rightPanelLayout.createSequentialGroup()
                                        .add(rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(rightPanelLayout.createSequentialGroup()
                                                .add(126, 126, 126)
                                                .add(hasDataType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 161, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                            .add(jLabel5))
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(AutoDatatypeButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 153, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                    .add(jLabel6)
                                    .add(jLabel15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 108, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(rightPanelLayout.createSequentialGroup()
                                        .add(rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                            .add(rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                .add(jLabel14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 108, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .add(thisConcept))
                                            .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel13)
                                            .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel16))
                                        .add(18, 18, 18)
                                        .add(rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(org.jdesktop.layout.GroupLayout.TRAILING, rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                                .add(jPanel1, 0, 0, Short.MAX_VALUE)
                                                .add(rightPanelLayout.createSequentialGroup()
                                                    .add(rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                                        .add(AutoSuffixButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 161, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                        .add(hasUnits, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 161, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                        .add(hasConceptCodeSuffix, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 161, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                        .add(hasConceptCodePrefix, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 161, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                        .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 161, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                        .add(hasFlagsToUse, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 161, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                        .add(AutoSuffixButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 161, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                    .add(rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                        .add(parentalConcepts, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 183, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                        .add(AutoPrefixButton)
                                                        .add(rightPanelLayout.createSequentialGroup()
                                                            .add(rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                                                .add(rightPanelLayout.createSequentialGroup()
                                                                    .add(jLabel24)
                                                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                                    .add(NthWord1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                                .add(rightPanelLayout.createSequentialGroup()
                                                                    .add(jLabel23)
                                                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                                    .add(charsPerWord, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                                                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                            .add(jLabel21)))))
                                            .add(rightPanelLayout.createSequentialGroup()
                                                .add(hasLowOfToxicValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 52, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(jLabel17)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(hasHighOfToxicValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 52, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                                    .add(rightPanelLayout.createSequentialGroup()
                                        .add(jButton4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 47, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jButton3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 47, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(18, 18, 18)
                                        .add(autoFollow))
                                    .add(fileName))))
                        .add(0, 4, Short.MAX_VALUE))
                    .add(rightPanelLayout.createSequentialGroup()
                        .add(newButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(fileButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(loadButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(saveButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 165, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        rightPanelLayout.setVerticalGroup(
            rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(rightPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(rightPanelLayout.createSequentialGroup()
                        .add(rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(newButton)
                            .add(fileButton)
                            .add(loadButton)
                            .add(saveButton))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(fileName)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(replaceNodeNames))
                    .add(jLabel4))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jSeparator2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(rightPanelLayout.createSequentialGroup()
                        .add(jLabel13)
                        .add(26, 26, 26)
                        .add(rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(hasLowOfToxicValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel17)
                            .add(hasHighOfToxicValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel16)))
                    .add(rightPanelLayout.createSequentialGroup()
                        .add(classNameLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel3)
                            .add(hasContext, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(assignContextToSubconceptsButton))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(rightPanelLayout.createSequentialGroup()
                                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                    .add(jButton1)
                                    .add(jButton2)
                                    .add(recursive)
                                    .add(jButton7)))
                            .add(jLabel2))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel1)
                            .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(thisConcept)
                            .add(parentalConcepts, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(AutoPrefixButton)
                            .add(hasConceptCodePrefix, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel14))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel15)
                            .add(hasConceptCodeSuffix, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(charsPerWord, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel21)
                            .add(jLabel23)
                            .add(AutoSuffixButton))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(AutoSuffixButton1)
                            .add(NthWord1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel24))
                        .add(13, 13, 13)
                        .add(rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel5)
                            .add(AutoDatatypeButton)
                            .add(hasDataType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel6)
                            .add(hasFlagsToUse, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(hasUnits, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jButton3)
                        .add(jButton4))
                    .add(autoFollow))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        getContentPane().add(rightPanel);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-949)/2, (screenSize.height-701)/2, 949, 701);
    }// </editor-fold>//GEN-END:initComponents

    private void saveFormInput() {

        DefaultMutableTreeNode node = lastSelectedNode;
        if (node == null) {
            return;
        }
        OntologyNode nodeInfo = (OntologyNode) node.getUserObject();

        String niceName = hasNiceName.getText().trim().replaceAll("\n", "");
        nodeInfo.setNiceName(niceName);

        nodeInfo.setDescription(hasDescription.getText());
        nodeInfo.setConceptCodeSuffix(hasConceptCodeSuffix.getText());
        nodeInfo.setConceptCodePrefix(hasConceptCodePrefix.getText());
        nodeInfo.setLowOfLowValue(hasLowOfLowValue.getText());
        nodeInfo.setHighOfLowValue(hasHighOfLowValue.getText());
        nodeInfo.setLowOfHighValue(hasLowOfHighValue.getText());
        nodeInfo.setHighOfHighValue(hasHighOfHighValue.getText());
        nodeInfo.setLowOfToxicValue(hasLowOfToxicValue.getText());
        nodeInfo.setHighOfToxicValue(hasHighOfToxicValue.getText());
        nodeInfo.setDataType(hasDataType.getSelectedItem().toString());
        nodeInfo.setFlagsToUse(hasFlagsToUse.getSelectedItem().toString());
        nodeInfo.setUnits(hasUnits.getSelectedItem().toString());
        nodeInfo.setContext(hasContext.getSelectedItem().toString());
        nodeInfo.setDoNotExport(doNotExport.isSelected());
    }

    private void hasLowOfToxicValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hasLowOfToxicValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hasLowOfToxicValueActionPerformed

     private void hasLowOfLowValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hasLowOfLowValueActionPerformed
         // TODO add your handling code here:
    }//GEN-LAST:event_hasLowOfLowValueActionPerformed

    private void hasHighOfLowValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hasHighOfLowValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hasHighOfLowValueActionPerformed

    private void hasLowOfHighValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hasLowOfHighValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hasLowOfHighValueActionPerformed

    private void hasHighOfHighValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hasHighOfHighValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hasHighOfHighValueActionPerformed

    private void hasDataTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hasDataTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hasDataTypeActionPerformed

    private String getOWLName(String element) {
        if (element == null) {
            return "";
        }
        String returnString = element.trim();
        returnString = returnString.replaceAll("\\)", "_");
        returnString = returnString.replaceAll("\\(", "_");
        returnString = returnString.replaceAll("\\]", "_");
        returnString = returnString.replaceAll("\\[", "_");
        returnString = returnString.replaceAll("\\/", "_");
        returnString = returnString.replaceAll(" ", "_");
        returnString = returnString.replaceAll("Ä", "Ae");
        returnString = returnString.replaceAll("Ö", "Oe");
        returnString = returnString.replaceAll("Ü", "Ue");
        returnString = returnString.replaceAll("ä", "ae");
        returnString = returnString.replaceAll("ö", "oe");
        returnString = returnString.replaceAll("ü", "ue");
        returnString = returnString.replaceAll("ß", "ss");

        returnString = returnString.replaceAll("[^\\p{L}\\p{N}-]", "_");

        return returnString;
    }

    /**
     *
     * @param nodeStr
     * @param treeBrowser
     * @return
     */
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

    private void newNodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newNodeActionPerformed
        saveFormInput();
        treeModel = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }
        String returnString = JOptionPane.showInputDialog("Please enter one or more concepts (uses commas to saparate): ");

        String split[] = returnString.split(",");

        for (int i = 0; i < split.length; i++) {

            String returnString2 = getOWLName(split[i]);
            String returnString3 = returnString2;

            int uniqueCnt = 0;
            while (searchNode(returnString3, tree) != null) {
                uniqueCnt++;
                System.out.println("Node already exists!");
                returnString3 = returnString2 + "_" + uniqueCnt;
            }

            OntologyNode newOntoNode = new OntologyNode(returnString3);

            newOntoNode.setOWLName(returnString3);

            newOntoNode.setNiceName(getValidOWLString(split[i]));
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newOntoNode);
            treeModel.insertNodeInto(newNode, node, node.getChildCount());
            tree.setModel(treeModel);

            lastInterestingNode = returnString3;

            TreeNode[] nodes = treeModel.getPathToRoot(node);
            if (jumpTo.isSelected()) {
                nodes = treeModel.getPathToRoot(newNode);
            }
            TreePath path = new TreePath(nodes);
            tree.scrollPathToVisible(path);
            tree.setSelectionPath(path);
        }

    }//GEN-LAST:event_newNodeActionPerformed

    private void deleteNodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteNodeActionPerformed


        treeModel = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        DefaultMutableTreeNode sourceNode2 = node.getNextSibling();

        if (node == null) {
            return;
        }
        treeModel.removeNodeFromParent(node);
        tree.setModel(treeModel);
        TreeNode[] nodes = treeModel.getPathToRoot(sourceNode2);
        TreePath path = new TreePath(nodes);
        tree.scrollPathToVisible(path);
        tree.setSelectionPath(path);

    }//GEN-LAST:event_deleteNodeActionPerformed

    private void duplicateNodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_duplicateNodeActionPerformed
        saveFormInput();

        treeModel = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }
        OntologyNode nodeInfo = (OntologyNode) node.getUserObject();
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new OntologyNode(nodeInfo.toString()));
        newNode.setUserObject(node.getUserObject());
        treeModel.insertNodeInto(newNode, (DefaultMutableTreeNode) node.getParent(), 0);
        tree.setModel(treeModel);
    }//GEN-LAST:event_duplicateNodeActionPerformed

    private void renameNodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_renameNodeActionPerformed
        saveFormInput();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }
        OntologyNode nodeInfo = (OntologyNode) node.getUserObject();
        String returnString = JOptionPane.showInputDialog("Please enter a new name: ", nodeInfo);
        returnString = getOWLName(returnString);
        String returnString2 = returnString;
        int uniqueCnt = 0;
        while (searchNode(returnString2, tree) != null) {
            uniqueCnt++;
            System.out.println("Node already exists!");
            returnString2 = returnString + "_" + uniqueCnt;
        }
        nodeInfo.setNodeName(returnString2);
        node.setUserObject(nodeInfo);
        tree.repaint();
    }//GEN-LAST:event_renameNodeActionPerformed

    private void hasHighOfToxicValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hasHighOfToxicValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hasHighOfToxicValueActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed

        saveFormInput();

        System.out.println("Exporting Ontology ...");
        treeModel = (DefaultTreeModel) tree.getModel();
        Object root = treeModel.getRoot();

        OntModel mdrSystem;
        mdrSystem = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

        //create an input stream for the input file
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(System.getProperty("user.dir") + File.separatorChar + "MDR-System.owl");
        } catch (FileNotFoundException e) {
            System.err.println("'" + inputFile + "' is an invalid input file.");
            e.printStackTrace();
        }
        mdrSystem.read(inputStream, "");

        outModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        outModel.addSubModel(mdrSystem);
        outModel.setNsPrefix("", NsPrefixURI);

        int count = treeModel.getChildCount(root);
        for (int i = 0; i < count; i++) {
            getTreeChildren(treeModel.getChild(root, i), "http://www.uk-erlangen.de/MDR-System#MDR-Dataelement");
        }

        OntClass thisClass1 = ontModel.getOntClass("http://www.uk-erlangen.de/MDR-System#MDR-Context");
        OntClass thisClass2 = outModel.createClass("http://www.uk-erlangen.de/MDR-System#MDR-Context");

        for (Iterator i = thisClass1.listInstances(); i.hasNext();) {
            Individual myIndividual = (Individual) i.next();
            thisClass2.createIndividual(myIndividual.toString());
        }

        FileOutputStream outFile;
        try {
            outFile = new FileOutputStream(inputFile);
            outModel.write(outFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OntoEditForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        JOptionPane.showMessageDialog(null, "Ontology saved!", "Ontology saved!", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_saveButtonActionPerformed

    private void loadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadButtonActionPerformed

        tree.setShowsRootHandles(true);
        rootNode = new DefaultMutableTreeNode(new OntologyNode(simplifyURI("http://www.uk-erlangen.de/MDR-System#MDR-Dataelement")));
        tree.setModel(new DefaultTreeModel(rootNode));

        //create an input stream for the input file
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(inputFile);
        } catch (FileNotFoundException e) {
        }
        ontModel.removeAll();

        // Load the facts into the ontModel:
        ontModel.read(inputStream, null);

        // Validate the file:
        System.out.println("Validating OWL file ...");
        ValidityReport validityReport = ontModel.validate();
        if (validityReport
                != null && !validityReport.isValid()) {
            Iterator i = validityReport.getReports();
            while (i.hasNext()) {
                System.err.println(
                        ((ValidityReport.Report) i.next()).getDescription());
            }
        }

        System.out.println("getNsPrefixURI: " + ontModel.getNsPrefixURI(""));
        NsPrefixURI = ontModel.getNsPrefixURI("");

        if (NsPrefixURI == null) {
            NsPrefixURI = "";
        }

        OntModel mdrSystem;
        mdrSystem = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

        //create an input stream for the input file
        inputStream = null;
        try {
            inputStream = new FileInputStream(System.getProperty("user.dir") + File.separatorChar + "MDR-System.owl");
        } catch (FileNotFoundException e) {
            System.err.println("'" + inputFile + "' is an invalid input file.");
        }

        mdrSystem.read(inputStream, "");
        ontModel.addSubModel(mdrSystem);

        loadMDRSystemStuff();

        String leftStartClass = "http://www.uk-erlangen.de/MDR-System#MDR-Dataelement";
        createOntologyBrowser(ontModel, leftStartClass, rootNode, tree);
    }//GEN-LAST:event_loadButtonActionPerformed

    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed

        //Center the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = newProjectWindow.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        newProjectWindow.setLocation((screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2);
        newProjectWindow.setVisible(true);
        newProjectWindow.show();

    }//GEN-LAST:event_newButtonActionPerformed

    private void contextInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contextInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_contextInputActionPerformed

    private void createProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createProjectActionPerformed

        inputFile = "NewProject.owl";
        if (inputFile.length() > 36) {
            fileName.setText("... " + inputFile.substring(inputFile.length() - 36));
        } else {
            fileName.setText(inputFile);
        }
        fileName.setToolTipText(inputFile);


        try {
            System.out.println("saveConfiguration called!");
            Properties properties = new Properties();
            properties.setProperty("inputFile", inputFile);

            File propertiesFileFile = new File("OntoEdit.properties");
            properties.store(new FileOutputStream(propertiesFileFile), "OntoEdit Properties File");
        } catch (IOException ex) {
            Logger.getLogger(OntoEditForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        rootNode = new DefaultMutableTreeNode(new OntologyNode(simplifyURI("http://www.uk-erlangen.de/MDR-System#MDR-Dataelement")));
        tree.setModel(new DefaultTreeModel(rootNode));

        tree.setShowsRootHandles(true);

        rootNode.setUserObject(new OntologyNode(simplifyURI("http://www.uk-erlangen.de/MDR-System#MDR-Dataelement")));

        OntologyNode ontoNode1 = new OntologyNode("New_Entry_1");
        OntologyNode ontoNode2 = new OntologyNode("New_Entry_2");
        OntologyNode ontoNode3 = new OntologyNode("New_Entry_3");

        DefaultMutableTreeNode newNode1 = new DefaultMutableTreeNode(ontoNode1);
        DefaultMutableTreeNode newNode2 = new DefaultMutableTreeNode(ontoNode2);
        DefaultMutableTreeNode newNode3 = new DefaultMutableTreeNode(ontoNode3);

        rootNode.add(newNode1);
        rootNode.add(newNode2);
        rootNode.add(newNode3);


        OntModel mdrSystem;
        mdrSystem = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

        //create an input stream for the input file
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(System.getProperty("user.dir") + File.separatorChar + "MDR-System.owl");
        } catch (FileNotFoundException e) {
            System.err.println("'" + inputFile + "' is an invalid input file.");

        }
        mdrSystem.read(inputStream, "");


        ontModel.addSubModel(mdrSystem);


        NsPrefixURI = namespaceInput.getText();
        System.out.println(NsPrefixURI);

        OntClass c = ontModel.getOntClass("http://www.uk-erlangen.de/MDR-System#MDR-Context");

        if (c != null) {
            c.createIndividual(NsPrefixURI + contextInput.getText());
        }

        loadMDRSystemStuff();
        newProjectWindow.hide();

    }//GEN-LAST:event_createProjectActionPerformed

    private void hasFlagsToUseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hasFlagsToUseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hasFlagsToUseActionPerformed

    private void hasUnitsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hasUnitsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hasUnitsActionPerformed

    private void fileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileButtonActionPerformed

        JFileChooser chooser = new JFileChooser();
        ExampleFileFilter filter = new ExampleFileFilter();
        filter.addExtension("owl");
        filter.setDescription("OWL Ontologies");

        System.setProperty("apple.awt.fileDialogForDirectories", "true");

        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            inputFile = chooser.getSelectedFile().getAbsolutePath();
            if (inputFile.length() > 36) {
                fileName.setText("... " + inputFile.substring(inputFile.length() - 36));
            } else {
                fileName.setText(inputFile);
            }
            fileName.setToolTipText(inputFile);

        } else {
            return;
        }

        try {
            System.out.println("saveConfiguration called!");
            Properties properties = new Properties();
            properties.setProperty("inputFile", inputFile);

            File propertiesFileFile = new File("OntoEdit.properties");
            properties.store(new FileOutputStream(propertiesFileFile), "OntoEdit Properties File");
        } catch (IOException ex) {
            Logger.getLogger(OntoEditForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_fileButtonActionPerformed

    private void hasContextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hasContextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hasContextActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void assignContextToSubconcepts(DefaultMutableTreeNode node, String context) {
        OntologyNode nodeInfo = (OntologyNode) node.getUserObject();
        nodeInfo.setContext(context);
        node.setUserObject(nodeInfo);
        System.out.println(node);
        int count = treeModel.getChildCount(node);
        for (int i = 0; i < count; i++) {
            assignContextToSubconcepts((DefaultMutableTreeNode) treeModel.getChild(node, i), context);
        }
    }

    private void assignContextToSubconceptsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_assignContextToSubconceptsButtonActionPerformed
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }
        assignContextToSubconcepts(node, hasContext.getSelectedItem().toString());
    }//GEN-LAST:event_assignContextToSubconceptsButtonActionPerformed

    private void assignPrefixToSubconcepts(DefaultMutableTreeNode node, String prefix) {

        OntologyNode nodeInfo = (OntologyNode) node.getUserObject();
        nodeInfo.setConceptCodePrefix(prefix);
        node.setUserObject(nodeInfo);
        System.out.println(node);
        int count = treeModel.getChildCount(node);
        for (int i = 0; i < count; i++) {
            assignPrefixToSubconcepts((DefaultMutableTreeNode) treeModel.getChild(node, i), prefix);
        }
    }

    private void AutoPrefixButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AutoPrefixButtonActionPerformed
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }
        assignPrefixToSubconcepts(node, hasConceptCodePrefix.getText());
    }//GEN-LAST:event_AutoPrefixButtonActionPerformed

    private int assignSuffixToSubconcepts(DefaultMutableTreeNode node, int cnt) {
        OntologyNode nodeInfo = (OntologyNode) node.getUserObject();


        String nodeName = node.toString();

        //System.out.println("Creating concept code suffix for '" + nodeName + "' ...");

        nodeName = nodeName.replaceAll("[^a-zA-Z0-9]", " ");

        String[] splitWords = nodeName.split(" ");

        String suffix = "";
        int splitSize = Integer.valueOf(charsPerWord.getText());

        for (int i = 0; i < splitWords.length; i++) {
            //System.out.println(splitWords[i]);

            String firstLetter = "";
            String remainder = "";

            if (splitWords[i].length() > 1) {
                firstLetter = splitWords[i].substring(0, 1).toUpperCase();
                remainder = splitWords[i].substring(1).toLowerCase();
                if (remainder.length() > splitSize - 1) {
                    remainder = remainder.substring(0, splitSize - 1);
                }
                suffix = suffix + firstLetter + remainder;
            } else if (splitWords[i].length() == 1) {
                suffix = suffix + splitWords[i].substring(0, 1).toUpperCase();
            }
        }

        cnt++;
        nodeInfo.setConceptCodeSuffix(cnt + "-" + suffix);
        node.setUserObject(nodeInfo);
        System.out.println(node);
        int count = treeModel.getChildCount(node);
        for (int i = 0; i < count; i++) {
            cnt = assignSuffixToSubconcepts((DefaultMutableTreeNode) treeModel.getChild(node, i), cnt);
        }
        return cnt;
    }

    private int assignTermToSubconcepts(DefaultMutableTreeNode node, int cnt) {
        OntologyNode nodeInfo = (OntologyNode) node.getUserObject();


        String nodeName = node.toString();

        //System.out.println("Creating concept code suffix for '" + nodeName + "' ...");

        nodeName = nodeName.replaceAll("[^a-zA-Z0-9-]", " ");

        String[] splitWords = nodeName.split(" ");

        String suffix = "";
        int splitSize = Integer.valueOf(charsPerWord.getText());


        Integer t = Integer.parseInt(NthWord1.getText());

        suffix = splitWords[t];

        cnt++;
        nodeInfo.setConceptCodeSuffix(suffix);
        node.setUserObject(nodeInfo);
        System.out.println(node);
        int count = treeModel.getChildCount(node);
        for (int i = 0; i < count; i++) {
            cnt = assignTermToSubconcepts((DefaultMutableTreeNode) treeModel.getChild(node, i), cnt);
        }
        return cnt;
    }

    private void AutoSuffixButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AutoSuffixButtonActionPerformed

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }
        assignSuffixToSubconcepts(node, 0);

    }//GEN-LAST:event_AutoSuffixButtonActionPerformed

    private void capitalizeSubconcepts(DefaultMutableTreeNode node) {
        OntologyNode nodeInfo = (OntologyNode) node.getUserObject();
        nodeInfo.setNiceName(nodeInfo.getNiceName().substring(0, 1).toUpperCase() + nodeInfo.getNiceName().substring(1));
        node.setUserObject(nodeInfo);
        System.out.println(node);
        int count = treeModel.getChildCount(node);
        for (int i = 0; i < count; i++) {
            capitalizeSubconcepts((DefaultMutableTreeNode) treeModel.getChild(node, i));
        }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (recursive.isSelected()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (node == null) {
                return;
            }
            capitalizeSubconcepts(node);
        }
        hasNiceName.setText(hasNiceName.getText().substring(0, 1).toUpperCase() + hasNiceName.getText().substring(1));
        saveFormInput();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void remColonsSubconcepts(DefaultMutableTreeNode node) {
        OntologyNode nodeInfo = (OntologyNode) node.getUserObject();
        nodeInfo.setNiceName(nodeInfo.getNiceName().replaceAll(":", ""));
        node.setUserObject(nodeInfo);
        System.out.println(node);
        int count = treeModel.getChildCount(node);
        for (int i = 0; i < count; i++) {
            remColonsSubconcepts((DefaultMutableTreeNode) treeModel.getChild(node, i));
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (recursive.isSelected()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (node == null) {
                return;
            }
            remColonsSubconcepts(node);
        }
        hasNiceName.setText(hasNiceName.getText().replaceAll(":", ""));
        saveFormInput();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void doNotExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doNotExportActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_doNotExportActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }

        String url = "http://localhost:8080/" + node + "#selected";
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch (IOException ex) {
            Logger.getLogger(OntoEditForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    }//GEN-LAST:event_jButton3ActionPerformed

    private void assignDataTypeToSubconcepts(DefaultMutableTreeNode node, String prefix) {

        OntologyNode nodeInfo = (OntologyNode) node.getUserObject();
        nodeInfo.setDataType(prefix);
        node.setUserObject(nodeInfo);
        System.out.println(node);
        int count = treeModel.getChildCount(node);
        for (int i = 0; i < count; i++) {
            assignDataTypeToSubconcepts((DefaultMutableTreeNode) treeModel.getChild(node, i), prefix);
        }
    }

    private void AutoDatatypeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AutoDatatypeButtonActionPerformed

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }
        assignDataTypeToSubconcepts(node, hasDataType.getSelectedItem().toString());

    }//GEN-LAST:event_AutoDatatypeButtonActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        saveFormInput();

        treeModel = (DefaultTreeModel) tree.getModel();
        String nodeInfo = lastInterestingNode;
        String returnString = JOptionPane.showInputDialog("Move to: ", nodeInfo);
        returnString = getOWLName(returnString);
        String returnString2 = returnString;

        System.out.println("returnString2: " + returnString);

        if (searchNode(returnString2, tree) == null) {
            JOptionPane.showMessageDialog(null, "Node not found");
        } else {

            DefaultMutableTreeNode sourceNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            DefaultMutableTreeNode sourceNode2 = sourceNode.getNextSibling();
            if (sourceNode2 == null) {
                sourceNode2 = (DefaultMutableTreeNode) sourceNode.getParent();
            }

            DefaultMutableTreeNode targetNode = searchNode(returnString2, tree);
            treeModel.removeNodeFromParent(sourceNode);
            treeModel.insertNodeInto(sourceNode, targetNode, targetNode.getChildCount());
            tree.setModel(treeModel);

            TreeNode[] nodes = treeModel.getPathToRoot(sourceNode2);
            if (jumpTo.isSelected()) {
                nodes = treeModel.getPathToRoot(sourceNode);
            }
            TreePath path = new TreePath(nodes);
            tree.scrollPathToVisible(path);
            tree.setSelectionPath(path);

            lastInterestingNode = returnString2;
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        treeModel = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        lastInterestingNode = node.toString();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void AutoSuffixButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AutoSuffixButton1ActionPerformed
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }
        assignTermToSubconcepts(node, 0);
    }//GEN-LAST:event_AutoSuffixButton1ActionPerformed

    private void NthWord1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NthWord1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NthWord1ActionPerformed

    private void replaceNodeNamesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_replaceNodeNamesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_replaceNodeNamesActionPerformed

    void getTreeChildren(Object node, String Parent) {

        //System.out.println("Exporting tree node: " + node);

        DefaultMutableTreeNode node2 = (DefaultMutableTreeNode) node;
        OntologyNode nodeInfo = (OntologyNode) node2.getUserObject();
        OntClass c1 = outModel.createClass(NsPrefixURI + nodeInfo.getOWLName());
        OntClass c2 = null;


        if (Parent.contains("http://")) {
            c2 = outModel.createClass(Parent);


        } else {
            c2 = outModel.createClass(NsPrefixURI + Parent);


        }

        outModel.add(outModel.createStatement(c1, RDFS.subClassOf, c2));


        String ns = "http://www.uk-erlangen.de/MDR-System#";

        // Datatype-Properties

        if (nodeInfo.getDoNotExport()) {
            outModel.add(outModel.createStatement(c1, RDFS.subClassOf, outModel.getOntClass(ns + "Unexported")));
        } else {
            outModel.add(outModel.createStatement(c1, RDFS.subClassOf, outModel.getOntClass(ns + "Exported")));
        }

        if (!nodeInfo.getNiceName().equals("")) {
            DatatypeProperty dtp = outModel.createDatatypeProperty(ns + "hasNiceName");
            outModel.add(outModel.createStatement(c1, dtp, nodeInfo.getNiceName()));
        }

        if (!nodeInfo.getConceptCodePrefix().equals("")) {
            DatatypeProperty dtp = outModel.createDatatypeProperty(ns + "hasConceptCodePrefix");
            outModel.add(outModel.createStatement(c1, dtp, nodeInfo.getConceptCodePrefix()));
        }

        if (!nodeInfo.getConceptCodeSuffix().equals("")) {
            DatatypeProperty dtp = outModel.createDatatypeProperty(ns + "hasConceptCodeSuffix");
            outModel.add(outModel.createStatement(c1, dtp, nodeInfo.getConceptCodeSuffix()));
        }

        if (!nodeInfo.getDescription().equals("")) {
            DatatypeProperty dtp = outModel.createDatatypeProperty(ns + "hasDescription");
            outModel.add(outModel.createStatement(c1, dtp, nodeInfo.getDescription()));


        }
        if (!nodeInfo.getFlagsToUse().equals("")) {
            DatatypeProperty dtp = outModel.createDatatypeProperty(ns + "hasFlagsToUse");
            outModel.add(outModel.createStatement(c1, dtp, nodeInfo.getFlagsToUse()));


        }
        if (!nodeInfo.getLowOfLowValue().equals("")) {
            DatatypeProperty dtp = outModel.createDatatypeProperty(ns + "hasLowOfLowValue");
            outModel.add(outModel.createStatement(c1, dtp, nodeInfo.getLowOfLowValue()));


        }
        if (!nodeInfo.getHighOfLowValue().equals("")) {
            DatatypeProperty dtp = outModel.createDatatypeProperty(ns + "hasHighOfLowValue");
            outModel.add(outModel.createStatement(c1, dtp, nodeInfo.getHighOfLowValue()));


        }
        if (!nodeInfo.getLowOfHighValue().equals("")) {
            DatatypeProperty dtp = outModel.createDatatypeProperty(ns + "hasLowOfHighValue");
            outModel.add(outModel.createStatement(c1, dtp, nodeInfo.getLowOfHighValue()));


        }
        if (!nodeInfo.getHighOfHighValue().equals("")) {
            DatatypeProperty dtp = outModel.createDatatypeProperty(ns + "hasHighOfHighValue");
            outModel.add(outModel.createStatement(c1, dtp, nodeInfo.getHighOfHighValue()));


        }
        if (!nodeInfo.getLowOfToxicValue().equals("")) {
            DatatypeProperty dtp = outModel.createDatatypeProperty(ns + "hasLowOfToxicValue");
            outModel.add(outModel.createStatement(c1, dtp, nodeInfo.getLowOfToxicValue()));


        }
        if (!nodeInfo.getHighOfToxicValue().equals("")) {
            DatatypeProperty dtp = outModel.createDatatypeProperty(ns + "hasHighOfToxicValue");
            outModel.add(outModel.createStatement(c1, dtp, nodeInfo.getHighOfToxicValue()));


        }

        // Object-Properties:

        if (!nodeInfo.getDataType().equals("")) {

            ObjectProperty op = outModel.createObjectProperty(ns + "hasDataType");

            System.out.println(c1);
            System.out.println(op);
            System.out.println(ns + nodeInfo.getDataType().toString());
            System.out.println(outModel.getIndividual(ns + nodeInfo.getDataType().toString()));
            Statement s = outModel.createStatement(c1, op, outModel.getIndividual(ns + nodeInfo.getDataType().toString()));

            outModel.add(s);


        }
        if (!nodeInfo.getUnits().equals("")) {
            ObjectProperty op = outModel.createObjectProperty(ns + "hasUnits");
            outModel.add(outModel.createStatement(c1, op, outModel.getIndividual(ns + nodeInfo.getUnits())));


        }
        if (!nodeInfo.getContext().equals("")) {
            ObjectProperty op = outModel.createObjectProperty(ns + "hasContext");
            System.out.println("exporting hasContext: " + NsPrefixURI + nodeInfo.getContext());
            outModel.add(outModel.createStatement(c1, op, outModel.getIndividual(NsPrefixURI + nodeInfo.getContext())));


        } // recurse into the tree:
        int count = treeModel.getChildCount(node);


        for (int i = 0; i
                < count; i++) {
            Parent = node.toString();
            getTreeChildren(
                    treeModel.getChild(node, i), Parent);


        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new OntoEditForm().setVisible(true);


            }
        });


    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AutoDatatypeButton;
    private javax.swing.JButton AutoPrefixButton;
    private javax.swing.JButton AutoSuffixButton;
    private javax.swing.JButton AutoSuffixButton1;
    private javax.swing.JTextField NthWord1;
    private javax.swing.JButton assignContextToSubconceptsButton;
    private javax.swing.JCheckBox autoFollow;
    private javax.swing.JTextField charsPerWord;
    private javax.swing.JLabel classNameLabel;
    private javax.swing.JTextField contextInput;
    private javax.swing.JButton createProject;
    private javax.swing.JButton deleteNode;
    private javax.swing.JTextArea descriptionInput;
    private javax.swing.JCheckBox doNotExport;
    private javax.swing.JButton duplicateNode;
    private javax.swing.JButton fileButton;
    private javax.swing.JLabel fileName;
    private javax.swing.JTextField hasConceptCodePrefix;
    private javax.swing.JTextField hasConceptCodeSuffix;
    private javax.swing.JComboBox hasContext;
    private javax.swing.JComboBox hasDataType;
    private javax.swing.JTextPane hasDescription;
    private javax.swing.JComboBox hasFlagsToUse;
    private javax.swing.JTextField hasHighOfHighValue;
    private javax.swing.JTextField hasHighOfLowValue;
    private javax.swing.JTextField hasHighOfToxicValue;
    private javax.swing.JTextField hasLowOfHighValue;
    private javax.swing.JTextField hasLowOfLowValue;
    private javax.swing.JTextField hasLowOfToxicValue;
    private javax.swing.JTextPane hasNiceName;
    private javax.swing.JComboBox hasUnits;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JCheckBox jumpTo;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JButton loadButton;
    private javax.swing.JTextField namespaceInput;
    private javax.swing.JButton newButton;
    private javax.swing.JButton newNode;
    private javax.swing.JFrame newProjectWindow;
    private javax.swing.JComboBox parentalConcepts;
    private javax.swing.JCheckBox recursive;
    private javax.swing.JButton renameNode;
    private javax.swing.JCheckBox replaceNodeNames;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JButton saveButton;
    private javax.swing.JLabel thisConcept;
    // End of variables declaration//GEN-END:variables

    private void getParentNodes(DefaultComboBoxModel model, DefaultMutableTreeNode node) {

        if (node.isRoot()) {
            return;


        }
        System.out.println(node.getParent());
        model.addElement(node.getParent());
        getParentNodes(
                model, (DefaultMutableTreeNode) node.getParent());



    }

    private void treeValueChanged(javax.swing.event.TreeSelectionEvent evt) {

        if (lastSelectedNode != null) {
            saveFormInput();


        }

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();


        if (node == null) {
            return;


        }

        DefaultComboBoxModel newParentalModel = new DefaultComboBoxModel();
        getParentNodes(
                newParentalModel, node);
        parentalConcepts.setModel(newParentalModel);
        OntologyNode nodeInfo = (OntologyNode) node.getUserObject();
        classNameLabel.setText(node.toString());
        hasNiceName.setText(nodeInfo.getNiceName());
        hasConceptCodePrefix.setText(nodeInfo.getConceptCodePrefix());
        hasConceptCodeSuffix.setText(nodeInfo.getConceptCodeSuffix());
        hasDescription.setText(nodeInfo.getDescription());
        hasDataType.setSelectedItem(nodeInfo.getDataType());
        hasFlagsToUse.setSelectedItem(nodeInfo.getFlagsToUse());
        hasLowOfLowValue.setText(nodeInfo.getLowOfLowValue());
        hasHighOfLowValue.setText(nodeInfo.getHighOfLowValue());
        hasLowOfHighValue.setText(nodeInfo.getLowOfHighValue());
        hasHighOfHighValue.setText(nodeInfo.getHighOfHighValue());
        hasLowOfToxicValue.setText(nodeInfo.getLowOfToxicValue());
        hasHighOfToxicValue.setText(nodeInfo.getHighOfToxicValue());
        hasUnits.setSelectedItem(nodeInfo.getUnits());
        hasContext.setSelectedItem(nodeInfo.getContext());
        doNotExport.setSelected(nodeInfo.getDoNotExport());
        lastSelectedNode = node;

        System.out.println("Node selected: " + nodeInfo.getNodeName());

        if (autoFollow.isSelected()) {
            jButton4ActionPerformed(null);
        }
    }

    private void createOntologyBrowser(OntModel model, String startClass, DefaultMutableTreeNode rootNode, JTree treeBrowser) {

        System.out.println("createOntologyBrowser() called for class " + startClass);

        rootNode.setUserObject(new OntologyNode(simplifyURI(startClass)));



        DefaultMutableTreeNode temp = new DefaultMutableTreeNode();
        OntClass artefact = model.getOntClass(startClass);
        List subClassesList = new ArrayList();


        for (Iterator i = artefact.listSubClasses(); i.hasNext();) {
            OntClass c = (OntClass) i.next();
            subClassesList.add(c.toString());


        }
        Collections.sort(subClassesList, Collator.getInstance());


        for (int i = 0; i
                < subClassesList.size(); i++) {
            getClassTree(model, model.getOntClass(subClassesList.get(i).toString()), rootNode);


        }

        treeBrowser.setModel(new DefaultTreeModel(rootNode));
        System.out.println("Ontology browser loaded!");


    }

    private void getClassTree(OntModel model, OntClass thisClass, DefaultMutableTreeNode node) {

        if (thisClass != null) {

            OntologyNode ontoNode = new OntologyNode(simplifyURI(thisClass.toString())); // this object stores als the MDR entries

            ontoNode.setOWLName(simplifyURI(thisClass.toString()));


            // find all properties related to this class:

            StmtIterator iProperties = thisClass.listProperties();


            while (iProperties.hasNext()) {
                Statement s = (Statement) iProperties.next();
                String predicateName = s.getPredicate().getLocalName();  // PREDICATE
                String objectName = s.getObject().toString();            // OBJECT

                predicateName = simplifyURI(predicateName);



                if (predicateName.equals("subClassOf") && objectName.equals("http://www.uk-erlangen.de/MDR-System#Unexported")) {
                    System.out.println(objectName);
                    ontoNode.setDoNotExport(true);


                }

                objectName = simplifyURI(objectName);


                if (predicateName.equals("hasNiceName")) {
                    ontoNode.setNiceName(objectName);

                    if (replaceNodeNames.isSelected()) {
                        //ontoNode.setNodeName(objectName.replaceAll("[^a-zA-Z0-9-]", "_"));
                    }

                }

                if (predicateName.equals("hasConceptCodePrefix")) {
                    ontoNode.setConceptCodePrefix(objectName);


                }
                if (predicateName.equals("hasConceptCodeSuffix")) {
                    ontoNode.setConceptCodeSuffix(objectName);


                }
                if (predicateName.equals("hasDescription")) {
                    ontoNode.setDescription(objectName);


                }
                if (predicateName.equals("hasDataType")) {
                    ontoNode.setDataType(objectName);


                }
                if (predicateName.equals("hasFlagsToUse")) {
                    ontoNode.setFlagsToUse(objectName);


                }
                if (predicateName.equals("hasLowOfLowValue")) {
                    ontoNode.setLowOfLowValue(objectName);


                }
                if (predicateName.equals("hasHighOfLowValue")) {
                    ontoNode.setHighOfLowValue(objectName);


                }
                if (predicateName.equals("hasLowOfHighValue")) {
                    ontoNode.setLowOfHighValue(objectName);


                }
                if (predicateName.equals("hasHighOfHighValue")) {
                    ontoNode.setHighOfHighValue(objectName);


                }
                if (predicateName.equals("hasLowOfToxicValue")) {
                    ontoNode.setLowOfToxicValue(objectName);


                }
                if (predicateName.equals("hasHighOfToxicValue")) {
                    ontoNode.setHighOfToxicValue(objectName);


                }
                if (predicateName.equals("hasUnits")) {
                    ontoNode.setUnits(objectName);


                }
                if (predicateName.equals("hasContext")) {
                    ontoNode.setContext(objectName);


                }
            }

            iProperties.close();

            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(ontoNode);
            node.add(newNode);

            // find all subclasses of this class:
            List subClassesList = new ArrayList();


            for (Iterator i = thisClass.listSubClasses(); i.hasNext();) {
                OntClass c = (OntClass) i.next();
                subClassesList.add(c.toString());


            } // sort the subclasses:
            Collections.sort(subClassesList, Collator.getInstance());

            // recurse into subclasses to add them:


            for (int i = 0; i
                    < subClassesList.size(); i++) {
                getClassTree(model, model.getOntClass(subClassesList.get(i).toString()), newNode);


            }

        } else {
            System.out.println("Error: artefact is null!");


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

    private void loadMDRSystemStuff() {

        OntModel mdrSystem;
        mdrSystem = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

        //create an input stream for the input file
        FileInputStream inputStream = null;


        try {
            inputStream = new FileInputStream(System.getProperty("user.dir") + File.separatorChar + "MDR-System.owl");


        } catch (FileNotFoundException e) {
            System.err.println("'" + inputFile + "' is an invalid input file.");



        }
        mdrSystem.read(inputStream, "");

        // load all Contexts:
        hasContext.removeAllItems();
        hasContext.addItem("");
        OntClass thisClass = ontModel.getOntClass("http://www.uk-erlangen.de/MDR-System#MDR-Context");


        for (Iterator i = thisClass.listInstances(); i.hasNext();) {
            Individual myIndividual = (Individual) i.next();
            hasContext.addItem(simplifyURI(myIndividual.toString()));


        }

        // load all Units:
        hasUnits.removeAllItems();
        hasUnits.addItem("");
        thisClass = ontModel.getOntClass("http://www.uk-erlangen.de/MDR-System#MDR-Unit");


        for (Iterator i = thisClass.listInstances(); i.hasNext();) {
            Individual myIndividual = (Individual) i.next();
            hasUnits.addItem(simplifyURI(myIndividual.toString()));


        }

        // load all Datatypes:
        hasDataType.removeAllItems();
        hasDataType.addItem("");
        thisClass = ontModel.getOntClass("http://www.uk-erlangen.de/MDR-System#MDR-DataType");


        for (Iterator i = thisClass.listInstances(); i.hasNext();) {
            Individual myIndividual = (Individual) i.next();
            hasDataType.addItem(simplifyURI(myIndividual.toString()));

        }
    }

    private String getValidOWLString(String string) {
        return string.replaceAll("#", "NUMBERSIGN");
    }
}
