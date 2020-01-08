package com.jdbctemplate.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.util.Pub;

public class JDBCTest {
	static Connection conn;
	private ClassPathXmlApplicationContext ctx = null;
	private JdbcTemplate jdbcTemplate;

	{
		ctx = Pub.getBeanCtx();
		jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");
	}

	@Test
	public void test01() {
		// 获取到Connection
		ComboPooledDataSource ds = (ComboPooledDataSource) Pub.getBeanCtx().getBean("dataSource");

		JdbcTemplate jdbcTemplate = (JdbcTemplate) Pub.getBeanCtx().getBean("jdbcTemplate");
		// jdbcTemplate.

		try {
			conn = ds.getConnection();
			System.out.println(conn.toString());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testUpdate() throws SQLException {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) Pub.getBeanCtx().getBean("jdbcTemplate");
		String sql = " update temp_spring set e = ? where aac003 =? ";
		sql = " insert into temp_spring (AAC003, AAC002, D, E, F, G, H, I, J, K)"
				+ "values ('陈杰aaa', '512533197001261316', '大邑县晋原镇堰北路1151号', '成都市', '5', '5', '4', '大邑县人民医院', '大邑中医院', '大邑县第二人民医院')";
//		jdbcTemplate.update(sql, "达州市", "陈杰aaa");
		sql = " insert into temp_spring (AAC003, AAC002, D, E, F, G, H, I, J, K)"
				+ "values ('陈杰xxx', '512533197001261316', '大邑县晋原镇堰北路1151号', '成都市', '5', '5', '4', '大邑县人民医院', '大邑中医院', '大邑县第二人民医院')";
		jdbcTemplate.execute(sql);
	}
	
	
	
	
	
	
	@Test
	public void testBatchUpdate() {
		String sql = " insert into temp_spring (AAC003, AAC002, D, E, F, G, H, I, J, K)"
				+ "values ('陈杰aaa', '?', '大邑县晋原镇堰北路1151号', '成都市', '5', '5', '4', '大邑县人民医院', '大邑中医院', '大邑县第二人民医院')";

		jdbcTemplate.execute(sql);

	}

	// public static void main(String[] args) {
	// ComboPooledDataSource ds = (ComboPooledDataSource) new
	// Pub().getBeanCtx().getBean("dataSource");
	// System.out.println("ds:"+ds.toString()+"\n");
	//// ComboPooledDataSource ds2 = (ComboPooledDataSource) new
	// Pub().getBeanCtx().getBean(DataSource.class);
	//// System.out.println("ds2:"+ds2.toString());
	//
	// try {
	// conn = ds.getConnection();
	//
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	public static void main(String[] args) {
		/*JdbcTemplate jdbcTemplate = (JdbcTemplate) Pub.getBeanCtx().getBean("jdbcTemplate");
		// String sql = " update temp_spring set e = ? where aac003 like '%?%'
		// ";
		String sql = " update temp_spring set e = ? where aac003 = '王文志' ";
		sql = " delete from  temp_spring  where aac003 = '陈杰aaa' ";
		int num = jdbcTemplate.update(sql);
		System.out.println("num:"+num);*/
		
		Pub.getBeanCtx();
	}
}
