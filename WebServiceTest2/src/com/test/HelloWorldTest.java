package com.test;

import java.rmi.RemoteException;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;

import com.util.UtilProperties;

public class HelloWorldTest {

	public String invokeRemoteFuc() {
		// 远程调用路径
//		String endpoint = "http://localhost:8082/WebServiceTest/services/HelloService";
		//webservice的访问地址，这里是封装成了一个工具方法，根据文件名以及键来获取写在properties文件里的访问地址
		String endpoint = UtilProperties.getValueByKeyViaProp("myendpointAddress", "ws.properties");
		//预定义失败的默认返回值
		String result = "call failed!";
		//步骤1 构建 org.apache.axis.client.Service 对象
		Service service = new Service();
		Call call;
		try {
			// 步骤2：通过org.apache.axis.client.Service对象创建一个Call,需要强转为 org.apache.axis.client.Call类型
			call = (Call) service.createCall();
			// 步骤3：设置目标地址，即需要访问的webservice地址
			call.setTargetEndpointAddress(endpoint);
			// 步骤4：设置调用的方法名
			call.setOperationName("sayHelloToPerson");

			// 步骤5： 设置参数名
			call.addParameter("name", // 参数名
					XMLType.XSD_STRING, // 参数类型:String
					ParameterMode.IN); // 参数模式：'IN' or 'OUT'

			// 步骤6：设置返回值类型
			call.setReturnType(XMLType.XSD_STRING); // 返回值类型：String
			String name = "WEIYONGLE";
			//步骤7 ：调用call.invoke(Object[] obj)方法
			result = (String) call.invoke(new Object[] { name });// 远程调用
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return result;
	}

	// 测试
	public static void main(String[] args) {
		HelloWorldTest test = new HelloWorldTest();
		String result = test.invokeRemoteFuc();
		System.out.println(result);
		String result2 = test.invokeRemoteFuc2("sayHello2","Japan");
//		String result2 = test.invokeRemoteFuc2("sayHello2","xxx");
		System.out.println("result2:"+result2);
		
	}
	
	/**
	 * 根据webservice接口地址，以及方法名，参数等调用webservice接口
	 * @param methodName 方法名
	 * @param paraName 参数
	 * @return 返回String类型的结果
	 */
	public String invokeRemoteFuc2(String methodName,String paraName) {
		// 远程调用路径
//		String endpoint = "http://localhost:8082/WebServiceTest/services/HelloService";
		//webservice的访问地址，这里是封装成了一个工具方法，根据文件名以及键来获取写在properties文件里的访问地址
		String endpoint = UtilProperties.getValueByKeyViaProp("myendpointAddress", "ws.properties");
		//预定义失败的默认返回值
		String result = "call failed!";
		//步骤1 构建 org.apache.axis.client.Service 对象
		Service service = new Service();
		Call call;
		try {
			// 步骤2：通过org.apache.axis.client.Service对象创建一个Call,需要强转为 org.apache.axis.client.Call类型
			call = (Call) service.createCall();
			// 步骤3：设置目标地址，即需要访问的webservice地址
			call.setTargetEndpointAddress(endpoint);
			// 步骤4：设置调用的方法名
			call.setOperationName(methodName);

			// 步骤5： 设置参数名
			call.addParameter("name", // 参数名
					XMLType.XSD_STRING, // 参数类型:String
					ParameterMode.IN); // 参数模式：'IN' or 'OUT'

			// 步骤6：设置返回值类型
			call.setReturnType(XMLType.XSD_STRING); // 返回值类型：String
			String name = paraName;
			//步骤7 ：调用call.invoke(Object[] obj)方法
			result = (String) call.invoke(new Object[] { name });// 远程调用
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return result;
	}

}