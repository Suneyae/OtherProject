 package cn.sinobest.framework.web.his;
 
 import java.io.StringReader;
 import java.util.HashMap;
 import javax.xml.parsers.SAXParser;
 import javax.xml.parsers.SAXParserFactory;
 import org.xml.sax.Attributes;
 import org.xml.sax.InputSource;
 import org.xml.sax.SAXException;
 import org.xml.sax.helpers.DefaultHandler;
 
 public class XMLSAXParser
   extends DefaultHandler
 {
   FuncModel fucModel = null;
   HashMap dataSetMap = null;
   HashMap fieldsMap = null;
   String dataSetName = null;
   Long rowID = null;
   
   public FuncModel getInputData(String xmlStr)
     throws Exception
   {
     StringBuffer sb = new StringBuffer(xmlStr);
     int startPos = sb.indexOf("<Output>");
     int endPos = sb.indexOf("</Output>");
     if ((startPos == -1) || (endPos == -1))
     {
       startPos = 0;
       endPos = 0;
     }
     if (endPos > 0) {
       sb.replace(startPos, endPos + 9, "");
     }
     parse(sb.toString());
     sb.delete(0, sb.length() - 1);
     sb = null;
     return this.fucModel;
   }
   
   public FuncModel getOutputData(String xmlStr)
     throws Exception
   {
     StringBuffer sb = new StringBuffer(xmlStr);
     int startPos = sb.indexOf("<Input>");
     int endPos = sb.indexOf("</Input>");
     if ((startPos == -1) || (endPos == -1))
     {
       startPos = 0;
       endPos = 0;
     }
     if (endPos > 0) {
       sb.replace(startPos, endPos + 8, "");
     }
     parse(xmlStr);
     sb.delete(0, sb.length() - 1);
     sb = null;
     return this.fucModel;
   }
   
   public void startDocument()
     throws SAXException
   {
     this.fucModel = new FuncModel();
   }
   
   public void startElement(String uri, String localName, String qName, Attributes attributes)
     throws SAXException
   {
     if ((qName.equalsIgnoreCase("FuncParam")) || (qName.equalsIgnoreCase("ReturnParam"))) {
       this.fucModel.addParam(attributes.getValue(0), attributes.getValue(1));
     }
     if (qName.equalsIgnoreCase("DataSet"))
     {
       this.dataSetMap = new HashMap();
       this.dataSetName = attributes.getValue(0);
     }
     if (qName.equalsIgnoreCase("Row"))
     {
       this.fieldsMap = new HashMap();
       this.rowID = Long.valueOf(attributes.getValue(0));
     }
     if (qName.equalsIgnoreCase("Col")) {
       this.fieldsMap.put(attributes.getValue(0), attributes.getValue(1));
     }
   }
   
   public void endElement(String uri, String localName, String qName)
     throws SAXException
   {
     if (qName.equalsIgnoreCase("DataSet")) {
       this.fucModel.addDataSet(this.dataSetName, this.dataSetMap);
     }
     if (qName.equalsIgnoreCase("Row")) {
       this.dataSetMap.put(this.rowID, this.fieldsMap);
     }
   }
   
   public void endDocument()
     throws SAXException
   {}
   
   private void parse(String xmlStr)
     throws Exception
   {
     SAXParserFactory spf = SAXParserFactory.newInstance();
     SAXParser saxParser = spf.newSAXParser();
     saxParser.parse(new InputSource(new StringReader(xmlStr)), this);
   }
 }