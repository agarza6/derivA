package edu.utep.cybershare.DerivAUI.components;

import java.util.Vector;

import java.util.HashMap;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;

public class OntologyComboBox extends IndividualComboBox {
	private static final long serialVersionUID = 1L;
	private HashMap<String,Integer> prettyNames = new HashMap<String,Integer>();
	
	public OntologyComboBox(String project) {
		super();
		queryAgents(project);
	}
	
	public static String stripURI(String formatURI)
	{
		int start = formatURI.indexOf('#') + 1;
		String name = formatURI.substring(start);
		
		return name;
	}

	public void queryAgents(String project) {
		Vector<Individual> individuals = new Vector<Individual>();

		edu.utep.trust.provenance.RDFStore_Service service = new edu.utep.trust.provenance.RDFStore_Service();
		edu.utep.trust.provenance.RDFStore proxy = service.getRDFStoreHttpPort();
		//String WDOs = proxy.getWDOsForProject(project);
		String query = "PREFIX wdo: <http://trust.utep.edu/2.0/wdo.owl#>" + '\n' + 
						"SELECT ?WDO WHERE {" +  '\n' + 
						"?WDO a wdo:WorkflowDrivenOntology . " + '\n' +  
						"}";

		String WDOs = proxy.doQuery(query);
		
		ResultSet results = ResultSetFactory.fromXML(WDOs);
		
//		System.out.println(project);
//		System.out.println(WDOs);
//		System.out.println(results);
		
		String WDO, WDOLabel;

		individuals.add(new Individual("none", " No Ontology Selected ", "none"));
		
		// try web service and update local store	
		if(results != null)
			while(results.hasNext())
			{
				QuerySolution QS = results.nextSolution();
				WDO = QS.get("?WDO").toString();
				
				//WDOLabel = QS.get("?wdoLabel").toString();
				WDOLabel = WDO.substring(WDO.lastIndexOf('/') + 1);
		
				if(WDO == null || WDOLabel == null)
				{
					System.out.println("Null Pretty Name Conversion");
					break;
				}
				else
				{
					individuals.add(new Individual(WDO, WDOLabel, WDO));
				}

				//			System.out.println(results.nextSolution().get("?x").toString());
			}
		// if web service not reached, populate from local store

		this.setIndividuals(individuals);
	}
}
