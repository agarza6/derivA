package edu.utep.cybershare.DerivAUI.components;


import java.util.Vector;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;

import edu.utep.cybershare.DerivA.util.ServerCredentials;


public class SourcesList extends IndividualList {
	
	private Vector<Individual> individuals;
	
	public SourcesList(){
		queryPMLP();
	}

	private void queryPMLP(){
		 individuals = new Vector<Individual>();
		
		edu.utep.trust.provenance.RDFStore_Service service = new edu.utep.trust.provenance.RDFStore_Service();
		edu.utep.trust.provenance.RDFStore proxy = service.getRDFStoreHttpPort();

		String query = "PREFIX pmlp: <http://inference-web.org/2.0/pml-provenance.owl#>" + 
				"SELECT ?uri ?name " + 
				"WHERE {?uri a pmlp:Agent ." +
				"?uri pmlp:hasName ?name . } ";
		
		String pml_j = proxy.doQuery(query);
	
		ResultSet results = ResultSetFactory.fromXML(pml_j);
		
//		System.out.println(pml_j);
//		System.out.println(results);
		
		String personName = "";
		String personURI = "";
		
		if(results != null)
			while(results.hasNext()){
				
				QuerySolution QS = results.nextSolution();
				
				personName = QS.get("?name").toString();
				
				personName = personName.substring(0, personName.indexOf('^'));
				
//				if(personName.contains("/")){
//					personName = personName.substring(personName.lastIndexOf('/') + 1);
//				}
				
				personURI = QS.get("?uri").toString();
				
				if(personName != null && personName.length() > 1){
					individuals.add(new Individual(personURI, personName, personURI));
				}

			}

	}
	
	public Vector<Individual> getSourceList(){return individuals;}
	
}
