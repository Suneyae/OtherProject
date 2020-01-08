package com.util;

/**
 * 字符串转换工具类
 * 
 * @author Wei
 * 
 *
 */
public class CharUtil {

	/**
	 * 检查是否为空,如果是null,或者"",那么返回true,否则返回false
	 * 
	 * @param obj
	 * @return boolean
	 */
	public static boolean isNull(Object obj) {
		String _tmp = "";
		if (obj != null) {
			_tmp = obj.toString();
		}
		if (_tmp.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * String转为Integer
	 * 
	 * @param str
	 * @return Integer
	 */
	public static Integer StrToInteger(String str) {
		if (!isNull(str)) {
			return Integer.valueOf(str);
		} else {
			return 0;
		}

	}

	/**
	 * String 转为int
	 * 
	 * @param str
	 * @return int
	 */
	public static int StrToInt(String str) {
		Integer _tmp = StrToInteger(str);
		int _tmpInt = _tmp.intValue();
		return _tmpInt;

	}

	/**
	 * Integer转为String
	 * 
	 * @param integer
	 * @return String
	 */
	public static String IntegerToStr(Integer integer) {
		if (!isNull(integer)) {
			return integer.toString();
		} else {
			return "";
		}
	}

	/**
	 * Integer转为 int
	 * 
	 * @param integer
	 * @return int
	 */
	public static int IntegerToInt(Integer integer) {
		return integer.intValue();
	}

	/**
	 * int转为String
	 * 
	 * @param i
	 * @return String
	 */
	public static String IntToStr(int i) {
		Integer tmpInt = new Integer(i);
		return IntegerToStr(tmpInt);
	}

	/**
	 * int转为Integer
	 * 
	 * @param i
	 * @return Integer
	 */
	public static Integer IntToInteger(int i) {
		return new Integer(i);
	}

	/**
	 * 测试类
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		int i = CharUtil.StrToInt("2342");
		System.out.println(i);
		boolean flag = CharUtil.isNull(null);
		System.out.println("flag:" + flag);
	}

}
