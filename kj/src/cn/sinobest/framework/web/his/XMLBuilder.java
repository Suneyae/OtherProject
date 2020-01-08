 package cn.sinobest.framework.web.his;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import java.io.ByteArrayOutputStream;
 import java.io.OutputStream;
 import java.util.HashMap;
 import java.util.Properties;
 import java.util.Set;
 import javax.xml.parsers.DocumentBuilder;
 import javax.xml.parsers.DocumentBuilderFactory;
 import javax.xml.transform.Transformer;
 import javax.xml.transform.TransformerFactory;
 import javax.xml.transform.dom.DOMSource;
 import javax.xml.transform.stream.StreamResult;
 import org.w3c.dom.Document;
 import org.w3c.dom.Element;
 import org.w3c.dom.ProcessingInstruction;
 import org.w3c.dom.Text;
 
 public class XMLBuilder
 {
   public static final String SESSIONID = "SESSIONID";
   private DocumentBuilderFactory factory = null;
   private DocumentBuilder builder = null;
   private Document doc = null;
   private Element root = null;
   private Element FuctionInfo = null;
   
   public XMLBuilder()
     throws Exception
   {
     init();
   }
   
   private void init()
     throws Exception
   {
     try
     {
       this.factory = DocumentBuilderFactory.newInstance();
       this.builder = this.factory.newDocumentBuilder();
       this.doc = this.builder.newDocument();
       this.doc.normalize();
       String veren = "version=\"1.0\"";
       ProcessingInstruction proce = this.doc.createProcessingInstruction("xml", veren);
       
       this.root = this.doc.createElement("HNBridge");
       
       Element Head = this.doc.createElement("Head");
       Element Version = this.doc.createElement("Version");
       Text textseg = this.doc.createTextNode("1.0");
       Version.appendChild(textseg);
       Head.appendChild(Version);
       
       this.root.appendChild(Head);
       
       this.FuctionInfo = this.doc.createElement("FunctionInfo");
     }
     catch (Exception e)
     {
       throw new AppException(e.getLocalizedMessage(), e);
     }
   }
   
   public String buildXml(FuncModel outputFuncModel)
     throws Exception
   {
     HashMap datapackage = outputFuncModel.getDataPackge();
     HashMap RParams = outputFuncModel.getParamsMap();
     RParams.put("SESSIONID", outputFuncModel.getSessionID());
     String xmlStr = "";
     String errMsg = outputFuncModel.getErrMsg();
     String helpMsg = outputFuncModel.getHelpMsg();
     try
     {
       Element Output = this.doc.createElement("Output");
       if ((!RParams.isEmpty()) && (RParams.get("FHZ") != null) && (RParams.get("FHZ").toString().trim() != ""))
       {
         Element ReturnParams = this.doc.createElement("ReturnParams");
         Object[] dataparam = RParams.keySet().toArray();
         String RPname = null;
         for (int i = 0; i < dataparam.length; i++)
         {
           RPname = (String)dataparam[i];
           Element ReturnParam = this.doc.createElement("ReturnParam");
           
           ReturnParam.setAttribute("RPName", RPname);
           Object tempvalue = null;
           if ((RPname != null) && (RParams.containsKey(RPname))) {
             tempvalue = RParams.get(RPname);
           }
           if (tempvalue != null) {
             ReturnParam.setAttribute("RPValue", tempvalue.toString());
           } else {
             ReturnParam.setAttribute("RPValue", " ");
           }
           ReturnParams.appendChild(ReturnParam);
         }
         Output.appendChild(ReturnParams);
       }
       else
       {
         Element ReturnParams = this.doc.createElement("ReturnParams");
         
         Element ReturnParam1 = this.doc.createElement("ReturnParam");
         ReturnParam1.setAttribute("RPName", "FHZ");
         ReturnParam1.setAttribute("RPValue", "-1");
         ReturnParams.appendChild(ReturnParam1);
         
         Element ReturnParam2 = this.doc.createElement("ReturnParam");
         ReturnParam2.setAttribute("RPName", "MSG");
         if ((errMsg != null) && (errMsg.length() > 0)) {
           ReturnParam2.setAttribute("RPValue", errMsg);
         } else {
           ReturnParam2.setAttribute("RPValue", "不能与应用服务器通讯，可能的原因是应用服务器未启动或已关闭、系统连接超时或业务逻辑出错！\r\n如果是应用服务器未启动或已关闭，需在应用服务器启动后重新启动前置服务!");
         }
         ReturnParams.appendChild(ReturnParam2);
         
         Element ReturnParam3 = this.doc.createElement("ReturnParam");
         ReturnParam3.setAttribute("RPName", "HELPMSG");
         if ((helpMsg != null) && (helpMsg.length() > 0)) {
           ReturnParam3.setAttribute("RPValue", helpMsg);
         } else {
           ReturnParam3.setAttribute("RPValue", "发生错误!请联系系统管理员");
         }
         ReturnParams.appendChild(ReturnParam3);
         
         Output.appendChild(ReturnParams);
       }
       Element DataPackage = this.doc.createElement("DataPackage");
       if (!datapackage.isEmpty())
       {
         Object[] indatapackage = datapackage.keySet().toArray();
         String dataSetName = null;
         for (int i = 0; i < indatapackage.length; i++)
         {
           dataSetName = (String)indatapackage[i];
           String dsName = dataSetName;
           
           HashMap rowsMap = new HashMap();
           rowsMap = (HashMap)datapackage.get(dataSetName);
           
           Object[] rows = rowsMap.keySet().toArray();
           Long rowID = null;
           
           Element dataSet = this.doc.createElement("DataSet");
           dataSet.setAttribute("DSName", dsName);
           for (int j = 0; j < rows.length; j++)
           {
             rowID = (Long)rows[j];
             Element row = this.doc.createElement("Row");
             row.setAttribute("RowID", String.valueOf(rowID.longValue()));
             
             HashMap fieldsMap = null;
             fieldsMap = (HashMap)rowsMap.get(rowID);
             
 
             Object[] fields = fieldsMap.keySet().toArray();
             for (int k = 0; k < fields.length; k++)
             {
               String colName = fields[k].toString();
               Element col = this.doc.createElement("Col");
               col.setAttribute("ColName", colName);
               Object colValue = fieldsMap.get(colName);
               if (colValue != null) {
                 col.setAttribute("ColValue", colValue.toString() != "null" ? colValue.toString() : "");
               } else {
                 col.setAttribute("ColValue", "");
               }
               row.appendChild(col);
             }
             dataSet.appendChild(row);
           }
           DataPackage.appendChild(dataSet);
         }
       }
       Output.appendChild(DataPackage);
       this.FuctionInfo.appendChild(Output);
       
       xmlStr = xmlTransform();
       if ((xmlStr.equalsIgnoreCase("")) || (xmlStr == null)) {}
       return "";
     }
     catch (Exception e)
     {
       throw new AppException(e.getLocalizedMessage(), e);
     }
   }
   
   private String xmlTransform()
     throws Exception
   {
     OutputStream outputStream = new ByteArrayOutputStream();
     String xmlStr = "";
     try
     {
       this.root.appendChild(this.FuctionInfo);
       this.doc.appendChild(this.root);
       
       TransformerFactory tFactory = TransformerFactory.newInstance();
       Transformer transformer = tFactory.newTransformer();
       DOMSource source = new DOMSource(this.doc);
       
       Properties properties = transformer.getOutputProperties();
       properties.setProperty("encoding", "ISO8859-1");
       
 
 
 
 
 
 
 
 
 
       transformer.setOutputProperties(properties);
       StreamResult result = new StreamResult(outputStream);
       
 
       transformer.transform(source, result);
       return outputStream.toString();
     }
     catch (Exception e)
     {
       throw new AppException(e.getLocalizedMessage(), e);
     }
   }
 }