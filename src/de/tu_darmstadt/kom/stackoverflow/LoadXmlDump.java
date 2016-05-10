package de.tu_darmstadt.kom.stackoverflow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import de.tu_darmstadt.kom.stackoverflow.model.PostDao;
import de.tu_darmstadt.kom.stackoverflow.model.UserDao;
import de.tu_darmstadt.kom.stackoverflow.model.VoteDao;
import de.tu_darmstadt.kom.stackoverflow.parser.PostXmlParser;
import de.tu_darmstadt.kom.stackoverflow.parser.UserXmlParser;
import de.tu_darmstadt.kom.stackoverflow.parser.VoteXmlParser;

public class LoadXmlDump {
	private List<String> m_dumpFileList;
	Logger m_log = Logger.getLogger(AppConfigs.APPLICATION_NAME);
	private DbOperations m_dbOperations;

	public LoadXmlDump(String dumpFileList, DbOperations dbOperations) {
		m_dumpFileList = Arrays.asList(dumpFileList.split(","));
		m_dbOperations = dbOperations;
		process();
	}

	private void process() {
		for (String filePath : m_dumpFileList) {
			int index = filePath.lastIndexOf("\\");
			String fileName = filePath.substring(index + 1);

			if (fileName.equals("Users.xml")) {
				m_log.info("Loading User xml file : " + filePath);
				File xmlFile = new File(filePath);
				UserDao userDao = new UserDao(m_dbOperations);
				UserXmlParser parser = new UserXmlParser(userDao);
				// Parse the file
				try {
					parser.parseXml(new FileInputStream(xmlFile));
				} catch (FileNotFoundException e) {
					m_log.error(e.getMessage());
				}

			} else if (fileName.equals("Posts.xml")) {
				m_log.info("Loading Post xml file : " + filePath);
				File xmlFile = new File(filePath);
				PostDao postDao = new PostDao(m_dbOperations);
				PostXmlParser parser = new PostXmlParser(postDao);
				// Parse the file
				try {
					parser.parseXml(new FileInputStream(xmlFile));
				} catch (FileNotFoundException e) {
					m_log.error(e.getMessage());
				}

			} else if (fileName.equals("Votes.xml")) {
				m_log.info("Loading Vote xml file : " + filePath);
				File xmlFile = new File(filePath);
				VoteDao voteDao = new VoteDao(m_dbOperations);
				VoteXmlParser parser = new VoteXmlParser(voteDao);
				// Parse the file
				try {
					parser.parseXml(new FileInputStream(xmlFile));
				} catch (FileNotFoundException e) {
					m_log.error(e.getMessage());
				}
			} else {
				m_log.error("Invalid Filenmae: " + filePath);
			}
		}
	}

}
