package ir.mod.tavana.toranj.services.communication;

import jade.content.AgentAction;

public class Sum implements AgentAction {
	
	private float firstElement;
	private float secondElement;
	
	
	public float getFirstElement() {
		return firstElement;
	}
	public void setFirstElement(float firstElement) {
		this.firstElement = firstElement;
	}
	public float getSecondElement() {
		return secondElement;
	}
	public void setSecondElement(float secondElement) {
		this.secondElement = secondElement;
	}
	
	@Override
	public String toString() {
		return "Sum ("+firstElement+","+secondElement+")";
	}
}
