 package cn.sinobest.framework.service.bigdict;
 
 import java.util.Comparator;
 
 public class DictSort
   implements Comparator<String[]>
 {
   public static final DictSort DICTSORTOR = new DictSort();
   
   public int compare(String[] o1, String[] o2)
   {
     if (o1[0] == null)
     {
       if (o2[0] == null) {
         return 0;
       }
       return -1;
     }
     if (o2[0] == null) {
       return 1;
     }
     if (o1[0].length() == o2[0].length()) {
       return o1[0].compareTo(o2[0]);
     }
     return o1[0].length() - o2[0].length();
   }
 }