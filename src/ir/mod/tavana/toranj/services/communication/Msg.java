package ir.mod.tavana.toranj.services.communication;

import jade.content.AgentAction;

public class Msg implements AgentAction {

	private String msg;
	private int mtype; // 1: direct , 2: multi-hop
	private int priority;
	private int lifetime;
	private String forwardList;
	private String source;
	private String target;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getLifetime() {
		return lifetime;
	}

	public void setLifetime(int lifetime) {
		this.lifetime = lifetime;
	}

	public int getMtype() {
		return mtype;
	}

	public void setMtype(int mtype) {
		this.mtype = mtype;
	}

	public String getForwardList() {
		return forwardList;
	}

	public void setForwardList(String forwardList) {
		this.forwardList = forwardList;
	}

	@Override
	public String toString() {
		return "msg (" + msg + "," + mtype + "," + priority + "," + lifetime + "," + forwardList + ")";
	}
}
