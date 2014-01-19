package ir.mod.tavana.toranj.services.broadcasting;

import jade.content.Predicate;

public class BCReply implements Predicate {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String bcReplyMsg = "";
	private String receiverLocalName = "";

	public String getReceiverLocalName() {
		return receiverLocalName;
	}

	public void setReceiverLocalName(String receiverLocalName) {
		this.receiverLocalName = receiverLocalName;
	}

	public String getBcReplyMsg() {
		return bcReplyMsg;
	}

	public void setBcReplyMsg(String bcReplyMsg) {
		this.bcReplyMsg = bcReplyMsg;
	}
	

}
