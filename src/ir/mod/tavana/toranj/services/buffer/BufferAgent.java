package ir.mod.tavana.toranj.services.buffer;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import jade.content.AgentAction;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class BufferAgent extends Agent implements BufferVocabulary{
	
	public static final String WSIG_FLAG = "wsig";
	
	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.addLanguages((new SLCodec()).getName());
		sd.setName("Buffer_agent");
		sd.setType("Buffer_Service");
		sd.addOntologies(ONTOLOGY_NAME);

		// WSIG properties
		sd.addProperties(new Property(WSIG_FLAG, "true"));

		
		dfd.addServices(sd);
		
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.addBehaviour(new BufferBehavior());
	}
	
	private class BufferBehavior extends CyclicBehaviour{

		int agentID;
		public final Lock accessLock = new ReentrantLock();
		private int act0 = -1;
		private int act1 = -1;
		private int both = 0;

		@Override
		public void action() {
			// TODO Auto-generated method stub
			ACLMessage aclMessage = myAgent.receive();
			if(aclMessage!=null){
				Action actExpr;
				try {
					actExpr = (Action) myAgent.getContentManager().extractContent(aclMessage);
					AgentAction action = (AgentAction) actExpr.getAction();
					if (action instanceof Set) {
						Set tempSet = (Set) action;
						set(tempSet.getValue(),tempSet.getAgentId());
						
						// Inja mikhaham yek payam be soccer befrestam
						
						List<AID> possibleReceivers = search("soccer");
						ACLMessage aclMessage2 = new ACLMessage(ACLMessage.REQUEST);
						aclMessage2.addReceiver(possibleReceivers.get(0));
						aclMessage2.setOntology("Soccer_ONTOLOGY");
						aclMessage2.setContentObject(new Get());
						send(aclMessage2);
						
						
					} else if (action instanceof Get) {
						// Inja ro ham mesle set por karde o method ro dar paein piadesazi konin
					}
				}catch (Exception e){
					
				}
			} else {
				block();
			}
		}

		public void set(int value, int agentId) // throws InterruptedException
		{
			// act0=-1;
			// act1=-1;
			agentID = agentId;
			accessLock.lock();
			// System.out.println("in set agent "+agentId);
			try {
				if (agentId == 0)
					act0 = value;
				else
					act1 = value;

				/*
				 * if ((act0!=-1) &&(act1!=-1)) { //canRead.signal();
				 * System.out.println("signal"); }
				 */
			}// end try
			finally {

				// System.out.println("in 2 set agent "+agentId + "  "+act0 +
				// "  "+act1);
				accessLock.unlock();
				both++;
			}// end finally
		}


		
	}
	
	private java.util.ArrayList<AID> search (String service_type) {
		java.util.ArrayList<AID> providers = new java.util.ArrayList<AID>();
		
		DFAgentDescription templateDFAD = new DFAgentDescription();
		ServiceDescription templateSD = new ServiceDescription();
		templateSD.setType(service_type);
		
		SearchConstraints sc = new SearchConstraints();
		sc.setMaxResults(new Long(5));
		DFAgentDescription[] results = null;
		try {
			results = DFService.search(this, templateDFAD, sc);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (results.length > 0) {
			for (int i = 0; i < results.length; ++i) {
				DFAgentDescription dfd = results[i];
				AID provider = dfd.getName();
				// The same agent may provide several services; we are only interested in the communication one
				Iterator it = dfd.getAllServices();
				while (it.hasNext()) {
					ServiceDescription sd = (ServiceDescription) it.next();
					if (sd.getType().equals(service_type)) {
						providers.add(provider);
					}

				}
			}
		} else {
		}
		return providers;
	}


}
