package com.wyl.entity;

public class MyUser {
	private Long id;

	/*
	 * user specified user ID
	 */
	private String username;


	String administrator;

	public MyUser() {
		setUsername(new String());
		setAdministrator("我是admin");
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAdministrator() {
		return administrator;
	}

	public void setAdministrator(String administrator) {
		this.administrator = administrator;
	}


}