package com.test.javaAPI.wechat.entity;

/**
 * 图文消息中的文章的实体类
 * 详见：https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140543&token=&
 * lang=zh_CN
 * 
 * @author Wei
 * @time 2016年10月14日 下午10:26:29
 */
public class Articles {
	private String Title;
	private String Description;
	private String PicUrl;
	private String Url;

	/**
	 * 无参构造器
	 */
	public Articles() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 有参数构造器
	 * 
	 * @param title
	 * @param description
	 * @param picUrl
	 * @param url
	 */
	public Articles(String title, String description, String picUrl, String url) {
		super();
		Title = title;
		Description = description;
		PicUrl = picUrl;
		Url = url;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

}
