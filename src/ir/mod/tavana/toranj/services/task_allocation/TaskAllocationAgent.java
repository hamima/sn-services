package ir.mod.tavana.toranj.services.task_allocation;

import ir.mod.tavana.toranj.services.box.BoxAgent;
import ir.mod.tavana.toranj.services.broadcasting.BroadCastingServicesOntology;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Vector;


import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class TaskAllocationAgent extends Agent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static TaskManager tm;
	public static AgentManager am;
	public static int Inf = 1000000;
	
	public static final String TYPE = "TaskAllocationAgent";
	public static final String NAME = "TaskAllocationAgent-agents";
	
	private int priceFactor;
	private boolean busy = false;
	private static int idCounter = 0;
	public int id;
	
	private SLCodec codec = new SLCodec();
	
/*	public static TaskAllocationAgent ta;
	public static TaskAllocationAgent theInstance;
	
	private TaskAllocationAgent(){
		super();
	}
	
	public static TaskAllocationAgent getTheInstance() {
		if (theInstance == null)
			theInstance = new TaskAllocationAgent();
		return theInstance;
	}

	public static void setTheInstance(TaskAllocationAgent theInstance) {
		TaskAllocationAgent.theInstance = theInstance;
	}
*/
	protected void setup() {

		getContentManager().registerLanguage(codec, FIPANames.ContentLanguage.FIPA_SL0);
		getContentManager().registerOntology(TaskAllocationOntology.getInstance());

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
/*		dfd.addLanguages(codec.getName());
		dfd.addProtocols(FIPANames.InteractionProtocol.FIPA_REQUEST);
*/		
		ServiceDescription sd = new ServiceDescription();
		sd.setType("TaskAllocationAgent");
		sd.setName("TaskAllocationAgent-agents");
/*		sd.addOntologies(TaskAllocationOntology.getInstance().getName());
		sd.addProtocols(FIPANames.InteractionProtocol.FIPA_REQUEST);
		sd.addLanguages(codec.getName());
*/		
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		tm = new TaskManager();
		am = new AgentManager();

		idCounter++;
		id = idCounter;
		Random r = new Random();
		priceFactor = r.nextInt() % 100;
		priceFactor = priceFactor < 0 ? -priceFactor : priceFactor;

//		System.out.println("Hello! TaskAllocation-agent " + getAID().getName() + " is ready.");
		System.out.println("!!Use language = " + FIPANames.ContentLanguage.FIPA_SL0 + " & ontology = " +
                TaskAllocationOntology.getInstance().getName());
		
		this.addBehaviour(new sendPriceMessage());
	}
	
/*	
	public static TaskAllocationAgent getTa() {
		return ta;
	}

	public static void setTa(TaskAllocationAgent ta) {
		TaskAllocationAgent.ta = ta;
	}
*/
	public void addTask(final String title) {
		
		addBehaviour(new OneShotBehaviour() {
			@Override
			public void action() {
//				am.resetAgents(myAgent);
//				tm.addTask(getNewTask(), myAgent);
			}
		});
	}
	
	public int getPrice(AID agent,Task t) {
/*		addBehaviour(new sendPriceMessage(agent,t.getTheString()));
*/
		return 0;
	}

	private class sendPriceMessage extends CyclicBehaviour {
		
/*		List<AID> listOfAIDs = new ArrayList<AID>();
		List<Agent> listOfAgnets = new ArrayList<Agent>();
*/
		List<AID> freeAgents = new ArrayList<AID>();

		private MessageTemplate mtLang = MessageTemplate.MatchLanguage(FIPANames.ContentLanguage.FIPA_SL0);
		private MessageTemplate mtOnto = MessageTemplate.MatchOntology(TaskAllocationOntology.ONTOLOGY_NAME);

		private int step = 0;

		private long sendingTime = 0;
		private long treshold = 10000;

		private int numberOfReceived = 0;
		private int numberOfIsBusyReceived = 0;
		private int numberOfIsBusySent = 0;
		private ACLMessage requestForBid;
		private Task taskTree;
		private Map<Integer, Task> treeNodes;
		ACLMessage isBusyMsg;

		public void action() {
			switch (step) {
				case 0:
					ACLMessage msg = myAgent.blockingReceive(/*mtOnto*/);
					if (msg != null) {
						if (msg.getPerformative() == ACLMessage.REQUEST) {
//								taskTree = (Task) myAgent.getContentManager().extractContent(msg);
								taskTree = getNewTask();

								treeNodes = new HashMap<Integer, Task>();
								taskTree = breakAsYouWish(taskTree);
								fillMapOfTree(treeNodes,taskTree);

								isBusyMsg = new ACLMessage(ACLMessage.QUERY_IF);
								isBusyMsg.setContent("IsBusy");
								
								setReservePrice(taskTree);
								freeAgents = search(BoxAgent.Type);
/*								List<AID> shouldBeRemoved = new ArrayList<AID>();
*/								
								for (AID isBusyAid: freeAgents) {
									isBusyMsg.addReceiver(isBusyAid);
									numberOfIsBusySent++;
/*									ACLMessage isBusyReplyMsg = blockingReceive(MessageTemplate.MatchInReplyTo(isBusyMsg.getReplyWith()),100000);
									if (isBusyReplyMsg == null || isBusyReplyMsg.getContent().equals("false")) {
										shouldBeRemoved.add(isBusyAid);
									}
*/									
								}
/*								freeAgents.removeAll(shouldBeRemoved);
*/
								isBusyMsg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
								myAgent.send(isBusyMsg);
								sendingTime = System.currentTimeMillis();
								step = 1;
/*								
 * 								if (freeAgents.size() > 0) {
									sendBidRequest(taskTree);
									step = 1;
								} else {
									step = 0;
								}
*/								
						}
					}
					break;

				case 1:
					ACLMessage isBusyReplyMsg = blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
					Vector<AID> notValidReceivers = new Vector<AID>();
					if(isBusyReplyMsg!= null){
						numberOfIsBusyReceived++;
						if (isBusyReplyMsg.getContent() == null && isBusyReplyMsg.getContent().equals("true") ) {
							notValidReceivers.add(isBusyReplyMsg.getSender());
						}
					}
					
					while (numberOfIsBusyReceived != numberOfIsBusySent && (System.currentTimeMillis() - sendingTime) < 100000) {
						isBusyReplyMsg = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
						if (isBusyReplyMsg != null) {
							numberOfIsBusyReceived++;
							if (isBusyReplyMsg.getContent() == null && isBusyReplyMsg.getContent().equals("true") ) {
								notValidReceivers.add(isBusyReplyMsg.getSender());
							}
						}
					}

					if (notValidReceivers.size() > 0) {
						for (AID notFree: notValidReceivers) {
							if (freeAgents.contains(notFree))
								freeAgents.remove(notFree);
						}
					}
					if(freeAgents.size() > 0) {
						sendBidRequest(taskTree);
						step = 2;
					} else {
						step = 0;
					}
					break;
					
//				The bids are gotten from box agents
				case 2:
					for (int i = 0; i < freeAgents.size(); i++) {
						ACLMessage bidMsg = myAgent.blockingReceive(mtOnto);
						Object bidObj = null;
						try {
							bidObj = myAgent.getContentManager().extractContent(bidMsg);
						} catch (UngroundedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (CodecException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (OntologyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (bidMsg != null && bidObj instanceof Bid) {
							numberOfReceived++;

							Bid receivedBid = (Bid) bidObj;
							Task currentTask = treeNodes.get(receivedBid.getTaskId());
							currentTask.setCost(receivedBid.getMyBid(), bidMsg.getSender());
						}
						bidMsg = null;
					}
//				Selecting the bests
					clearAuction(taskTree);
					break;
			}
		}

		private void clearAuction(Task t) {
			if (t.isFinished()) System.out.println("The task is not a tree!");
			while (!t.isFinished()) {
				simpleClearAuction(t);
				PriorityQueue<Task> pQueue;
				pQueue = new PriorityQueue<Task>(10,new TaskComparator());
				addMarked2Q(t, pQueue);
				Iterator<Task> it = pQueue.iterator();
				while (it.hasNext()) {
					Task t2 = it.next();
					AgentTask a = t2.getBestAgent();
					if (a == null) continue;
					ACLMessage isBusyMsg = new ACLMessage(ACLMessage.REQUEST);
					isBusyMsg.addReceiver(a.getBiddingAgent());
					isBusyMsg.setContent("IsBusy");
					isBusyMsg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
					myAgent.send(isBusyMsg);
					ACLMessage isBusyReplyMsg = blockingReceive(MessageTemplate.MatchInReplyTo(isBusyMsg.getReplyWith()));
					if (isBusyReplyMsg.getContent() == "0") {
						removeWinnerBids(t,a);
						ACLMessage doTaskMsg = new ACLMessage(ACLMessage.INFORM);
						doTaskMsg.addReceiver(a.getBiddingAgent());
						doTaskMsg.setContent("DoTask: " + t2.getId());
						doTaskMsg.setPerformative(ACLMessage.INFORM);
						doTaskMsg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
						myAgent.send(doTaskMsg);
//						a.doTask(t2);
						t2.satisfy();
					}
				}
			}
		}

		private void removeWinnerBids(Task t, AgentTask a) {
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
		
		private Task myNext(Iterator<Task> it) {
			while (it.hasNext()) {
				Task t = it.next();
				if (t.isRemoved() || t.isSatisfied()) return null;
				return t;
			}
			return null;
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
		
		private void fillMapOfTree(Map<Integer, Task> treeNodes2, Task taskTree2) {
			// TODO Auto-generated method stub
			treeNodes.put(taskTree.getId(), taskTree2);
			if(taskTree2.isLeaf())
				for(Task childTask:taskTree2.getSubTasks())
					fillMapOfTree(treeNodes2, childTask);
		}

		private void sendBidRequest (Task currentNode) {
			RequestForBid reqForBid = new RequestForBid();
			reqForBid.setDescription(currentNode.getTheString());
			reqForBid.setId(currentNode.getId());

			requestForBid = new ACLMessage(ACLMessage.CFP);
			requestForBid.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
			requestForBid.setOntology(TaskAllocationOntology.ONTOLOGY_NAME);
			for (AID myAg: freeAgents)
				requestForBid.addReceiver(myAg);
			try {
				myAgent.getContentManager().fillContent(requestForBid, reqForBid);
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				RequestForBid req = (RequestForBid) myAgent.getContentManager().extractContent(requestForBid);
				System.out.println(req.getId()+": "+req.getDescription());
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			myAgent.send(requestForBid);

			if (currentNode.isLeaf()) return;
			Vector<Task> subT = currentNode.getSubTasks();
			Iterator<Task> it = subT.iterator();
			while (it.hasNext()) {
				sendBidRequest(it.next());
			}
		}

		private Task breakAsYouWish(Task t) {
			/** to do **/
			return t;
		}

		private void setReservePrice(Task t) {
			t.setReservePrice(getPrice(t));
			if (t.isLeaf()) return;
			Vector<Task> subT = t.getSubTasks();
			for(Task t2 : subT) {
				setReservePrice(t2);
			}
		}

		public int getPrice(Task t) {
			/** to do **/
			int price;
			if (t.isLeaf()) {
				price = priceFactor;
			} else {
				Vector<Task> subTasks = t.getSubTasks();
				if (t.getType() == "And") {
					price = 0;
					for (int i = 0; i < subTasks.size(); i++) {
						price = price + getPrice(subTasks.get(i));
					}
				} else {
					price = Inf;
					for (int i = 0; i < subTasks.size(); i++) {
						int p = getPrice(subTasks.get(i));
						price = price < p ? price : p;
					}
				}
			}
			return price;		
		}

		private java.util.ArrayList<AID> search(String service_type) {
			java.util.ArrayList<AID> providers = new java.util.ArrayList<AID>();
			// Search for services of type "weather-forecast"
			// System.out.println("Agent " + getLocalName() +
			// " searching for services of type \"" + service_type
			// + "\"");
			try {
				// Build the description used as template for the search
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription templateSd = new ServiceDescription();
				templateSd.setType(service_type);
				template.addServices(templateSd);

				SearchConstraints sc = new SearchConstraints();
				// We want to receive 10 results at most
				sc.setMaxResults(new Long(10));

				DFAgentDescription[] results = DFService.search(myAgent, template, sc);
				if (results.length > 0) {
					// System.out.println("Agent " + getLocalName() +
					// " found the following \"" + service_type
					// + "\" services:");
					for (int i = 0; i < results.length; ++i) {
						DFAgentDescription dfd = results[i];
						AID provider = dfd.getName();
						// The same agent may provide several services; we are only
						// interested in the communication one
						Iterator it = dfd.getAllServices();
						while (it.hasNext()) {
							ServiceDescription sd = (ServiceDescription) it.next();
							if (sd.getType().equals(service_type)) {
								// System.out.println("- Service \"" + sd.getName()
								// + "\" provided by agent "
								// + provider.getName());
								providers.add(provider);
							}

						}
					}
				} else {
					// System.out.println("Agent " + getLocalName() +
					// " did not find any \"communication\" service");
				}
			} catch (FIPAException fe) {
				fe.printStackTrace();
			}
			return providers;
		}
	}

	private Task getNewTask() {
		Task tC  = new Task(null,"C","Or");
		
		Task tB1 = new Task(tC,"B","And");
		Task tB2 = new Task(tC,"B","And");
		Task tB3 = new Task(tC,"B","And");
		
		Task tA1 = new Task(tB1,"A","And");
		Task tA2 = new Task(tB1,"A","And");
		Task tA3 = new Task(tB1,"A","And");
		Task tA4 = new Task(tB2,"A","And");
		Task tA5 = new Task(tB2,"A","And");
		Task tA6 = new Task(tB2,"A","And");
		Task tA7 = new Task(tB3,"A","And");
		Task tA8 = new Task(tB3,"A","And");
		Task tA9 = new Task(tB3,"A","And");
		
		Task t11 = new Task(tA1,"GoTo_3_7","And");   tA1.addChild(t11);
		Task t12 = new Task(tA1,"Push_13_17","And"); tA1.addChild(t12);
		Task t21 = new Task(tA2,"GoTo_5_6","And");   tA2.addChild(t21);
		Task t22 = new Task(tA2,"Push_13_17","And"); tA2.addChild(t22);
		Task t31 = new Task(tA3,"GoTo_8_2","And");   tA3.addChild(t31);
		Task t32 = new Task(tA3,"Push_13_17","And"); tA3.addChild(t32);

		Task t41 = new Task(tA4,"GoTo_3_7","And"); tA4.addChild(t41);
		Task t42 = new Task(tA4,"Push_1_1","And"); tA4.addChild(t42);
		Task t51 = new Task(tA5,"GoTo_5_6","And"); tA5.addChild(t51);
		Task t52 = new Task(tA5,"Push_1_1","And"); tA5.addChild(t52);
		Task t61 = new Task(tA6,"GoTo_8_2","And"); tA6.addChild(t61);
		Task t62 = new Task(tA6,"Push_1_1","And"); tA6.addChild(t62);

		Task t71 = new Task(tA7,"GoTo_3_7","And"); tA7.addChild(t71);
		Task t72 = new Task(tA7,"Push_5_7","And"); tA7.addChild(t72);
		Task t81 = new Task(tA8,"GoTo_5_6","And"); tA8.addChild(t81);
		Task t82 = new Task(tA8,"Push_5_7","And"); tA8.addChild(t82);
		Task t91 = new Task(tA9,"GoTo_8_2","And"); tA9.addChild(t91);
		Task t92 = new Task(tA9,"Push_5_7","And"); tA9.addChild(t92);

		tC.addChild(tB1);tC.addChild(tB2);tC.addChild(tB3);
		tB1.addChild(tA1);tB1.addChild(tA2);tB1.addChild(tA3);
		tB2.addChild(tA4);tB2.addChild(tA5);tB2.addChild(tA6);
		tB3.addChild(tA7);tB3.addChild(tA8);tB3.addChild(tA9);

		return tC;
	}
		
/*	public static AgentManager getAgentManager() {
		return am;
	}
	
	public static TaskManager getTaskManager() {
		return tm;
	}
*/	
	protected void takeDown() {
		// Printout a dismissal message
		System.out.println("Bye! TaskAllocation-agent " + getAID().getName() + " is terminating.");
	}
}