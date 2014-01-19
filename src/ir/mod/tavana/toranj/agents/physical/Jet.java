package ir.mod.tavana.toranj.agents.physical;

import jade.core.Agent;

public class Jet extends Agent {
	private int ID;
	private double X;
	private double Y;
	private double v;
	private double RCS;
	private double manAcc;

	public Jet() {

	}

	public Jet(int ID, double X, double Y, double v, double RCS, double manAcc) {
		this.ID = ID;
		this.X = X;
		this.Y = Y;
		this.v = v;
		this.RCS = RCS;
		this.manAcc = manAcc;
	}

	@Override
	protected void setup() {
		print();
	}

	public void print() {
		System.out.println("Hello, my name is " + this.getLocalName());
	}
}
