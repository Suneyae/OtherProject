package com.java1234.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
	private static Properties props = new Properties();
	private static Connection conn = null;
	/**
	 * 通过db.properties获取配置
	 * @return
	 */
	public Connection getConn(){
		
		File file = new File("util/db.properties");
		FileReader fr;
		Connection conn = null;
		try {
			fr = new FileReader(file);
			props.load(fr);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Class.forName(props.getProperty("drivername"));
			conn = DriverManager.getConnection(props.getProperty("url"),
										props.getProperty("username"), 
										props.getProperty("pwd"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	/**
	 * 返回一个Connection,非空
	 * add by 卫永乐 0113
	 * @return
	 */
	public Connection getConnection2(){
		String driverName = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@10.162.128.131:1521/ybrsjhk";
		String username = "dgsbkf_ggyw";
		String pwd = "aa";
		
		try {
			Class.forName(driverName);
			conn = DriverManager.getConnection(url,username,pwd);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return conn;
	}
	/**
	 * 返回一个空的PreparedStatement
	 * add by 卫永乐 0113
	 * @return
	 */
	public PreparedStatement getPrep(){
		
		return null;
	}
	/**
	 * 返回一个空的ResultSet
	 * add by 卫永乐 0113
	 * @return
	 */
	public ResultSet getRst(){
		return null;
	}
}
