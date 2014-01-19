package ir.mod.tavana.toranj.services.mediator;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ConceptSchema;
import jade.content.schema.PrimitiveSchema;
import jade.content.schema.TermSchema;

public class MediatorOntology extends Ontology implements MediatorVocabulary {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Ontology theInstance = new MediatorOntology();
	

	public final static Ontology getInstance() {
		return theInstance;
	}

	public MediatorOntology() {
		super(ONTOLOGY_NAME, BasicOntology.getInstance());

		try {
			add(new AgentActionSchema(REGISTER),ir.mod.tavana.toranj.services.mediator.Register.class);
			add(new AgentActionSchema(DEREGISTER),ir.mod.tavana.toranj.services.mediator.DeRegister.class);
			add(new AgentActionSchema(LOGIN),ir.mod.tavana.toranj.services.mediator.LogIn.class);
			add(new ConceptSchema(PROFILE),Profile.class);
			add(new AgentActionSchema("Poll"),ir.mod.tavana.toranj.services.avatar.polling.Poll.class);
			
			AgentActionSchema as;
			
			as = (AgentActionSchema) getSchema("Poll");
			as.add("question", (PrimitiveSchema)getSchema(BasicOntology.STRING));
			as.add("options", (PrimitiveSchema)getSchema(BasicOntology.STRING));
			as.add("scope", (PrimitiveSchema)getSchema(BasicOntology.STRING));
			as.add("timeout", (PrimitiveSchema)getSchema(BasicOntology.STRING));
			as.setResult((PrimitiveSchema)getSchema(BasicOntology.STRING));

			ConceptSchema cs = (ConceptSchema) getSchema(PROFILE);
			cs.add(NAME, (PrimitiveSchema)getSchema(BasicOntology.STRING));
			cs.add(AGE, (PrimitiveSchema)getSchema(BasicOntology.INTEGER));
			cs.add(TYPE, (PrimitiveSchema)getSchema(BasicOntology.STRING));
			cs.add(QUEST, (PrimitiveSchema)getSchema(BasicOntology.STRING));
			
			
			as = (AgentActionSchema) getSchema(REGISTER);
			as.add(PROFILE, (ConceptSchema) getSchema(PROFILE));
			as.setResult((PrimitiveSchema) getSchema(BasicOntology.STRING));
			
			as = (AgentActionSchema) getSchema(DEREGISTER);
			as.add(AID, (PrimitiveSchema)getSchema(BasicOntology.STRING));
			as.setResult((PrimitiveSchema) getSchema(BasicOntology.STRING));
			
			as = (AgentActionSchema) getSchema(LOGIN);
			as.add(USERNAME, (PrimitiveSchema)getSchema(BasicOntology.STRING));
			as.setResult((PrimitiveSchema) getSchema(BasicOntology.STRING));

		} catch (OntologyException oe) {
			oe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
