package edu.utep.cybershare.DerivAUI.tools;

import java.awt.Cursor;
import java.util.Vector;

import javax.swing.JOptionPane;

import edu.utep.cybershare.DerivA.util.CIServerDump;
import edu.utep.cybershare.DerivA.util.NodeSetBuilder;
import edu.utep.cybershare.DerivA.util.ServerCredentials;
import edu.utep.cybershare.DerivAUI.DerivAUI;
import edu.utep.cybershare.DerivAUI.components.IndividualList;
import edu.utep.cybershare.DerivAUI.components.InferenceRulesList;
import edu.utep.cybershare.DerivAUI.components.IndividualList.Individual;
import edu.utep.trust.provenance.RDFAggregater;
import edu.utep.trust.provenance.RDFAggregater_Service;


@SuppressWarnings("serial")
public class AddAgentTool extends javax.swing.JFrame {

	// Variables declaration - do not modify                     
    private javax.swing.JLabel addAgentLabel;
    private javax.swing.JButton addButton;
    private javax.swing.JLabel addIRLabel;
    private javax.swing.JLabel addNameLabel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JTextField descriptionTF;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField nameTF;
    private javax.swing.JButton removeButton;
    private javax.swing.JButton submitButton;
	
	private IndividualList Avail_IR;
	private IndividualList Selected_IR;
	private Vector<edu.utep.cybershare.DerivAUI.components.IndividualList.Individual> Avail_IRVector;
	private Vector<edu.utep.cybershare.DerivAUI.components.IndividualList.Individual> Selected_IRVector;
	public Vector<Individual> inferenceRuleVector;
	
	private DerivAUI instance;
	private ServerCredentials creds;
	// End of variables declaration         
	
	/** Creates new form addNewAgentTool */
	public AddAgentTool(DerivAUI inst, ServerCredentials sc) {
    	creds = sc;
    	instance = inst;
		initComponents();
	}

	private void initComponents() {

		addAgentLabel = new javax.swing.JLabel();
		jScrollPane1 = new javax.swing.JScrollPane();
		jScrollPane2 = new javax.swing.JScrollPane();
		addButton = new javax.swing.JButton();
		removeButton = new javax.swing.JButton();
		addIRLabel = new javax.swing.JLabel();
		addNameLabel = new javax.swing.JLabel();
		nameTF = new javax.swing.JTextField();
		cancelButton = new javax.swing.JButton();
		submitButton = new javax.swing.JButton();
        descriptionLabel = new javax.swing.JLabel();
        descriptionTF = new javax.swing.JTextField();
		
        Avail_IRVector = new InferenceRulesList().getPMLList();
        Selected_IRVector = new Vector<edu.utep.cybershare.DerivAUI.components.IndividualList.Individual>();
		
		Avail_IR = new IndividualList();
		Selected_IR = new IndividualList();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Create Actor Tool");

		addAgentLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
		addAgentLabel.setText("Add New Actor");

		jScrollPane1.setViewportView(Avail_IR);
		Avail_IR.setModel(Avail_IRVector);

		jScrollPane2.setViewportView(Selected_IR);

		addButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("../images/001_01.gif"))); // NOI18N
		addButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addAction(evt);
			}
		});

		removeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("../images/001_02.gif"))); // NOI18N
		removeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				removeAction(evt);
			}
		});
		
		addIRLabel.setText("Add Plan/Metho(s) to Actor");

		addNameLabel.setText("Actor Name:");
        descriptionLabel.setText("Description:");

		cancelButton.setText("Close");
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addAgentLabel)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addNameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nameTF, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(submitButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addComponent(addIRLabel)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addButton)
                            .addComponent(removeButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(descriptionLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(descriptionTF, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addAgentLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addNameLabel)
                    .addComponent(nameTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(descriptionLabel)
                    .addComponent(descriptionTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addIRLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cancelButton)
                                    .addComponent(submitButton)))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(addButton)
                        .addGap(18, 18, 18)
                        .addComponent(removeButton)))
                .addContainerGap())
        );
        pack();
		setLocationRelativeTo(this);
	}                       

	public void addAction(java.awt.event.ActionEvent evt){
		Object CSS = Avail_IR.getSelectedValue();
		if(Selected_IRVector != null){
			if(!Selected_IRVector.contains(CSS)){
				Selected_IRVector.add((edu.utep.cybershare.DerivAUI.components.IndividualList.Individual) CSS);
			}
		}else{
			Selected_IRVector = new Vector<edu.utep.cybershare.DerivAUI.components.IndividualList.Individual>();
			Selected_IRVector.add((edu.utep.cybershare.DerivAUI.components.IndividualList.Individual) CSS);
		}

		Selected_IR.setModel(Selected_IRVector);
		Selected_IR.repaint();
	}
	
	public void removeAction(java.awt.event.ActionEvent evt){
		Object CSA = Selected_IR.getSelectedValue();
		if(Selected_IRVector != null){
			if(Selected_IRVector.contains(CSA)){
				Selected_IRVector.remove(CSA);
			}
		}
		Selected_IR.setModel(Selected_IRVector);
		Selected_IR.repaint();
	}
	
	public void cancelAction(java.awt.event.ActionEvent evt){
		dispose();
	}
	
	public void submitAction(java.awt.event.ActionEvent evt){
		
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
		//fetch all info
		String fullName = nameTF.getText();
		String description = descriptionTF.getText();
		
		NodeSetBuilder NSB = new NodeSetBuilder();
		NSB.inferenceRuleVector = Selected_IRVector;
		String AgentString = NSB.createAgent("http://someuri.owl#uri", fullName, description, creds.getServerURL() + "pmlp/" + fullName);
		
		//Upload data file to CI-Server
		CIServerDump uploader = new CIServerDump(creds.getServerURL() + "pmlp/", creds.getUsername(), creds.getPassword());
		byte[] resource_bytes = AgentString.getBytes();
		String artifactURI = uploader.savePMLPToCIServer(fullName + ".owl", creds.getProject(), resource_bytes, false);
		
		
		RDFAggregater_Service Service = new RDFAggregater_Service();
		RDFAggregater proxy = Service.getRDFAggregaterHttpPort();
		String result = "";
		int intents = 0;
		while(!result.equalsIgnoreCase("SUCCESS") && intents < 3){
			result = proxy.addDocumentAt(artifactURI, uploader.getUserEmail());
			intents++;
		}

		if(result.equalsIgnoreCase("SUCCESS")){
			JOptionPane.showMessageDialog(null, "Upload Successful");
		}else{
			JOptionPane.showMessageDialog(null, "Aggregation Failed");
		}
		
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
}

