/**
 * CalculateServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package edu.sjtu.webservice;

public class CalculateServiceServiceLocator extends org.apache.axis.client.Service implements edu.sjtu.webservice.CalculateServiceService {

    public CalculateServiceServiceLocator() {
    }


    public CalculateServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CalculateServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CalculateService
    private java.lang.String CalculateService_address = "http://localhost:8082/WebServiceTest/services/CalculateService";

    public java.lang.String getCalculateServiceAddress() {
        return CalculateService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CalculateServiceWSDDServiceName = "CalculateService";

    public java.lang.String getCalculateServiceWSDDServiceName() {
        return CalculateServiceWSDDServiceName;
    }

    public void setCalculateServiceWSDDServiceName(java.lang.String name) {
        CalculateServiceWSDDServiceName = name;
    }

    public edu.sjtu.webservice.CalculateService getCalculateService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CalculateService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCalculateService(endpoint);
    }

    public edu.sjtu.webservice.CalculateService getCalculateService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            edu.sjtu.webservice.CalculateServiceSoapBindingStub _stub = new edu.sjtu.webservice.CalculateServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getCalculateServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCalculateServiceEndpointAddress(java.lang.String address) {
        CalculateService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (edu.sjtu.webservice.CalculateService.class.isAssignableFrom(serviceEndpointInterface)) {
                edu.sjtu.webservice.CalculateServiceSoapBindingStub _stub = new edu.sjtu.webservice.CalculateServiceSoapBindingStub(new java.net.URL(CalculateService_address), this);
                _stub.setPortName(getCalculateServiceWSDDServiceName());
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
        if ("CalculateService".equals(inputPortName)) {
            return getCalculateService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://webservice.sjtu.edu", "CalculateServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://webservice.sjtu.edu", "CalculateService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CalculateService".equals(portName)) {
            setCalculateServiceEndpointAddress(address);
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
