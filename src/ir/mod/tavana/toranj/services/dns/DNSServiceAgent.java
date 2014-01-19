package ir.mod.tavana.toranj.services.dns;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;

public class DNSServiceAgent extends Agent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String SERVICE_NAME = "Dns";
	public static final String SERVICE_TYPE = "Dns_Agent";
	
	public static final String ACTUAL_RECEIVERS = "Actual_Receivers";
	public static final String CONVERSATION_ID_DNS_REPLY = "dns_reply";
		
	protected void setup(){

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());

		ServiceDescription sd = new ServiceDescription();
		sd.setType(SERVICE_TYPE);
		sd.setName(SERVICE_NAME);
		sd.setOwnership("BroadcastingOwner");
		dfd.addServices(sd);

		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		this.addBehaviour(new DNSRequestHandler(this));
	}
	
	@SuppressWarnings("serial")
	private class DNSRequestHandler extends CyclicBehaviour {

		public DNSRequestHandler(Agent a) {
			super(a);
		}

		@Override
		public void action() {
			// TODO Auto-generated method stub
			ACLMessage msg = (ACLMessage) myAgent.receive();

			if (msg != null){
				String responseOfQuery = "";
				if(msg.getContent() != null && !msg.getContent().equals("")){
					/*
					 *
					 In this part the execution of the predefined query is performed ... .
					 Not the duty of this work and is considered as a dummy agent :)
					 */
					
					ACLMessage replyQuery = msg.createReply();
					List<AID> newAidList = new ArrayList<AID>();
					for(AID aid: newAidList){
						responseOfQuery += aid.getLocalName();
						responseOfQuery += ",";
					}
					responseOfQuery = responseOfQuery.substring(0, responseOfQuery.length());
					replyQuery.setConversationId(CONVERSATION_ID_DNS_REPLY);
//					replyQuery.addUserDefinedParameter(BroadcastingServiceAgent.LIST_OF_RECEIVERS, responseOfQuery);
//					replyQuery.addUserDefinedParameter(BroadcastingServiceAgent.APPLICATION, BroadcastingServiceAgent.DNS_REQUEST_RESPONSE);
					
					send(replyQuery);
				}
			} else {
				block();
			}
		}
	}
}
