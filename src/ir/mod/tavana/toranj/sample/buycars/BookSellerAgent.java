package ir.mod.tavana.toranj.sample.buycars;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;

import java.util.*;

public class BookSellerAgent extends Agent {
	// The catalogue of books for sale (maps the title of a book to its price)
	private Hashtable catalogue;
	// Put agent initializations here
	protected void setup() {
		// Create the catalogue
		catalogue = new Hashtable();
		// Add the behaviour serving requests for offer from buyer agents
		addBehaviour(new OfferRequestsServer());
		// Add the behaviour serving purchase orders from buyer agents
//		addBehaviour(new PurchaseOrdersServer());
	}

	// Put agent clean-up operations here
	protected void takeDown() {
		// 	Close the GUI
		// Printout a dismissal message
		System.out.println("Seller-agent "+getAID().getName()+" terminating.");
	}

	/**
	 * This is invoked by the GUI when the user adds a new book for sale
	 */
	public void updateCatalogue(final String title, final int price) {
		addBehaviour(new OneShotBehaviour() {
			public void action() {
				catalogue.put(title, new Integer(price));
			}
		});
	}
	
	private class OfferRequestsServer extends CyclicBehaviour {
		public void action() {
			ACLMessage msg = myAgent.receive();
			if (msg != null) {
				// 	Message received. Process it
				String title = msg.getContent();
				ACLMessage reply = msg.createReply();
				Integer price = (Integer) catalogue.get(title);
				if (price != null) {
					// 	The requested book is available for sale. Reply with the price
					reply.setPerformative(ACLMessage.PROPOSE);
					reply.setContent(String.valueOf(price.intValue()));
				} else {
					// The requested book is NOT available for sale.
					reply.setPerformative(ACLMessage.REFUSE);
					reply.setContent("not-available");
				}
				myAgent.send(reply);
			} else {
				block();
			}
		}
	} // End of inner class OfferRequestsServer
	
	
}