
import java.net.*;
import java.io.*;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.ValidityReport;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author sebmate
 */
public class QuickMappForm extends javax.swing.JFrame {

    List<DefaultMutableTreeNode> SelectionHistory = new ArrayList<DefaultMutableTreeNode>();
    ;
    DefaultMutableTreeNode leftRootNode;
    DefaultMutableTreeNode rightRootNode;
    DefaultMutableTreeNode leftRootNote;// = new DefaultMutableTreeNode("");
    DefaultMutableTreeNode rightRootNote;// = new DefaultMutableTreeNode("");
    DefaultTreeModel leftTreeModel = new DefaultTreeModel(leftRootNote);
    DefaultTreeModel rightTreeModel = new DefaultTreeModel(rightRootNote);
    String leftFile = "/Users/sebmate/Documents/Uni/Hauptstudium/Diplomarbeit/Ontologien/DPKK-Datensatz-Erlangen.owl";
    String leftNameSpace = "http://www.uk-erlangen.de/DPKK-Datensatz-Erlangen#";
    String leftStartClass = "DPKK-Datenelement";
    String rightFile = "/Users/sebmate/Documents/Uni/Hauptstudium/Diplomarbeit/Ontologien/Soarian.owl";
    String rightNameSpace = "http://www.uk-erlangen.de/Soarian#";
    String rightStartClass = "Soarian_Item";
    String mappingNameSpace = "http://www.uk-erlangen.de/DPKK-Erlangen-Datamapping#";
    String inputFile = "/Users/sebmate/Documents/Uni/Hauptstudium/Diplomarbeit/Ontologien/DPKK-Mapping-Leer.owl";
    String mappingSystemNameSpace = "";
    String selectedObject;
    String orphansDeleteString = "";
    //OntModel leftModel = null;
    //OntModel rightModel = null;
    OntModel ontModel = null;
    DefaultTableModel leftPropertiesTableModel = new DefaultTableModel(
            new Object[][]{},
            new String[]{
                "Predicate", "Object"
            });
    DefaultTableModel rightPropertiesTableModel = new DefaultTableModel(
            new Object[][]{},
            new String[]{
                "Predicate", "Object"
            });
    List leftSearchList = new ArrayList();
    List rightSearchList = new ArrayList();
    private List searchList;
    private int desiredSearch = 0;  // whether we want to search the left (0) or the right (1) ontology
    // Für Auswertung
    private int numClasses = 0;
    private int numClassesWithSubClasses = 0;
    private int numIndividuals = 0;
    private int numClassesLeft = 0;
    private int numClassesWithSubClassesLeft = 0;
    private int numIndividualsLeft = 0;
    private int numClassesRight = 0;
    private int numClassesWithSubClassesRight = 0;
    private int numIndividualsRight = 0;

    /**
     * Creates new form QuickMappForm
     */
    public QuickMappForm() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(QuickMappForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(QuickMappForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(QuickMappForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(QuickMappForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        initComponents();

        // maximize the window:
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {
                setExtendedState(MAXIMIZED_BOTH);
            }
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        SearchWindow = new javax.swing.JFrame();
        Top1 = new javax.swing.JPanel();
        searchText = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        Bottom = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        searchResults = new javax.swing.JList();
        FileDialog = new javax.swing.JFrame();
        leftNameSpaceInput = new javax.swing.JTextField();
        rightNameSpaceInput = new javax.swing.JTextField();
        leftRootClassInput = new javax.swing.JTextField();
        rightRootClassInput = new javax.swing.JTextField();
        mappingNameSpaceInput = new javax.swing.JTextField();
        fileSelectButton = new javax.swing.JButton();
        loadOntoButton = new javax.swing.JButton();
        inputFileLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        mappingSystemNameSpaceInput = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        LoadingInfo = new javax.swing.JFrame();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        OrphansWindow = new javax.swing.JFrame();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        orphanMappingStatements = new javax.swing.JTextArea();
        deleteOrphansButton = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        Top = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        openOntologyButton = new javax.swing.JButton();
        saveOntologyButton = new javax.swing.JButton();
        openSearchWindowButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        CenterPanel = new javax.swing.JPanel();
        LeftPanel = new javax.swing.JPanel();
        LeftTreePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        LeftTreeBrowser = new javax.swing.JTree();
        highlightAllLeft = new javax.swing.JCheckBox();
        highlightSourceOntology = new javax.swing.JCheckBox();
        CloseLeftTree = new javax.swing.JButton();
        LeftPropertiesPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        LeftProperties = new javax.swing.JTable();
        RightPanel = new javax.swing.JPanel();
        RightTreePanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        RightTreeBrowser = new javax.swing.JTree();
        highlightAllRight = new javax.swing.JCheckBox();
        highlightTargetOntology = new javax.swing.JCheckBox();
        CloseRightTree = new javax.swing.JButton();
        highlightCandidates = new javax.swing.JCheckBox();
        RightPropertiesPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        RightProperties = new javax.swing.JTable();
        BottomPanel = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        MappingCode = new javax.swing.JTextArea();
        DeleteButton = new javax.swing.JButton();
        SaveButton = new javax.swing.JButton();
        SimpleMapping = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        InsertSelection = new javax.swing.JButton();
        SelectInTree = new javax.swing.JButton();
        createDateLinkage = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        ShowInFormVisualizer = new javax.swing.JButton();
        getFromFormVisualizer = new javax.swing.JButton();
        visuFollows = new javax.swing.JCheckBox();
        createDateLinkage1 = new javax.swing.JButton();

        SearchWindow.setTitle("Ontology Search");
        SearchWindow.setAlwaysOnTop(true);
        SearchWindow.setLocationByPlatform(true);
        SearchWindow.setMinimumSize(new java.awt.Dimension(286, 343));
        SearchWindow.getContentPane().setLayout(new javax.swing.BoxLayout(SearchWindow.getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        Top1.setMaximumSize(new java.awt.Dimension(32767, 86));

        searchButton.setText("Search");
        searchButton.setMaximumSize(new java.awt.Dimension(97, 29));
        searchButton.setMinimumSize(new java.awt.Dimension(97, 29));
        searchButton.setPreferredSize(new java.awt.Dimension(97, 29));
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Top1Layout = new org.jdesktop.layout.GroupLayout(Top1);
        Top1.setLayout(Top1Layout);
        Top1Layout.setHorizontalGroup(
            Top1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, Top1Layout.createSequentialGroup()
                .addContainerGap()
                .add(searchText, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                .add(10, 10, 10)
                .add(searchButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 121, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        Top1Layout.setVerticalGroup(
            Top1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(Top1Layout.createSequentialGroup()
                .addContainerGap()
                .add(Top1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(searchText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(searchButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 29, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        SearchWindow.getContentPane().add(Top1);

        searchResults.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                searchResultsValueChanged(evt);
            }
        });
        jScrollPane5.setViewportView(searchResults);

        org.jdesktop.layout.GroupLayout BottomLayout = new org.jdesktop.layout.GroupLayout(Bottom);
        Bottom.setLayout(BottomLayout);
        BottomLayout.setHorizontalGroup(
            BottomLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(BottomLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                .addContainerGap())
        );
        BottomLayout.setVerticalGroup(
            BottomLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(BottomLayout.createSequentialGroup()
                .add(jScrollPane5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                .addContainerGap())
        );

        SearchWindow.getContentPane().add(Bottom);

        FileDialog.setTitle("Open Ontology");
        FileDialog.setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        FileDialog.setMaximizedBounds(new java.awt.Rectangle(0, 0, 0, 0));

        fileSelectButton.setText("Select File");
        fileSelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileSelectButtonActionPerformed(evt);
            }
        });

        loadOntoButton.setText("Load Ontology");
        loadOntoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadOntoButtonActionPerformed(evt);
            }
        });

        inputFileLabel.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        inputFileLabel.setText("No file selected!");

        jLabel4.setText("Left namespace:");

        jLabel5.setText("Right namespace:");

        jLabel6.setText("Left root class:");

        jLabel7.setText("Right root class:");

        jLabel8.setText("Mapping namespace:");

        mappingSystemNameSpaceInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mappingSystemNameSpaceInputActionPerformed(evt);
            }
        });

        jLabel9.setText("Mapping-System NS:");

        org.jdesktop.layout.GroupLayout FileDialogLayout = new org.jdesktop.layout.GroupLayout(FileDialog.getContentPane());
        FileDialog.getContentPane().setLayout(FileDialogLayout);
        FileDialogLayout.setHorizontalGroup(
            FileDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(FileDialogLayout.createSequentialGroup()
                .addContainerGap()
                .add(FileDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(FileDialogLayout.createSequentialGroup()
                        .add(fileSelectButton)
                        .add(18, 18, 18)
                        .add(inputFileLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE))
                    .add(FileDialogLayout.createSequentialGroup()
                        .add(FileDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel8)
                            .add(jLabel7)
                            .add(jLabel6)
                            .add(jLabel4)
                            .add(jLabel5)
                            .add(jLabel9))
                        .add(18, 18, 18)
                        .add(FileDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(mappingSystemNameSpaceInput, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                            .add(leftNameSpaceInput, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                            .add(leftRootClassInput, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, rightNameSpaceInput, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                            .add(rightRootClassInput, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                            .add(mappingNameSpaceInput, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, loadOntoButton))
                .addContainerGap())
        );
        FileDialogLayout.setVerticalGroup(
            FileDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(FileDialogLayout.createSequentialGroup()
                .addContainerGap()
                .add(FileDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(fileSelectButton)
                    .add(inputFileLabel))
                .add(18, 18, 18)
                .add(FileDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(leftNameSpaceInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(FileDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel6)
                    .add(leftRootClassInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(FileDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5)
                    .add(rightNameSpaceInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(FileDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(rightRootClassInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel7))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(FileDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel8)
                    .add(mappingNameSpaceInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(FileDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(mappingSystemNameSpaceInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel9))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(loadOntoButton)
                .addContainerGap())
        );

        LoadingInfo.setBackground(new java.awt.Color(153, 153, 153));
        LoadingInfo.setForeground(java.awt.Color.gray);
        LoadingInfo.setMinimumSize(new java.awt.Dimension(335, 62));
        LoadingInfo.setUndecorated(true);

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 3, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Loading ontology, please wait ...");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout LoadingInfoLayout = new org.jdesktop.layout.GroupLayout(LoadingInfo.getContentPane());
        LoadingInfo.getContentPane().setLayout(LoadingInfoLayout);
        LoadingInfoLayout.setHorizontalGroup(
            LoadingInfoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        LoadingInfoLayout.setVerticalGroup(
            LoadingInfoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jToggleButton1.setText("jToggleButton1");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 100, Short.MAX_VALUE)
        );

        OrphansWindow.setTitle("Warning");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 0, 0));
        jLabel10.setText("Warning: orphaned mappings have been found!");

        orphanMappingStatements.setColumns(20);
        orphanMappingStatements.setRows(5);
        jScrollPane7.setViewportView(orphanMappingStatements);

        deleteOrphansButton.setText("Delete orphaned mappings from mapping ontology");
        deleteOrphansButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteOrphansButtonActionPerformed(evt);
            }
        });

        jLabel11.setText("This happens whenever a concept with existing mappings gets deleted or renamed in the target ontology.");

        org.jdesktop.layout.GroupLayout OrphansWindowLayout = new org.jdesktop.layout.GroupLayout(OrphansWindow.getContentPane());
        OrphansWindow.getContentPane().setLayout(OrphansWindowLayout);
        OrphansWindowLayout.setHorizontalGroup(
            OrphansWindowLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(OrphansWindowLayout.createSequentialGroup()
                .addContainerGap()
                .add(OrphansWindowLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 834, Short.MAX_VALUE)
                    .add(jLabel11)
                    .add(jLabel10)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, deleteOrphansButton))
                .addContainerGap())
        );
        OrphansWindowLayout.setVerticalGroup(
            OrphansWindowLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(OrphansWindowLayout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel10)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel11)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(deleteOrphansButton)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("QuickMapp");
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        Top.setMaximumSize(new java.awt.Dimension(32767, 200));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/logo_klinikum.gif"))); // NOI18N

        openOntologyButton.setText("Open");
        openOntologyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openOntologyButtonActionPerformed(evt);
            }
        });

        saveOntologyButton.setText("Save");
        saveOntologyButton.setEnabled(false);
        saveOntologyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveOntologyButtonActionPerformed(evt);
            }
        });

        openSearchWindowButton.setText("Search");
        openSearchWindowButton.setEnabled(false);
        openSearchWindowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openSearchWindowButtonActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 3, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("QuickMapp");

        org.jdesktop.layout.GroupLayout TopLayout = new org.jdesktop.layout.GroupLayout(Top);
        Top.setLayout(TopLayout);
        TopLayout.setHorizontalGroup(
            TopLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, TopLayout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 287, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(openOntologyButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(saveOntologyButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(openSearchWindowButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 268, Short.MAX_VALUE)
                .add(jLabel3))
        );
        TopLayout.setVerticalGroup(
            TopLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(TopLayout.createSequentialGroup()
                .add(TopLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel3)
                    .add(TopLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(TopLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel1)
                            .add(openOntologyButton)
                            .add(saveOntologyButton)
                            .add(openSearchWindowButton))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(Top);

        CenterPanel.setLayout(new java.awt.GridLayout(1, 2));

        LeftPanel.setLayout(new javax.swing.BoxLayout(LeftPanel, javax.swing.BoxLayout.Y_AXIS));

        LeftTreeBrowser.setModel(leftTreeModel);
        LeftTreeBrowser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LeftTreeBrowserMouseClicked(evt);
            }
        });
        LeftTreeBrowser.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                LeftTreeBrowserValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(LeftTreeBrowser);

        highlightAllLeft.setText("Highlight mapped");
        highlightAllLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                highlightAllLeftActionPerformed(evt);
            }
        });

        highlightSourceOntology.setText("Auto highlight source");
        highlightSourceOntology.setEnabled(false);
        highlightSourceOntology.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                highlightSourceOntologyActionPerformed(evt);
            }
        });

        CloseLeftTree.setText("Collapse Hierarchy");
        CloseLeftTree.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CloseLeftTreeActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout LeftTreePanelLayout = new org.jdesktop.layout.GroupLayout(LeftTreePanel);
        LeftTreePanel.setLayout(LeftTreePanelLayout);
        LeftTreePanelLayout.setHorizontalGroup(
            LeftTreePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(LeftTreePanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(LeftTreePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(LeftTreePanelLayout.createSequentialGroup()
                        .add(highlightAllLeft)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(highlightSourceOntology)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 133, Short.MAX_VALUE)
                        .add(CloseLeftTree))
                    .add(jScrollPane1))
                .addContainerGap())
        );
        LeftTreePanelLayout.setVerticalGroup(
            LeftTreePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(LeftTreePanelLayout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(LeftTreePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(highlightAllLeft)
                    .add(highlightSourceOntology)
                    .add(CloseLeftTree)))
        );

        LeftPanel.add(LeftTreePanel);

        LeftPropertiesPanel.setMaximumSize(new java.awt.Dimension(32767, 200));
        LeftPropertiesPanel.setPreferredSize(new java.awt.Dimension(506, 132));

        LeftProperties.setModel(leftPropertiesTableModel);
        LeftProperties.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        LeftProperties.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LeftPropertiesMouseClicked(evt);
            }
        });
        LeftProperties.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                LeftPropertiesPropertyChange(evt);
            }
        });
        jScrollPane2.setViewportView(LeftProperties);

        org.jdesktop.layout.GroupLayout LeftPropertiesPanelLayout = new org.jdesktop.layout.GroupLayout(LeftPropertiesPanel);
        LeftPropertiesPanel.setLayout(LeftPropertiesPanelLayout);
        LeftPropertiesPanelLayout.setHorizontalGroup(
            LeftPropertiesPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(LeftPropertiesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE)
                .addContainerGap())
        );
        LeftPropertiesPanelLayout.setVerticalGroup(
            LeftPropertiesPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(LeftPropertiesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                .addContainerGap())
        );

        LeftPanel.add(LeftPropertiesPanel);

        CenterPanel.add(LeftPanel);

        RightPanel.setLayout(new javax.swing.BoxLayout(RightPanel, javax.swing.BoxLayout.Y_AXIS));

        RightTreeBrowser.setModel(rightTreeModel);
        RightTreeBrowser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RightTreeBrowserMouseClicked(evt);
            }
        });
        RightTreeBrowser.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                RightTreeBrowserValueChanged(evt);
            }
        });
        jScrollPane4.setViewportView(RightTreeBrowser);

        highlightAllRight.setText("Highlight mapped");
        highlightAllRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                highlightAllRightActionPerformed(evt);
            }
        });

        highlightTargetOntology.setText("Auto highlight target");
        highlightTargetOntology.setEnabled(false);
        highlightTargetOntology.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                highlightTargetOntologyActionPerformed(evt);
            }
        });

        CloseRightTree.setText("Collapse Hierarchy");
        CloseRightTree.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CloseRightTreeActionPerformed(evt);
            }
        });

        highlightCandidates.setText("Highlight candidates");
        highlightCandidates.setEnabled(false);
        highlightCandidates.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                highlightCandidatesActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout RightTreePanelLayout = new org.jdesktop.layout.GroupLayout(RightTreePanel);
        RightTreePanel.setLayout(RightTreePanelLayout);
        RightTreePanelLayout.setHorizontalGroup(
            RightTreePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(RightTreePanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(RightTreePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(RightTreePanelLayout.createSequentialGroup()
                        .add(highlightAllRight)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(highlightTargetOntology)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(highlightCandidates)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 14, Short.MAX_VALUE)
                        .add(CloseRightTree))
                    .add(jScrollPane4))
                .addContainerGap())
        );
        RightTreePanelLayout.setVerticalGroup(
            RightTreePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(RightTreePanelLayout.createSequentialGroup()
                .add(jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(RightTreePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(highlightAllRight)
                    .add(highlightTargetOntology)
                    .add(CloseRightTree)
                    .add(highlightCandidates)))
        );

        RightPanel.add(RightTreePanel);

        RightPropertiesPanel.setMaximumSize(new java.awt.Dimension(32767, 200));

        RightProperties.setModel(rightPropertiesTableModel);
        RightProperties.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        RightProperties.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RightPropertiesMouseClicked(evt);
            }
        });
        RightProperties.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                RightPropertiesPropertyChange(evt);
            }
        });
        jScrollPane3.setViewportView(RightProperties);

        org.jdesktop.layout.GroupLayout RightPropertiesPanelLayout = new org.jdesktop.layout.GroupLayout(RightPropertiesPanel);
        RightPropertiesPanel.setLayout(RightPropertiesPanelLayout);
        RightPropertiesPanelLayout.setHorizontalGroup(
            RightPropertiesPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(RightPropertiesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE)
                .addContainerGap())
        );
        RightPropertiesPanelLayout.setVerticalGroup(
            RightPropertiesPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(RightPropertiesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        RightPanel.add(RightPropertiesPanel);

        CenterPanel.add(RightPanel);

        getContentPane().add(CenterPanel);

        BottomPanel.setBackground(java.awt.Color.gray);
        BottomPanel.setMaximumSize(new java.awt.Dimension(30000, 128));
        BottomPanel.setRequestFocusEnabled(false);

        MappingCode.setColumns(20);
        MappingCode.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        MappingCode.setLineWrap(true);
        MappingCode.setRows(1);
        MappingCode.setWrapStyleWord(true);
        MappingCode.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                MappingCodePropertyChange(evt);
            }
        });
        jScrollPane6.setViewportView(MappingCode);

        DeleteButton.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        DeleteButton.setForeground(new java.awt.Color(255, 0, 51));
        DeleteButton.setText("Delete");
        DeleteButton.setEnabled(false);
        DeleteButton.setMaximumSize(new java.awt.Dimension(100, 29));
        DeleteButton.setMinimumSize(new java.awt.Dimension(100, 29));
        DeleteButton.setPreferredSize(new java.awt.Dimension(100, 29));
        DeleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteButtonActionPerformed(evt);
            }
        });

        SaveButton.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        SaveButton.setForeground(new java.awt.Color(255, 0, 51));
        SaveButton.setText("Save");
        SaveButton.setEnabled(false);
        SaveButton.setMaximumSize(new java.awt.Dimension(100, 29));
        SaveButton.setMinimumSize(new java.awt.Dimension(100, 29));
        SaveButton.setPreferredSize(new java.awt.Dimension(100, 29));
        SaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveButtonActionPerformed(evt);
            }
        });

        SimpleMapping.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mapp.png"))); // NOI18N
        SimpleMapping.setToolTipText("Create simple mapping");
        SimpleMapping.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SimpleMappingActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/visualize.png"))); // NOI18N
        jButton2.setToolTipText("Visualize mapping tree");

        InsertSelection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/insert.png"))); // NOI18N
        InsertSelection.setToolTipText("Insert selected item from the right browser");
        InsertSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InsertSelectionActionPerformed(evt);
            }
        });

        SelectInTree.setIcon(new javax.swing.ImageIcon(getClass().getResource("/select.png"))); // NOI18N
        SelectInTree.setToolTipText("Show selection in right browser");
        SelectInTree.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectInTreeActionPerformed(evt);
            }
        });

        createDateLinkage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/calendar.png"))); // NOI18N
        createDateLinkage.setToolTipText("create hasDate statement");
        createDateLinkage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createDateLinkageActionPerformed(evt);
            }
        });

        jCheckBox1.setBackground(java.awt.Color.gray);
        jCheckBox1.setText("Search window auto follow");
        jCheckBox1.setEnabled(false);
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jCheckBox2.setBackground(java.awt.Color.gray);
        jCheckBox2.setText("Weighted Levenshtein matching");
        jCheckBox2.setEnabled(false);

        ShowInFormVisualizer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/form.png"))); // NOI18N
        ShowInFormVisualizer.setToolTipText("Show item in form visualizer");
        ShowInFormVisualizer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShowInFormVisualizerActionPerformed(evt);
            }
        });

        getFromFormVisualizer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/form2.png"))); // NOI18N
        getFromFormVisualizer.setToolTipText("Get selected item from form visualizer");
        getFromFormVisualizer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getFromFormVisualizerActionPerformed(evt);
            }
        });

        visuFollows.setBackground(java.awt.Color.gray);
        visuFollows.setText("Visualizer auto follow");
        visuFollows.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visuFollowsActionPerformed(evt);
            }
        });

        createDateLinkage1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/calendarRe.png"))); // NOI18N
        createDateLinkage1.setToolTipText("create hasDate statement");
        createDateLinkage1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createDateLinkage1ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout BottomPanelLayout = new org.jdesktop.layout.GroupLayout(BottomPanel);
        BottomPanel.setLayout(BottomPanelLayout);
        BottomPanelLayout.setHorizontalGroup(
            BottomPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(BottomPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(BottomPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(BottomPanelLayout.createSequentialGroup()
                        .add(SimpleMapping, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 53, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 53, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(InsertSelection, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 53, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(SelectInTree, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 53, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(ShowInFormVisualizer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 53, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(getFromFormVisualizer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 53, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(createDateLinkage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 53, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(7, 7, 7)
                        .add(createDateLinkage1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 53, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(BottomPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jCheckBox1)
                            .add(jCheckBox2))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(visuFollows)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 22, Short.MAX_VALUE)
                        .add(DeleteButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(SaveButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jScrollPane6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 993, Short.MAX_VALUE))
                .addContainerGap())
        );
        BottomPanelLayout.setVerticalGroup(
            BottomPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(BottomPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 54, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(BottomPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(SimpleMapping, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, BottomPanelLayout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(DeleteButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 46, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(BottomPanelLayout.createSequentialGroup()
                        .add(BottomPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jCheckBox1)
                            .add(visuFollows))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jCheckBox2))
                    .add(jButton2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(InsertSelection, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, SelectInTree, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(createDateLinkage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(ShowInFormVisualizer, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(getFromFormVisualizer, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(createDateLinkage1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(SaveButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(BottomPanel);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-1029)/2, (screenSize.height-510)/2, 1029, 510);
    }// </editor-fold>//GEN-END:initComponents

    private void LeftTreeBrowserValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_LeftTreeBrowserValueChanged


        //      setNodeTextHighlight(LeftTreeBrowser.getLastSelectedPathComponent().toString(), LeftTreeBrowser, leftTreeModel, true);

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) LeftTreeBrowser.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }

        leftPropertiesTableModel.setRowCount(0);
        System.out.println("Left Node selected: " + node);
        String predicateName = "";
        String objectName = "";

        OntClass artefact = ontModel.getOntClass(leftNameSpace + node);


        if (artefact != null) {
            StmtIterator iProperties = artefact.listProperties();
            while (iProperties.hasNext()) {
                Statement s = (Statement) iProperties.next();
                predicateName = s.getPredicate().getLocalName();  // PREDICATE
                objectName = s.getObject().toString();            // OBJECT

                objectName = simplifyURI(objectName);
                predicateName = simplifyURI(predicateName);

                System.out.println("     " + predicateName + " : " + objectName);

                if (!predicateName.equals("type") && !predicateName.equals("subClassOf")) {
                    leftPropertiesTableModel.addRow(new Object[]{predicateName, objectName});
                }
            }
            iProperties.close();
        } else {
            Individual artefact2 = ontModel.getIndividual(leftNameSpace + node);
            StmtIterator iProperties = artefact2.listProperties();
            while (iProperties.hasNext()) {
                Statement s = (Statement) iProperties.next();
                predicateName = s.getPredicate().getLocalName();  // PREDICATE
                objectName = s.getObject().toString();            // OBJECT

                objectName = simplifyURI(objectName);
                predicateName = simplifyURI(predicateName);

                System.out.println("     " + predicateName + " : " + objectName);

                if (!predicateName.equals("type") && !predicateName.equals("subClassOf")) {
                    leftPropertiesTableModel.addRow(new Object[]{predicateName, objectName});
                }
            }
            iProperties.close();
        }


        if (highlightSourceOntology.isSelected()) {

            System.out.println("Visualizing mapping ...");

            highlightAllRight.setSelected(false);
            highlightAllRightActionPerformed(null);
            highlightSourceOntology.setSelected(true);

            expandAll(RightTreeBrowser, false);

            ResultSet response2;
            response2 = runQuery(" select ?importnode"
                    + " WHERE "
                    + "{ "
                    + "{ target:" + node + " omsys:hasImport ?importnode . } "
                    + "}", ontModel);
            while (response2.hasNext()) {
                QuerySolution soln = response2.nextSolution();
                RDFNode subject = soln.get("?importnode");

                selectNode(simplifyURI(subject.toString()), RightTreeBrowser, rightTreeModel);
                setNodeHighlight(simplifyURI(subject.toString()), RightTreeBrowser, rightTreeModel, true);
            }

        }
    }//GEN-LAST:event_LeftTreeBrowserValueChanged

    private void SaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveButtonActionPerformed

        String inText = MappingCode.getText();

        String text[] = inText.split(";");


        for (int i = 0; i < text.length; i++) {

            if (text[i].endsWith(".")) { // then it's a statement

                SourceStatementCreator myAnnotator = new SourceStatementCreator(ontModel, mappingSystemNameSpace, rightNameSpace);
                try {
                    myAnnotator.parseAndSetStatement(text[i]);
                    myAnnotator.addStatement();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex, "Error", JOptionPane.ERROR_MESSAGE);
                }

                ontModel = myAnnotator.getModel();
                RightTreeBrowserValueChanged(null);

            } else if (text[i].contains(" : ")) { // then it's a mapping code

                OntoMappingCreator myMappingCreator = new OntoMappingCreator(ontModel, mappingNameSpace, mappingSystemNameSpace, leftNameSpace, rightNameSpace);

                //OntModel newStatements = myMappingCreator.getModel();

                try {
                    myMappingCreator.addExpression(text[i]);
                } catch (OntoMappingCreator.ParserException ex) {
                    JOptionPane.showMessageDialog(null, ex, "Error", JOptionPane.ERROR_MESSAGE);
                }
                ontModel = myMappingCreator.getModel();
                LeftTreeBrowserValueChanged(null);

            } else {
                JOptionPane.showMessageDialog(null, "Unrecognized syntax: " + text[i], "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_SaveButtonActionPerformed

    private void RightTreeBrowserValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_RightTreeBrowserValueChanged

        setNodeTextHighlight(RightTreeBrowser.getLastSelectedPathComponent().toString(), RightTreeBrowser, rightTreeModel, true);

        if (visuFollows.isSelected()) {
            ShowInFormVisualizerActionPerformed(null);
        }

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) RightTreeBrowser.getLastSelectedPathComponent();

        if (node == null) {
            return;
        }
        rightPropertiesTableModel.setRowCount(0);
        System.out.println("Right Node selected: " + node);
        String predicateName = "";
        String objectName = "";

        StmtIterator iProperties = null;

        try { // Is the selected item a Class?
            OntClass artefact = ontModel.getOntClass(rightNameSpace + node);
            iProperties = artefact.listProperties();
        } catch (Exception e) {
        }
        try { // Is the selected item an Instance?
            Individual artefact = ontModel.getIndividual(rightNameSpace + node);
            iProperties = artefact.listProperties();
        } catch (Exception e) {
        }

        while (iProperties.hasNext()) {
            Statement s = (Statement) iProperties.next();
            predicateName = s.getPredicate().getLocalName();  // PREDICATE
            objectName = s.getObject().toString();            // OBJECT

            objectName = simplifyURI(objectName);
            predicateName = simplifyURI(predicateName);

            if (!predicateName.equals("type") && !predicateName.equals("subClassOf")) {
                rightPropertiesTableModel.addRow(new Object[]{predicateName, objectName});
            }
        }
        iProperties.close();

        if (highlightTargetOntology.isSelected()) {
            System.out.println("Visualizing mapping ...");
            highlightAllLeft.setSelected(false);
            highlightAllLeftActionPerformed(null);
            highlightTargetOntology.setSelected(true);
            expandAll(LeftTreeBrowser, false);
            ResultSet response2;

            response2 = runQuery(" select ?sourcenode"
                    + " WHERE "
                    + "{ "
                    + "{ ?sourcenode omsys:hasImport target:" + node + " . } UNION "
                    + "{ ?sourcenode omsys:hasOperand1 target:" + node + " . } UNION "
                    + "{ ?sourcenode omsys:hasOperand2 target:" + node + " . } "
                    + "}", ontModel);

            while (response2.hasNext()) {
                QuerySolution soln = response2.nextSolution();
                RDFNode subject = soln.get("?sourcenode");

                selectNode(simplifyURI(subject.toString()), LeftTreeBrowser, leftTreeModel);
                setNodeHighlight(simplifyURI(subject.toString()), LeftTreeBrowser, leftTreeModel, true);
            }
        }
}//GEN-LAST:event_RightTreeBrowserValueChanged

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed


        DefaultListModel model = new DefaultListModel();
        searchResults.setModel(model);


        System.out.println("---- Performing Search ----");

        for (int i = 0; i < searchList.size(); i++) {

            if (searchList.get(i).toString().toUpperCase().indexOf(searchText.getText().toUpperCase()) > -1) {
                System.out.println(searchList.get(i));

                //searchResults.add(searchList.get(i).toString(), null);
                int pos = model.getSize();
                model.add(pos, searchList.get(i).toString());
            }


        }

        System.out.println("Done!");        // TODO add your handling code here:

}//GEN-LAST:event_searchButtonActionPerformed

    private void LeftTreeBrowserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LeftTreeBrowserMouseClicked
        searchList = leftSearchList;
        if (desiredSearch == 1) {
            resetSearchWindow();
        }
        desiredSearch = 0;

    }//GEN-LAST:event_LeftTreeBrowserMouseClicked

    private void RightTreeBrowserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RightTreeBrowserMouseClicked
        searchList = rightSearchList;
        if (desiredSearch == 0) {
            resetSearchWindow();
        }
        desiredSearch = 1;
    }//GEN-LAST:event_RightTreeBrowserMouseClicked

    private void openSearchWindowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openSearchWindowButtonActionPerformed
        SearchWindow.setVisible(true);
        searchList = leftSearchList;
    }//GEN-LAST:event_openSearchWindowButtonActionPerformed

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

    private void setNodeTextHighlight(String nodeName, JTree treeBrowser, DefaultTreeModel treeModel, Boolean highlighted) {

        while (SelectionHistory.size() > 26) {
            SelectionHistory.remove(0);
        }



        for (int a = 0; a < SelectionHistory.size(); a++) {
            DefaultMutableTreeNode node2 = SelectionHistory.get(a);
            OntologyNode myNode2 = (OntologyNode) node2.getUserObject();
            int newRed = (int) (myNode2.getColor().getRed());
            newRed = newRed + 20;
            if (newRed >= 255) {
                newRed = 255;
            }

            myNode2.setColor(new Color(newRed, 255, 255));
            node2.setUserObject(myNode2);
            SelectionHistory.set(a, node2);

        }

        DefaultMutableTreeNode node = searchNode(nodeName, treeBrowser);
        if (node != null) {
            OntologyNode myNode = (OntologyNode) node.getUserObject();
            Color curCol = myNode.getColor();
            myNode.setColor(new Color(0, 255, 255));
            node.setUserObject(myNode);

            if (!SelectionHistory.contains(node)) {
                SelectionHistory.add(node);
            }

        } else {
            System.out.println("Node not found");
        }

        LeftTreeBrowser.repaint();
        RightTreeBrowser.repaint();

    }

    private void setNodeHighlight(String nodeName, JTree treeBrowser, DefaultTreeModel treeModel, Boolean highlighted) {

        DefaultMutableTreeNode node = searchNode(nodeName, treeBrowser);

        if (node != null) {

            OntologyNode myNode = (OntologyNode) node.getUserObject();
            if (highlighted) {
                myNode.setHighlighted(true);
            } else {
                myNode.setHighlighted(false);
            }
            node.setUserObject(myNode);

        } else {
            System.out.println("Node not found");
        }
    }

    private void selectNode(String nodeName, JTree treeBrowser, DefaultTreeModel treeModel) {

        DefaultMutableTreeNode node = searchNode(nodeName, treeBrowser);

        if (node != null) {

            TreeNode[] nodes = treeModel.getPathToRoot(node);
            TreePath path = new TreePath(nodes);
            treeBrowser.scrollPathToVisible(path);
            treeBrowser.setSelectionPath(path);

        } else {
            System.out.println("Node not found");
        }

        //LeftTreeBrowser.setModel(m_model);

    }

    private void searchResultsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_searchResultsValueChanged


        if (searchResults.getSelectedValue() != null) {

            String selection = searchResults.getSelectedValue().toString();
            String selectedNode = selection.substring(0, selection.indexOf(" "));

            if (desiredSearch == 0) {
                selectNode(selectedNode, LeftTreeBrowser, leftTreeModel);
            } else {
                selectNode(selectedNode, RightTreeBrowser, rightTreeModel);
            }

        }

    }//GEN-LAST:event_searchResultsValueChanged

    private void saveOntologyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveOntologyButtonActionPerformed
        try {
            FileOutputStream outFile = new FileOutputStream(inputFile);
            ontModel.write(outFile);
            try {
                outFile.close();
            } catch (IOException ex) {
                Logger.getLogger(QuickMappForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(QuickMappForm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_saveOntologyButtonActionPerformed

    private void openOntologyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openOntologyButtonActionPerformed
        //Center the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        FileDialog.pack();
        Dimension frameSize = FileDialog.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        FileDialog.setLocation((screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2);
        FileDialog.setVisible(true);



        BufferedInputStream stream = null;
        Properties properties = new Properties();
        try {
            stream = new BufferedInputStream(new FileInputStream("QuickMapp.properties"));
            properties.load(stream);
            stream.close();
            inputFile = properties.getProperty("inputFile");
            leftNameSpace = properties.getProperty("leftNameSpace");
            leftStartClass = properties.getProperty("leftStartClass");
            rightNameSpace = properties.getProperty("rightNameSpace");
            rightStartClass = properties.getProperty("rightStartClass");
            mappingNameSpace = properties.getProperty("mappingNameSpace");
            mappingSystemNameSpace = properties.getProperty("mappingSystemNameSpace");

            inputFileLabel.setText(inputFile);
            leftNameSpaceInput.setText(leftNameSpace);
            leftRootClassInput.setText(leftStartClass);
            rightNameSpaceInput.setText(rightNameSpace);
            rightRootClassInput.setText(rightStartClass);
            mappingNameSpaceInput.setText(mappingNameSpace);
            mappingSystemNameSpaceInput.setText(mappingSystemNameSpace);


        } catch (IOException ex) {
            Logger.getLogger(QuickMappForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_openOntologyButtonActionPerformed

    private void loadOntoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadOntoButtonActionPerformed

        inputFile = inputFileLabel.getText();
        leftNameSpace = leftNameSpaceInput.getText();
        leftStartClass = leftRootClassInput.getText();
        rightNameSpace = rightNameSpaceInput.getText();
        rightStartClass = rightRootClassInput.getText();
        mappingNameSpace = mappingNameSpaceInput.getText();
        mappingSystemNameSpace = mappingSystemNameSpaceInput.getText();

        try {
            System.out.println("saveConfiguration called!");
            Properties properties = new Properties();
            properties.setProperty("inputFile", inputFile);
            properties.setProperty("leftNameSpace", leftNameSpace);
            properties.setProperty("leftStartClass", leftStartClass);
            properties.setProperty("rightNameSpace", rightNameSpace);
            properties.setProperty("rightStartClass", rightStartClass);
            properties.setProperty("mappingNameSpace", mappingNameSpace);
            properties.setProperty("mappingSystemNameSpace", mappingSystemNameSpace);


            File propertiesFileFile = new File("QuickMapp.properties");
            properties.store(new FileOutputStream(propertiesFileFile), "QuickMapp Properties File");
        } catch (IOException ex) {
            Logger.getLogger(QuickMappForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        FileDialog.setVisible(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = LoadingInfo.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        LoadingInfo.pack();
        LoadingInfo.setLocation((screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2);
        LoadingInfo.setVisible(true);

        new Thread(new Runnable() {

            private int resultCnt;

            public void run() {

                leftSearchList.clear();
                rightSearchList.clear();
                initialize();
                LoadingInfo.setVisible(false);
                saveOntologyButton.setEnabled(true);
                openSearchWindowButton.setEnabled(true);
                SaveButton.setEnabled(true);
                System.out.println("Items loaded on the left: " + leftSearchList.size());
                System.out.println("Items loaded on the right: " + rightSearchList.size());



                ResultSet response2;
                response2 = runQuery(" select ?class"
                        + " WHERE "
                        + "{ "
                        + "{ ?class omsys:hasImport ?otherThing . } "
                        + "}", ontModel);

                while (response2.hasNext()) {
                    response2.nextSolution();
                    resultCnt++;
                }

                resultCnt--;

                response2 = runQuery(" select ?class"
                        + " WHERE "
                        + "{ "
                        + "{ ?class omsys:hasImport ?otherThing .  "
                        + "  ?otherThing rdf:type omsys:ProcessedItem . } "
                        + "}", ontModel);

                int resultCnt2 = 0;


                while (response2.hasNext()) {
                    response2.nextSolution();
                    resultCnt2++;
                }

                resultCnt2--;

                response2 = runQuery(" select ?class"
                        + " WHERE "
                        + "{ "
                        + "{ ?class omsys:hasImport ?otherThing .  "
                        + "  ?otherThing rdf:type omsys:UnprocessedItem . } "
                        + "}", ontModel);

                int resultCnt3 = 0;

                while (response2.hasNext()) {
                    response2.nextSolution();
                    resultCnt3++;
                }

                resultCnt3--;


                response2 = runQuery(" select ?class"
                        + " WHERE "
                        + "{ "
                        + "{ ?class omsys:hasImport ?otherThing1 .  "
                        + "  ?class omsys:hasImport ?otherThing2 .  "
                        + "  ?otherThing1 rdf:type omsys:ProcessedItem . "
                        + "  ?otherThing2 rdf:type omsys:UnprocessedItem . } "
                        + "}", ontModel);

                int resultCnt4 = 0;

                while (response2.hasNext()) {
                    response2.nextSolution();
                    resultCnt4++;
                }

                resultCnt4--;

                JOptionPane.showMessageDialog(null,
                        "Target Ontology:"
                        + "\n       Number of classes: " + numClassesLeft
                        + "\n       Number of classes with subclasses: " + numClassesWithSubClassesLeft + " ( hierarchy)"
                        + "\n       Number of leaf classes: " + (numClassesLeft - numClassesWithSubClassesLeft) + " (nodes mapped to)"
                        + "\n       Number of instances: " + numIndividualsLeft + " (should be zero)"
                        + "\n\nSource Ontology:"
                        + "\n       Number of classes : " + numClassesRight
                        + "\n       Number of classes with subclasses: " + numClassesWithSubClassesRight + " (hierarchy)"
                        + "\n       Number of leaf classes: " + (numClassesRight - numClassesWithSubClassesRight) + " (attributes)"
                        + "\n       Number of instances: " + numIndividualsRight + " (values)"
                        + "\n\nMapping Ontology:"
                        + "\n       Number of hasImport (total) : " + resultCnt
                        + "\n       Number of hasImport to ProcessedItem (simple mappings) : " + resultCnt2
                        + "\n       Number of hasImport to UnprocessedItem (complex mappings) : " + resultCnt3
                        + "\n       Number of hasImport of mixed type (simple + complex mappings) : " + resultCnt4,
                        "Ontology statistics", JOptionPane.INFORMATION_MESSAGE);
            }
        }).start();

    }//GEN-LAST:event_loadOntoButtonActionPerformed

    private void fileSelectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileSelectButtonActionPerformed

        JFileChooser chooser = new JFileChooser();
        ExampleFileFilter filter = new ExampleFileFilter();
        filter.addExtension("owl");
        filter.setDescription("OWL Ontologies");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            inputFileLabel.setText(chooser.getSelectedFile().getAbsolutePath());
        }
        FileDialog.toFront();

    }//GEN-LAST:event_fileSelectButtonActionPerformed

    private void LeftPropertiesPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_LeftPropertiesPropertyChange
    }//GEN-LAST:event_LeftPropertiesPropertyChange

    private void LeftPropertiesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LeftPropertiesMouseClicked

        String selectedProperty = LeftProperties.getValueAt(LeftProperties.getSelectedRow(), 0).toString();
        selectedObject = LeftProperties.getValueAt(LeftProperties.getSelectedRow(), 1).toString();

        if (selectedProperty.equals("hasImport")) {

            OntoMappingParser myOntoMappingParser = new OntoMappingParser(ontModel, mappingSystemNameSpace, "");
            String mappingCode = myOntoMappingParser.getMappingCode(mappingNameSpace + selectedObject);

            if (mappingCode.equals("")) {
                mappingCode = myOntoMappingParser.getMappingCode(rightNameSpace + selectedObject);
            }

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) LeftTreeBrowser.getLastSelectedPathComponent();
            MappingCode.setText(node.toString() + " : " + mappingCode);
            DeleteButton.setEnabled(true);

        } else {

            selectedObject = "";
            DeleteButton.setEnabled(false);
            MappingCode.setText("");
        }

        RightProperties.clearSelection();
    }//GEN-LAST:event_LeftPropertiesMouseClicked

    private void mappingSystemNameSpaceInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mappingSystemNameSpaceInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mappingSystemNameSpaceInputActionPerformed

    private void DeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteButtonActionPerformed

        String text = MappingCode.getText();

        if (text.endsWith(".")) { // then it's a statement

            SourceStatementDeleter myDeleter = new SourceStatementDeleter(ontModel, mappingSystemNameSpace, rightNameSpace);
            try {
                myDeleter.parseAndSetStatement(text);
                myDeleter.deleteStatement();
                RightTreeBrowserValueChanged(null);
            } catch (Exception ex) {
                Logger.getLogger(QuickMappForm.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (text.contains(" : ")) { // then it's a mapping code

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) LeftTreeBrowser.getLastSelectedPathComponent();
            System.out.println("Delete Request: " + selectedObject);
            OntoMappingDeleter myOntoMappingDeleter = new OntoMappingDeleter(ontModel, mappingSystemNameSpace, mappingNameSpace);
            myOntoMappingDeleter.deleteMapping(leftNameSpace + node.toString(), rightNameSpace + selectedObject);
            myOntoMappingDeleter.deleteMapping(leftNameSpace + node.toString(), mappingNameSpace + selectedObject);
            LeftTreeBrowserValueChanged(null);
            MappingCode.setText("");
        }
    }//GEN-LAST:event_DeleteButtonActionPerformed

    private void MappingCodePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_MappingCodePropertyChange
    }//GEN-LAST:event_MappingCodePropertyChange

    private void SimpleMappingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SimpleMappingActionPerformed


        TreePath[] paths = RightTreeBrowser.getSelectionPaths();


        String newCode = "";

        for (int a = 0; a < paths.length - 1; a++) {

            System.out.println(paths[a].getLastPathComponent());
            newCode += LeftTreeBrowser.getLastSelectedPathComponent() + " : " + paths[a].getLastPathComponent() + "; ";
        }

        newCode += LeftTreeBrowser.getLastSelectedPathComponent() + " : " + paths[paths.length - 1].getLastPathComponent() + "";



        MappingCode.setText(newCode);
    }//GEN-LAST:event_SimpleMappingActionPerformed

    private void InsertSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InsertSelectionActionPerformed
        String newCode = "" + RightTreeBrowser.getLastSelectedPathComponent();

        int cursorPos = MappingCode.getCaretPosition();
        try {

            String leftPart = MappingCode.getText(0, cursorPos);
            String rightPart = MappingCode.getText().substring(cursorPos);
            MappingCode.setText(leftPart + newCode + rightPart);

        } catch (BadLocationException ex) {
            Logger.getLogger(QuickMappForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_InsertSelectionActionPerformed

    private void SelectInTreeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectInTreeActionPerformed

        String selectedItem = MappingCode.getSelectedText();
        System.out.println("Selecting node: " + selectedItem);
        selectNode(selectedItem, RightTreeBrowser, rightTreeModel);
    }//GEN-LAST:event_SelectInTreeActionPerformed

    private void createDateLinkageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createDateLinkageActionPerformed

        String newCode = RightTreeBrowser.getLastSelectedPathComponent() + " hasDate DATEITEM .";
        MappingCode.setText(newCode);

    }//GEN-LAST:event_createDateLinkageActionPerformed

    private void RightPropertiesPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_RightPropertiesPropertyChange
    }//GEN-LAST:event_RightPropertiesPropertyChange

    private void RightPropertiesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RightPropertiesMouseClicked

        String selectedProperty = RightProperties.getValueAt(RightProperties.getSelectedRow(), 0).toString();
        selectedObject = RightProperties.getValueAt(RightProperties.getSelectedRow(), 1).toString();

        if (ontModel.getObjectProperty(mappingSystemNameSpace + selectedProperty) != null) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) RightTreeBrowser.getLastSelectedPathComponent();
            MappingCode.setText(node.toString() + " " + selectedProperty + " " + selectedObject + " .");
            DeleteButton.setEnabled(true);
        } else {
            selectedObject = "";
            DeleteButton.setEnabled(false);
            MappingCode.setText("");
        }

        LeftProperties.clearSelection();
    }//GEN-LAST:event_RightPropertiesMouseClicked

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void deleteOrphansButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteOrphansButtonActionPerformed
        String[] orphStmts = orphansDeleteString.split("\n");
        for (int i = 0; i < orphStmts.length; i++) {
            System.out.println(orphStmts[i]);
            if (!orphStmts[i].equals("")) {
                String[] elements = orphStmts[i].split(" : ");
                OntoMappingDeleter myOntoMappingDeleter = new OntoMappingDeleter(ontModel, mappingSystemNameSpace, mappingNameSpace);
                myOntoMappingDeleter.deleteMapping(leftNameSpace + elements[0], rightNameSpace + elements[1]);
                myOntoMappingDeleter.deleteMapping(leftNameSpace + elements[0], mappingNameSpace + elements[1]);
                LeftTreeBrowserValueChanged(null);
            }
        }
        orphanMappingStatements.setText("");
    }//GEN-LAST:event_deleteOrphansButtonActionPerformed

    private void ShowInFormVisualizerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ShowInFormVisualizerActionPerformed
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) RightTreeBrowser.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }

        String url = "http://localhost:8080/" + node + "#selected";
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch (IOException ex) {
            Logger.getLogger(QuickMappForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_ShowInFormVisualizerActionPerformed

    private void getFromFormVisualizerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getFromFormVisualizerActionPerformed
        try {
            URL url = new URL("http://localhost:8080/getLastSelection");
            URLConnection yc = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            String lastSelection = "";
            while ((inputLine = in.readLine()) != null) {

                if (!inputLine.equals("")) {
                    lastSelection += inputLine;
                }
            }
            in.close();

            System.out.println("LastSelection = " + lastSelection);


            for (int i = 0; i < rightSearchList.size(); i++) {
                if (rightSearchList.get(i).toString().toUpperCase().indexOf(lastSelection.toUpperCase()) > -1) {

                    String myNode = rightSearchList.get(i).toString().substring(0, rightSearchList.get(i).toString().indexOf(" "));

                    System.out.println(myNode);
                    selectNode(myNode, RightTreeBrowser, rightTreeModel);
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(QuickMappForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_getFromFormVisualizerActionPerformed

    private void visuFollowsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visuFollowsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_visuFollowsActionPerformed

    private void recursiveAnnotate(DefaultMutableTreeNode node, String annotation) throws Exception {
        SourceStatementCreator myAnnotator = new SourceStatementCreator(ontModel, mappingSystemNameSpace, rightNameSpace);

        myAnnotator.parseAndSetStatement(node.toString() + " " + annotation);
        myAnnotator.addStatement();

        ontModel = myAnnotator.getModel();
        //RightTreeBrowserValueChanged(null);
        int count = rightTreeModel.getChildCount(node);
        for (int i = 0; i < count; i++) {
            recursiveAnnotate((DefaultMutableTreeNode) rightTreeModel.getChild(node, i), annotation);
        }
    }

    private void createDateLinkage1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createDateLinkage1ActionPerformed
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) RightTreeBrowser.getLastSelectedPathComponent();
        String returnString = JOptionPane.showInputDialog("Please finish statement (predicate object .):");
        TreePath[] paths = RightTreeBrowser.getSelectionPaths();
        try {
            for (int i = 0; i < paths.length; i++) {

                DefaultMutableTreeNode node2 = (DefaultMutableTreeNode) paths[i].getLastPathComponent();
                System.out.println(node2.toString());
                recursiveAnnotate(node2, returnString);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex, "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(QuickMappForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        RightTreeBrowserValueChanged(null);
    }//GEN-LAST:event_createDateLinkage1ActionPerformed

    private void highlightAllLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_highlightAllLeftActionPerformed

        highlightTargetOntology.setSelected(false);
        expandAll(LeftTreeBrowser, false);

        ResultSet response2;
        ArrayList classList2 = new ArrayList();
        response2 = runQuery(" select ?destclass"
                + " WHERE "
                + "{ "
                + "?destclass omsys:hasImport ?importnode . "
                + "}", ontModel);
        while (response2.hasNext()) {
            QuerySolution soln = response2.nextSolution();
            RDFNode subject = soln.get("?destclass");

            selectNode(simplifyURI(subject.toString()), LeftTreeBrowser, leftTreeModel);

            if (highlightAllLeft.isSelected()) {
                setNodeHighlight(simplifyURI(subject.toString()), LeftTreeBrowser, leftTreeModel, true);
            } else {
                setNodeHighlight(simplifyURI(subject.toString()), LeftTreeBrowser, leftTreeModel, false);
            }
        }
    }//GEN-LAST:event_highlightAllLeftActionPerformed

    private void highlightSourceOntologyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_highlightSourceOntologyActionPerformed
        highlightAllRight.setSelected(false);
        LeftTreeBrowserValueChanged(null);
    }//GEN-LAST:event_highlightSourceOntologyActionPerformed

    private void highlightAllRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_highlightAllRightActionPerformed

        highlightSourceOntology.setSelected(false);
        expandAll(RightTreeBrowser, false);

        ResultSet response2;
        ArrayList classList2 = new ArrayList();
        response2 = runQuery(" select ?importnode"
                + " WHERE "
                + "{{ "
                + "?destclass omsys:hasImport ?importnode . "
                + "} UNION {"
                + "?destclass omsys:hasOperand1 ?importnode . "
                + "} UNION {"
                + "?destclass omsys:hasOperand2 ?importnode . "
                + "}}", ontModel);
        while (response2.hasNext()) {
            QuerySolution soln = response2.nextSolution();
            RDFNode subject = soln.get("?importnode");

            selectNode(simplifyURI(subject.toString()), RightTreeBrowser, rightTreeModel);

            if (highlightAllRight.isSelected()) {
                setNodeHighlight(simplifyURI(subject.toString()), RightTreeBrowser, rightTreeModel, true);
            } else {
                setNodeHighlight(simplifyURI(subject.toString()), RightTreeBrowser, rightTreeModel, false);
            }
        }
    }//GEN-LAST:event_highlightAllRightActionPerformed

    private void highlightTargetOntologyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_highlightTargetOntologyActionPerformed
        highlightAllLeft.setSelected(false);
        RightTreeBrowserValueChanged(null);
    }//GEN-LAST:event_highlightTargetOntologyActionPerformed

    private void CloseLeftTreeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseLeftTreeActionPerformed
        expandAll(LeftTreeBrowser, false);
    }//GEN-LAST:event_CloseLeftTreeActionPerformed

    private void CloseRightTreeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseRightTreeActionPerformed
        expandAll(RightTreeBrowser, false);
    }//GEN-LAST:event_CloseRightTreeActionPerformed

    private void highlightCandidatesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_highlightCandidatesActionPerformed
    }//GEN-LAST:event_highlightCandidatesActionPerformed

    // If expand is true, expands all nodes in the tree.
    // Otherwise, collapses all nodes in the tree.
    public void expandAll(JTree tree, boolean expand) {
        TreeNode root = (TreeNode) tree.getModel().getRoot();

        // Traverse tree from root
        expandAll(tree, new TreePath(root), expand);
    }

    private void expandAll(JTree tree, TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }

        // Expansion or collapse must be done bottom-up
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new QuickMappForm().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Bottom;
    private javax.swing.JPanel BottomPanel;
    private javax.swing.JPanel CenterPanel;
    private javax.swing.JButton CloseLeftTree;
    private javax.swing.JButton CloseRightTree;
    private javax.swing.JButton DeleteButton;
    private javax.swing.JFrame FileDialog;
    private javax.swing.JButton InsertSelection;
    private javax.swing.JPanel LeftPanel;
    private javax.swing.JTable LeftProperties;
    private javax.swing.JPanel LeftPropertiesPanel;
    private javax.swing.JTree LeftTreeBrowser;
    private javax.swing.JPanel LeftTreePanel;
    private javax.swing.JFrame LoadingInfo;
    private javax.swing.JTextArea MappingCode;
    private javax.swing.JFrame OrphansWindow;
    private javax.swing.JPanel RightPanel;
    private javax.swing.JTable RightProperties;
    private javax.swing.JPanel RightPropertiesPanel;
    private javax.swing.JTree RightTreeBrowser;
    private javax.swing.JPanel RightTreePanel;
    private javax.swing.JButton SaveButton;
    private javax.swing.JFrame SearchWindow;
    private javax.swing.JButton SelectInTree;
    private javax.swing.JButton ShowInFormVisualizer;
    private javax.swing.JButton SimpleMapping;
    private javax.swing.JPanel Top;
    private javax.swing.JPanel Top1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton createDateLinkage;
    private javax.swing.JButton createDateLinkage1;
    private javax.swing.JButton deleteOrphansButton;
    private javax.swing.JButton fileSelectButton;
    private javax.swing.JButton getFromFormVisualizer;
    private javax.swing.JCheckBox highlightAllLeft;
    private javax.swing.JCheckBox highlightAllRight;
    private javax.swing.JCheckBox highlightCandidates;
    private javax.swing.JCheckBox highlightSourceOntology;
    private javax.swing.JCheckBox highlightTargetOntology;
    private javax.swing.JLabel inputFileLabel;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JTextField leftNameSpaceInput;
    private javax.swing.JTextField leftRootClassInput;
    private javax.swing.JButton loadOntoButton;
    private javax.swing.JTextField mappingNameSpaceInput;
    private javax.swing.JTextField mappingSystemNameSpaceInput;
    private javax.swing.JButton openOntologyButton;
    private javax.swing.JButton openSearchWindowButton;
    private javax.swing.JTextArea orphanMappingStatements;
    private javax.swing.JTextField rightNameSpaceInput;
    private javax.swing.JTextField rightRootClassInput;
    private javax.swing.JButton saveOntologyButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JList searchResults;
    private javax.swing.JTextField searchText;
    private javax.swing.JCheckBox visuFollows;
    // End of variables declaration//GEN-END:variables

    void initialize() {


        leftRootNote = new DefaultMutableTreeNode("Loading, please wait ...");
        rightRootNote = new DefaultMutableTreeNode("Loading, please wait ...");

        OntologyNodeRenderer myRenderer = new OntologyNodeRenderer();

        LeftTreeBrowser.setCellRenderer(myRenderer);
        RightTreeBrowser.setCellRenderer(myRenderer);


        // See: http://www.java-forum.org/java-faq-beitraege/39070-jtable-teil-8-sortieren.html
        LeftProperties.setDefaultRenderer(Point.class,
                new PointRender());

        RightProperties.setDefaultRenderer(Point.class,
                new PointRender());
        TableRowSorter<TableModel> sorter1 = new TableRowSorter<TableModel>();
        TableRowSorter<TableModel> sorter2 = new TableRowSorter<TableModel>();
        LeftProperties.setRowSorter(sorter1);
        RightProperties.setRowSorter(sorter2);
        sorter1.setModel(leftPropertiesTableModel);
        sorter2.setModel(rightPropertiesTableModel);


        //create an input stream for the input file
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(inputFile);
        } catch (FileNotFoundException e) {
            System.err.println("'" + inputFile + "' is an invalid input file.");

        }

        System.out.println("Setting up Jena model with inferencing level 'none' ...");
        ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

        System.out.println("Loading OWL file ...");

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


        createOntologyBrowser(ontModel, leftNameSpace, leftStartClass, leftRootNote, LeftTreeBrowser, LeftProperties, leftSearchList);

        numClassesLeft = numClasses;
        numClassesWithSubClassesLeft = numClassesWithSubClasses;
        numIndividualsLeft = numIndividuals;
        numClasses = 0;
        numClassesWithSubClasses = 0;
        numIndividuals = 0;

        System.out.println(" ------------ Left Browser ------------");
        System.out.println("Number of Classes: " + numClasses);
        System.out.println("Number of Classes with Subclasses: " + numClassesWithSubClasses);
        System.out.println("Number of Individuals: " + numIndividuals);


        createOntologyBrowser(ontModel, rightNameSpace, rightStartClass, rightRootNote, RightTreeBrowser, RightProperties, rightSearchList);

        numClassesRight = numClasses;
        numClassesWithSubClassesRight = numClassesWithSubClasses;
        numIndividualsRight = numIndividuals;
        numClasses = 0;
        numClassesWithSubClasses = 0;
        numIndividuals = 0;


        System.out.println(" ------------ Right Browser ------------");
        System.out.println("Number of Classes: " + numClasses);
        System.out.println("Number of Classes with Subclasses: " + numClassesWithSubClasses);
        System.out.println("Number of Individuals: " + numIndividuals);

        // Find orphaned Mappings
        ResultSet response1;
        ArrayList classList1 = new ArrayList();


        
          response1 = runQuery(" select ?destclass" + " WHERE " + "{ " +        "?destclass omsys:hasImport ?importnode . " + "?destclass          rdfs:subClassOf ?anyClass . " + "}", ontModel);
         

        // Display ALL mappings (instead of orphaned mappings):

        //response1 = runQuery(" select ?node" + " WHERE " + "{ " + "?node omsys:hasCommandTyoe ?commandtype . " + "}", ontModel);




        while (response1.hasNext()) {
            QuerySolution soln = response1.nextSolution();
            RDFNode subject = soln.get("?destclass");
            //System.out.println("Found Node: " + subject.toString());
            classList1.add(subject.toString());
        }


        removeDuplicate(classList1);

        ResultSet response2;
        ArrayList classList2 = new ArrayList();
        response2 = runQuery(" select ?destclass"
                + " WHERE "
                + "{ "
                + "?destclass omsys:hasImport ?importnode . "
                + "}", ontModel);

        while (response2.hasNext()) {
            QuerySolution soln = response2.nextSolution();
            RDFNode subject = soln.get("?destclass");
            // System.out.println("Found Node: " + subject.toString());
            classList2.add(subject.toString());
        }

        removeDuplicate(classList2);
        classList2.removeAll(classList1);

        if (classList2.size() > 0) {

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            OrphansWindow.pack();
            Dimension frameSize = OrphansWindow.getSize();
            if (frameSize.height > screenSize.height) {
                frameSize.height = screenSize.height;
            }
            if (frameSize.width > screenSize.width) {
                frameSize.width = screenSize.width;
            }
            OrphansWindow.setLocation((screenSize.width - frameSize.width) / 2,
                    (screenSize.height - frameSize.height) / 2);
            OrphansWindow.setVisible(true);

            String orphans = "";

            for (int i = 0; i < classList2.size(); i++) {
                System.out.println("Found Orphan: " + classList2.get(i));

                Resource orphan = ontModel.getResource(classList2.get(i).toString());
                StmtIterator iProperties = orphan.listProperties();

                while (iProperties.hasNext()) {
                    Statement s = (Statement) iProperties.next();
                    String predicateName = s.getPredicate().getLocalName();  // PREDICATE
                    String objectName = s.getObject().toString();            // OBJECT

                    objectName = simplifyURI(objectName);
                    predicateName = simplifyURI(predicateName);

                    if (predicateName.equals("hasImport")) {
                        System.out.println("objectName: " + objectName);

                        OntoMappingParser myOntoMappingParser = new OntoMappingParser(ontModel, mappingSystemNameSpace, mappingNameSpace);

                        String mappingCode = myOntoMappingParser.getMappingCode(mappingNameSpace + objectName);

                        if (mappingCode.equals("")) {
                            mappingCode = myOntoMappingParser.getMappingCode(rightNameSpace + objectName);
                        }

                        System.out.println(classList2.get(i) + " : " + mappingCode);

                        orphans += simplifyURI(classList2.get(i).toString()) + " : " + mappingCode + "\n";
                        orphansDeleteString += simplifyURI(classList2.get(i).toString()) + " : " + objectName + "\n";
                    }
                }
                orphans += "\n";
            }
            orphanMappingStatements.setText(orphans);
        }
    }

    public static void removeDuplicate(ArrayList arlList) {
        HashSet h = new HashSet(arlList);
        arlList.clear();
        arlList.addAll(h);
    }

    ResultSet runQuery(String queryRequest, Model model) {

        StringBuilder queryStr = new StringBuilder();
        queryStr.append("PREFIX mdrsys" + ": <" + "http://www.uk-erlangen.de/MDR-System#" + "> ");
        queryStr.append("PREFIX omsys" + ": <" + "http://www.uk-erlangen.de/OntoMappingSystem#" + "> ");
        queryStr.append("PREFIX owl" + ": <" + "http://www.w3.org/2002/07/owl#" + "> ");
        //queryStr.append("PREFIX mapp" + ": <" + "http://www.uk-erlangen.de/UKER-DPKK-Mapping#" + "> ");
        queryStr.append("PREFIX rdfs" + ": <" + "http://www.w3.org/2000/01/rdf-schema#" + "> ");
        queryStr.append("PREFIX rdf" + ": <" + "http://www.w3.org/1999/02/22-rdf-syntax-ns#" + "> ");
        //queryStr.append("PREFIX soa" + ": <" + "http://www.uk-erlangen.de/Soarian#" + "> ");
        //queryStr.append("PREFIX ont" + ": <" + "http://www.uk-erlangen.de/DPKK-Datensatz#" + "> ");

        queryStr.append("PREFIX target" + ": <" + leftNameSpace + "> ");
        queryStr.append("PREFIX source" + ": <" + rightNameSpace + "> ");

        queryStr.append(queryRequest);

        System.out.println("SPARQL query is " + queryStr);

        Query query = QueryFactory.create(queryStr.toString());
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        ResultSet response = qexec.execSelect();
        return response;
    }

    private void createOntologyBrowser(OntModel model, String nameSpace, String startClass, DefaultMutableTreeNode rootNode, JTree treeBrowser, JTable propertiesViewer, List searchList) {

        System.out.println("createOntologyBrowser() called for class " + nameSpace + startClass);

        rootNode.setUserObject(new OntologyNode(startClass, 1));
        DefaultMutableTreeNode root = rootNode;
        DefaultTreeModel m_model = new DefaultTreeModel(root);
        DefaultMutableTreeNode temp = new DefaultMutableTreeNode();
        OntClass artefact = model.getOntClass(nameSpace + startClass);
        temp = getClassTree(model, nameSpace, artefact, root, searchList);
        treeBrowser.setModel(new DefaultTreeModel(temp));
        treeBrowser.setShowsRootHandles(true);
        System.out.println("Ontology browser loaded!");
    }

    private DefaultMutableTreeNode getClassTree(OntModel model, String nameSpace, OntClass artefact, DefaultMutableTreeNode node, List searchList) {

        StmtIterator iProperties = null;
        String niceName = "";

        if (artefact != null) {

            List subClassesList = new ArrayList();
            List individualsList = new ArrayList();

            // process subclasses:

            if (artefact.isClass()) {
                numClasses++;
            }


            if (artefact.hasSubClass()) {
                numClassesWithSubClasses++;
                System.out.println("Mit Subclass: " + artefact.toString());
            }

            for (Iterator i = artefact.listSubClasses(); i.hasNext();) {
                OntClass c = (OntClass) i.next();


                //System.out.println("Processing Ontology Class: " + simplifyURI(c.getURI()));
                //System.out.println("                           " + c.getLocalName());

                subClassesList.add(simplifyURI(c.getURI()));

                // process properties of each subclass to get the "hasNiceName" properties,
                // we need these for the search function:

                iProperties = c.listProperties();
                niceName = "";
                while (iProperties.hasNext()) {
                    Statement s = (Statement) iProperties.next();
                    String predicateName = s.getPredicate().getLocalName();  // PREDICATE
                    String objectName = s.getObject().toString();            // OBJECT

                    objectName = simplifyURI(objectName);
                    predicateName = simplifyURI(predicateName);

                    if (predicateName.equals("hasNiceName") || predicateName.equals("hasValue")) {
                        niceName += " " + objectName;
                    }
                }
                //System.out.println(niceName);
                iProperties.close();

                searchList.add(simplifyURI(c.getURI()) + " -" + niceName);
            }

            Collections.sort(subClassesList, Collator.getInstance());

            // add subclasses:

            for (int i = 0; i < subClassesList.size(); i++) {

                //DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(subClassesList.get(i));
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new OntologyNode(subClassesList.get(i).toString(), 0));
                DefaultMutableTreeNode children;
                children = getClassTree(model, nameSpace, model.getOntClass(nameSpace + subClassesList.get(i).toString()), newNode, searchList);

                if (children == null) {
                    System.out.println(subClassesList.get(i));
                }

                node.add(newNode);
            }

            // process individuals:

            ExtendedIterator iIndividuals = artefact.listInstances();
            while (iIndividuals.hasNext()) {

                numIndividuals++;

                Individual i = (Individual) iIndividuals.next();
                individualsList.add(i.getLocalName());
                //System.out.println("Processing Individual: " + i.getLocalName());


                // process properties of each subclass to get the "hasNiceName" properties,
                // we need these for the search function:

                iProperties = i.listProperties();
                niceName = "";
                while (iProperties.hasNext()) {
                    Statement s = (Statement) iProperties.next();
                    String predicateName = s.getPredicate().getLocalName();  // PREDICATE
                    String objectName = s.getObject().toString();            // OBJECT
                    objectName = simplifyURI(objectName);
                    predicateName = simplifyURI(predicateName);
                    if (predicateName.equals("hasNiceName") || predicateName.equals("hasValue")) {
                        niceName += " " + objectName;
                    }
                }
                //System.out.println(niceName);
                iProperties.close();
                searchList.add(i.getLocalName() + " -" + niceName);

            }

            iIndividuals.close();
            Collections.sort(individualsList, Collator.getInstance());

            // add individuals:
            for (int i = 0; i < individualsList.size(); i++) {
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new OntologyNode(individualsList.get(i).toString(), 1));
                node.add(newNode);
            }
            return node;

        } else {
            System.out.println("Error: artefact is null!");
            System.out.println("       node: " + node);
            return null;
        }
    }

    private void resetSearchWindow() {
        searchResults.setModel(new DefaultListModel());
        //searchText.setText("");
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
}
