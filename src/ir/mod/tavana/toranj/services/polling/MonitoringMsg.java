package ir.mod.tavana.toranj.services.polling;

import jade.content.AgentAction;

public class MonitoringMsg implements AgentAction {

	private String qid;

	public String getQid() {
		return qid;
	}

	public void setQid(String qid) {
		this.qid = qid;
	}

	@Override
	public String toString() {
		return qid;
	}
}
