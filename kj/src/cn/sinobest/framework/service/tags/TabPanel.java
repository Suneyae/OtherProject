 package cn.sinobest.framework.service.tags;
 
 import java.io.Serializable;
 import java.util.List;
 
 public class TabPanel
   implements Serializable
 {
   private static final long serialVersionUID = 1L;
   private List<Tab> tabs;
   private String init;
   
   public void setTabs(List<Tab> tabs)
   {
     this.tabs = tabs;
   }
   
   public void setInit(String init)
   {
     this.init = init;
   }
   
   public List<Tab> getTabs()
   {
     return this.tabs;
   }
   
   public String getInit()
   {
     return this.init;
   }
 }