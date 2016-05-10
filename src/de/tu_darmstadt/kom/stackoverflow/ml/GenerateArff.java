package de.tu_darmstadt.kom.stackoverflow.ml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.experiment.InstanceQuery;
import de.tu_darmstadt.kom.stackoverflow.AppConfigs;
import de.tu_darmstadt.kom.stackoverflow.DbOperations;

/**
 * Generates ARFF file from mysql
 * @author Asit
 *
 */
public class GenerateArff {
	
	private DbOperations m_dbOperations;
	Logger m_log = Logger.getLogger(AppConfigs.APPLICATION_NAME);
	private PrintWriter m_writer;
	private HashMap<String, String> m_columns;
	
	FastVector m_fastVector;
	Instances m_instances;
	
	public GenerateArff(DbOperations dbOperations) {
		m_dbOperations = dbOperations;
	}
	
	
	/**
	 * @param columns
	 * @param classColumn
	 * @param classValues
	 * @param table
	 * @param whereClause
	 * @param limits
	 * @param filename
	 * @throws Exception
	 */
	public void db2Arff(HashMap<String, String> columns, String classColumn,
			String[] classValues, String table, String whereClause, int limits, String filename) throws Exception{
		
		m_fastVector = new FastVector();
		m_log.info("File to be generated : " + filename);
		
		m_columns = columns;
		String selecedQuery = "select ";
		StringBuilder query = new StringBuilder();
		for(String s: columns.keySet()) {
			query.append(s).append(",");
		}

		selecedQuery += query.toString() + " " + classColumn + " from " + table + " where " + whereClause + " limit " + limits;
		
		m_log.info("Query : " + selecedQuery);
		
		
		for (Map.Entry<String, String> entry : columns.entrySet()) {
			String key = entry.getKey().replace(".","-");
			if(entry.getValue() == "integer" || entry.getValue() == "double") {
				m_fastVector.addElement(new Attribute(key));
			}else if (entry.getValue() == "string") {
				m_fastVector.addElement(new Attribute(key, (FastVector)null));
			}

		}
		
		FastVector classAttrs = new FastVector(); 
		for (String attr: classValues) 
			classAttrs.addElement(attr); 
		//Attribute attr1 = new Attribute("my-nominal", myNomVals); - See more at: http://www.zitnik.si/wordpress/2011/09/25/quick-intro-to-weka/#sthash.RUSsEgG0.dpuf
		//m_fastVector.addElement(new Attribute("attr" + classColumn.replace(".", "-"), (FastVector)null));
		m_fastVector.addElement(new Attribute("attr-" + classColumn.replace(".", "-"), classAttrs));
		
		// 2. create Instances object
		m_instances = new Instances("Stackoverflow_Dump", m_fastVector, 0);
		m_instances.setClassIndex(m_instances.numAttributes()-1);
				
		generate(selecedQuery, classColumn, filename);
	}
	
	public void generate(String query, String classColumn, String filename) {
		Connection conn = null;
		Statement stmt = null;
		
		int noRecords = 0;
		
		try {
			conn = m_dbOperations.getConnection();
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery(query);
		
			double vals[];
			
			while(rs.next()) {
		
				vals = new double[m_instances.numAttributes()];
				
				int i=0;
				for (HashMap.Entry<String, String> entry : m_columns.entrySet()) {
					
					if (entry.getValue().equals("string"))
						vals[i] = m_instances.attribute(i).addStringValue(rs.getString(entry.getKey()));
					else if (entry.getValue().equals("double"))
						vals[i] = rs.getDouble(entry.getKey());
					else 
						vals[i] = rs.getInt(entry.getKey());

					i++;
				}
				
				//vals[i] = m_instances.attribute(i).addStringValue(rs.getString(classColumn));
				vals[i] = m_instances.attribute(i).indexOfValue(rs.getString(classColumn));
				
				m_instances.add(new Instance(1.0, vals));
				noRecords ++;
			}
			
		}catch(SQLException e) {
			m_log.error(e.getMessage());
		}finally{
			if ( stmt != null )
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
		}
		
		// Create file
		try {
			FileWriter fstream = null;
			fstream = new FileWriter(filename, true);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(m_instances.toString());
			out.close();
		} catch (IOException e) {
			m_log.error(e.getMessage());
		}
		
		m_log.info("No. of records written: " + noRecords);
	}
	
	
}
