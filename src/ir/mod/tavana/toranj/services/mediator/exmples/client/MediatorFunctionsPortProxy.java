package ir.mod.tavana.toranj.services.mediator.exmples.client;

public class MediatorFunctionsPortProxy implements ir.mod.tavana.toranj.services.mediator.exmples.client.MediatorFunctionsPort {
  private String _endpoint = null;
  private ir.mod.tavana.toranj.services.mediator.exmples.client.MediatorFunctionsPort mediatorFunctionsPort = null;
  
  public MediatorFunctionsPortProxy() {
    _initMediatorFunctionsPortProxy();
  }
  
  public MediatorFunctionsPortProxy(String endpoint) {
    _endpoint = endpoint;
    _initMediatorFunctionsPortProxy();
  }
  
  private void _initMediatorFunctionsPortProxy() {
    try {
      mediatorFunctionsPort = (new ir.mod.tavana.toranj.services.mediator.exmples.client.MediatorFunctionsServiceLocator()).getMediatorFunctionsPort();
      if (mediatorFunctionsPort != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)mediatorFunctionsPort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)mediatorFunctionsPort)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (mediatorFunctionsPort != null)
      ((javax.xml.rpc.Stub)mediatorFunctionsPort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public ir.mod.tavana.toranj.services.mediator.exmples.client.MediatorFunctionsPort getMediatorFunctionsPort() {
    if (mediatorFunctionsPort == null)
      _initMediatorFunctionsPortProxy();
    return mediatorFunctionsPort;
  }
  
  public java.lang.String register_Profile(ir.mod.tavana.toranj.services.mediator.exmples.client.Profile profile) throws java.rmi.RemoteException{
    if (mediatorFunctionsPort == null)
      _initMediatorFunctionsPortProxy();
    return mediatorFunctionsPort.register_Profile(profile);
  }
  
  public java.lang.String register_String_Integer_String_String(java.lang.String name, int age, java.lang.String type, java.lang.String quest) throws java.rmi.RemoteException{
    if (mediatorFunctionsPort == null)
      _initMediatorFunctionsPortProxy();
    return mediatorFunctionsPort.register_String_Integer_String_String(name, age, type, quest);
  }
  
  public java.lang.String deregister(java.lang.String aid) throws java.rmi.RemoteException{
    if (mediatorFunctionsPort == null)
      _initMediatorFunctionsPortProxy();
    return mediatorFunctionsPort.deregister(aid);
  }
  
  
}