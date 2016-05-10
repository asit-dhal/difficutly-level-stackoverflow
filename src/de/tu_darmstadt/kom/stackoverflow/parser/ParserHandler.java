package de.tu_darmstadt.kom.stackoverflow.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Asit
 * ParserHandler class
 *
 */
abstract public class ParserHandler extends DefaultHandler{
	
	/**
	 * called when startDocument is found in XML file 
	 */
	public void startDocument() throws SAXException {

	}
	
	/**
	 * called when endDocument is found in XML file 
	 */
	public void endDocument() throws SAXException {
		lastInsert();
	}
	
	/**
	 * called when a new xml node is found in the file 
	 * @param uri String
	 * @param localName String
	 * @param qName name of the node
	 * @param attributes attributes of an XML node
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		processNode(qName, attributes);
	}
	
	/**
	 * function to be called when end of document is reached
	 */
	abstract public void lastInsert();
	
	/**
	 * called when a new node is found in xml file
	 */
	abstract public void processNode(String qName, Attributes attriutes);

}
