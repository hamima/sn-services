package ir.mod.tavana.toranj.services.avatar.polling;

import jade.content.AgentAction;
import jade.core.AID;

public class ResponsePoll implements AgentAction {
	
	private String question;
	private String options;
	private AID pollingAgent;
	
	public AID getPollingAgent() {
		return pollingAgent;
	}
	public void setPollingAgent(AID pollingAgent) {
		this.pollingAgent = pollingAgent;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}

}
