package com.test.javaAPI.wechat;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;

import com.test.javaAPI.wechat.entity.ArticleMessage;
import com.test.javaAPI.wechat.entity.Articles;
import com.test.javaAPI.wechat.entity.PictureMessage;

/**
 * 微信公众号的访问地址
 * 
 * @author Wei
 * @time 2016年10月13日 下午9:17:02
 */
public class WechatServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/**
		 * 获取access_token,用于get请求中
		 */
//		String accesstoken = UtilAccess.GetAccessToken2(UtilStories.ACCESS_TOKEN_URL);
//		System.out.println("accesstoken:"+accesstoken);
		
		
		
		req.setCharacterEncoding("utf-8");
		resp.setContentType("text/xml;charset=utf-8");
		PrintWriter out = resp.getWriter();
		System.out.println("WechatServlet querystring:" + req.getQueryString());
		String projectName = req.getContextPath();
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rst = null;
		try {
			Map<String, String> map = UtilMessage.xmlToMap(req);
			String toUserName = map.get("ToUserName");
			String fromUserName = map.get("FromUserName");
			String msgType = map.get("MsgType");
			String content = map.get("Content");

			String message = null;
			if ("text".equals(msgType)) { // 对文本消息进行处理
				TextMessage text = new TextMessage();
				text.setFromUserName(toUserName); // 发送和回复是反向的
				text.setToUserName(fromUserName);
				text.setMsgType("text");
				text.setCreateTime(new Date().getTime() + "");
				text.setContent("你发送的消息是：" + content);

				conn = DBConnUtil.getConn();
				prep = conn.prepareStatement("select * from web_url where descr like '%" + content + "%'");
				rst = prep.executeQuery();
				// 排除业绩查询,社保查询以及账户查询
				if (rst.next() && !(content.contains("yjcx") || content.contains("sbcx") || content.contains("zhcx"))) {
					// 如果根据用户关键字在oracle数据库里查出来的有对应的消息，而且消息类型是1，也就是文本消息，那么我们前台就展示文本消息
					String type = rst.getString("TYPE");
					if ("1".equals(type)) {
						// 接收文本消息
						String rtnmsg = rst.getString("RTNMSG");
						String descr = rst.getString("DESCR");
						text.setContent(rtnmsg);
						if (!"".equals(content) && content.contains("笑话")) {
							int i = new Random().nextInt(2);
							String story = "";
							switch (i) {
							case 0:
								story = UtilWechat.STORY00;
								break;
							case 1:
								story = UtilWechat.STORY01;
								break;
							case 2:
								story = UtilWechat.STORY02;
								break;
							default:
								break;
							}
							text.setContent(story);
						} else if (!"".equals(content) && descr.contains(content)) {
							// 根据用户关键词查询数据库里的文本消息
							text.setContent(rtnmsg);
						}
						/**
						 * 组装返回的string类型返回值
						 */
						message = UtilMessage.textMessageToXml(text);
					} else if ("2".equals(type)) {
						if (!"".equals(content) && content.contains("图文")) {
							/**
							 * 如果用户提出需要返回图文消息，那么就提供图文 返回图文消息
							 */
							ArticleMessage msg = new ArticleMessage();
							msg.setArticleCount(1);

							List arr_articles = new ArrayList<Articles>();
							// Articles(String title, String description, String
							// picUrl,
							// String url)
							Articles articles = new Articles("从你的全世界走过", "这是一个电影的标题，好好欣赏哦",
									"http://s.dgtle.com/forum/201509/25/192432xwkyrwfyk5l5amcs.jpg",
									UtilWechat.NEWS_DIGITAL_TAIL);
							arr_articles.add(articles);
							msg.setArticles(arr_articles);
							msg.setCreateTime(new Date().getTime());
							msg.setFromUserName(toUserName);
							msg.setMsgId("234124");
							msg.setMsgType("news");
							msg.setToUserName(fromUserName);
							// message 用来接收组装好的图文消息
							message = UtilMessage.MessageToXml(msg);
							String articlename = articles.getClass().getName();
							message = message.replaceAll(articlename, "item");

						} else if (!"".equals(content)) {
							/**
							 * 如果用户提出需要返回图文消息，那么就提供图文 返回图文消息
							 */
							prep = conn
									.prepareStatement("select * from web_url a where a.descr like '%" + content + "%'");
							rst = prep.executeQuery();
							// 图文消息的地址默认为爱范儿
							String weburl = "http://www.ifanr.com/";
							// 默认图片,马尔代夫
							String picUrl = "http://s.dgtle.com/forum/201610/09/162847asxp636y6p3j6gb6.jpg";
							String descr = "没有找到相关的图文消息，请欣赏爱范儿";
							try {
								if (rst.next()) {
									picUrl = rst.getString("PICURL");
									descr = rst.getString("DESCR");
									weburl = rst.getString("URL");
								}
							} catch (Exception e) {
								System.out.println("获取图文地址消息时候出错,用户录入的关键词:" + content);
							}
							ArticleMessage msg = new ArticleMessage();
							msg.setArticleCount(1);

							List arr_articles = new ArrayList<Articles>();
							// Articles(String title, String description, String
							// picUrl,
							// String url)
							Articles articles = new Articles("从你的全世界走过", descr, picUrl, weburl);
							arr_articles.add(articles);
							msg.setArticles(arr_articles);
							msg.setCreateTime(new Date().getTime());
							msg.setFromUserName(toUserName);
							msg.setMsgId("234124");
							msg.setMsgType("news");
							msg.setToUserName(fromUserName);
							// message 用来接收组装好的图文消息
							message = UtilMessage.MessageToXml(msg);
							String articlename = articles.getClass().getName();
							message = message.replaceAll(articlename, "item");
						}
					}else if("3".equals(type)){
						//回复图片消息
						PictureMessage picMsg = new PictureMessage(toUserName, fromUserName, Long.valueOf(214124), "image", "1234");
						UtilMessage.MessageToXml(picMsg);
						System.out.println("");
						
					}
				} else if (!"".equals(content) && (content.contains("社保") || content.contains("社会"))) {
					String story = "";
					story = UtilWechat.SBCX;
					text.setContent(story);
					/**
					 * 组装返回的string类型返回值
					 */
					message = UtilMessage.textMessageToXml(text);
				} else if (!"".equals(content) && (content.contains("sbcx") && (content.length() > 4))) {
					/**
					 * 养老参保人查询
					 */
					String cxtj = content.substring(content.indexOf("sbcx") + 4);
					cxtj = cxtj.trim();
					/*
					 * String querySql =
					 * "select count(distinct(aac001)) CNT from ac02@TO_MSSCK where aac008 = '1' and aae140 = '110' and  aab001 in (select aab001 from ae01@TO_MSSCK where aab999  like '%"
					 * + cxtj + "%')";
					 */
					String querySql = GetQuery(cxtj);
					System.out.println("养老参保人数  查询语句打印：" + querySql);
					conn = DBConnUtil.getConn();
					prep = conn.prepareStatement(querySql);
					rst = prep.executeQuery();
					if (rst.next()) {
						int num = rst.getInt("CNT");
//						text.setContent("单位 " + cxtj + "共有正常参保人数 :" + num);
						text.setContent("暂时没有得到社保局授权无法获取到数据!如果获取到授权，该功能可以开放");
					} else {
						text.setContent("对不起，我没查到任何信息，可能是我报错了!");
					}
					/**
					 * 组装返回的string类型返回值
					 */
					message = UtilMessage.textMessageToXml(text);
				} else if (!"".equals(content) && (content.contains("zhcx") && (content.length() > 4))) {
					/**
					 * 养老个人账户查询
					 */
					String cxtj = content.substring(content.indexOf("zhcx") + 4);
					cxtj = cxtj.trim();
					String querySql = "select cac047 from sic81@TO_MSSCK where aac001 in (select aac001 from ac01@TO_MSSCK where aac002 ='"
							+ cxtj + "')";
					System.out.println("养老账户  查询语句打印：" + querySql);
					prep = conn.prepareStatement(querySql);
					rst = prep.executeQuery();
					if (rst.next()) {
						String num = rst.getString("cac047");
//						text.setContent("您" + cxtj + "的养老账户余额 :" + num);
						text.setContent("暂时没有得到社保局授权无法获取到数据!");
					} else {
						text.setContent("对不起，我没查关于您" + cxtj + "的养老账户余额!");
					}
					message = UtilMessage.textMessageToXml(text);
				} else if (!"".equals(content) && content.contains("yjcx") && content.length() > 4) {
					content = content.substring(content.indexOf("yjcx") + 4).trim();
					String query_sql = "select * from ac35_yg where aac003 like '%" + content + "%'";
					prep = conn.prepareStatement(query_sql);
					rst = prep.executeQuery();
					Long saleroom = new Long(0);
					if (rst.next()) {
						saleroom = rst.getLong("SALEROOM");
					}
					// 根据用户查询消息的格式来查询销售额
					text.setContent("员工" + content + "的销售额为：" + saleroom);
					message = UtilMessage.textMessageToXml(text);
				} else {
					// 如果在数据库里查询了，但是没有匹配的任何记录，那么就默认回复文本消息
//					text.setContent("Sorry,没有查询到关于" + content + "的任何内容," + text.getContent()+",请联系卫永乐更新内容");
					text.setContent("哇,竟然没查询到关于'" + content + "'的任何内容," + text.getContent()+",请联系藤井的井进行更新内容!");
					message = UtilMessage.textMessageToXml(text);
				}

			} else if ("image".equals(msgType)) {
				PicMessage img = new PicMessage();
				System.out.println("==========你发送了图片============");
				img.setToUserName(fromUserName);
				img.setFromUserName(toUserName);
				img.setCreateTime(new Date().getTime());
				img.setMsgType(msgType);
				// img.setPicUrl(projectName+"/myimg/ydd.jpg");
				// img.setPicUrl("/myimg/ydd.jpg");
				img.setPicUrl("http://s.dgtle.com/forum/201509/25/192432xwkyrwfyk5l5amcs.jpg");
				img.setMediaId(map.get("MediaId"));
				img.setMsgId(map.get("MsgId"));

				message = UtilMessage.MessageToXml(img);
			}else if("event".equals(msgType)){
				//事件判定
				String event = map.get("Event");
				TextMessage text = new TextMessage();
				text.setFromUserName(toUserName); // 发送和回复是反向的
				text.setToUserName(fromUserName);
				text.setMsgType("text");
				text.setCreateTime(new Date().getTime() + "");
				if(UtilWechat.EVENT_MSG_SUBSCRIBE.equals(event)){
					text.setContent(UtilWechat.MSG_WELCOME);
				}else if(UtilWechat.EVENT_MSG_UNSUBSCRIBE.equals(event)){
					text.setContent(UtilWechat.MSG_WELCOME_);
				}
				message = UtilMessage.textMessageToXml(text);
				
			}
			System.out.println("message:\n" + message);
			out.print(message); // 将回应发送给微信服务器
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			out.close();
		}

	}

	private String GetQuery(String cxtj) {
		String querySql = UtilWechat.queryms + cxtj + "%')";
		return querySql;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/**
		 * 微信公众号的入口地址，在这里 千万注意不能调用父类的 的doGet(req,resp)方法， 否则会报错，400的错误 HTTP
		 * Status 400 - HTTP method GET is not supported by this URL
		 */

		// super.doGet(req, resp);
		String query = req.getQueryString();
		System.out.println("query:" + query);

		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr");
		System.out.println(
				"signature:" + signature + ",timestamp:" + timestamp + ",nonce:" + nonce + ",echostr:" + echostr);
		PrintWriter out = resp.getWriter();
		if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
			System.out.println("echostr:" + echostr);
			// out.print(echostr); // 校验通过，原样返回echostr参数内容
			out.write(echostr);
		}
	}
	public static void main(String[] args) {
		PictureMessage picMsg = new PictureMessage("2UserName", "fromUserName", Long.valueOf(214124), "image", "1234");
		String msg = UtilMessage.MessageToXml(picMsg);
		System.out.println("msg:\n"+msg);
	}
}