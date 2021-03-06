package edu.utep.cybershare.DerivAUI.components;


import java.util.Vector;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;


public class PMLJList extends IndividualList {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Vector<Individual> individuals = new Vector<Individual>();;

	public PMLJList(){}
	
	public PMLJList(String project){
		queryPMLJ(project);
	}

	private void queryPMLJ(String project){
		individuals = new Vector<Individual>();


		edu.utep.trust.provenance.RDFStore_Service service = new edu.utep.trust.provenance.RDFStore_Service();
		edu.utep.trust.provenance.RDFStore proxy = service.getRDFStoreHttpPort();
		//		String pml_j = proxy.getNodeSetsForProject(project);
		//		ResultSet results = ResultSetFactory.fromXML(pml_j);
		String pml_j;
		if(project.equalsIgnoreCase("ALL_PROJECTS_$!!$!")){
			pml_j = proxy.getNodeSetsAndConclusionURLs();
		}else{
			pml_j = proxy.getNodeSetsAndConclusionURLsForProject(project);
		}

		ResultSet results = ResultSetFactory.fromXML(pml_j);

//		System.out.println(pml_j);
//		System.out.println(results);

		String conclusionName = "";
		String conclusionURI = "";

		if(results != null)
			while(results.hasNext()){

				QuerySolution QS = results.nextSolution();

				conclusionName = QS.get("?conclusionURL").toString();

				conclusionName = conclusionName.substring(0, conclusionName.indexOf('^'));

				if(conclusionName.contains("/")){
					if(conclusionName.lastIndexOf('/') == (conclusionName.length() - 1))
						conclusionName = conclusionName.substring(0, conclusionName.length() - 1);
					else
						conclusionName = conclusionName.substring(conclusionName.lastIndexOf('/') + 1);
				}

				conclusionURI = QS.get("?nodeset").toString();

				if(conclusionName != null && conclusionName.length() > 1){
					individuals.add(new Individual(conclusionURI, conclusionName, conclusionURI));
				}
			}
	}

	public int queryAntecedentsByWorkflow(String conclusionTypeURI){
		

		String getAllAntecedentsQuery =  "SELECT DISTINCT ?nodeset " +
				"WHERE { " +
				"<" + conclusionTypeURI + "> <http://www.w3.org/2000/01/rdf-schema#subClassOf> ?concType . " +
				"?conclusion a ?concType. " +
				"?nodeset <http://inference-web.org/2.0/pml-justification.owl#hasConclusion> ?conclusion . " +
				"?nodeset a <http://inference-web.org/2.0/pml-justification.owl#NodeSet> . " +
				"FILTER regex(str(?concType),\"wdo/.*\", \"i\")" +
				"}";

		edu.utep.trust.provenance.RDFStore_Service service = new edu.utep.trust.provenance.RDFStore_Service();
		edu.utep.trust.provenance.RDFStore proxy = service.getRDFStoreHttpPort();
		
		String qResult = proxy.doQuery(getAllAntecedentsQuery);
		ResultSet rSet = ResultSetFactory.fromXML(qResult);

		//System.out.println(getAllAntecedentsQuery);
		//System.out.println(qResult);
		
		String antecedentURI, antecedentLabel;
		int count = 0;

		if(rSet != null)
			while(rSet.hasNext()){

				QuerySolution QS = rSet.nextSolution();
				antecedentURI = QS.get("nodeset").toString();
				antecedentLabel = antecedentURI.substring(0, antecedentURI.indexOf('#'));;
	
				if(antecedentLabel.contains("^^")){
					antecedentLabel = antecedentLabel.substring(0, antecedentLabel.indexOf("^^"));
				}
				
				if(antecedentLabel.contains("/")){
					if(antecedentLabel.lastIndexOf('/') == (antecedentLabel.length() - 1))
						antecedentLabel = antecedentLabel.substring(0, antecedentLabel.length() - 1);
					else
						antecedentLabel = antecedentLabel.substring(antecedentLabel.lastIndexOf('/') + 1);
				}

				individuals.add(new Individual(antecedentURI, antecedentLabel, antecedentURI));
				count++;

			}
		return count;
	}
	
	public Vector<Individual> getPMLList(){return individuals;}

}
