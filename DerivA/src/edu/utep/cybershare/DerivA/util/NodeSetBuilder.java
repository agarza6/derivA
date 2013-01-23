package edu.utep.cybershare.DerivA.util;

import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Vector;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.inference_web.pml.v2.pmlp.IWAgent;
import org.inference_web.pml.v2.pmlp.IWInferenceEngine;
import org.inference_web.pml.v2.pmlp.IWInformation;
import org.inference_web.pml.v2.util.PMLObjectManager;
import org.inference_web.pml.v2.vocabulary.PMLP;


import edu.utep.cybershare.DerivAUI.components.IndividualList.Individual;
import pml.dumping.writer.NodesetWriter;


public class NodeSetBuilder {

	//General Variables
	public String[] sources;
	public String pmljURI;
	public String dateTime;
	public String artifactURI;
	public String formatURI;
	public String fileName;
	

	//Assertion Variables
	public String dataFilePath;
	public String dataFileName;
	public String docTypeURI;

	//Inference Engine Variables
	public Vector<Individual> inferenceRuleVector;
	
	//Derivation Variables
	public String agentURI, agentLabel;
	public String IRURI, IRLabel;
	public Vector<Individual> antecedentsURIs;

	private ServerCredentials creds;

	public NodeSetBuilder(){}
	public NodeSetBuilder(ServerCredentials sc){creds = sc;}
	
	public String assertArtifact(){

		GregorianCalendar gcal = new GregorianCalendar();
		XMLGregorianCalendar xgcal;
		String time = "";

		try
		{
			xgcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
			time = xgcal.toXMLFormat();
		}
		catch(Exception e)
		{e.printStackTrace();}

		NodesetWriter wtr = new NodesetWriter();

		//Set Information Instance (type)
		//		wtr.setInformationByClass(docTypeURI);
		wtr.setInformation(docTypeURI);

		//Set Time
		System.out.println("time: " + time);
		wtr.setTimestamp(time);

		//Set Conclusion
		wtr.setConclusionAsURL(artifactURI, formatURI);

		//Set Rule
		wtr.setRule("http://inference-web.org/registry/DPR/Told.owl#Told");

		wtr.setFileName(dataFileName);
		
		//Set Source
		String[] aSource = sources;
		wtr.setSource(aSource);

		System.out.println("serverURL: " + creds.getServerURL());
		String resultURI = wtr.writePML(creds.getServerURL(), creds.getProject(), creds.getUsername(), creds.getPassword());

		return resultURI;
	}

	public String derivateArtifact(){

		GregorianCalendar gcal = new GregorianCalendar();
		XMLGregorianCalendar xgcal;
		String time = "";

		try
		{
			xgcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
			time = xgcal.toXMLFormat();
		}
		catch(Exception e)
		{e.printStackTrace();}

		NodesetWriter wtr = new NodesetWriter();

		//Set Antecedents
		for(Iterator<Individual> iter = antecedentsURIs.iterator(); iter.hasNext();){
			Individual URI = iter.next();
			//		nodeSet = (IWNodeSet) PMLObjectManager.createPMLObject(PMLJ.NodeSet_lname);

			wtr.addAntecedent(URI.getURI());
		}

		//Set Time
		wtr.setTimestamp(time);

		//Set Filename
		wtr.setFileName(fileName);

		//Set Paths
		wtr.setBasePath(creds.getServerURL());
		wtr.setBaseURL(creds.getServerURL());

		//Set Conclusion Information
		//		wtr.setInformationByClass(docTypeURI);
		wtr.setInformation(docTypeURI);
		wtr.setConclusionAsURL(artifactURI, formatURI);
		wtr.setEngine(agentURI);
		wtr.setRule(IRURI);

		//Set Time
		System.out.println("time: " + time);
		wtr.setTimestamp(time);
		
		wtr.setIdentifier();

		String resultURI = wtr.writePML(creds.getServerURL(), creds.getProject(), creds.getUsername(), creds.getPassword());

		return resultURI;
	}

	public String createAgent(String id, String agentName,String description, String URL){
		
		IWInferenceEngine agent = (IWInferenceEngine)PMLObjectManager.createPMLObject(PMLP.InferenceEngine_lname);

		agent.setIdentifier(PMLObjectManager.getObjectID(id));

		agentName = agentName.replaceAll("&#xD;", "");
		agentName = agentName.replaceAll("&#xA;", "");
		agentName = agentName.replaceAll('\r' + "", "");
		agentName = agentName.replaceAll('\n' + "", "");
		agentName = agentName.replaceAll(" ", "_");
		agent.setHasName(agentName);

		IWInformation info = (IWInformation)PMLObjectManager.createPMLObject(PMLP.Information_lname);
		info.setHasRawString(description);
		info.setHasLanguage("http://inference-web.org/registry/LG/English.owl#English");
		
		URL = URL.replaceAll("&#xD;", "");
		URL = URL.replaceAll("&#xA;", "");
		URL = URL.replaceAll('\r' + "", "");
		URL = URL.replaceAll('\n' + "", "");
		URL = URL.replaceAll(" ", "_");
		info.setHasURL(URL);
		agent.addHasDescription(info);
		
		Vector<String> IRURIs = new Vector<String>();
		for(Iterator<Individual> iter = inferenceRuleVector.iterator(); iter.hasNext();){
			Individual temp = iter.next();
			IRURIs.add(temp.getURI());
//			agent.addHasInferenceEngineRule(temp.getURI());
		}
		
		agent.setHasInferenceEngineRule(IRURIs);

		//		IWInferenceRule IR = (IWInferenceRule)PMLObjectManager.createPMLObject(PMLP.InferenceRule_lname);
		//		IR.setIdentifier(PMLObjectManager.getObjectID("http://someuri2.owl#uri"));
		//		IR.setHasName("SOMENAME");
		//		IR.setHasOwner(agent);
		//		PMLObjectManager.savePMLObject(IR, "c:/IR.owl");

		String AgentString = PMLObjectManager.printPMLObjectToString(agent);
		return AgentString;

		
		
//		PMLObjectManager.savePMLObject(agent, "c:/agent.owl");

	}
	
}
