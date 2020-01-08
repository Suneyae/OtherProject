package com.wyl.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 
 * @author Wei
 * @time  2017年4月22日 上午9:23:22
 */
public class UtilHttp {

	public UtilHttp() {
		// TODO Auto-generated constructor stub
	}
	
	public static void SendPost(String urlReq){
		try {
			URL url = new URL(urlReq);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.connect();
			conn.disconnect();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void SendGet(String urlReq){
		try {
			URL url = new URL(urlReq);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.connect();
			conn.disconnect();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) throws UnsupportedEncodingException {
		String url = "http://localhost:8080/servlet/AndroidServlet?name=杨丹丹";
		
//		UtilHttp.SendPost(url);
		String info = "Yang杨丹丹";
//		String s = URLDecoder.decode("卫永乐", "UTF-8");
		String s = URLEncoder.encode(info, "UTF-8");
		System.out.println("s加密:"+s);
		s = URLDecoder.decode(s, "UTF-8");
		System.out.println("s解密:"+s);
	}
}
