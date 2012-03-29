package edu.utep.cybershare.DerivAUI.components;

import java.util.Vector;

import java.util.HashMap;

import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;

public class AgentComboBox extends IndividualComboBox {
	private static final long serialVersionUID = 1L;
	private HashMap<String,Integer> prettyNames = new HashMap<String,Integer>();
	public AgentComboBox(boolean bol) {
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
		String agents = proxy.getInferenceEngines();
		
		ResultSet results = ResultSetFactory.fromXML(agents);
		
//		System.out.println(agents);
//		System.out.println(results);
		
		String format;

		individuals.add(new Individual("Choose Agent", " -- Choose Agent -- ", "Choose Agent"));
		
		// try web service and update local store	
		if(results != null)
			while(results.hasNext())
			{
				
				format = results.nextSolution().get("?inferenceEngine").toString();

				String prettyName = stripURI(format);		
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
}
