package edu.utep.cybershare.DerivAUI.components;


import java.util.Vector;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;


public class PMLJList extends IndividualList {

	private Vector<Individual> individuals;

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

		System.out.println(pml_j);
		System.out.println(results);

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

	public Vector<Individual> getPMLList(){return individuals;}

}
