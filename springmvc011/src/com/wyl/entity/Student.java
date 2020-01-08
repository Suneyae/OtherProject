package com.wyl.entity;
/**
 * 学生实体类
 * @author Wei
 * @time  2016年12月4日 上午10:03:11
 */
public class Student {
	private int age;
	private String name;
	private int stuid;

	public Student() {
		// TODO Auto-generated constructor stub
	}

	public Student(int stuid, String name, int age) {
		super();
		this.stuid = stuid;
		this.name = name;
		this.age = age;
	}

	public int getAge() {
		return age;
	}

	public String getName() {
		return name;
	}

	public int getStuid() {
		return stuid;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStuid(int stuid) {
		this.stuid = stuid;
	}

}
