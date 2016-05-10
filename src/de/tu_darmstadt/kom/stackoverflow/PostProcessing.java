package de.tu_darmstadt.kom.stackoverflow;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.apache.log4j.Logger;

/**
 * PostProcessing class removes HTML Tags from text of the questions
 * @author Asit
 *
 */
public class PostProcessing {

	private DbOperations m_dbOperations;
	private Logger m_log;
	
	/**
	 * Creates PostProcessing object 
	 * @param dbOperations DbOperations Object
	 */
	public PostProcessing(DbOperations dbOperations) {
		m_dbOperations = dbOperations;
		m_log = Logger.getLogger(AppConfigs.APPLICATION_NAME);
	}
	
	/**
	 * Removes HTML tags from the text of the questions
	 * @throws SQLException
	 */
		
	public void removeHtmlTags(String tableName) throws SQLException {
		m_log.info("Removing HTML tags in table : " + tableName);
		Connection conn = null;
		Statement stmt = null;
		String body = null;
		int stack_id = 0;
		int records = 0;
		int loc = 0;
		try {
			conn = m_dbOperations.getConnection();
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = stmt.executeQuery("select stack_id, body, code, loc from " + tableName + " where post_type_id=1");
			
			//fetch stack_id(primary key), body, loc from database and update it
			while(rs.next()) {
				body = rs.getString("body");
				stack_id = rs.getInt("stack_id");
				String code = getCode(body);
				rs.updateString("code", code);
				rs.updateDouble("loc", getLocPercentage(body, code));
				body = body.replaceAll("\"", "");
				body = removeTags(body);
				rs.updateString("body", body);
				rs.updateRow();
				body = null;
				records ++;
			}
		}catch(SQLException e) {
			m_log.error(e.getMessage());
			m_log.error("String : " + body);
			m_log.error("Stack id : " + stack_id);
		}finally{
			if ( stmt != null )
				stmt.close();
		}
		m_log.info("Records processed : " + records);
	}
	
	/**
	 * gets code section from question body
	 * multiple code sections are joined
	 * @param html
	 * @return 
	 */
	public String getCode(String html) {
		String code = "";
		Document doc = Jsoup.parse(html);
		Elements firstH1 = doc.select("code");
		for(int i=0; i<firstH1.size(); i++)
			code += firstH1.get(i) + "\n";
		return code;
	}
	
	/**
	 * removes code section from question body
	 * @param html
	 * @return question body without any text
	 */
	private String removeTags(String html) {
		String noCode = "";
		Document doc = Jsoup.parse(html);
		doc.select("code").remove();
		return doc.text();
	}
	
	/**
	 * gets LOC percentage a feature
	 * LOC_percentage = (loc / loc + bodyLength) x 100
	 * @param body
	 * @param code
	 * @return
	 */
	private double getLocPercentage(String body, String code) {
		int loc = 0;
		if (code.length() > 0)
			loc= code.split("\r\n|\r|\n").length;
		//body length is no. of statements
		int bodyLength = body.split("\\.").length; 
		//m_log.debug("LOC : " + loc);
		//m_log.debug("body length : " + bodyLength);
		//m_log.debug("percentage : " + loc*100.0/(loc+bodyLength));
		return loc*100.0/(loc+bodyLength); 
		
	}
	
}
