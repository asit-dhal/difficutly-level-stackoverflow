package de.tu_darmstadt.kom.stackoverflow.parser;

import java.io.InputStream;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import de.tu_darmstadt.kom.stackoverflow.AppConfigs;
import de.tu_darmstadt.kom.stackoverflow.model.UserDao;
import de.tu_darmstadt.kom.stackoverflow.utility.Utility;
 
/**
 * @author Asit
 * Class to parse Users.xml file
 */
public class UserXmlParser
{
	private UserDao m_userDao;
	Logger m_log = Logger.getLogger(AppConfigs.APPLICATION_NAME);
	
	/**
	 * Constructor
	 * @param userDao UserDao object
	 */
	public UserXmlParser(UserDao userDao) {
		m_userDao = userDao;
	}
	
	/**
	 * Entry point of parser
	 * @param in InputStream which points to Users.xml file in the disk
	 */
    public void parseXml(InputStream in)
    {
        try
        {
            UserParserHandler handler = new UserParserHandler(m_userDao);
            XMLReader parser = XMLReaderFactory.createXMLReader();
            parser.setContentHandler(handler);
            InputSource source = new InputSource(in);
            parser.parse(source);
 
        } catch (SAXException e) {
        	m_log.fatal(Utility.stackTrace(e));
        } catch (Exception e) {
        	m_log.fatal(Utility.stackTrace(e));
        }
    }
}