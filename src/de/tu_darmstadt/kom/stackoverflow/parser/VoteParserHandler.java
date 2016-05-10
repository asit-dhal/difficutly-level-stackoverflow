package de.tu_darmstadt.kom.stackoverflow.parser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;

import de.tu_darmstadt.kom.stackoverflow.AppConfigs;
import de.tu_darmstadt.kom.stackoverflow.model.Vote;
import de.tu_darmstadt.kom.stackoverflow.model.VoteDao;
import de.tu_darmstadt.kom.stackoverflow.utility.Utility;

public class VoteParserHandler extends ParserHandler{

	private ArrayList<Vote> m_voteList;
	private VoteDao m_voteDao;
	Logger m_log = Logger.getLogger(AppConfigs.APPLICATION_NAME);
	private int cnt;
	
	/**
	 * 
	 */
	public VoteParserHandler(VoteDao voteDao) {
		m_voteList = new ArrayList<Vote>();
		cnt = 0;
		m_voteDao = voteDao;
	}

	
	public void lastInsert(){
		if(m_voteList.size() > 0) {
			m_log.info("Inserting rest of the records");
			m_voteDao.safeBulkInsert(m_voteList);
			m_log.info("Total " + cnt + " records inserted...");
			m_voteList = null;
		}
	}

	public void processNode(String qName, Attributes attributes) {
		if ("row".equals(qName)) {
			try {
				Vote vote = new Vote();

				for (int i = 0; i < attributes.getLength(); i++) {
					if (attributes.getQName(i).equals("Id"))
						vote.setId(Integer.parseInt(attributes.getValue(i)));
					
					else if (attributes.getQName(i).equals("PostId"))
						vote.setPostId(Integer.parseInt(attributes
								.getValue(i)));
					
					else if (attributes.getQName(i).equals("CreationDate"))
						vote.setCreationDate(Utility
								.stackStringToDate(attributes.getValue(i)));
					
					else if (attributes.getQName(i).equals("VoteTypeId"))
						vote.setVoteTypeId(Integer.parseInt(attributes.getValue(i)));
					
					else if (attributes.getQName(i).equals("UserId"))
						vote.setUserId(Integer.parseInt(attributes.getValue(i)));
					
					else if (attributes.getQName(i).equals("BountyAmount"))
						vote.setBountyAmount(Integer.parseInt(attributes
								.getValue(i)));
					else
						System.out.println("Invalid Attribute : " + attributes.getQName(i));
				}
				
				
				if(vote.getCreationDate().getYear() >= 113) { //we need votes created which are older than 2013(2013-1900)
					m_voteList.add(vote);
					cnt ++;
					
				}
				
				if (m_voteList.size() == AppConfigs.BULK_INSERT_SIZE ) {
					m_log.info("Inserting into votes " + AppConfigs.BULK_INSERT_SIZE + " records...");
					m_voteDao.safeBulkInsert(m_voteList);
					m_log.info("Total " + cnt + " records inserted...");
					m_voteList = new ArrayList<Vote>();
				} // bulk insert BULK_INSERT_SIZE(55000) User objects
			} catch (ParseException e) {
				m_log.error(e.getMessage());
			}
		}

	}

}
