 package jnlp.sample.servlet;
 
 import java.util.ArrayList;
 import java.util.List;
 import org.w3c.dom.Attr;
 import org.w3c.dom.Element;
 import org.w3c.dom.NamedNodeMap;
 import org.w3c.dom.Node;
 import org.w3c.dom.Text;
 
 public class XMLParsing
 {
   public static XMLNode convert(Node n)
   {
     if (n == null) {
       return null;
     }
     if ((n instanceof Text))
     {
       Text tn = (Text)n;
       return new XMLNode(tn.getNodeValue());
     }
     if ((n instanceof Element))
     {
       Element en = (Element)n;
       
       XMLAttribute xmlatts = null;
       NamedNodeMap attributes = en.getAttributes();
       for (int i = attributes.getLength() - 1; i >= 0; i--)
       {
         Attr ar = (Attr)attributes.item(i);
         xmlatts = new XMLAttribute(ar.getName(), ar.getValue(), xmlatts);
       }
       XMLNode thisNode = new XMLNode(en.getNodeName(), xmlatts, null, null);
       XMLNode last = null;
       Node nn = en.getFirstChild();
       while (nn != null)
       {
         if (thisNode.getNested() == null)
         {
           last = convert(nn);
           thisNode.setNested(last);
         }
         else
         {
           XMLNode nnode = convert(nn);
           last.setNext(nnode);
           last = nnode;
         }
         last.setParent(thisNode);
         nn = nn.getNextSibling();
       }
       return thisNode;
     }
     return null;
   }
   
   public static boolean isElementPath(XMLNode root, String path)
   {
     return findElementPath(root, path) != null;
   }
   
   public static String getPathString(XMLNode e)
   {
     return getPathString(e.getParent()) + "<" + e.getName() + ">";
   }
   
   public static String getElementContent(XMLNode root, String path)
   {
     return getElementContent(root, path, null);
   }
   
   public static String[] getMultiElementContent(XMLNode root, String path)
   {
     List list = new ArrayList();
     visitElements(root, path, new ElementVisitor()
     {
       public void visitElement(XMLNode n)
       {
         String value = XMLParsing.getElementContent(n, "");
         if (value != null) {
           XMLParsing.this.add(value);
         }
       }
     });
     if (list.size() == 0) {
       return null;
     }
     return (String[])list.toArray(new String[list.size()]);
   }
   
   public static String getElementContent(XMLNode root, String path, String defaultvalue)
   {
     XMLNode e = findElementPath(root, path);
     if (e == null) {
       return defaultvalue;
     }
     XMLNode n = e.getNested();
     if ((n != null) && (!n.isElement())) {
       return n.getName();
     }
     return defaultvalue;
   }
   
   public static XMLNode findElementPath(XMLNode elem, String path)
   {
     if (elem == null) {
       return null;
     }
     if ((path == null) || (path.length() == 0)) {
       return elem;
     }
     int idx = path.indexOf('>');
     String head = path.substring(1, idx);
     String tail = path.substring(idx + 1);
     return findElementPath(findChildElement(elem, head), tail);
   }
   
   public static XMLNode findChildElement(XMLNode elem, String tag)
   {
     XMLNode n = elem.getNested();
     while (n != null)
     {
       if ((n.isElement()) && (n.getName().equals(tag))) {
         return n;
       }
       n = n.getNext();
     }
     return null;
   }
   
   public static void visitElements(XMLNode root, String path, ElementVisitor ev)
   {
     int idx = path.lastIndexOf('<');
     String head = path.substring(0, idx);
     String tag = path.substring(idx + 1, path.length() - 1);
     
     XMLNode elem = findElementPath(root, head);
     if (elem == null) {
       return;
     }
     XMLNode n = elem.getNested();
     while (n != null)
     {
       if ((n.isElement()) && (n.getName().equals(tag))) {
         ev.visitElement(n);
       }
       n = n.getNext();
     }
   }
   
   public static void visitChildrenElements(XMLNode elem, ElementVisitor ev)
   {
     XMLNode n = elem.getNested();
     while (n != null)
     {
       if (n.isElement()) {
         ev.visitElement(n);
       }
       n = n.getNext();
     }
   }
   
   public static abstract class ElementVisitor
   {
     public abstract void visitElement(XMLNode paramXMLNode);
   }
 }