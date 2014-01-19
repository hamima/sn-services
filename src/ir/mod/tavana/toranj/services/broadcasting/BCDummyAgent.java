package ir.mod.tavana.toranj.services.broadcasting;

import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.util.Iterator;

public class BCDummyAgent extends Agent {

	protected void setup(){

		getContentManager().registerLanguage(new SLCodec(),FIPANames.ContentLanguage.FIPA_SL0);
		getContentManager().registerOntology(BroadCastingServicesOntology.getInstance());

		DFAgentDescription dfdDummy = new DFAgentDescription();
		dfdDummy.setName(getAID());
		dfdDummy.addProtocols(FIPANames.InteractionProtocol.FIPA_REQUEST);

		ServiceDescription sd = new ServiceDescription();
		sd.addProtocols(FIPANames.InteractionProtocol.FIPA_REQUEST);
		sd.setName("Dummy Agent");
		sd.setType("Dummy Agent");
		sd.addOntologies(BroadCastingServicesOntology.getInstance().getName());

		dfdDummy.addServices(sd);

		try {
			DFService.register(this, dfdDummy);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		addBehaviour(new OneShotBehaviour() {
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				
				BCRequest bcRequest = new BCRequest();
				bcRequest.setBroadcast(false);
				bcRequest.setReceiverQuery("Select All");
				bcRequest.setApplication(BroadCastingServicesOntology.SEND_BROADCASTING);
				
				ACLMessage bcMsg = new ACLMessage(ACLMessage.REQUEST);
				bcMsg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
				bcMsg.setOntology(BroadCastingServicesOntology.ONTOLOGY_NAME);
				AID bcAgent = search(BroadcastingServiceAgent.SERVICE_TYPE).get(0);
				if (bcAgent != null){
					bcMsg.addReceiver(bcAgent);
				}
				try {
					myAgent.getContentManager().fillContent(bcMsg, bcRequest);
				} catch (CodecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OntologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
				myAgent.send(bcMsg);
			}
		});
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
