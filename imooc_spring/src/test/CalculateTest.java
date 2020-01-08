package test;

import static org.junit.Assert.*;

import org.junit.Test;

import imooc_spring.test.util.Calculate;

public class CalculateTest {

	@Test
	public void testadd(){
//		assertEquals(expected, actual);
		assertEquals(6, new Calculate().add(2, 4));
	}
	@Test
	public void testSubtract(){
		assertEquals(3, new Calculate().sub(5,2));
	}

	
}
