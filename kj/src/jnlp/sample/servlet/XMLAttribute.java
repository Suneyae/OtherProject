 package jnlp.sample.servlet;
 
 public class XMLAttribute
 {
   private String _name;
   private String _value;
   private XMLAttribute _next;
   
   public XMLAttribute(String name, String value)
   {
     this._name = name;
     this._value = value;
     this._next = null;
   }
   
   public XMLAttribute(String name, String value, XMLAttribute next)
   {
     this._name = name;
     this._value = value;
     this._next = next;
   }
   
   public String getName()
   {
     return this._name;
   }
   
   public String getValue()
   {
     return this._value;
   }
   
   public XMLAttribute getNext()
   {
     return this._next;
   }
   
   public void setNext(XMLAttribute next)
   {
     this._next = next;
   }
   
   public boolean equals(Object o)
   {
     if ((o == null) || (!(o instanceof XMLAttribute))) {
       return false;
     }
     XMLAttribute other = (XMLAttribute)o;
     
 
 
     return (match(this._name, other._name)) && (match(this._value, other._value)) && (match(this._next, other._next));
   }
   
   private static boolean match(Object o1, Object o2)
   {
     if (o1 == null) {
       return o2 == null;
     }
     return o1.equals(o2);
   }
   
   public String toString()
   {
     if (this._next != null) {
       return this._name + "=\"" + this._value + "\" " + this._next.toString();
     }
     return this._name + "=\"" + this._value + "\"";
   }
 }