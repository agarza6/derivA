package edu.utep.cybershare.DerivAUI.tools;

import java.awt.Cursor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.swing.JOptionPane;


import edu.utep.cybershare.DerivA.util.CIServerDump;
import edu.utep.cybershare.DerivA.util.NodeSetBuilder;
import edu.utep.cybershare.DerivA.util.ServerCredentials;
import edu.utep.cybershare.DerivAUI.DerivAUI;
import edu.utep.trust.provenance.RDFAggregater;
import edu.utep.trust.provenance.RDFAggregater_Service;

public class AddFormatTool extends javax.swing.JFrame {

	private javax.swing.JButton CancelButton;
	private javax.swing.JButton SubmitButton;
	private javax.swing.JLabel formatDescLabel;
	private javax.swing.JTextField formatDescTF;
	private javax.swing.JLabel formatNameLabel;
	private javax.swing.JTextField formatNameTF;
	private javax.swing.JLabel mainLabel;

	private ServerCredentials creds;
	private DerivAUI instance; 

	public AddFormatTool(DerivAUI inst, ServerCredentials sc) {
		creds = sc;
		instance = inst;
		initComponents();
	}

	private void initComponents() {

		mainLabel = new javax.swing.JLabel();
		formatNameLabel = new javax.swing.JLabel();
		formatDescLabel = new javax.swing.JLabel();
		formatNameTF = new javax.swing.JTextField();
		formatDescTF = new javax.swing.JTextField();
		CancelButton = new javax.swing.JButton();
		SubmitButton = new javax.swing.JButton();

		setTitle("derivA - Add New Format Tool");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		mainLabel.setText("Add New Format");

		formatNameLabel.setText("Name: ");

		formatDescLabel.setText("URL of Description");

		CancelButton.setText("Cancel");
		CancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				CancelButtonActionPerformed(evt);
			}
		});

		SubmitButton.setText("Submit");
		SubmitButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SubmitButtonActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup()
										.addComponent(formatNameLabel)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(formatNameTF))
										.addGroup(layout.createSequentialGroup()
												.addComponent(mainLabel)
												.addGap(0, 0, Short.MAX_VALUE))
												.addGroup(layout.createSequentialGroup()
														.addComponent(formatDescLabel)
														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(formatDescTF))
														.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
																.addGap(0, 244, Short.MAX_VALUE)
																.addComponent(SubmitButton)
																.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																.addComponent(CancelButton)))
																.addContainerGap())
				);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(mainLabel)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(formatNameLabel)
								.addComponent(formatNameTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(formatDescLabel)
										.addComponent(formatDescTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(CancelButton)
												.addComponent(SubmitButton))
												.addContainerGap())
				);

		pack();
		setLocationRelativeTo(instance);
	}

	private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
		dispose();
	}

	private void SubmitButtonActionPerformed(java.awt.event.ActionEvent evt) {

		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
		try {
			//fetch all info
			String name = formatNameTF.getText();
			String description = formatDescTF.getText();

			String filename = name;
			filename = filename.replaceAll("[*<>\\[\\]\\+\",]", "-");
			filename = filename.replaceAll(" ", "_");
			filename = filename.replaceAll("&#xD;", "");
			filename = filename.replaceAll("&#xA;", "");

			String pmlP = getFormatPML(creds.getServerURL() + "pmlp/" + name + ".owl", name, description);

			//Upload data file to CI-Server
			CIServerDump uploader = new CIServerDump(creds.getServerURL() + "pmlp/", creds.getUsername(), creds.getPassword());
			byte[] resource_bytes = pmlP.getBytes();
			String artifactURI = uploader.savePMLPToCIServer(name + ".owl", creds.getProject(), resource_bytes, true);

			System.out.println(pmlP);
			
			//Aggregate to a Triple Store HERE
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

			instance.refreshFormatsUI();

		} catch (Throwable e) {
			e.printStackTrace();
			String errMsg = "Error adding agent: " + e.toString();
			JOptionPane.showMessageDialog(this, errMsg, "Error", JOptionPane.ERROR_MESSAGE);

		} finally {
			JOptionPane.showMessageDialog(null, "Format Created.");
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

	private String getFormatPML(String path, String name, String desc){

		path = path.trim();
		path = path.replaceAll("[*<>\\[\\]\\+\",]", "-");
		path = path.replaceAll("(\\r|\\n)", "");
		path = path.replaceAll(" ", "_");
		path = path.replaceAll("&#xD;", "");
		path = path.replaceAll("&#xA;", "");
		
		String pmlp = "<rdf:RDF" + '\n';
		pmlp = pmlp + "\t" + "xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"" + '\n';
		pmlp = pmlp + "\t" + "xmlns:owl=\"http://www.w3.org/2002/07/owl#\"" + '\n';
		pmlp = pmlp + "\t" + "xmlns:pmlp=\"http://inference-web.org/2.0/pml-provenance.owl#\"" + '\n';
		pmlp = pmlp + "\t" + "xmlns:daml=\"http://www.daml.org/2001/03/daml+oil#\"" + '\n';
		pmlp = pmlp + "\t" + "xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"" + '\n';
		pmlp = pmlp + "\t" + "xmlns:ds=\"http://inference-web.org/2.0/ds.owl#\">" + '\n';
		pmlp = pmlp + "\t" + "<pmlp:Format rdf:about=\"" + path + "#" + name + "\">" + '\n';
		pmlp = pmlp + "\t\t" + "<pmlp:hasDescription>" + '\n';
		pmlp = pmlp + "\t\t\t" + "<pmlp:Information>" + '\n';
		pmlp = pmlp + "\t\t\t\t" + "<pmlp:hasLanguage rdf:resource=\"http://inference-web.org/registry/LG/English.owl#English\"/>" + '\n';
		pmlp = pmlp + "\t\t\t\t" + "<pmlp:hasURL rdf:datatype=\"http://www.w3.org/2001/XMLSchema#anyURI\">" + desc + "</pmlp:hasURL>" + '\n';
		pmlp = pmlp + "\t\t\t\t" + "</pmlp:Information>" + '\n';
		pmlp = pmlp + "\t\t\t\t" + "</pmlp:hasDescription>" + '\n';
		pmlp = pmlp + "\t\t\t" + "<pmlp:hasName rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\">" + name + "</pmlp:hasName>" + '\n';
		pmlp = pmlp + "\t" + "</pmlp:Format>" + '\n';
		pmlp = pmlp + "</rdf:RDF>";

		return pmlp;

	}

}
