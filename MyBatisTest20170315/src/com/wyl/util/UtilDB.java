package com.wyl.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class UtilDB {
	
	private static String PATH_CONFIG = "com/wyl/config/Configuration.xml";
	public UtilDB() {
		
	}
	/**
	 * 获取SqlSession
	 * @return
	 * @throws IOException
	 */
	public static SqlSession getSqlSession() throws IOException{
		InputStream ips = Resources.getResourceAsStream(PATH_CONFIG);
		SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(ips);
		SqlSession sqlsession = factory.openSession();
		return sqlsession;
	}
	
	public static void main(String[] args) throws IOException {
		SqlSession sqlsession = getSqlSession();
		System.out.println("sqlsession:"+sqlsession);
	}
}
