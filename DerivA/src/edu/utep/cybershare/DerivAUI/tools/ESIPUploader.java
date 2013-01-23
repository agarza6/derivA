package edu.utep.cybershare.DerivAUI.tools;


import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;

import edu.utep.cybershare.DerivA.util.CIServerDump;
import edu.utep.trust.provenance.RDFAggregater;
import edu.utep.trust.provenance.RDFAggregater_Service;


public class ESIPUploader {

	//Person + FOAF variables//
	static String email, title, depiction, phone, homepage, workpage, projectpage, schoolpage;
	static String lname, fname, sha1Sum;

	//Organization variables//
	static String pmlp_url, shortName, fullName, OrgURL, memberOfURI,memberOfName;
	static String[] CurrentlyShort;
	static String[] CurrentlyURI;

	public static String createFOAF_PMLP(){

		shortName = lname + "_" + fname;
		shortName = shortName.replaceAll("[*<>\\[\\]\\+\",]", "-");
		shortName = shortName.replaceAll(" ", "_");

		String pml_foaf = "<rdf:RDF" + '\n';
		//Imports
		pml_foaf += '\t' + "xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"" + '\n';
		pml_foaf += '\t' + "xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"" + '\n';
		pml_foaf += '\t' + "xmlns:pmlp=\"http://inference-web.org/2.0/pml-provenance.owl#\"" + '\n';
		pml_foaf += '\t' + "xmlns:owl=\"http://www.w3.org/2002/07/owl#\"" + '\n';
		pml_foaf += '\t' + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\"" + '\n';
		pml_foaf += '\t' + "xmlns:foaf=\"http://xmlns.com/foaf/0.1/\"" + '\n';
		pml_foaf += '\t' + "xmlns:admin=\"http://webns.net/mvcb/\">" + '\n';
		//Foaf/PML declarations
		pml_foaf += '\t' + "<foaf:Person rdf:about=\"" + pmlp_url + "pmlp/" + shortName + ".owl" + '#' + shortName + "\">" + '\n';
		pml_foaf += "\t\t" + "<rdf:type rdf:resource=\"http://inference-web.org/2.0/pml-provenance.owl#Person\"/>" + '\n';
		pml_foaf += "\t\t" + "<foaf:primaryTopic rdf:resource=\"#me\"/>" + '\n';
		pml_foaf += "\t\t" + "<admin:generatorAgent rdf:resource=\"http://trust.cs.utep.edu/derivA/\"/>" + '\n';
		pml_foaf += "\t\t" + "<admin:errorReportsTo rdf:resource=\"mailto:agarza6@miners.utep.edu\"/>" + '\n';
		//PML Info
		pml_foaf += "\t\t" + "<pmlp:hasName rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\">" + fullName + "</pmlp:hasName>" + '\n';
		if(!memberOfURI.isEmpty() && memberOfURI.length() > 0)
			pml_foaf += "\t\t" + "<pmlp:isMemberOf rdf:resource=\"" + memberOfURI + "\"/>" + '\n';

		//FOAF Info

		//get sha1 for email
		try {
			sha1Sum = SHA1(email);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		pml_foaf += "\t\t" + "<foaf:name>" + fullName + "</foaf:name>" + '\n';

		if(!title.isEmpty() && title.length() > 0)
			pml_foaf += "\t\t" + "<foaf:title>" + title + "</foaf:title>" + '\n';
		pml_foaf += "\t\t" + "<foaf:mbox_sha1sum>" + sha1Sum + "</foaf:mbox_sha1sum>" + '\n';
		if(!depiction.isEmpty() && depiction.length() > 0)
			pml_foaf += "\t\t" + "<foaf:depiction rdf:resource=\"" + depiction + "\"/>" + '\n';
		if(!phone.isEmpty() && phone.length() > 0)
			pml_foaf += "\t\t" + "<foaf:phone rdf:resource=\"tel:" + phone + "\"/>" + '\n';
		if(!homepage.isEmpty() && homepage.length() > 0)
			pml_foaf += "\t\t" + "<foaf:homepage rdf:resource=\"" + homepage + "\"/>" + '\n';
		if(!workpage.isEmpty() && workpage.length() > 0)
			pml_foaf += "\t\t" + "<foaf:workplaceHomepage rdf:resource=\"" + workpage  + "\"/>" + '\n';
		if(!projectpage.isEmpty() && projectpage.length() > 0)
			pml_foaf += "\t\t" + "<foaf:workInfoHomepage rdf:resource=\"" + projectpage + "\"/>" + '\n';
		if(!schoolpage.isEmpty() && schoolpage.length() > 0)
			pml_foaf += "\t\t" + "<foaf:schoolHomepage rdf:resource=\"" + schoolpage + "\"/>" + '\n';
		//Knows
		pml_foaf += "\t\t" + "<foaf:knows>" + '\n';
		pml_foaf += "\t\t" + "</foaf:knows>" + '\n';
		//End File
		pml_foaf += '\t' + "</foaf:Person>" + '\n';
		pml_foaf += "</rdf:RDF>" + '\n';

		return pml_foaf;
	}

	private static String convertToHex(byte[] data) { 
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) { 
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do { 
				if ((0 <= halfbyte) && (halfbyte <= 9)) 
					buf.append((char) ('0' + halfbyte));
				else 
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			} while(two_halfs++ < 1);
		} 
		return buf.toString();
	} 

	public static String SHA1(String text) 
			throws NoSuchAlgorithmException, UnsupportedEncodingException  { 
		MessageDigest md;
		md = MessageDigest.getInstance("SHA-1");
		byte[] sha1hash = new byte[40];
		md.update(text.getBytes("iso-8859-1"), 0, text.length());
		sha1hash = md.digest();
		return convertToHex(sha1hash);
	} 

	public static String createOrganizationPMLP(){

		shortName = shortName.replaceAll("[*<>\\[\\]\\+\",]", "-");
		shortName = shortName.replaceAll(" ", "_");

		String pmlP = "<rdf:RDF" + '\n';
		pmlP += '\t' + "xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#'" + '\n';
		pmlP += '\t' + "xmlns:pmlp='http://inference-web.org/2.0/pml-provenance.owl#'" + '\n';
		pmlP += '\t' + "xmlns:owl='http://www.w3.org/2002/07/owl#'" + '\n';
		pmlP += '\t' + "xmlns:xsd='http://www.w3.org/2001/XMLSchema#'" + '\n';
		pmlP += '\t' + "xmlns:rdfs='http://www.w3.org/2000/01/rdf-schema#'>" + '\n';

		pmlP += '\t' + "<pmlp:Organization rdf:about='" + pmlp_url + "pmlp/" + shortName + ".owl" + '#' + shortName + "'>" + '\n';
		pmlP += '\t' + "<pmlp:hasName rdf:datatype='http://www.w3.org/2001/XMLSchema#string'>" + fullName + "</pmlp:hasName>" + '\n';
		pmlP += '\t' + "<pmlp:hasDescription>" + '\n';
		pmlP += "\t\t" + "<pmlp:Information>" + '\n';
		pmlP += "\t\t\t" + "<pmlp:hasURL rdf:datatype='http://www.w3.org/2001/XMLSchema#anyURI'>" + OrgURL + "</pmlp:hasURL>" + '\n';
		pmlP += "\t\t" + "</pmlp:Information>" + '\n';
		pmlP += '\t' + "</pmlp:hasDescription>" + '\n';

		//check if member of another Organization
		if(memberOfName != null && !memberOfName.isEmpty()){
			pmlP += '\t' + "<pmlp:isMemberOf rdf:resource='" + memberOfURI + "#" + memberOfName + "'/>" + '\n';
		}

		pmlP += '\t' + "</pmlp:Organization>" + '\n';
		pmlP += "</rdf:RDF>";

		//		System.out.println(pmlP);

		return pmlP;
	}

	private static String uploadProvenance(String pmlP, String uploaderName, String uploaderPass, String CIProject){

		//Upload data file to CI-Server
		CIServerDump uploader = new CIServerDump(pmlp_url + "pmlp/", uploaderName, uploaderPass);

		byte[] resource_bytes = pmlP.getBytes();

		String artifactURI = uploader.savePMLPToCIServer(shortName + ".owl", CIProject, resource_bytes, false);

		//Add To The Triple Store
		RDFAggregater_Service Service = new RDFAggregater_Service();
		RDFAggregater proxy = Service.getRDFAggregaterHttpPort();
		String result = "";
		int intents = 0;
		while(!result.equalsIgnoreCase("SUCCESS") && intents < 3){
			result = proxy.addDocumentAt(artifactURI, uploader.getUserEmail());
			intents++;
		}

				if(result.equalsIgnoreCase("SUCCESS")){
					System.out.println("Aggregation Successful");
				}else{
					System.out.println("Aggregation Failed");
				}

		return artifactURI;
	}

	//shortName, FullName, Organization Web, Parent Org shortname
	private static void setValues(String a, String b, String c, String d, String e){
		shortName = a;
		fullName = b;
		OrgURL = c;
		memberOfURI = d;
		memberOfName = e;
	}

	//fname,lname,title,email,depiction,phone,homepage,workpage,projectpage,schoolpage,orgURI
	private static void setValuesPeople(String a, String b, String c, String d, String e, String f, String g, String h, String i, String j, String k){
		fname = a;
		lname = b;
		fullName = fname + " " + lname;
		title = c;
		email = d;
		depiction = e; 
		phone = f;
		homepage = g; 
		workpage = h;
		projectpage = i;
		schoolpage = j;
		memberOfURI = k;
	}

	private static void writePMLFile(){
		try{
			// Create file 
			FileWriter outFile = new FileWriter("out.txt", true);
			PrintWriter out = new PrintWriter(outFile);

			for(int i = 0; i < CurrentlyShort.length; i++){
				out.println(CurrentlyShort[i] + "," + CurrentlyURI[i]);
			}
			//Close the output stream
			out.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	public static void main(String[] args){

		pmlp_url = "http://rio.cs.utep.edu/ciserver/ciprojects/";
		captureEngine();
		//		ArrayList<String> orgs = new ArrayList<String>();
		//		ArrayList<String> readTemp = new ArrayList<String>();
		//
		//
		//		try{
		//			// Open the file that is the first 
		//			// command line parameter
		//			FileInputStream fstream = new FileInputStream("out.txt");
		//			// Get the object of DataInputStream
		//			DataInputStream in = new DataInputStream(fstream);
		//			BufferedReader br = new BufferedReader(new InputStreamReader(in));
		//			String strLine;
		//			//Read File Line By Line
		//			while ((strLine = br.readLine()) != null)   {
		//				// Print the content on the console
		//				readTemp.add(strLine);
		//			}
		//			in.close();
		//
		//		}catch (Exception e){//Catch exception if any
		//			System.err.println("Error: " + e.getMessage());
		//		}
		//
		//		try{
		//			// Open the file that is the first 
		//			// command line parameter
		//			FileInputStream fstream = new FileInputStream("esip.txt");
		//			// Get the object of DataInputStream
		//			DataInputStream in = new DataInputStream(fstream);
		//			BufferedReader br = new BufferedReader(new InputStreamReader(in));
		//			String strLine;
		//			//Read File Line By Line
		//			while ((strLine = br.readLine()) != null)   {
		//				// Print the content on the console
		//				orgs.add(strLine);
		//			}
		//
		//			in.close();
		//
		//		}catch (Exception e){//Catch exception if any
		//			System.err.println("Error: " + e.getMessage());
		//		}
		//
		//		CurrentlyShort = new String[orgs.size() + readTemp.size()];
		//		CurrentlyURI = new String[orgs.size() + readTemp.size()];
		//
		//		int counter  = 0;
		//		for(Iterator<String> iter = readTemp.iterator(); iter.hasNext();){
		//			String one = iter.next();
		//			CurrentlyURI[counter] = one.substring(0, one.indexOf(','));
		//			CurrentlyShort[counter] = one.substring(one.indexOf(','));
		//			counter++;
		//		}
		//
		//		//		capturePerson(orgs);
		//
		//		for(Iterator<String> iter = orgs.iterator(); iter.hasNext();){
		//			String temp = iter.next();
		//			String[] aTemp = temp.split(",");
		//			String parentURI = null;
		//
		//			if(aTemp[3].equalsIgnoreCase("null")){
		//				aTemp[3] = null;
		//				parentURI = null;
		//			}else{
		//				for(int i = 0; i < CurrentlyShort.length; i++){
		//					if(CurrentlyShort[i] != null && CurrentlyShort[i].equalsIgnoreCase(aTemp[3])){
		//						parentURI = CurrentlyURI[i];
		//						break;
		//					}
		//				}
		//			}
		//
		//			setValues(aTemp[0], aTemp[1], aTemp[2], parentURI,aTemp[3]);
		//			//			setValuesPeople(aTemp[0], aTemp[1], aTemp[2], aTemp[4], aTemp[5], aTemp[6], aTemp[7], aTemp[8], aTemp[9], aTemp[10], aTemp[11]);
		//
		//			String pmlp = createOrganizationPMLP();
		//			CurrentlyURI[counter] = uploadProvenance(pmlp, "agarza6", "Vegeta1984!", "ESIP-Network");
		//			CurrentlyShort[counter] = aTemp[0];
		//			counter++;
		//		}
		//
		//		writePMLFile();

		System.out.println("WORK COMPLETE");

	}

	private static void capturePerson(ArrayList<String> person){
		for(Iterator<String> iter = person.iterator(); iter.hasNext();){
			String temp = iter.next();
			String[] aTemp = temp.split(",");

			setValuesPeople(aTemp[0], aTemp[1], aTemp[2], aTemp[3], aTemp[4], aTemp[5], aTemp[6], aTemp[7], aTemp[8], aTemp[9], aTemp[10]);

			String pmlp = createFOAF_PMLP();
			System.out.println(pmlp);
			//uploadProvenance(pmlp, "agarza6", "Vegeta1984!", "ESIP-Network-Members");
		}

	}

	private static void captureFormat(){

		String[] formats = {"HDF"};

		pmlp_url = "http://rio.cs.utep.edu/ciserver/ciprojects/";
		String pmlP = "";


		for(int i = 0; i < formats.length; i++){

			shortName = formats[i];

			pmlP = "<rdf:RDF" + '\n';
			pmlP += '\t' + "xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"" + '\n';
			pmlP += '\t' + "xmlns:owl=\"http://www.w3.org/2002/07/owl#\"" + '\n';
			pmlP += '\t' + "xmlns:pmlp=\"http://inference-web.org/2.0/pml-provenance.owl#\"" + '\n';
			pmlP += '\t' + "xmlns:daml=\"http://www.daml.org/2001/03/daml+oil#\"" + '\n';
			pmlP += '\t' + "xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"" + '\n';
			pmlP += '\t' + "xmlns:ds=\"http://inference-web.org/2.0/ds.owl#\">" + '\n';
			pmlP += "\t\t" + "<pmlp:Format rdf:about=\"http://rio.cs.utep.edu/ciserver/ciprojects/EDAC/" + formats[i] + ".owl#" + formats[i] + "\">" + '\n';
			pmlP += "\t\t" + "<pmlp:hasDescription>" + '\n';
			pmlP += "\t\t\t" + "<pmlp:Information>" + '\n';
			pmlP += "\t\t\t" + "<pmlp:hasLanguage rdf:resource=\"http://inference-web.org/registry/LG/English.owl#English\"/>" + '\n';
			pmlP += "\t\t\t" + "</pmlp:Information>" + '\n';
			pmlP += "\t\t\t" + "</pmlp:hasDescription>" + '\n';
			pmlP += "\t\t\t" + "<pmlp:hasName rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\">" + formats[i] + "</pmlp:hasName>" + '\n';
			pmlP += "\t\t" + "</pmlp:Format>" + '\n';
			pmlP += '\t' + "</rdf:RDF>";

			uploadProvenance(pmlP, "agarza6", "AGarza62012!", "EDAC");
			System.out.println("1");
		}
	}

	private static void captureEngine(){

		String[] formats = {"TXT", "PNG", "SHP", "GML", "KML", "GeoJSON", "CSU", "GeoTIFF", "SID", "ECW", "HDF", "NetCDF", "ArcASCIIGrid", "DEM", "JSON", "XML", "DOC", "DOCX",
				"XLSX", "PDF", "HTML", "PNG", "GIF", "JPEG", "ZIP", "GeoRSS", "GRIB", "GeoDB", "XLS", "ISOXML", "FGDCXML", "DCXML", "WCSCapXML", "WMSCapXML",
				"WCSDescribeXML", "WFSDescribeFeatTypeXML", "WaterML"};

		pmlp_url = "http://rio.cs.utep.edu/ciserver/ciprojects/";
		String pmlP = "";


//		for(int i = 0; i < formats.length; i++){

			shortName = "REST";

			pmlP = "<rdf:RDF" + '\n';
			pmlP += "\t" + "xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"" + '\n';
			pmlP += "\t" + "xmlns:owl=\"http://www.w3.org/2002/07/owl#\"" + '\n';
			pmlP += "\t" + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\"" + '\n';
			pmlP += "\t" + "xmlns:pmlp=\"http://inference-web.org/2.0/pml-provenance.owl#\"" + '\n';
			pmlP += "\t" + "xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"" + '\n';
			pmlP += "\t" + "xmlns:ds=\"http://inference-web.org/2.0/ds.owl#\">" + '\n';
			
			
			pmlP += "\t" + "<pmlp:InferenceEngine rdf:about=\"http://rio.cs.utep.edu/ciserver/ciprojects/pmlp/" + shortName + ".owl#" + shortName + "\">" + '\n';
			
			
//			pmlP += "\t\t" + "<pmlp:hasInferenceEngineRule rdf:resource=\"http://rio.cs.utep.edu/ciserver/ciprojects/wdo/EDAC#m11\"/>" + '\n';
//			pmlP += "\t\t" + "<pmlp:hasInferenceEngineRule rdf:resource=\"http://rio.cs.utep.edu/ciserver/ciprojects/wdo/EDAC#m12\"/>" + '\n';
//			pmlP += "\t\t" + "<pmlp:hasInferenceEngineRule rdf:resource=\"http://rio.cs.utep.edu/ciserver/ciprojects/wdo/EDAC#m17\"/>" + '\n';
//			pmlP += "\t\t" + "<pmlp:hasInferenceEngineRule rdf:resource=\"http://rio.cs.utep.edu/ciserver/ciprojects/wdo/EDAC#m13\"/>" + '\n';
//			pmlP += "\t\t" + "<pmlp:hasInferenceEngineRule rdf:resource=\"http://rio.cs.utep.edu/ciserver/ciprojects/wdo/EDAC#m3\"/>" + '\n';
//			pmlP += "\t\t" + "<pmlp:hasInferenceEngineRule rdf:resource=\"http://rio.cs.utep.edu/ciserver/ciprojects/wdo/EDAC#m55\"/>" + '\n';
//			pmlP += "\t\t" + "<pmlp:hasInferenceEngineRule rdf:resource=\"http://rio.cs.utep.edu/ciserver/ciprojects/wdo/EDAC#m18\"/>" + '\n';
//			pmlP += "\t\t" + "<pmlp:hasInferenceEngineRule rdf:resource=\"http://rio.cs.utep.edu/ciserver/ciprojects/wdo/EDAC#m14\"/>" + '\n';
//			pmlP += "\t\t" + "<pmlp:hasInferenceEngineRule rdf:resource=\"http://rio.cs.utep.edu/ciserver/ciprojects/wdo/EDAC#m11\"/>" + '\n';
//			pmlP += "\t\t" + "<pmlp:hasInferenceEngineRule rdf:resource=\"http://rio.cs.utep.edu/ciserver/ciprojects/wdo/EDAC#m21\"/>" + '\n';
//			pmlP += "\t\t" + "<pmlp:hasInferenceEngineRule rdf:resource=\"http://rio.cs.utep.edu/ciserver/ciprojects/wdo/EDAC#m22\"/>" + '\n';
//			pmlP += "\t\t" + "<pmlp:hasInferenceEngineRule rdf:resource=\"http://rio.cs.utep.edu/ciserver/ciprojects/wdo/EDAC#m4\"/>" + '\n';
//			pmlP += "\t\t" + "<pmlp:hasInferenceEngineRule rdf:resource=\"http://rio.cs.utep.edu/ciserver/ciprojects/wdo/EDAC#m20\"/>" + '\n';
//			pmlP += "\t\t" + "<pmlp:hasInferenceEngineRule rdf:resource=\"http://rio.cs.utep.edu/ciserver/ciprojects/wdo/EDAC#m15\"/>" + '\n';
//			pmlP += "\t\t" + "<pmlp:hasInferenceEngineRule rdf:resource=\"http://rio.cs.utep.edu/ciserver/ciprojects/wdo/EDAC#m54\"/>" + '\n';
						
			pmlP += "\t\t" + "<pmlp:hasDescription>" + '\n';
			pmlP += "\t\t" + "<pmlp:Information>" + '\n';
			pmlP += "\t\t" + "<pmlp:hasLanguage rdf:resource=\"http://inference-web.org/registry/LG/English.owl#English\"/>" + '\n';
			pmlP += "\t\t" + "<pmlp:hasRawString rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\">" + shortName + "</pmlp:hasRawString>" + '\n';
			pmlP += "\t\t" + "<pmlp:hasURL rdf:datatype=\"http://www.w3.org/2001/XMLSchema#anyURI\">http://rio.cs.utep.edu/ciserver/ciprojects/pmlp/" + shortName + ".owl</pmlp:hasURL>" + '\n';
			pmlP += "\t\t" + "</pmlp:Information>" + '\n';
			pmlP += "\t\t" + "</pmlp:hasDescription>" + '\n';
			pmlP += "\t\t" + "<pmlp:hasName rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\">" + shortName + "</pmlp:hasName>" + '\n';
			pmlP += "\t" + "</pmlp:InferenceEngine>" + '\n';
			pmlP += "" + "</rdf:RDF>";

			System.out.println(pmlP);
			
			uploadProvenance(pmlP, "agarza6", "vegeta1984!", "EDAC");

//		}
	}

}
