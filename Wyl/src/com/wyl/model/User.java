package com.wyl.model;

import javax.servlet.http.HttpServletRequest;

public class User {
	private HttpServletRequest rq;
	public HttpServletRequest getRq() {
		return rq;
	}

	public void setRq(HttpServletRequest rq) {
		this.rq = rq;
	}

	private String uName;
	private String password;

	public String getuName() {
		return uName;
	}

	public void setuName(String uName) {
		this.uName = uName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
