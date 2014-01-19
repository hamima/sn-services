package ir.mod.tavana.toranj.agents.physical;

import jade.core.Agent;

public class AnyLogicAgent extends Agent {

	protected void setup() {
    	// Register the user service in the yellow pages
    	jade.domain.FIPAAgentManagement.DFAgentDescription dfd = new jade.domain.FIPAAgentManagement.DFAgentDescription();
		dfd.setName(getAID());
		jade.domain.FIPAAgentManagement.ServiceDescription sd = new jade.domain.FIPAAgentManagement.ServiceDescription();
		sd.setType("AnyLogic");
		sd.setName("AnyLogic-agents");
		dfd.addServices(sd);
		try {
			jade.domain.DFService.register(this, dfd);
		} catch (jade.domain.FIPAException fe) {
			fe.printStackTrace();
		}
    }
    
    public void sendMsg(String msg, String sender, String receiver)
	{
    	jade.lang.acl.ACLMessage request = new jade.lang.acl.ACLMessage(jade.lang.acl.ACLMessage.REQUEST);
		request.setContent(msg);
		request.addReceiver(new jade.core.AID(receiver, true));
		send(request);
	}
}
