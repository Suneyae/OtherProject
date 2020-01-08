package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import test.testdemo.Test1;
import test.testdemo.Test2;
import test.testdemo.Test3;

/**
 * 1 测试套件就是组织测试类一起运行的
 * 
 * 写一个作为测试套件的入口类，这个类里不包含其他的方法,比如本类MySuiteTest 更改测试运行器 @RunWith(Suite.class)
 * 将要测试的类作为数组传入到数组中@Suite.SuiteClasses({Test1.class})
 * 
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ Test1.class, Test2.class, Test3.class })
public class MySuiteTest {
	/**
	 * 1 测试套件就是组织测试类一起运行的
	 * 
	 * 写一个作为测试套件的入口类，这个类里不包含其他的方法,比如本类MySuiteTest 更改测试运行器 @RunWith(Suite.class)
	 * 将要测试的类作为数组传入到数组中@Suite.SuiteClasses({Test1.class})
	 * 
	 */
}
