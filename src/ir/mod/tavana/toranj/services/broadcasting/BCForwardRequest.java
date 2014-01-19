package ir.mod.tavana.toranj.services.broadcasting;

import jade.content.Predicate;

public class BCForwardRequest implements Predicate {
	
	Object content;
	String actual_sender;
	
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}
	public String getActual_sender() {
		return actual_sender;
	}
	public void setActual_sender(String actual_sender) {
		this.actual_sender = actual_sender;
	}
	
}
