package huarong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.UIBean;

import com.opensymphony.xwork2.util.ValueStack;

public class WylBean extends UIBean{

	public WylBean(ValueStack stack, HttpServletRequest request,
			HttpServletResponse response) {
		super(stack, request, response);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getDefaultTemplate() {
		// TODO Auto-generated method stub
		return "wylgt";
	}

}
