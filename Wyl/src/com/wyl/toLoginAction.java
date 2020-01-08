package com.wyl;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.Preparable;
import com.wyl.model.User;
import com.wyl.service.loginServcie;

public class toLoginAction implements Action,Preparable {
	private HttpServletRequest rq;
	private String querystring;
	private Enumeration attrNames;
	private String attrNames_;
	public String getAttrNames_() {
		return attrNames_;
	}

	public void setAttrNames_(String attrNames_) {
		this.attrNames_ = attrNames_;
	}

	public Enumeration getAttrNames() {
		return attrNames;
	}

	public void setAttrNames(Enumeration attrNames) {
		this.attrNames = attrNames;
	}

	public String getQuerystring() {
		return querystring;
	}

	public void setQuerystring(String querystring) {
		this.querystring = querystring;
	}

	public HttpServletRequest getRq() {
		return rq;
	}

	public void setRq(HttpServletRequest rq) {
		this.rq = rq;
	}

	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		return SUCCESS;
	}

	private String UserName;
	private String passWord;

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

	public String denglu() {
		return "shibai";
	}

	private String mingzi;

	public String getMingzi() {
		return mingzi;
	}

	public void setMingzi(String mingzi) {
		this.mingzi = mingzi;
	}
	
	private String mima;
	public String getMima() {
		return mima;
	}

	public void setMima(String mima) {
		this.mima = mima;
	}
	private User user = new User();
	private loginServcie service = new loginServcie();
	//登陆时调用的action
	public String tijiao() {
		user.setuName(mingzi);
		user.setPassword(mima);
//		user.setRq(rq);
		if(service.ToLonin(user)){
			return SUCCESS;
		}else{
			return ERROR;
		}
	}

	@Override
	public void prepare() throws Exception {
		HashMap<String, Object> data = new HashMap();
		// TODO Auto-generated method stub
		HttpServletRequest rq = ServletActionContext.getRequest();
		String querystring = rq.getQueryString();
		this.querystring = querystring;
		Enumeration attrNames= rq.getAttributeNames();
		while (attrNames.hasMoreElements()) {
			String key = (String) attrNames.nextElement();
			data.put(key, rq.getAttribute(key));
		}
		String attrNames_ = transMapToString(data);
		this.attrNames = attrNames;//相当于是setter方法
		this.attrNames_ = attrNames_;
		this.rq = rq;
	}
	
	public String vali2(){
		return SUCCESS;
	}
	public static String transMapToString(Map map){  
	  java.util.Map.Entry entry;  
	  StringBuffer sb = new StringBuffer();  
	  for(Iterator iterator = map.entrySet().iterator(); iterator.hasNext();)  
	  {  
	    entry = (java.util.Map.Entry)iterator.next();  
	      sb.append(entry.getKey().toString()).append( "'" ).append(null==entry.getValue()?"":  
	      entry.getValue().toString()).append (iterator.hasNext() ? "^" : "");  
	  }  
	  return sb.toString();  
	}  
}
