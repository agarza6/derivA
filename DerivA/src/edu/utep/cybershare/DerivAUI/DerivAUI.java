/**
Copyright (c) 2012, University of Texas at El Paso
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation 
and/or other materials provided with the distribution.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE 
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT 
LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH 
DAMAGE.
 */

package edu.utep.cybershare.DerivAUI;

import java.awt.Button;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.xmlrpc.XmlRpcException;

import edu.utep.cybershare.DerivA.*;
import edu.utep.cybershare.DerivA.util.ServerCredentials;
import edu.utep.cybershare.DerivAUI.components.*;
import edu.utep.cybershare.DerivAUI.tools.*;
import edu.utep.cybershare.ciclient.ciconnect.CIClient;
import edu.utep.cybershare.ciclient.ciconnect.CIKnownServerTable;
import edu.utep.cybershare.ciclient.ciui.CIGetProjectListDialog;
import edu.utep.cybershare.ciclient.ciui.CIGetResourceSaveLocationDialog;




public class DerivAUI extends javax.swing.JFrame implements PropertyChangeListener {

	private static final long serialVersionUID = 1L;
	public static DerivAUI instance;
	public ServerCredentials creds = new ServerCredentials();
	private int MODE;
	private int ASSERT_MODE = 1;
	private int DERIVATE_MODE = 2;
	private int DOC_DERIVATE_MODE = 3;

	// Variables declaration - do not modify
	private Cursor waiting = new Cursor(Cursor.WAIT_CURSOR), normal;
	private JFileChooser fc;
	private File oFile;
	private ImageIcon check, uncheck;
	private ImageIcon SourceIcon, ConclusionIcon, IAIcon, IRIcon, AntecedentIcon;

	//Combo Boxes
	private AgentComboBox agentComboBox;
	private formatComboBox conclusionFormatComboBox;
	private typeComboBox conclusionTypeComboBox;
	private InferenceRulesComboBox inferenceRuleComboBox;
	private javax.swing.JComboBox<String> IATypeComboBox;

	//Panels
	private javax.swing.JPanel mainPanel;
	private javax.swing.JPanel AntecedentPanel;
	private javax.swing.JPanel ConclusionPanel;
	private javax.swing.JPanel IAgentPanel;
	private javax.swing.JPanel IRulePanel;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel conclusionFromURLTab;
	private javax.swing.JPanel conclusionFromServerTab;
	private javax.swing.JPanel conclusionFromLocalTab;
	private javax.swing.JPanel SourcesPanel;
	private javax.swing.JTabbedPane conclusionFromTab;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JScrollPane jScrollPane3;
	private javax.swing.JScrollPane jScrollPane4;
	private javax.swing.JScrollPane jScrollPane5;
	private javax.swing.JTabbedPane Tabs;
	//	private javax.swing.JTabbedPane jTabbedPane1;

	//Labels
	private javax.swing.JLabel AntecedentLabel;
	private javax.swing.JLabel InferenceAgentLabel;
	private javax.swing.JLabel InferenceRuleLabel;
	private javax.swing.JLabel conclusionLabel;
	private javax.swing.JLabel ProjectLabel;
	private javax.swing.JLabel ServerLabel;
	private javax.swing.JLabel selectedProjectLabel;
	private javax.swing.JLabel selectedServerLabel;
	private javax.swing.JLabel selectedWorkflowLabel;
	private javax.swing.JLabel WorkflowLabel;
	private javax.swing.JLabel availableAntecedentLabel;
	private javax.swing.JLabel fileFormatLabel;
	private javax.swing.JLabel fileTypeLabel;
	private javax.swing.JLabel currentlySelectedAntecedentLabel;
	private javax.swing.JLabel assertArtifactModeLabel;
	private javax.swing.JLabel selectedOntologyLabel;
	private javax.swing.JLabel OntologyLabel;
	private javax.swing.JLabel DerivateArtifactModeLabel;
	private javax.swing.JLabel DocumentDerivationModeLabel;
	private javax.swing.JLabel ModeLabel;
	private javax.swing.JLabel SourcesLabel;
	private javax.swing.JLabel availableSourcesLabel;
	private javax.swing.JLabel currentlySelectedSourcesLabel;
	private javax.swing.JLabel localFileSystemLabel;
	private javax.swing.JLabel fromServerLabel;
	private javax.swing.JLabel fromURLLabel;
	private javax.swing.JLabel IASortByLabel;

	//Buttons
	private javax.swing.JButton conclusionBrowserButton;
	private Button captureProvenanceButton;
	private javax.swing.JButton addAntecedentButton;
	private javax.swing.JButton removeAntecedentButton;
	private javax.swing.JButton assertArtifactModeButton;
	private javax.swing.JButton derivateArtifactModeButton;
	private javax.swing.JButton docArtifactModeButton;
	private javax.swing.JButton removeSourceButton;
	private javax.swing.JButton addSourceButton;
	private javax.swing.JButton viewWorkflowButton;

	//Menu Items
	private javax.swing.JMenu fileMenu;
	private javax.swing.JMenuBar jMenuBar1;
	private javax.swing.JMenu ToolsMenu;
	private javax.swing.JMenu HelpMenu;
	private javax.swing.JMenuItem loadSAWItem;
	private javax.swing.JMenuItem loadWDOItem;
	private javax.swing.JMenuItem logInItem;
	private javax.swing.JMenuItem aboutItem;
	private javax.swing.JMenuItem CloseItem;
	private javax.swing.JPopupMenu.Separator jSeparator3;
	private javax.swing.JMenuItem tripleStoreAggregater;
	private javax.swing.JMenuItem addSourceTool;
	private javax.swing.JMenuItem addAgentTool;
	private javax.swing.JMenuItem addFormatTool;

	//Lists
	private IndividualList antecedentList;
	private IndividualList currentlySelectedAntecedentList;
	private IndividualList conclusionList;
	private Vector<IndividualList.Individual> General_PMLJ_Nodes;
	private Vector<IndividualList.Individual> Project_PMLJ_Nodes;
	private Vector<IndividualList.Individual> CurrentlySelectedAntecedentsVector;
	private Vector<IndividualList.Individual> CurrentlySelectedSourcesVector;
	private Vector<IndividualList.Individual> SourcesPML_Nodes;
	private IndividualList SourcesList;
	private IndividualList currentlySelectedSourcesList;

	//Extras
	private javax.swing.JProgressBar ProgressBar;
	private javax.swing.JCheckBox antecedentsByProjectCheckBox;
	private javax.swing.JCheckBox IncludeUserCheckBox;
	private javax.swing.JTextField conclusionBrowserTF;
	private javax.swing.JTextField fromURLTF;

	//Strings
	private String selectedOntologySTR = null, selectedWorkflowSTR = null;;

	//tools
	private DerivationMaker DM;
	private AssertionMaker AM;
	private AddAgentTool AAT;
	private AddFormatTool AFT;
	private AddSourceTool AST;
	private WorkflowDriver Driver;
	private about about;

	private LoadTask Loadtask;
	private deriveTask deriveTask;
	private assertTask assertTask;

	// End of variables declaration


	/** Creates new form DerivAGUI */
	public DerivAUI() {
		serverLogin();
	}

	public void setSelectedOntology(String sel){
		selectedOntologySTR = sel;
		String temp = selectedOntologySTR.substring(selectedOntologySTR.lastIndexOf('/') + 1);
		selectedOntologyLabel.setText(temp);
	}

	public void setSelectedWorkflow(String sel){
		selectedWorkflowSTR = sel;
		String temp = selectedWorkflowSTR.substring(selectedWorkflowSTR.lastIndexOf('/') + 1);
		selectedWorkflowLabel.setText(temp);
		viewWorkflowButton.setVisible(true);
	}

	private void initTripleStoreComponents(){

		setCursor(waiting);

		ProgressBar.setString("Loading Components");
		ProgressBar.setStringPainted(true);

		Loadtask = new LoadTask();
		Loadtask.addPropertyChangeListener(this);
		Loadtask.execute();
	}

	private void initComponents() {

		conclusionList = new IndividualList();
		antecedentList = new IndividualList();
		SourcesList = new IndividualList();
		currentlySelectedSourcesList = new IndividualList();
		currentlySelectedAntecedentList = new IndividualList();
		conclusionFormatComboBox = new formatComboBox();
		conclusionTypeComboBox = new typeComboBox();
		agentComboBox = new AgentComboBox();
		inferenceRuleComboBox = new InferenceRulesComboBox();

		mainPanel = new javax.swing.JPanel();
		ServerLabel = new javax.swing.JLabel();
		selectedServerLabel = new javax.swing.JLabel();
		ProjectLabel = new javax.swing.JLabel();
		selectedProjectLabel = new javax.swing.JLabel();
		OntologyLabel = new javax.swing.JLabel();
		selectedOntologyLabel = new javax.swing.JLabel();
		WorkflowLabel = new javax.swing.JLabel();
		selectedWorkflowLabel = new javax.swing.JLabel();
		jPanel1 = new javax.swing.JPanel();
		ModeLabel = new javax.swing.JLabel();
		assertArtifactModeButton = new javax.swing.JButton();
		derivateArtifactModeButton = new javax.swing.JButton();
		assertArtifactModeLabel = new javax.swing.JLabel();
		DerivateArtifactModeLabel = new javax.swing.JLabel();
		DocumentDerivationModeLabel = new javax.swing.JLabel();
		docArtifactModeButton = new javax.swing.JButton();
		Tabs = new javax.swing.JTabbedPane();
		SourcesPanel = new javax.swing.JPanel();
		SourcesLabel = new javax.swing.JLabel();
		availableSourcesLabel = new javax.swing.JLabel();
		currentlySelectedSourcesLabel = new javax.swing.JLabel();
		jScrollPane4 = new javax.swing.JScrollPane();
		jScrollPane5 = new javax.swing.JScrollPane();
		addSourceButton = new javax.swing.JButton();
		viewWorkflowButton = new javax.swing.JButton();
		removeSourceButton = new javax.swing.JButton();
		IncludeUserCheckBox = new javax.swing.JCheckBox();
		ConclusionPanel = new javax.swing.JPanel();
		conclusionLabel = new javax.swing.JLabel();
		fileFormatLabel = new javax.swing.JLabel();
		fileTypeLabel = new javax.swing.JLabel();
		captureProvenanceButton = new java.awt.Button();
		conclusionFromTab = new javax.swing.JTabbedPane();
		conclusionFromLocalTab = new javax.swing.JPanel();
		localFileSystemLabel = new javax.swing.JLabel();
		conclusionBrowserButton = new javax.swing.JButton();
		conclusionBrowserTF = new javax.swing.JTextField();
		conclusionFromServerTab = new javax.swing.JPanel();
		fromServerLabel = new javax.swing.JLabel();
		jScrollPane1 = new javax.swing.JScrollPane();
		conclusionFromURLTab = new javax.swing.JPanel();
		fromURLLabel = new javax.swing.JLabel();
		fromURLTF = new javax.swing.JTextField();
		IAgentPanel = new javax.swing.JPanel();
		InferenceAgentLabel = new javax.swing.JLabel();
		IRulePanel = new javax.swing.JPanel();
		InferenceRuleLabel = new javax.swing.JLabel();
		AntecedentPanel = new javax.swing.JPanel();
		AntecedentLabel = new javax.swing.JLabel();
		jScrollPane2 = new javax.swing.JScrollPane();
		antecedentsByProjectCheckBox = new javax.swing.JCheckBox();
		jScrollPane3 = new javax.swing.JScrollPane();
		addAntecedentButton = new javax.swing.JButton();
		removeAntecedentButton = new javax.swing.JButton();
		availableAntecedentLabel = new javax.swing.JLabel();
		currentlySelectedAntecedentLabel = new javax.swing.JLabel();
		ProgressBar = new javax.swing.JProgressBar();
		jMenuBar1 = new javax.swing.JMenuBar();
		fileMenu = new javax.swing.JMenu();
		logInItem = new javax.swing.JMenuItem();
		loadWDOItem = new javax.swing.JMenuItem();
		loadSAWItem = new javax.swing.JMenuItem();
		jSeparator3 = new javax.swing.JPopupMenu.Separator();
		CloseItem = new javax.swing.JMenuItem();
		ToolsMenu = new javax.swing.JMenu();
		tripleStoreAggregater = new javax.swing.JMenuItem();
		HelpMenu = new javax.swing.JMenu();
		aboutItem = new javax.swing.JMenuItem();
		addSourceTool = new javax.swing.JMenuItem();
		addAgentTool = new javax.swing.JMenuItem();
		addFormatTool = new javax.swing.JMenuItem();
		IASortByLabel = new javax.swing.JLabel();
		
		ProgressBar.setForeground(Color.green);
		ProgressBar.setBackground(Color.black);

		check = new ImageIcon(getClass().getResource("images/tick.png"));
		uncheck = new ImageIcon(getClass().getResource("images/checkbox_unchecked.png"));

		SourceIcon = uncheck;
		ConclusionIcon = uncheck;
		IAIcon = uncheck;
		IRIcon = uncheck; 
		AntecedentIcon = uncheck;

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		this.setTitle("derivA - Manual Annotation of Provenance. PML Edition");

		ServerLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
		ServerLabel.setText("Server:");

		selectedServerLabel.setText("none");

		ProjectLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
		ProjectLabel.setText("Project:");

		selectedProjectLabel.setText("none");

		OntologyLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
		OntologyLabel.setText("Ontology:");

		selectedOntologyLabel.setText("none");

		WorkflowLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
		WorkflowLabel.setText("Workflow: ");

		selectedWorkflowLabel.setText("none");

		ModeLabel.setFont(new java.awt.Font("Tahoma", 1, 12));
		ModeLabel.setText("Select Provenance Step Type");

		assertArtifactModeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("images/assert.png"))); // NOI18N
		assertArtifactModeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				assertModeAction(evt);
			}
		});

		derivateArtifactModeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("images/makeDerivation.png"))); // NOI18N
		derivateArtifactModeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				derivateModeAction(evt);
			}
		});

		docArtifactModeButton.setEnabled(false);
		docArtifactModeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("images/docDerivatio.png"))); // NOI18N
		docArtifactModeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				docDerivateModeAction(evt);
			}
		});

		assertArtifactModeLabel.setText("Assert Artifact");

		DerivateArtifactModeLabel.setText("Derive Artifact");

		DocumentDerivationModeLabel.setText("Document Derivation");

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(
				jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup()
						.addComponent(ModeLabel)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(jPanel1Layout.createSequentialGroup()
								.addGap(56, 56, 56)
								.addComponent(assertArtifactModeLabel)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(DerivateArtifactModeLabel)
								.addGap(104, 104, 104)
								.addComponent(DocumentDerivationModeLabel)
								.addGap(37, 37, 37))
								.addGroup(jPanel1Layout.createSequentialGroup()
										.addContainerGap()
										.addComponent(assertArtifactModeButton)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
										.addComponent(derivateArtifactModeButton)
										.addGap(29, 29, 29)
										.addComponent(docArtifactModeButton)
										.addContainerGap(12, Short.MAX_VALUE))
				);
		jPanel1Layout.setVerticalGroup(
				jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup()
						.addComponent(ModeLabel)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(assertArtifactModeButton)
								.addComponent(derivateArtifactModeButton)
								.addComponent(docArtifactModeButton))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(assertArtifactModeLabel)
										.addComponent(DerivateArtifactModeLabel)
										.addComponent(DocumentDerivationModeLabel))
										.addContainerGap())
				);

		viewWorkflowButton.setForeground(new java.awt.Color(102, 102, 255));
        viewWorkflowButton.setText("View");
        viewWorkflowButton.setBorder(null);
        viewWorkflowButton.setBorderPainted(false);
        viewWorkflowButton.setContentAreaFilled(false);
        viewWorkflowButton.setVisible(false);
        viewWorkflowButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openWorkflowViewAction(evt);
			}
		});

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addComponent(ServerLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(selectedServerLabel)
                                .addGap(18, 18, 18)
                                .addComponent(ProjectLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(selectedProjectLabel))
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(OntologyLabel)
                                    .addComponent(WorkflowLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(selectedOntologyLabel)
                                    .addGroup(mainPanelLayout.createSequentialGroup()
                                        .addComponent(selectedWorkflowLabel)
                                        .addGap(18, 18, 18)
                                        .addComponent(viewWorkflowButton))))))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ServerLabel)
                    .addComponent(selectedServerLabel)
                    .addComponent(ProjectLabel)
                    .addComponent(selectedProjectLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(OntologyLabel)
                    .addComponent(selectedOntologyLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(WorkflowLabel)
                    .addComponent(selectedWorkflowLabel)
                    .addComponent(viewWorkflowButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

		SourcesLabel.setFont(new java.awt.Font("Tahoma", 1, 12));
		SourcesLabel.setText("Select All Sources");

		availableSourcesLabel.setText("Available Sources");

		currentlySelectedSourcesLabel.setText("Currently Selected Sources");

		jScrollPane4.setViewportView(SourcesList);

		jScrollPane5.setViewportView(currentlySelectedSourcesList);

		addSourceButton.setFont(new java.awt.Font("Tahoma", 1, 14));
		addSourceButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("images/001_01.gif"))); // NOI18N
		addSourceButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addSourceAction(evt);
			}
		});

		removeSourceButton.setFont(new java.awt.Font("Tahoma", 1, 14));
		removeSourceButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("images/001_02.gif"))); // NOI18N
		removeSourceButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				removeSourceAction(evt);
			}
		});

		IncludeUserCheckBox.setSelected(false);
		IncludeUserCheckBox.setText("Include Session User");
		IncludeUserCheckBox.setEnabled(false);

		javax.swing.GroupLayout SourcesPanelLayout = new javax.swing.GroupLayout(SourcesPanel);
		SourcesPanel.setLayout(SourcesPanelLayout);
		SourcesPanelLayout.setHorizontalGroup(
				SourcesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(SourcesPanelLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(SourcesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(SourcesPanelLayout.createSequentialGroup()
										.addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(SourcesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(removeSourceButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(addSourceButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
												.addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE))
												.addGroup(SourcesPanelLayout.createSequentialGroup()
														.addGroup(SourcesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																.addComponent(SourcesLabel)
																.addComponent(availableSourcesLabel)
																.addComponent(IncludeUserCheckBox))
																.addGap(0, 0, Short.MAX_VALUE)))
																.addContainerGap())
																.addGroup(SourcesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																		.addGroup(SourcesPanelLayout.createSequentialGroup()
																				.addGap(316, 316, 316)
																				.addComponent(currentlySelectedSourcesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
																				.addContainerGap(131, Short.MAX_VALUE)))
				);
		SourcesPanelLayout.setVerticalGroup(
				SourcesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(SourcesPanelLayout.createSequentialGroup()
						.addGap(88, 88, 88)
						.addComponent(addSourceButton)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(removeSourceButton)
						.addContainerGap(93, Short.MAX_VALUE))
						.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SourcesPanelLayout.createSequentialGroup()
								.addContainerGap()
								.addComponent(SourcesLabel)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(availableSourcesLabel)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(SourcesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
										.addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
										.addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(IncludeUserCheckBox)
										.addGap(36, 36, 36))
										.addGroup(SourcesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(SourcesPanelLayout.createSequentialGroup()
														.addGap(34, 34, 34)
														.addComponent(currentlySelectedSourcesLabel)
														.addContainerGap(210, Short.MAX_VALUE)))
				);

		Tabs.addTab("Sources", SourceIcon, SourcesPanel);

		conclusionLabel.setFont(new java.awt.Font("Tahoma", 1, 12));
		conclusionLabel.setText("Select Conclusion Artifact");

		fileFormatLabel.setText("File Type:");

		fileTypeLabel.setText("File Format:");

		captureProvenanceButton.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
		captureProvenanceButton.setBackground(Color.green);
		captureProvenanceButton.setLabel("CAPTURE PROVENANCE");
		captureProvenanceButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				captureProvenanceAction(evt);
			}
		});

		localFileSystemLabel.setText("Select Conclusion From Local File System");

		conclusionBrowserButton.setText("Browse");
		conclusionBrowserButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				browseAction(evt);
			}
		});

		conclusionBrowserTF.getDocument().addDocumentListener(new MyDocumentListener());

		javax.swing.GroupLayout conclusionFromLocalTabLayout = new javax.swing.GroupLayout(conclusionFromLocalTab);
		conclusionFromLocalTab.setLayout(conclusionFromLocalTabLayout);
		conclusionFromLocalTabLayout.setHorizontalGroup(
				conclusionFromLocalTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(conclusionFromLocalTabLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(conclusionFromLocalTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(conclusionFromLocalTabLayout.createSequentialGroup()
										.addComponent(localFileSystemLabel)
										.addGap(0, 372, Short.MAX_VALUE))
										.addGroup(conclusionFromLocalTabLayout.createSequentialGroup()
												.addComponent(conclusionBrowserButton)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(conclusionBrowserTF)))
												.addContainerGap())
				);
		conclusionFromLocalTabLayout.setVerticalGroup(
				conclusionFromLocalTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(conclusionFromLocalTabLayout.createSequentialGroup()
						.addContainerGap()
						.addComponent(localFileSystemLabel)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(conclusionFromLocalTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(conclusionBrowserButton)
								.addComponent(conclusionBrowserTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addContainerGap(67, Short.MAX_VALUE))
				);

		conclusionFromTab.addTab("From Local File System", conclusionFromLocalTab);

		fromServerLabel.setText("Select Conclusion From Server");
		conclusionList.setEnabled(false);
		jScrollPane1.setViewportView(conclusionList);

		javax.swing.GroupLayout conclusionFromServerTabLayout = new javax.swing.GroupLayout(conclusionFromServerTab);
		conclusionFromServerTab.setLayout(conclusionFromServerTabLayout);
		conclusionFromServerTabLayout.setHorizontalGroup(
				conclusionFromServerTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(conclusionFromServerTabLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(conclusionFromServerTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(fromServerLabel))
								.addContainerGap(302, Short.MAX_VALUE))
				);
		conclusionFromServerTabLayout.setVerticalGroup(
				conclusionFromServerTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(conclusionFromServerTabLayout.createSequentialGroup()
						.addContainerGap()
						.addComponent(fromServerLabel)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
						.addContainerGap())
				);

		conclusionFromTab.addTab("From CI-Server", conclusionFromServerTab);

		fromURLLabel.setText("Select Conclusion From URL");

		fromURLTF.setText("http://");

		javax.swing.GroupLayout conclusionFromURLTabLayout = new javax.swing.GroupLayout(conclusionFromURLTab);
		conclusionFromURLTab.setLayout(conclusionFromURLTabLayout);
		conclusionFromURLTabLayout.setHorizontalGroup(
				conclusionFromURLTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(conclusionFromURLTabLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(conclusionFromURLTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(fromURLTF, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
								.addComponent(fromURLLabel))
								.addContainerGap())
				);
		conclusionFromURLTabLayout.setVerticalGroup(
				conclusionFromURLTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(conclusionFromURLTabLayout.createSequentialGroup()
						.addContainerGap()
						.addComponent(fromURLLabel)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(fromURLTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(70, Short.MAX_VALUE))
				);

		conclusionFromTab.addTab("From URL", conclusionFromURLTab);
		conclusionFormatComboBox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				changeConclusionCheckStatus(evt);
			}
		});

		conclusionTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				changeConclusionCheckStatus(evt);
			}
		});

		javax.swing.GroupLayout ConclusionPanelLayout = new javax.swing.GroupLayout(ConclusionPanel);
		ConclusionPanel.setLayout(ConclusionPanelLayout);
		ConclusionPanelLayout.setHorizontalGroup(
				ConclusionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(ConclusionPanelLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(ConclusionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(ConclusionPanelLayout.createSequentialGroup()
										.addGroup(ConclusionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(conclusionLabel)
												.addGroup(ConclusionPanelLayout.createSequentialGroup()
														.addComponent(fileFormatLabel)
														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(conclusionTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
														.addGroup(ConclusionPanelLayout.createSequentialGroup()
																.addComponent(fileTypeLabel)
																.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(conclusionFormatComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
																.addGap(0, 0, Short.MAX_VALUE))
																.addComponent(conclusionFromTab, javax.swing.GroupLayout.Alignment.TRAILING))
																.addContainerGap())
				);
		ConclusionPanelLayout.setVerticalGroup(
				ConclusionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(ConclusionPanelLayout.createSequentialGroup()
						.addContainerGap()
						.addComponent(conclusionLabel)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(ConclusionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(fileFormatLabel)
								.addComponent(conclusionTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(ConclusionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(fileTypeLabel)
										.addComponent(conclusionFormatComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(conclusionFromTab)
										.addGap(25, 25, 25))
				);

		Tabs.addTab("Conclusion", ConclusionIcon, ConclusionPanel);

		InferenceAgentLabel.setFont(new java.awt.Font("Tahoma", 1, 12));
		InferenceAgentLabel.setText("Select an Agent");
		agentComboBox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				changeIACheckStatus(evt);
			}
		});

		String[] IATypes = {"Agent", "Software", "Person", "Organization" };
		IATypeComboBox = new javax.swing.JComboBox<String>(IATypes);
		IATypeComboBox.setSelectedIndex(0);
		IATypeComboBox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				sortIABy(evt);
			}
		});

		javax.swing.GroupLayout IAgentPanelLayout = new javax.swing.GroupLayout(IAgentPanel);
		IAgentPanel.setLayout(IAgentPanelLayout);
		IAgentPanelLayout.setHorizontalGroup(
				IAgentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(IAgentPanelLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(IAgentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(agentComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(InferenceAgentLabel)
								.addGroup(IAgentPanelLayout.createSequentialGroup()
										.addComponent(IASortByLabel)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(IATypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
										.addContainerGap(433, Short.MAX_VALUE))
				);
		IAgentPanelLayout.setVerticalGroup(
				IAgentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(IAgentPanelLayout.createSequentialGroup()
						.addContainerGap()
						.addComponent(InferenceAgentLabel)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(IAgentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(IASortByLabel)
								.addComponent(IATypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGap(9, 9, 9)
								.addComponent(agentComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addContainerGap(172, Short.MAX_VALUE))
				);

		Tabs.addTab("Agent", IAIcon, IAgentPanel);
		InferenceRuleLabel.setFont(new java.awt.Font("Tahoma", 1, 12));

		InferenceRuleLabel.setText("Select Activity Method");
		inferenceRuleComboBox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				changeIRCheckStatus(evt);
			}
		});

		javax.swing.GroupLayout IRulePanelLayout = new javax.swing.GroupLayout(IRulePanel);
		IRulePanel.setLayout(IRulePanelLayout);
		IRulePanelLayout.setHorizontalGroup(
				IRulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(IRulePanelLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(IRulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(InferenceRuleLabel)
								.addComponent(inferenceRuleComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addContainerGap(396, Short.MAX_VALUE))
				);
		IRulePanelLayout.setVerticalGroup(
				IRulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(IRulePanelLayout.createSequentialGroup()
						.addContainerGap()
						.addComponent(InferenceRuleLabel)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(inferenceRuleComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(206, Short.MAX_VALUE))
				);

		Tabs.addTab("Activity Method", IRIcon, IRulePanel);


		AntecedentLabel.setFont(new java.awt.Font("Tahoma", 1, 12));
		AntecedentLabel.setText("Select All Antecedents that Derive the Selected Conclusion");
		jScrollPane2.setViewportView(antecedentList);

		antecedentsByProjectCheckBox.setSelected(false);
		antecedentsByProjectCheckBox.setText("Artifacts by Project ONLY");
		antecedentsByProjectCheckBox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				updateAntecedentList(evt);
			}
		});
		jScrollPane3.setViewportView(currentlySelectedAntecedentList);

		addAntecedentButton.setFont(new java.awt.Font("Tahoma", 1, 14));
		addAntecedentButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("images/001_01.gif"))); // NOI18N
		addAntecedentButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addAntecedentAction(evt);
			}
		});

		removeAntecedentButton.setFont(new java.awt.Font("Tahoma", 1, 14));
		removeAntecedentButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("images/001_02.gif"))); // NOI18N
		removeAntecedentButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				removeAntecedentAction(evt);
			}
		});

		availableAntecedentLabel.setText("Available Artifacts");

		currentlySelectedAntecedentLabel.setText("Currently Selected Artifacts");
		jScrollPane3.setViewportView(currentlySelectedAntecedentList);

		javax.swing.GroupLayout AntecedentPanelLayout = new javax.swing.GroupLayout(AntecedentPanel);
		AntecedentPanel.setLayout(AntecedentPanelLayout);
		AntecedentPanelLayout.setHorizontalGroup(
				AntecedentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(AntecedentPanelLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(AntecedentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(AntecedentPanelLayout.createSequentialGroup()
										.addGroup(AntecedentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(AntecedentPanelLayout.createSequentialGroup()
														.addGroup(AntecedentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																.addComponent(availableAntecedentLabel)
																.addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
																.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addGroup(AntecedentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
																		.addComponent(removeAntecedentButton, 0, 0, Short.MAX_VALUE)
																		.addComponent(addAntecedentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
																		.addComponent(antecedentsByProjectCheckBox))
																		.addGroup(AntecedentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																				.addGroup(AntecedentPanelLayout.createSequentialGroup()
																						.addGap(10, 10, 10)
																						.addComponent(currentlySelectedAntecedentLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
																						.addGap(0, 105, Short.MAX_VALUE))
																						.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AntecedentPanelLayout.createSequentialGroup()
																								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																								.addComponent(jScrollPane3))))
																								.addComponent(AntecedentLabel))
																								.addContainerGap())
				);
		AntecedentPanelLayout.setVerticalGroup(
				AntecedentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(AntecedentPanelLayout.createSequentialGroup()
						.addContainerGap()
						.addComponent(AntecedentLabel)
						.addGroup(AntecedentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(AntecedentPanelLayout.createSequentialGroup()
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(AntecedentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(availableAntecedentLabel)
												.addComponent(currentlySelectedAntecedentLabel))
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addGroup(AntecedentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
														.addComponent(jScrollPane3)
														.addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE))
														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(antecedentsByProjectCheckBox))
														.addGroup(AntecedentPanelLayout.createSequentialGroup()
																.addGap(64, 64, 64)
																.addComponent(addAntecedentButton)
																.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																.addComponent(removeAntecedentButton)))
																.addGap(66, 66, 66))
				);

		Tabs.addTab("Derived From", AntecedentIcon, AntecedentPanel);

		fileMenu.setText("File");

		logInItem.setText("Log In");
		//		fileMenu.add(logInItem);

		loadWDOItem.setText("Load Ontology");
		loadWDOItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openOntologyToolAction(evt);
			}
		});
		fileMenu.add(loadWDOItem);

		loadSAWItem.setText("Load Workflow");
		loadSAWItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openWorkflowToolAction(evt);
			}
		});
		fileMenu.add(loadSAWItem);
		fileMenu.add(jSeparator3);

		CloseItem.setText("Close");
		CloseItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				closeAction(evt);
			}
		});
		fileMenu.add(CloseItem);

		jMenuBar1.add(fileMenu);

		ToolsMenu.setText("Tools");

		tripleStoreAggregater.setText("Add Conclusion to Triple Store");
		tripleStoreAggregater.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aggregateToolAction(evt);
			}
		});
		ToolsMenu.add(tripleStoreAggregater);

		addSourceTool.setText("Create New Person Entity");
		addSourceTool.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addSourceToolAction(evt);
			}
		});
		ToolsMenu.add(addSourceTool);

		addAgentTool.setText("Create New Actor");
		addAgentTool.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addAgentToolAction(evt);
			}
		});
		ToolsMenu.add(addAgentTool);

		addFormatTool.setText("Create New Format");
		addFormatTool.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addFormatToolAction(evt);
			}
		});
		ToolsMenu.add(addFormatTool);

		jMenuBar1.add(ToolsMenu);

		HelpMenu.setText("Help");

		aboutItem.setText("About derivA");
		HelpMenu.add(aboutItem);
		aboutItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aboutAction(evt);
			}
		});

		jMenuBar1.add(HelpMenu);

		setJMenuBar(jMenuBar1);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
								.addComponent(mainPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(Tabs, javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(ProgressBar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(captureProvenanceButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addContainerGap())
				);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(Tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(captureProvenanceButton, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(ProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap())
				);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);

		MODE = ASSERT_MODE;

		Tabs.setEnabledAt(0, true);
		Tabs.setEnabledAt(1, true);
		Tabs.setEnabledAt(2, false);
		Tabs.setEnabledAt(3, false);
		Tabs.setEnabledAt(4, false);

		conclusionList.setEnabled(false);
		conclusionBrowserButton.setEnabled(true);
	}

	public void aboutAction(java.awt.event.ActionEvent evt){
		if(about == null){
			about = new about();
		}
		about.setVisible(true);
	}

	public void assertModeAction(java.awt.event.ActionEvent evt){
		MODE = ASSERT_MODE;

		Tabs.setEnabledAt(0, true);
		Tabs.setEnabledAt(1, true);
		Tabs.setEnabledAt(2, false);
		Tabs.setEnabledAt(3, false);
		Tabs.setEnabledAt(4, false);

		conclusionList.setEnabled(false);
		conclusionBrowserButton.setEnabled(true);

		captureProvenanceButton.setLabel("ASSERT ARTIFACT!");

		if(Tabs.getSelectedIndex() > 1)
			Tabs.setSelectedIndex(0);
	}

	public void derivateModeAction(java.awt.event.ActionEvent evt){
		MODE = DERIVATE_MODE;

		Tabs.setEnabledAt(0, false);
		Tabs.setEnabledAt(1, true);
		Tabs.setEnabledAt(2, true);
		Tabs.setEnabledAt(3, true);
		Tabs.setEnabledAt(4, true);

		conclusionList.setEnabled(false);
		conclusionBrowserButton.setEnabled(true);

		captureProvenanceButton.setLabel("DERIVE ARTIFACT!");

		if(Tabs.getSelectedIndex() == 0)
			Tabs.setSelectedIndex(1);
	}

	public void docDerivateModeAction(java.awt.event.ActionEvent evt){
		MODE = DOC_DERIVATE_MODE;

		Tabs.setEnabledAt(0, false);
		Tabs.setEnabledAt(1, true);
		Tabs.setEnabledAt(2, true);
		Tabs.setEnabledAt(3, true);
		Tabs.setEnabledAt(4, true);

		conclusionList.setEnabled(true);
		conclusionBrowserButton.setEnabled(false);

		if(Tabs.getSelectedIndex() == 0)
			Tabs.setSelectedIndex(1);

	}

	public void updateAntecedentList(java.awt.event.ActionEvent evt){

		if(!antecedentsByProjectCheckBox.isSelected()){
			if(CurrentlySelectedAntecedentsVector != null)
				CurrentlySelectedAntecedentsVector.clear();
			if(General_PMLJ_Nodes == null)
				General_PMLJ_Nodes = new PMLJList("ALL_PROJECTS_$!!$!").getPMLList();
			antecedentList.setModel(General_PMLJ_Nodes);		
			antecedentList.repaint();
		}else{
			refreshAntecedentUI();
		}
	}

	public void refreshAntecedentUI(){
		if(CurrentlySelectedAntecedentsVector != null)
			CurrentlySelectedAntecedentsVector.clear();
		Project_PMLJ_Nodes = new PMLJList(creds.getServerURL()).getPMLList();

		if(MODE == DOC_DERIVATE_MODE)
			conclusionList.setModel(Project_PMLJ_Nodes);

		antecedentList.setModel(Project_PMLJ_Nodes);		
		antecedentList.repaint();
	}

	public void refreshSourcesUI(){
		if(CurrentlySelectedSourcesVector != null)
			CurrentlySelectedSourcesVector.clear();
		SourcesPML_Nodes = new SourcesList().getSourceList();
		SourcesList.setModel(SourcesPML_Nodes);
		SourcesList.repaint();
	}

	public void refreshInferenceAgentUI(int sel){
		agentComboBox.queryAgents(sel);
		agentComboBox.repaint();
	}

	public void refreshFormatsUI(){
		conclusionFormatComboBox.queryFormats();
		conclusionFormatComboBox.repaint();
	}

	public void sortIABy(java.awt.event.ActionEvent evt){
		int selection = IATypeComboBox.getSelectedIndex();
		refreshInferenceAgentUI(selection);
	}

	public void filterByWDO(){

		System.out.println("filtering by: " + selectedOntologySTR);

		conclusionTypeComboBox.setOntology(selectedOntologySTR);
		conclusionTypeComboBox.queryInformationTypes();

		inferenceRuleComboBox.setOntology(selectedOntologySTR);
		inferenceRuleComboBox.queryInferenceRules();

		conclusionTypeComboBox.repaint();
		inferenceRuleComboBox.repaint();
	}

	public void changeIACheckStatus(java.awt.event.ActionEvent evt){
		IndividualComboBox.Individual agentInd = (IndividualComboBox.Individual) agentComboBox.getSelectedItem();
		if(!agentInd.getName().equalsIgnoreCase(" -- Choose Agent -- ")){
			Tabs.setIconAt(2, check);
			//inferenceRuleComboBox.queryInferenceRulesByInferenceEngine(agentInd.getURI());
		}else
			Tabs.setIconAt(2,uncheck);
	}
	public void changeIRCheckStatus(java.awt.event.ActionEvent evt){
		IndividualComboBox.Individual ruleInd = (IndividualComboBox.Individual) inferenceRuleComboBox.getSelectedItem();
		if(!ruleInd.getName().equalsIgnoreCase(" -- Choose Activity Method -- "))
			Tabs.setIconAt(3, check);
		else
			Tabs.setIconAt(3,uncheck);
	}

	public void changeConclusionCheckStatus(java.awt.event.ActionEvent evt){
		int count = 0;

		IndividualComboBox.Individual formatInd = (IndividualComboBox.Individual) conclusionFormatComboBox.getSelectedItem();
		IndividualComboBox.Individual typeInd = (IndividualComboBox.Individual) conclusionTypeComboBox.getSelectedItem();

		if(!formatInd.getName().equalsIgnoreCase(" -- Choose Format -- "))
			count++;

		if(!typeInd.getName().equalsIgnoreCase(" -- Choose Type -- "))
			count++;

		if(!conclusionBrowserTF.getText().isEmpty())
			count++;

		if(count == 3)
			Tabs.setIconAt(1, check);
		else
			Tabs.setIconAt(1,uncheck);
	}

	/**
	 * ASSERT ARTIFACT Event Call
	 * @param evt
	 */
	public void captureProvenanceAction(java.awt.event.ActionEvent evt){

		if(MODE == ASSERT_MODE){
			assertTask = new assertTask();
			assertTask.addPropertyChangeListener(this);
			assertTask.execute();
		}else if(MODE == DERIVATE_MODE){
			deriveTask = new deriveTask();
			deriveTask.addPropertyChangeListener(this);
			deriveTask.execute();
		}

	}

	public void assertArtifact(Vector<IndividualList.Individual> sources, String typeURI, String formatURI){

		setCursor(waiting);
		String newConclusion = null;

		try {
			AM = new AssertionMaker(creds);

			if(oFile != null){
				AM.setDataFilePath(oFile.getAbsolutePath(), false);
				AM.setFile(oFile);

			} else if(fromURLTF.getText().length() > 0){
				AM.setDataFilePath(fromURLTF.getText(), true);
			}

			//			AM.setUseSessionUser(IncludeUserCheckBox.isSelected());
			AM.setSources(sources);

			AM.setDocumentTypeURI(typeURI);
			AM.setDocumentFormatURI(formatURI);


			newConclusion = AM.generateAssertation();

			refreshAntecedentUI();

		} catch (Throwable e) {
			e.printStackTrace();
			String errMsg = "Assertion Error: " + e.toString();
			JOptionPane.showMessageDialog(this, errMsg, "Error", JOptionPane.ERROR_MESSAGE);

		} finally {
//			JOptionPane.showMessageDialog(null, "Assertion Complete.");
			setCursor(normal);
			if(selectedWorkflowSTR != null){
				Driver = new WorkflowDriver(instance, creds, selectedWorkflowSTR, selectedOntologySTR, newConclusion);
				Driver.setVisible(true);
			}
		}
	}

	public void closeAction(java.awt.event.ActionEvent evt){
		System.exit(0);
	}

	public void addSourceToolAction(java.awt.event.ActionEvent evt){
		if(AST == null){
			AST = new AddSourceTool(instance, creds);
		}
		AST.setVisible(true);
	}

	public void addAgentToolAction(java.awt.event.ActionEvent evt){
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		if(AAT == null){
			AAT = new AddAgentTool(instance, creds);
		}
		AAT.setVisible(true);
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	public void addFormatToolAction(java.awt.event.ActionEvent evt){
		if(AFT == null){
			if(creds != null) {
				AFT = new AddFormatTool(instance, creds);
			}
			AFT.addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosed(WindowEvent e) {
					AFT = null;
				}

			});
		}
		AFT.setVisible(true);
	}

	public void aggregateToolAction(java.awt.event.ActionEvent evt){
		new AggregaterTool().setVisible(true);
	}

	public void openOntologyToolAction(java.awt.event.ActionEvent evt){
		new OpenOntologyTool(instance).setVisible(true);
	}

	public void openWorkflowToolAction(java.awt.event.ActionEvent evt){
		new OpenWorkflowTool(instance, creds, Driver).setVisible(true);
	}

	public void openWorkflowViewAction(java.awt.event.ActionEvent evt){
		new WorkflowViewer(selectedWorkflowSTR).setVisible(true);
	}
	
	public void startWorkflowDriver(String workflow, String ontology, String conclusion){
		Driver = new WorkflowDriver(instance, creds, workflow, ontology, conclusion);
	}

	public void addSourceAction(java.awt.event.ActionEvent evt){
		Object CSS = SourcesList.getSelectedValue();
		if(CurrentlySelectedSourcesVector != null){
			if(!CurrentlySelectedSourcesVector.contains(CSS)){
				CurrentlySelectedSourcesVector.add((edu.utep.cybershare.DerivAUI.components.IndividualList.Individual) CSS);
			}
		}else{
			CurrentlySelectedSourcesVector = new Vector<edu.utep.cybershare.DerivAUI.components.IndividualList.Individual>();
			CurrentlySelectedSourcesVector.add((edu.utep.cybershare.DerivAUI.components.IndividualList.Individual) CSS);
		}

		Tabs.setIconAt(0, check);

		currentlySelectedSourcesList.setModel(CurrentlySelectedSourcesVector);
		currentlySelectedSourcesList.repaint();



	}

	public void removeSourceAction(java.awt.event.ActionEvent evt){
		Object CSS = currentlySelectedSourcesList.getSelectedValue();
		if(CurrentlySelectedSourcesVector != null){
			if(CurrentlySelectedSourcesVector.contains(CSS)){
				CurrentlySelectedSourcesVector.remove(CSS);
			}
		}

		if(CurrentlySelectedSourcesVector.isEmpty())
			Tabs.setIconAt(0, uncheck);

		currentlySelectedSourcesList.setModel(CurrentlySelectedSourcesVector);
		currentlySelectedSourcesList.repaint();
	}

	public void addAntecedentAction(java.awt.event.ActionEvent evt){
		Object CSA = antecedentList.getSelectedValue();
		if(CurrentlySelectedAntecedentsVector != null){
			if(!CurrentlySelectedAntecedentsVector.contains(CSA)){
				CurrentlySelectedAntecedentsVector.add((edu.utep.cybershare.DerivAUI.components.IndividualList.Individual) CSA);
			}
		}else{
			CurrentlySelectedAntecedentsVector = new Vector<edu.utep.cybershare.DerivAUI.components.IndividualList.Individual>();
			CurrentlySelectedAntecedentsVector.add((edu.utep.cybershare.DerivAUI.components.IndividualList.Individual) CSA);
		}

		Tabs.setIconAt(4, check);

		currentlySelectedAntecedentList.setModel(CurrentlySelectedAntecedentsVector);
		currentlySelectedAntecedentList.repaint();

	}

	public void removeAntecedentAction(java.awt.event.ActionEvent evt){
		Object CSA = currentlySelectedAntecedentList.getSelectedValue();
		if(CurrentlySelectedAntecedentsVector != null){
			if(CurrentlySelectedAntecedentsVector.contains(CSA)){
				CurrentlySelectedAntecedentsVector.remove(CSA);
			}
		}

		if(CurrentlySelectedAntecedentsVector.isEmpty())
			Tabs.setIconAt(4, uncheck);

		currentlySelectedAntecedentList.setModel(CurrentlySelectedAntecedentsVector);
		currentlySelectedAntecedentList.repaint();
	}

	/**
	 * DERIVATE ARTIFACT
	 * @param evt
	 */
	public void derivateAction(java.awt.event.ActionEvent evt){


	}

	public void derivateArtifact(String filepath, String formatURI, String typeURI, String InfEngine, String InfRule, Vector<IndividualList.Individual> ants){

		setCursor(waiting);
		String newConclusion = null;

		try {
			DM = new DerivationMaker(creds);

			//set conclusion information
			DM.setDataFilePath(filepath);
			DM.setFile(new File(filepath));

			DM.setConclusionFormatURI(formatURI);
			DM.setConclusionTypeURI(typeURI);

			//set derivation information
			DM.setAgentURI(InfEngine);
			DM.setInferenceRuleURI(InfRule);

			//set antecedents
			DM.setAntecedentURIs(ants);

			newConclusion = DM.generateDerivation();

			refreshAntecedentUI();

		} catch (Throwable e) {
			e.printStackTrace();
			String errMsg = "Derivation Error: " + e.toString();
			JOptionPane.showMessageDialog(this, errMsg, "Error", JOptionPane.ERROR_MESSAGE);

		} finally {
//			JOptionPane.showMessageDialog(null, "Derivation Complete.");
			setCursor(normal);
			if(selectedWorkflowSTR != null){
				Driver = new WorkflowDriver(instance, creds, selectedWorkflowSTR, selectedOntologySTR, newConclusion);
				Driver.setVisible(true);
			}
		}
	}

	public void browseAction(java.awt.event.ActionEvent evt){
		//Create a file chooser
		fc = new JFileChooser(oFile);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

		int returnVal = fc.showOpenDialog(DerivAUI.this);

		String file = null;
		if (returnVal == JFileChooser.APPROVE_OPTION) 
			file = fc.getSelectedFile().getAbsolutePath();

		conclusionBrowserTF.setText(file);
		oFile = fc.getSelectedFile();

	}

	/** CI-Server LOGIN
	 * The purpose of this method is not actually login to CI Server, but acquiring correct
	 * details about the login session (Server URL, valid username and password, and project Name).
	 */
	public void serverLogin() {

		String projectName = null;

		do{
			String path = CIGetResourceSaveLocationDialog.showDialog(this, this,"","pmlj"); //URI
			path = "http://rio.cs.utep.edu/ciserver/ciprojects/pmlj/";

			int CIServerID = CIKnownServerTable.getInstance().ciGetServerEntryFromURL(path);

			try {
				if(CIServerID != -1){


					creds.setUsername(CIKnownServerTable.getInstance().ciGetServerUsername(CIServerID));
					creds.setPassword(CIKnownServerTable.getInstance().ciGetServerPassword(CIServerID));

					CIClient client = new CIClient(CIServerID);

					projectName = CIGetProjectListDialog.showDialog(null, null, client);
					if(projectName!=null && !projectName.isEmpty()){
						creds.setProject(projectName);
					}else{
						JOptionPane.showMessageDialog(this, "Bad Username or Password", "Error", JOptionPane.ERROR_MESSAGE);
						projectName = null;
					}

					creds.setServerURL(path.substring(0, path.lastIndexOf('/') - 4));

				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (XmlRpcException e) {
				e.printStackTrace();
			}
		}while(projectName == null);
		initComponents();

		selectedServerLabel.setText(creds.getServerURL());
		selectedProjectLabel.setText(creds.getProject());

		initTripleStoreComponents();

	}

	/**
	 * MAIN METHOD
	 * @param args
	 */
	public static void main(String args[]) {

		try {
			// Set cross-platform Java L&F (also called "Metal")
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch (UnsupportedLookAndFeelException e) {
			// handle exception
		}
		catch (ClassNotFoundException e) {
			// handle exception
		}
		catch (InstantiationException e) {
			// handle exception
		}
		catch (IllegalAccessException e) {
			// handle exception
		}


		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				instance = new DerivAUI();
			}
		});
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			ProgressBar.setValue(progress);
		} 
	}

	class MyDocumentListener implements DocumentListener {
		String newline = "\n";

		public void insertUpdate(DocumentEvent ev) {
			int count = 0;

			IndividualComboBox.Individual formatInd = (IndividualComboBox.Individual) conclusionFormatComboBox.getSelectedItem();
			IndividualComboBox.Individual typeInd = (IndividualComboBox.Individual) conclusionTypeComboBox.getSelectedItem();

			if(!formatInd.getName().equalsIgnoreCase(" -- Choose Format -- "))
				count++;

			if(!typeInd.getName().equalsIgnoreCase(" -- Choose Type -- "))
				count++;

			if(!conclusionBrowserTF.getText().isEmpty())
				count++;

			if(count == 3)
				Tabs.setIconAt(1, check);
			else
				Tabs.setIconAt(1,uncheck);
		}

		public void removeUpdate(DocumentEvent ev) {
			int count = 0;

			IndividualComboBox.Individual formatInd = (IndividualComboBox.Individual) conclusionFormatComboBox.getSelectedItem();
			IndividualComboBox.Individual typeInd = (IndividualComboBox.Individual) conclusionTypeComboBox.getSelectedItem();

			if(!formatInd.getName().equalsIgnoreCase(" -- Choose Format -- "))
				count++;

			if(!typeInd.getName().equalsIgnoreCase(" -- Choose Type -- "))
				count++;

			if(!conclusionBrowserTF.getText().isEmpty())
				count++;

			if(count == 3)
				Tabs.setIconAt(1, check);
			else
				Tabs.setIconAt(1,uncheck);
		}

		public void changedUpdate(DocumentEvent ev) {
		}
	}

	class LoadTask extends SwingWorker<Void, Void> {
		/*
		 * Main task. Executed in background thread.
		 */
		@Override
		public Void doInBackground() {

			ProgressBar.setString("Loading Sources");
			SourcesPML_Nodes = new SourcesList().getSourceList();
			SourcesList.setModel(SourcesPML_Nodes);
			setProgress(15);

			ProgressBar.setString("Loading Data Formats");
			conclusionFormatComboBox.queryFormats();
			setProgress(25);

			ProgressBar.setString("Loading Data Types");
			conclusionTypeComboBox.queryInformationTypes();
			setProgress(35);

			ProgressBar.setString("Loading Agents");
			agentComboBox.queryAgents(0);
			setProgress(50);

			ProgressBar.setString("Loading Activity Methods");
			inferenceRuleComboBox.queryInferenceRules();
			setProgress(65);

			ProgressBar.setString("Loading Antecedents");
			setProgress(79);

			Project_PMLJ_Nodes = new PMLJList("ALL_PROJECTS_$!!$!").getPMLList();
			antecedentList.setModel(Project_PMLJ_Nodes);
			ProgressBar.setString("Loading Conclusions");
			setProgress(89);

			conclusionList.setModel(Project_PMLJ_Nodes);
			conclusionList.setEnabled(false);
			setProgress(100);
			return null;
		}

		/*
		 * Executed in event dispatching thread
		 */
		@Override
		public void done() {
			setProgress(100);
			setCursor(normal); //turn off the wait cursor
			ProgressBar.setString("Fully Loaded!");
			ProgressBar.setStringPainted(true);
		}
	}

	class deriveTask extends SwingWorker<Void, Void> {
		/*
		 * Main task. Executed in background thread.
		 */
		@Override
		public Void doInBackground() {

			ProgressBar.setString("Please Wait for Derivation");
			ProgressBar.setIndeterminate(true);

			IndividualComboBox.Individual docFormatInd = (IndividualComboBox.Individual) conclusionFormatComboBox.getSelectedItem();
			IndividualComboBox.Individual docTypeInd = (IndividualComboBox.Individual) conclusionTypeComboBox.getSelectedItem();
			IndividualComboBox.Individual agentInd = (IndividualComboBox.Individual) agentComboBox.getSelectedItem();
			IndividualComboBox.Individual IRInd = (IndividualComboBox.Individual) inferenceRuleComboBox.getSelectedItem();

			derivateArtifact(conclusionBrowserTF.getText(), docFormatInd.getURI(),docTypeInd.getURI(),agentInd.getURI(),IRInd.getURI(),CurrentlySelectedAntecedentsVector);


			return null;
		}

		/* (non-Javadoc)
		 * @see javax.swing.SwingWorker#done()
		 */
		@Override
		public void done() {
			setProgress(100);
			ProgressBar.setIndeterminate(false);
			setCursor(normal); //turn off the wait cursor
			ProgressBar.setString("Derivation Complete!");
			ProgressBar.setStringPainted(true);
		}
	}

	class assertTask extends SwingWorker<Void, Void> {
		/*
		 * Main task. Executed in background thread.
		 */
		@Override
		public Void doInBackground() {

			ProgressBar.setString("Please Wait for Assertion");
			ProgressBar.setIndeterminate(true);

			IndividualComboBox.Individual docTypeInd = (IndividualComboBox.Individual) conclusionTypeComboBox.getSelectedItem();
			IndividualComboBox.Individual docFormatInd = (IndividualComboBox.Individual) conclusionFormatComboBox.getSelectedItem();

			assertArtifact(CurrentlySelectedSourcesVector,docTypeInd.getURI(),docFormatInd.getURI());

			return null;
		}

		/* (non-Javadoc)
		 * @see javax.swing.SwingWorker#done()
		 */
		@Override
		public void done() {
			setProgress(100);
			ProgressBar.setIndeterminate(false);
			setCursor(normal); //turn off the wait cursor
			ProgressBar.setString("Assertion Complete!");
			ProgressBar.setStringPainted(true);
		}
	}

}
