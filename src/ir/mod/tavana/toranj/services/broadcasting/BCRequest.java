package ir.mod.tavana.toranj.services.broadcasting;

import jade.content.Predicate;
import jade.core.AID;
import jade.util.leap.ArrayList;
import jade.util.leap.List;


public class BCRequest implements Predicate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List receivers;
	private String application = "";
	private boolean broadcast = false;
	private String receiverQuery = "";
	
	public List getReceivers() {
		if (receivers == null)
			receivers = new ArrayList();
		return receivers;
	}
	public void setReceivers(List receivers) {
		this.receivers = receivers;
	}
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	public boolean isBroadcast() {
		return broadcast;
	}
	public void setBroadcast(boolean broadcast) {
		this.broadcast = broadcast;
	}
	public boolean getBroadcast() {
		return broadcast;
	}
	public String getReceiverQuery() {
		return receiverQuery;
	}
	public void setReceiverQuery(String receiverQuery) {
		this.receiverQuery = receiverQuery;
	}
	
}
