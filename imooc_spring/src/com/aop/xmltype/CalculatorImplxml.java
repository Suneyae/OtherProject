package com.aop.xmltype;

import org.springframework.stereotype.Repository;

@Repository("calculatorImplxml")
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
		System.out.println("int com.aop.xmltype.CalculatorImplxml.doAbs(int "+a+", int "+b+")");
		return Math.abs(a - b);
	}

}
