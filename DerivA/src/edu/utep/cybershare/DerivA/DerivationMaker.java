package edu.utep.cybershare.DerivA;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.JOptionPane;

import edu.utep.cybershare.DerivA.util.*;
import edu.utep.cybershare.DerivAUI.components.IndividualList.Individual;

public class DerivationMaker {
	

	private ServerCredentials creds;
	private String dataFilePath;
	private File file;
	
	private String agentURI, inferenceRuleURI;
	private String conclusionURI, conclusionFormatURI, conclusionTypeURI;
	private Vector<Individual> antecedentURIs;
	
	public static final String DATE_FORMAT_NOW = "YYYY-MM-DDTHH:MM:SSZ";
	
	public void setAgentURI(String URI){agentURI = URI;}
	public void setInferenceRuleURI(String URI){inferenceRuleURI = URI;}
	public void setConclusionURI(String URI){conclusionURI = URI;}
	public void setConclusionFormatURI(String frmat){conclusionFormatURI = frmat;}
	public void setConclusionTypeURI(String type){conclusionTypeURI = type;}
	public void setAntecedentURIs(Vector<Individual> URIs){antecedentURIs = URIs;}
	
	public void setDataFilePath(String path){dataFilePath = path;}
	public void setFile(File f){file = f;}
	
	public DerivationMaker(){}
	public DerivationMaker(ServerCredentials sc){
		creds = sc;
	}
	
	public String getDateTime() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}
	
	public String generateDerivation(){
		
		NodeSetBuilder NSB = new NodeSetBuilder(creds);
		
		//Upload data file to CI-Server
		CIServerDump uploader = new CIServerDump(creds.getServerURL() + "udata/", creds.getUsername(), creds.getPassword());
		
		FTPServerUploader FTP = new FTPServerUploader();	
		conclusionURI = FTP.uploadFile(dataFilePath);
		
//		byte[] resource_bytes = null;
//
//		//			contents = GetURLContents.downloadText("file:\\"+dataFilePath);
//		try {
//			resource_bytes = CIUtils.ciGetBytesFromFile(file);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		String dataFileName = dataFilePath.substring(dataFilePath.lastIndexOf('\\') + 1);
		
//		conclusionURI = uploader.saveDataToCIServer(dataFileName, creds.getProject(), resource_bytes, true);
		
		NSB.artifactURI = conclusionURI;
		NSB.formatURI = conclusionFormatURI;
		NSB.docTypeURI = conclusionTypeURI;
		
		NSB.antecedentsURIs = antecedentURIs;
		
		NSB.IRURI = inferenceRuleURI;
		NSB.agentURI = agentURI;
		NSB.fileName = dataFileName;
		
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
			JOptionPane.showMessageDialog(null, "Derivation: Upload Successful \n " + pmljURI);
		}else{
			JOptionPane.showMessageDialog(null, "Aggregation Failed");
		}
		
		return pmljURI;
	}
	
}
