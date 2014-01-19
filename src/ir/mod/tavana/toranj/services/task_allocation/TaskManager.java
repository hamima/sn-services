package ir.mod.tavana.toranj.services.task_allocation;

import jade.core.Agent;

import java.util.Vector;


public class TaskManager {
	
	private Vector<Task> tasks; 
	
	public TaskManager() {
		tasks = new Vector<Task>();
	}
	
	public Task getTask(int id) {
		return tasks.get(id);
	}
	
	public void addTask(Task t, Agent tAAgent) {
		tasks.add(t);
		((TaskAllocationAgent)tAAgent).getAgentManager().assign(t,tAAgent);
	}
}
