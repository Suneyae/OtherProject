package test.testdemo;

import org.springframework.jdbc.core.JdbcTemplate;

import com.util.Pub;

public class Test4 {

	public static void main(String[] args) {
		String sql = " insert into temp_spring (AAC003, AAC002, D, E, F, G, H, I, J, K)"
				+ "values ('Tom', '51253319700126xxxx', '大邑县晋原镇堰北路1151号', '成都市', '5', '5', '4', '大邑县人民医院', '大邑中医院', '大邑县第二人民医院')";
		// 用来接收replace后的sql语句
		String sql2;
		JdbcTemplate jdbcTemplate = (JdbcTemplate) Pub.getBeanCtx().getBean("jdbcTemplate");
		for (int i = 1; i < 10; i++) {
			// 接收sql语句，否则如果sql=sql.replace("Tom","Tom"+i);那么发现插入的都是同样的数据
			sql2 = sql.replace("Tom", "Tom" + i);
			jdbcTemplate.execute(sql2);
		}
	}
}
