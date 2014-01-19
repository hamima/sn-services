package ir.mod.tavana.toranj.services.polling;

import ir.mod.tavana.toranj.services.avatar.AvatarOntology;
import ir.mod.tavana.toranj.services.avatar.polling.PollRequest;
import ir.mod.tavana.toranj.services.avatar.polling.PollRequestForward;
import ir.mod.tavana.toranj.services.polling.PollingMsg;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import jade.content.AgentAction;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class PollingServiceAgent extends Agent implements PollingServicesVocabulary{

	public static final String WSIG_FLAG = "wsig";
	public static final String WSIG_MAPPER = "wsig-mapper";
	public static final String WSIG_PREFIX = "wsig-prefix";
	
	private Logger log = Logger.getLogger(PollingServiceAgent.class.getName());
	public static AID myAID = null;
	private SLCodec codec = new SLCodec();
	private long start;

	private PollingDB pollingDB;

	protected void setup() {
		log.setLevel(Level.FATAL);
		log.info( "A PollingS-Agent is starting...");
		log.info("Agent name: " + getLocalName());

		pollingDB = new PollingDB();

		// Get agent arguments
		Object[] args = getArguments();

		// Register codec/onto
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(FIPAManagementOntology.getInstance());
		getContentManager().registerOntology(PollingServicesOntology.getInstance());
		getContentManager().registerOntology(AvatarOntology.getInstance());

		// Prepare a DFAgentDescription
		DFAgentDescription dfad = new DFAgentDescription();
		dfad.setName(this.getAID());
		dfad.addLanguages(codec.getName());
		dfad.addProtocols(FIPANames.InteractionProtocol.FIPA_REQUEST);
		ServiceDescription sd;
		sd = new ServiceDescription();
		sd.addLanguages(codec.getName());
		sd.addProtocols(FIPANames.InteractionProtocol.FIPA_REQUEST);
		sd.setType(POLLING_SERVICE_TYPE);
		sd.setOwnership("PollingOwner");
		sd.addOntologies(PollingServicesOntology.getInstance().getName());
		
		// WSIG properties
		sd.addProperties(new Property(WSIG_FLAG, "true"));

		// Service name
		String wsigServiceName = "Polling";
		if (args.length >= 1) {
			wsigServiceName = (String) args[0];
		}
		log.info("Service name: " + wsigServiceName);
		sd.setName(wsigServiceName);

		// Mapper
/*		boolean isMapperPresent = false;
		if (args.length >= 2) {
			isMapperPresent = Boolean.parseBoolean((String) args[1]);
		}
*/		// log.info("Mapper present: " + isMapperPresent);
/*		if (isMapperPresent) {
			sd.addProperties(new Property(WSIG_MAPPER, "ir.mod.tavana.toranj.wsig.examples.MathOntologyMapper"));
		}
*/
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

//		registerBCAgent(getLocalName());

		// Add communication behavior
		this.addBehaviour(new OfferRequestServer(this));
	}

/*	private void registerBCAgent(String localName) {
		// TODO Auto-generated method stub
		List<AID> bcList = search(BroadcastingServiceAgent.SERVICE_TYPE);

		if (bcList!= null && bcList.size()!=0){
			ACLMessage registerationReq = new ACLMessage(ACLMessage.REQUEST);
			registerationReq.addUserDefinedParameter(BroadcastingServiceAgent.APPLICATION, BroadcastingServiceAgent.REGISTERATION);
			registerationReq.addUserDefinedParameter(BroadcastingServiceAgent.LOCAL_NAME, getLocalName());
			registerationReq.addReceiver(bcList.get(0));
			send(registerationReq);
		}
		return;
	}
*/
	private class OfferRequestServer extends CyclicBehaviour {
		private MessageTemplate template = MessageTemplate.MatchOntology(PollingServicesOntology.getInstance().getName());

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
					if (action instanceof PollingMsg) {
						serveMsgAction((PollingMsg) action, actExpr, msg);
					} else if(action instanceof MonitoringMsg) {
						MonitoringMsg mmsg = (MonitoringMsg) action;
						sendNotification(actExpr, msg, ACLMessage.INFORM, pollingDB.getPollingQuestion(mmsg.getQid()));
					}
				} catch (Exception e) {
					log.error("Exception: " + e.getMessage(), e);
					e.printStackTrace();
				}
			} else {
				block();
			}
		}
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

	private void serveMsgAction(PollingMsg msg_class, Action actExpr, ACLMessage request) throws Exception {
		// send the message to the all receivers and waiting for the response
		String result = "";
		this.addBehaviour(new MsgPerformer(msg_class, actExpr, request, ACLMessage.INFORM, result));
	}

	private class MsgPerformer extends Behaviour {

		private PollingMsg msg_class; // the message should be delivered
		private Action actExpr;
		private ACLMessage request;
		private int performative;
		private Object result;

		private MessageTemplate mt; // The template to receive replies
		private int step = 0;
		private int repliesCnt = 0;
		private String question;
		private float timeout;
		private String[] options;
		private AID[] scope;
		private String[] answers;
		private int qid = -1;

		public MsgPerformer(PollingMsg msg_class, Action actExpr, ACLMessage request, int performative, Object result)
				throws Exception {
			this.msg_class = msg_class;
			this.actExpr = actExpr;
			this.request = request;
			this.performative = performative;
			this.result = result;
			
//			factory = new AnnotationConfiguration().
//	                   configure().
//	                   //addPackage("com.xyz") //add package if used.
//	                   addAnnotatedClass(ir.mod.tavana.toranj.entities.PollBean.class).
//	                   buildSessionFactory();


			// phase1: get the message
			java.util.ArrayList<AID> users = search(AVATAR_TYPE);
			if (!msg_class.getTimeout().equals("*"))
				timeout = 1000 * Float.valueOf(msg_class.getTimeout());
			else
				timeout = Float.MAX_VALUE;
			question = msg_class.getQuestion();
			options = msg_class.getOptions().split(" ");
			if (msg_class.getScope().equals("*")) {
				scope = new AID[users.size()];
				for (int i = 0; i < users.size(); i++)
					scope[i] = users.get(i);
			} else if (msg_class.getScope().startsWith("R:")) {
				int r = Integer.parseInt(msg_class.getScope().split(":")[1]);
				scope = new AID[r];
				int size = users.size();
				HashSet<Integer> indexes = new HashSet<Integer>();
				Random rand = new Random();
				for (int i = 0; i < r; i++) {
					int curr_index = rand.nextInt(size - 1);
					while (indexes.contains(curr_index)) {
						curr_index = rand.nextInt(size - 1);

					}
					scope[i] = users.get(curr_index);
					indexes.add(curr_index);
				}
			} else {
				String[] user_names = msg_class.getScope().split(" ");
				scope = new AID[user_names.length];
				for (int i = 0; i < user_names.length; i++) {
					AID user = new AID(user_names[i], AID.ISLOCALNAME);
					if (users.contains(user))
						scope[i] = user;
				}
			}
			answers = new String[scope.length];
			for (int i = 0; i < answers.length; i++)
				answers[i] = "na";
			StringBuffer sscope = new StringBuffer();
			for (AID a : scope)
				sscope.append(a.getLocalName() + "-");
			sscope.delete(sscope.length() - 1, sscope.length());
			StringBuffer soptions = new StringBuffer();
			for (String op : options)
				soptions.append(op + "-");
			soptions.delete(soptions.length() - 1, soptions.length());

			// log questions and options into polling database
			qid = pollingDB.addPollingQuestion(question, 0, sscope.toString(), timeout, soptions.toString());
		}

		public void action() {
			switch (step) {
			case 0:
				// phase2: send QOS (Question-Options-Scope) to the targets
				start = System.currentTimeMillis();

				ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
				request.setOntology(AVATAR_ONTOLOGY);
				request.setLanguage(codec.getName());
				
				PollRequest pollRequest = new PollRequest();
				pollRequest.setQuestion(question);
				pollRequest.setOption(msg_class.getOptions());

				PollRequestForward pollRequestForward = new PollRequestForward();
				pollRequestForward.setPollRequest(pollRequest);
				
				Action act = new Action(getAID(), pollRequestForward);
				
				try {
					getContentManager().fillContent(request, act);
				} catch (CodecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OntologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
/*				
				request.setContent(question);
				request.addUserDefinedParameter("options",
						msg_class.getOptions());
				request.addUserDefinedParameter("scope", msg_class.getScope());
*/
				for (AID target : scope) {
					request.addReceiver(target);
				}
				request.setConversationId("polling-delivery");
				request.addUserDefinedParameter("replyWith",
						"cfp" + System.currentTimeMillis());
				request.setReplyWith("cfp" + System.currentTimeMillis());
				myAgent.send(request);

				// Prepare the template to get response
				mt = MessageTemplate
						.and(MessageTemplate
								.MatchConversationId("polling-delivery"),
								MessageTemplate.MatchInReplyTo(request
										.getReplyWith()));
				step = 1;
				break;
			case 1:
				// Receive all QAS (Question-Answers-Scope) from user agents
				long elapsedTimeMillis = System.currentTimeMillis() - start;
				float elapsedTimeSec = elapsedTimeMillis / 1000F;
				if (elapsedTimeSec >= timeout) {
					StringBuffer buffer = new StringBuffer();
					for (String answer : this.answers)
						buffer.append(answer + ":::");
					buffer.delete(buffer.length() - 3, buffer.length());
					this.result = buffer.toString();
					sendNotification(this.actExpr, this.request,
							this.performative, this.result);
					step = 2;
				}

				ACLMessage reply = myAgent.receive(mt);
				if (reply != null) {
					// Reply received
					if (reply.getPerformative() == ACLMessage.CONFIRM) {
						String answers = reply.getContent();
						String id = reply.getSender().getLocalName();
						this.answers[repliesCnt] = id + ":" + answers;
						pollingDB.addPollingAnswer(qid, id, answers);
					}
					repliesCnt++;
					if (repliesCnt >= scope.length) {
						// We received all replies
						pollingDB.updatePollingQuestion(qid, 1);
						StringBuffer buffer = new StringBuffer();
						for (String answer : this.answers)
							buffer.append(answer + ":::");
						buffer.delete(buffer.length() - 3, buffer.length());
						this.result = buffer.toString();
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
			pollingDB.shutdown();
		} catch (Exception e) {
			log.error(e);
		}

		log.debug("A CommunicationS-Agent is taken down now.");
	}
}