package com.bjsxt.shopping.util;
import java.sql.*;

import oracle.jdbc.driver.OracleDriver;

public class DB {
	public static Connection getConn() {
		String url = "jdbc:oracle:thin:@localhost:1521:orcl2"; 
	    String user = "hr"; 
	    String pwd = "hr"; 
		Connection conn = null;
		try {
//			Class.forName("com.mysql.jdbc.Driver");
			DriverManager.registerDriver(new OracleDriver());
//			conn = DriverManager.getConnection("jdbc:mysql://localhost/shopping?user=root&password=123");
			conn = DriverManager.getConnection(url, user, pwd);// 得到连接对象 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
	public static PreparedStatement prepare(Connection conn,  String sql) {
		PreparedStatement pstmt = null; 
		try {
			if(conn != null) {
				pstmt = conn.prepareStatement(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pstmt;
	}
	
	public static PreparedStatement prepare(Connection conn,  String sql, int autoGenereatedKeys) {
		PreparedStatement pstmt = null; 
		try {
			if(conn != null) {
				pstmt = conn.prepareStatement(sql, autoGenereatedKeys);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pstmt;
	}
	
	public static Statement getStatement(Connection conn) {
		Statement stmt = null; 
		try {
			if(conn != null) {
				stmt = conn.createStatement();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stmt;
	}
	
	/*
	public static ResultSet getResultSet(Connection conn, String sql) {
		Statement stmt = getStatement(conn);
		ResultSet rs = getResultSet(stmt, sql);
		close(stmt);
		return rs;
	}
	*/
	
	public static ResultSet getResultSet(Statement stmt, String sql) {
		ResultSet rs = null;
		try {
			if(stmt != null) {
				rs = stmt.executeQuery(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public static void executeUpdate(Statement stmt, String sql) {
		try {
			if(stmt != null) {
				stmt.executeUpdate(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(Connection conn) {
		try {
			if(conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(Statement stmt) {
		try {
			if(stmt != null) {
				stmt.close();
				stmt = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(ResultSet rs) {
		try {
			if(rs != null) {
				rs.close();
				rs = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
