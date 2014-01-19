/**
 * MediatorFunctionsServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ir.mod.tavana.toranj.services.mediator.exmples.client;

public class MediatorFunctionsServiceLocator extends org.apache.axis.client.Service implements ir.mod.tavana.toranj.services.mediator.exmples.client.MediatorFunctionsService {

    public MediatorFunctionsServiceLocator() {
    }


    public MediatorFunctionsServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public MediatorFunctionsServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for MediatorFunctionsPort
    private java.lang.String MediatorFunctionsPort_address = "http://localhost:8080/wsig-examples/ws/MediatorFunctions";

    public java.lang.String getMediatorFunctionsPortAddress() {
        return MediatorFunctionsPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String MediatorFunctionsPortWSDDServiceName = "MediatorFunctionsPort";

    public java.lang.String getMediatorFunctionsPortWSDDServiceName() {
        return MediatorFunctionsPortWSDDServiceName;
    }

    public void setMediatorFunctionsPortWSDDServiceName(java.lang.String name) {
        MediatorFunctionsPortWSDDServiceName = name;
    }

    public ir.mod.tavana.toranj.services.mediator.exmples.client.MediatorFunctionsPort getMediatorFunctionsPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(MediatorFunctionsPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getMediatorFunctionsPort(endpoint);
    }

    public ir.mod.tavana.toranj.services.mediator.exmples.client.MediatorFunctionsPort getMediatorFunctionsPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            ir.mod.tavana.toranj.services.mediator.exmples.client.MediatorFunctionsBindingStub _stub = new ir.mod.tavana.toranj.services.mediator.exmples.client.MediatorFunctionsBindingStub(portAddress, this);
            _stub.setPortName(getMediatorFunctionsPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setMediatorFunctionsPortEndpointAddress(java.lang.String address) {
        MediatorFunctionsPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (ir.mod.tavana.toranj.services.mediator.exmples.client.MediatorFunctionsPort.class.isAssignableFrom(serviceEndpointInterface)) {
                ir.mod.tavana.toranj.services.mediator.exmples.client.MediatorFunctionsBindingStub _stub = new ir.mod.tavana.toranj.services.mediator.exmples.client.MediatorFunctionsBindingStub(new java.net.URL(MediatorFunctionsPort_address), this);
                _stub.setPortName(getMediatorFunctionsPortWSDDServiceName());
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
        if ("MediatorFunctionsPort".equals(inputPortName)) {
            return getMediatorFunctionsPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:MediatorFunctions", "MediatorFunctionsService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:MediatorFunctions", "MediatorFunctionsPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("MediatorFunctionsPort".equals(portName)) {
            setMediatorFunctionsPortEndpointAddress(address);
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
