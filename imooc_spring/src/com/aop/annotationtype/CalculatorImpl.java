package com.aop.annotationtype;

import org.springframework.stereotype.Component;

@Component
public class CalculatorImpl implements ICalculator {

	{
		System.out.println("CalculatorImpl...{} ...");
	}

	@Override
	public int doAdd(int a, int b) {
		// TODO Auto-generated method stub
		return a + b;
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
		return Math.abs(a - b);
	}

}
