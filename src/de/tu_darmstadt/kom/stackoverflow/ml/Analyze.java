package de.tu_darmstadt.kom.stackoverflow.ml;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import de.tu_darmstadt.kom.stackoverflow.AppConfigs;
import de.tu_darmstadt.kom.stackoverflow.utility.AppExceptions;
import de.tu_darmstadt.kom.stackoverflow.utility.Utility;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.meta.CostSensitiveClassifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.Utils;


/**
 * @author Asit
 * Main class that builds model
 */
public class Analyze {
	
	//private FilterData m_filterData;
	private Instances m_data;
	private CostSensitiveClassifier m_cModel;
	private String m_filterOptions;
	private String m_modelOptions;
	private Evaluation m_evaluation;
	private Feature m_feature;
	Logger m_log = Logger.getLogger(AppConfigs.APPLICATION_NAME);
	private int m_folds;
	private Set<String> m_strongWords;
	private String m_classAttribute;
	private double m_weightToStrongWords;
		
	
	/**
	 * Constructor
	 * Step 1. Create a cost sensitive classifier
	 * Step 2. Create a classification model in cost sensitive classifier
	 * @param model name of classification model
	 * @param mdlOptions weka classification model options
	 * @param weight weight of strong words
	 * @param filterOptions StringToWordVector filter options
	 * @param costMatrix cost matrix for cost sensitive classification
	 * @param folds n-fold-cross validation
	 * @throws Exception
	 */
	public Analyze(String model, String mdlOptions, double weight, String filterOptions, String costMatrix, int folds) throws Exception {
		
		m_strongWords = null;
		m_weightToStrongWords = weight;
	
		String[] modelOptions = Utils.splitOptions(mdlOptions);
		m_cModel = new CostSensitiveClassifier();
		String[] costSenOptions;
		try {
			costSenOptions = Utils.splitOptions("-cost-matrix \"" + costMatrix + "\"");
			m_cModel.setOptions(costSenOptions);
		} catch (Exception e) {
			m_log.fatal(Utility.stackTrace(e));
			throw new AppExceptions("CostSensitiveClassifier Options set failed");
		}
			
		if(model.equals("naivebayes")) {
			NaiveBayes mdl = new NaiveBayes();
			mdl.setOptions(modelOptions);
			m_cModel.setClassifier(mdl);
			m_log.info("Using Naive Bayes Classification model");
		}
		else if(model.equals("smo")) {
			SMO mdl = new SMO();
			mdl.setOptions(modelOptions);
			m_cModel.setClassifier(mdl);
			m_log.info("Using SVM model");
		}else if(model.equals("j48")){
			J48 mdl = new J48();
			mdl.setOptions(modelOptions);
			m_cModel.setClassifier(mdl);
			m_log.info("Using J48 decision tree model");
		}else {
			m_log.info("Invalid Model");
			throw new AppExceptions("Invalid Model");
		}
		
		//m_filterData = new FilterData();
		m_filterOptions = filterOptions;
		m_folds = folds;
	}
	
	/**
	 * Load data from arff file
	 * @param filename
	 */
	public void loadData(String filename) {
		try {
			m_log.info("Loading data to model");
			m_data = new LoadData().load(filename);
			
			m_data.setClassIndex(m_data.numAttributes()-1);
						
			m_feature = new Feature(m_data);
			//mark LOC, user reputation
			m_feature.setMandatoryAttribute();
			
		}catch(Exception e) {
			m_log.fatal(Utility.stackTrace(e));
			throw new AppExceptions("Load Data failed");
		}
	}	
	
	
	/**
	 * Evaluate the model
	 */
	private void evaluate() {
		try {
			m_evaluation = new Evaluation(m_data);
			m_evaluation.crossValidateModel(m_cModel, m_data, m_folds, new Random(1));
		}catch (Exception e) {
			m_log.fatal(Utility.stackTrace(e));
			throw new AppExceptions("Evaluation failed");
		}
		
		
	}
	
	/**
	 * Increase the weight of strong words
	 */
	private void selectAttributes(String whichType) {
		try {
			
			if(whichType.equals("important-words")) {
				//increase weight of mandatory words
				m_data = m_feature.setWeights(new ArrayList<String>(m_strongWords), m_weightToStrongWords, m_data);
			}
			
		} catch (Exception e) {
			m_log.fatal(Utility.stackTrace(e));
			throw new AppExceptions("selelectAttributes failed");
		}
		
	}
	
	/**
	 * Entry point
	 * @throws IOException
	 */
	public void process() throws IOException {
		
		try {
			m_strongWords = m_feature.getStrongWords();
			m_log.info("Strong words found : " + m_strongWords);
			m_data = m_feature.stringToWordVectorFilterData(m_filterOptions);
			//increase weight of strong words
			selectAttributes("important-words");
			
			//m_log.info("No. of attributes : " + m_data.numAttributes());
			//m_log.info("No. of attributes after String transformation : " + m_data.numAttributes());
			m_classAttribute = m_data.attribute(m_data.classIndex()).name();
			
			evaluate();
			printResult();
			
		} catch (Exception e) {
			m_log.fatal(Utility.stackTrace(e));
			throw new AppExceptions("Processing failed");
		}
	}
	
	/**
	 * Prints the evaluation of model
	 */
	private void printResult(){
		try {
			m_log.info(m_evaluation.toSummaryString());
			m_log.info(m_evaluation.toClassDetailsString());
			m_log.info(m_evaluation.toMatrixString());
		}catch (Exception e) {
			m_log.fatal(Utility.stackTrace(e));
			throw new AppExceptions("printResult failed");
		}
	}
	
	
	//debugging 
	public void toFile() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("./data/debug.arff"));
		writer.write(m_data.toString());
		writer.flush();
		writer.close();
	}
	
	public void toFile2(String str) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("./data/debug" + str + ".arff"));
		writer.write(m_data.toString());
		writer.flush();
		writer.close();
	}
	
	
	

}
