package com.bjsxt.shopping.util;

import java.sql.Connection;

public class DB2 {
	private static DB2 db;
	static {
		db = new DB2();
	}
	
	private DB2(){};
	
	public static Connection getConn(){
		Connection conn = null;
		
		return conn;
	}
}
