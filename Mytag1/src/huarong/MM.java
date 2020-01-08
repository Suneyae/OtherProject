package huarong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.UIBean;

import com.opensymphony.xwork2.util.ValueStack;
public class MM extends UIBean {
	static {
		System.out.println("Bean:static{........}  ");
	}

	private String message;

	private boolean flag;

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public MM(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
		// TODO Auto-generated constructor stub
	}
	public void setMessage(String message) {
		this.message = message;
		System.out.println("Bean:...11111.....");
	}

	@Override
	protected String getDefaultTemplate() {
		// TODO Auto-generated method stub
		System.out.println("Bean:...22222.....");
		return "wyl";

	}

	@Override
	protected void evaluateExtraParams() {
		// TODO Auto-generated method stub
		super.evaluateExtraParams();
		System.out.println("Bean:...33333.....");
		message += "卫永乐";
		if (message != null) {
			addParameter("message", findString(message));
		}
	}
}
