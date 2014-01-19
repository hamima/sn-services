package ir.mod.tavana.toranj.services.mediator;

import jade.content.AgentAction;

public class Register implements AgentAction{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Profile profile;

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public Register() {
		super();
	}

}
