package edu.utep.cybershare.DerivAUI.components;

import java.util.Vector;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;

import edu.utep.cybershare.DerivAUI.components.IndividualComboBox.Individual;

public class InferenceRulesList extends IndividualList {

	private static final long serialVersionUID = 1L;
	private Vector<Individual> individuals;

	public InferenceRulesList(){
		queryPMLJ();
	}

	private void queryPMLJ(){
		individuals = new Vector<Individual>();

		edu.utep.trust.provenance.RDFStore_Service service = new edu.utep.trust.provenance.RDFStore_Service();
		edu.utep.trust.provenance.RDFStore proxy = service.getRDFStoreHttpPort();
		//		String pml_j = proxy.getNodeSetsForProject(project);
		//		ResultSet results = ResultSetFactory.fromXML(pml_j);
		String pml_j = proxy.getInferenceRulesWithInfo();

		ResultSet results = ResultSetFactory.fromXML(pml_j);

		//		System.out.println(pml_j);
		//		System.out.println(results);

		String rule, IRClass, IRName;

		if(results != null)
			while(results.hasNext()){

				QuerySolution QS = results.nextSolution();

				rule = QS.get("?inferenceRule").toString();
				IRName = QS.get("?ruleLabel").toString();
				IRClass = QS.get("?ruleClass").toString();
				String prettyName = IRName;

				if(IRClass.substring(IRClass.lastIndexOf('#') + 1).equalsIgnoreCase("resource")){
					rule = null;
				}

				if(prettyName.contains("@")){
					prettyName = prettyName.substring(0, prettyName.indexOf('@'));
				}

				if(prettyName.startsWith("\"")){
					prettyName = prettyName.substring(1);
				}

				if(rule != null && prettyName != null)
				{
					individuals.add(new Individual(rule, prettyName, rule));
				}

			}

	}
	
	public Vector<Individual> getPMLList(){return individuals;}

}
