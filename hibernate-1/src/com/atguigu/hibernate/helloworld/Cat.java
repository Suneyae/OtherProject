package com.atguigu.hibernate.helloworld;

/**
 * Cat类，实现Animal接口
 * 
 * @author Wei
 *
 */
public class Cat {
	private Integer id;
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	private String name;
	private String sex;

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Cat(String name,String sex) {
		this.name = name;
		this.sex = sex;
	}

	public Cat() {
	}

}
