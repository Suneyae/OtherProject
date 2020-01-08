package com.zrar.bizcomm.service.webservice;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javax.xml.namespace.QName;
import org.apache.axis.AxisFault;
import org.apache.axis.NoEndPointException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Stub;
import org.apache.axis.constants.Style;
import org.apache.axis.constants.Use;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;
import org.apache.axis.encoding.DeserializerFactory;
import org.apache.axis.encoding.ser.ArrayDeserializerFactory;
import org.apache.axis.encoding.ser.ArraySerializerFactory;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;
import org.apache.axis.encoding.ser.EnumDeserializerFactory;
import org.apache.axis.encoding.ser.EnumSerializerFactory;
import org.apache.axis.encoding.ser.SimpleDeserializerFactory;
import org.apache.axis.encoding.ser.SimpleListDeserializerFactory;
import org.apache.axis.encoding.ser.SimpleListSerializerFactory;
import org.apache.axis.encoding.ser.SimpleSerializerFactory;
import org.apache.axis.soap.SOAPConstants;
import org.apache.axis.utils.JavaUtils;

public class MsgServiceServiceSoapBindingStub extends Stub implements IMsgService {
	private Vector cachedSerClasses = new Vector();
	private Vector cachedSerQNames = new Vector();
	private Vector cachedSerFactories = new Vector();
	private Vector cachedDeserFactories = new Vector();

	static OperationDesc[] _operations = new OperationDesc[1];

	private static void _initOperationDesc1() {
		OperationDesc oper = new OperationDesc();
		oper.setName("getResponse");
		ParameterDesc param = new ParameterDesc(new QName("", "arg0"), (byte) 1,
				new QName("http://webservice.service.bizcomm.zrar.com/", "requestContext"), RequestContext.class, false,
				false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://webservice.service.bizcomm.zrar.com/", "msgResponse"));
		oper.setReturnClass(MsgResponse.class);
		oper.setReturnQName(new QName("", "return"));
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[0] = oper;
	}

	public MsgServiceServiceSoapBindingStub() throws AxisFault {
		this(null);
	}

	public MsgServiceServiceSoapBindingStub(URL endpointURL, javax.xml.rpc.Service service) throws AxisFault {
		this(service);
		this.cachedEndpoint = endpointURL;
	}

	@SuppressWarnings("all")
	public MsgServiceServiceSoapBindingStub(javax.xml.rpc.Service service) throws AxisFault {
		if (service == null)
			this.service = new org.apache.axis.client.Service();
		else {
			this.service = service;
		}
		((org.apache.axis.client.Service) this.service).setTypeMappingVersion("1.2");

		Class beansf = BeanSerializerFactory.class;
		Class beandf = BeanDeserializerFactory.class;
		Class enumsf = EnumSerializerFactory.class;
		Class enumdf = EnumDeserializerFactory.class;
		Class arraysf = ArraySerializerFactory.class;
		Class arraydf = ArrayDeserializerFactory.class;
		Class simplesf = SimpleSerializerFactory.class;
		Class simpledf = SimpleDeserializerFactory.class;
		Class simplelistsf = SimpleListSerializerFactory.class;
		Class simplelistdf = SimpleListDeserializerFactory.class;
		QName qName = new QName("http://webservice.service.bizcomm.zrar.com/", ">>requestContext>msgMap>entry");
		this.cachedSerQNames.add(qName);
		Class cls = RequestContextMsgMapEntry.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);

		qName = new QName("http://webservice.service.bizcomm.zrar.com/", ">requestContext>msgMap");
		this.cachedSerQNames.add(qName);
		cls = com.zrar.bizcomm.service.webservice.RequestContextMsgMapEntry.class;
		this.cachedSerClasses.add(cls);
		qName = new QName("http://webservice.service.bizcomm.zrar.com/", ">>requestContext>msgMap>entry");
		QName qName2 = new QName("", "entry");
		this.cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		this.cachedDeserFactories.add(new ArrayDeserializerFactory());

		qName = new QName("http://webservice.service.bizcomm.zrar.com/", "commonResponse");
		this.cachedSerQNames.add(qName);
		cls = CommonResponse.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);

		qName = new QName("http://webservice.service.bizcomm.zrar.com/", "getResponse");
		this.cachedSerQNames.add(qName);
		cls = GetResponse.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);

		qName = new QName("http://webservice.service.bizcomm.zrar.com/", "getResponseResponse");
		this.cachedSerQNames.add(qName);
		cls = GetResponseResponse.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);

		qName = new QName("http://webservice.service.bizcomm.zrar.com/", "msgBean");
		this.cachedSerQNames.add(qName);
		cls = MsgBean.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);

		qName = new QName("http://webservice.service.bizcomm.zrar.com/", "msgResponse");
		this.cachedSerQNames.add(qName);
		cls = MsgResponse.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);

		qName = new QName("http://webservice.service.bizcomm.zrar.com/", "requestContext");
		this.cachedSerQNames.add(qName);
		cls = RequestContext.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
	}

	protected Call createCall() throws RemoteException {
		try {
			Call _call = super._createCall();
			if (this.maintainSessionSet) {
				_call.setMaintainSession(this.maintainSession);
			}
			if (this.cachedUsername != null) {
				_call.setUsername(this.cachedUsername);
			}
			if (this.cachedPassword != null) {
				_call.setPassword(this.cachedPassword);
			}
			if (this.cachedEndpoint != null) {
				_call.setTargetEndpointAddress(this.cachedEndpoint);
			}
			if (this.cachedTimeout != null) {
				_call.setTimeout(this.cachedTimeout);
			}
			if (this.cachedPortName != null) {
				_call.setPortName(this.cachedPortName);
			}
			Enumeration keys = this.cachedProperties.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				_call.setProperty(key, this.cachedProperties.get(key));
			}

			synchronized (this) {
				if (firstCall()) {
					_call.setEncodingStyle(null);
					for (int i = 0; i < this.cachedSerFactories.size(); i++) {
						Class cls = (Class) this.cachedSerClasses.get(i);

						QName qName = (QName) this.cachedSerQNames.get(i);

						Object x = this.cachedSerFactories.get(i);
						if ((x instanceof Class)) {
							Class sf = (Class) this.cachedSerFactories.get(i);

							Class df = (Class) this.cachedDeserFactories.get(i);

							_call.registerTypeMapping(cls, qName, sf, df, false);
						} else if ((x instanceof javax.xml.rpc.encoding.SerializerFactory)) {
							org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory) this.cachedSerFactories
									.get(i);

							DeserializerFactory df = (DeserializerFactory) this.cachedDeserFactories.get(i);

							_call.registerTypeMapping(cls, qName, sf, df, false);
						}
					}
				}
			}
			return _call;
		} catch (Throwable _t) {
			throw new AxisFault("Failure trying to get the Call object", _t);
		}
	}

	public MsgResponse getResponse(RequestContext arg0) throws RemoteException {
		if (this.cachedEndpoint == null) {
			throw new NoEndPointException();
		}
		Call _call = createCall();
		_call.setOperation(_operations[0]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setEncodingStyle(null);
		_call.setProperty("sendXsiTypes", Boolean.FALSE);
		_call.setProperty("sendMultiRefs", Boolean.FALSE);
		_call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
//		_call.setOperationName(new QName("http://webservice.service.bizcomm.zrar.com/", "getResponse"));
		_call.setOperationName(new QName("http://webservice.service.bizcomm/", "getResponse"));
		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[] { arg0 });
//			arg0.setChannelID("203");
			if ((_resp instanceof RemoteException)) {
				throw ((RemoteException) _resp);
			}

			extractAttachments(_call);
			try {
				return (MsgResponse) _resp;
			} catch (Exception _exception) {
				return (MsgResponse) JavaUtils.convert(_resp, MsgResponse.class);
			}
		} catch (AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	static {
		_initOperationDesc1();
	}
}
