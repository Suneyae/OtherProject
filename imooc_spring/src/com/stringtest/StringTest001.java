package com.stringtest;

public class StringTest001 {
	/**
	 * 结论 1. JVM只能缓存那些在编译时可以确定的常量，而非运行时常量。
	 * 
	 * 上述代码中的constValue属于编译时常量，而staticValue则属于运行时常量。
	 * 
	 * 2. 通过使用 new方式创建出来的字符串，JVM缓存的方式是不一样的。
	 * 
	 * 所以上述代码中，v1不等同于v3。
	 */
	public static final String constValue = "ab";
	public static final String staticValue;

	static {
		staticValue = "ab";
	}

	public static void main(String[] args) {
		String v1 = "ab";
		String v2 = "ab";
		// true
		System.out.println("v1 == v2 : " + (v1 == v2));
		String v3 = new String("ab");
		// false
		System.out.println("v1 == v3 : " + (v1 == v3));
		String v4 = "abcd";
		String v5 = "ab" + "cd";
		// true
		System.out.println("v4 == v5 : " + (v4 == v5));
		String v6 = v1 + "cd";
		// false 疑问？
		System.out.println("v4 == v6 : " + (v4 == v6));
		String v7 = constValue + "cd";
		// true
		System.out.println("v4 == v7 : " + (v4 == v7));
		String v8 = staticValue + "cd";
		// false
		System.out.println("v4 == v8 : " + (v4 == v8));
		String v9 = v4.intern();
		System.out.println("v4 == v9 :" + (v4 == v9));
		String v10 = new String(new char[] { 'a', 'b', 'c', 'd' });
		String v11 = v10.intern();
		// true
		System.out.println("v4 == v11 :" + (v4 == v11));
		// false
		System.out.println("v10 == v11 :" + (v10 == v11));
	}
}