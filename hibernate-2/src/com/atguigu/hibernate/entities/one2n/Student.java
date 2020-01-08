package com.atguigu.hibernate.entities.one2n;

import java.io.Serializable;

public class Student implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String sName;
	private String sSex;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getsName() {
		return sName;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}

	public String getsSex() {
		return sSex;
	}

	public void setsSex(String sSex) {
		this.sSex = sSex;
	}

	public Student() {
		// TODO Auto-generated constructor stub
	}

	public Student(String sName, String sSex) {
		super();
		this.sName = sName;
		this.sSex = sSex;
	}

}
