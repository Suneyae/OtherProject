package com.test.javaAPI.wechat.util;

import com.test.javaAPI.wechat.UtilMessage;
import com.test.javaAPI.wechat.entity.Meterial;

/**
 * 上传图片素材的工具类
 * 开发文档参见：https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738729&
 * token=&lang=zh_CN
 * 
 * @author Wei
 * @time 2016年10月17日 下午8:24:21
 */
public class UtilUpload {
	public static void uploadMeterial() {
		Meterial m = new Meterial("我是标题啊", "122", "卫永乐", "我是摘要", "0", "<html><strong>我是卫大爷</strong></html>",
				"http://www.dgtle.com/article-16143-1.html");
		String msg = UtilMessage.MessageToXml(m);
		System.out.println("msg:"+msg);
	}
	public static void main(String[] args) {
		uploadMeterial();
	}
}
