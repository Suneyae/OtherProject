package com.atguigu.struts2.helloworld;

import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class TestActionContextAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	@Override
	public String execute() throws Exception {
		ActionContext ac = ActionContext.getContext();
		Map<String,Object> map = ac.getApplication();
		map.put("lastName", "卫");
		map.put("name","卫永乐");
		
		Map<String,Object> map2 = ac.getSession();
		map2.put("age", 33);
		
		
		Map<String,Object> map3 = (Map<String, Object>) ac.get("request");
		map3.put("name_byReq", 555);
		System.out.println("从页面上获取到的时间：");
		return SUCCESS;
	}
}
