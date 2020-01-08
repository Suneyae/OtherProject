package com.aop.annotationtype;

/**
 * 加减乘除接口，用于AOP测试
 * 
 * @author Wei
 *
 */
public interface ICalculator {
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
