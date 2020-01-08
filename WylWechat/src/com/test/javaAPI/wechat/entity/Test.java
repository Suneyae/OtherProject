package com.test.javaAPI.wechat.entity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParser;

import com.google.gson.Gson;
import com.test.javaAPI.wechat.UtilAccess;
import com.test.javaAPI.wechat.UtilWechat;
/**
 * 原文：http://www.cnblogs.com/always-online/p/3871679.html
 * @author Wei
 * @time  2016年10月17日 下午8:56:14
 */
public class Test {
	public static final String GET_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";// 获取access
	public static final String UPLOAD_IMAGE_URL = "http://file.api.weixin.qq.com/cgi-bin/media/upload";// 上传多媒体文件
	public static final String UPLOAD_FODDER_URL = "https://api.weixin.qq.com/cgi-bin/media/uploadnews";
	public static final String APP_ID = "wxa549b28c24cf341e";
	public static final String SECRET = "78d8a8cd7a4fa700142d06b96bf44a37";

	/**
	 * 上传素材
	 * 
	 * @param uploadurl
	 *            apiurl
	 * @param access_token
	 *            访问token
	 * @param data
	 *            提交数据
	 * @return
	 */
	public static String uploadFodder(String uploadurl, String access_token, String data) {
//		org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
//		String posturl = String.format("%s?access_token=%s", uploadurl, access_token);
//		PostMethod post = new PostMethod(posturl);
//		post.setRequestHeader("User-Agent",
//				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:30.0) Gecko/20100101 Firefox/30.0");
//		post.setRequestHeader("Host", "file.api.weixin.qq.com");
//		post.setRequestHeader("Connection", "Keep-Alive");
//		post.setRequestHeader("Cache-Control", "no-cache");
//		String result = null;
//		try {
//			post.setRequestBody(data);
//			int status = client.executeMethod(post);
//			if (status == HttpStatus.SC_OK) {
//				String responseContent = post.getResponseBodyAsString();
//				System.out.println(responseContent);
//				JsonParser jsonparer = new JsonParser();// 初始化解析json格式的对象
//				JsonObject json = jsonparer.parse(responseContent).getAsJsonObject();
//				if (json.get("errcode") == null) {// 正确 { "type":"news",
//													// "media_id":"CsEf3ldqkAYJAU6EJeIkStVDSvffUJ54vqbThMgplD-VJXXof6ctX5fI6-aYyUiQ","created_at":1391857799}
//					result = json.get("media_id").getAsString();
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			return result;
//		}
		return null;
	}

	public static void main(String[] args) throws Exception {
//		String accessToken = getToken(GET_TOKEN_URL, APP_ID, SECRET);// 获取token在微信接口之一中获取
		String accessToken = (String) UtilAccess.GetAccessToken2Map(UtilWechat.ACCESS_TOKEN_URL).get("access_token");
		if (accessToken != null)// token成功获取
		{
			System.out.println(accessToken);
			File file = new File("f:" + File.separator + "2000.JPG"); // 获取本地文件
//			String id = uploadImage(UPLOAD_IMAGE_URL, accessToken, "image", file);// java微信接口之三—上传多媒体文件
																					// 可获取
			String id = "";
			if (id != null) {
				// 构造数据
				Map map = new HashMap();
//				map.put("thumb_media_id", id);
				map.put("author", "wxx");
				map.put("title", "标题");
				map.put("content", "测试fdsfdsfsdfssfdsfsdfsdfs");
				map.put("digest", "digest");
				map.put("show_cover_pic", "0");

				Map map2 = new HashMap();
//				map2.put("thumb_media_id", id);
				map2.put("author", "wxx");
				map2.put("content_source_url", "www.google.com");
				map2.put("title", "标题");
				map2.put("content", "测试fdsfdsfsdfssfdsfsdfsdfs");
				map2.put("digest", "digest");

				Map map3 = new HashMap();
				List<Map> list = new ArrayList<Map>();
				list.add(map);
				list.add(map2);
				map3.put("articles", list);

				Gson gson = new Gson();
				String result = gson.toJson(map3);// 转换成json数据格式
				String mediaId = uploadFodder(UPLOAD_FODDER_URL, accessToken, result);
				if (mediaId != null) {
					System.out.println(mediaId);
				}
			}
		}
	}
}
