package ir.mod.tavana.toranj.services.avatar.polling;

import jade.content.AgentAction;

public class PollRequestForward implements AgentAction{
	
	private PollRequest pollRequest;

	public PollRequest getPollRequest() {
		return pollRequest;
	}

	public void setPollRequest(PollRequest pollRequest) {
		this.pollRequest = pollRequest;
	}
	

}
