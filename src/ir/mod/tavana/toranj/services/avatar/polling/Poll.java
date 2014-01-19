package ir.mod.tavana.toranj.services.avatar.polling;

import jade.content.AgentAction;

public class Poll implements AgentAction{
	
	private String question;
	private String options;
	private String scope;
	private String timeout;
	
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
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getTimeout() {
		return timeout;
	}
	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}

}
