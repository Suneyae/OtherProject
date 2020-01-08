package com.test;

import org.znfancy.sms.SmsGateway;

public class SMSTest {

	public SMSTest() {
		// TODO Auto-generated constructor stub
	}

	public static String generateMsg(String msg) {

		return msg;
	}

	public static void main(String[] args) {
		String msg = generateMsg("i am the test Message ");
		// msg = "我是短信内容";
		System.out.println("msg:" + msg);
		// SmsGateway.send("18181663714", msg);
		SmsGateway.send("13238431961", msg);
		// SmsGateway.send("13238431961",generateMsg("i am the test Message
		// "+UtilTime.getCurrentTime(null)));
	}
}
