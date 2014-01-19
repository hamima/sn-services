package ir.mod.tavana.toranj.services.avatar.polling;

import jade.content.Concept;
import jade.util.leap.List;

public class PollRequestList implements Concept {
	
	private List pollRequestListList;

	public List getPollRequestListList() {
		return pollRequestListList;
	}

	public void setPollRequestListList(List pollRequestListList) {
		this.pollRequestListList = pollRequestListList;
	}

}
