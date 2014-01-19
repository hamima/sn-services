package ir.mod.tavana.toranj.agents.physical;

import java.util.PriorityQueue;
import java.util.Queue;

public class Sync {
	private Queue<String> msgs = new PriorityQueue<String>();
	
	public int getSize() {
		return msgs.size();
	}

	public String pop() {
		return msgs.remove();
	}

	public void push(String msg) {
		msgs.add(msg);
	}
}
