package ir.mod.tavana.toranj.services.task_allocation;

import jade.content.Predicate;

public class Bid implements Predicate {
	
	/**
	 * 
	 */

	private Integer myBid = 0;
	private Integer taskId = 0;

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Integer getMyBid() {
		return myBid;
	}

	public void setMyBid(Integer myBid) {
		this.myBid = myBid;
	}

}
