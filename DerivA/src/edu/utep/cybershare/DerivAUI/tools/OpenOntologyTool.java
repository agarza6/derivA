package edu.utep.cybershare.DerivAUI.tools;

import java.awt.Cursor;
import java.io.File;
import javax.swing.JFileChooser;

import edu.utep.cybershare.DerivAUI.DerivAUI;
import edu.utep.cybershare.DerivAUI.components.*;

/**
 * openOntology.java
 * @ Author Antonio Garza
 * Created on Mar 15, 2011, 11:16:24 AM
 */
public class OpenOntologyTool extends javax.swing.JFrame {

    /** Creates new form openOntology */
    public OpenOntologyTool(DerivAUI inst) {
    	instance = inst;
        initComponents();
    }

    private void initComponents() {

        ButtonGroup = new javax.swing.ButtonGroup();
        openOntologyLabel = new javax.swing.JLabel();
        fromLocalRadioButton = new javax.swing.JRadioButton();
        browseButton = new javax.swing.JButton();
        LocalTF = new javax.swing.JTextField();
        fromCIServerRadioButton = new javax.swing.JRadioButton();
        OntComboBox = new OntologyComboBox("");
        cancelButton = new javax.swing.JButton();
        submitButton = new javax.swing.JButton();
        fromURIRadioButton = new javax.swing.JRadioButton();
        URITF = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Open Ontology");

        openOntologyLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        openOntologyLabel.setText("Open Ontology");

        fromLocalRadioButton.setText("From Local File System");
        fromLocalRadioButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				fromLocalAction(evt);
			}
		});

        browseButton.setText("Browse");
        browseButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				browseAction(evt);
			}
		});

        fromCIServerRadioButton.setText("From CI-Server");
        fromCIServerRadioButton.setSelected(true);
        fromCIServerRadioButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				fromLocalAction(evt);
			}
		});

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelAction(evt);
			}
		});

        submitButton.setText("Submit");
        submitButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				submitAction(evt);
			}
		});

        fromURIRadioButton.setText("From URI");
        fromURIRadioButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				fromLocalAction(evt);
			}
		});

        URITF.setText("http://");
        
        ButtonGroup.add(fromCIServerRadioButton);
        ButtonGroup.add(fromLocalRadioButton);
        ButtonGroup.add(fromURIRadioButton);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(OntComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(fromCIServerRadioButton)
                    .addComponent(fromLocalRadioButton)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(URITF, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE))
                    .addComponent(fromURIRadioButton)
                    .addComponent(openOntologyLabel)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(submitButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(browseButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LocalTF, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(openOntologyLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(fromURIRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(URITF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(fromLocalRadioButton)
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(browseButton)
                    .addComponent(LocalTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fromCIServerRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(OntComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(submitButton))
                .addContainerGap())
        );

	    LocalTF.setEnabled(false);
	    OntComboBox.setEnabled(true);
	    URITF.setEnabled(false);
	    browseButton.setEnabled(false);
        
        pack();
        setLocationRelativeTo(null);
    }
    
	public void fromLocalAction(java.awt.event.ActionEvent evt){
//	    LocalTF.setEnabled(true);
//	    OntologyComboBox.setEnabled(false);
//	    URITF.setEnabled(false);
//	    browseButton.setEnabled(true);
	}
	
	public void fromServerAction(java.awt.event.ActionEvent evt){
	    LocalTF.setEnabled(false);
	    OntComboBox.setEnabled(true);
	    URITF.setEnabled(false);
	    browseButton.setEnabled(false);
	}
	
	public void fromURIAction(java.awt.event.ActionEvent evt){
//	    LocalTF.setEnabled(false);
//	    OntologyComboBox.setEnabled(false);
//	    URITF.setEnabled(true);
//	    browseButton.setEnabled(false);
	}

	public void submitAction(java.awt.event.ActionEvent evt){
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
		IndividualComboBox.Individual Ontology = (IndividualComboBox.Individual) OntComboBox.getSelectedItem();
		
		instance.setSelectedOntology(Ontology.getURI());
		instance.filterByWDO();
		setVisible(false);
		
		
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
    
	public void cancelAction(java.awt.event.ActionEvent evt){
		dispose();
	}
    
	public void browseAction(java.awt.event.ActionEvent evt){
		//Create a file chooser
		fc = new JFileChooser(oFile);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

		int returnVal = fc.showOpenDialog(OpenOntologyTool.this);

		String file = null;
		if (returnVal == JFileChooser.APPROVE_OPTION) 
			file = fc.getSelectedFile().getAbsolutePath();

		LocalTF.setText(file);
		oFile = fc.getSelectedFile();
	}
    
    // Variables declaration - do not modify
	private DerivAUI instance;
	
    private javax.swing.ButtonGroup ButtonGroup;
    private javax.swing.JTextField LocalTF;
    private OntologyComboBox OntComboBox;
    private javax.swing.JTextField URITF;
    private javax.swing.JButton browseButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JRadioButton fromCIServerRadioButton;
    private javax.swing.JRadioButton fromLocalRadioButton;
    private javax.swing.JRadioButton fromURIRadioButton;
    private javax.swing.JLabel openOntologyLabel;
    private javax.swing.JButton submitButton;
    
	private JFileChooser fc;
	private File oFile;
    // End of variables declaration

}

