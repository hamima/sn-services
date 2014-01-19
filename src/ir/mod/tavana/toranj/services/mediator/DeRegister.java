package ir.mod.tavana.toranj.services.mediator;

import jade.content.AgentAction;

public class DeRegister implements AgentAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String aid;

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public DeRegister() {
		super();
	}
	
}
