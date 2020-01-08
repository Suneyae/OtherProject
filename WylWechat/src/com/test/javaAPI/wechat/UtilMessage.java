package com.test.javaAPI.wechat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import com.test.javaAPI.wechat.entity.ArticleMessage;
import com.test.javaAPI.wechat.entity.Articles;
import com.thoughtworks.xstream.XStream;

/**
 * 微信消息工具类
 * 
 * @author Wei
 * @time 2016年10月12日 下午10:45:44
 */
public class UtilMessage {
	/**
	 * xml转换为map
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static Map<String, String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException {
		Map<String, String> map = new HashMap<String, String>();
		//生成解析器对象，使用的dom4j-1.6.1版本
		SAXReader reader = new SAXReader();
		//根据HttpServletRequest获得输入流
		InputStream ins = request.getInputStream();
		//通过SAXReader对象把输入流转换成Document对象
		Document doc = reader.read(ins);

		Element root = doc.getRootElement();

		List<Element> list = root.elements();
		for (Element e : list) {
			map.put(e.getName(), e.getText());

		}
		String json = UtilJackson.mapToJsonstr(map);
		System.out.println("json:" + json);
		ins.close();

		return map;
	}

	/**
	 * text转换为 XML
	 * 
	 * @param msg
	 * @return
	 */
	public static String textMessageToXml(TextMessage msg) {
		System.out.println(msg + "的全类名：" + msg.getClass().getName());
		XStream xstream = new XStream();
		// 这里需要替换 ,把<com.test.javaAPI.wechat.TextMessage>这样的标签替换为<xml>
		return xstream.toXML(msg).replaceAll(msg.getClass().getName(), "xml");
	}

	/**
	 * 消息体转为xml格式的字符串
	 * 
	 * @param msg
	 * @return
	 */
	public static String MessageToXml(Object msg) {
		System.out.println(msg + "的全类名：" + msg.getClass().getName());
		XStream xstream = new XStream();
		// 这里需要替换 ,把<com.test.javaAPI.wechat.TextMessage>这样的标签替换为<xml>
		return xstream.toXML(msg).replaceAll(msg.getClass().getName(), "xml");
	}
	
	public static String initArticleMessage(){
		ArticleMessage msg = new ArticleMessage();
		List<Articles> artList = new ArrayList<>();
//		msg.set
		return null;
	}
	
	@Test
	public static void dom4jTest() throws JsonGenerationException, JsonMappingException, IOException{
		Map<String, String> map = new HashMap<String, String>();
		SAXReader saxReader = new SAXReader();
		Document doc = null;
		try {
			doc = saxReader.read(new File("src/tongji.txt"));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Element root = doc.getRootElement();

		List<Element> list = root.elements();
		for (Element e : list) {
			map.put(e.getName(), e.getText());
		}
		String json = UtilJackson.mapToJsonstr(map);
		System.out.println("json:" + json);
	}
	
	
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
//		dom4jTest();
		XStream s = new XStream();
//		String str_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><response><retCode>999999</retCode><retMsg>xml报文不存在！</retMsg></response>";
		
		
//		s.toXML(str_xml);
		
		TextMessage msg = new TextMessage();
		msg.setContent("我是内容");
		msg.setFromUserName("张三");
		String str_xml = s.toXML(msg);
		System.out.println("str_xml:\n"+str_xml);
	}
}
