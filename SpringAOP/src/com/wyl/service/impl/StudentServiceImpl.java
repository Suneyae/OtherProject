package com.wyl.service.impl;

import com.wyl.service.StudentService;
/**
 * 实现操作类这个接口
 * @author Wei
 *
 */
public class StudentServiceImpl implements StudentService {

	public StudentServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addStudent(String name) {
		// TODO Auto-generated method stub
		System.out.println("添加学生:" + name);
	}

}
