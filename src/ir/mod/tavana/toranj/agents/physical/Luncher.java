package ir.mod.tavana.toranj.agents.physical;

import jade.core.Agent;

public class Luncher extends Agent {
	private int ID;
	private double X;
	private double Y;
	private int maxMissles;
	private double maxRange;
	private double missleVelocity;
	private double missleHitRate;

	public Luncher() {

	}

	public Luncher(int ID, double X, double Y, int maxMissles, double maxRange, double missleVelocity, double missleHitRate) {
		this.ID = ID;
		this.X = X;
		this.Y = Y;
		this.maxMissles = maxMissles;
		this.maxRange = maxRange;
		this.missleVelocity = missleVelocity;
		this.missleHitRate = missleHitRate;
	}

	@Override
	protected void setup() {
		print();
	}

	public void print() {
		System.out.println("Hello, my name is " + this.getLocalName());
	}
}
