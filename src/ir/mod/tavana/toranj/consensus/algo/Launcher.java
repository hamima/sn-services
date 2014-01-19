/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ir.mod.tavana.toranj.consensus.algo;
import jade.core.*;
import jade.core.Runtime;
import jade.wrapper.*;
/**
 *
 * @author Admin
 */
public abstract class Launcher {

	public static AgentController createInitialAgent(String name , String className , Object ref[]) {
		Profile p = new ProfileImpl();
		Runtime rt = Runtime.instance();
		AgentController agentC = null;

		try
		{
			ContainerController cc = rt.createMainContainer(p);
			AgentController rma = cc.createNewAgent("gui", "jade.tools.rma.rma", ref);
			rma.start();
			if(name.length() > 0 &&
					className.length() > 0)
			{
				agentC = cc.createNewAgent(name, className, ref);
				agentC.start();
			}
		}
		catch (Exception e)
		{
			
		}
		return agentC;
	}
        public static AgentController createOtherAgent(String name , String className , Object ref[])
	{
		Profile p = new ProfileImpl();
		Runtime rt = Runtime.instance();
		AgentController agentC = null;

		try
		{
			ContainerController cc = rt.createAgentContainer(p);
			try
			{
				if(name.length() > 0 &&
						className.length() > 0)
				{
					agentC = cc.createNewAgent(name, className, ref);
					agentC.start();
				}
			}
			catch(Exception ex)
			{
				System.out.println(ex);
			}
		}
		catch (Exception e)
		{

		}
		return agentC;
	}
	public static void main(String[] args)
	{
//		Object[] ref1 = {"oil","ready_table"};
//		Object[] ref2 = {"furniture"};
//		Object[] ref3 = {"chemical"};
//		Object[] ref4 = {"woodworks"};
//		Object[] ref5 = {"woodworks"};
//		createInitialAgent("Buyer","agents.CoreAgent",ref1);
//		createOtherAgent("Supplier_1","agents.SupplierAgent",ref2);
//		createOtherAgent("Supplier_2","agents.SupplierAgent",ref3);
//		createOtherAgent("Supplier_3","agents.SupplierAgent",ref4);
//		createOtherAgent("Supplier_4","agents.SupplierAgent",ref5);
	}
}
