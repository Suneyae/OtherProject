package com.java1234.action;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.io.SAXReader;

/**
 * Dom4j测试类 
 * 参见：http://www.cnblogs.com/mengdd/archive/2013/06/05/3119927.html
 * @author Wei
 * @time  2017年4月18日 上午10:46:45
 */
public class Dom4jTest2 {
	@SuppressWarnings("all")
	public static void main(String[] args) throws DocumentException {
		Element e = null;
		// new一个文件
		File file = new File("C:/Users/Wei/workspace/HeadFirstStruts2Chap08/src/com/java1234/action/ddd.xml");
		System.out.println(file);
		//第一步  获取一个 org.dom4j.io.SAXReader 对象
		SAXReader reader = new SAXReader();
		//第二步  用SAXReader来读取文件，并转换成Document，
		Document doc = reader.read(file);
		// 使用Document的selectNodes来读取节点，返回list
		List<Object> list2 = doc.selectNodes("/syptRequest/syptGsdjxx/NSRMC");
		if (list2.size() > 0) {
			e = (Element) list2.get(0);
		}
		System.out.println("list2.size():"+list2.size());
		if (null != e) {
			Object obj = e.getData();
			System.out.println("----------" + obj);
		} else {
			System.out.println("Element为空");
		}
		
		Element root = doc.getRootElement();
		Namespace namespace = root.getNamespace();
		System.out.println(namespace);
		
		String name = root.getName();
		System.out.println(name);
		
		System.out.println("===========");
		// 获取所有子元素
        List<Element> childList = root.elements();
        System.out.println("total child count: " + childList.size());
        
        // 获取特定名称的子元素
        List<Element> childList2 = root.elements("weiyongle");
        System.out.println("hello child: " + childList2.size());
        
        Element e2 = childList2.get(0);
        String eleName = e2.getName();
        System.out.println(eleName);
        
	}
}
