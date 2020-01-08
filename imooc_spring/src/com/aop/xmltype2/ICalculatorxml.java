package com.aop.xmltype2;

/**
 * 加减乘除接口，用于AOP测试
 * @author Wei
 * @time  2017年3月28日 下午1:32:02
 */
public interface ICalculatorxml {
	/**
	 * 加法
	 * 
	 * @param a
	 * @param b
	 * @return a+b
	 */
	public int doAdd(int a, int b);

	/**
	 * 减法
	 * 
	 * @param a
	 * @param b
	 * @return a-b
	 */
	public int doSub(int a, int b);

	/**
	 * 乘法
	 * 
	 * @param a
	 * @param b
	 * @return a*b
	 */
	public int doMul(int a, int b);

	/**
	 * 除法
	 * 
	 * @param a
	 * @param b
	 * @return a/b
	 */
	public int doDiv(int a, int b);

	/**
	 * 求绝对值
	 * 
	 * @param a
	 * @param b
	 * @return |a-b|
	 */
	public int doAbs(int a, int b);
}
