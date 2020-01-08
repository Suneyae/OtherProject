package com.java1234.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import oracle.jdbc.driver.OracleDriver;

public class DBConnUtil {
	static Connection conn;
	String url = "jdbc:oracle:thin:@localhost:1521:orcl2";
	String user = "hr";
	String pwd = "hr";

	public Connection getConn() throws SQLException {
		DriverManager.registerDriver(new OracleDriver());
		Connection conn = DriverManager.getConnection(url, user, pwd);
		System.out.println("DBConnUtil.getConn()....,获取到了链接conn");
		return conn;
	}

	public static Connection getConnBySpring() {
		// 获取到数据源
		ComboPooledDataSource ds = (ComboPooledDataSource) Pub.getBeanCtx().getBean("dataSource");
		// 可以用批量操作sql

		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			System.out.println("com.util.DBConnUtil.getConnBySpring(),获取数据库连接失败...");
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 获取JdbcTemplate，用于批量操作sql
	 * 
	 * @return
	 */
	public static JdbcTemplate getJdbcTemplate() {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) Pub.getBeanCtx().getBean("jdbcTemplate");
		return jdbcTemplate;
	}

	public static void main(String[] args) {
		System.out.println("Pub.getBeanCtx():" + Pub.getBeanCtx());
	}
}
