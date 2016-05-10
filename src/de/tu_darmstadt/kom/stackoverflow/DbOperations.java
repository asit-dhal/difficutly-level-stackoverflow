package de.tu_darmstadt.kom.stackoverflow;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.log4j.Logger;

import de.tu_darmstadt.kom.stackoverflow.model.Post;
import de.tu_darmstadt.kom.stackoverflow.utility.AppExceptions;
import weka.core.Attribute;
import weka.core.Instances;
import weka.experiment.InstanceQuery;

/**
 * DbOperation Class deals with all database interactions
 * @author Asit
 *
 */
/**
 * @author Asit
 *
 */
public class DbOperations {

	//private String DATABASE_URL = "jdbc:mysql://localhost/";	
	private Connection m_connection;
	private String m_query;
	private String m_dbUrl;
	private String m_password;
	private String m_username;
	Logger m_log = Logger.getLogger(AppConfigs.APPLICATION_NAME);
	
	/**
	 * Creates DbOperations Object
	 * @param username username of database
	 * @param password password of database
	 * @param db database name
	 */
	public DbOperations(String username, String password, String db, String url) {
		try {
			String myDriver = "org.gjt.mm.mysql.Driver";
			m_dbUrl = "jdbc:mysql://" + url + "/" + db + "?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8";
			Class.forName(myDriver);
		    m_password = password;
		    m_username = username;
		    m_connection = DriverManager.getConnection(m_dbUrl, username, password);
		} catch (Exception e){
			m_log.error(e.getMessage());
			throw new AppExceptions("DbOperation object creation failed");
		}
		
	}
	
	
	/**
	 * Executes an SQL Update, no. of records updated are logged 
	 * @param sql Update/Insert sql query
	 * @return true if successfully executed else false
	 */
	public boolean executeUpdate(String sql) {
		Statement stmt = null;
		
		try {
			stmt = m_connection.createStatement();
			m_log.info("Executing : " + sql);
			int updateCount = stmt.executeUpdate(sql);
			m_log.info("No. of rows updated : " + updateCount);
		}catch(SQLException e) {
			m_log.error(e.getMessage());
			return false;
		}
		return true;
	}
	
	
	/**
	 * Closes connection to database
	 */
	public void cleanup() {
		try {
			m_connection.close();
		}catch(SQLException e) {
			m_log.error(e.getMessage());
		}
	}
	
	/**
	 * @return Connection object
	 */
	public Connection getConnection() {
		return m_connection;
	}
	
	
	/**
	 * Converts a relational table to arff file
	 * @param arffQuery SQL Query that returns a no. of records
	 * @param filename Name of the ARFF file 
	 * @throws Exception
	 */
	public void db2Arff(String arffQuery, String filename) throws Exception{
		
		m_log.info("File to be generated : " + filename);
		
		//Reference: sample code sipped in weka application
		InstanceQuery query = new InstanceQuery();
	    query.setDatabaseURL(m_dbUrl);
	    query.setUsername(m_username);
	    query.setPassword(m_password);
	    query.setQuery(arffQuery);
	    //query.setOptions(options);
	    
	    Instances data = query.retrieveInstances();
	    data.setClassIndex(data.numAttributes() - 1);
	    System.out.println("type : " + data.attribute(1));
	    //Attribute body = new Attribute()
	    
	    
	    System.gc(); //free memory
	    
	    File file = new File(filename);
	    FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(data.toString());
		bw.close();
	}


}
