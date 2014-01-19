package ir.mod.tavana.toranj.services.avatar;


import ir.mod.tavana.toranj.Enum.Operator;
import ir.mod.tavana.toranj.Enum.YesOrNo;
import ir.mod.tavana.toranj.services.avatar.polling.AllPollRequest;
import ir.mod.tavana.toranj.services.avatar.polling.Poll;
import ir.mod.tavana.toranj.services.avatar.polling.PollRequest;
import ir.mod.tavana.toranj.services.avatar.polling.PollRequestForward;
import ir.mod.tavana.toranj.services.avatar.polling.PollRequestList;
import ir.mod.tavana.toranj.services.avatar.polling.ResponsePoll;
import ir.mod.tavana.toranj.services.avatar.search.AddProperty;
import ir.mod.tavana.toranj.services.avatar.search.Search;
import ir.mod.tavana.toranj.services.avatar.search.SearchForwardRequest;
import ir.mod.tavana.toranj.services.broadcasting.BCRequest;
import ir.mod.tavana.toranj.services.mediator.Profile;
import ir.mod.tavana.toranj.services.polling.PollingMsg;
import ir.mod.tavana.toranj.services.polling.PollingServiceAgent;
import ir.mod.tavana.toranj.services.polling.PollingServicesOntology;
import ir.mod.tavana.toranj.services.search.SearchOntology;
import ir.mod.tavana.toranj.websocket.server.ExampleClient;
import ir.mod.tavana.toranj.websocket.server.WordGameClientEndpoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;

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
import jade.core.ServiceException;
import jade.core.behaviours.Behaviour;
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
import jade.domain.persistence.PersistenceOntology;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.Properties;

public class AvatarAgent extends Agent implements AvatarVocabulary, Savable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(PollingServiceAgent.class.getName());
	public static AID myAID = null;
	private SLCodec codec = new SLCodec();

	private Profile profile;
	private Map<String, String> propertyMap = new HashMap<String, String>();

	public AvatarAgent (Profile profile) {
		super();
		this.profile = profile;
	}

	public AvatarAgent() {
		super();
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		log.setLevel(Level.FATAL);
		log.info( "An Avatar-Agent is starting...");
		log.info("Agent name: " + getLocalName());

		// Get agent arguments
		Object[] args = getArguments();
		Properties bootProperties = getBootProperties();

		// Register codec/onto
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(FIPAManagementOntology.getInstance());
		getContentManager().registerOntology(PersistenceOntology.getInstance());
		getContentManager().registerOntology(AvatarOntology.getInstance());
		getContentManager().registerOntology(PollingServicesOntology.getInstance());
		getContentManager().registerOntology(SearchOntology.getInstance());

		// Prepare a DFAgentDescription
		DFAgentDescription dfad = new DFAgentDescription();
		dfad.setName(this.getAID());
		dfad.addLanguages(codec.getName());
		dfad.addProtocols(FIPANames.InteractionProtocol.FIPA_REQUEST);
		ServiceDescription sd;
		sd = new ServiceDescription();
		sd.addLanguages(codec.getName());
		sd.addProtocols(FIPANames.InteractionProtocol.FIPA_REQUEST);
		sd.setName(bootProperties.getProperty(AVATAR_LOCAL_NAME));
		sd.setType(AVATAR_TYPE);
		sd.setOwnership("AvatarOwner");
		sd.addOntologies(AvatarOntology.getInstance().getName());
		sd.addOntologies(POLLING_ONTOLOGY_NAME);
		sd.addOntologies(SEARCH_ONTOLOGY_NAME);

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
			sd.addProperties(new Property(WSIG_MAPPER, "ir.mod.tavana.toranj.services.avatar.AvatarMapper"));
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

		this.addBehaviour(new PlayerBehavior(this));
	}

	private class PlayerBehavior extends CyclicBehaviour{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private MessageTemplate avatarTemplate = MessageTemplate.MatchOntology(ONTOLOGY_NAME);
		private List<PollRequest> pollRequests = new ArrayList<PollRequest>();

		public PlayerBehavior(Agent agent){
			super(agent);
		}

		@Override
		public void action() {
			// TODO Auto-generated method stub
			ACLMessage msg = myAgent.receive(avatarTemplate);

			if (msg != null) {
				Action actExpr;
					try {
						actExpr = (Action) myAgent.getContentManager().extractContent(msg);
						AgentAction action = (AgentAction) actExpr.getAction();
						if (action instanceof Poll) {
							servePollAction((Poll) action, actExpr, msg);
						} else if (action instanceof BroadCast) {
							serveBroadcastAction((BroadCast) action, actExpr, msg);
						} else if (action instanceof Consensus) {
							serveConsensusAction((Consensus) action, actExpr, msg);
						} else if (action instanceof Search) {
							serveSearchAction((Search) action, actExpr, msg);
						} else if (action instanceof SearchForwardRequest) {
							serveSearchQuestion((SearchForwardRequest) action, actExpr, msg);
						} else if (action instanceof LogOff) {
							serveLogOffAction((LogOff) action, actExpr, msg);
						} else if (action instanceof ListOfAvatars) { 
							serveAllAvatars((ListOfAvatars) action, actExpr, msg);
						} else if (action instanceof PollRequestForward) {
							addPollingRequest(msg);
						} else if (action instanceof AllPollRequest){
							getPollingRequests((AllPollRequest)action, actExpr, msg);
						} else if (action instanceof ResponsePoll) {
							serveResponsePoll((ResponsePoll)action, actExpr, msg);
						} else if (action instanceof AddProperty) {
							serveAddProperty((AddProperty)action, actExpr, msg);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			} else {
				block();
			}
		}
		
		private void serveSearchQuestion(SearchForwardRequest action, Action actExpr, ACLMessage msg) {
			// TODO Auto-generated method stub
			
			String value = "";
			String result = YesOrNo.NO.toString();
			String compValue = action.getValue();
			if(action.getKey() != null && !action.getKey().equals(""))
				value = propertyMap.get(action.getKey());
			
			String operator = action.getOperator();
			if(operator.equals(Operator.Eq)) {
				if(compValue.equals(value))
					result = YesOrNo.YES.toString();
			} else if (operator.equals(Operator.NEq)) {
				if(!compValue.equals(value))
					result = YesOrNo.YES.toString();
			} else if (operator.equals(Operator.GrEqTh)) {
				if(Integer.parseInt(compValue) >= Integer.parseInt(value))
					result = YesOrNo.YES.toString();
			} else if (operator.equals(Operator.GrTh)) {
				if(Integer.parseInt(compValue) > Integer.parseInt(value))
					result = YesOrNo.YES.toString();
			} else if (operator.equals(Operator.LeEqTh)) {
				if(Integer.parseInt(compValue) <= Integer.parseInt(value))
					result = YesOrNo.YES.toString();
			} else if (operator.equals(Operator.LeTh)) {
				if(Integer.parseInt(compValue) < Integer.parseInt(value))
					result = YesOrNo.YES.toString();
			}
			
			sendNotification(actExpr, msg, ACLMessage.INFORM, result);
		}

		private void serveAddProperty(AddProperty action, Action actExpr, ACLMessage msg) { 
			// TODO Auto-generated method stub
			propertyMap.put(action.getKey(), action.getValue());
		}

		private void serveResponsePoll(ResponsePoll action, Action actExpr, ACLMessage msg) {
			// TODO Auto-generated method stub
			
		}
		
		private void sendWebSocketMessage(String msg){
			ExampleClient c;
			try {
				c = new ExampleClient( new URI( "ws://localhost:8887" ), new Draft_10() );
				c.connect();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void addPollingRequest(ACLMessage msg){
			PollRequest pollRequest = new PollRequest();
			pollRequest.setQuestion(msg.getContent());
			pollRequest.setOption(msg.getUserDefinedParameter("options"));
			
			pollRequests.add(pollRequest);
		}
		
		private void getPollingRequests(AllPollRequest action, Action actExpr, ACLMessage msg) {
			PollRequestList pollRequestList = new PollRequestList();
			
			for (PollRequest polReq: pollRequests) {
				pollRequestList.getPollRequestListList().add(polReq);	
			}
			sendNotification(actExpr, msg, ACLMessage.INFORM, pollRequestList);
			pollRequests.removeAll(pollRequests);
		}

		private void serveLogOffAction(LogOff action, Action actExpr, ACLMessage msg) {
			// TODO Auto-generated method stub
			PersistenceHelper helper;
			try {
				helper = (PersistenceHelper) myAgent.getHelper(PersistenceHelper.NAME);
				helper.save(PersistenceManager.DEFAULT_REPOSITORY);
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void serveAllAvatars(ListOfAvatars action, Action actExpr, ACLMessage msg) {
			List<AID> allAvatars = search(AVATAR_TYPE);
			String listOfCurrentAvatars = "";
			for (AID currentAid: allAvatars) {
				listOfCurrentAvatars += currentAid.getLocalName() + " ";
			}
			String result = listOfCurrentAvatars.substring(0, listOfCurrentAvatars.length()-2);
			sendNotification(actExpr, msg, ACLMessage.INFORM, result);
		}

		private void serveSearchAction(Search action, Action actExpr, ACLMessage msg) {
			// TODO Auto-generated method stub
			
			List<AID> search_providers = search(AVATAR_TYPE);
			ACLMessage searchMsg = new ACLMessage(msg.getPerformative());
			searchMsg.setLanguage(codec.getName());
			searchMsg.setOntology(SEARCH_ONTOLOGY_NAME);

			SearchForwardRequest searchForwardRequest = new SearchForwardRequest();
			searchForwardRequest.setKey(action.getKey());
			searchForwardRequest.setValue(action.getValue());
			searchForwardRequest.setOperator(action.getOperator());

			for(AID avatar: search_providers){
				searchMsg.addReceiver(avatar);
			}

			try {
				myAgent.getContentManager().fillContent(searchMsg, searchForwardRequest);
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			myAgent.send(searchMsg);
			
			myAgent.addBehaviour(new SearchResponsePerformer(search_providers,action, actExpr, msg));
			
		}
		
		private class SearchResponsePerformer extends Behaviour {

			private int numberOfReceived= 0;
			
			private List<AID> avatars;
			private Search action;
			private Action actExpr;
			private ACLMessage msg;
			
			public SearchResponsePerformer(List<AID> avatars,Search action, Action actExpr, ACLMessage msg ) {
				
				this.avatars = avatars;
				this.action = action;
				this.actExpr = actExpr;
				this.msg = msg;
			}
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				long requestTime = System.currentTimeMillis();
				String result = "";
				while(System.currentTimeMillis()-requestTime < 1000000 || numberOfReceived < avatars.size()){
					ACLMessage searchReceive = myAgent.blockingReceive(1000000);
					numberOfReceived++;
					String yesOrNo = searchReceive.getContent();
					if(yesOrNo.equals(YesOrNo.YES)){
						result += searchReceive.getSender().getLocalName() ;
						result += "***";
					}
				}
				numberOfReceived = 0;
				sendNotification(actExpr, msg, ACLMessage.INFORM, result);
			}

			@Override
			public boolean done() {
				// TODO Auto-generated method stub
				return false;
			}
			
		}

		private void serveConsensusAction(Consensus action, Action actExpr,	ACLMessage msg) {
			// TODO Auto-generated method stub

		}

		private void serveBroadcastAction(BroadCast action, Action actExpr,	ACLMessage msg) {
			// TODO Auto-generated method stub
			java.util.List<AID> providers = search(BROADCASTING_TYPE);
			ACLMessage broadcastingMsg = new ACLMessage(action.getPerformance());
			broadcastingMsg.setLanguage(codec.getName());
			broadcastingMsg.setOntology(BROADCASTING_ONTOLOGY_NAME);
			BCRequest bcReq = new BCRequest();

			if (providers.size() > 0) {
				broadcastingMsg.addReceiver(providers.get(0));
				if (action.getQuery() != null && !action.getQuery().equals("")) {
					bcReq.setReceiverQuery(action.getQuery());
				}
			}
			myAgent.send(broadcastingMsg);
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
			forwardedPollingMsg.setSender(myAgent.getAID());
			
			Action act = new Action(getAID(), pollingMsg);
			
			try {
				myAgent.getContentManager().fillContent(forwardedPollingMsg, act);
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			send(forwardedPollingMsg);
		}
		
		private void sendAsyncNotification(Object result) {
			ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
			
			List<AID> wsigAgents = search("WSIG Agent");
			reply.addReceiver(wsigAgents.get(0));
			reply.addUserDefinedParameter(ACLMessage.IGNORE_FAILURE, "true");
			send(reply);
		}
		
		private void sendNotification (Action actExpr, ACLMessage request, int performative, Object result) {
			// TODO Auto-generated method stub
			// Send back a proper reply to the requester
			ACLMessage reply = request.createReply();
			if (performative == ACLMessage.INFORM) {
				reply.setPerformative(ACLMessage.INFORM);
				try {
					ContentElement ce = null;
					if (result != null) {
						// If the result is a java.util.List, convert it into a jade.util.leap.List t make the ontology "happy"
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
							providers.add(provider);
						}
					}
				}
			} else {
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
