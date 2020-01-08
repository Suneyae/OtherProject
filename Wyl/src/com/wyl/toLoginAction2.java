package com.wyl;

import com.opensymphony.xwork2.Action;
import com.wyl.model.User;
import com.wyl.service.loginServcie;

public class toLoginAction2 implements Action {
	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		return SUCCESS;
	}

	private String UserName;
	private String passWord;
	private String mingzi;
	private String mima;
	private User user = new User();
	private loginServcie service = new loginServcie();
	
	
	public String getUserName() {
		return UserName;
	}


	public void setUserName(String userName) {
		UserName = userName;
	}


	public String getPassWord() {
		return passWord;
	}


	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}


	public String getMingzi() {
		return mingzi;
	}


	public void setMingzi(String mingzi) {
		this.mingzi = mingzi;
	}


	public String getMima() {
		return mima;
	}


	public void setMima(String mima) {
		this.mima = mima;
	}


	//登陆时调用的action
	public String tijiao() {
		user.setuName(mingzi);
		user.setPassword(mima);
		if(service.ToLonin(user)){
			return SUCCESS;
		}else{
			return ERROR;
		}
	}
}
