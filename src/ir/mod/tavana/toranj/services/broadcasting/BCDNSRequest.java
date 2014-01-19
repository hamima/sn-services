package ir.mod.tavana.toranj.services.broadcasting;

import jade.content.Predicate;

public class BCDNSRequest implements Predicate {
	
	private String receiverQuery = "";

	public String getReceiverQuery() {
		return receiverQuery;
	}

	public void setReceiverQuery(String receiverQuery) {
		this.receiverQuery = receiverQuery;
	}

}
