package edu.utep.cybershare.DerivAUI.components;

import java.util.HashMap;
import java.util.Vector;

import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;


public class genericComboBox extends IndividualComboBox {

	private static final long serialVersionUID = 1L;
	private String query;
	
	public genericComboBox(String Q) {
		super();
		query = Q;
		queryURIs(0);
	}
	
	public static String stripURI(String formatURI)
	{
		int start = formatURI.indexOf('#') + 1;
		String name = formatURI.substring(start);
		
		return name;
	}

	public void queryURIs(int selection) {
		Vector<Individual> individuals = new Vector<Individual>();

		edu.utep.trust.provenance.RDFStore_Service service = new edu.utep.trust.provenance.RDFStore_Service();
		edu.utep.trust.provenance.RDFStore proxy = service.getRDFStoreHttpPort();
		
		String qResult = proxy.doQuery(query);
		
		ResultSet results = ResultSetFactory.fromXML(qResult);
		
//		System.out.println(agents);
//		System.out.println(results);
		
		String URI;

		individuals.add(new Individual("Choose URI", " -- Choose URI -- ", "Choose URI"));
		
		// try web service and update local store	
		if(results != null)
			while(results.hasNext())
			{
				
				URI = results.nextSolution().get("?URI").toString();

				String prettyName = stripURI(URI);		
				if(URI == null || prettyName == null)
				{
					System.out.println("Null Pretty Name Conversion");
					break;
				}
				else
				{
					individuals.add(new Individual(URI, prettyName, URI));
				}

				//			System.out.println(results.nextSolution().get("?x").toString());
			}
		// if web service not reached, populate from local store

		this.setIndividuals(individuals);
	}
	
}
