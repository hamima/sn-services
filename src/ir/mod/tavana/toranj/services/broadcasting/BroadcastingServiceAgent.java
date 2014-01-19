package ir.mod.tavana.toranj.services.broadcasting;

import ir.mod.tavana.toranj.services.dns.DNSServiceAgent;

import java.util.Iterator;
import java.util.List;


import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class BroadcastingServiceAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String SERVICE_NAME = "Broadcasting";
	public static final String SERVICE_TYPE = "Broadcastings-Agent";
	public static final String CONVERSATION_ID_BC_REQUEST = "broadcasting_request";
	public static final String WSIG_FLAG = "wsig";

/*	
 * public static final String isBROADCAST = "isBROADCAST";
	public static final String RECEIVERQUERY = "RECEIVERQUERY";
	public static final String YES = "YES";
	public static final String NO = "NO";
	public static final String TYPE = "TYPE";

	public static final String LOCAL_NAME = "LOCAL_NAME";
	public static final String ACTUAL_SENDER = "ACTUAL_SENDER";
	public static final String LIST_OF_RECEIVERS = "LIST_OF_RECEIVERS";
*/
	public static final String CONVERSATION_DNS = "CONVERSATION_DNS";

	private SLCodec codec = new SLCodec();
	
	protected void setup() {

		getContentManager().registerLanguage(codec, FIPANames.ContentLanguage.FIPA_SL0);
		getContentManager().registerOntology(BroadCastingServicesOntology.getInstance());

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		dfd.addLanguages(codec.getName());
		dfd.addProtocols(FIPANames.InteractionProtocol.FIPA_REQUEST);
		
		ServiceDescription sd = new ServiceDescription();
		sd.addLanguages(codec.getName());
		sd.addProtocols(FIPANames.InteractionProtocol.FIPA_REQUEST);
		sd.setType(SERVICE_TYPE);
		sd.setName(SERVICE_NAME);
		sd.setOwnership("BroadcastingOwner");
		sd.addOntologies(BroadCastingServicesOntology.getInstance().getName());
		
		// WSIG properties
		sd.addProperties(new Property(WSIG_FLAG, "true"));

		dfd.addServices(sd);

		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		System.out.println("!!Use language = " + FIPANames.ContentLanguage.FIPA_SL0 + " & ontology = " +
                BroadCastingServicesOntology.getInstance().getName());

		this.addBehaviour(new ProcessMessageHandler());
	}

	private class ProcessMessageHandler extends Behaviour {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private int step = 0;
		private int numberOfSents = 0;
		private int numberOfReceived = 0;
		private ACLMessage msgForwarding;
		private ACLMessage msgReceived;
		private long sendingTime = 0;
		private long treshold = 10000;
		
		private BCRequest bcRequset;
		private Object BCRequestObject;
		
		private MessageTemplate mtLang = MessageTemplate.MatchLanguage(FIPANames.ContentLanguage.FIPA_SL0);
		private MessageTemplate mtOnto = MessageTemplate.MatchOntology(BroadCastingServicesOntology.ONTOLOGY_NAME);
		private MessageTemplate mtDns  = MessageTemplate.MatchConversationId(CONVERSATION_DNS);
		
		private AID actualSender;

		public ProcessMessageHandler() {
			super();
		}

		@Override
		public void action() {
			// TODO Auto-generated method stub
			
			switch (step) {
			
			case 0:				
				msgReceived = (ACLMessage) myAgent.blockingReceive(MessageTemplate.and(mtOnto, mtLang));
				try {
					BCRequestObject = myAgent.getContentManager().extractContent(msgReceived);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				if (BCRequestObject != null && BCRequestObject instanceof BCRequest) {

					bcRequset = (BCRequest) BCRequestObject;

					actualSender = msgReceived.getSender();				
					msgForwarding = new ACLMessage(msgReceived.getPerformative());
					msgForwarding.setEncoding(msgReceived.getEncoding());
					msgForwarding.setLanguage(msgReceived.getLanguage());
					msgForwarding.setReplyWith(msgReceived.getReplyWith());

					if (bcRequset.getApplication().equals(BroadCastingServicesOntology.SEND_BROADCASTING)) {
							if (bcRequset.isBroadcast()) {
								List<AID> receiversBC = searchAll();

       							for (AID target : receiversBC) {
       								msgForwarding.addReceiver(new AID(target.getLocalName(),AID.ISLOCALNAME));
       							}
       							msgForwarding.removeReceiver(getAID());
								step = 2;
							} else {
								if (bcRequset.getReceivers().size() > 0) {
									Iterator bcRequestReceiverItr = bcRequset.getReceivers().iterator();
									while (bcRequestReceiverItr.hasNext())
										msgForwarding.addReceiver((AID) bcRequestReceiverItr.next());
								}

								if (!bcRequset.getReceiverQuery().equals("")) {
//							 		If the Query is not "", send to DNS service to determine the rest of recipient list
									List<AID> dnsReceivers = search(DNSServiceAgent.SERVICE_TYPE);

									if (dnsReceivers.size() > 0) /* There is some DNS service available in environment */ {

										ACLMessage dnsMessage = new ACLMessage(ACLMessage.REQUEST);
										dnsMessage.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
										dnsMessage.setSender(getAID());
										dnsMessage.setConversationId(CONVERSATION_DNS);

										BCDNSRequest bcdnsRequestMsg = new BCDNSRequest();
										bcdnsRequestMsg.setReceiverQuery(bcRequset.getReceiverQuery());

										dnsMessage.addReceiver(dnsReceivers.get(0));
										dnsMessage.setOntology(BroadCastingServicesOntology.ONTOLOGY_NAME);
										try {
											myAgent.getContentManager().fillContent(dnsMessage, bcdnsRequestMsg);
										} catch (CodecException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (OntologyException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										myAgent.send(dnsMessage);
										step = 1;
									} else {
										ACLMessage noDNSAgentMsg = msgReceived.createReply();
										noDNSAgentMsg.setOntology(BroadCastingServicesOntology.ONTOLOGY_NAME);
										BCReply bcReplyMsg = new BCReply();
										bcReplyMsg.setBcReplyMsg("No DNS service provider exists in network!");
										try {
											myAgent.getContentManager().fillContent(noDNSAgentMsg, bcReplyMsg);
										} catch (CodecException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (OntologyException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										send(noDNSAgentMsg);
										step = 0;
										myAgent.doWait();
									}
								} else {
									step = 2;
								}
							}
					}
				}
				break;

			case 1:
				ACLMessage dnsReplyMsg = myAgent.blockingReceive(MessageTemplate.and(mtOnto, mtDns));
				Object dnsReplyObj = null;
				try {
					dnsReplyObj = myAgent.getContentManager().extractContent(dnsReplyMsg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (dnsReplyObj != null && dnsReplyObj instanceof BCDNSReply) {
					BCDNSReply dnsReply = (BCDNSReply) dnsReplyObj;
					Iterator dnsReplyIterator = dnsReply.dnsReceivers.iterator();
					AID currentId = null;
					while (dnsReplyIterator.hasNext()) {
						currentId = (AID) dnsReplyIterator.next();
						msgForwarding.addReceiver(currentId);
					}
					step = 2;
				}
				break;

			case 2:
				if (msgForwarding != null) {

					msgForwarding.setSender(getAID());
					BCForwardRequest bcForwardRequest = new BCForwardRequest();
					bcForwardRequest.setActual_sender(actualSender.getLocalName());

					Iterator<AID> receiverItr = msgForwarding.getAllReceiver();

 					for (;receiverItr.hasNext();receiverItr.next())
 						numberOfSents++;

					if (numberOfSents > 0) {
						myAgent.send(msgForwarding);

//						myAgent.send(msgForwarding);
						sendingTime = System.currentTimeMillis();
						step = 3;
					} else { // No Receiver
						ACLMessage noRecMsg = new ACLMessage(ACLMessage.INFORM);
						noRecMsg.setOntology(BroadCastingServicesOntology.ONTOLOGY_NAME);
						noRecMsg.addReceiver(msgForwarding.getSender());
						BCReply bcReply = new BCReply();
						bcReply.setBcReplyMsg("No receiver is retrieved!");
						try {
							myAgent.getContentManager().fillContent(noRecMsg, bcReply);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						send(noRecMsg);
						step = 0;
						myAgent.doWait();
					}
				}

				break;

			case 3:
				while ( (System.currentTimeMillis() - sendingTime) < treshold || numberOfReceived == numberOfSents) {
					ACLMessage acknowledgeMsg = myAgent.blockingReceive(MessageTemplate.and
							(mtOnto, MessageTemplate.MatchInReplyTo(msgForwarding.getReplyWith())), treshold);
					Object bcForwardReplyObj = null;
					if (acknowledgeMsg != null) {
						try {
							bcForwardReplyObj = myAgent.getContentManager().extractContent(acknowledgeMsg);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
						if (bcForwardReplyObj != null && bcForwardReplyObj instanceof BCForwardReply) {
							numberOfReceived++;

							ACLMessage finalAckMsg = msgForwarding.createReply();
							finalAckMsg.addReceiver(actualSender);
							finalAckMsg.setOntology(BroadCastingServicesOntology.ONTOLOGY_NAME);

							BCReply bcReply = (BCReply) bcForwardReplyObj;
							bcReply.setBcReplyMsg(bcReply.getBcReplyMsg());
							bcReply.setReceiverLocalName(acknowledgeMsg.getSender().getLocalName());
							
							try {
								myAgent.getContentManager().fillContent(finalAckMsg, bcReply);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							send(finalAckMsg);
						}
					}
				}
				step = 0;
				myAgent.doWait();

				break;
			default: 
				break;
			}
		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return false;
		}		
	}

	private java.util.ArrayList<AID> searchAll() {
		java.util.ArrayList<AID> providers = new java.util.ArrayList<AID>();
		// Search for services of type "weather-forecast"
		try {
			// Build the description used as template for the search
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription templateSd = new ServiceDescription();
//			templateSd.setType(service_type);
//			AID aa = new AID("2323", true);
//			aa.
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
// 						if (sd.getType().equals(service_type)) {
							// System.out.println("- Service \"" + sd.getName()
							// + "\" provided by agent "
							// + provider.getName());
							providers.add(provider);
//						}
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