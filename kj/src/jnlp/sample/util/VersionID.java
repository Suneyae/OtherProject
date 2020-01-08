 package jnlp.sample.util;
 
 import java.util.ArrayList;
 import java.util.Arrays;
 
 public class VersionID
   implements Comparable
 {
   private String[] _tuple;
   private boolean _usePrefixMatch;
   private boolean _useGreaterThan;
   private boolean _isCompound;
   private VersionID _rest;
   
   public VersionID(String str)
   {
     this._usePrefixMatch = false;
     this._useGreaterThan = false;
     this._isCompound = false;
     if ((str == null) && (str.length() == 0))
     {
       this._tuple = new String[0];
       return;
     }
     int amp = str.indexOf("&");
     if (amp >= 0)
     {
       this._isCompound = true;
       VersionID firstPart = new VersionID(str.substring(0, amp));
       this._rest = new VersionID(str.substring(amp + 1));
       this._tuple = firstPart._tuple;
       this._usePrefixMatch = firstPart._usePrefixMatch;
       this._useGreaterThan = firstPart._useGreaterThan;
     }
     else
     {
       if (str.endsWith("+"))
       {
         this._useGreaterThan = true;
         str = str.substring(0, str.length() - 1);
       }
       else if (str.endsWith("*"))
       {
         this._usePrefixMatch = true;
         str = str.substring(0, str.length() - 1);
       }
       ArrayList list = new ArrayList();
       int start = 0;
       for (int i = 0; i < str.length(); i++) {
         if (".-_".indexOf(str.charAt(i)) != -1)
         {
           if (start < i)
           {
             String value = str.substring(start, i);
             list.add(value);
           }
           start = i + 1;
         }
       }
       if (start < str.length()) {
         list.add(str.substring(start, str.length()));
       }
       this._tuple = new String[list.size()];
       this._tuple = ((String[])list.toArray(this._tuple));
     }
   }
   
   public boolean isSimpleVersion()
   {
     return (!this._useGreaterThan) && (!this._usePrefixMatch) && (!this._isCompound);
   }
   
   public boolean match(VersionID vid)
   {
     if ((this._isCompound) && 
       (!this._rest.match(vid))) {
       return false;
     }
     return 
       this._useGreaterThan ? vid.isGreaterThanOrEqual(this) : this._usePrefixMatch ? isPrefixMatch(vid) : 
       matchTuple(vid);
   }
   
   public boolean equals(Object o)
   {
     if (matchTuple(o))
     {
       VersionID ov = (VersionID)o;
       if (((this._rest == null) || (this._rest.equals(ov._rest))) && 
         (this._useGreaterThan == ov._useGreaterThan) && 
         (this._usePrefixMatch == ov._usePrefixMatch)) {
         return true;
       }
     }
     return false;
   }
   
   private boolean matchTuple(Object o)
   {
     if ((o == null) || (!(o instanceof VersionID))) {
       return false;
     }
     VersionID vid = (VersionID)o;
     
 
     String[] t1 = normalize(this._tuple, vid._tuple.length);
     String[] t2 = normalize(vid._tuple, this._tuple.length);
     for (int i = 0; i < t1.length; i++)
     {
       Object o1 = getValueAsObject(t1[i]);
       Object o2 = getValueAsObject(t2[i]);
       if (!o1.equals(o2)) {
         return false;
       }
     }
     return true;
   }
   
   private Object getValueAsObject(String value)
   {
     if ((value.length() > 0) && (value.charAt(0) != '-')) {
       try
       {
         return Integer.valueOf(value);
       }
       catch (NumberFormatException localNumberFormatException) {}
     }
     return value;
   }
   
   public boolean isGreaterThan(VersionID vid)
   {
     return isGreaterThanOrEqualHelper(vid, false);
   }
   
   public boolean isGreaterThanOrEqual(VersionID vid)
   {
     return isGreaterThanOrEqualHelper(vid, true);
   }
   
   private boolean isGreaterThanOrEqualHelper(VersionID vid, boolean allowEqual)
   {
     if ((this._isCompound) && 
       (!this._rest.isGreaterThanOrEqualHelper(vid, allowEqual))) {
       return false;
     }
     String[] t1 = normalize(this._tuple, vid._tuple.length);
     String[] t2 = normalize(vid._tuple, this._tuple.length);
     for (int i = 0; i < t1.length; i++)
     {
       Object e1 = getValueAsObject(t1[i]);
       Object e2 = getValueAsObject(t2[i]);
       if (!e1.equals(e2))
       {
         if (((e1 instanceof Integer)) && ((e2 instanceof Integer))) {
           return ((Integer)e1).intValue() > ((Integer)e2).intValue();
         }
         String s1 = t1[i].toString();
         String s2 = t2[i].toString();
         return s1.compareTo(s2) > 0;
       }
     }
     return allowEqual;
   }
   
   public boolean isPrefixMatch(VersionID vid)
   {
     if ((this._isCompound) && 
       (!this._rest.isPrefixMatch(vid))) {
       return false;
     }
     String[] t2 = normalize(vid._tuple, this._tuple.length);
     for (int i = 0; i < this._tuple.length; i++)
     {
       Object e1 = this._tuple[i];
       Object e2 = t2[i];
       if (!e1.equals(e2)) {
         return false;
       }
     }
     return true;
   }
   
   private String[] normalize(String[] list, int minlength)
   {
     if (list.length < minlength)
     {
       String[] newlist = new String[minlength];
       System.arraycopy(list, 0, newlist, 0, list.length);
       Arrays.fill(newlist, list.length, newlist.length, "0");
       return newlist;
     }
     return list;
   }
   
   public int compareTo(Object o)
   {
     if ((o == null) || (!(o instanceof VersionID))) {
       return -1;
     }
     VersionID vid = (VersionID)o;
     return isGreaterThanOrEqual(vid) ? 1 : equals(vid) ? 0 : -1;
   }
   
   public String toString()
   {
     StringBuffer sb = new StringBuffer();
     for (int i = 0; i < this._tuple.length - 1; i++)
     {
       sb.append(this._tuple[i]);
       sb.append('.');
     }
     if (this._tuple.length > 0) {
       sb.append(this._tuple[(this._tuple.length - 1)]);
     }
     if (this._usePrefixMatch) {
       sb.append('+');
     }
     return sb.toString();
   }
 }