package com.test.javaAPI.wechat.entity;

/**
 * 图片消息
 * 
 * @author Wei
 * @time 2016年10月17日 下午9:15:49
 */
public class PictureMessage {
	private String ToUserName;
	private String FromUserName;
	private Long CreateTime;
	private String MsgType;
	private String MediaId;

	public PictureMessage() {
	}

	public PictureMessage(String toUserName, String fromUserName, Long createTime, String msgType, String mediaId) {
		super();
		ToUserName = toUserName;
		FromUserName = fromUserName;
		CreateTime = createTime;
		MsgType = msgType;
		MediaId = mediaId;
	}

	public String getToUserName() {
		return ToUserName;
	}

	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}

	public String getFromUserName() {
		return FromUserName;
	}

	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}

	public Long getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Long createTime) {
		CreateTime = createTime;
	}

	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}

}
