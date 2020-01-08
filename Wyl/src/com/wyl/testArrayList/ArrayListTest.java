package com.wyl.testArrayList;

import java.util.ArrayList;
import java.util.List;

public class ArrayListTest {
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		list.add("wei");
		list.size();
		System.out.println(list.size());
		
		// 移除
		list.remove(list.size()-1);
		System.out.println(list.size());
		
		
		
	}
}
