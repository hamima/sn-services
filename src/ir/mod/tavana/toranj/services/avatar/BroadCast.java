package ir.mod.tavana.toranj.services.avatar;

import jade.content.AgentAction;

public class BroadCast implements AgentAction {
	
	private String receivers;
	private int performance;
	private String query;

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public int getPerformance() {
		return performance;
	}

	public void setPerformance(int performance) {
		this.performance = performance;
	}

	public String getReceivers() {
		
		return receivers;
	}

	public void setReceivers(String receivers) {
		this.receivers = receivers;
	}

}
