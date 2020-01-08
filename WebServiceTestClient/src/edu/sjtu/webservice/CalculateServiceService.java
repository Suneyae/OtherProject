/**
 * CalculateServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package edu.sjtu.webservice;

public interface CalculateServiceService extends javax.xml.rpc.Service {
    public java.lang.String getCalculateServiceAddress();

    public edu.sjtu.webservice.CalculateService getCalculateService() throws javax.xml.rpc.ServiceException;

    public edu.sjtu.webservice.CalculateService getCalculateService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
