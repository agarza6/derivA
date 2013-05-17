package edu.utep.cybershare.DerivAUI.components;

import java.util.Vector;

import java.util.HashMap;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;

import edu.utep.cybershare.DerivAUI.components.IndividualComboBox.Individual;

public class AgentComboBox extends IndividualComboBox {
	
	private static final long serialVersionUID = 1L;
	private HashMap<String,Integer> prettyNames = new HashMap<String,Integer>();
	private Vector<Individual> individuals = new Vector<Individual>();
	
	public AgentComboBox() {
		super();
		queryAgents(0);
	}
	
	public static String stripURI(String formatURI)
	{
		int start = formatURI.indexOf('#') + 1;
		String name = formatURI.substring(start);
		
		return name;
	}

	public void queryAgents(int selection) {
		Vector<Individual> individuals = new Vector<Individual>();

		edu.utep.trust.provenance.RDFStore_Service service = new edu.utep.trust.provenance.RDFStore_Service();
		edu.utep.trust.provenance.RDFStore proxy = service.getRDFStoreHttpPort();
		
		String query = "", subject = "?name";
		switch (selection){
		case 0: query = "PREFIX pmlp: <http://inference-web.org/2.0/pml-provenance.owl#> " + 
				"SELECT ?uri ?name " + 
				"WHERE {?uri a pmlp:Agent ." +
				"?uri pmlp:hasName ?name . }";
		break;
		case 1: query = "PREFIX pmlp: <http://inference-web.org/2.0/pml-provenance.owl#> " +
				"SELECT ?uri ?name" +
				"WHERE { ?uri a pmlp:InferenceEngine . }";
				subject = "?uri";
		break;
		case 2: query = "PREFIX pmlp: <http://inference-web.org/2.0/pml-provenance.owl#> " +
				"SELECT ?uri ?name " +
				"WHERE { ?uri a pmlp:Person . " +
				"?uri pmlp:hasName ?name . }";
		break;
		case 3: query = "PREFIX pmlp: <http://inference-web.org/2.0/pml-provenance.owl#> " +
				"SELECT ?uri ?name " +
				"WHERE { ?uri a pmlp:Organization . " +
				"?uri pmlp:hasName ?name . }";
				subject = "?uri";
		break;
		}
		
		String agents = proxy.doQuery(query);
		
		ResultSet results = ResultSetFactory.fromXML(agents);
		
//		System.out.println(agents);
//		System.out.println(results);
		
		String format;

		individuals.add(new Individual("Choose Agent", " -- Choose Agent -- ", "Choose Agent"));
		
		// try web service and update local store	
		if(results != null)
			while(results.hasNext())
			{
				
				format = results.nextSolution().get(subject).toString();

				String prettyName = "";
				if(subject.equals("?uri")){
					prettyName= stripURI(format);
				}else{
					prettyName = format.substring(0, format.indexOf('^'));
				}
				
				 
				if(format == null || prettyName == null)
				{
					System.out.println("Null Pretty Name Conversion");
					break;
				}
				else
				{
					individuals.add(new Individual(format, prettyName, format));
				}

				//			System.out.println(results.nextSolution().get("?x").toString());
			}
		// if web service not reached, populate from local store

		this.setIndividuals(individuals);
	}
	
public void queryInfernceEngineByWorkflow(String inferenceRuleURI){
		
		String getInferenceEngineQuery = "SELECT DISTINCT ?inferenceEngine ?inferenceEngineName " +
				"WHERE { " +
				"?inferenceEngine a <http://inference-web.org/2.0/pml-provenance.owl#InferenceEngine> . " +
				"<" + inferenceRuleURI + "> <http://www.w3.org/2000/01/rdf-schema#subClassOf> ?method . " +
				"?inferenceEngine <http://inference-web.org/2.0/pml-provenance.owl#hasInferenceEngineRule> ?method . " +
				"?inferenceEngine <http://inference-web.org/2.0/pml-provenance.owl#hasName> ?inferenceEngineName . " +
				"}";

		edu.utep.trust.provenance.RDFStore_Service service = new edu.utep.trust.provenance.RDFStore_Service();
		edu.utep.trust.provenance.RDFStore proxy = service.getRDFStoreHttpPort();
		
		String qResult = proxy.doQuery(getInferenceEngineQuery);
		ResultSet rSet = ResultSetFactory.fromXML(qResult);
		
//		System.out.println(getInferenceEngineQuery);
//		System.out.println(qResult);
		
		String inferenceEngineURI, inferenceEngineLabel;
		
		if(rSet != null)
			while(rSet.hasNext()){
				
				QuerySolution QS = rSet.nextSolution();
				inferenceEngineURI = QS.get("inferenceEngine").toString();
				inferenceEngineLabel = QS.get("inferenceEngineName").toString();
				
				if(inferenceEngineLabel.contains("^^")){
					inferenceEngineLabel = inferenceEngineLabel.substring(0, inferenceEngineLabel.indexOf("^^"));
				}
				
				if(inferenceEngineURI != null && inferenceEngineLabel != null)
					individuals.add(new Individual(inferenceRuleURI, inferenceEngineLabel, inferenceEngineURI));
				
			}
		
		this.setIndividuals(individuals);
	}
	
}
