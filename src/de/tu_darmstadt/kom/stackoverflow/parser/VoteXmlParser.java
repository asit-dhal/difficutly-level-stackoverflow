package de.tu_darmstadt.kom.stackoverflow.parser;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import de.tu_darmstadt.kom.stackoverflow.AppConfigs;
import de.tu_darmstadt.kom.stackoverflow.model.UserDao;
import de.tu_darmstadt.kom.stackoverflow.model.VoteDao;
import de.tu_darmstadt.kom.stackoverflow.utility.Utility;

public class VoteXmlParser {

	private VoteDao m_voteDao;
	Logger m_log = Logger.getLogger(AppConfigs.APPLICATION_NAME);
	
	public VoteXmlParser(VoteDao voteDao) {
		m_voteDao = voteDao;
	}
	
    public void parseXml(InputStream in)
    {
        try
        {
            VoteParserHandler handler = new VoteParserHandler(m_voteDao);
            XMLReader parser = XMLReaderFactory.createXMLReader();
            parser.setContentHandler(handler);
            InputSource source = new InputSource(in);
            parser.parse(source);
 
        } catch (SAXException e) {
        	m_log.fatal(Utility.stackTrace(e));
        } catch (IOException e) {
        	m_log.fatal(Utility.stackTrace(e));
        }
    }
}
