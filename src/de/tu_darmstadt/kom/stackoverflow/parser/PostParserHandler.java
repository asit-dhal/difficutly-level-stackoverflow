package de.tu_darmstadt.kom.stackoverflow.parser;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;

import de.tu_darmstadt.kom.stackoverflow.AppConfigs;
import de.tu_darmstadt.kom.stackoverflow.model.Post;
import de.tu_darmstadt.kom.stackoverflow.model.PostDao;
import de.tu_darmstadt.kom.stackoverflow.utility.Utility;


/**
 * @author Asit
 * Concrete class for Posts.xml file
 *
 */
public class PostParserHandler extends ParserHandler{
	
	private ArrayList<Post> m_postList;
	private PostDao m_postDao;
	Logger m_log = Logger.getLogger(AppConfigs.APPLICATION_NAME);
	private int cnt; //count no. of records processed

	/**
	 * Constructor
	 * @param postDao PostDao object(To interact with Posts table)
	 */
	public PostParserHandler(PostDao postDao) {
		m_postList = new ArrayList<Post>();
		cnt = 0;
		m_postDao = postDao;
	}

	/**
	 * function called insert all records
	 */
	public void lastInsert(){
		/**
		 * processNode does bulkInsert of AppConfigs.BULK_INSERT_SIZE records
		 * lastInsert will insert records if end of file is reached and some records are left 
		 * in the buffer.
		 */
		if(m_postList.size() > 0) {
			m_log.info("Inserting rest of the records");
			m_postDao.safeBulkInsert(m_postList);
			m_log.info("Total " + cnt + " records inserted...");
			m_postList = null;
		}
	}

	/**
	  * function called to process each node of Posts.xml file
	  * @param qName name of node
	  * @param attributes attributes of a node
	  */
	public void processNode(String qName, Attributes attributes) {
		
		if ("row".equals(qName)) { //for stackoverflow dump files, name of node is "row"
			try {
				Post post = new Post();

				for (int i = 0; i < attributes.getLength(); i++) {
					
					//process individual attributes
					if (attributes.getQName(i).equals("Id")) {
						post.setId(Integer.parseInt(attributes.getValue(i)));
					} else if (attributes.getQName(i).equals("PostTypeId")) {
						post.setPostTypeId(Integer.parseInt(attributes.getValue(i)));
					} else if (attributes.getQName(i).equals("AcceptedAnswerId")) {
						post.setAcceptedAnswerId(Integer.parseInt(attributes.getValue(i)));
					} else if (attributes.getQName(i).equals("ParentId")) {
						post.setParentID(Long.parseLong(attributes.getValue(i)));
					} else if (attributes.getQName(i).equals("CreationDate")) {
						// "2008-07-31T21:42:52.667" to be converted to Java date
						post.setCreationDate(Utility.stackStringToDate(attributes.getValue(i)));
					} else if (attributes.getQName(i).equals("Score")) {
						post.setScore(Long.parseLong(attributes
								.getValue(i)));
					} else if (attributes.getQName(i).equals("ViewCount")) {
						post.setViewCount(Long.parseLong(attributes
								.getValue(i)));
					} else if (attributes.getQName(i).equals("Body")) {
						post.setBody(attributes.getValue(i));
					} else if (attributes.getQName(i).equals("OwnerUserId")) {
						post.setOwnerUserId(Long
								.parseLong(attributes.getValue(i)));
					} else if (attributes.getQName(i).equals("LastEditorUserId")) {
						post.setLastEditorUserId(Long
								.parseLong(attributes.getValue(i)));
					} else if (attributes.getQName(i).equals("LastEditorDisplayName")) {
						post.setLastEditorDisplayName(attributes
								.getValue(i));
					} else if (attributes.getQName(i).toString()
							.equals("LastEditDate")) {
						post.setLastEditDate(Utility.stackStringToDate(attributes
								.getValue(i)));
						
					} else if (attributes.getQName(i).toString()
							.equals("LastActivityDate")) {
						post.setLastActivityDate(Utility.stackStringToDate(attributes
								.getValue(i)));

					} else if (attributes.getQName(i).toString()
							.equals("Title")) {
						post.setTitle(attributes.getValue(i));
						continue;
					} else if (attributes.getQName(i).toString()
							.equals("Tags")) {
						/*
						 * a tag is surrounded by angular
						 * braces(&lt, &t) Tags field conatins more
						 * than one tags. We need to remove &lt and
						 * &gt
						 */
						String tagText = attributes.getValue(i);
						tagText = tagText
								.replaceAll("&lt", tagText);
						tagText = tagText
								.replaceAll("&gt", tagText);
						String[] tags = tagText.split(";");
						ArrayList<String> allTags = new ArrayList<String>();
						for (int i1 = 0; i1 < tags.length; i1++)
							if (tags[i1].length() > 0)
								allTags.add(tags[i1]);
						post.setTags(allTags);

					} else if (attributes.getQName(i).toString()
							.equals("CommentCount")) {
						post.setCommentCount(Integer
								.parseInt(attributes.getValue(i)));

					} else if (attributes.getQName(i).toString()
							.equals("FavoriteCount")) {
						post.setFavoriteCount(Long
								.parseLong(attributes.getValue(i)));

					} else if (attributes.getQName(i).toString()
							.equals("ClosedDate")) {
						post.setClosedDate(Utility.stackStringToDate(attributes
								.getValue(i)));

					} else if (attributes.getQName(i).toString()
							.equals("CommunityOwnedDate")) {
						post.setCommunityOwnedDate(Utility.stackStringToDate(attributes
								.getValue(i)));

					} else if (attributes.getQName(i).toString()
							.equals("AnswerCount")) {
						post.setAnswerCount(Integer
								.parseInt(attributes.getValue(i)));
					}

					//else
					//	System.out.println("Invalid Attribute : " + attributes.getQName(i));
				
				}//for ends
				
				//if creation date is greater than or equal to 2013(2013-1900), data needs to inserted
				if(post.GetCreationDate().getYear() >= 113 ) { 
					m_postList.add(post);
					cnt ++;
				}
				
				/*
				 * for quick insert, data is inserted when buffer size 
				 * reaches to AppConfigs.BULK_INSERT_SIZE. 
				 */
				if (m_postList.size() == AppConfigs.BULK_INSERT_SIZE) {
					m_log.info("Inserting into posts " + AppConfigs.BULK_INSERT_SIZE + " records...");
					m_postDao.safeBulkInsert(m_postList);
					m_log.info("Total " + cnt + " records inserted...");
					m_postList = new ArrayList<Post>(); //buffer is reset 
				} 
			
			} catch (Exception e) {
				m_log.error(e.getMessage());
			}
		} //if ends

	}
}


