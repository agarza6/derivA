package edu.utep.cybershare.DerivAUI.components;

import java.awt.Component;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;


public class IndividualList extends JList {
	private static final long serialVersionUID = 1L;

	public IndividualList() {
		super();
		this.setCellRenderer(new IndividualListCellRenderer());
	}
	
	/**
	 * Sets the model of the list.
	 * Sorts the list being passed using the local name of Individuals as the sorting key 
	 * @param list
	 */
	public void setModel(Vector<Individual> list) {
		// get the local names of individuals to use as key to sort the list
		HashMap<String,Individual> temp = new HashMap<String,Individual>();
		for (Iterator<Individual> i=list.iterator(); i.hasNext(); ) {
			Individual ind = i.next();
			temp.put(ind.getURI(), ind);
			
		}
		
		// sort by local name
		String[] sortedNames = new String[temp.size()];
		temp.keySet().toArray(sortedNames);
		Arrays.sort(sortedNames);
		Individual[] sortedList = new Individual[temp.size()];
		for (int i=0; i<sortedList.length; i++) {
			sortedList[i] = temp.get(sortedNames[i]);
		}
		// set model with sorted list
		setModel(new IndividualListModel(sortedList));
	}
	
    /**
     * Model for Individual list
     * @author Leonardo Salayandia
     */
    private class IndividualListModel extends AbstractListModel {
		private static final long serialVersionUID = 1L;
		Individual[] list;
    	
    	protected IndividualListModel(Individual[] list) {
			super();
			this.list = list;			
		}
		
    	@Override
        public int getSize() { return list.length; }
        
		@Override
		public Object getElementAt(int index) {
			return list[index];
		}
    }
    
	/**
	 * Item used to populate the IndividualComboBox model.
	 * @author Leonardo Salayandia
	 */
	public class Individual implements Comparable<Individual> {
		private String uri;
    	private String friendlyName;
    	private String description;
    	
    	public Individual(String uri, String name, String desc) {
    		this.uri = uri;
    		friendlyName = name;
    		description = desc;
    	}
    	
    	public String getURI() {
    		return uri;
    	}
    	
    	public String getName() {
    		return friendlyName;
    	}
    	
    	public String getDescription() {
    		return description;
    	}
    	
    	@Override
    	public boolean equals(Object o) {
    		if (o != null && o instanceof Individual) {
    			Individual ind = (Individual) o;
    			return getURI().equalsIgnoreCase(ind.getURI());
    		}
    		return false;
    	}
    	
    	@Override
    	public String toString() {
    		return (friendlyName == null || friendlyName.isEmpty()) ? getURI() : getName();
    	}

		@Override
		public int compareTo(Individual o) {
			int ans = toString().compareTo(o.toString());
			// if friendly names are the same and URIs are not the same, compare based on URIs
			if (ans == 0 && !equals(o)) {
				ans = getURI().compareTo(o.getURI());
			}
			return ans;
		}
	}
    
    
    /**
     * Cell renderer for Individual List
     * @author Leonardo Salayandia
     */
    private class IndividualListCellRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = 1L;
		
		protected IndividualListCellRenderer() {
			super();
		}
		
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			Individual ind = (Individual) value;
//			setText(SAW.getIndividualLocalName(ind));
			setToolTipText(ind.getURI());
			return this;
		}
    }
}

