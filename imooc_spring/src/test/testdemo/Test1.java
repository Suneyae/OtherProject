package test.testdemo;

import org.junit.Test;

public class Test1 {
	@Test
	public void MyTest1(){
		System.out.println("我是Test1.MyTest1()...");
	}
	@SuppressWarnings("all")
	public static void main(String[] args) {
		Test1 t = new Test1();
		t.MyTest1();
	}
}
