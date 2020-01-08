package com.wyl.entity;

import java.util.List;

public class Student2 {
//	private java.sql.Types.BOOLEAN xx;
//	java.sql.Types;
	private int age;
	private int id;
	private String name;
	private String sex;
	private List wdId;
	
	public List getWdId() {
		return wdId;
	}
	public void setWdId(List wdId) {
		this.wdId = wdId;
	}
	/**
	 * 空构造器
	 */
	public Student2() {
		
	}
	/**
	 * 构造器
	 * @param age
	 * @param name
	 * @param sex
	 */
	public Student2(int age,String name,String sex){
		this.age = age;
		this.name = name;
		this.sex = sex;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAge() {
		return age;
	}
	public String getName() {
		return name;
	}
	public String getSex() {
		return sex;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public void setName(String name) {
		this.name = name;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	@Override
	public String toString() {
		return "Student2 [age=" + age + ", id=" + id + ", name=" + name + ", sex=" + sex + ", wdId=" + wdId + "]";
	}
	
}
