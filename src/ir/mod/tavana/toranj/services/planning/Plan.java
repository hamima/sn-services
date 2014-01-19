package ir.mod.tavana.toranj.services.planning;

import jade.content.AgentAction;

public class Plan implements AgentAction {
	
	private int numberOfSteps;
	private int numberOfAgents;

	public int getNumberOfSteps() {
		return numberOfSteps;
	}
	public void setNumberOfSteps(int numberOfSteps) {
		this.numberOfSteps = numberOfSteps;
	}
	public int getNumberOfAgents() {
		return numberOfAgents;
	}
	public void setNumberOfAgents(int numberOfAgents) {
		this.numberOfAgents = numberOfAgents;
	}
}
