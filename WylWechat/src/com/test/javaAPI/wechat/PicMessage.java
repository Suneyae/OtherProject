package com.test.javaAPI.wechat;

/**
 * 图片消息实体类
 * 
 * @author Wei
 * @time 2016年10月14日 上午10:20:10
 */
public class PicMessage extends MessagePub {
	private String PicUrl;
	private String MediaId;
	private String ToUserName;

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}

	public String getToUserName() {
		return ToUserName;
	}

	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}
}
