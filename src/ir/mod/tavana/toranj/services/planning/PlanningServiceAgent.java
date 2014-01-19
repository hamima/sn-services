package ir.mod.tavana.toranj.services.planning;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import jade.content.AgentAction;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FIPAManagementOntology;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.persistence.PersistenceOntology;
import jade.lang.acl.ACLMessage;
import jade.util.leap.ArrayList;

public class PlanningServiceAgent extends Agent implements PlanningVocabulary {
	
	public static final String WSIG_FLAG = "wsig";
	public static final String WSIG_MAPPER = "wsig-mapper";
	public static final String WSIG_PREFIX = "wsig-prefix";

	private Logger log = Logger.getLogger(PlanningServiceAgent.class.getName());
	public static AID myAID = null;
	private SLCodec codec = new SLCodec();
	
	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		
		log.setLevel(Level.FATAL);
		log.info( "A Planning-Agent is starting...");
		log.info("Agent name: " + getLocalName());

		// Get agent arguments
		Object[] args = getArguments();

		// Register codec onto
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(FIPAManagementOntology.getInstance());
		getContentManager().registerOntology(PersistenceOntology.getInstance());
		getContentManager().registerOntology(PlanningOntology.getInstance());
		
		// Prepare a DFAgentDescription

		DFAgentDescription dfad = new DFAgentDescription();
		dfad.setName(this.getAID());
		dfad.addLanguages(codec.getName());
		dfad.addProtocols(FIPANames.InteractionProtocol.FIPA_REQUEST);
		ServiceDescription sd;
		sd = new ServiceDescription();
		sd.addLanguages(codec.getName());
		sd.addProtocols(FIPANames.InteractionProtocol.FIPA_REQUEST);
		sd.setType(PLANNING_SERVICE_TYPE);
		sd.addOntologies(PlanningOntology.getInstance().getName());
		
		// WSIG properties
		sd.addProperties(new Property(WSIG_FLAG, "true"));

		// Service name
		String wsigServiceName = WSIG_SERVICENAME ;
		if (args.length >= 1) {
			wsigServiceName = (String) args[0];
		}
		
		log.info("Service name: " + wsigServiceName);
		sd.setName(wsigServiceName);

		dfad.addServices(sd);
		
		// DF registration
		try {
			DFService.register(this, dfad);
		} catch (Exception e) {
			log.error("Problem during DF registration", e);
			e.printStackTrace();
			doDelete();
		}

		this.addBehaviour(new PlanBehavior(this));		
	}
	 private class PlanBehavior extends CyclicBehaviour {

		public PlanBehavior(Agent agent) {
			super(agent);
		}
		
		@Override
		public void action() {
		// TODO Auto-generated method stub
			ACLMessage receivedMsg = receive();
			if (receivedMsg != null){
				Action actExpr;
				try {
					actExpr = (Action) myAgent.getContentManager().extractContent(receivedMsg);
					AgentAction action = (AgentAction) actExpr.getAction();
					if (action instanceof Plan) {
						String result = servePlanService(actExpr, (Plan) action, receivedMsg);
						sendNotification(actExpr, receivedMsg, ACLMessage.INFORM, result);
					}
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
			} else {
				block();
			}
			
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
		
		private String servePlanService(Action actExpr, Plan action,
				ACLMessage receivedMsg) {
			// TODO Auto-generated method stub
			
			int numberOfAgents = action.getNumberOfAgents();
			int numberOfSteps = action.getNumberOfSteps();
			
			Buffer buffer = new Buffer(numberOfSteps, numberOfAgents);
			Soccer sc = buffer.getsocer();
			ExecutorService executor = Executors.newCachedThreadPool();

			for (int index = 0; index < numberOfAgents; index++){
				executor.execute(new Planning(index, numberOfAgents , numberOfSteps, buffer, sc));
			}
			String returnValue = buffer.finalPlan();
			executor.shutdown();
			return returnValue;

		}
		 
	 }

}
