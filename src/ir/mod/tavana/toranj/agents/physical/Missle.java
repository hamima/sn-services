package ir.mod.tavana.toranj.agents.physical;

import jade.core.Agent;

public class Missle extends Agent {
	private int ID;
	private double xinit; // X
	private double yinit; // Y
	private double v;
	private double maxRange;
	private double hitRate;

	public Missle() {

	}

	public Missle(int ID, double xinit, double yinit, double v, double maxRange, double hitRate) {
		this.ID = ID;
		this.xinit = xinit;
		this.yinit = yinit;
		this.v = v;
		this.maxRange = maxRange;
		this.hitRate = hitRate;
	}

	@Override
	protected void setup() {
		print();
	}

	public void print() {
		System.out.println("Hello, my name is " + this.getLocalName());
	}
}
