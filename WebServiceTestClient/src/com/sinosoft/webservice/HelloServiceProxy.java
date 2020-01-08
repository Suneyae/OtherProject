package com.sinosoft.webservice;

public class HelloServiceProxy implements com.sinosoft.webservice.HelloService {
  private String _endpoint = null;
  private com.sinosoft.webservice.HelloService helloService = null;
  
  public HelloServiceProxy() {
    _initHelloServiceProxy();
  }
  
  public HelloServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initHelloServiceProxy();
  }
  
  private void _initHelloServiceProxy() {
    try {
      helloService = (new com.sinosoft.webservice.HelloServiceServiceLocator()).getHelloService();
      if (helloService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)helloService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)helloService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (helloService != null)
      ((javax.xml.rpc.Stub)helloService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.sinosoft.webservice.HelloService getHelloService() {
    if (helloService == null)
      _initHelloServiceProxy();
    return helloService;
  }
  
  public java.lang.String sayHello() throws java.rmi.RemoteException{
    if (helloService == null)
      _initHelloServiceProxy();
    return helloService.sayHello();
  }
  
  public java.lang.String sayHelloToPerson(java.lang.String name) throws java.rmi.RemoteException{
    if (helloService == null)
      _initHelloServiceProxy();
    return helloService.sayHelloToPerson(name);
  }
  
  
}