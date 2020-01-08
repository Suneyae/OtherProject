 package cn.sinobest.framework.dao.workflow;
 
 import java.util.Comparator;
 
 public class WorkItemComparator
   implements Comparator<WfWorkItem>
 {
   public int compare(WfWorkItem o1, WfWorkItem o2)
   {
     long v1 = o1.getWORK_ITEM_ID().longValue();
     long v2 = o2.getWORK_ITEM_ID().longValue();
     
     return v1 > v2 ? 1 : v1 == v2 ? 0 : -1;
   }
 }