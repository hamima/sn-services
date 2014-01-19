package ir.mod.tavana.toranj.services.communication;

import jade.content.AgentAction;

public class SearchUsers implements AgentAction {

	private String stype;

	public String getStype() {
		return stype;
	}

	public void setStype(String stype) {
		this.stype = stype;
	}

	@Override
	public String toString() {
		return "Search (" + stype + ")";
	}
}
