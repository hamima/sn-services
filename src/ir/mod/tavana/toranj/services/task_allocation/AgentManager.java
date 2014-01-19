package ir.mod.tavana.toranj.services.task_allocation;

import ir.mod.tavana.toranj.services.box.BoxAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.Vector;


public class AgentManager {
	
	private Vector<MyAgent> agents;
	private static ir.mod.tavana.toranj.services.box.BoxAgent bAgent = new BoxAgent();
	private TaskAllocationAgent myAgent;
	private Vector<AID> busyAgents;
	
	public AgentManager() {
			agents = new Vector<MyAgent>();
	}
	
	public void resetAgents(Agent myAgent) {
		this.myAgent = (TaskAllocationAgent) myAgent;
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		// I've assumed that the service is on the search radars. This line might have to change
		sd.setType("BoxAgent");
		template.addServices(sd);
		MyAgent.reset();
		agents.clear();		
		busyAgents.clear();

		try {
			DFAgentDescription[] result = DFService.search(myAgent, template);
			for (int i = 0; i < result.length; ++i) {
				MyAgent tmp = new MyAgent(result[i].getName());
				agents.add(tmp);
				tmp = new MyAgent(result[i].getName());
				agents.add(tmp);
			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

	public /*static*/ int getPrice(AID agent, Task t, Agent tAAgent) {
		//we suppose that t is a leaf in the tree.
		//a message has to be sent to agent, asking for its price for task t
		//then the price should be returned
		int p = ((TaskAllocationAgent) myAgent).getPrice(agent,t);
		//int p = (int)bAgnet.getPrice(t.getTheString(), 0, 0);
		//String s = t.getTheString();
		return p;
	}

	public static Task breakTask(AID agent,Task t) {
		//this function is optional
		//t, as the root of task tree, should be given to an agent
		//the agent can break the task rooted in t in another way
		//root of a new task tree should be taken from the agent
		return t;
	}

	public static void giveTask(AID agent,Task t) {
		//the "busy" flag of agent should become true.
		//task t should be assigned to agent
		//the "busy" flag should change into false once again after the task is finished (this should be done internally in the agent)
	}

	private boolean isAgentBusy(AID agent) {
		//the busy flag of the agent should be returned
		return false;
	}

	public Vector<MyAgent> getFreeAgents() {
		Vector<MyAgent> d = new Vector<MyAgent>();
		for (MyAgent a : agents) {
			if (!isAgentBusy(a.getRealAgent())) d.add(a);
		}
		return d;
	}

	public void assign(Task t, Agent tAAgent) {
		if (agents.isEmpty()) {
			System.out.println("No Free Agent Exists");
		} else
			agents.get(0).assign(t,tAAgent);
	}
}