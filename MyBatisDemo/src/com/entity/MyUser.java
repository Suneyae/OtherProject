package com.entity;


public class MyUser {
	private Long id;

	/*
	 * user specified user ID
	 */
	private String username;

	String administrator;
	String password;
	
	
	
	public MyUser() {
		super();
	}

	public MyUser(Long id, String username, String administrator, String password) {
		super();
		this.id = id;
		this.username = username;
		this.administrator = administrator;
		this.password = password;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
