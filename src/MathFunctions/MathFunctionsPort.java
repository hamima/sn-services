/**
 * MathFunctionsPort.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package MathFunctions;

public interface MathFunctionsPort extends java.rmi.Remote {
    public java.lang.String convertDate(java.util.Calendar date) throws java.rmi.RemoteException;
    public float[] getComponents(MathFunctions.Complex complex) throws java.rmi.RemoteException;
    public void printComplex(MathFunctions.Complex complex) throws java.rmi.RemoteException;
    public MathFunctions.Complex sumComplex(MathFunctions.Complex firstComplexElement, MathFunctions.Complex secondComplexElement) throws java.rmi.RemoteException;
    public MathFunctions.Complex getRandom() throws java.rmi.RemoteException;
    public float sum(float firstElement, java.lang.Float secondElement) throws java.rmi.RemoteException;
    public MathFunctions.AgentInfo getAgentInfo() throws java.rmi.RemoteException;
    public float diff(float firstElement, float secondElement) throws java.rmi.RemoteException;
    public void printTime() throws java.rmi.RemoteException;
    public float multiplication(float[] numbers) throws java.rmi.RemoteException;
    public float abs(MathFunctions.Complex complex) throws java.rmi.RemoteException;
}
