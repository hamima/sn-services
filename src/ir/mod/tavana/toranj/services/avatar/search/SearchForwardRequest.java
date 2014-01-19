package ir.mod.tavana.toranj.services.avatar.search;

import jade.content.AgentAction;

public class SearchForwardRequest implements AgentAction{
	
	private String key;
	private String value;
	private String operator;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}

}
