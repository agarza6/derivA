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
	private ServerCredentials creds = new ServerCredentials();

	public static final String DATE_FORMAT_NOW = "YYYY-MM-DDTHH:MM:SSZ";
	
	public AssertionMaker(){}
	public AssertionMaker(ServerCredentials sc){creds = sc;}

	public void setDataFilePath(String path){dataFilePath = path;}
	public void setDataFilePath(String path, boolean from){dataFilePath = path; conclusionFromURL = from;}
	public void setArtifactURI(String uri){artifactURI = uri;}
	public void setPMLJ_URI(String uri){pmljURI = uri;}
	public void setDocumentTypeURI(String uri){docTypeURI = uri;}
	public void setDocumentFormatURI(String uri){formatURI = uri;}
	public void setDocumentTypeLabel(String label){docTypeLabel = label;}
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

	public String generateAssertation(){

		NodeSetBuilder NSB = new NodeSetBuilder(creds);

		String dataFileName = "";
		
		CIServerDump uploader = new CIServerDump(creds.getServerURL() + "udata/", creds.getUsername(), creds.getPassword());
		
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
			artifactURI = uploader.saveDataToCIServer(dataFileName, creds.getProject(), resource_bytes, true);

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
			NSB.dataFilePath = dataFilePath;
			NSB.artifactURI = artifactURI;
			NSB.docTypeURI = docTypeURI;
			NSB.formatURI = formatURI;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		
		//Build Nodeset
		NSB.dataFileName = dataFileName;
		pmljURI = NSB.assertArtifact();

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
		return pmljURI;
	}
}
