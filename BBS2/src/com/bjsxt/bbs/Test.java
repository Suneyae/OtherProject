package com.bjsxt.bbs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
@SuppressWarnings("all")
public class Test {
	public static void main(String[] args) throws SQLException {
		// request.setCharacterEncoding("GBK");
		// String action = request.getParameter("action");
		String action = "post";

		if (action != null && action.trim().equals("post")) {
			String title = "xxxx";
			System.out.println(title);
			String cont = "rwerwde";
			System.out.println(cont);
			Connection conn = DB.getConn();

			boolean autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);

			int rootId = -1;

			// String sql = "insert into article values (null, ?, ?, ?, ?,
			// now(), ?)";
			String sql = "insert into article values (bxgx_seq_aaz611.nextval, ?, ?, ?, ?, to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd'), ?)";
			// out.println("sql:"+sql);
			System.out.println("sql:" + sql);
			PreparedStatement pstmt = DB.prepareStmt(conn, sql, Statement.RETURN_GENERATED_KEYS);
			try {
				pstmt.setInt(1, 0);
				pstmt.setInt(2, rootId);
				pstmt.setString(3, title);
				pstmt.setString(4, cont);
				pstmt.setInt(5, 0);
				pstmt.executeUpdate();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			ResultSet rsKey = pstmt.getGeneratedKeys();
			Boolean flag = rsKey.next();
			System.out.println("flag:"+flag);
//			rootId = rsKey.getInt(1);
			rootId = 2;
			Statement stmt = DB.createStmt(conn);
			stmt.executeUpdate("update article set rootid = " + rootId + " where id = " + rootId);

			conn.commit();
			conn.setAutoCommit(autoCommit);
			DB.close(pstmt);
			DB.close(stmt);
			DB.close(conn);

			// response.sendRedirect("article.jsp");

		}

	}
}
