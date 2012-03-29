package edu.utep.cybershare.DerivA;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;
import java.net.URLEncoder;

import javax.swing.JOptionPane;

import edu.utep.trust.provenance.*;
import edu.utep.cybershare.ciclient.CIUtils;
import edu.utep.cybershare.DerivA.util.*;
import edu.utep.cybershare.DerivAUI.components.IndividualList.Individual;

public class AssertionMaker {

	private String dataFilePath;
	private File file;

	private boolean conclusionFromURL;
	private String artifactURI;
	private String pmljURI;
	private String docTypeURI;
	private String formatURI;
	private String docTypeLabel;
	private String[] sources;

	private boolean useSessionUser = false;
	private String password, username;
	private String CIProject;
	private String CIServerPath;

	public static final String DATE_FORMAT_NOW = "YYYY-MM-DDTHH:MM:SSZ";

	public void setDataFilePath(String path){dataFilePath = path;}
	public void setDataFilePath(String path, boolean from){dataFilePath = path; conclusionFromURL = from;}
	public void setArtifactURI(String uri){artifactURI = uri;}
	public void setPMLJ_URI(String uri){pmljURI = uri;}
	public void setDocumentTypeURI(String uri){docTypeURI = uri;}
	public void setDocumentFormatURI(String uri){formatURI = uri;}
	public void setDocumentTypeLabel(String label){docTypeLabel = label;}
	public void setUsername(String name){username = name;}
	public void setUserPassword(String pass){password = pass;}
	public void setCIProjectName(String name){CIProject = name;}
	public void setCIServerPath(String path){CIServerPath = path;}
	public void setFile(File f){file = f;}
	public void setUseSessionUser(boolean b){useSessionUser = b;}
	public void setSources(Vector<edu.utep.cybershare.DerivAUI.components.IndividualList.Individual> temp){
		if(temp != null){
			int size = temp.size();
			int counter = 0;
			if(useSessionUser)
				size++;
			sources = new String[size];

			for(Iterator<Individual> iter = temp.iterator(); iter.hasNext();){
				edu.utep.cybershare.DerivAUI.components.IndividualList.Individual ind = (Individual) iter.next();
				sources[counter] = ind.getURI();
				counter++;
			}
		}else{
			if(useSessionUser)
				sources = new String[1];
		}

	}

	public String getDateTime() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}

	public void generateAcertation(){

		NodeSetBuilder NSB = new NodeSetBuilder();

		String dataFileName = "";
		CIServerDump uploader = new CIServerDump(CIServerPath + "udata/", username, password);
		
		System.out.println("conclusion from URL: " + conclusionFromURL);
	

		if(!conclusionFromURL){
			//Upload data file to CI-Server
			byte[] resource_bytes = null;

			try {
				resource_bytes = CIUtils.ciGetBytesFromFile(file);
			} catch (IOException e) {
				e.printStackTrace();
			}

			dataFileName = dataFilePath.substring(dataFilePath.lastIndexOf('\\') + 1);
			artifactURI = uploader.saveDataToCIServer(dataFileName, CIProject, resource_bytes, true);
			

		}else{
			artifactURI = dataFilePath;
			dataFileName = dataFilePath.replace("http","");
			dataFileName = dataFileName.replace("://", "");
			if(dataFileName.lastIndexOf('/') == (dataFileName.length() - 1)){
				dataFileName = dataFileName.substring(0, dataFileName.length() - 1);
			}
			dataFileName = dataFileName.replace("/", "_slash_");
		}

		if(useSessionUser){
			sources[sources.length - 1] = uploader.getUserInformation();
		}

		NSB.sources = sources;

		try {
			dataFileName = URLEncoder.encode(dataFileName, "UTF-8");
			NSB.projectName = CIProject;
			NSB.dataFilePath = dataFilePath;
			NSB.artifactURI = artifactURI;
			NSB.docTypeURI = docTypeURI;
			NSB.formatURI = formatURI;
			NSB.ServerURL = CIServerPath + "pmlj/";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		System.out.println("*******************");
		System.out.println(dataFilePath);
		System.out.println(dataFileName);
		System.out.println("*******************");
		
		//Build Nodeset
		NSB.dataFileName = dataFileName;
		pmljURI = NSB.assertArtifact();

		System.out.println("PML-J URI: " + pmljURI);

		RDFAggregater_Service Service = new RDFAggregater_Service();
		RDFAggregater proxy = Service.getRDFAggregaterHttpPort();
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
