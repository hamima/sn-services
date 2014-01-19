/**
 * MediatorFunctionsPort.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ir.mod.tavana.toranj.services.mediator.exmples.client;

public interface MediatorFunctionsPort extends java.rmi.Remote {
    public java.lang.String register_Profile(ir.mod.tavana.toranj.services.mediator.exmples.client.Profile profile) throws java.rmi.RemoteException;
    public java.lang.String register_String_Integer_String_String(java.lang.String name, int age, java.lang.String type, java.lang.String quest) throws java.rmi.RemoteException;
    public java.lang.String deregister(java.lang.String aid) throws java.rmi.RemoteException;
}
