package org.wso2.siddhi.gpl.extension.nlp.dictionary;

import org.apache.log4j.Logger;
import org.wso2.siddhi.gpl.extension.nlp.utility.Constants;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by malithi on 8/29/14.
 */
public class DictionaryHandler extends DefaultHandler {
    private static Logger logger = Logger.getLogger(DictionaryHandler.class);

    private Constants.EntityType entityType;
    private Dictionary dictionary;
    private String value = null;
    private boolean read = false;

    public DictionaryHandler(Constants.EntityType entityType, Dictionary dictionary) {
        this.entityType = entityType;
        this.dictionary = dictionary;
    }

    @Override
    public void startDocument() throws SAXException {
        logger.info("Loading dictionary ...");
    }

    @Override
    public void endDocument() throws SAXException {
        logger.info("Dictionary loading [COMPLETED]");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase(Constants.DictionaryTag.ENTITY.getTag())){
            read = attributes.getValue(Constants.DictionaryTag.ID.getTag()).equalsIgnoreCase(entityType.name());
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(read){
            if(qName.equalsIgnoreCase(Constants.DictionaryTag.ENTRY.getTag())){
                dictionary.addEntry(entityType,value);
            }
        }
    }

    @Override
    public void characters(char[] chars, int start, int length) throws SAXException {
        value = String.copyValueOf(chars, start, length).trim();
    }

    @Override
    public void warning(SAXParseException e) throws SAXException {
        logger.warn(e.getMessage());
        throw e;
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        logger.error(e.getMessage());
        throw e;
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        logger.fatal(e.getMessage());
        throw e;
    }
}
