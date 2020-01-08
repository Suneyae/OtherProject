package com.test.javaAPI.wechat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import oracle.jdbc.driver.OracleDriver;

/**
 * 数据连接工具类
 * @author Wei
 * @time  2016年9月22日 下午10:20:52
 */
public class DBConnUtil {
	static Connection conn;
	static String driver = "oracle.jdbc.driver.OracleDriver";
	static String url = "jdbc:oracle:thin:@localhost:1521:orcl2";
	static String user = "hr";
	static String pwd = "hr";
	/**
	 * 链接到本地数据库，账号密码都是hr,
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConn() throws SQLException {
		DriverManager.registerDriver(new OracleDriver());
		Connection conn = DriverManager.getConnection(url, user, pwd);
		System.out.println("DBConnUtil.getConn()....,获取到了链接conn");
		return conn;
	}

}
