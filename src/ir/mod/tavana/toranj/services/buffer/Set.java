package ir.mod.tavana.toranj.services.buffer;

import jade.content.AgentAction;

public class Set implements AgentAction{
	
	private int value;
	private int agentId;

	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getAgentId() {
		return agentId;
	}
	public void setAgentId(int agentId) {
		this.agentId = agentId;
	}
	

}
