/**
 * HelloServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sinosoft.webservice;

public class HelloServiceServiceLocator extends org.apache.axis.client.Service implements com.sinosoft.webservice.HelloServiceService {

    public HelloServiceServiceLocator() {
    }


    public HelloServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public HelloServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for HelloService
    private java.lang.String HelloService_address = "http://localhost:8082/WebServiceTest/services/HelloService";

    public java.lang.String getHelloServiceAddress() {
        return HelloService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String HelloServiceWSDDServiceName = "HelloService";

    public java.lang.String getHelloServiceWSDDServiceName() {
        return HelloServiceWSDDServiceName;
    }

    public void setHelloServiceWSDDServiceName(java.lang.String name) {
        HelloServiceWSDDServiceName = name;
    }

    public com.sinosoft.webservice.HelloService getHelloService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(HelloService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getHelloService(endpoint);
    }

    public com.sinosoft.webservice.HelloService getHelloService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.sinosoft.webservice.HelloServiceSoapBindingStub _stub = new com.sinosoft.webservice.HelloServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getHelloServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setHelloServiceEndpointAddress(java.lang.String address) {
        HelloService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.sinosoft.webservice.HelloService.class.isAssignableFrom(serviceEndpointInterface)) {
                com.sinosoft.webservice.HelloServiceSoapBindingStub _stub = new com.sinosoft.webservice.HelloServiceSoapBindingStub(new java.net.URL(HelloService_address), this);
                _stub.setPortName(getHelloServiceWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("HelloService".equals(inputPortName)) {
            return getHelloService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://webservice.sinosoft.com", "HelloServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://webservice.sinosoft.com", "HelloService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("HelloService".equals(portName)) {
            setHelloServiceEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
