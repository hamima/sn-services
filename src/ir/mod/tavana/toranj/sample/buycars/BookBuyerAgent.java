package ir.mod.tavana.toranj.sample.buycars;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class BookBuyerAgent extends Agent{
	
	private String targetBookTitle;
	private AID[] sellerAgents = {
			new AID("seller1",AID.ISLOCALNAME),
			new AID("seller2",AID.ISLOCALNAME)
	};
	
	protected void setup(){
		System.out.println("Hello! Buyer-agent "+getAID().getName()+" is ready.");
		// Get the title of the book to buy as a start-up argument
		Object[] args = getArguments();
		args[0] = "Karamazov Bros";
		if (args != null && args.length > 0) {
			targetBookTitle = (String) args[0];
			System.out.println("Trying to buy "+targetBookTitle);
/*			this.addBehaviour(new TickerBehaviour(this, 60000){
				protected void onTick(){
					// Update the list of seller agents
					DFAgentDescription template = new DFAgentDescription();
					ServiceDescription sd = new ServiceDescription();
					sd.setType("book-selling");
					template.addServices(sd);
					try {
						DFAgentDescription[] result = DFService.search(myAgent, template);
						sellerAgents = new AID[result.length];
						for (int i = 0; i < result.length; ++i) {
							sellerAgents[i] = result[i].getName();
						}
					} catch (FIPAException fe) {
							fe.printStackTrace();
					}
				}
			});
*/			// Perform the request
			this.addBehaviour(new RequestPerformer());

		} else {
		// Make the agent terminate immediately
			System.out.println("No book title specified");
			doDelete();
		}
	}
		// Put agent clean-up operations here
	protected void takeDown() {
	// Printout a dismissal message
		System.out.println("Buyer-agent "+getAID().getName()+" terminating.");
	}

	/**
	Inner class RequestPerformer.
	This is the behaviour used by Book-buyer agents to request seller
	agents the target book.
	*/
	private class RequestPerformer extends CyclicBehaviour {
		private AID bestSeller; // The agent who provides the best offer
		private int bestPrice; // The best offered price
		private int repliesCnt = 0; // The counter of replies from seller agents
		private MessageTemplate mt; // The template to receive replies
		private int step = 0;

		public RequestPerformer() {
			super();
			// TODO Auto-generated constructor stub
		}

		public RequestPerformer(Agent a) {
			super(a);
			// TODO Auto-generated constructor stub
		}

		public void action() {
			
			ACLMessage msg = (ACLMessage) myAgent.receive();
			
			if (msg != null){
				targetBookTitle = msg.getContent();
				System.out.println("Some agent has requested a book with title " + targetBookTitle);
			}
/*			switch (step) {
			case 0:
		// Send the cfp to all sellers
				ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
				for (int i = 0; i < sellerAgents.length; ++i) {
					cfp.addReceiver(sellerAgents[i]);
				}
				cfp.setContent(targetBookTitle);
				cfp.setConversationId("book-trade");
				cfp.setReplyWith("cfp"+System.currentTimeMillis()); // Unique value
				myAgent.send(cfp);
		// 	Prepare the template to get proposals
				mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
						MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
				step = 1;
				break;
			case 1:
		// 	Receive all proposals/refusals from seller agents
				ACLMessage reply = myAgent.receive(mt);
				if (reply != null) {
					// 	Reply received
					if (reply.getPerformative() == ACLMessage.PROPOSE) {
						// 	This is an offer
						int price = Integer.parseInt(reply.getContent());
						if (bestSeller == null || price < bestPrice) {
							// This is the best offer at present
							bestPrice = price;
							bestSeller = reply.getSender();
						}
					}
					repliesCnt++;
					if (repliesCnt >= sellerAgents.length) {
						// We received all replies
						step = 2;
					}
				} else {
					block();
				}
				break;
			case 2:
				// Send the purchase order to the seller that provided the best offer
				ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
				order.addReceiver(bestSeller);
				order.setContent(targetBookTitle);
				order.setConversationId("book-trade");
				order.setReplyWith("order"+System.currentTimeMillis());
				myAgent.send(order);
				// 	Prepare the template to get the purchase order reply
				mt = MessageTemplate.and(MessageTemplate.MatchConversationId("11book-trade"),
						MessageTemplate.MatchInReplyTo(order.getReplyWith()));
				step = 3;
				break;
			case 3:
				// Receive the purchase order reply
				reply = myAgent.receive(mt);
				if (reply != null) {
					// 	Purchase order reply received
					if (reply.getPerformative() == ACLMessage.INFORM) {
						// 	Purchase successful. We can terminate
						System.out.println(targetBookTitle+" successfully purchased.");
						System.out.println("Price = " + bestPrice);
						myAgent.doDelete();
					}
					step = 4;
				} else {
					block();
				}
				break;
			}
*/		}

/*		public boolean done() {
			return ((step == 2 && bestSeller == null) || step == 4);
		}*/
	} // End of inner class RequestPerformer
}