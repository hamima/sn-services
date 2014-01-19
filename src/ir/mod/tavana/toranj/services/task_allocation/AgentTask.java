package ir.mod.tavana.toranj.services.task_allocation;

import jade.core.AID;

public class AgentTask {
	
	private AID biddingAgent;
	private Integer bid;
	private Task biddedTask;
	
	public AgentTask(AID a, Task t, Integer b) {
		this.biddingAgent = a;
		this.biddedTask = t;
		this.bid = b;
	}

	public AID getBiddingAgent() {
		return biddingAgent;
	}

	public void setBiddingAgent(AID biddingAgent) {
		this.biddingAgent = biddingAgent;
	}

	public Integer getBid() {
		return bid;
	}

	public void setBid(Integer bid) {
		this.bid = bid;
	}

	public Task getBiddedTask() {
		return biddedTask;
	}

	public void setBiddedTask(Task biddedTask) {
		this.biddedTask = biddedTask;
	}
}