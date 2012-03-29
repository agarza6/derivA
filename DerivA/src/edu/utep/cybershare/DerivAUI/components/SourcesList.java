package edu.utep.cybershare.DerivAUI.components;


import java.util.Vector;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;


public class SourcesList extends IndividualList {
	
	private Vector<Individual> individuals;
	
	public SourcesList(){
		queryPMLP();
	}

	private void queryPMLP(){
		 individuals = new Vector<Individual>();

		
		edu.utep.trust.provenance.RDFStore_Service service = new edu.utep.trust.provenance.RDFStore_Service();
		edu.utep.trust.provenance.RDFStore proxy = service.getRDFStoreHttpPort();

		String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" + 
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
		"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" +
		"PREFIX pmlj: <http://inference-web.org/2.0/pml-justification.owl#>" +
		"PREFIX pmlp: <http://inference-web.org/2.0/pml-provenance.owl#>" +
		"PREFIX pml-sparql: <http://trust.utep.edu/sparql-pml#>" +
		"PREFIX ds: <http://inference-web.org/2.0/ds.owl#>" +
		"select ?URI ?NAME where {" +
		"?URI a pmlp:Person ." +
		"?URI pmlp:hasName ?NAME ." +
		"}";
		
		String pml_j = proxy.doQuery(query);
	
		ResultSet results = ResultSetFactory.fromXML(pml_j);
		
//		System.out.println(pml_j);
//		System.out.println(results);
		
		String personName = "";
		String personURI = "";
		
		if(results != null)
			while(results.hasNext()){
				
				QuerySolution QS = results.nextSolution();
				
				personName = QS.get("?NAME").toString();
				
				personName = personName.substring(0, personName.indexOf('^'));
				
//				if(personName.contains("/")){
//					personName = personName.substring(personName.lastIndexOf('/') + 1);
//				}
				
				personURI = QS.get("?URI").toString();
				
				if(personName != null && personName.length() > 1){
					individuals.add(new Individual(personURI, personName, personURI));
				}

			}

	}
	
	public Vector<Individual> getSourceList(){return individuals;}
	
}
