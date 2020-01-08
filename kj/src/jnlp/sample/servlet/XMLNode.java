 package jnlp.sample.servlet;
 
 import java.io.PrintWriter;
 import java.io.StringWriter;
 
 public class XMLNode
 {
   private boolean _isElement;
   private String _name;
   private XMLAttribute _attr;
   private XMLNode _parent;
   private XMLNode _nested;
   private XMLNode _next;
   
   public XMLNode(String name)
   {
     this(name, null, null, null);
     this._isElement = false;
   }
   
   public XMLNode(String name, XMLAttribute attr)
   {
     this(name, attr, null, null);
   }
   
   public XMLNode(String name, XMLAttribute attr, XMLNode nested, XMLNode next)
   {
     this._isElement = true;
     this._name = name;
     this._attr = attr;
     this._nested = nested;
     this._next = next;
     this._parent = null;
   }
   
   public String getName()
   {
     return this._name;
   }
   
   public XMLAttribute getAttributes()
   {
     return this._attr;
   }
   
   public XMLNode getNested()
   {
     return this._nested;
   }
   
   public XMLNode getNext()
   {
     return this._next;
   }
   
   public boolean isElement()
   {
     return this._isElement;
   }
   
   public void setParent(XMLNode parent)
   {
     this._parent = parent;
   }
   
   public XMLNode getParent()
   {
     return this._parent;
   }
   
   public void setNext(XMLNode next)
   {
     this._next = next;
   }
   
   public void setNested(XMLNode nested)
   {
     this._nested = nested;
   }
   
   public boolean equals(Object o)
   {
     if ((o == null) || (!(o instanceof XMLNode))) {
       return false;
     }
     XMLNode other = (XMLNode)o;
     boolean result = 
       (match(this._name, other._name)) && 
       (match(this._attr, other._attr)) && 
       (match(this._nested, other._nested)) && 
       (match(this._next, other._next));
     return result;
   }
   
   public String getAttribute(String name)
   {
     XMLAttribute cur = this._attr;
     while (cur != null)
     {
       if (name.equals(cur.getName())) {
         return cur.getValue();
       }
       cur = cur.getNext();
     }
     return "";
   }
   
   private static boolean match(Object o1, Object o2)
   {
     if (o1 == null) {
       return o2 == null;
     }
     return o1.equals(o2);
   }
   
   public void printToStream(PrintWriter out)
   {
     printToStream(out, 0);
   }
   
   public void printToStream(PrintWriter out, int n)
   {
     if (!isElement())
     {
       out.print(this._name);
     }
     else if (this._nested == null)
     {
       String attrString = " " + this._attr.toString();
       lineln(out, n, "<" + this._name + attrString + "/>");
     }
     else
     {
       String attrString = " " + this._attr.toString();
       lineln(out, n, "<" + this._name + attrString + ">");
       this._nested.printToStream(out, n + 1);
       if (this._nested.isElement()) {
         lineln(out, n, "</" + this._name + ">");
       } else {
         out.print("</" + this._name + ">");
       }
     }
     if (this._next != null) {
       this._next.printToStream(out, n);
     }
   }
   
   private static void lineln(PrintWriter out, int indent, String s)
   {
     out.println("");
     for (int i = 0; i < indent; i++) {
       out.print("  ");
     }
     out.print(s);
   }
   
   public String toString()
   {
     StringWriter sw = new StringWriter(1000);
     PrintWriter pw = new PrintWriter(sw);
     printToStream(pw);
     pw.close();
     return sw.toString();
   }
 }