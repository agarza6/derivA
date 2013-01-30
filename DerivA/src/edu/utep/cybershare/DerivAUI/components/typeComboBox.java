package edu.utep.cybershare.DerivAUI.components;

import java.util.ArrayList;
import java.util.Vector;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;

import edu.utep.cybershare.DerivAUI.components.IndividualComboBox.Individual;

public class typeComboBox extends IndividualComboBox {
	private static final long serialVersionUID = 1L;
	private String ontology = null;
	private Vector<Individual> individuals = new Vector<Individual>();
	private ArrayList<String> antecedentTypeURI = new ArrayList<String>();

	public void setOntology(String Ont) {
		ontology = Ont;
	}

	public typeComboBox() {
		super();
		queryInformationTypes();
	}

	public static String stripURI(String formatURI)
	{
		int start = formatURI.indexOf('#') + 1;
		String name = formatURI.substring(start);

		return name;
	}

	public void queryInformationTypes() {
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

	public void queryNextInformationTypesByWorkflow(String inferenceRuleURI, String workflow){

		System.out.println("INFER: " + inferenceRuleURI);
		
		String getNextConclusionTypeQuery = "SELECT DISTINCT ?conclusionDef ?conclusionTypeName " +
				"WHERE { " +
				"?method <http://www.w3.org/2000/01/rdf-schema#subClassOf> <" + inferenceRuleURI + "> . " +
				"?method <http://trust.utep.edu/2.0/wdo.owl#hasOutput> ?conclusionType . " + 
				"?conclusionType <http://www.w3.org/2000/01/rdf-schema#subClassOf> ?conclusionDef . " + 
				"?conclusionDef <http://www.w3.org/2000/01/rdf-schema#label> ?conclusionTypeName . " + 
				"FILTER regex(str(?conclusionDef),\"http://rio.cs.utep.edu/ciserver/ciprojects/wdo/.*\", \"i\") " +
				"}"; 


		edu.utep.trust.provenance.RDFStore_Service service = new edu.utep.trust.provenance.RDFStore_Service();
		edu.utep.trust.provenance.RDFStore proxy = service.getRDFStoreHttpPort();

		String qResult = proxy.doQuery(getNextConclusionTypeQuery);
		ResultSet rSet = ResultSetFactory.fromXML(qResult);

		//		System.out.println(getNextConclusionTypeQuery);
				System.out.println(qResult);

		String conclusionTypeURI, conclusionTypeLabel;

		if(rSet != null)
			while(rSet.hasNext()){

				QuerySolution QS = rSet.nextSolution();
				conclusionTypeURI = QS.get("conclusionDef").toString();
				conclusionTypeLabel = QS.get("conclusionTypeName").toString();

				if(conclusionTypeLabel.contains("@")){
					conclusionTypeLabel = conclusionTypeLabel.substring(0, conclusionTypeLabel.indexOf('@'));
				}

				individuals.add(new Individual(conclusionTypeURI, conclusionTypeLabel, conclusionTypeURI));

			}

		this.setIndividuals(individuals);

	}

	public ArrayList<String> getAllAntecedentTypesFromInferenceRule(String inferenceRuleURI, String workflow){

		String getNextConclusionTypeQuery = "SELECT DISTINCT ?antecedentType " +
				"WHERE { " +
				"?subRule <http://www.w3.org/2000/01/rdf-schema#subClassOf> <" + inferenceRuleURI + "> ." +
				"?subRule <http://trust.utep.edu/2.0/wdo.owl#hasInput> ?antecedentType . " +
				//				"?antecedentType <http://www.w3.org/2000/01/rdf-schema#subClassOf> ?antecedentTypeDef . " +
				"FILTER regex(str(?antecedentType), \"" + workflow + "\") " +
				"}"; 

		edu.utep.trust.provenance.RDFStore_Service service = new edu.utep.trust.provenance.RDFStore_Service();
		edu.utep.trust.provenance.RDFStore proxy = service.getRDFStoreHttpPort();

		String qResult = proxy.doQuery(getNextConclusionTypeQuery);
		ResultSet rSet = ResultSetFactory.fromXML(qResult);

		//		System.out.println(getNextConclusionTypeQuery);
		//		System.out.println(qResult);

		if(rSet != null)
			while(rSet.hasNext()){

				QuerySolution QS = rSet.nextSolution();
				antecedentTypeURI.add(QS.get("antecedentType").toString());

			}
		return antecedentTypeURI;
	}

}

