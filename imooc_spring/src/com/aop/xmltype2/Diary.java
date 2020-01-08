package com.aop.xmltype2;

import org.springframework.stereotype.Service;

/**
 * 实际上切面就是日志类
 * @author Wei
 * @time  2017年3月28日 下午1:31:44
 */
@Service("wylDiary")
public class Diary {
	
	public void myBegin(){
		System.out.println("Diary.myBegin()...");
	}
	
	public void myEnd(){
		System.out.println("Diary.myEnd()...");
	}
}
