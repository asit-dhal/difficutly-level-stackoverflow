package de.tu_darmstadt.kom.stackoverflow.ml;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import weka.core.Attribute;
import weka.core.AttributeStats;
import weka.core.Instances;
import weka.core.Utils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;
import de.tu_darmstadt.kom.stackoverflow.AppConfigs;
import de.tu_darmstadt.kom.stackoverflow.utility.AppExceptions;
import de.tu_darmstadt.kom.stackoverflow.utility.Utility;


/**
 * @author Asit
 * Feature class for processing on feature
 */
public class Feature {
	
	Logger m_log = Logger.getLogger(AppConfigs.APPLICATION_NAME);
	private Instances m_data;
	private ArrayList<String> m_mandatoryFeatures;
	
	/**
	 * Constructor
	 * @param data Instances
	 */
	public Feature(Instances data) {
		m_data = new Instances(data); //copy 
		m_mandatoryFeatures = new ArrayList<String>();
	}
	
	/**
	 * get a list of strong words
	 * @return list of words
	 */
	public Set<String> getStrongWords() {
		Set<String> strongWords = new LinkedHashSet<String>();
		
		Instances tfInstances = getTF(); //not used currenty
		Instances tdidfInstances = getTDIDF();
		
		boolean mandatoryAttrFlg = false;
		TreeMap<String, Double> stringAttributMap = new TreeMap<String, Double>();
		
		//get dictionary of attributes and the corresponding values of tdIdf(max value)
		for(int i=1; i<tfInstances.numAttributes(); i++) {
			if ( tfInstances.attribute(i).isNumeric()) {
				
				for(String mandatoryAttr : m_mandatoryFeatures) {
					if (mandatoryAttr.equals(tfInstances.attribute(i).name())) {
						m_log.debug("mandatory attribute : " + mandatoryAttr + " ignored" );
						mandatoryAttrFlg = true; 
						break;
					}
				}
				
				if ( ! mandatoryAttrFlg ) {
					AttributeStats tfStats = tfInstances.attributeStats(i);
					AttributeStats tdIdfStats = tdidfInstances.attributeStats(i);
					stringAttributMap.put(tfInstances.attribute(i).name(), tdIdfStats.numericStats.max);
					//if ( tdIdfStats.numericStats.max > 6.0)
					//	strongWords.add(tfInstances.attribute(i).name());
				}else {
					mandatoryAttrFlg = false;
				}
			}
		}
		
		Map<String, Double> sortedMap = Utility.sortByValues(stringAttributMap);
		Set keys = sortedMap.entrySet();
	    Iterator i = keys.iterator();
	 
	    //we assume 10% words are strong
	    int index = 0;
	    int totalStongWords = tfInstances.numAttributes()*10/100;
	    //only 10% words are strong words
	    //sort dictionary according to th tdIdf value and select top 10% words 
	    while(i.hasNext()) {
	    	index ++;
	    	Map.Entry me = (Map.Entry)i.next();
	    	strongWords.add((String)me.getKey());
	    	if ( index > totalStongWords )
	    		break;
	    }
			
		m_log.info("Total no. of strong words : " + strongWords.size());
		
		return strongWords;
	}
	
	private Instances getTF() {
		String tfFilterOption = "-I -L";
		return stringToWordVectorFilterData(tfFilterOption);

	}
	
	private Instances getTDIDF() {
		String tfidfFilterOption = "-T -C -L";
		return stringToWordVectorFilterData(tfidfFilterOption);
	}
	
	
	
	/**
	 * Convert to string to word vectors
	 * @param filterOption
	 * @return
	 */
	public Instances stringToWordVectorFilterData(String filterOption) {
		StringToWordVector stringToWordVector = new StringToWordVector();
		
		try {
			//m_log.info("Using StringToWordVector filter");
			String[] filterOptions = Utils.splitOptions(filterOption);
			//m_log.info("Setting filter options : " + filterOption);
			stringToWordVector.setOptions(filterOptions);
			stringToWordVector.setInputFormat(m_data);
			//stringToWordVector
			m_data = Filter.useFilter(m_data, stringToWordVector);
			//printDetails(data);
			return m_data;
		} catch (Exception e) {
			m_log.fatal(Utility.stackTrace(e));
			throw new AppExceptions("stringToWordVectorFilterData failed");
		
		}

	}
	
	//has to be called just after data loaded from file
	//attributes like LOC percentage and user reputation should never be ignored 
	public void setMandatoryAttribute() {
		try {
			for(int i=1; i<m_data.numAttributes(); i++) {
				if ( m_data.attribute(i).isNumeric()) {
					m_mandatoryFeatures.add(m_data.attribute(i).name());
				}
			}
			
		}catch(Exception e) {
			m_log.fatal(Utility.stackTrace(e));
			throw new AppExceptions("setMandatoryAttribute failed");
		}
	
	}
	
	public Instances selectFeatures(ArrayList<String> features, Instances data) {
		for(int i=1; i<data.numAttributes(); i++) {
			if (!Arrays.asList(features).contains(data.attribute(i).name())) {
				data.deleteAttributeAt(i);
				//m_log.debug("deleting... : " + data.attribute(i).name());
			}
			else {
				//m_log.debug("Selected attribute " + data.attribute(i).name());
			}
		}
		
		return data;
		
	}
	
	public ArrayList<String> getMandatoryFeatures() {
		return m_mandatoryFeatures;
	}
	
	/**
	 * Increase the weight of instances which contain strong words
	 * @param strong word list
	 * @param weight
	 * @param data instances
	 * @return  instances
	 */
	public Instances setWeights(ArrayList<String> features, double weight, Instances data) {
		for(int i=0; i<data.numInstances(); i++) {
			double wgt = 0.0;
			for(int j=1; j<data.numAttributes(); j++) {
				if (!Arrays.asList(features).contains(data.attribute(j).name())) {
					wgt += weight;
				}
			}
			if(wgt > 0.0)
				data.instance(i).setWeight(wgt);
		}
		
		return data;
	}
	

}
