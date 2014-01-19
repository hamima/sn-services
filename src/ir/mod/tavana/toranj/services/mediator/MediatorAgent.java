package ir.mod.tavana.toranj.services.mediator;

import ir.mod.tavana.toranj.services.avatar.polling.Poll;
import ir.mod.tavana.toranj.services.polling.PollingMsg;
import ir.mod.tavana.toranj.services.polling.PollingServiceAgent;
import ir.mod.tavana.toranj.services.polling.PollingServicesOntology;

import java.util.Iterator;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import jade.Boot;
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
import jade.core.behaviours.CyclicBehaviour;
import jade.core.persistence.PersistenceHelper;
import jade.core.persistence.PersistenceManager;
import jade.core.persistence.Savable;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FIPAManagementOntology;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.persistence.LoadAgent;
import jade.domain.persistence.PersistenceOntology;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.ArrayList;

public class MediatorAgent extends Agent implements MediatorVocabulary, Savable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String WSIG_FLAG = "wsig";
	public static final String WSIG_MAPPER = "wsig-mapper";
	public static final String WSIG_PREFIX = "wsig-prefix";

	private Logger log = Logger.getLogger(PollingServiceAgent.class.getName());
	public static AID myAID = null;
	private SLCodec codec = new SLCodec();
	
	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		
		log.setLevel(Level.FATAL);
		log.info( "A Mediator-Agent is starting...");
		log.info("Agent name: " + getLocalName());

		// Get agent arguments
		Object[] args = getArguments();

		// Register codec onto
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(FIPAManagementOntology.getInstance());
		getContentManager().registerOntology(PersistenceOntology.getInstance());
		getContentManager().registerOntology(MediatorOntology.getInstance());
		getContentManager().registerOntology(PollingServicesOntology.getInstance());
		
		// Persistence Work
		PersistenceHelper helper;
/*		try {
			helper = (PersistenceHelper) getHelper(PersistenceHelper.NAME);
			helper.registerSavable(this);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
*/		// Prepare a DFAgentDescription

		DFAgentDescription dfad = new DFAgentDescription();
		dfad.setName(this.getAID());
		dfad.addLanguages(codec.getName());
		dfad.addProtocols(FIPANames.InteractionProtocol.FIPA_REQUEST);
		ServiceDescription sd;
		sd = new ServiceDescription();
		sd.addLanguages(codec.getName());
		sd.addProtocols(FIPANames.InteractionProtocol.FIPA_REQUEST);
		sd.setType("MediatorS-Agent");
		sd.setOwnership("MediatorOwner");
		sd.addOntologies(MediatorOntology.getInstance().getName());
		
		// WSIG properties
		sd.addProperties(new Property(WSIG_FLAG, "true"));

		// Service name
		String wsigServiceName = WSIG_SERVICENAME ;
		if (args.length >= 1) {
			wsigServiceName = (String) args[0];
		}
		
		log.info("Service name: " + wsigServiceName);
		sd.setName(wsigServiceName);

		// Mapper
		boolean isMapperPresent = false;
		if (args.length >= 2) {
			isMapperPresent = Boolean.parseBoolean((String) args[1]);
		}
		// log.info("Mapper present: " + isMapperPresent);
		if (isMapperPresent) {
			sd.addProperties(new Property(WSIG_MAPPER, "ir.mod.tavana.toranj.services.mediator.MediatorMapper"));
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
			e.printStackTrace();
			doDelete();
		}

		this.addBehaviour(new MediatorBehavior(this));		
	}
	
	private class MediatorBehavior extends CyclicBehaviour {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private MessageTemplate mediatorTemplate = MessageTemplate.MatchOntology(ONTOLOGY_NAME);
		private ACLMessage currentPollACLMessage; 
		
		
		public MediatorBehavior(Agent agent) {
			// TODO Auto-generated constructor stub
			super(agent);
		}

		@Override
		public void action() {
			// TODO Auto-generated method stub
			ACLMessage msg = myAgent.receive(mediatorTemplate);
			
			if (msg != null) {
				Action actExpr;
					try {
						actExpr = (Action) myAgent.getContentManager().extractContent(msg);
						AgentAction action = (AgentAction) actExpr.getAction();
						if (action instanceof Register) {
							serveRegisterAction((Register) action, actExpr, msg);
						} else if (action instanceof DeRegister) {
							serveDeRegisterAction((DeRegister) action, actExpr, msg);
						} else if (action instanceof LogIn) {
							serveLogInAction((LogIn) action, actExpr, msg);
						} else if (action instanceof Poll) {
							servePollAction((Poll) action, actExpr, msg);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
			} else {
				block();
			}
		}
		
		private void servePollAction(Poll action, Action actExpr, ACLMessage msg) {
			// TODO Auto-generated method stub
			
			PollingMsg pollingMsg = new PollingMsg();
			pollingMsg.setOptions(action.getOptions()); 
			pollingMsg.setQuestion(action.getQuestion());
			pollingMsg.setScope(action.getScope());
			pollingMsg.setTimeout(action.getTimeout());
			
			java.util.List<AID> pollingService = search(POLLING_TYPE);
			AID myPollService = pollingService.get(0);
			ACLMessage forwardedPollingMsg = new ACLMessage(ACLMessage.REQUEST);
			forwardedPollingMsg.setOntology(POLLING_ONTOLOGY_NAME);
			forwardedPollingMsg.addReceiver(myPollService);
			forwardedPollingMsg.setLanguage(codec.getName());
			
			try {
				myAgent.getContentManager().fillContent(forwardedPollingMsg, pollingMsg);
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			send(forwardedPollingMsg);
		}


		
		private void serveLogInAction(LogIn action, Action actExpr, ACLMessage msg) {

			log.debug("MediatorAgent.serveLogInAction");
			
			ACLMessage loadMessage = new ACLMessage(ACLMessage.REQUEST);
			loadMessage.addReceiver(getAMS());
			loadMessage.setOntology(PersistenceOntology.NAME);
			java.util.ArrayList<jade.core.AID> searchAvatar = searchAvatar(action.getUsername());
			LoadAgent loadAgent = new LoadAgent();
			loadAgent.setAgent(searchAvatar.get(0));
			loadAgent.setRepository(PersistenceManager.DEFAULT_REPOSITORY);
			
			send(loadMessage);
		}

		private void serveDeRegisterAction(DeRegister action, Action actExpr, ACLMessage msg) {
			// TODO Auto-generated method stub

			log.debug("MediatorAgent.serveDeRegisterAction");
			ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
			
//			float result = diff.getFirstElement() - diff.getSecondElement();
			String result = "";
			sendNotification(actExpr, msg, ACLMessage.INFORM, result);
			
		}
		
		private void serveRegisterAction(Register action, Action actExpr, ACLMessage msg) {
			// TODO Auto-generated method stub
			
			log.debug("MediatorAgent.serveDeRegisterAction");
			
			String[] local_args = { "-gui", "-services", "jade.core.persistence.PersistenceService;jade.core.event.NotificationService;jade.core.mobility.AgentMobilityService" ,
					"-meta-db", "JADE_persistence.properties","-container",
					"AvatarSAgent" + action.getProfile().getName() + ":ir.mod.tavana.toranj.services.avatar.AvatarAgent(AvatarFunctions" + action.getProfile().getName() + " true)",
					"-name", "WSIGPlatform", "-local_name", action.getProfile().getName()};
			
			new Boot(local_args);
			
			String result = "http://localhost:8080/wsig-examples/ws/AvatarFunctions" + action.getProfile().getName() + "?WSDL";
			sendNotification(actExpr, msg, ACLMessage.INFORM, result);

		}
		
		private void sendNotification(Action actExpr, ACLMessage request, int performative, Object result) {
			// TODO Auto-generated method stub
			// Send back a proper reply to the requester
			ACLMessage reply = request.createReply();
			if (performative == ACLMessage.INFORM) {
				reply.setPerformative(ACLMessage.INFORM);
				try {
					ContentElement ce = null;
					if (result != null) {
						// If the result is a java.util.List, convert it into a jade.util.leap.List t make the ontology "happy"
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
	}
	
	private java.util.ArrayList<AID> searchAvatar (String my_name) {
		java.util.ArrayList<AID> providers = new java.util.ArrayList<AID>();
		
		DFAgentDescription templateDFAD = new DFAgentDescription();
		ServiceDescription templateSD = new ServiceDescription();
		templateSD.addProperties(new Property(AVATAR_ID, my_name));
		templateSD.setType(AVATAR_TYPE);
		
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
					if (sd.getType().equals(AVATAR_TYPE)) {
						providers.add(provider);
					}

				}
			}
		} else {
		}
		return providers;
	}
	
	private java.util.List<AID> search (String service_type) {
		java.util.List<AID> providers = new java.util.ArrayList<AID>();
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
				// " did not find any \"communication\" service");
			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		return providers;
	}

	@Override
	public void afterLoad() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterReload() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterThaw() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeFreeze() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeReload() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

}
