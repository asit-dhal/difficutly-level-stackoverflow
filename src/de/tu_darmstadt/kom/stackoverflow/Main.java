package de.tu_darmstadt.kom.stackoverflow;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.PatternLayout;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import de.tu_darmstadt.kom.stackoverflow.ml.Analyze;
import de.tu_darmstadt.kom.stackoverflow.ml.GenerateArff;
import de.tu_darmstadt.kom.stackoverflow.utility.Utility;

/**
 * Program's entry point
 * @author Asit
 *
 */
public class Main {

	Logger log = setupLogFile();
	DbOperations m_dbOperations = null;
	
	@Parameter(names={ "-h", "--help" }, description = "Display help information")
	private boolean m_printHelp;
	
	@Parameter(names={"-l", "--load"}, description="<file-name> Load Stackoverflow dump")
	private String m_loadDumpFile;
	
	@Parameter(names={"-p", "--process"}, description="Do post processing")
	private boolean m_postProcessing;
	
	@Parameter(names={"-g", "--generate"}, description="<file-name> Generate arff file")
	private String m_generate;
	
	@Parameter(names={"-d", "--db"}, description="<db-name> Database name")
	private String m_dbName;
	
	@Parameter(names={"--url"}, description="<ip address:port number> of database")
	private String m_url;
	
	@Parameter(names={"-u", "--user"}, description="<user-name> User name")
	private String m_username;
	
	@Parameter(names={ "--password"}, description="Password")
	private String m_password;
	
	@Parameter(names={ "--records"}, description="No. of records")
	private int m_recordCount;
	
	@Parameter(names={ "--xmlformat"}, description="xml format config file")
	private String m_xmlFileFormat;
	
	@Parameter(names={ "--analyze"}, description="analyze")
	private String m_analyze;
	
	@Parameter(names={ "--model"}, description="model")
	private String m_model;
	
	@Parameter(names={ "--model-options"}, description="model options")
	private String m_modelOptions;
	
	@Parameter(names={ "--filter-options"}, description="filter options")
	private String m_filterOptions;
	
	@Parameter(names={ "--cost-matrix"}, description="cost matrix")
	private String m_costMatrix;
	
	@Parameter(names={ "--folds"}, description="cross validation folds")
	private int m_folds;
	
	@Parameter(names={ "--weight"}, description="weight to strong words")
	private double m_weight;
	
	@Parameter(names={ "--tags"}, description="tag(s) list in CSV format")
	private String m_tags;
	
	
	private String[] m_tagList;
	

	/**
	 * Processes commandline arguments. Calls appropriate methods to do the task.
	 * @param args Commandline arguments to the application
	 * @throws Exception
	 */
	public void execute(String...args) throws Exception {
		System.out.println(Arrays.toString(args));
		BasicConfigurator.configure();
		final JCommander jCommander = new JCommander(this);
		jCommander.setProgramName(AppConfigs.APPLICATION_NAME);
		jCommander.parse(args);
		
		if (m_username != null && m_password != null && m_dbName != null)
			m_dbOperations = new DbOperations(m_username, m_password, m_dbName, m_url);
		
		m_tagList = null;
		if(m_tags != null) {
			m_tagList = m_tags.split(",");
			log.info("Tags : " + Arrays.toString(m_tagList));
		}
		
		//if user asks for help, usage message to be shown
		if(m_printHelp) {
			jCommander.usage();
		}
		//if load argument is mentioned
		else if( m_loadDumpFile != null) {
			log.info("Loading XML File");
			long startTime = System.nanoTime();
			new LoadXmlDump(m_loadDumpFile, m_dbOperations);
			long stopTime = System.nanoTime();
			log.info("Loading of XML File finished");
			log.info("Time Taken : " + (stopTime - startTime)/ 1000000000 + " seconds");
		} 
		//for post processing of posts_python records
		else if(m_postProcessing) {
			log.info("Doing post processing of data");
			long startTime = System.nanoTime();
			postProcessing();
			long stopTime = System.nanoTime();
			log.info("Post procssing finished");
			log.info("Time Taken : " + (stopTime - startTime)/ 1000000000 + " seconds");
		}
		//generate arff file
		else if(m_generate != null) {
			log.info("Generating ARFF file");
			long startTime = System.nanoTime();
			generateArffFile();
			long stopTime = System.nanoTime();
			log.info("Generating ARFF file finished");
			log.info("Time Taken : " + (stopTime - startTime)/ 1000000000.0 + " seconds");
		}
		//do the analysis
		else if(m_analyze != null) {
			log.info("model : " + m_model);
			log.info("model options: " + m_modelOptions);
			log.info("StringToWordVector filter options: " + m_filterOptions);
			log.info("cross validation folds: " + m_folds);
			log.info("file to load: " + m_analyze);
			analyze(m_analyze, m_model, m_modelOptions, m_weight, m_filterOptions, m_costMatrix, m_folds);
		}
		else {
			jCommander.usage();
		}
		
		
		if(m_dbOperations != null)
			m_dbOperations.cleanup();
	}

	/**
	 * Analyze the arff file
	 * @param filename name of arff file
	 * @param model name of classification model
	 * @param modelOptions options for classification model(weka format)
	 * @param weight weight of strong word
	 * @param filterOptions StringToWordVectorFilter options(weka format)
	 * @param costMatrix costMatrix for optimization
	 * @param folds a number for n-fold validation
	 */
	private void analyze(String filename, String model, String modelOptions, double weight, String filterOptions,
				String costMatrix, int folds) {
		
		try {
			Analyze analyze = new Analyze(model, modelOptions, weight, filterOptions, costMatrix, folds);
			analyze.loadData(filename);
			analyze.process();
			//for debugging 
			//analyze.toFile();
		}catch(Exception e) {
			log.fatal(Utility.stackTrace(e));
		}

	}
	
		
	/**
	 * Sets up a rolling log file
	 * @return Logger insance
	 */
	private Logger setupLogFile() {
		//Refernce: http://www.codejava.net/coding/configure-log4j-for-creating-daily-rolling-log-files
		PatternLayout layout = new PatternLayout();
        String conversionPattern = "[%p] %d %c %M - %m%n";
        layout.setConversionPattern(conversionPattern);
 
        // creates daily rolling file appender
        DailyRollingFileAppender rollingAppender = new DailyRollingFileAppender();
        rollingAppender.setFile(AppConfigs.LOG + AppConfigs.APPLICATION_NAME + ".log");
        rollingAppender.setDatePattern("'.'yyyy-MM-dd");
        rollingAppender.setLayout(layout);
        rollingAppender.activateOptions();
 
        // configures the root logger
        Logger rootLogger = Logger.getRootLogger();
        rootLogger.setLevel(Level.DEBUG);
        rootLogger.addAppender(rollingAppender);
 
        // creates a custom logger and log messages
        return Logger.getLogger(AppConfigs.APPLICATION_NAME);
	}
	
		
	/**
	 * Program Entry point
	 * @param args Command line arguments
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Main main = new Main();
		main.execute(args);
	}
	
	/**
	 * Generates Arff file
	 * @throws Exception
	 */
	private void generateArffFile() throws Exception {
		GenerateArff generateArff = new GenerateArff(m_dbOperations);
		HashMap<String, String> cols = new HashMap<String, String>();
		
		for(String tag : m_tagList) { 
			cols.clear();
			//attribute names and types
			cols.put("posts_" + tag + ".body", "string");  
			cols.put("posts_" + tag + ".title", "string");
			cols.put("posts_" + tag + ".loc", "double");
			cols.put("users.reputation", "integer");
			
			//class attributes and nominal values
			String[] classVals = new String[2];
			classVals[0]= "easy";
			classVals[1]= "difficult";
			generateArff.db2Arff(cols, "posts_" + tag + ".label", //cols 
						classVals,  //class nomial attributes
						"posts_" + tag + ", users",  //tables
						"posts_" + tag + ".owner_user_id = users.user_id and posts_" + tag + ".post_type_id = 1 and posts_" + tag + ".label is not null", //where condition
						m_recordCount,  m_generate + "_"  + tag + ".arff");
		}
		
		
	}
	
	/**
	 * Do post processing
	 * LOC feature is extracted here
	 * @throws QSLException
	 */
	private void postProcessing() throws SQLException {
		PostProcessing obj = new PostProcessing(m_dbOperations);
		for(String tag : m_tagList) {
			obj.removeHtmlTags("posts_" + tag);
		}
	}

}
