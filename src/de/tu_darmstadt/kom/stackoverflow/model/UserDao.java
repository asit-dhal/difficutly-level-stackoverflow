package de.tu_darmstadt.kom.stackoverflow.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.log4j.Logger;

import de.tu_darmstadt.kom.stackoverflow.AppConfigs;
import de.tu_darmstadt.kom.stackoverflow.DbOperations;

/**
 * Data Access Class for Users object
 * @author Asit
 */
public class UserDao{
	
	private DbOperations m_dbOperations;
	private String m_insertQuery;
	private String m_selectQuery;
	Logger m_log = Logger.getLogger(AppConfigs.APPLICATION_NAME);
	
	/**
	 * Constructor for UserDao
	 * @param dbOperations
	 */
	public UserDao(DbOperations dbOperations) {
		m_dbOperations = dbOperations;
		
		String[] all_columns = { "user_id", "reputation", "views", "up_votes", "down_votes", "account_id"};
		StringBuilder strQuery1 = new StringBuilder();
		for(String s: all_columns) {
			strQuery1.append(s).append(",");
		}
		strQuery1.setLength(strQuery1.length()-1);

		StringBuilder strQuery2 = new StringBuilder();
		for(String s: all_columns) {
			strQuery2.append("?").append(",");
		}
		strQuery2.setLength(strQuery2.length()-1);

		m_insertQuery = "insert into users (" + strQuery1.toString() + ")"
				+ " values ( " + strQuery2.toString() + ")";
		m_selectQuery = " select " + strQuery1.toString() + " from users where user_id = ";
	}
	
	/**
	 * bulk insert of list of User object
	 * @param users users buffer
	 */
	public void safeBulkInsert(List<User> users) {
		try {
			final PreparedStatement stmt = m_dbOperations.getConnection().prepareStatement(m_insertQuery);
			for(User user: users) {
				stmt.setObject(1, user.getId());
				stmt.setObject(2, user.getReputation());
				stmt.setObject(3, user.getViews());
				stmt.setObject(4, user.getUpVotes());
				stmt.setObject(5, user.getDownVotes());
				stmt.setObject(6, user.getAccountId());
				stmt.addBatch();
			}
			
			stmt.executeBatch();
			stmt.close();
			
		}catch(SQLException e) {
			m_log.error(e.getMessage());
		}
		
	}
	
	/**
	 * gets an user from database
	 * @param userId stackoverflow user id of the user
	 * @return User object
	 */
	public User getUser(int userId) {
		User user = new User();
		Statement stmt = null;
		try {
			stmt = m_dbOperations.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(m_selectQuery + userId );
            while(rs.next())
            {
                user.setId(userId);
                user.setReputation(rs.getInt(2));
                user.setViews(rs.getInt(3));
                user.setUpVotes(rs.getInt(4));
                user.setDownVotes(rs.getInt(5));
                user.setAccountId(rs.getInt(6));
                
            }
            rs.close();
            stmt.close();
            
		}catch(SQLException e) {
			m_log.error(e.getMessage());
		}
		return user;
	}

}
