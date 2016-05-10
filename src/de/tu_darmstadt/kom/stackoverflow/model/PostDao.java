package de.tu_darmstadt.kom.stackoverflow.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import de.tu_darmstadt.kom.stackoverflow.AppConfigs;
import de.tu_darmstadt.kom.stackoverflow.DbOperations;

/**
 * Data Access Class for Posts object
 * @author Asit
 *
 */
public class PostDao {
	
	private DbOperations m_dbOperations;
	private String m_insertQuery;
	Logger m_log = Logger.getLogger(AppConfigs.APPLICATION_NAME);
	
	/**
	 * Constructor for PostDao
	 * @param dbOperations
	 */
	
	public PostDao(DbOperations dbOperations) {
		m_dbOperations = dbOperations;
		
		String[] all_columns = { "stack_id", "post_type_id", "accepted_answer_id", "parent_id", "creation_date", 
				"score", "view_count", "body", "owner_user_id", "last_editor_user_id", 
				"last_editor_display_name", "last_edit_date", "last_activity_date", "title", 
				"tags", "answer_count", "comment_count", "favorite_count", "status", "closed_date", 
				"community_owned_date" };
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

		m_insertQuery = " insert into posts (" + strQuery1.toString() + ")"
				+ " values ( " + strQuery2.toString() + ")";

	}
	
	/**
	 * bulk insert of list of Post object
	 * @param posts
	 * 
	 */
	public void safeBulkInsert(List<Post> posts) {
		
		try {
			final PreparedStatement stmt = m_dbOperations.getConnection().prepareStatement(m_insertQuery);
			for(Post post: posts) {
				stmt.setObject(1, post.GetId());
				stmt.setObject(2, post.GetPostTypeId());
				stmt.setObject(3, post.GetAcceptedAnswerId());
				stmt.setObject(4, post.GetParentId());
				stmt.setObject(5, post.GetCreationDate());
				stmt.setObject(6, post.GetScore());
				stmt.setObject(7, post.GetViewCount());
				stmt.setObject(8, post.GetBody());
				stmt.setObject(9, post.GetOwnerUserId());
				stmt.setObject(10, post.GetLastEditorUserId());
				stmt.setObject(11, post.GetLastEditorDisplayName());
				stmt.setObject(12, post.GetLastEditDate());
				stmt.setObject(13, post.GetLastActivityDate());
				stmt.setObject(14, post.GetTitle());
				StringBuilder tags = new StringBuilder();
				
				//collection of tags to one tag separated by :
				for(String s: post.GetTags()) {
					tags.append(s).append(":");
				}
				if (tags.length() > 0) 
					tags.setLength(tags.length()-1);
				
				stmt.setObject(15, tags.toString());
				stmt.setObject(16, post.GetAnswerCount());
				stmt.setObject(17, post.GetCommentCount());
				stmt.setObject(18, post.GetFavoriteCount());
				stmt.setObject(19, post.GetStatus());
				stmt.setObject(20, post.GetClosedDate());
				stmt.setObject(21, post.GetCommunityOwnedDate());
				stmt.addBatch();
			}
			
			stmt.executeBatch();
			stmt.close();
			
		}catch(SQLException e) {
			m_log.error(e.getMessage());
		}
		
	}

}
