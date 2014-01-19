package ir.mod.tavana.toranj.services.planning;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Buffer {
	public int T;
	Soccer soccer;

	public final Lock accessLock = new ReentrantLock();
	public final Lock accessLock2 = new ReentrantLock();
	// private final Condition canWrite = accessLock.newCondition();
	// private final Condition canRead = accessLock2.newCondition();
	private int[] act;
	private int sts = -1;
	private int agentID;
	private int both = 0;
	private int numberOfAgents;
	int[] s = new int[4];
	int[][] action;
	
	public double totalReward = 0.0;

	public Buffer(int numberOfSteps, int numberOfAgents) {
		T = numberOfSteps;
		action = new int[numberOfSteps][numberOfAgents];
		this.numberOfAgents = numberOfAgents;
		act = new int[numberOfAgents];
		soccer = new Soccer(T, Model.PROBLEM_SOCCER2x3);

	}

	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	public void set(int value, int agentId) // throws InterruptedException
	{
		// act0=-1;
		// act1=-1;
		agentID = agentId;
		accessLock.lock();
		// System.out.println("in set agent "+agentId);
		try {
			act[agentId] = value;

			/*
			 * if ((act0!=-1) &&(act1!=-1)) { //canRead.signal();
			 * System.out.println("signal"); }
			 */
		}// end try
		finally {

			// System.out.println("in 2 set agent "+agentId + "  "+act0 +
			// "  "+act1);
			accessLock.unlock();
			both++;
		}// end finally
	}

	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	public synchronized int get(int temp, int pres) throws InterruptedException// wich
	{
		try {
			while ((both % numberOfAgents) != 0)
				Thread.sleep(40);
			totalReward += soccer.getReward(pres, act[0], act[1]);
			// System.out.println("reward   "+ totalReward);
			s = soccer.nextState(pres, act[0], act[1]);
			sts = s[temp];
		} finally {

			// accessLock2.unlock();
			// System.out.println("in 2 get agent "+agentID);
		}

		return sts;
	}
	
	public String finalPlan(){
		
		String finalPlan = "";
		
		for (int step =0; step<T; step++){
			for (int id=0; id<numberOfAgents; id++){
				finalPlan += "Step: " + step + " " + "Agent: "  + id + " Action: " + action[step][id] + "\n"; 
			}
		}
		finalPlan += "Total Reward: " + totalReward + "\n"; 
		return finalPlan;
	}

	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	public Soccer getsocer() {
		return soccer;
	}
}
