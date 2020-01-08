package huarong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ui.AbstractUITag;

import com.opensymphony.xwork2.util.ValueStack;

public class MMTag extends AbstractUITag {
	static {
		System.out.println("TAG:static{........}  ");
	}
	private static final long serialVersionUID = 1L;
	private String message;
	private boolean flag;
	@Override
	public Component getBean(ValueStack arg0, HttpServletRequest arg1,
			HttpServletResponse arg2) {
		// TODO Auto-generated method stub
		//第二步，获取bean，
		System.out.println("TAG:.....11111.....");
		return new MM(arg0, arg1, arg2);
	}

	@Override
	protected void populateParams() {
		// TODO Auto-generated method stub
		super.populateParams();
		//第三步，
		System.out.println("TAG:.......22222.......");

		MM mm = (MM) component;
		mm.setMessage(message);
	}
	public void setMessage(String message) {
		// 第一步 ，实例化MMTag，然后调用setMessage方法进行赋值
		this.message = message;
		System.out.println("TAG:......333333......");
	}

}
