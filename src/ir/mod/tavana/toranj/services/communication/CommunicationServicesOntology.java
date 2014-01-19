package ir.mod.tavana.toranj.services.communication;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ConceptSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PrimitiveSchema;
import jade.content.schema.TermSchema;

public class CommunicationServicesOntology extends Ontology implements CommunicationServicesVocabulary{

	private final static Ontology theInstance = new CommunicationServicesOntology();

	public final static Ontology getInstance() {
		return theInstance;
	}

	public CommunicationServicesOntology() {
		super(ONTOLOGY_NAME, BasicOntology.getInstance());

		try {
			add(new AgentActionSchema(SUM), Sum.class);
			add(new AgentActionSchema(USERAGENTS), SearchUsers.class);
			add(new AgentActionSchema(SEND), Msg.class);
			
			AgentActionSchema as = (AgentActionSchema) getSchema(SUM);
			as.add(FIRST_ELEMENT, (PrimitiveSchema) getSchema(BasicOntology.FLOAT));
			as.add(SECOND_ELEMENT, (PrimitiveSchema) getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL);
			as.setResult((PrimitiveSchema)getSchema(BasicOntology.FLOAT));
			
			as = (AgentActionSchema) getSchema(USERAGENTS);
			as.add(STYPE, (PrimitiveSchema) getSchema(BasicOntology.STRING));
			as.setResult((PrimitiveSchema)getSchema(BasicOntology.STRING));

			// communication
			as = (AgentActionSchema) getSchema(SEND);
			as.add(MSG, (PrimitiveSchema) getSchema(BasicOntology.STRING));
			as.add(MTYPE, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
			as.add(PRIORITY, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
			as.add(LIFETIME, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
			as.add(FORWARDLIST, (PrimitiveSchema) getSchema(BasicOntology.STRING));
			as.add(SOURCE, (PrimitiveSchema) getSchema(BasicOntology.STRING));
			as.add(TARGET, (PrimitiveSchema) getSchema(BasicOntology.STRING));
			as.setResult((PrimitiveSchema)getSchema(BasicOntology.STRING));

		} catch (OntologyException oe) {
			oe.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
