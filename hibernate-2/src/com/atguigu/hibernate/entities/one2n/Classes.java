package com.atguigu.hibernate.entities.one2n;

import java.io.Serializable;
import java.util.Set;

public class Classes implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2465635152130854369L;
	private Integer id;
	private String cName;
	private Set<Student> cStudent;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getcName() {
		return cName;
	}

	public void setcName(String cName) {
		this.cName = cName;
	}

	public Set<Student> getcStudent() {
		return cStudent;
	}

	public void setcStudent(Set<Student> cStudent) {
		this.cStudent = cStudent;
	}


	public Classes() {

	}

	
}
