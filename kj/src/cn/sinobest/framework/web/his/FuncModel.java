 package cn.sinobest.framework.web.his;
 
 import java.io.Serializable;
 import java.util.HashMap;
 
 public class FuncModel
   implements Serializable
 {
   private String helpMsg = "";
   private String errMsg = "";
   private String sessionID = "";
   private String sequenceID = "";
   private String agentServerIP = "";
   private HashMap mapParams;
   private HashMap mapDataPackage;
   
   public FuncModel()
   {
     this.sequenceID = "";
     this.mapParams = new HashMap();
     this.mapDataPackage = new HashMap();
   }
   
   public String getHelpMsg()
   {
     return this.helpMsg;
   }
   
   public void setHelpMsg(String helpMsg)
   {
     this.helpMsg = helpMsg;
   }
   
   public String getErrMsg()
   {
     return this.errMsg;
   }
   
   public void setErrMsg(String errMsg)
   {
     this.errMsg = errMsg;
   }
   
   public void setAgentServerIP(String agentIP)
   {
     this.agentServerIP = agentIP;
   }
   
   public String getAgentServerIP()
   {
     return this.agentServerIP;
   }
   
   public void setSessionID(String id)
   {
     this.sessionID = id;
   }
   
   public String getSessionID()
   {
     return this.sessionID;
   }
   
   public void setSequenceID(String id)
   {
     this.sequenceID = id;
   }
   
   public String getSequenceID()
   {
     return this.sequenceID;
   }
   
   public void setParamsMap(HashMap params)
   {
     this.mapParams.putAll(params);
   }
   
   public HashMap getParamsMap()
   {
     return this.mapParams;
   }
   
   public void setDataPackage(HashMap dataPackage)
   {
     this.mapDataPackage = dataPackage;
   }
   
   public HashMap getDataPackge()
   {
     return this.mapDataPackage;
   }
   
   public void addDataSet(String dataSetName, HashMap dataSet)
   {
     this.mapDataPackage.put(dataSetName, dataSet);
   }
   
   public HashMap getDataSet(String dataSetName)
   {
     return (HashMap)this.mapDataPackage.get(dataSetName);
   }
   
   public void addDataSetField(String dataSetName, Long rowID, String colName, Object fieldValue)
   {
     HashMap rowsMap = null;
     HashMap fieldsMap = null;
     
     rowsMap = (HashMap)this.mapDataPackage.get(dataSetName);
     if (rowsMap == null) {
       rowsMap = new HashMap();
     }
     fieldsMap = (HashMap)rowsMap.get(rowID);
     if (fieldsMap == null) {
       fieldsMap = new HashMap();
     }
     if (fieldValue == null) {
       fieldsMap.put(colName, "");
     } else {
       fieldsMap.put(colName, fieldValue);
     }
     rowsMap.put(rowID, fieldsMap);
     
     this.mapDataPackage.put(dataSetName, rowsMap);
   }
   
   public void addRow2DataSet(String dataSetName, Long rowID, HashMap fieldsMap)
   {
     HashMap rowsMap = null;
     
     rowsMap = (HashMap)this.mapDataPackage.get(dataSetName);
     if (rowsMap == null) {
       rowsMap = new HashMap();
     }
     rowsMap.put(rowID, fieldsMap);
     
     this.mapDataPackage.put(dataSetName, rowsMap);
   }
   
   public Object getDataSetField(String dataSetName, Long rowID, String colName)
   {
     Object field = null;
     HashMap rowsMap = null;HashMap fieldsMap = null;
     rowsMap = (HashMap)this.mapDataPackage.get(dataSetName);
     if (rowsMap != null)
     {
       fieldsMap = (HashMap)rowsMap.get(rowID);
       if (fieldsMap != null) {
         field = fieldsMap.get(colName);
       }
     }
     return field;
   }
   
   public void addParam(String paramName, Object paramValue)
   {
     if (paramValue == null) {
       this.mapParams.put(paramName, "");
     } else {
       this.mapParams.put(paramName, paramValue);
     }
   }
   
   public void removeParam(String paramName)
   {
     if (this.mapParams.containsKey(paramName)) {
       this.mapParams.remove(paramName);
     }
   }
   
   public Object getParam(String paramName)
   {
     if (this.mapParams.containsKey(paramName)) {
       return this.mapParams.get(paramName);
     }
     return "";
   }
 }