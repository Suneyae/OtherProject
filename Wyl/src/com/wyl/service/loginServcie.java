package com.wyl.service;

import com.wyl.model.User;
public class loginServcie {
	public boolean ToLonin(User user) {
		//必须为WYL，123才可以正常登录
		if(user.getuName().equals("WYL")&&user.getPassword().equals("123")){
			return true;
		}else{
			return false;
		}
	}
}
