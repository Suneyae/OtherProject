package com.lm.activemq;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 封装一些时间相关的操作
 * 
 * @author Wei
 * @time 2016年9月17日 上午10:20:53
 */
public class UtilTime {
	/**
	 * 获取当前毫秒数
	 * 
	 * @return
	 */
	public static long getCurTime() {
		long curTime = new Date().getTime();
		return curTime;
	}

	/**
	 * 获取当前时间传入的时间相差的毫秒数
	 * 
	 * @param beginTime
	 * @return
	 */
	public static long getInterValTime(long beginTime) {
		long curTime = getCurTime();
		return curTime - beginTime;
	}

	public static void main(String[] args) {
		// getInterValTime(beginTime)
		long t = getCurTime();
		System.out.println(t);
		System.out.println("currentTime:" + getCurrentTime(null));

	}

	/**
	 * 获取当前时间
	 * 
	 * @param timeformat
	 *            传入的时间格式，如果传入""或者null,那么使用默认格式
	 *            yyyy.MM.dd HH:mm:ss z
	 * @return 时间字符串
	 */
	public static String getCurrentTime(String timeformat) {
		if ("".equals(timeformat) || null == timeformat) {
			// 2016.09.27 公元 at 21:59:15 CST
			// timeformat = "yyyy.MM.dd G 'at' HH:mm:ss z";
			timeformat = "yyyy.MM.dd HH:mm:ss z";
		}
		String time = new SimpleDateFormat(timeformat).format(System.currentTimeMillis());
		return time;
	}
}
