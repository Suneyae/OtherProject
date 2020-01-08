package com.test;

import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

public class WebClientTest2 {

	public WebClientTest2() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		Call call;
		Service service = new Service();
		String endpointAddress = "http://localhost:8082/WebServiceTest/services/HelloService";
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress("");
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
