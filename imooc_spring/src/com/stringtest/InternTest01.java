package com.stringtest;

public class InternTest01 {
	public static void main(String[] args) {
		String str1 = "str01";
		String str2 = new String("str") + new String("01");
		str2.intern();
		System.out.println(str2 == str1);// #8
	}
}

