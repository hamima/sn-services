package ir.mod.tavana.toranj.services.communication;

import java.util.Date;
import java.util.Iterator;

import jade.content.AgentAction;
import jade.content.ContentElement;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FIPAManagementOntology;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.ArrayList;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.TreeBidiMap;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class CommunicationServiceAgent extends Agent {

	public static final String WSIG_FLAG = "wsig";
	public static final String WSIG_MAPPER = "wsig-mapper";
	public static final String WSIG_PREFIX = "wsig-prefix";

	private Logger log = Logger.getLogger(CommunicationServiceAgent.class.getName());
	public static AID myAID = null;
	private SLCodec codec = new SLCodec();
	private Date startDate;

	private BidiMap title_id = new TreeBidiMap();
	private int[][] matrix;
	private int m_size = 0;

	protected void setup() {
		log.setLevel(Level.FATAL);
		matrix = new int[m_size][m_size];
		log.info("A CommunicationS-Agent is starting...");
		log.info("Agent name: " + getLocalName());

		// Get agent arguments
		Object[] args = getArguments();

		// Register codec/onto
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(FIPAManagementOntology.getInstance());
		getContentManager().registerOntology(CommunicationServicesOntology.getInstance());

		// Prepare a DFAgentDescription
		DFAgentDescription dfad = new DFAgentDescription();
		dfad.setName(this.getAID());
		dfad.addLanguages(codec.getName());
		dfad.addProtocols(FIPANames.InteractionProtocol.FIPA_REQUEST);
		ServiceDescription sd;
		sd = new ServiceDescription();
		sd.addLanguages(codec.getName());
		sd.addProtocols(FIPANames.InteractionProtocol.FIPA_REQUEST);
		sd.setType("CommunicationS-Agent");
		sd.setOwnership("CommunicationOwner");
		sd.addOntologies(CommunicationServicesOntology.getInstance().getName());

		// WSIG properties
		sd.addProperties(new Property(WSIG_FLAG, "true"));

		// Service name
		String wsigServiceName = "Communication";
		if (args.length >= 1) {
			wsigServiceName = (String) args[0];
		}
		// log.info("Service name: " + wsigServiceName);
		sd.setName(wsigServiceName);

		// Mapper
		boolean isMapperPresent = false;
		if (args.length >= 2) {
			isMapperPresent = Boolean.parseBoolean((String) args[1]);
		}
		// log.info("Mapper present: " + isMapperPresent);
		if (isMapperPresent) {
			sd.addProperties(new Property(WSIG_MAPPER, "ir.mod.tavana.toranj.examples.MathOntologyMapper"));
		}

		// Prefix
		String wsigPrefix = "";
		if (args.length >= 3) {
			wsigPrefix = (String) args[2];
		}
		// log.info("Prefix: " + wsigPrefix);
		if (wsigPrefix != null && !wsigPrefix.equals("")) {
			sd.addProperties(new Property(WSIG_PREFIX, wsigPrefix));
		}

		dfad.addServices(sd);

		// DF registration
		try {
			DFService.register(this, dfad);
		} catch (Exception e) {
			log.error("Problem during DF registration", e);
			doDelete();
		}

		// log.debug("A CommunicationS-Agent is started.");
		startDate = new Date();

		// Add communication behaviour
		this.addBehaviour(new OfferRequestServer(this));

		// this.addBehaviour(new TickerBehaviour(this, 30000) {
		// protected void onTick() {
		// java.util.ArrayList<AID> users = search("userAgent");
		//
		// // Message carrying a request for ping-pong
		// ACLMessage req = new ACLMessage(ACLMessage.REQUEST);
		// for (AID user : users) {
		// req.addReceiver(user);
		// }
		// req.setContent("ping");
		// myAgent.send(req);
		// }
		// });
	}

	private class OfferRequestServer extends CyclicBehaviour {
		private MessageTemplate template = MessageTemplate.MatchOntology(CommunicationServicesOntology.getInstance()
				.getName());

		public OfferRequestServer(Agent a) {
			super(a);
		}

		public void action() {
			ACLMessage msg = myAgent.receive(template);
			if (msg != null) {
				Action actExpr;
				try {
					actExpr = (Action) myAgent.getContentManager().extractContent(msg);
					AgentAction action = (AgentAction) actExpr.getAction();
					if (action instanceof Msg) {
						serveMsgAction((Msg) action, actExpr, msg);
					} else {
						log.warn("Undefined Action!");
					}
				} catch (Exception e) {
					log.error("Exception: " + e.getMessage(), e);
				}
			} else {
				msg = myAgent.receive();
				if (msg != null) {
					try {
						if (msg.getPerformative() == ACLMessage.SUBSCRIBE) {
							// System.out.println("<<<<a subscribe message>>>>");
							serveMsgActionSubscribe(msg);
						} else {
							log.warn("Undefined Action!");
						}
					} catch (Exception e) {
						log.error("Exception: " + e.getMessage(), e);
					}
				} else {
					block();
				}
			}
		}
	}

	private void serveMsgActionSubscribe(ACLMessage msg) {
		String content = msg.getContent();
		String[] neighbors = content.split(",");
		title_id.put(msg.getSender().getLocalName(), ++m_size);
		int[] int_neighbors = new int[m_size];
		for (int i = 0; i < m_size; i++)
			int_neighbors[i] = -1;
		for (String neighbor : neighbors) {
			if (neighbor.equals(""))
				continue;
			int id = (Integer) title_id.get(neighbor.split(":")[0]);
			int weight = Integer.parseInt(neighbor.split(":")[1]);
			int_neighbors[id - 1] = weight;
		}
		int_neighbors[m_size - 1] = 0;
		appendMatrix(int_neighbors);
	}

	private java.util.ArrayList<AID> search(String service_type) {
		java.util.ArrayList<AID> providers = new java.util.ArrayList<AID>();
		// Search for services of type "weather-forecast"
		// System.out.println("Agent " + getLocalName() + " searching for services of type \"" + service_type
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
				// System.out.println("Agent " + getLocalName() + " found the following \"" + service_type
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
							// System.out.println("- Service \"" + sd.getName() + "\" provided by agent "
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

	private void serveMsgAction(Msg msg_class, Action actExpr, ACLMessage request) {
		// send the message to the receiver and waiting for the response
		String result = "message is delivered!";
		this.addBehaviour(new MsgPerformer(msg_class, actExpr, request, ACLMessage.INFORM, result));
	}

	private void appendMatrix(int[] neighbors) {
		int[][] new_matrix = new int[m_size][m_size];
		for (int i = 0; i < m_size - 1; i++)
			for (int j = 0; j < m_size - 1; j++)
				new_matrix[i][j] = matrix[i][j];
		for (int i = 0; i < m_size; i++) {
			new_matrix[m_size - 1][i] = neighbors[i];
			new_matrix[i][m_size - 1] = neighbors[i];
		}
		matrix = new_matrix;
	}

	private String getForwardList(String source, String target) {
		DistanceVector dv = new DistanceVector(matrix);

		int from = (Integer) title_id.get(source);
		int to = (Integer) title_id.get(target);

		PrintShortestPath path_print = new PrintShortestPath(matrix, dv, from, to);
		int[] path = path_print.getPath();
		StringBuffer impression_path = new StringBuffer();
		int k = 0;
		while (path[k] != 0) {
			if (path[k] != from)
				impression_path.append(title_id.getKey(path[k]) + ",");
			k++;
		}
		impression_path.replace(impression_path.length() - 1, impression_path.length(), "");
		return impression_path.toString();
	}

	private class MsgPerformer extends Behaviour {

		private Msg msg_class; // the message should be delivered
		private Action actExpr;
		private ACLMessage request;
		private int performative;
		private Object result;

		private MessageTemplate mt; // The template to receive replies
		private int step = 0;

		public MsgPerformer(Msg msg_class, Action actExpr, ACLMessage request, int performative, Object result) {
			this.msg_class = msg_class;
			this.actExpr = actExpr;
			this.request = request;
			this.performative = performative;
			this.result = result;
		}

		public void action() {
			switch (step) {
			case 0:
				java.util.ArrayList<AID> users = search("userAgent");
				// Send the request to the receiver
				ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
				AID receiver = new AID(msg_class.getTarget(), AID.ISLOCALNAME);
				// if (users.contains(receiver)) {
				if (msg_class.getMtype() == 1) { // DIRECT
					request.addReceiver(receiver);
					request.setContent(msg_class.getMsg());
					request.addUserDefinedParameter("mtype", String.valueOf(msg_class.getMtype()));
					request.addUserDefinedParameter("priority", String.valueOf(msg_class.getPriority()));
					request.addUserDefinedParameter("lifetime", String.valueOf(msg_class.getLifetime()));
					request.addUserDefinedParameter("forwardList", msg_class.getForwardList());
					request.addUserDefinedParameter("source", msg_class.getSource());
					request.addUserDefinedParameter("target", msg_class.getTarget());
					request.setConversationId("msg-delivery");
					request.setReplyWith("cfp" + System.currentTimeMillis());
					myAgent.send(request);
					// Prepare the template to get response
					mt = MessageTemplate.and(MessageTemplate.MatchConversationId("msg-delivery"),
							MessageTemplate.MatchInReplyTo(request.getReplyWith()));
					step = 1;
				} else if (msg_class.getMtype() == 2) { // MULTI-HOP
					String forwardList = msg_class.getForwardList().trim();
					if (forwardList.equals(""))
						forwardList = getForwardList(msg_class.getSource(), msg_class.getTarget());
					String[] forward_array = forwardList.split(",");
					String current_hop = forward_array[0];
					StringBuffer new_forwardList = new StringBuffer();
					if (forward_array.length > 1) {
						for (int i = 1; i < forward_array.length; i++)
							new_forwardList.append(forward_array[i] + ",");
						new_forwardList.delete(new_forwardList.length() - 1, new_forwardList.length());
					}
					request.addReceiver(new AID(current_hop, AID.ISLOCALNAME));
					request.setContent(msg_class.getMsg());
					request.addUserDefinedParameter("mtype", String.valueOf(msg_class.getMtype()));
					request.addUserDefinedParameter("priority", String.valueOf(msg_class.getPriority()));
					request.addUserDefinedParameter("lifetime", String.valueOf(msg_class.getLifetime()));
					request.addUserDefinedParameter("forwardList", new_forwardList.toString());
					request.addUserDefinedParameter("source", msg_class.getSource());
					request.addUserDefinedParameter("target", msg_class.getTarget());
					request.addUserDefinedParameter("hob", myAgent.getLocalName());
					request.setConversationId("msg-routing");
					request.addUserDefinedParameter("replyWith", "cfp" + System.currentTimeMillis());
					request.setReplyWith("cfp" + System.currentTimeMillis());
					myAgent.send(request);
					// Prepare the template to get response
					mt = MessageTemplate.and(MessageTemplate.MatchConversationId("msg-routing"),
							MessageTemplate.MatchInReplyTo(request.getReplyWith()));
					step = 1;
				}
				// } else {
				// this.result = "unknown receiver";
				// sendNotification(this.actExpr, this.request, this.performative, this.result);
				// step = 2;
				// }
				break;
			case 1:
				// Receive the response from the target agents
				ACLMessage reply = myAgent.receive(mt);
				if (reply != null) {
					// Reply received
					if (reply.getPerformative() == ACLMessage.CONFIRM) {
						// This is an ack
						sendNotification(this.actExpr, this.request, this.performative, this.result);
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

	private void sendNotification(Action actExpr, ACLMessage request, int performative, Object result) {
		// Send back a proper reply to the requester
		ACLMessage reply = request.createReply();
		if (performative == ACLMessage.INFORM) {
			reply.setPerformative(ACLMessage.INFORM);
			try {
				ContentElement ce = null;
				if (result != null) {
					// If the result is a java.util.List, convert it into a
					// jade.util.leap.List t make the ontology "happy"
					if (result instanceof java.util.List) {
						ArrayList l = new ArrayList();
						l.fromList((java.util.List) result);
						result = l;
					}
					ce = new Result(actExpr, result);
				} else {
					ce = new Done(actExpr);
				}
				getContentManager().fillContent(reply, ce);
			} catch (Exception e) {
				log.error("Agent " + getName() + ": Unable to send notification" + e);
				e.printStackTrace();
			}
		} else {
			reply.setPerformative(performative);

		}
		reply.addUserDefinedParameter(ACLMessage.IGNORE_FAILURE, "true");
		send(reply);
	}

	protected void takeDown() {
		// deregister itself from the DF
		try {
			DFService.deregister(this);
		} catch (Exception e) {
			log.error(e);
		}

		log.debug("A CommunicationS-Agent is taken down now.");
	}

	private void printTest() {

		DistanceVector dv = new DistanceVector(matrix);

		int from = 2;
		int to = 3;

		System.out.println("The shortest path from " + from + " to " + to + " is:");
		PrintShortestPath path_print = new PrintShortestPath(matrix, dv, from, to);
		int[] path = path_print.getPath();
		StringBuffer impression_path = new StringBuffer();
		System.out.println("Impression Path");
		int k = 0;
		while (path[k] != 0) {
			impression_path.append(String.valueOf(path[k]) + ", ");
			k++;
		}
		impression_path.replace(impression_path.length() - 2, impression_path.length(), "");
		System.out.println(impression_path);
	}
}
