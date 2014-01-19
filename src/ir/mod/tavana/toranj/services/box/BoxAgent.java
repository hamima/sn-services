package ir.mod.tavana.toranj.services.box;

import ir.mod.tavana.toranj.services.broadcasting.BroadCastingServicesOntology;
import ir.mod.tavana.toranj.services.task_allocation.Bid;
import ir.mod.tavana.toranj.services.task_allocation.RequestForBid;
import ir.mod.tavana.toranj.services.task_allocation.TaskAllocationOntology;

import java.util.Random;


import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class BoxAgent extends Agent {
		
	private int x,y;
	private boolean busy = false;
//	private MyAgent my;
	private static int idCounter = 0;
	public static String Type = "BoxAgent";

	private SLCodec codec = new SLCodec();
	
	public int getPrice(String type,int x1,int y1) {
		/*if (type.length() < 4) type=type+"___";
		if (type.substring(0, 4).equals("GoTo")) {
			return Math.sqrt((x-x1)*(x-x1)+(y-y1)*(y-y1));
		}
		else if (type.substring(0, 4).equals("Push")) {
			return 2*Math.sqrt((x-x1)*(x-x1)+(y-y1)*(y-y1));
		} else
			return 100000;
		*/
		if (type.substring(0, 1).equals("G")) return 1;
		if (type.substring(0, 1).equals("P")) return 1;
		Random r = new Random();
		x = r.nextInt() % 10000;
		x = x < 0 ? - x : x;
		return x;
	}

	public boolean isBusy() { 
		return busy;
	}

	public void doTask(String type,int x1,int y1) {
		busy = true;
		if (type.equals("GoTo")) {
			x = x1;
			y = y1;
		}
		else if (type.equals("Push")) {
			x = x1;
			y = y1;
		}
		else {
			//do a compound task
		}
		try {
			wait(10000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		busy = false;
	}

	protected void setup() {

		getContentManager().registerLanguage(codec, FIPANames.ContentLanguage.FIPA_SL0);
		getContentManager().registerOntology(TaskAllocationOntology.getInstance());

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
//		dfd.addLanguages(codec.getName());
//		dfd.addProtocols(FIPANames.InteractionProtocol.FIPA_REQUEST);

		
		ServiceDescription sd = new ServiceDescription();
//		sd.addProtocols(FIPANames.InteractionProtocol.FIPA_REQUEST);
		sd.setType("BoxAgent");
		sd.setName("BoxAgent-agents");
//		sd.addOntologies(TaskAllocationOntology.getInstance().getName());
		sd.addLanguages(codec.getName());
		
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		
		Random r = new Random();
		x = r.nextInt() % 100;
		y = r.nextInt() % 100;
		
		addBehaviour(new getMessage());
		System.out.println("Hello! Box-agent "+getAID().getName()+" is ready.");
	}
	
	protected void takeDown() {
		// Printout a dismissal message
		System.out.println("Bye! Box-agent "+getAID().getName()+" is terminating.");
	}

	private class getMessage extends CyclicBehaviour {

		public void action() {
			ACLMessage msg = myAgent.blockingReceive();
			if (msg != null) {
				ACLMessage reply = performMessage(msg);
				myAgent.send(reply);
			}
		}

		private ACLMessage performMessage(ACLMessage msg) {
			ACLMessage reply = msg.createReply();
			int performative = msg.getPerformative();
			if (performative == ACLMessage.CFP) {
				RequestForBid reqForBid = null;
				try {
					reqForBid = (RequestForBid) myAgent.getContentManager().extractContent(msg);
				} catch (UngroundedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CodecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OntologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String content = "" + getPrice(reqForBid.getDescription(), 0, 0);
				reply.setOntology(TaskAllocationOntology.ONTOLOGY_NAME);
				Bid myBid = new Bid();
				myBid.setMyBid(getPrice(msg.getContent(), 0, 0));
				reply.setPerformative(ACLMessage.PROPOSE);
				reply.setOntology(TaskAllocationOntology.ONTOLOGY_NAME);
				reply.setContent(content);
				try {
					myAgent.getContentManager().fillContent(reply,myBid);
				} catch (CodecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OntologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (performative == ACLMessage.QUERY_IF) {
				String content = "" + isBusy();
				reply.setPerformative(ACLMessage.INFORM);
				reply.setContent(content);				
			} else if (performative == ACLMessage.REQUEST) {
				doTask(msg.getContent(), 0, 0);
				reply.setPerformative(ACLMessage.CONFIRM);
				reply.setContent("");
			}
			return reply;
		}
	}
}


