package cz.jtbank.konsolidace.xml;

import cz.jtbank.konsolidace.common.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.io.*;
import java.util.*;

public class UsersParser 
{
  public static void main(String[] args)
  {
//Je treba nastavit system property rucne nebo jako java parametr    
//-Dorg.xml.sax.driver=oracle.xml.parser.v2.SAXParser   
    if(System.getProperty("org.xml.sax.driver")==null) {
      System.setProperty("org.xml.sax.driver","oracle.xml.parser.v2.SAXParser");
    }
    FileInputStream fis = null;
    UsersHandler uh = new UsersHandler();
    try {
      fis = new FileInputStream(Constants.JAZN_XML_FILE);
      byte[] data = new byte[ fis.available() ];
      fis.read(data);
      fis.close();
         
      String xmlDoc = new String ( data ).trim();
   
      ByteArrayInputStream byteIS = new ByteArrayInputStream ( xmlDoc.getBytes() );
    
      XMLReader saxParser = XMLReaderFactory.createXMLReader();
      saxParser.setContentHandler( uh );
      saxParser.parse( new InputSource ( byteIS ) );
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    
    Map map = uh.getParsedData();
    System.out.println(map);
  }
}