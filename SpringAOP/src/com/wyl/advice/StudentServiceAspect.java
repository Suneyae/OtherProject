package com.wyl.advice;

/**
 * 切面，为了便于理解，可以直接这样想，用于记日记的类就是一个切面
 * 
 * @author Wei
 *
 */
public class StudentServiceAspect {

	/**
	 * 加上了 Joinpoint这个参数就会报错
	 * 
	 * @param p
	 */
	// public void doBefore(Joinpoint p){
	// System.out.println("开始添加学生...");
	// }
	/**
	 * 这样就不会报错
	 */
	public void doBefore() {
		System.out.println("开始添加学生...");
	}

}
