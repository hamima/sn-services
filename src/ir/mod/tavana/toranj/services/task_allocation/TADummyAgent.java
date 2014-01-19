package ir.mod.tavana.toranj.services.task_allocation;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class TADummyAgent extends Agent {
	
	protected void setup() {

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("TADummyAgent");
		sd.setName("TADummyAgent-agents");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		addBehaviour(new sendPriceMessage());
	}

	private class sendPriceMessage extends CyclicBehaviour {

		@Override
		public void action() {
			// TODO Auto-generated method stub

			ACLMessage aclMsg = blockingReceive();
			if (aclMsg != null) {
				List<AID> taList = search("TaskAllocationAgent");
				if (taList.size() > 0) {
					ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
					aclMessage.addReceiver(taList.get(0));
					myAgent.send(aclMessage);
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
//				Build the description used as template for the search
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription templateSd = new ServiceDescription();
				templateSd.setType(service_type);
				template.addServices(templateSd);

				SearchConstraints sc = new SearchConstraints();
				// We want to receive 10 results at most
				sc.setMaxResults(new Long(10));

				DFAgentDescription[] results = DFService.search(myAgent, template, sc);
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
}