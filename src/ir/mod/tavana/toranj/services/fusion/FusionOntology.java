package ir.mod.tavana.toranj.services.fusion;

import ir.mod.tavana.toranj.services.mediator.MediatorOntology;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;

public class FusionOntology extends Ontology {
	
	private final static Ontology theInstance = new FusionOntology();
	

	public final static Ontology getInstance() {
		return theInstance;
	}

	public FusionOntology() {
		super("FusionOntology", BasicOntology.getInstance());

	}


}
