package com.aop.xmltype2;

import org.springframework.stereotype.Repository;
/**
 * 重新复习切面切点相关的知识，是切点接口的实现类
 * @author Wei
 * @time  2017年3月28日 下午1:31:10
 */
@Repository("calculatorImplxml2")
public class CalculatorImplxml implements ICalculatorxml {

	/*
	 * { System.out.println("CalculatorImpl...{} ..."); }
	 */

	@Override
	public int doAdd(int a, int b) {
		int rtn = a + b;
		System.out.println(a + "+" + b + "=" + rtn);
		return rtn;
	}

	@Override
	public int doSub(int a, int b) {
		// TODO Auto-generated method stub
		return a - b;
	}

	@Override
	public int doMul(int a, int b) {
		// TODO Auto-generated method stub
		return a * b;
	}

	@Override
	public int doDiv(int a, int b) {
		if (b == 0) {
			return -1;
		}
		return a / b;
	}

	@Override
	public int doAbs(int a, int b) {
		System.out.println("int com.aop.xmltype.CalculatorImplxml.doAbs(int a, int b)");
		return Math.abs(a - b);
	}

}
