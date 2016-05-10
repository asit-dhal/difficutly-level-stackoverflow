package de.tu_darmstadt.kom.stackoverflow.parser;

import java.io.InputStream;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import de.tu_darmstadt.kom.stackoverflow.AppConfigs;
import de.tu_darmstadt.kom.stackoverflow.model.PostDao;
import de.tu_darmstadt.kom.stackoverflow.utility.Utility;



/**
 * @author Asit
 * Class to parse Posts.xml file
 */
public class PostXmlParser {

	private PostDao m_postDao;
	
	Logger m_log = Logger.getLogger(AppConfigs.APPLICATION_NAME);
	
	/**
	 * Constructor
	 * @param postDao PostDao object
	 */
	public PostXmlParser(PostDao postDao) {
		m_postDao = postDao;
	}
	
	
	/**
	 * Entry point of parser
	 * @param in InputStream which points to Posts.xml file in the disk
	 */
    public void parseXml(InputStream in)
    {
        try
        {
            PostParserHandler handler = new PostParserHandler(m_postDao);
            XMLReader parser = XMLReaderFactory.createXMLReader();
            parser.setContentHandler(handler);
            InputSource source = new InputSource(in);
            parser.parse(source);
 
        } catch (SAXParseException e) {
        	m_log.error("Parsing error, line " + e.getLineNumber()+ ", column no. " + e.getColumnNumber());
            m_log.fatal(Utility.stackTrace(e));
        } catch (Exception e) {
        	m_log.fatal(Utility.stackTrace(e));
        }
    }
}
