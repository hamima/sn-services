package ir.mod.tavana.toranj.agents.post;

import ir.mod.tavana.toranj.services.communication.UserAgent;

import java.util.Iterator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class SensorPost extends Agent {
	private Logger log = Logger.getLogger(UserAgent.class.getName());

	@Override
	protected void setup() {
		log.setLevel(Level.INFO);
		log.info("A SensorPost-Agent is starting...");
		log.info("Agent name: " + getLocalName());

		// Register the SP in the yellow pages
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("SP");
		sd.setName("sp-agents");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		// Add communication behaviour
		this.addBehaviour(new OfferRequestServer(this));
	}

	private class OfferRequestServer extends CyclicBehaviour {

		public OfferRequestServer(Agent a) {
			super(a);
		}

		public void action() {
			ACLMessage msg = myAgent.receive(MessageTemplate.MatchContent("Get Sensor Env Status"));
			if (msg != null) {
				try {
					if (msg.getContent().contains("Get Sensor Env Status")) {
						serveSearchAction("SearchRadar", "Get Targets");
					}
				} catch (Exception e) {
					log.error("Exception: " + e.getMessage(), e);
				}
			} else {
				block();
			}
		}
	}

	private void serveSearchAction(String type, String msg) throws Exception {
		// send the message to the all receivers and waiting for the response
		this.addBehaviour(new SearchPerformer(type, msg));
	}

	private class SearchPerformer extends Behaviour {

		private String agent_type;
		private String msg;
		private String[] answers;

		private MessageTemplate mt; // The template to receive replies
		private int step = 0;
		private int repliesCnt = 0;

		private java.util.ArrayList<AID> search_agents;

		public SearchPerformer(String agent_type, String msg) throws Exception {
			this.agent_type = agent_type;
			this.msg = msg;

			// phase1: get the message
			search_agents = search(this.agent_type);
			answers = new String[search_agents.size()];
		}

		public void action() {
			switch (step) {
			case 0:
				// phase2: send request to the targets
				ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
				request.setContent(msg);

				for (AID target : search_agents) {
					request.addReceiver(target);
				}
				request.setConversationId("SP-delivery");
				request.addUserDefinedParameter("replyWith", "cfp" + System.currentTimeMillis());
				request.setReplyWith("cfp" + System.currentTimeMillis());
				myAgent.send(request);

				// Prepare the template to get response
				mt = MessageTemplate.and(MessageTemplate.MatchConversationId("SP-delivery"),
						MessageTemplate.MatchInReplyTo(request.getReplyWith()));
				step = 1;
				break;
			case 1:
				// Receive all response from target agents
				ACLMessage reply = myAgent.receive(mt);
				if (reply != null) {
					// Reply received
					String answer = reply.getContent();
					String id = reply.getSender().getLocalName();
					this.answers[repliesCnt] = id + ":" + answer;
					repliesCnt++;
					System.out.println(repliesCnt);
					if (repliesCnt >= search_agents.size()) {
						// We received all replies
						StringBuffer buffer = new StringBuffer();
						for (String a : this.answers) {
							buffer.append("\t" + a + "\n");
						}
						System.out.println("Sensors Status: \n" + buffer.toString());
						step = 2;
					}
				} else {
					block();
				}
				break;
			}
		}

		public boolean done() {
			return (step == 2);
		}
	} // End of inner class RequestPerformer

	protected void takeDown() {
		// deregister itself from the DF
		try {
			DFService.deregister(this);
		} catch (Exception e) {
			log.error(e);
		}
		log.debug("A SP-Agent is taken down now.");
	}

	private java.util.ArrayList<AID> search(String service_type) {
		java.util.ArrayList<AID> providers = new java.util.ArrayList<AID>();
		// Search for services of type "weather-forecast"
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
				// System.out.println("Agent " + getLocalName() +
				// " did not find any \"communication\" service");
			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		return providers;
	}

}
