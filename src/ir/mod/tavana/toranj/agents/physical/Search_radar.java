package ir.mod.tavana.toranj.agents.physical;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Iterator;

public class Search_radar extends Agent {
	private int ID;
	private double X;
	private double Y;
	private double RCS;
	private double updatetime;
	private String targets;

	public Search_radar() {

	}

	public Search_radar(int ID, double X, double Y, double RCS, double updatetime, String targets) {
		this.ID = ID;
		this.X = X;
		this.Y = Y;
		this.RCS = RCS;
		this.updatetime = updatetime;
		this.targets = targets;
	}
	

	@Override
	protected void setup() {
		// Register the user service in the yellow pages
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("SearchRadar");
		sd.setName("SearchRadar-agents");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		// Add the behaviour serving queries from communication agents
		this.addBehaviour(new RequestServer(this));
	}

	private java.util.ArrayList<AID> search(String service_type) {
		java.util.ArrayList<AID> providers = new java.util.ArrayList<AID>();
		// Search for services of type "service_type"
		// System.out.println("Agent " + getLocalName() +
		// " searching for services of type \"" + service_type
		// + "\"");
		try {
			// Build the description used as template for the search
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription templateSd = new ServiceDescription();
			templateSd.setType(service_type);
			template.addServices(templateSd);

			SearchConstraints sc = new SearchConstraints();
			// We want to receive 10 results at most
			sc.setMaxResults(new Long(10));

			DFAgentDescription[] results = DFService.search(this, template, sc);
			if (results.length > 0) {
				// System.out.println("Agent " + getLocalName() +
				// " found the following \"" + service_type
				// + "\" services:");
				for (int i = 0; i < results.length; ++i) {
					DFAgentDescription dfd = results[i];
					AID provider = dfd.getName();
					// The same agent may provide several services; we are only
					// interested in the communication one
					Iterator it = dfd.getAllServices();
					while (it.hasNext()) {
						ServiceDescription sd = (ServiceDescription) it.next();
						if (sd.getType().equals(service_type)) {
							// System.out.println("- Service \"" + sd.getName()
							// + "\" provided by agent "
							// + provider.getName());
							providers.add(provider);
						}
					}
				}
			} else {
				System.out.println("Agent " + getLocalName() + " did not find any \"communication\" service");
			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		return providers;
	}

	private class RequestServer extends CyclicBehaviour {

		public RequestServer(Agent a) {
			super(a);
		}

		public void action() {
			ACLMessage msg = myAgent.receive();
			if (msg != null) {
				if (msg.getPerformative() == ACLMessage.REQUEST) {
					if (msg.getConversationId().equals("SP-delivery")) { // Sensor Post request
						String content = msg.getContent();
						ACLMessage reply = msg.createReply();
						reply.setPerformative(ACLMessage.CONFIRM);
						reply.setContent("labla " + getLocalName());
						send(reply);
					}
				}
			} else {
				block();
			}
		}
	} // End of inner class RequestsServer

	public void print() {
		System.out.println("Hello, my name is " + this.getLocalName());
	}
}
