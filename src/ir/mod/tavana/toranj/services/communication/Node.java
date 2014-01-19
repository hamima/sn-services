package ir.mod.tavana.toranj.services.communication;

/*
 * Class Node
 * This class is called to store useful informations : index and cost in each "node" of the table.   
 */

public class Node {

	private int index;
	private int cost;
	
	/*
	 * init
	 */
	public Node(){
		index = 0;
		cost= 0;	
	}
	
	public Node(int pIndex, int pCost){
		index = pIndex;
		cost = pCost;
	}
	  
	public int getIndex(){
		return index;
	}
	public int getCost(){
		return cost;
	}
	
	public void setIndex(int pIndex){
		index = pIndex;
	}
	
	public void setCost(int pCost){
		cost = pCost;
	}

	
}
