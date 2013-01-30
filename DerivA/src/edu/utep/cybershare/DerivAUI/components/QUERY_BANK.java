package edu.utep.cybershare.DerivAUI.components;

public class QUERY_BANK {

	public String method, type, workflow, ontology;



	public String GET_WORKFLOWS_QUERY = "PREFIX wdo: <http://trust.utep.edu/2.0/wdo.owl#> " +
			"SELECT ?URI WHERE {?URI a wdo:SemanticAbstractWorkflow . }";

	public String GET_METHODS_BY_CONCLUSIONTYPE = "SELECT DISTINCT ?methodRule ?methodName " +
			"WHERE {  " +
			"?data <http://www.w3.org/2000/01/rdf-schema#subClassOf> <" + type + "> .  " +
			"?data <http://trust.utep.edu/2.0/wdo.owl#isInputTo> ?method .  " +
			"?method <http://www.w3.org/2000/01/rdf-schema#subClassOf> ?methodRule .  " +
			"?methodRule <http://www.w3.org/2000/01/rdf-schema#label> ?methodName " +
			"FILTER regex(str(?methodRule),\"http://rio.cs.utep.edu/ciserver/ciprojects/wdo/.*\", \"i\") " +
			"}";

	public String GET_ANTECEDENTTYPE_BY_METHOD = "SELECT DISTINCT ?antecedentType WHERE {  " +
			"?subRule <http://www.w3.org/2000/01/rdf-schema#subClassOf> <" + method + "> .  " +
			"?subRule <http://trust.utep.edu/2.0/wdo.owl#hasInput> ?antecedentType .   " +
			"FILTER regex(str(?antecedentType), \"" + workflow + "\")   " +
			"}";

	public String GET_ANTECEDENTS_BY_WORKFLOW = "SELECT DISTINCT ?nodeset ?conclusionURL ?concType WHERE {  " +
			"?nodeset a <http://inference-web.org/2.0/pml-justification.owl#NodeSet> .  " +
			"<" + type + "> <http://www.w3.org/2000/01/rdf-schema#subClassOf> ?concType . " + 
			"?nodeset <http://inference-web.org/2.0/pml-justification.owl#hasConclusion> ?conclusion .  " +
			"?conclusion a ?concType.  " +
			"?conclusion <http://inference-web.org/2.0/pml-provenance.owl#hasURL> ?conclusionURL .  " +
			"FILTER (?concType != <http://inference-web.org/2.0/pml-provenance.owl#Information>)  " +
			"FILTER regex(str(?concType),\"http://rio.cs.utep.edu/ciserver/ciprojects/wdo/.*\", \"i\")  " +
			"}";
	
	public 	String GET_NXT_CONCLUSION_TYPE = "SELECT DISTINCT ?conclusionDef ?conclusionTypeName " +
			"WHERE { " +
			"<" + method + "> <http://trust.utep.edu/2.0/wdo.owl#hasOutput> ?conclusionType . " + 
			"?conclusionType <http://www.w3.org/2000/01/rdf-schema#subClassOf> ?conclusionDef . " + 
			"?conclusionDef <http://www.w3.org/2000/01/rdf-schema#label> ?conclusionTypeName . " + 
			"FILTER regex(str(?conclusionDef),\"http://rio.cs.utep.edu/ciserver/ciprojects/wdo/.*\", \"i\") " +
			"}"; 
	
	public String GET_ALL_ANTECEDENTS_BY_TYPE =  "SELECT DISTINCT ?nodeset " +
			"WHERE { " +
			"<" + type + "> <http://www.w3.org/2000/01/rdf-schema#subClassOf> ?concType . " +
			"?conclusion a ?concType. " +
			"?nodeset <http://inference-web.org/2.0/pml-justification.owl#hasConclusion> ?conclusion . " +
			"?nodeset a <http://inference-web.org/2.0/pml-justification.owl#NodeSet> . " +
			"FILTER regex(str(?concType),\"wdo/.*\", \"i\")" +
			"}";
	
	public 	String IS_FINAL_CONCLUSION = "SELECT DISTINCT ?method " +
			"WHERE {  " +
			"?type <http://www.w3.org/2000/01/rdf-schema#subClassOf> <" + type + "> . " +
			"?type <http://trust.utep.edu/2.0/wdo.owl#isInputTo> ?method .  " +
			"OPTIONAL { ?method2 <http://www.w3.org/2000/01/rdf-schema#subClassOf> <http://inference-web.org/2.0/pml-provenance.owl#Source> . FILTER(?method2 = ?method) } " +
			"FILTER(!bound(?method2)) " +
			"}";
	
}
