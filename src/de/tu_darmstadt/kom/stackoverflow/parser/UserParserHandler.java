/**
 * 
 */
package de.tu_darmstadt.kom.stackoverflow.parser;

/**
 * @author Asit
 *
 */
import java.text.ParseException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;

import de.tu_darmstadt.kom.stackoverflow.AppConfigs;
import de.tu_darmstadt.kom.stackoverflow.model.User;
import de.tu_darmstadt.kom.stackoverflow.model.UserDao;
import de.tu_darmstadt.kom.stackoverflow.utility.Utility;

/**
 * @author Asit
 * Concrete class for Users.xml file
 */
public class UserParserHandler extends ParserHandler {

	private ArrayList<User> m_userList;
	private UserDao m_userDao;
	Logger m_log = Logger.getLogger(AppConfigs.APPLICATION_NAME);
	private int cnt;

	/**
	 * Constructor
	 * @param userDao UserDao object(To interact with Users table)
	 */
	public UserParserHandler(UserDao userDao) {
		m_userList = new ArrayList<User>();
		cnt = 0;
		m_userDao = userDao;
	}

	/**
	 * function called insert all records
	 */
	public void lastInsert(){
		/*
		 * processNode does bulkInsert of AppConfigs.BULK_INSERT_SIZE records
		 * lastInsert will insert records if end of file is reached and some records are left 
		 * in the buffer.
		 */
		if(m_userList.size() > 0) {
			m_log.info("Inserting rest of the records");
			m_userDao.safeBulkInsert(m_userList);
			m_log.info("Total " + cnt + " records inserted...");
			m_userList = null;
		}
	}

	/**
	  * function called to process each node of Users.xml file
	  * @param qName name of node
	  * @param attributes attributes of a node
	  */
	public void processNode(String qName, Attributes attributes) {
		if ("row".equals(qName)) { //for stackoverflow dump files, name of node is "row"
			cnt ++;
			try {
				User user = new User();

				for (int i = 0; i < attributes.getLength(); i++) {
					if (attributes.getQName(i).equals("Id"))
						user.setId(Integer.parseInt(attributes.getValue(i)));
					else if (attributes.getQName(i).equals("Reputation"))
						user.setReputation(Integer.parseInt(attributes
								.getValue(i)));
					else if (attributes.getQName(i).equals("CreationDate"))
						user.setCreationDate(Utility
								.stackStringToDate(attributes.getValue(i)));
					else if (attributes.getQName(i).equals("DisplayName"))
						user.setDisplayName(attributes.getValue(i));
					else if (attributes.getQName(i).equals("LastAccessDate"))
						user.setLastAccessDate(Utility
								.stackStringToDate(attributes.getValue(i)));
					else if (attributes.getQName(i).equals("WebsiteUrl"))
						user.setWebsiteUrl(attributes.getValue(i));
					else if (attributes.getQName(i).equals("Location"))
						user.setLocation(attributes.getValue(i));
					else if (attributes.getQName(i).equals("AboutMe"))
						user.setAboutMe(attributes.getValue(i));
					else if (attributes.getQName(i).equals("Views"))
						user.setViews(Integer.parseInt(attributes.getValue(i)));
					else if (attributes.getQName(i).equals("UpVotes"))
						user.setUpVotes(Integer.parseInt(attributes.getValue(i)));
					else if (attributes.getQName(i).equals("DownVotes"))
						user.setDownVotes(Integer.parseInt(attributes
								.getValue(i)));
					else if (attributes.getQName(i).equals("AccountId"))
						user.setAccountId(Integer.parseInt(attributes
								.getValue(i)));
					else if (attributes.getQName(i).equals("ProfileImageUrl"))
						user.setProfileImageUrl(attributes.getValue(i));
					else if (attributes.getQName(i).equals("Age"))
						user.setAge(Integer.parseInt(attributes
								.getValue(i)));
					//else
					//	System.out.println("Invalid Attribute : " + attributes.getQName(i));
				}

				m_userList.add(user);
				
				/*
				 * for quick insert, data is inserted when buffer size 
				 * reaches to AppConfigs.BULK_INSERT_SIZE. 
				 */
				if (m_userList.size() == AppConfigs.BULK_INSERT_SIZE ) {
					m_log.info("Inserting into users " + AppConfigs.BULK_INSERT_SIZE + " records...");
					m_userDao.safeBulkInsert(m_userList);
					m_log.info("Total " + cnt + " records inserted...");
					m_userList = new ArrayList<User>();
				}
			} catch (ParseException e) {
				m_log.error(e.getMessage());
			}
		}

	}

	
}