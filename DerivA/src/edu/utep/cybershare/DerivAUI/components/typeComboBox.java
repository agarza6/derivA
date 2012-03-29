package edu.utep.cybershare.DerivAUI.components;

import java.util.Vector;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;

public class typeComboBox extends IndividualComboBox {
	private static final long serialVersionUID = 1L;
	private String ontology = null;
	
	public void setOntology(String Ont) {
		ontology = Ont;
	}
	
	public typeComboBox(boolean bol) {
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
		
		String types = "";
		if(ontology == null)
			types = proxy.getInformationSubclasses();
		else
			types = proxy.getInferenceRulesWithInfoInWDO(ontology);
		
		ResultSet results = ResultSetFactory.fromXML(types);
		
		String type;
		
//		System.out.println(types);

		individuals.add(new Individual("Choose Type", " -- Choose Type -- ", "Choose Type"));
		
		// try web service and update local store	
		if(results != null)
			while(results.hasNext())
			{
				QuerySolution QS = results.nextSolution();
				type = QS.get("?informationSubclass").toString();

				String prettyName = QS.get("?subclassLabel").toString();
				
				if(prettyName.contains("@")){
					prettyName = prettyName.substring(0, prettyName.indexOf('@'));
				}
				
				if(type == null || prettyName == null)
				{
					System.out.println("Null Pretty Name Conversion");
					break;
				}
				else
				{
					individuals.add(new Individual(type, prettyName, type));
				}

				//			System.out.println(results.nextSolution().get("?x").toString());
			}
		// if web service not reached, populate from local store

		this.setIndividuals(individuals);
	}
}

