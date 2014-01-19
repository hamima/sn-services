package ir.mod.tavana.toranj.services.task_allocation;

import jade.core.AID;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Vector;


public class Task {
	private Vector<Task> subTasks = null;
	private PriorityQueue<AgentTask> pQueue;
	private Comparator<AgentTask> comparator;
	private boolean mark = false;
	private boolean satisfied = false;
	private boolean removed = false;
	public Task parent = null;
	private String type;
	private int bestPrice = TaskAllocationAgent.Inf;
	private int reservePrice;
	private static int idCounter = 0;
	public Integer id;
	private int depth = 0;
	private String theTask;
	
	public String getTheString() {
		return theTask;
	}
	
	public void removeBids(AgentTask a) {
		Iterator<AgentTask> it = pQueue.iterator();
		AgentTask toBeRemoved = null;
		while (it.hasNext()) {
			AgentTask at = it.next();
			if (at.getBiddingAgent() == a.getBiddingAgent()) {
				toBeRemoved = at;
				break;
			}
		}
		pQueue.remove(toBeRemoved);
		if (bestPrice == toBeRemoved.getBid()) {
			it = pQueue.iterator();
			if (it.hasNext()) {
				AgentTask at = it.next();
				bestPrice=at.getBid();
			}
		}
	}
	
	public boolean isFinished() {
		if (isSatisfied()) return true;
		if (subTasks.isEmpty()) {
			if (isSatisfied()) return true;
			else return false;
		}
		if (type.equals("Or")) {
			for (Task t:subTasks) {
				if (t.isFinished()) return true;
			}
		}
		if (type.equals("And")) {
			boolean b = true;
			for (Task t:subTasks) 
				if (!t.isFinished()) b = false;
			if (b) return true;
		}
			
		return false;
	}
	
	/*private boolean satisfiedOneChild() {
		for (Task t: subTasks) 
			if (t.isFinished(0)) 
				return true;
		return false;
				
	}
	
	private boolean satisfiedallChildren() {
		for (Task t: subTasks) {
			if (!t.isFinished(0)) {

				return false;
			}
		}
		return true;
	}
	*/
	
	public void setReservePrice(int p) {
		reservePrice = p;
	}
	
	public int getProfit() {
		return reservePrice - bestPrice;
	}
	
	public String getType() {
		return type;
	}
	
	public int getBestPrice() {
		return bestPrice;
	}
	
	public void setBestPrice(int price) {
		bestPrice = price;
	}
		
	public void setCost(Integer bid, AID bidder) {
		pQueue.add(new AgentTask(bidder, this, bid));
		if (bid < bestPrice)
			bestPrice = bid;
	}
	
	private boolean allChildrenSatisfied() {
		for (Task t: subTasks)
			if (!t.isSatisfied()) return false;
		return true;
	}
	
	public AgentTask getBestAgent() {
		if (pQueue.isEmpty()) return null;
		Iterator<AgentTask> it =  pQueue.iterator();
		while(it.hasNext()){
			AgentTask a = it.next();
			if (a.getBid() == bestPrice) return a;
		}
			
		return null;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public Task(Task parent,String ss,String type) {
		this.theTask = ss;
		idCounter++;
		id = idCounter;
		this.type = type;
		comparator = new AgentComparator();
		pQueue = new PriorityQueue<AgentTask>(10,comparator);
		subTasks = new Vector<Task>();
		this.parent = parent;
		if (parent != null) depth = parent.depth + 1;
	}
	
	public void addChild(Task t) {
		subTasks.add(t);
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Vector<Task> getSubTasks() {
		return subTasks;
	}
	
	public boolean isLeaf() {
		return (subTasks.isEmpty() || allChildrenSatisfied());
	}
	
	public Task getParent() {
		return parent;
	}

	public void remove() {
		removed = true;
	}
	
	public boolean isRemoved() {
		return removed;
	}
	
	public void unRemove() {
		removed = false;
	}
	
	public void satisfy() {
		if (parent != null) {
			//parent.subTasks.remove(this);
		}
		satisfied = true;
	}
	
	public boolean isSatisfied() {
		return satisfied;
	}
	
	public void unSatisfy() {
		satisfied = false;
	}
	
	public boolean isMarked() {
		return mark;
	}
	
	public void mark() {
		mark = true;
	}
	
	public void unMark() {
		mark = false;
	}
}
