package ir.mod.tavana.toranj.services.broadcasting;

import java.util.ArrayList;
import java.util.List;

import jade.core.AID;

import jade.lang.acl.ACLMessage;

public class BroadcastinMessage extends ACLMessage{

	
	public static final int FROM_REGS = 0;
	public static final int FROM_OUT = 1;
//	public static final int FROM_DNS = 2;
	
	private boolean isBroadcast;
	private String receiverQuery;
	private int type;
	private List<AID> receivers;
	private BroadcastinMessage lastBCMsg;
	
	public BroadcastinMessage getLastBCMsg() {
		if(lastBCMsg == null)
			lastBCMsg = new BroadcastinMessage();
		return lastBCMsg;
	}
	public void setLastBCMsg(BroadcastinMessage lastBCMsg) {
		this.lastBCMsg = lastBCMsg;
	}
	public List<AID> getReceivers() {
		if(receivers == null)
			receivers = new ArrayList<AID>();
		return receivers;
	}
	public void setReceivers(List<AID> receivers) {
		this.receivers = receivers;
	}
	public boolean isBroadcast() {
		return isBroadcast;
	}
	public void setBroadcast(boolean isBroadcast) {
		this.isBroadcast = isBroadcast;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getReceiverQuery() {
		return receiverQuery;
	}
	public void setReceiverQuery(String receiverQuery) {
		this.receiverQuery = receiverQuery;
	}
	
}
