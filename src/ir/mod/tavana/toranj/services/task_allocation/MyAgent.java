package ir.mod.tavana.toranj.services.task_allocation;

import jade.core.AID;
import jade.core.Agent;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Vector;



public class MyAgent {
	private boolean busy = false;
	private static int idCounter = 0;
	public int id;
	private AID realAgent;
	private TaskAllocationAgent tAAgent;
	
	public MyAgent(AID realAgent) {
		this.realAgent = realAgent;
		idCounter++;
		id = idCounter;
	}
	
	public static void reset() {
		idCounter = 0;
	}
	
	public AID getRealAgent() {
		return realAgent;
	}
	
	public boolean isBusy() {
		return busy;
	}
	
	public int getPrice(Task t, Agent tAAgent) {
		/** to do **/
		int price;
		//if (t.isLeaf()) {
			price = AgentManager.getPrice(realAgent,t, tAAgent);
		//} else {
		//	Vector<Task> subTasks = t.getSubTasks();
		//	if (t.getType().equals("And")) {
		//		price = 0;
		//		for (int i = 0; i < subTasks.size(); i++) { 
		//			price = price + getPrice(subTasks.get(i));
		//		}
		//	} else {
		//		price = TaskAllocationAgent.Inf;
		//		for (int i = 0; i < subTasks.size(); i++) { 
		//			int p = getPrice(subTasks.get(i));
		//			price = price < p ? price : p;
		//		}
		//	}
		//}
			
		return price;		
	}
	
	private Task breakAsYouWish(Task t) {
		/** to do **/
		return AgentManager.breakTask(realAgent,t);
	}
	
	private void setReservePrice(Task t,Agent tAAgent) {
//		this.tAAgent = (TaskAllocationAgent) tAAgent;
		t.setReservePrice(getPrice(t, tAAgent));
		if (t.isLeaf()) return;
		Vector<Task> subT = t.getSubTasks();
		for(Task t2 : subT) {
			setReservePrice(t2, tAAgent);
		}
	}

	public void assign(Task t, Agent tAAgent) {
		t = breakAsYouWish(t);
		setReservePrice(t, tAAgent);
		Vector<MyAgent> aList = ((TaskAllocationAgent)tAAgent).getAgentManager().getFreeAgents();
		auction(t, aList);
	}

	private void getBids(Task t, Vector<MyAgent> v) {
		for (int i = 0; i < v.size(); i++) {
			t.setCost(v.get(i));
		}
		if (t.isLeaf()) return;
		Vector<Task> subT = t.getSubTasks();
		Iterator<Task> it = subT.iterator();
		while (it.hasNext()) {
			getBids(it.next(), v);
		}
	}

	private void addMarked2Q(Task t, PriorityQueue<Task> q) {
		if (t.isSatisfied()) return;
		if (t.isMarked()) q.add(t);
		if (t.isLeaf()) return;
		Vector<Task> subT = t.getSubTasks();
		Iterator<Task> it = subT.iterator();
		while (it.hasNext()) {
			addMarked2Q(it.next(), q);
		}
	}

	private void clearAuction(Task t) {
		if (t.isFinished()) System.out.println("The task is not a tree!");
		while (!t.isFinished()) {
			simpleClearAuction(t);
			PriorityQueue<Task> pQueue;
			pQueue = new PriorityQueue<Task>(10,new TaskComparator());
			addMarked2Q(t,pQueue);
			Iterator<Task> it = pQueue.iterator();
			while (it.hasNext()) {
				Task t2 = it.next();
				AgentTask a = t2.getBestAgent();
				if (a==null) continue;
				if (!a.isBusy()) {
					removeWinnerBids(t,a);
					a.doTask(t2);
					t2.satisfy();
				}
			}
		}
		//simpleClearAuction(t);
		//printMarkedNodes(t);
	}

	private void printMarkedNodes(Task t) {
		if (t.isMarked()) System.out.println(t.id);
		if (t.isLeaf()) {
			return;
		}
		Vector<Task> S = t.getSubTasks();
		Iterator<Task> it = S.iterator();
		while (it.hasNext()) {
			printMarkedNodes(it.next());
		}
	}

	private void removeWinnerBids(Task t, MyAgent a) {
		if (t.isSatisfied()) return;
		t.removeBids(a);
		if (t.isLeaf()) {
			return;
		}
		Vector<Task> S = t.getSubTasks();
		Iterator<Task> it = S.iterator();
		while (it.hasNext()) {
			removeWinnerBids(it.next(), a);
		}
	}

	private Task myNext(Iterator<Task> it) {
		while (it.hasNext()) {
			Task t = it.next();
			if (t.isRemoved() || t.isSatisfied()) return null;
			return t;
		}
		return null;
	}

	private void simpleClearAuction(Task t) {
		InitializeSimpleClear(t);
		Task Np = null;
		while (Np != t) {
			Task Nmax = maxDepthSubTask(t);
			Np = Nmax.getParent();
			Vector<Task> S = Np.getSubTasks();
			int Ps;
			if (Np.getType().equals("And")) {
				Ps = 0;
				Iterator<Task> it = S.iterator();
				while (true) {
					Task tmp = myNext(it);
					if (tmp==null) break;
					Ps = Ps + tmp.getBestPrice();
				}
			} else {
				Ps = TaskAllocationAgent.Inf;
				Iterator<Task> it = S.iterator();
				while (true) {
					Task tmp = myNext(it);
					if (tmp==null) break;
					int tmpPrice = tmp.getBestPrice();
					Ps = Ps < tmpPrice ? Ps : tmpPrice;
				}
			}
			if (Np.getBestPrice() <= Ps) {
				Np.mark();
				unmarkDecendants(Np);
			} else {
				Np.setBestPrice(Ps);
				if (Np.getType().equals("Or")) { //then just the best child should be marked, all others should be unmarked
					Iterator<Task> it = S.iterator(); 
					boolean tempMark = false;
					while (true) {
						Task tmp = myNext(it);
						if (tmp==null) break;
						if (tmp.getBestPrice() == Ps && !tempMark) {
							tempMark = true;
							continue;
						}
						tmp.unMark();
						unmarkDecendants(tmp);
					}
				}
			}
			Iterator<Task> it = S.iterator();
			while (true) {
				Task tmp = myNext(it);
				if (tmp==null) break;
				tmp.remove();
			}
		}
	}
	
	private void unmarkDecendants(Task t) {
		if (t.isSatisfied()) return;
		if (t.isLeaf()) {
			return;
		}
		Vector<Task> S = t.getSubTasks();
		Iterator<Task> it = S.iterator();
		while (it.hasNext()) {
			Task tt = it.next();
			tt.unMark();
			unmarkDecendants(tt);
		}
	}
	
	private Task maxDepthSubTask(Task t) {
		if (t.isRemoved()) return null;
		if (t.isSatisfied()) return null;
		if (t.isLeaf()) return t;
		Vector<Task> S = t.getSubTasks();
		Iterator<Task> it = S.iterator();
		int maxD = 0;
		Task maxT = t;
		while (true) {
			Task tmp = myNext(it);
			if (tmp==null) break;
			Task tT = maxDepthSubTask(tmp);
			if (tT.getDepth() > maxD) {
				maxD = tT.getDepth();
				maxT = tT;
			}
		}
		return maxT;
	}
	
	private void InitializeSimpleClear(Task t) {
		if (t.isSatisfied()) return;
		t.unRemove();
		if (t.isLeaf()) {
			t.mark();
		} else {
			t.unMark();
			Vector<Task> subT = t.getSubTasks();
			Iterator<Task> it = subT.iterator();
			while (it.hasNext()) {
				InitializeSimpleClear(it.next());
			}
		}
	}
	
	private void auction(Task t, Vector<MyAgent> v) {
		getBids(t,v);
		clearAuction(t);
	}
	
	private void doTask(Task t) {
		busy = true;
		AgentManager.giveTask(realAgent,t);
		System.out.println("I'm Agent #" + id + ". I am doing task #" + t.id + " with price " + getPrice(t,tAAgent));
	}
}