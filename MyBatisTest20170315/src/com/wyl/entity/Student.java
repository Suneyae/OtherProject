package com.wyl.entity;

public class Student {
//	private java.sql.Types.BOOLEAN xx;
//	java.sql.Types;
	private int age;
	private int id;
	private String name;
	private String sex;
	/**
	 * 空构造器
	 */
	public Student() {
		
	}
	/**
	 * 构造器
	 * @param age
	 * @param name
	 * @param sex
	 */
	public Student(int age,String name,String sex){
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
		return "Student [age=" + age + ", id=" + id + ", name=" + name + ", sex=" + sex + "]";
	}
	
}
