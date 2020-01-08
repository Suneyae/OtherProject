package com.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class UtilProperties {

	public UtilProperties() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param key
	 *            properties的键值对的键
	 * @param properitesName
	 *            properties文件的文件名
	 * @return 返回key对应的值
	 */
	public static String getValueByKeyViaProp(String key, String properitesName) {
		Properties props = new Properties();
		InputStream ips = UtilProperties.class.getResourceAsStream("/" + properitesName);
		String result = "";
		try {
			props.load(ips);
			result = props.getProperty(key);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if("".equals(result)||(null==result)){
			throw new ExceptionWyl("读取配置文件"+properitesName+"的时候出现异常,请检查参数"+key+"以及"+properitesName+"是否正确");
		}
		return result;
	}
}
