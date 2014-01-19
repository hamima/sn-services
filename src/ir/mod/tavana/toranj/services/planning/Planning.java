package ir.mod.tavana.toranj.services.planning;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author saeedeh
 */
public class Planning implements Runnable {
	public double[] Bt;
	public int Anum0, Anum1;
	public int Onum;
	public double totalReward = 0.0;
	public double Aprob;
	public double Oprob, pht[], PHT[];
	public int[][] states;
	public int numAgents, cnt = 0;
	public boolean communicate;
	Soccer soccer;
	// SoccerState[] S;
	Set<Integer> prs;
	int[][] Ho, Ha;
	int T;
	int AgentId;
	Buffer bf;

	// ***************************************
	public Planning(int ID, int numAgent, int Time, Buffer bufer, Soccer sc) {
		T = Time;
		AgentId = ID;
		bf = bufer;
		soccer = sc;
		states = new int[T][4];
		numAgents = numAgent;
		pht = new double[T];
		PHT = new double[T];
		Anum0 = Model.numAct0;
		Anum1 = Model.numAct1;
		Onum = Model.numObs0;
		Aprob = Soccer.ACTION_PROB;
		Oprob = Model.OBS_PROB;
		Bt = new double[T];
		Bt[0] = (float) Model.OBS_PROB;
		pht[0] = Model.OBS_PROB * Model.OBS_PROB;
		Ho = new int[T][numAgent];
		Ha = new int[T][numAgent];

	}

	// **************************************

	@Override
	public void run() {
		Random generator = new Random();

		try {
			Thread.sleep(generator.nextInt(500));
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}

		int[][] Ho_ = new int[T][numAgents];
		int[][] Ha_ = new int[T][numAgents];
		int[] sts = new int[T];
		int[] sts_ = new int[T];

		int[] temp = new int[3];
		int init = initialstate();

		Ha[0] = bestAction(init);
		states[1] = soccer.nextState(init, Ha[0][0], Ha[0][1]);
		double rw = soccer.getReward(init, Ha[0][0], Ha[0][1]);
		sts[0] = init;
		sts[1] = states[1][3];

		// System.out.println(" initial state "+sts[0] );
		Ho[1] = soccer.getObsForState(sts[1]);
		pht[1] = pht[0] * soccer.getObs1(Ho[1][AgentId], AgentId, sts[1]);
		PHT = pht;

		for (int t = 1; t < T - 1; t++) {
			try {
				Thread.sleep(generator.nextInt(500));
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}

			try {
				System.out.println("Step: " + t);
				pht = PHT;
				Ho_ = Ho;
				Ha_ = Ha;
				sts_ = sts;

				temp = policy(sts_[t], t);
				Ha_[t][0] = temp[0];// Ù…Ø§Ù„ Ø¹Ø§Ù…Ù„ Ø§ÙˆÙ„
				Ha_[t][1] = temp[1];
				// states[t+1]=soccer.nextState(sts_[t],Ha_[t][0], Ha_[t][1]);//
				// Ø§Ø¬Ø±Ø§ÛŒ Ø§Ù‚Ø¯Ø§Ù…
				if (AgentId == 0)
					bf.set(temp[0], AgentId);
				else
					bf.set(temp[1], AgentId);
				sts_[t + 1] = bf.get(temp[2], sts_[t]);
				// states[t+1][temp[2]];

				Ho_[t + 1] = soccer.getObsForState(sts_[t + 1]);

				pht[t + 1] = pht[t]
						* soccer.getObs1(Ho_[t + 1][AgentId], AgentId,
								sts_[t + 1]);
				if (pht[t + 1] > 0) {
					PHT = pht;
					bf.action[t][AgentId] = temp[AgentId];
					System.out.println(" Select action: " + temp[0] + "  "
							+ temp[1] + "  next " + sts_[t + 1]);
					Ho = Ho_;
					Ha = Ha_;
					sts = sts_;
					System.out.println("  "+Ha[2]);
					System.out.println();
					// bf.set(Ho, AgentId);
					if (inconsistency(Ho, Ha, sts, t)) {
						communicate = false;
						System.out.println(" inconsistent ............");
					} else
						communicate = false;
					Bt[++cnt] = beleif(Ho[t + 1], Ha[t], sts[t + 1], cnt);

				}
			} catch (InterruptedException ex) {
				Logger.getLogger(Planning.class.getName()).log(Level.SEVERE,
						null, ex);
			}

		}
		rw += bf.totalReward;
		System.out.println("Total reward: " + rw);
		// return totalReward;
	}

	// **************************************
	public int initialstate() {
		int state = 0;
		double acc = 0.0, thr = 0.7;

		for (int s = 0; s < soccer.numStates; ++s) {
			acc += soccer.getStartDist(s);
			if (acc >= thr) {
				state = s;

				break;
			}
		}
		return state;
	}

	// *************************************
	public boolean inconsistency(int[][] Ho, int[][] Ha, int[] sts, int t) {
		double temp, result = 0, epsilon = 0.01;

		// for (int i=0;i<soccer.numStates;i++){
		temp = 0;
		// for(int j=0;j<soccer.numStates;j++)
		// if (soccer.getTrans(j,Ha[t][0],Ha[t][1],i)>0)

		prs = soccer.getPrevStates(Ha[t][0], Ha[t][1], sts[t + 1]);

		if (prs != null)
			for (int st : prs)
				temp += Bt[t - 1]
						* soccer.getTrans(st, Ha[t][0], Ha[t][1], sts[t + 1]);
		result += temp
				* soccer.getObs1(Ho[t + 1][AgentId], AgentId, sts[t + 1]);
		// }

		// System.out.println("temp  "+result);
		if (result < epsilon)
			return true;
		else
			return false;
	}// **************************************

	public double beleif(int[] O, int[] A, int s_, int t)// call beleif with
															// A[t-1]
	{

		double OP = 0;
		if (communicate)
			OP = (soccer.getObs(O[0], O[1], A[0], A[1], s_));
		else
			OP = (soccer.getObs1(O[AgentId], AgentId, s_));
		double sum = 0, mop = 0, temp = 0;

		prs = soccer.getPrevStates(A[0], A[1], s_);
		if (prs != null) {
			for (int st : prs)
				sum += soccer.getTrans(st, A[0], A[1], s_) * Bt[t - 1];

			sum *= OP;
		} else
			sum = OP * 0.0001;

		for (int i = 0; i < soccer.numStates; i++) {

			prs = soccer.getPrevStates(A[0], A[1], i);
			if (communicate)
				OP = (soccer.getObs(O[0], O[1], A[0], A[1], i));
			else
				OP = (soccer.getObs1(O[AgentId], AgentId, i));
			temp = 0;
			if (prs != null)
				for (int st : prs)
					temp += soccer.getTrans(st, A[0], A[1], i) * Bt[t - 1];
			// System.out.println(" within beleif  mop  "+soccer.getTrans(st,A[0],A[1],i));}
			mop += OP * temp;
		}

		return (sum / mop);
	}

	// *****************************************************
	public int[] bestAction(int s) {
		int[] A = new int[2];
		double rw, temp = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < Anum0; i++)
			for (int j = 0; j < Anum1; j++) {
				rw = soccer.getReward(s, i, j);
				if (temp < rw) {
					A[0] = i;
					A[1] = j;
					temp = rw;
				}
			}
		return A;
	}

	// ****************************************************
	public int[] policy(int s, int t)// Act[2] = the number of selecting state
										// from 4 next candidate.
	{
		int[] Acts = new int[numAgents + 1];
		int[] Obs = new int[numAgents], Ba = new int[numAgents];
		double temp, max_, max = Double.NEGATIVE_INFINITY, rw = 0;
		int s__ = 0;
		float polic;

		int[] ar = new int[4];
		/*
		 * if (cnt%8==0&&cnt>1) { Random rand = new Random();
		 * Acts[0]=rand.nextInt(6); Acts[1]=rand.nextInt(6);
		 * Acts[numAgents]=rand.nextInt(4); //
		 * System.out.println("ohhhhhhhh  "+Acts[0]+ "k "+Acts[numAgents]);
		 * return Acts; }
		 */
		for (int i = 0; i < Anum0; i++)
			for (int j = 0; j < Anum1; j++)

			{
				polic = 0;
				max_ = Double.NEGATIVE_INFINITY;
				s__ = 0;

				ar = soccer.nextState(s, i, j);
				rw = soccer.getReward(s, i, j);
				for (int k = 0; k < ar.length; k++) {
					temp = 0;
					Obs = soccer.getObsForState(ar[k]);// in multi agent DEC we
														// have only 1 OBS.
					if (communicate)
						temp = soccer.getTrans(s, i, j, ar[k])
								* soccer.getObs(Obs[0], Obs[1], i, j, ar[k]);
					else
						temp = soccer.getTrans(s, i, j, ar[k])
								* soccer.getObs1(Obs[AgentId], AgentId, ar[k]);
					Ba = bestAction(ar[k]);
					double temp2 = soccer.getReward(ar[k], Ba[0], Ba[1]);
					/*
					 * ah=soccer.nextState(ar[k], Ba[0], Ba[1]);
					 * Ba=bestAction(ah[3]);//heurstic for two step
					 * temp2+=Math.max(Math.max(soccer.getReward(ah[1],Ba[0],
					 * Ba[1]), soccer.getReward(ah[0],Ba[0], Ba[1])),
					 * Math.max(soccer.getReward(ah[3],Ba[0], Ba[1]),
					 * soccer.getReward(ah[2],Ba[0], Ba[1])));
					 */
					temp = temp * temp2;
					polic += temp;

					if (max_ <= temp2) {
						max_ = temp2;
						s__ = k;
					}
				}
				polic += rw;
				// System.out.println("polic2  "+polic);
				polic *= (float) Bt[t - 1];
				// System.out.println("polic3  "+polic+ "  "+ar[k]);
				if (max < polic) {
					max = polic;
					Acts[0] = i;
					Acts[1] = j;
					Acts[2] = s__;
				}
			}// end of for i

		return Acts;
	}
	// Ø¨Ø±Ø§ÛŒ Ù�Ø±Ø§Ø± Ø§Ø² Ø¨ÛŒØ´ÛŒÙ†Ù‡ Ù…Ø­Ù„ÛŒ Ù¾Ø³ Ø§Ø² Ù‡Ø±8Ø¨Ø§Ø±
	// Ø§Ù‚Ø¯Ø§Ù…Ø§Øª Ø¨Ù‡ ØµÙˆØ±Øª ØªØµØ§Ø¯Ù�ÛŒ Ø§Ø®Ø° Ù…ÛŒØ´ÙˆÙ†Ø¯.
	// *********************************************************

}
