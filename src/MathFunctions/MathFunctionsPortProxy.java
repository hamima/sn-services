package MathFunctions;

public class MathFunctionsPortProxy implements MathFunctions.MathFunctionsPort {
  private String _endpoint = null;
  private MathFunctions.MathFunctionsPort mathFunctionsPort = null;
  
  public MathFunctionsPortProxy() {
    _initMathFunctionsPortProxy();
  }
  
  public MathFunctionsPortProxy(String endpoint) {
    _endpoint = endpoint;
    _initMathFunctionsPortProxy();
  }
  
  private void _initMathFunctionsPortProxy() {
    try {
      mathFunctionsPort = (new MathFunctions.MathFunctionsServiceLocator()).getMathFunctionsPort();
      if (mathFunctionsPort != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)mathFunctionsPort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)mathFunctionsPort)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (mathFunctionsPort != null)
      ((javax.xml.rpc.Stub)mathFunctionsPort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public MathFunctions.MathFunctionsPort getMathFunctionsPort() {
    if (mathFunctionsPort == null)
      _initMathFunctionsPortProxy();
    return mathFunctionsPort;
  }
  
  public java.lang.String convertDate(java.util.Calendar date) throws java.rmi.RemoteException{
    if (mathFunctionsPort == null)
      _initMathFunctionsPortProxy();
    return mathFunctionsPort.convertDate(date);
  }
  
  public float[] getComponents(MathFunctions.Complex complex) throws java.rmi.RemoteException{
    if (mathFunctionsPort == null)
      _initMathFunctionsPortProxy();
    return mathFunctionsPort.getComponents(complex);
  }
  
  public void printComplex(MathFunctions.Complex complex) throws java.rmi.RemoteException{
    if (mathFunctionsPort == null)
      _initMathFunctionsPortProxy();
    mathFunctionsPort.printComplex(complex);
  }
  
  public MathFunctions.Complex sumComplex(MathFunctions.Complex firstComplexElement, MathFunctions.Complex secondComplexElement) throws java.rmi.RemoteException{
    if (mathFunctionsPort == null)
      _initMathFunctionsPortProxy();
    return mathFunctionsPort.sumComplex(firstComplexElement, secondComplexElement);
  }
  
  public MathFunctions.Complex getRandom() throws java.rmi.RemoteException{
    if (mathFunctionsPort == null)
      _initMathFunctionsPortProxy();
    return mathFunctionsPort.getRandom();
  }
  
  public float sum(float firstElement, java.lang.Float secondElement) throws java.rmi.RemoteException{
    if (mathFunctionsPort == null)
      _initMathFunctionsPortProxy();
    return mathFunctionsPort.sum(firstElement, secondElement);
  }
  
  public MathFunctions.AgentInfo getAgentInfo() throws java.rmi.RemoteException{
    if (mathFunctionsPort == null)
      _initMathFunctionsPortProxy();
    return mathFunctionsPort.getAgentInfo();
  }
  
  public float diff(float firstElement, float secondElement) throws java.rmi.RemoteException{
    if (mathFunctionsPort == null)
      _initMathFunctionsPortProxy();
    return mathFunctionsPort.diff(firstElement, secondElement);
  }
  
  public void printTime() throws java.rmi.RemoteException{
    if (mathFunctionsPort == null)
      _initMathFunctionsPortProxy();
    mathFunctionsPort.printTime();
  }
  
  public float multiplication(float[] numbers) throws java.rmi.RemoteException{
    if (mathFunctionsPort == null)
      _initMathFunctionsPortProxy();
    return mathFunctionsPort.multiplication(numbers);
  }
  
  public float abs(MathFunctions.Complex complex) throws java.rmi.RemoteException{
    if (mathFunctionsPort == null)
      _initMathFunctionsPortProxy();
    return mathFunctionsPort.abs(complex);
  }
  
  
}