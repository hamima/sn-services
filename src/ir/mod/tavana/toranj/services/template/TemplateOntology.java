package ir.mod.tavana.toranj.services.template;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;

public class TemplateOntology extends Ontology implements TemplateVocabulary {

	private final static Ontology theInstance = new TemplateOntology();

	public final static Ontology getInstance() {
		return theInstance;
	}

	/**
	 * Constructor
	 */
	public TemplateOntology() {
		// TODO Auto-generated constructor stub
		super(ONTOLOGY_NAME, BasicOntology.getInstance());
	}
}
