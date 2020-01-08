package com.stringtest;

public class StringTest {
    public static void main(String[] args) {
        String str1 = "string";
        String str2 = new String("string");
        String str3 = str2.intern();
 
        System.out.println(str1==str2);//#1
        System.out.println(str1==str3);//#2
    }
}
