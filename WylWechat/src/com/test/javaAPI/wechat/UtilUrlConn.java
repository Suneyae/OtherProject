package com.test.javaAPI.wechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class UtilUrlConn {
	private static String URL_HAO123 = "https://www.hao123.com/";
	private static String URL_WECHAT = "https://mp.weixin.qq.com/debug/cgi-bin/apiinfo?t=index&type=%E5%9F%BA%E7%A1%80%E6%94%AF%E6%8C%81&form=%E8%8E%B7%E5%8F%96access_token%E6%8E%A5%E5%8F%A3%20/token";

	/**
	 * 根据url地址获取网页内容
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String getWebContent(String url) throws IOException {
		URL Ourl = new URL(url);
		URLConnection conn = Ourl.openConnection();

		// 设置通用属性
		conn.setRequestProperty("Accept", "*/*");
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");

		conn.connect();
		InputStream ins = conn.getInputStream();
		// BufferedReader reader = new BufferedReader(ins);
		BufferedReader breader = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
		String line;
		String result = "";
		// 读取返回的内容
		while ((line = breader.readLine()) != null) {
			result += line;
		}
		System.out.println("=========================UtilUrlConn.getWebContent(String url) :" + result);
		return result;
	}

	public static void main(String[] args) throws IOException {
//		String result = getWebContent(URL_HAO123);
//		getWebContent(UtilStories.ACCESS_TOKEN_URL);
//		UtilAccess.GetResultByUrl(URL_HAO123);
		getWebContent(UtilWechat.ACCESS_TOKEN_URL);
	}
}
