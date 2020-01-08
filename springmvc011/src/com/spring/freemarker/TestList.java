package com.spring.freemarker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 应该是引用类型导致的，其中
 * map是一个引用类型，
 * @author Wei
 * @time  2017年4月19日 上午11:08:02
 */
public class TestList {

	public TestList() {
	}
	@SuppressWarnings("all")
	public static void main(String[] args) {
		List list = new ArrayList<String>();
		Map map = new HashMap<String,Object>();
		map.put("name", "zhangsan");
		list.add(map);
		System.out.println(list);// [{a=xingming}]
		map.put("name", "lisi");
//		list.add(map);
		System.out.println(list);// [{a=englishName}, {a=englishName}]
		javax.servlet.jsp.JspFactory f = javax.servlet.jsp.JspFactory.getDefaultFactory();
//		f.
	}
}
