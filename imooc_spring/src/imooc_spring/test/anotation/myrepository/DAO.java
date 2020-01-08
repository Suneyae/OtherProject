package imooc_spring.test.anotation.myrepository;

import org.springframework.stereotype.Repository;

/**
 * 指定id,默认为dAO,即类名首字母小写，如果指定了名称那么只能ctx.getBean(指定名称)来获取bean
 * 这个例子里就只能通过ctx.getBean("wyldao)来获取DAO 的实例了;
 * 
 * @author Wei
 */
@Repository("wyldao")
public class DAO {
	/**
	 * 返回x和y的乘积
	 * 
	 * @param x
	 * @param y
	 * @return x*y
	 */
	public int multi(int x, int y) {
		return x * y;
	}
}
