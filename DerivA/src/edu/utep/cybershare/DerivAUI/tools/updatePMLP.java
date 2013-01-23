package edu.utep.cybershare.DerivAUI.tools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import edu.utep.cybershare.DerivA.util.CIServerDump;
import edu.utep.trust.provenance.RDFAggregater;
import edu.utep.trust.provenance.RDFAggregater_Service;

public class updatePMLP {
	
	
	private static void deleteIsMemberOf(String sub, String sup){
		ArrayList<String> workFile = readURI(sub);
		String finalFile = "";
		
		for(Iterator<String> iter = workFile.iterator(); iter.hasNext();){
			String temp = iter.next();
			if(!temp.contains(sup)){
				finalFile = finalFile + temp + '\n';
			}
		}
		
		uploadProvenance(finalFile,"agarza6", "Vegeta1984!", "ESIP-Network", sub);
		
		System.out.println(finalFile);
	}
	
	public static void addIsMemberOf(String sub, String sup){
		
		ArrayList<String> workFile = readURI(sub);
		String finalFile = "";
		
		for(Iterator<String> iter = workFile.iterator(); iter.hasNext();){
			String temp = iter.next();
			if(temp.contains("</pmlp:hasDescription>")){
				finalFile = finalFile + temp + '\n';
				finalFile = finalFile + '\t' + "<pmlp:isMemberOf rdf:resource='" + sup + "'/>" + '\n';
			}else{
				finalFile = finalFile + temp + '\n';
			}
		}
		
		uploadProvenance(finalFile,"agarza6", "Vegeta1984!", "ESIP-Network", sub);
		
		System.out.println(finalFile);
		
	}
	
	private static ArrayList<String> readURI(String uri){
		ArrayList<String> file = new ArrayList<String>();
		String inputLine;

		try{
			URL oracle = new URL(uri);
			BufferedReader in = new BufferedReader( new InputStreamReader(oracle.openStream()));

			while ((inputLine = in.readLine()) != null){
				inputLine.trim();
				file.add(inputLine);
			}

			in.close();

			return file;
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String uploadProvenance(String pmlP, String uploaderName, String uploaderPass, String CIProject, String URI){

		String pmlp_url = "http://rio.cs.utep.edu/ciserver/ciprojects/";
		String shortName = URI.substring(URI.lastIndexOf('/') + 1, URI.lastIndexOf('#'));
		
		//Upload data file to CI-Server
		CIServerDump uploader = new CIServerDump(pmlp_url + "pmlp/", uploaderName, uploaderPass);
		byte[] resource_bytes = pmlP.getBytes();
		String artifactURI = uploader.savePMLPToCIServer(shortName, CIProject, resource_bytes, false);

//		RDFAggregater_Service Service = new RDFAggregater_Service();
//		RDFAggregater proxy = Service.getRDFAggregaterHttpPort();
//		String result = "";
//		int intents = 0;
//		while(!result.equalsIgnoreCase("SUCCESS") && intents < 3){
//			result = proxy.addDocumentAt(artifactURI, uploader.getUserEmail());
//			intents++;
//		}

//		if(result.equalsIgnoreCase("SUCCESS")){
//			System.out.println("Aggregation Successful");
//		}else{
//			System.out.println("Aggregation Failed");
//		}

		return artifactURI;
	}
	
	public static void main(String[] args){
		
		ArrayList<String> orgs = new ArrayList<String>();
		
		// Open the file that is the first 
		// command line parameter
		FileInputStream fstream;
		try {
			fstream = new FileInputStream("updateOrgs1.txt");

		// Get the object of DataInputStream
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		//Read File Line By Line
		while ((strLine = br.readLine()) != null)   {
			// Print the content on the console
			orgs.add(strLine);
		}
		in.close();
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(Iterator<String> iter = orgs.iterator(); iter.hasNext();){
			addIsMemberOf(iter.next(), "http://rio.cs.utep.edu/ciserver/ciprojects/pmlp/ESIP.owl#ESIP");
		}
		
	}
	
}

