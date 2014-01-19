package ir.mod.tavana.toranj.services.mediator;

import jade.content.AgentAction;

public class LogIn implements AgentAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;

	public LogIn() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
