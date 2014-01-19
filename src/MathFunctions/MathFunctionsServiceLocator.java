/**
 * MathFunctionsServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package MathFunctions;

public class MathFunctionsServiceLocator extends org.apache.axis.client.Service implements MathFunctions.MathFunctionsService {

    public MathFunctionsServiceLocator() {
    }


    public MathFunctionsServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public MathFunctionsServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for MathFunctionsPort
    private java.lang.String MathFunctionsPort_address = "http://localhost:8080/wsig-examples/ws/MathFunctions";

    public java.lang.String getMathFunctionsPortAddress() {
        return MathFunctionsPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String MathFunctionsPortWSDDServiceName = "MathFunctionsPort";

    public java.lang.String getMathFunctionsPortWSDDServiceName() {
        return MathFunctionsPortWSDDServiceName;
    }

    public void setMathFunctionsPortWSDDServiceName(java.lang.String name) {
        MathFunctionsPortWSDDServiceName = name;
    }

    public MathFunctions.MathFunctionsPort getMathFunctionsPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(MathFunctionsPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getMathFunctionsPort(endpoint);
    }

    public MathFunctions.MathFunctionsPort getMathFunctionsPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            MathFunctions.MathFunctionsBindingStub _stub = new MathFunctions.MathFunctionsBindingStub(portAddress, this);
            _stub.setPortName(getMathFunctionsPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setMathFunctionsPortEndpointAddress(java.lang.String address) {
        MathFunctionsPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (MathFunctions.MathFunctionsPort.class.isAssignableFrom(serviceEndpointInterface)) {
                MathFunctions.MathFunctionsBindingStub _stub = new MathFunctions.MathFunctionsBindingStub(new java.net.URL(MathFunctionsPort_address), this);
                _stub.setPortName(getMathFunctionsPortWSDDServiceName());
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
        if ("MathFunctionsPort".equals(inputPortName)) {
            return getMathFunctionsPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:MathFunctions", "MathFunctionsService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:MathFunctions", "MathFunctionsPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("MathFunctionsPort".equals(portName)) {
            setMathFunctionsPortEndpointAddress(address);
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
