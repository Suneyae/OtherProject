package com.lm.activemq;

import org.znfancy.sms.SmsGateway;

public class SMSTest {

	public SMSTest() {
		// TODO Auto-generated constructor stub
	}

	public static String generateMsg(String msg) {
		
		return msg;
	}

	public static void main(String[] args) {
		
		SmsGateway.send("18181663714",generateMsg("i am the test Message "+UtilTime.getCurrentTime(null)));
	}
}
