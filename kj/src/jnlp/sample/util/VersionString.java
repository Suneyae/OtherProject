 package jnlp.sample.util;
 
 import java.util.ArrayList;
 import java.util.StringTokenizer;
 
 public class VersionString
 {
   private ArrayList _versionIds;
   
   public VersionString(String vs)
   {
     this._versionIds = new ArrayList();
     if (vs != null)
     {
       StringTokenizer st = new StringTokenizer(vs, " ", false);
       while (st.hasMoreElements()) {
         this._versionIds.add(new VersionID(st.nextToken()));
       }
     }
   }
   
   public boolean contains(VersionID m)
   {
     for (int i = 0; i < this._versionIds.size(); i++)
     {
       VersionID vi = (VersionID)this._versionIds.get(i);
       boolean check = vi.match(m);
       if (check) {
         return true;
       }
     }
     return false;
   }
   
   public boolean contains(String versionid)
   {
     return contains(new VersionID(versionid));
   }
   
   public boolean containsGreaterThan(VersionID m)
   {
     for (int i = 0; i < this._versionIds.size(); i++)
     {
       VersionID vi = (VersionID)this._versionIds.get(i);
       boolean check = vi.isGreaterThan(m);
       if (check) {
         return true;
       }
     }
     return false;
   }
   
   public boolean containsGreaterThan(String versionid)
   {
     return containsGreaterThan(new VersionID(versionid));
   }
   
   public static boolean contains(String vs, String vi)
   {
     return new VersionString(vs).contains(vi);
   }
   
   public String toString()
   {
     StringBuffer sb = new StringBuffer();
     for (int i = 0; i < this._versionIds.size(); i++)
     {
       sb.append(this._versionIds.get(i).toString());
       sb.append(' ');
     }
     return sb.toString();
   }
 }