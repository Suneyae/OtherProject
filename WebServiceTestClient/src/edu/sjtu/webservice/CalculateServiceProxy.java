package edu.sjtu.webservice;

public class CalculateServiceProxy implements edu.sjtu.webservice.CalculateService {
  private String _endpoint = null;
  private edu.sjtu.webservice.CalculateService calculateService = null;
  
  public CalculateServiceProxy() {
    _initCalculateServiceProxy();
  }
  
  public CalculateServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initCalculateServiceProxy();
  }
  
  private void _initCalculateServiceProxy() {
    try {
      calculateService = (new edu.sjtu.webservice.CalculateServiceServiceLocator()).getCalculateService();
      if (calculateService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)calculateService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)calculateService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (calculateService != null)
      ((javax.xml.rpc.Stub)calculateService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public edu.sjtu.webservice.CalculateService getCalculateService() {
    if (calculateService == null)
      _initCalculateServiceProxy();
    return calculateService;
  }
  
  public float divide(float x, float y) throws java.rmi.RemoteException{
    if (calculateService == null)
      _initCalculateServiceProxy();
    return calculateService.divide(x, y);
  }
  
  public float multiply(float x, float y) throws java.rmi.RemoteException{
    if (calculateService == null)
      _initCalculateServiceProxy();
    return calculateService.multiply(x, y);
  }
  
  public float minus(float x, float y) throws java.rmi.RemoteException{
    if (calculateService == null)
      _initCalculateServiceProxy();
    return calculateService.minus(x, y);
  }
  
  public float plus(float x, float y) throws java.rmi.RemoteException{
    if (calculateService == null)
      _initCalculateServiceProxy();
    return calculateService.plus(x, y);
  }
  
  
}