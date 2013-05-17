package edu.utep.cybershare.DerivAUI.tools;

import java.awt.Cursor;
import javax.swing.JOptionPane;
import edu.utep.trust.provenance.RDFAggregater;
import edu.utep.trust.provenance.RDFAggregater_Service;

/**
 * aggregaterTool.java
 * Created on Mar 15, 2011, 11:08:20 AM
 * @author Antonio Garza
 */

public class AggregaterTool extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;
	/** Creates new form aggregaterTool */
    public AggregaterTool() {
        initComponents();
    }

    private void initComponents() {

        enterURILabel = new javax.swing.JLabel();
        URITF = new javax.swing.JTextField();
        cancelButton = new javax.swing.JButton();
        submitButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add to the Triple Store");

        enterURILabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        enterURILabel.setText("Enter Full URI");

        URITF.setText("http://");

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
                    .addComponent(URITF, javax.swing.GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE)
                    .addComponent(enterURILabel)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(submitButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(enterURILabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(URITF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(submitButton))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(this);
    }
    
	public void submitAction(java.awt.event.ActionEvent evt){
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		String URI = URITF.getText();
		
		RDFAggregater_Service Service = new RDFAggregater_Service();
		RDFAggregater proxy = Service.getRDFAggregaterHttpPort();
		String result = "";
		int intents = 0;
		while(!result.equalsIgnoreCase("SUCCESS") && intents < 3){
			result = proxy.addDocumentAt(URI, "generic@generic.com");
			intents++;
		}

		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		
		if(result.equalsIgnoreCase("SUCCESS")){
			JOptionPane.showMessageDialog(null, "Upload Successful");
		}else{
			JOptionPane.showMessageDialog(null, "Aggregation Failed");
		}
		
	}
	
	public void cancelAction(java.awt.event.ActionEvent evt){
		dispose();
	}

    // Variables declaration - do not modify
    private javax.swing.JTextField URITF;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel enterURILabel;
    private javax.swing.JButton submitButton;
    // End of variables declaration

}
