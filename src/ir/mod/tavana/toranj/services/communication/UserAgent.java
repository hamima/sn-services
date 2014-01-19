package ir.mod.tavana.toranj.services.communication;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;
import jade.util.leap.Iterator;

public class UserAgent extends Agent {

	private Logger log = Logger.getLogger(UserAgent.class.getName());
	private HashMap<AID, Integer> neighbors;
	private static Random rand = new Random();
	private HashSet<Integer> received_sprs;
	private HashMap<Integer, String> msg_routing;

	protected void setup() {
		log.setLevel(Level.FATAL);
		// log.info("A user agent is starting...");
		// log.info("Agent name: " + getLocalName());

		neighbors = new HashMap<AID, Integer>();
		received_sprs = new HashSet<Integer>();
		msg_routing = new HashMap<Integer, String>();

		Object[] args = getArguments();
		StringBuffer content = new StringBuffer();
		if (args != null && args.length > 0) {
			for (Object neighbor : args) {
				content.append((String) neighbor + ",");
				String[] name_weight = ((String) neighbor).split(":");
				neighbors.put(new AID(name_weight[0], false), Integer.valueOf(name_weight[1]));
			}
			content.delete(content.length() - 1, content.length());
		} else {
			content.append("");
			// log.info("An isolated agent is created!");
		}
		// send neighbors to communicationS-Agent
		java.util.ArrayList<AID> providers = search("CommunicationS-Agent");
		ACLMessage cfp = new ACLMessage(ACLMessage.SUBSCRIBE);
		for (AID provider : providers) {
			cfp.addReceiver(provider);
		}
		cfp.setContent(content.toString());
		this.send(cfp);

		// send neighbors to other UserAgents
		ACLMessage cfp2 = new ACLMessage(ACLMessage.REQUEST);
		for (AID neighbor : neighbors.keySet()) {
			cfp2.addReceiver(neighbor);
		}
		cfp2.setContent(content.toString());
		cfp2.setConversationId("neighboring");
		this.send(cfp2);

		// Register the user service in the yellow pages
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("userAgent");
		sd.setName("user-agents");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		// Add the behaviour serving queries from communication agents
		this.addBehaviour(new RequestServer(this));

		// Make this agent terminate
		// doDelete();
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
			// MessageTemplate mt = MessageTemplate
			// .MatchPerformative(ACLMessage.Request);
			ACLMessage msg = myAgent.receive();
			if (msg != null) {
				if (msg.getPerformative() == ACLMessage.REQUEST) {
					if (msg.getConversationId().equals("polling-delivery")) { // POLLING
						String question = msg.getContent();
						String[] options = msg.getUserDefinedParameter("options").split("\\s*;\\s*");
						String scope = msg.getUserDefinedParameter("scope");
						// put your strategy here
						StringBuffer answers = new StringBuffer();
						double val = 1.0 / options.length;
						for (String option : options) {
							answers.append(val + ";");
						}
						answers.delete(answers.length() - 1, answers.length());
						ACLMessage reply = msg.createReply();
						reply.setPerformative(ACLMessage.CONFIRM);
						reply.setContent(answers.toString());
						send(reply);
					} else if (msg.getConversationId().equals("spreading")) // SPREADING
					{
						String content = msg.getContent();
						String id = msg.getUserDefinedParameter("id");
						if (id == null) {
							id = String.valueOf(rand.nextInt());
						}
						String source = msg.getUserDefinedParameter("source");
						if (source == null)
							source = myAgent.getLocalName();
						String level = msg.getUserDefinedParameter("level");
						if (level == null)
							level = String.valueOf(Integer.MAX_VALUE);
						String mode = msg.getUserDefinedParameter("mode");
						if (mode == null)
							mode = "direct";
						String my_name = getAID().getLocalName();
						String sender = msg.getSender().getLocalName();
						if (!msg_routing.containsKey(Integer.valueOf(id))) {
							msg_routing.put(Integer.valueOf(id), sender);
						}
						if (my_name.equals(content) && !received_sprs.contains(Integer.valueOf(id))) {
							// send a confirm message to the source
							if (mode.equals("direct")) {
								ACLMessage confirm = new ACLMessage(ACLMessage.INFORM);
								confirm.addReceiver(new AID(source, AID.ISLOCALNAME));
								confirm.setContent(my_name);
								confirm.setConversationId("spreading_confirm");
								confirm.addUserDefinedParameter("id", id);
								send(confirm);
							} else {
								String target = msg_routing.get(Integer.valueOf(id));
								ACLMessage confirm = new ACLMessage(ACLMessage.INFORM);
								confirm.addReceiver(new AID(target, AID.ISLOCALNAME));
								confirm.setContent(my_name);
								confirm.setConversationId("spreading_confirm");
								confirm.addUserDefinedParameter("id", id);
								send(confirm);
							}
						}
						if (!received_sprs.contains(Integer.valueOf(id))) {
							received_sprs.add(Integer.valueOf(id));
							Integer l = Integer.parseInt(level);
							l--;
							if (l >= 0) {
								level = l.toString();
								// send spr_msg to all neighbors
								ACLMessage spr_msg = new ACLMessage(ACLMessage.REQUEST);
								for (AID neighbor : neighbors.keySet()) {
									spr_msg.addReceiver(neighbor);
								}
								spr_msg.setContent(content);
								spr_msg.addUserDefinedParameter("id", id);
								spr_msg.addUserDefinedParameter("source", source);
								spr_msg.addUserDefinedParameter("level", level);
								spr_msg.addUserDefinedParameter("mode", mode);
								spr_msg.setConversationId("spreading");
								send(spr_msg);
							}
						}
					} else if (msg.getConversationId().equals("neighboring")) {
						neighbors.put(msg.getSender(), 8888);
					} else { // COMMUNICATION SERVICE
								// CFP Message received. Process it
						String content = msg.getContent();
						String priority = msg.getUserDefinedParameter("priority");
						String lifetime = msg.getUserDefinedParameter("lifetime");
						String forwardList = msg.getUserDefinedParameter("forwardList");
						String source = msg.getUserDefinedParameter("source");
						String target = msg.getUserDefinedParameter("target");
						String replyWith = msg.getUserDefinedParameter("replyWith");
						String hob = msg.getUserDefinedParameter("hob");
						int mtype = Integer.parseInt(msg.getUserDefinedParameter("mtype"));
						ACLMessage reply = msg.createReply();
						if (msg.getPerformative() == ACLMessage.REQUEST) {
							if ((content != null) && (content.indexOf("ping") != -1)) {
								log.info("Agent " + getLocalName() + " - Received PING Request from "
										+ msg.getSender().getLocalName());
								reply.setPerformative(ACLMessage.INFORM);
								reply.setContent("pong");
							} else if (mtype == 1) {
								// log.info("Agent " + getLocalName() +
								// " - DIRECT request [" + content
								// + "] received from " +
								// msg.getSender().getLocalName());
								reply.setPerformative(ACLMessage.CONFIRM);
								reply.setContent("received!");
								reply.addUserDefinedParameter("mtype", String.valueOf(mtype));
								reply.addUserDefinedParameter("priority", priority);
								reply.addUserDefinedParameter("lifetime", lifetime);
								reply.addUserDefinedParameter("source", source);
								reply.addUserDefinedParameter("target", target);
								reply.addUserDefinedParameter("forwardList", "");
								send(reply);
							} else if (mtype == 2) {
								// log.info("Agent " + getLocalName() +
								// " - MULTI-HOP request [" + content
								// + "] received from " +
								// msg.getSender().getLocalName());
								// extract the next receiver and forward the message
								if (getLocalName().equals(target)) {
									ACLMessage confirm = new ACLMessage(ACLMessage.CONFIRM);
									confirm.addReceiver(new AID(hob, AID.ISLOCALNAME));
									// for (AID communicationAgent : search("CommunicationS-Agent"))
									// confirm.addReceiver(communicationAgent);
									confirm.setContent("received!");
									confirm.addUserDefinedParameter("mtype", String.valueOf(mtype));
									confirm.addUserDefinedParameter("priority", priority);
									confirm.addUserDefinedParameter("lifetime", lifetime);
									confirm.addUserDefinedParameter("source", source);
									confirm.addUserDefinedParameter("target", target);
									confirm.addUserDefinedParameter("forwardList", "");
									confirm.addUserDefinedParameter("replyWith", replyWith);
									confirm.setInReplyTo(replyWith);
									confirm.setConversationId("msg-routing");
									send(confirm);
								} else {
									String[] forward_array = forwardList.split(",");
									String next_hop = forward_array[0];
									StringBuffer new_forwardList = new StringBuffer();
									if (forward_array.length > 1) {
										for (int i = 1; i < forward_array.length; i++)
											new_forwardList.append(forward_array[i] + ",");
										new_forwardList.delete(new_forwardList.length() - 1,
												new_forwardList.length());
									}
									ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
									AID receiver = new AID(next_hop, AID.ISLOCALNAME);
									request.addReceiver(receiver);
									request.setContent(content);
									request.addUserDefinedParameter("mtype", String.valueOf(mtype));
									request.addUserDefinedParameter("priority", priority);
									request.addUserDefinedParameter("lifetime", lifetime);
									request.addUserDefinedParameter("source", source);
									request.addUserDefinedParameter("target", target);
									request.addUserDefinedParameter("forwardList", new_forwardList.toString());
									request.addUserDefinedParameter("replyWith", replyWith);
									request.addUserDefinedParameter("hob", hob);
									request.setConversationId("msg-routing");
									myAgent.send(request);
								}
							}
						} else {
							// log.info("Agent " + getLocalName() +
							// " - Unexpected message ["
							// + ACLMessage.getPerformative(msg.getPerformative()) +
							// "] received from "
							// + msg.getSender().getLocalName());
							reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
							reply.setContent("( (Unexpected-act "
									+ ACLMessage.getPerformative(msg.getPerformative()) + ") )");
							send(reply);
						}
					}
				} else if (msg.getPerformative() == ACLMessage.PROPAGATE) {
					if (msg.getConversationId().equals("spreading")) // SPREADING
					{
						String content = msg.getContent();
						String id = msg.getUserDefinedParameter("id");
						if (id == null) {
							id = String.valueOf(rand.nextInt());
						}
						String source = msg.getUserDefinedParameter("source");
						if (source == null)
							source = myAgent.getLocalName();
						String level = msg.getUserDefinedParameter("level");
						if (level == null)
							level = String.valueOf(Integer.MAX_VALUE);
						String mode = msg.getUserDefinedParameter("mode");
						if (mode == null)
							mode = "direct";
						if (!received_sprs.contains(Integer.valueOf(id))) {
							received_sprs.add(Integer.valueOf(id));
							Integer l = Integer.parseInt(level);
							l--;
							if (l >= 0) {
								level = l.toString();
								// send spr_msg to all neighbors
								ACLMessage spr_msg = new ACLMessage(ACLMessage.PROPAGATE);
								for (AID neighbor : neighbors.keySet()) {
									spr_msg.addReceiver(neighbor);
								}
								spr_msg.setContent(content);
								spr_msg.addUserDefinedParameter("id", id);
								spr_msg.addUserDefinedParameter("source", source);
								spr_msg.addUserDefinedParameter("level", level);
								spr_msg.setConversationId("spreading");
								send(spr_msg);
							}
						}
					}
				} else if (msg.getPerformative() == ACLMessage.INFORM) {
					if (msg.getConversationId().equals("spreading_confirm")) {
						String id = msg.getUserDefinedParameter("id");
						String founder = msg.getContent();
						String target = msg_routing.get(Integer.valueOf(id));
						if (target != null) {
							ACLMessage confirm = new ACLMessage(ACLMessage.INFORM);
							confirm.addReceiver(new AID(target, AID.ISLOCALNAME));
							confirm.setContent(founder);
							confirm.setConversationId("spreading_confirm");
							confirm.addUserDefinedParameter("id", id);
							send(confirm);
						}
					}
				} else {
					System.out.println("Un-known message!");
				}
			} else {
				block();
			}
		}
	} // End of inner class OfferRequestsServer

	private void subscribe(final String service_type) {
		// Build the description used as template for the subscription
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription templateSd = new ServiceDescription();
		templateSd.setType(service_type);
		template.addServices(templateSd);

		SearchConstraints sc = new SearchConstraints();
		// We want to receive 10 results at most
		sc.setMaxResults(new Long(10));

		addBehaviour(new SubscriptionInitiator(this, DFService.createSubscriptionMessage(this,
				getDefaultDF(), template, sc)) {
			protected void handleInform(ACLMessage inform) {
				// System.out.println("Agent " + getLocalName() +
				// ": Notification received from DF");
				try {
					DFAgentDescription[] results = DFService.decodeNotification(inform.getContent());
					if (results.length > 0) {
						for (int i = 0; i < results.length; ++i) {
							DFAgentDescription dfd = results[i];
							AID provider = dfd.getName();
							// The same agent may provide several services; we
							// are only interested in the one
							Iterator it = dfd.getAllServices();
							while (it.hasNext()) {
								ServiceDescription sd = (ServiceDescription) it.next();
								if (sd.getType().equals(service_type)) {
									System.out.println("\"" + service_type + "\"" + "service found:");
									System.out.println("- Service \"" + sd.getName()
											+ "\" provided by agent " + provider.getName());
								}
							}
						}
					}
					System.out.println();
				} catch (FIPAException fe) {
					fe.printStackTrace();
				}
			}
		});
	}

}
