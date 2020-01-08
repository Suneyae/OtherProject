package com.wyl.alltest;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class TemplateTest {
	static String sql = "insert into AE01 (BAE001, AAB001, CAB003, "
			+ "AAB999, AAB004, AAB021, CAZ011, CAZ012, AAA017, AAE007, "
			+ "AAE004, AAE005, AAE006, AAE159, CAE005, CAE006, AAZ099, "
			+ "AAB077, AAB451, BCE326, AAE013, AAB014, AAB013, AAB015, "
			+ "CAB008, CAB009, CAB010, CAE380, CAE381, CAE382, CAE383, "
			+ "CAC005, CAZ075, CAB161, CAE223, CAB053, CAB054, CAE883, "
			+ "CAE884, YHHH, YHZH, ZHMC)values ('511402', '511500035265', "
			+ "'01', '02347330', '眉山市翔龙印务有限公司', '40', "
			+ "'511500035265', null, '1', null, null, null, "
			+ "'眉山市东坡区崇礼镇上东街50号', null, null, null, "
			+ "null, null, null, '1', null, '511121196408044193', "
			+ "'干志勇', null, '028-38601301', '雷蕾', null, null, "
			+ "'颜瑗岑', null, null, null, null, null, '1', "
			+ "'15082350455', null, null, null, null, null, null)";
	public TemplateTest() {
		
	}
	/**
	 * 不知道为什么出现 QueryTimeoutException 异常，应该是jar包的问题。
	 * @param args
	 */
	public static void main(String[] args) {
		String sql = "insert into spring_wyl_test (corpanme) values (?)";
		String sql2 = "insert into spring_wyl_test (corpanme) values ('xxxx')";
		//jdbcTemplate
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans-auto2.xml");
		JdbcTemplate template = (JdbcTemplate)ctx.getBean(org.springframework.jdbc.core.JdbcTemplate.class);
//		template.execute()
		template.update(sql, "测试用"+getDate());
//		template.execute(sql2);
	}
	
	@Test
	public static String getDate(){
		String FORMAT_STR = "yyyy-MM-dd HH:mm:ss am";
		FORMAT_STR = "yyyyMMddHHmmss";
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STR);
		String time = sdf.format(date);
		System.out.println(time);
		return time;
	}
}
