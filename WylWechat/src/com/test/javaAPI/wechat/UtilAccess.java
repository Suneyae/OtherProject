package com.test.javaAPI.wechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

/**
 * 封装了获取access_token的方法 获取各种access
 * 根据url发送get请求：参考：http://langgufu.iteye.com/blog/2159634
 * 
 * @author Wei
 * @time 2016年10月17日 上午9:26:49
 */
public class UtilAccess {

	/**
	 * 
	 * @param url
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public static String GetResultByUrl(String url)
			throws MalformedURLException, IOException, UnsupportedEncodingException {
		String result = "";// 返回的结果
		BufferedReader in = null;// 读取响应输入流
		StringBuffer sb = new StringBuffer();// 存储参数
		String params = "";// 编码之后的参数
		URL connUrl = new URL(url);
		URLConnection httpConn = connUrl.openConnection();
		// 设置通用属性
		httpConn.setRequestProperty("Accept", "*/*");
		httpConn.setRequestProperty("Connection", "Keep-Alive");
		httpConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
		// 建立实际的连接
		httpConn.connect();
		// 响应头部获取
		Map<String, List<String>> headers = httpConn.getHeaderFields();
		// 遍历所有的响应头字段
		for (String key : headers.keySet()) {
			System.out.println(key + "\t：\t" + headers.get(key));
		}
		// 定义BufferedReader输入流来读取URL的响应,并设置编码方式
		in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
		String line;
		// 读取返回的内容
		while ((line = in.readLine()) != null) {
			result += line;
		}
		if (in != null) {
			in.close();
		}
		return result;
	}

	/**
	 * 封装了微信中获取access_token的方法,根据url把返回值转换为map类型,并返回这个map
	 * 我自己的微信公众号获取access_token的地址：UtilStories.ACCESS_TOKEN_URL
	 * 
	 * @param url
	 * @throws IOException
	 */
	public static Map<String, Object> GetAccessToken2Map(String url) throws IOException {
		String result = GetResultByUrl(url);
		// 一个实例就是:
		// result:{"access_token":"oEHRE52tf-NDt27oizqxiv2h3xFfeWqUTXyjshyp-GTwAdKp_VwDfn8nuGEXFEs6HijSobEFZWD7kVSz1ofZJBjLTFcECAZwHhby_yUcG3BNh3m-oYiaHLEi9-4aA8CdNDXaAIAYOI","expires_in":7200}
		System.out.println("result:" + result);

		String access_token = result.substring(result.indexOf("access_token") + "access_token".length() + 3,
				result.indexOf("expires_in") - 3);
		String expires_in = result.substring(result.indexOf("expires_in") + "expires_in".length() + 2).replaceAll("}",
				"");
		Map<String, Object> map = new HashMap<>();
		map.put("access_token", access_token);
		map.put("expires_in", Integer.valueOf(expires_in));
		// System.out.println("access_token:"+access_token+",expires_in:"+expires_in);
		return map;
	}

	/**
	 * 根据微信公众号平台官方文档提供的地址获取微信服务器的地址，
	 * 可以参见官网文档，https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140187&token=&lang=zh_CN
	 * 的 关于"获取微信服务器IP地址"的章节里的介绍
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @throws MalformedURLException
	 */
	@Test
	public static String getWechatIpByUrl(String url)
			throws MalformedURLException, UnsupportedEncodingException, IOException {
		/**
		 * 1 获取token
		 */
		Map map = GetAccessToken2Map(UtilWechat.ACCESS_TOKEN_URL);
		String access_token = (String) map.get("access_token");
		/**
		 * 根据微信公众号平台提供的获取微信服务器地址的get请求地址获取返回值,通过这个结果可以获取微信公众号平台提供的动态变化的token，默认有效时间是7200秒
		 */
		String result = GetResultByUrl(UtilWechat.URL_ACCESS_WECHAT_IP + access_token);
		/**
		 * 
		 * 打印结果：result:{"ip_list":["101.226.62.77","101.226.62.78","101.226.62.79","101.226.62.80","101.226.62.81","101.226.62.82","101.226.62.83","101.226.62.84","101.226.62.85","101.226.62.86","101.226.103.59","101.226.103.60","101.226.103.61","101.226.103.62","101.226.103.63","101.226.103.69","101.226.103.70","101.226.103.71","101.226.103.72","101.226.103.73","140.207.54.73","140.207.54.74","140.207.54.75","140.207.54.76","140.207.54.77","140.207.54.78","140.207.54.79","140.207.54.80","182.254.11.203","182.254.11.202","182.254.11.201","182.254.11.200","182.254.11.199","182.254.11.198","59.37.97.100","59.37.97.101","59.37.97.102","59.37.97.103","59.37.97.104","59.37.97.105","59.37.97.106","59.37.97.107","59.37.97.108","59.37.97.109","59.37.97.110","59.37.97.111","59.37.97.112","59.37.97.113","59.37.97.114","59.37.97.115","59.37.97.116","59.37.97.117","59.37.97.118","112.90.78.158","112.90.78.159","112.90.78.160","112.90.78.161","112.90.78.162","112.90.78.163","112.90.78.164","112.90.78.165","112.90.78.166","112.90.78.167","140.207.54.19","140.207.54.76","140.207.54.77","140.207.54.78","140.207.54.79","140.207.54.80","180.163.15.149","180.163.15.151","180.163.15.152","180.163.15.153","180.163.15.154","180.163.15.155","180.163.15.156","180.163.15.157","180.163.15.158","180.163.15.159","180.163.15.160","180.163.15.161","180.163.15.162","180.163.15.163","180.163.15.164","180.163.15.165","180.163.15.166","180.163.15.167","180.163.15.168","180.163.15.169","180.163.15.170","101.226.103.0\/25","101.226.233.128\/25","58.247.206.128\/25","182.254.86.128\/25","103.7.30.21","103.7.30.64\/26","58.251.80.32\/27","183.3.234.32\/27","121.51.130.64\/27"]}
		 */
		System.out.println("result:" + result);
		return null;
	}

	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// String accesstoken =
		// UtilAccess.GetAccessToken2(UtilStories.ACCESS_TOKEN_URL);
		// System.out.println("accesstoken:"+accesstoken);
//		Map map = GetAccessToken2Map(UtilStories.ACCESS_TOKEN_URL);
		//用于获取测试用户的token
		Map map = GetAccessToken2Map(UtilWechat.ACCESS_TOKEN_URL_TEST);
		String token = (String) map.get("access_token");
		int timeout = (int) map.get("expires_in");
		System.out.println("token:" + token + ",timeout:" + timeout);
		//测试获取微信服务器地址，
//		 getWechatIpByUrl("");
		
		
		
		
	}

	/**
	 * 根据url地址返回获取到的get请求
	 * 
	 * @param url
	 * @return
	 */
	public static String getResponseByUrl(String url) {

		return null;

	}

}
