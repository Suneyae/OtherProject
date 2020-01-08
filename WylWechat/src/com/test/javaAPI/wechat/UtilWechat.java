package com.test.javaAPI.wechat;

/**
 * 微信的一些操作提示
 * 
 * @author Wei
 * @time 2016年10月14日 下午5:20:59
 */
public class UtilWechat {
	/**
	 * 笑话0
	 */
	public static String STORY00 = "我：大师，帮我看看我和女友的恋情能顺利吗。大师眉头紧皱：这个……我：大师有何难言不妨直说。大师：怒我直言，你这个牌子的充气娃娃容易爆。";
	/**
	 * 笑话1
	 */
	public static String STORY01 = "小李：“我爱上个女护士，非常漂亮，想要追求她，怎么展开？”大黄：“简单，你假装生病，然后接近她，日久生情，肯定能成功！”几天后，小李又来找大黄小李：“你出的什么狗屁主意，昨天我像她表白，她说，别的科室病人可以接受，她科室的病人，她不敢接受！”大黄：“你没问为什么？”小李：“问了，她说，来他们性病科的男人，肯定都不老实！”大黄：“我靠，她是性病科的护士啊！”";
	/**
	 * 笑话2
	 */
	public static String STORY02 = "天上飘着雪花，妻子在屋里补衣服，老公边做木工活儿边问：“雪下多厚啦？”妻子答：“有煎饼那么厚了。”过了一会儿，木匠又问：“雪下多厚啦？”妻子答：“有油馍那么厚了。”又过了一个小时，木匠再次问：“雪下多厚了？”妻答：“有烧饼那么厚了。”木匠怒道：“你就知道吃！”顺手给了妻子一个耳光。妻子指着自己的嘴说：“看你把我打的，嘴肿得跟包子似的。”";
	/**
	 * 回复格式说明
	 */
	public static String SBCX = "查询格式 sbcx单位编号,现在只是测试， 只能够查询 单位的正常参保的总人数,查询格式 sbcx 单位编号 ,参保人数查询支持模糊查询; 例如 sbcx02123456 ,养老账户余额的查询格式： zhcx身份证号,账户余额不支持模糊查询，因为这样的话可能导致查询超时";
	/**
	 * 眉山养老正常参保的查询语句
	 */
	public static String queryms = "select count(distinct(aac001)) CNT from ac02@TO_MSSCK where aac008 = '1' and aae140 = '110' and  aab001 in (select aab001 from ae01@TO_MSSCK where aab999  like '%";
	/**
	 * 宜宾养老正常参保的查询语句
	 */
	public static String queryyb = "select count(distinct(aac001)) CNT from ac02@TO_SCK where aac008 = '1' and aae140 = '110' and  aab001 in (select aab001 from ae01@TO_SCK where aab999  like '%";

	public static String DESCR = "暂时支持的关键词 公众号";
	public static String NEWS_JIANGSU = "http://www.js.xinhuanet.com/";

	public static String NEWS_DIGITAL_TAIL = "http://www.dgtle.com/";

	public static final String MSG_WELCOME = "欢迎关注卫永乐的微信公众号,以下是一些现在支持的一些小功能 , \n 1 业务查询  \n业绩查询的格式：yjcx姓名,例如如果要查许洁的销售额,那么输入 yjcx许洁。\n2 关键字查询  \n目前支持的关键字：熊猫tv,熊猫tv月亮luna,数字尾巴,江苏新闻,好123,网页导航,京东商城,苏宁易购,西华大学,锦地苑,微信公众号申请页面如何使用";
	
//	public static final String MSG_WELCOME = "欢迎关注藤井的井的微信公众号,以下是一些现在支持的一些小功能 , \n 1 业务查询  \n业绩查询的格式：yjcx姓名,例如如果要查许洁的销售额,那么输入 yjcx许洁。\n2 关键字查询  \n目前支持的关键字：熊猫tv,熊猫tv月亮luna,数字尾巴,江苏新闻,好123,网页导航,京东商城,苏宁易购,西华大学,锦地苑,微信公众号申请页面如何使用";
	
	public static final String MSG_WELCOME_ = "欢迎下次继续使用!";
	
	/**
	 * 订阅事件
	 */
	public static final String EVENT_MSG_SUBSCRIBE = "subscribe";
	/**
	 * 取消订阅事件
	 */
	public static final String EVENT_MSG_UNSUBSCRIBE = "unsubscribe";
	/**
	 * 我的公众号(奋力啊奋力)的access_token_url
	 * 对应着微信公众平台页面的基本配置里的AppSecret,
	 * 下面是微信公众号首页
	 * https://mp.weixin.qq.com/advanced/advanced?action=dev&t=advanced/dev&token=945658411&lang=zh_CN
	 * AppSecret:caab8f8868f89fe516c9853e3e887529
	 * AppId	:wx62e81b5cdb35f419
	 */
	public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx62e81b5cdb35f419&secret=caab8f8868f89fe516c9853e3e887529";
	/**
	 * 这是获取微信服务器ip的get请求地址,需要在这个地址的最后加上access_token才可以使用,可以参见微信官方文档：https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140187&token=&lang=zh_CN
	 */
	public static final String URL_ACCESS_WECHAT_IP = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=";
	
	/**
	 * 测试用户 access_token_url，详见：http://mp.weixin.qq.com/debug/cgi-bin/sandboxinfo?action=showinfo&t=sandbox/index 
	 */
	public static final String ACCESS_TOKEN_URL_TEST = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx382faaae3d6dd9fa&secret=0ac4c67ccbb2a13d8e2408c5da4639db";
	
	public static final String ACCESS_TOKEN_URL_TES_ = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx382faaae3d6dd9fa&secret=0ac4c67ccbb2a13d8e2408c5da4639db";
	
	

}
