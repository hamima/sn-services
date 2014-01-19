package ir.mod.tavana.toranj.services.buffer;

import jade.content.AgentAction;

public class Get implements AgentAction{
	
	private int temp;
	private int previousState;

	public int getTemp() {
		return temp;
	}
	public void setTemp(int temp) {
		this.temp = temp;
	}
	public int getPreviousState() {
		return previousState;
	}
	public void setPreviousState(int previousState) {
		this.previousState = previousState;
	}

}
