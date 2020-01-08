 package cn.sinobest.framework.service.tags;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IDAO;
 import cn.sinobest.framework.dao.mapper.SQLModel;
 import cn.sinobest.framework.service.Buss2SysOrgService;
 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.HashMap;
 import java.util.HashSet;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import java.util.Set;
 import java.util.Stack;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;
 
 @Service
 public class TreeService
 {
   @Autowired
   IDAO commDAO;
   @Autowired
   Buss2SysOrgService buss2SysOrgService;
   public static final String TREE_CHILDREN = "children";
   public static final String DB_SELECT = "select
   public static final String TREE_AJAX = "isLazy";
   
   public static enum TreeAttr
   {
     TREEKEY("key", false),  TREETITLE("title", false),  TREEVALUE("url", true);
     
     public final String jsattr;
     public final boolean optional;
     
     private TreeAttr(String jsattr, boolean optional)
     {
       this.jsattr = jsattr;
       this.optional = optional;
     }
   }
   
   public List<Map<String, Object>> getTreeModel(Map<String, Object> parameters)
   {
     String key = (String)parameters.get("key");
     String view = (String)parameters.get("view");
     String depthStr = (String)parameters.get("depth");
     String whereCls = (String)parameters.get("whereCls");
     String orderSql = (String)parameters.get("orderSql");
     String showRoot = (String)parameters.get("showRoot");
     
 
     List<String> selected = (List)parameters.get("selected");
     if (key.length() % 2 != 0) {
       throw new AppException("EFW0117", null, new Object[] { key });
     }
     int depth = 1;
     if (depthStr.length() != 0) {
       try
       {
         depth = Integer.parseInt(depthStr);
       }
       catch (NumberFormatException e)
       {
         depth = 1;
       }
     }
     List<Map<String, Object>> roots = getTreeModel(view, key, depth, 
       whereCls, orderSql, null, Boolean.valueOf(showRoot).booleanValue());
     if (depth != -1) {
       enableAjax(roots, true);
     }
     if ((selected != null) && (!selected.isEmpty())) {
       initSelected(roots, selected);
     }
     return roots;
   }
   
   public List<Map<String, Object>> getTreeModel(String view, String rootNode, int depth, String whereCls, String orderSql, String bussFuncId, boolean showRoot)
   {
     StringBuilder sql = new StringBuilder("select
       .append(view)
       .append(" where ")
       .append(getWhereCls(rootNode, depth, whereCls, orderSql, 
       bussFuncId, showRoot));
     SQLModel sqlModel = new SQLModel();
     sqlModel.setSql(sql.toString());
     List<Map<String, Object>> list = null;
     List<Map<String, Object>> root = new ArrayList();
     Stack<Map<String, Object>> path = new Stack();
     try
     {
       list = this.commDAO.selectSQL(sqlModel);
       TreeAttr[] attrs = TreeAttr.values();
       TreeAttr attr;
       if (!list.isEmpty())
       {
         Map<String, Object> sample = (Map)list.get(0);
         for (attr : attrs) {
           if ((!attr.optional) && (!sample.containsKey(attr.name()))) {
             throw new AppException("EFW0118", null, new Object[] {
               sample.toString() });
           }
         }
       }
       for (Map<String, Object> node : list)
       {
         tmp = new HashMap(3, 1.0F);
         for (TreeAttr attr : attrs) {
           if (node.containsKey(attr.name())) {
             ((Map)tmp).put(attr.jsattr, node.get(attr.name()));
           }
         }
         Object parentNode = getParent(path, (Map)tmp);
         if (parentNode == null)
         {
           root.add(tmp);
         }
         else
         {
           Object childrenNodes = 
             (List)((Map)parentNode).get("children");
           if (childrenNodes == null)
           {
             childrenNodes = new ArrayList();
             ((Map)parentNode).put("children", childrenNodes);
           }
           ((List)childrenNodes).add(tmp);
         }
       }
       list = null;
       
 
       int minDepth = 100;
       for (Object tmp = root.iterator(); ((Iterator)tmp).hasNext();)
       {
         Map<String, Object> rootItem = (Map)((Iterator)tmp).next();
         minDepth = Math.min(minDepth, 
           ((String)rootItem.get(TreeAttr.TREEKEY.jsattr)).length());
       }
       Iterator<Map<String, Object>> rootIt = root.iterator();
       while (rootIt.hasNext()) {
         if (((String)((Map)rootIt.next()).get(TreeAttr.TREEKEY.jsattr))
           .length() != minDepth) {
           rootIt.remove();
         }
       }
     }
     catch (AppException e)
     {
       throw e;
     }
     catch (Exception e)
     {
       throw new AppException("EFW0119", e, new Object[] { sql.toString() });
     }
     return root;
   }
   
   public void enableAjax(List<Map<String, Object>> node, boolean flag)
   {
     for (Map<String, Object> childNode : node)
     {
       childNode.put("isLazy", Boolean.valueOf(flag));
       if (childNode.get("children") != null) {
         enableAjax(
         
           (List)childNode.get("children"), 
           flag);
       }
     }
   }
   
   public Map<String, Object> getParent(Stack<Map<String, Object>> path, Map<String, Object> node)
   {
     String parentId = (String)node.get(TreeAttr.TREEKEY.jsattr);
     if ((parentId.length() == 0) || (path.isEmpty()))
     {
       path.push(node);
       return null;
     }
     parentId = parentId.substring(0, parentId.length() - 2);
     Map<String, Object> rst = null;
     do
     {
       Map<String, Object> obj = (Map)path.peek();
       if (obj.get(TreeAttr.TREEKEY.jsattr).equals(parentId))
       {
         rst = obj;
         break;
       }
       path.pop();
     } while (!
     
 
 
 
 
 
 
       path.isEmpty());
     path.push(node);
     return rst;
   }
   
   public String getWhereCls(String rootNode, int depth, String whereCls, String orderSql, String bussFuncId, boolean showRoot)
   {
     StringBuilder sql = new StringBuilder(whereCls);
     
     StringBuilder sql_root = new StringBuilder();
     if (sql.length() == 0) {
       sql.append("1=1");
     }
     int root_level = 0;
     if ((rootNode != null) && (rootNode.length() >= 2))
     {
       root_level = rootNode.length() / 2;
       sql_root.append(TreeAttr.TREEKEY.name()).append(" like '")
         .append(rootNode).append("%'");
       if (!showRoot) {
         sql_root.append(" and ").append(TreeAttr.TREEKEY.name()).append(" != '").append(rootNode).append("'");
       }
     }
     if (depth != -1)
     {
       if (sql_root.length() > 0) {
         sql_root.append(" and ");
       }
       sql_root.append(" f_length(").append(TreeAttr.TREEKEY.name()).append(")<= ").append((root_level + depth)
     }
     if ((showRoot) && (bussFuncId != null) && (bussFuncId.trim().length() > 0) && 
       (root_level > 0))
     {
       char[] strTemplate = { '_', '_' };
       StringBuilder sql_buss = new StringBuilder(" ( ");
       for (int i = 0; i < root_level; i++)
       {
         sql_buss.append(TreeAttr.TREEKEY.name()).append(" like '").append(rootNode.substring(0, i
         sql_buss.append("' or ");
       }
       sql_buss.delete(sql_buss.length() - 3, sql_buss.length()).append(") and ");
       
       String bussSql = this.buss2SysOrgService.getBuss2SysOrg(rootNode, 
         bussFuncId, TreeAttr.TREEKEY.name());
       sql_buss.append(bussSql);
       sql.append(" and (").append(sql_buss).append(")or(")
         .append(sql_root).append(')');
     }
     else if (sql_root.length() > 0)
     {
       sql.append(" and ").append(sql_root);
     }
     sql.append(" order by ").append(orderSql);
     return sql.toString();
   }
   
   public void initSelected(List<Map<String, Object>> nodes, List<String> selectedNodes)
   {
     if ((selectedNodes == null) || (selectedNodes.isEmpty()) || (nodes == null) || 
       (nodes.isEmpty())) {
       return;
     }
     Set<String> selectedSet = new HashSet(selectedNodes);
     recurseInitSelected(nodes, selectedSet);
   }
   
   private void recurseInitSelected(List<Map<String, Object>> nodes, Collection<String> selectedNodes)
   {
     for (Map<String, Object> childNode : nodes)
     {
       if (selectedNodes.contains(childNode.get(TreeAttr.TREEKEY.jsattr))) {
         childNode.put("select", Boolean.valueOf(true));
       }
       if (childNode.get("children") != null) {
         recurseInitSelected(
         
           (List)childNode.get("children"), 
           selectedNodes);
       }
     }
   }
 }