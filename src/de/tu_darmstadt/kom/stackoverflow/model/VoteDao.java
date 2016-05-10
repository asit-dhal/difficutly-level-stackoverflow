package de.tu_darmstadt.kom.stackoverflow.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import de.tu_darmstadt.kom.stackoverflow.AppConfigs;
import de.tu_darmstadt.kom.stackoverflow.DbOperations;

public class VoteDao {

	private DbOperations m_dbOperations;
	private String m_insertQuery;
	private String m_selectQuery;
	Logger m_log = Logger.getLogger(AppConfigs.APPLICATION_NAME);
	
	public VoteDao(DbOperations dbOperations) {
		m_dbOperations = dbOperations;
		
		String[] all_columns = { "vote_id", "post_id", "vote_type_id", "user_id", "creation_date", "bounty_amount"};
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

		m_insertQuery = " insert into votes (" + strQuery1.toString() + ")"
				+ " values ( " + strQuery2.toString() + ")";
	}
	
	public void safeBulkInsert(List<Vote> votes) {
		
		try {
			//m_log.info(m_insertQuery);
			final PreparedStatement stmt = m_dbOperations.getConnection().prepareStatement(m_insertQuery);
			for(Vote vote: votes) {
				stmt.setObject(1, vote.getId());
				stmt.setObject(2, vote.getPostId());
				stmt.setObject(3, vote.getVoteTypeId());
				stmt.setObject(4, vote.getUserId());
				stmt.setObject(5, vote.getCreationDate());
				stmt.setObject(6, vote.getBountyAmount());
				stmt.addBatch();
			}
			
			stmt.executeBatch();
			stmt.close();
			
		}catch(SQLException e) {
			m_log.error(e.getMessage());
		}
		
	}

}
