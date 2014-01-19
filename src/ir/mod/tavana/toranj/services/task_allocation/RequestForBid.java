package ir.mod.tavana.toranj.services.task_allocation;

import jade.content.Predicate;

public class RequestForBid implements Predicate {
	
	private long id = 0;
	private String description = "";
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
