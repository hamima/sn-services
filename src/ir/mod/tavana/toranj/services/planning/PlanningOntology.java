package ir.mod.tavana.toranj.services.planning;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.PredicateSchema;
import jade.content.schema.PrimitiveSchema;

public class PlanningOntology extends Ontology implements PlanningVocabulary {
	
	private final static Ontology theInstance = new PlanningOntology();

	public final static Ontology getInstance() {
		return theInstance;
	}

	public PlanningOntology() {
		super(PLANNING_ONTOLOGY_NAME, BasicOntology.getInstance());

		try {
			add(new AgentActionSchema(PLAN),ir.mod.tavana.toranj.services.planning.Plan.class);
			
			AgentActionSchema as = (AgentActionSchema)getSchema(PLAN);
			as.add(PLAN_NUMBER_OF_AGENTS, (PrimitiveSchema)getSchema(BasicOntology.INTEGER));
			as.add(PLAN_NUMBER_OF_STEPS, (PrimitiveSchema)getSchema(BasicOntology.INTEGER));
			as.setResult((PrimitiveSchema) getSchema(BasicOntology.STRING));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
