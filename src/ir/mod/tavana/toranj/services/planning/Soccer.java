package ir.mod.tavana.toranj.services.planning;


import java.util.*;

/**
 * @author Feng Wu
 *
 */
 class Model {
   public final static double OBS_PROB = 0.9;
   public final static int PROBLEM_SOCCER2x3 = 1;
   public final static int PROBLEM_SOCCER3x3 = 2;

   public int problemID;
   public int numStates;
   public int numHorizons;
   public static int numAct0=6, numAct1=6;
   public static int numObs0=11, numObs1=11;

   public Model(int h) {
       numHorizons = h;
   }
}

 class Soccer extends Model {
	public static int GRID_X = 2;
	public static int GRID_Y = 3;
	public static int GOAL_X = GRID_X - 1;
	public static int GOAL_Y = 1;
	
	public Map<String, Integer> stateMap;
	public ArrayList<SoccerState> stateList;
	
	public Set<Integer>[][][] prevStates;
	public Set<Integer>[][][] nextStates;
	
	public int[][][][] transMap;
	public int[][] obsMap;
	
	public double[] startDist;
	public int initState;
	
	@SuppressWarnings("unchecked")
	public Soccer(int h, int id)
	{
		super(h);
		super.problemID = id;
		if (id == Model.PROBLEM_SOCCER2x3)
		{
			Soccer.GRID_X = 2;
			Soccer.GRID_Y = 3;
			Soccer.GOAL_X = 1;
			Soccer.GOAL_Y = 1;
		}
		else if (id == Model.PROBLEM_SOCCER3x3)
		{
			Soccer.GRID_X = 3;
			Soccer.GRID_Y = 3;
			Soccer.GOAL_X = 2;
			Soccer.GOAL_Y = 1;
                        System.out.println("Soccer 3*3");
		}
		System.out.print("Start init soccer problem ");
		
		stateMap = new HashMap<String, Integer>();
		stateList = new ArrayList<SoccerState>();
		
		int stateCnt = 0;
		for (int x0 = 0; x0 < Soccer.GRID_X; ++x0)
			for (int y0 = 0; y0 < Soccer.GRID_Y; ++y0)
				for (int d0 = 0; d0 < 4; ++d0)
				{
					Vec3 a0 = new Vec3(x0, y0, d0);
					for (int x1 = 0; x1 < Soccer.GRID_X; ++x1)
						for (int y1 = 0; y1 < Soccer.GRID_Y; ++y1)
							for (int d1 = 0; d1 < 4; ++d1)
							{
								Vec3 a1 = new Vec3(x1, y1, d1);
								if (a0.equalPos(a1)) 
									continue;
								for (int x2 = 0; x2 < Soccer.GRID_X; ++x2)
									for (int y2 = 0; y2 < Soccer.GRID_Y; ++y2)
									{
										Vec3 o = new Vec3(x2, y2);
										if (o.equalPos(a0) || o.equalPos(a1)) 
											continue;
										for (int b = 0; b < 2; ++b)
										{
											SoccerState state = new SoccerState(b, a0, a1, o);
											stateMap.put(state.toHashCode(), stateCnt);
											stateList.add(state);
											++stateCnt;
										}
									}
							}
				}
		stateMap.put(Soccer.STATE_GOAL.toHashCode(), stateCnt);
		stateList.add(Soccer.STATE_GOAL);
		++stateCnt;
		
		stateMap.put(Soccer.STATE_LOSE.toHashCode(), stateCnt);
		stateList.add(Soccer.STATE_LOSE);
		++stateCnt;
		
		stateMap.put(Soccer.STATE_OUT.toHashCode(), stateCnt);
		stateList.add(Soccer.STATE_OUT);
		++stateCnt;
		
		super.numStates = stateCnt;
		
		
		startDist = new double[super.numStates];
		Arrays.fill(startDist, 0.0);
		String key = 0 + "|" + // ball
				0 + ":" + 0 + ":" + 3 + "|" + // agent 0
				0 + ":" + 2 + ":" + 3 + "|" + // agent 1
				(Soccer.GOAL_X) + ":" + (Soccer.GOAL_Y) + ":" + 0; // opponent
              /*  String key = 0 + "|" + // ball
				1 + ":" + 0 + ":" + 3 + "|" + // agent 0
				0 + ":" + 2 + ":" + 3 + "|" + // agent 1
				(Soccer.GOAL_X) + ":" + (Soccer.GOAL_Y) + ":" + 0; // opponent    for 3*3 */
		if (stateMap.containsKey(key))
		{
			initState = stateMap.get(key); 
                        System.out.println(" initial  "+key);
		}
		startDist[initState] = 1.0;
		
		System.out.print("... ");
		prevStates = new HashSet[super.numAct0][super.numAct1][super.numStates];
		nextStates = new HashSet[super.numStates][super.numAct0][super.numAct1];
		
		transMap = new int[super.numStates][super.numAct0][super.numAct1][4];
		for (int s = 0; s < super.numStates; ++s)
			for (int a0 = 0; a0 < super.numAct0; ++a0)
				for (int a1 = 0; a1 < super.numAct1; ++a1)
				{
					transMap[s][a0][a1] = nextState(s, a0, a1);
					for (int i = 0; i < 4; ++i)
					{
						int s_ = transMap[s][a0][a1][i];
						if (nextStates[s][a0][a1] == null)
							nextStates[s][a0][a1] = new HashSet<Integer>();
						nextStates[s][a0][a1].add(s_);
						if (prevStates[a0][a1][s_] == null)
							prevStates[a0][a1][s_] = new HashSet<Integer>();
						prevStates[a0][a1][s_].add(s);
					}
				}
		
		System.out.print("... ");
		obsMap = new int[super.numStates][2];
		for (int s = 0; s < super.numStates; ++s)
			obsMap[s] = generateObs(s);
		
		System.out.println("ok!");
		System.out.println("states: " + super.numStates);
		System.out.println("act 0: " + super.numAct0);
		System.out.println("act 1: " + super.numAct1);
		System.out.println("obs 0: " + super.numObs0);
		System.out.println("obs 1: " + super.numObs1);
	}
	
	public double[] getStartDist()
	{
		return startDist;
	}
	
	public double getStartDist(int s)
	{
		return startDist[s];
	}
	
	public int[] getObsForState(int s)
	{
		return obsMap[s];
	}
	
	public Set<Integer> getNextStates(int s, int a0, int a1)
	{
		return nextStates[s][a0][a1];
	}
	
	public Set<Integer> getPrevStates(int a0, int a1, int s_)
	{
		return prevStates[a0][a1][s_];
                
	}
	public int getnumState()
        {
            return super.numStates;
        }
	public double getTrans(int s, int a0, int a1, int s_)
	{
		int[] result = transMap[s][a0][a1];
//		int[] result = nextState(s, a0, a1);
		
		double prob = 0.0;
		if (s_ == result[3])
			prob += Soccer.ACTION_PROB * Soccer.ACTION_PROB;
                else if (s_ == result[2])
			prob += Soccer.ACTION_PROB * (1 - Soccer.ACTION_PROB);
                else if (s_ == result[1])
			prob += Soccer.ACTION_PROB * (1 - Soccer.ACTION_PROB);
                else if (s_ == result[0])
			prob += (1 - Soccer.ACTION_PROB) * (1 - Soccer.ACTION_PROB);
		return prob;
	}
	
	public double getObs(int o0, int o1, int a0, int a1, int s_)
	{
		double prob0 = Model.OBS_PROB;
		double prob1 = (1.0 - prob0) / (numObs0 * numObs1 - 1);
		
		int[] result = obsMap[s_];
		if (o0 == result[0] && o1 == result[1])
			return prob0;
		else
			return prob1;
	}
	//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        public double getObs1(int o0,int agent,  int s_)
	{
		double prob0 = Model.OBS_PROB*0.1;
		double prob1 = (1.0 - prob0) / (numObs0 * numObs1 - 1);
		
		int[] result = obsMap[s_];
		if ((o0 == result[0]&&agent == 0)||(o0 == result[1]&&agent == 1))
			return prob0;
		else
			return prob1;
	}
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	public double getReward(int s, int a1, int a2, int s_)
	{
		SoccerState state_ = stateList.get(s_);
		
		if (state_.equalState(Soccer.STATE_GOAL))
			return 100.0;
		if (state_.equalState(Soccer.STATE_OUT))
			return -20.0;
		if (state_.equalState(Soccer.STATE_LOSE))
			return -50.0;
		return -2.0;
	}
	
	public double getReward(int s, int a0, int a1)
	{
		double tempReward = 0.0D;
		Set<Integer> nStates = nextStates[s][a0][a1];
		if (nStates != null)
		{
			for (int s_ : nStates)
				tempReward += getTrans(s, a0, a1, s_) * getReward(s, a0, a1, s_);
		}
//		for (int s_ = 0; s_ < numStates; ++s_) 
//			tempReward += this.getTrans(s, a0, a1, s_) * this.getReward(s, a0, a1, s_);
             
		return tempReward;
	}
	
	public int[] nextState(int s, int a0, int a1)
	{
		SoccerState state = stateList.get(s);
		if (state.equalState(Soccer.STATE_GOAL) || state.equalState(Soccer.STATE_LOSE) || state.equalState(Soccer.STATE_OUT))
		{
			return new int[]{initState, initState, initState, initState};
		}
		
		SoccerState[] sList = new SoccerState[4];
		
		// move action
		sList[0] = opponentMove(state);	  //!a0 &!a1
		sList[1] = agentMove(0, sList[0], a0);// a0 &!a1
		sList[2] = agentMove(1, sList[0], a1);//!a0 & a1
		sList[3] = agentMove(1, sList[1], a1);// a0 & a1
		
		// pass action & check ball out of field
		sList[1] = agentPass(0, sList[1], a0, a1);// a0 &!a1
		sList[2] = agentPass(1, sList[2], a0, a1);//!a0 & a1
		sList[3] = agentPass(0, sList[3], a0, a1);// a0 & a1
		sList[3] = agentPass(1, sList[3], a0, a1);// a0 & a1
		
		// check score goal
		for (int i = 0; i < sList.length; ++i)
			if (isScoreGoal(sList[i]))
				sList[i] = Soccer.STATE_GOAL;
		
		// check lose ball
		for (int i = 0; i < sList.length; ++i)
			if (isLoseBall(sList[i]))
				sList[i] = Soccer.STATE_LOSE;
		
		// check opponent failure
		for (int i = 0; i < sList.length; ++i)
			if (isOppFailed(sList[i]))
            	sList[i].opponent = new Vec3(state.opponent);
		
		int[] sArray = new int[4];
		for (int i = 0; i < sList.length; ++i)
		{
			String key = sList[i].toHashCode();
			if (stateMap.containsKey(key))
				sArray[i] = stateMap.get(key);
			else
				sArray[i] = 0;
		}
		
		return sArray;
	}
	
	private int[] generateObs(int s)
	{
		SoccerState state = stateList.get(s);
		if (state.equalState(Soccer.STATE_GOAL))
			return new int[]{10, 10};
		if (state.equalState(Soccer.STATE_LOSE))
			return new int[]{10, 10};
		if (state.equalState(Soccer.STATE_OUT))
			return new int[]{10, 10};
		
		int o0 = 2 * getAheadObject(0, state) + state.ball;
		int o1 = 2 * getAheadObject(1, state) + state.ball;
		return new int[]{o0, o1};
	}
	
	private int getAheadObject(int idx, SoccerState state)//عاملدر جهتی که هست چه میبیند
	{
		Vec3 opp = state.opponent;
		Vec3 mate, agent;
		if (idx == 0)
		{
			mate = state.agent1;
			agent = state.agent0;
		}
		else
		{
			mate = state.agent0;
			agent = state.agent1;
		}
		
		int aheadX = 0, aheadY = 0;
		switch (agent.D)
		{
		case Soccer.DIR_NORTH:
			aheadX = agent.X;
			aheadY = agent.Y + 1;
			break;
		case Soccer.DIR_SOUTH:
			aheadX = agent.X;
			aheadY = agent.Y - 1;
			break;
		case Soccer.DIR_WEST:
			aheadX = agent.X - 1;
			aheadY = agent.Y;
			break;
		case Soccer.DIR_EAST:
			aheadX = agent.X + 1;
			aheadY = agent.Y;
			break;
		}
		
		Vec3 ahead = new Vec3(aheadX, aheadY);
		if (!withinGrid(ahead))
			return Soccer.OBS_WALL;
		if (mate.equalPos(ahead))
			return Soccer.OBS_TEAM;
		if (opp.equalPos(ahead))
			return Soccer.OBS_OPPT;
		if (withinGoal(ahead))
			return Soccer.OBS_GOAL;
		return Soccer.OBS_FREE;
	}
	
	private SoccerState opponentMove(SoccerState state)
	{
		SoccerState state_ = new SoccerState(state);
		
		Vec3 opp = state_.opponent;
		int[] oppX = new int[]{opp.X, opp.X, opp.X + 1, opp.X - 1};
		int[] oppY = new int[]{opp.Y + 1, opp.Y - 1, opp.Y, opp.Y};
		
		Vec3 ball = (state_.ball == SoccerState.BALL_AGENT0) ? state.agent0 : state.agent1;
		Vec3 mate = (state_.ball == SoccerState.BALL_AGENT0) ? state.agent1 : state.agent0;
		
		double minEva = Double.POSITIVE_INFINITY;
		for (int i = 0; i < 4; ++i)
		{
			if (!withinGrid(oppX[i], oppY[i]))
				continue;
			if (oppX[i] == mate.X && oppY[i] == mate.Y)
				continue;
			double eva = Math.abs(ball.X - oppX[i]) + Math.abs(ball.Y - oppY[i]);
                        //agar be toop nazdiktar mishe besamte onja mire
			if (eva < minEva)
			{
				minEva = eva;
				opp.X = oppX[i];
				opp.Y = oppY[i];
			}
		}
		return state_;
	}
	
	private SoccerState agentPass(int idx, SoccerState state, int act0, int act1)
	{
		if (idx == 0 && act0 == Soccer.ACT_PASS && state.ball == SoccerState.BALL_AGENT0)
		{
			if (act1 == Soccer.ACT_STAY)
				state.ball = SoccerState.BALL_AGENT1;
			else
				return Soccer.STATE_LOSE;
		}
		if (idx == 1 && act1 == Soccer.ACT_PASS && state.ball == SoccerState.BALL_AGENT1)
		{
			if (act0 == Soccer.ACT_STAY)
				state.ball = SoccerState.BALL_AGENT0;
			else
				return Soccer.STATE_LOSE;
		}
		return state;
	}
	
	private SoccerState agentMove(int idx, SoccerState state, int act)
	{
		SoccerState state_ = new SoccerState(state);
		if (act == Soccer.ACT_STAY || act == Soccer.ACT_PASS)
			return state_;
		
		Vec3 opp = state_.opponent;
		Vec3 agent, mate;
		if (idx == 0)
		{
			agent = state_.agent0;
			mate = state_.agent1;//یار
		}
		else
		{
			agent = state_.agent1;
			mate = state_.agent0;
		}
		
		Vec3 pos = new Vec3(agent);
		switch (act)
		{
		case Soccer.ACT_NORTH:
			pos.D = Soccer.DIR_NORTH;
			pos.Y += 1;
			break;
		case Soccer.ACT_SOUTH:
			pos.D = Soccer.DIR_SOUTH;
			pos.Y -= 1;
			break;
		case Soccer.ACT_WEST:
			pos.D = Soccer.DIR_WEST;
			pos.X -= 1;
			break;
		case Soccer.ACT_EAST:
			pos.D = Soccer.DIR_EAST;
			pos.X += 1;
			break;
		default:
			break;
		}
		
		agent.D = pos.D;
		if (withinGrid(pos) && !pos.equalPos(opp) && !pos.equalPos(mate))
		{
			agent.X = pos.X;
			agent.Y = pos.Y;
		}
		
		return state_;
	}
	
	private boolean isOppFailed(SoccerState state)
    {
        if ((state.ball == SoccerState.BALL_AGENT0) && state.opponent.equalPos(state.agent1))
            return true;
        if ((state.ball == SoccerState.BALL_AGENT1) && state.opponent.equalPos(state.agent0))
            return true;
        return false;
    }
	
	private boolean isLoseBall(SoccerState state)
	{
		if (state.ball == SoccerState.BALL_LOSE)
			return true;
		if ((state.ball == SoccerState.BALL_AGENT0) && state.opponent.equalPos(state.agent0))
			return true;
		if ((state.ball == SoccerState.BALL_AGENT1) && state.opponent.equalPos(state.agent1))
			return true;
		return false;
	}
	
	private boolean isScoreGoal(SoccerState state)
	{
		if (state.ball == SoccerState.BALL_GOAL)
			return true;
		if ((state.ball == SoccerState.BALL_AGENT0) && withinGoal(state.agent0))
			return true;
		if ((state.ball == SoccerState.BALL_AGENT1) && withinGoal(state.agent1))
			return true;
		return false;
	}
	
	private boolean withinGrid(Vec3 pos)
	{
		return withinGrid(pos.X, pos.Y);
	}
	
	private boolean withinGrid(int X, int Y)
	{
		return ((X >= 0) && (X < Soccer.GRID_X) && (Y >= 0) && (Y < Soccer.GRID_Y));
	}
	
	private boolean withinGoal(Vec3 pos)
	{
		return ((pos.X == Soccer.GOAL_X) && (pos.Y == Soccer.GOAL_Y));
	}
	
	public final static SoccerState STATE_GOAL = new SoccerState(SoccerState.BALL_GOAL);
	public final static SoccerState STATE_LOSE = new SoccerState(SoccerState.BALL_LOSE);
	public final static SoccerState STATE_OUT = new SoccerState(SoccerState.BALL_OUT);
	
	public final static double ACTION_PROB = 0.9;
	
	public final static int ACT_STAY = 0;
	public final static int ACT_NORTH = 1;
	public final static int ACT_SOUTH = 2;
	public final static int ACT_WEST = 3;
	public final static int ACT_EAST = 4;
	public final static int ACT_PASS = 5;
	
	public final static int OBS_FREE = 0;
	public final static int OBS_WALL = 1;
	public final static int OBS_TEAM = 2;
	public final static int OBS_OPPT = 3;
	public final static int OBS_GOAL = 4;
	
	public final static int DIR_NORTH = 0;
	public final static int DIR_SOUTH = 1;
	public final static int DIR_WEST = 2;
	public final static int DIR_EAST = 3;

	public String finalPlan() {
		// TODO Auto-generated method stub
		return null;
	}

/*    public static void main(String[] args) {
        Soccer soccer = new Soccer(100, Model.PROBLEM_SOCCER2x3);
        //حالت بعدی3840برابر 541است به ازای هر اقدامی 
        Random rand = new Random();
        System.out.println("Run random policy.");

        int state = 0;
        double acc = 0.0, thr = rand.nextDouble();
   
        for (int s = 0; s < soccer.numStates; ++s) {
            
        //    System.out.println(" saeedeh"+soccer.stateMap.get(s));
            acc += soccer.getStartDist(s);
            if (acc >= thr) {
                state = s;
           System.out.println("Run  "+soccer.getStartDist(s)+"     "+s);
               break;
            }
        }
      // for (int i = 0; i <6;i++)
        //   for(int j=0;j<6;j++)
          //     for(int k=0;k<4;k++)
              {
                System.out.println(" transmap"+soccer.transMap[state][i][j][k] + " k "+k);
                state =soccer.transMap[state][i][j][0];

               }
    }

        double totalReward = 0.0;
        for (int t = 0; t < soccer.numHorizons; ++t) {
            System.out.println("Step: " + t);

            int a0 = rand.nextInt(soccer.numAct0);
            int a1 = rand.nextInt(soccer.numAct1);
            System.out.println(" Select action: " + a0 + " " + a1);

            int state_ = 0;
            acc = 0.0; thr = rand.nextDouble();
            for (int s = 0; s < soccer.numStates; ++s) {
                acc += soccer.getTrans(state, a0, a1, s);
                if (acc >= thr) {
                    state_ = s;
                    break;
                }
            }

            double reward = soccer.getReward(state, a0, a1, state_);
            totalReward += reward;
            state = state_;
        }

        System.out.println("Total reward: " + totalReward);
    }*/
}

class SoccerState {
	public int ball;
	public Vec3 agent0;
	public Vec3 agent1;
	public Vec3 opponent;
	
	public SoccerState(int b)
	{
		ball = b;
		agent0 = agent1 = opponent = null;
	}
	
	public SoccerState(int b, Vec3 a0, Vec3 a1, Vec3 o)
	{
		ball = b;
		agent0 = a0;
		agent1 = a1;
		opponent = o;
	}
	
	public SoccerState(SoccerState state)
	{
		ball = state.ball;
		agent0 = new Vec3(state.agent0);
		agent1 = new Vec3(state.agent1);
		opponent = new Vec3(state.opponent);
	}
	
	public String toHashCode()
	{
		String str0 = (agent0 == null) ? 0 + ":" + 0 + ":" + 0 : agent0.toHashCode();//agent0 is kind of vec3m   
		String str1 = (agent1 == null) ? 0 + ":" + 0 + ":" + 0 : agent1.toHashCode();
		String str2 = (opponent == null) ? 0 + ":" + 0 + ":" + 0 : opponent.toHashCode();
		return ball + "|" + str0 + "|" + str1 + "|" + str2;
	}
	
	public boolean equalState(SoccerState state)
	{
		if (state == null || ball != state.ball)
			return false;
		if (ball == SoccerState.BALL_AGENT0 || ball == SoccerState.BALL_AGENT1)
		{
			return (agent0.equalTo(state.agent0) &&
					agent1.equalTo(state.agent1) &&
					opponent.equalTo(state.opponent));
		}
		return true;
	}
	
	public final static int BALL_AGENT0 = 0;
	public final static int BALL_AGENT1 = 1;
	public final static int BALL_LOSE = 3;
	public final static int BALL_GOAL = 4;
	public final static int BALL_OUT = 5;
}

class Vec3 {
	public int X, Y, D;
	
	public Vec3() 
	{ 
		X = Y = D = 0;
	}
	
	public Vec3(int x, int y) 
	{ 
		X = x; 
		Y = y; 
		D = 0;
	}
	
	public Vec3(int x, int y, int d) 
	{ 
		X = x; 
		Y = y; 
		D = d; 
	}
	
	public Vec3(Vec3 v)
	{
		X = v.X;
		Y = v.Y;
		D = v.D;
	}
	
	public String toHashCode()
	{
		return X+":"+Y+":"+D;
	}

	public boolean equalPos(Vec3 pos) 
	{ 
		if (pos == null)
			return false;
		return ((X == pos.X) && (Y == pos.Y));
	}
	
	public boolean equalTo(Vec3 pos) 
	{ 
		if (pos == null)
			return false;
		return ((X == pos.X) && (Y == pos.Y) && (D == pos.D));
	}
}
