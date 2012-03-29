package edu.utep.cybershare.DerivAUI.components;

import java.util.Vector;

import java.util.HashMap;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;

public class InferenceRulesComboBox extends IndividualComboBox {
	private static final long serialVersionUID = 1L;
	private HashMap<String,Integer> prettyNames = new HashMap<String,Integer>();
	private String Ontology = null;
	
	public void setOntology(String ont) {
		Ontology = ont;
	}
	
	public InferenceRulesComboBox(boolean bol) {
		super();
		if(bol)
			queryAgents();
	}
	
	public static String stripURI(String formatURI)
	{
		int start = formatURI.indexOf('#') + 1;
		String name = formatURI.substring(start);
		
		return name;
	}

	public void queryAgents() {
		Vector<Individual> individuals = new Vector<Individual>();

		edu.utep.trust.provenance.RDFStore_Service service = new edu.utep.trust.provenance.RDFStore_Service();
		edu.utep.trust.provenance.RDFStore proxy = service.getRDFStoreHttpPort();

		String rules = "";
		if(Ontology == null)
			rules = proxy.getInferenceRulesWithInfo();
		else
			rules = proxy.getInferenceRulesWithInfoInWDO(Ontology);
		
		ResultSet results = ResultSetFactory.fromXML(rules);
		
//		System.out.println(rules);
		
		String rule, IRClass, IRName;

		individuals.add(new Individual("Choose Inference Rule", " -- Choose Inference Rule -- ", "Choose Inference Rule"));
		
		// try web service and update local store	
		if(results != null)
			while(results.hasNext())
			{
				QuerySolution QS = results.nextSolution();
				
				rule = QS.get("?inferenceRule").toString();
				IRName = QS.get("?ruleLabel").toString();
				IRClass = QS.get("?ruleClass").toString();
//				String prettyName = IRClass.substring(IRClass.lastIndexOf('#') + 1) + " : " + IRName;
				String prettyName = IRName;
				
//				System.out.println(prettyName);
				
				if(IRClass.substring(IRClass.lastIndexOf('#') + 1).equalsIgnoreCase("resource")){
					rule = null;
				}
				
				if(prettyName.contains("@")){
					prettyName = prettyName.substring(0, prettyName.indexOf('@'));
				}
				
				if(prettyName.startsWith("\"")){
					prettyName = prettyName.substring(1);
				}
				
//				if(prettyName.equalsIgnoreCase("null") || prettyName.equalsIgnoreCase(""))
//					prettyName = stripURI(rule);

				if(rule != null && prettyName != null)
				{
					individuals.add(new Individual(rule, prettyName, rule));
				}

				//			System.out.println(results.nextSolution().get("?x").toString());
			}
		// if web service not reached, populate from local store

		this.setIndividuals(individuals);
	}
}
