package edu.utep.cybershare.DerivA;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.JOptionPane;

import pml.dumping.writer.NodesetWriter;
import edu.utep.cybershare.DerivA.util.*;
import edu.utep.cybershare.DerivAUI.components.IndividualList.Individual;
import edu.utep.cybershare.ciclient.CIUtils;

public class DerivationMaker {
	
	private String password, username;
	private String CIProject;
	private String CIServerPath;
	private String dataFilePath;
	private File file;
	
	private String agentURI, inferenceRuleURI;
	private String conclusionURI, conclusionFormatURI, conclusionTypeURI;
	private Vector<Individual> antecedentURIs;
	
	public static final String DATE_FORMAT_NOW = "YYYY-MM-DDTHH:MM:SSZ";
	
	public void setUsername(String name){username = name;}
	public void setUserPassword(String pass){password = pass;}
	public void setCIProjectName(String name){CIProject = name;}
	public void setCIServerPath(String path){CIServerPath = path;}
	
	public void setAgentURI(String URI){agentURI = URI;}
	public void setInferenceRuleURI(String URI){inferenceRuleURI = URI;}
	public void setConclusionURI(String URI){conclusionURI = URI;}
	public void setConclusionFormatURI(String frmat){conclusionFormatURI = frmat;}
	public void setConclusionTypeURI(String type){conclusionTypeURI = type;}
	public void setAntecedentURIs(Vector<Individual> URIs){antecedentURIs = URIs;}
	
	public void setDataFilePath(String path){dataFilePath = path;}
	public void setFile(File f){file = f;}
	
	
	public String getDateTime() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}
	
	public void generateDerivation(){
		
		NodeSetBuilder NSB = new NodeSetBuilder();
		
		//Upload data file to CI-Server
		CIServerDump uploader = new CIServerDump(CIServerPath + "udata/", username, password);
		byte[] resource_bytes = null;

		//			contents = GetURLContents.downloadText("file:\\"+dataFilePath);
		try {
			resource_bytes = CIUtils.ciGetBytesFromFile(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String dataFileName = dataFilePath.substring(dataFilePath.lastIndexOf('\\') + 1);
		
		conclusionURI = uploader.saveDataToCIServer(dataFileName, CIProject, resource_bytes, true);

		System.out.println("conclusionURI: " + conclusionURI);
		System.out.println("conclusionFormatURI: " + conclusionFormatURI);
		
		NSB.projectName = CIProject;
		NSB.artifactURI = conclusionURI;
		NSB.formatURI = conclusionFormatURI;
		NSB.docTypeURI = conclusionTypeURI;
		
		NSB.antecedentsURIs = antecedentURIs;
		
		NSB.IRURI = inferenceRuleURI;
		NSB.agentURI = agentURI;
		NSB.fileName = dataFileName;
		NSB.ServerURL = CIServerPath + "pmlj/";
		
		String pmljURI = NSB.derivateArtifact();
		
		edu.utep.trust.provenance.RDFAggregater_Service Service = new edu.utep.trust.provenance.RDFAggregater_Service();
		edu.utep.trust.provenance.RDFAggregater proxy = Service.getRDFAggregaterHttpPort();
		String result = "";
		int intents = 0;
		while(!result.equalsIgnoreCase("SUCCESS") && intents < 3){
			result = proxy.addDocumentAt(pmljURI, uploader.getUserEmail());
			intents++;
		}

		if(result.equalsIgnoreCase("SUCCESS")){
			JOptionPane.showMessageDialog(null, "Upload Successful");
		}else{
			JOptionPane.showMessageDialog(null, "Aggregation Failed");
		}
	}
	
}
