package edu.utep.cybershare.DerivAUI.components;

public class QUERY_BANK {

	public String 	GET_WORKFLOWS_QUERY = "PREFIX wdo: <http://trust.utep.edu/2.0/wdo.owl#> " +
			"SELECT ?URI WHERE {?URI a wdo:SemanticAbstractWorkflow . }";
	
}
