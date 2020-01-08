package org.znfancy.sms;

import com.zrar.bizcomm.service.webservice.IMsgService;
import com.zrar.bizcomm.service.webservice.MsgBean;
import com.zrar.bizcomm.service.webservice.MsgResponse;
import com.zrar.bizcomm.service.webservice.MsgServiceServiceLocator;
import com.zrar.bizcomm.service.webservice.RequestContext;
import com.zrar.bizcomm.service.webservice.RequestContextMsgMapEntry;
import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;

public class SmsGateway2 {
	private static MsgServiceServiceLocator lc = new MsgServiceServiceLocator();
	private static String address = "http://71.12.50.14:8002/SmsService";

	public static void send(String sj, String sms) {
		lc.setMsgServicePortEndpointAddress(address);
		try {
			IMsgService s = lc.getMsgServicePort();
			RequestContext req = new RequestContext();
			req.setChannelID("202");

			MsgBean msgBean = new MsgBean();

			msgBean.setSubsystem("odps");
			msgBean.setMsgcontent(sms);

			msgBean.setPriority(Integer.valueOf(9));
			req.setMsgBean(msgBean);
			RequestContextMsgMapEntry mp = new RequestContextMsgMapEntry();
			mp.setKey("1");
			mp.setValue(sj);
			RequestContextMsgMapEntry[] mspMap = { mp };
			req.setMsgMap(mspMap);
			MsgResponse res = s.getResponse(req);
			if (!"2021".equals(res.getReturnCode()))
				;
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
