package cz.jtbank.konsolidace.xml;

import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.util.*;

public class UsersHandler implements ContentHandler 
{
  private boolean isUser = false;
  private String last = null;
  private Map map = new HashMap();
  private String key = null, value = null;

  public Map getParsedData() {
    return map;
  }
  
  public void characters(char[] values, int param, int param2) throws SAXException {
    if(isUser) {
      String str = new String ( values, param, param2 );
      //System.out.println ( last+"="+str );
            
      if("name".equalsIgnoreCase(last)) {
        key = str;
        value = "";
      }
      else if("description".equalsIgnoreCase(last)) {
        value = str;
      }
      if("name".equalsIgnoreCase(last) || "description".equalsIgnoreCase(last)) {
        map.put(key,value);
      }
    }
  }

  public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
    if("user".equalsIgnoreCase(qName)) isUser = true;
    if(isUser) last = qName;
  }
  
  public void endElement(String uri, String localName, String qName) throws SAXException {
    if("user".equalsIgnoreCase(qName)) isUser = false;
    last = null;
  }
  
  public void startDocument() throws org.xml.sax.SAXException {
  }

  public void endDocument() throws org.xml.sax.SAXException {
  }

  public void ignorableWhitespace(char[] values, int param, int param2) throws SAXException {
  }
  
  public void processingInstruction(String str, String str1) throws SAXException {
  }
  
  public void setDocumentLocator(Locator locator) {
  }
  
  public void startPrefixMapping(String prefix, String uri) {
  }

  public void endPrefixMapping(String prefix) {
  }
  
  public void skippedEntity(String name) {
  }
}