package ir.mod.tavana.toranj.services.polling;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ConceptSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PrimitiveSchema;
import jade.content.schema.TermSchema;

public class PollingServicesOntology extends Ontology implements PollingServicesVocabulary {

	private final static Ontology theInstance = new PollingServicesOntology();

	public final static Ontology getInstance() {
		return theInstance;
	}

	public PollingServicesOntology() {
		super(ONTOLOGY_NAME, BasicOntology.getInstance());

		try {
			add(new AgentActionSchema(POLLING), ir.mod.tavana.toranj.services.polling.PollingMsg.class);
			add(new AgentActionSchema(MONITORING), ir.mod.tavana.toranj.services.polling.MonitoringMsg.class);
			AgentActionSchema as = (AgentActionSchema) getSchema(POLLING);
			as.add(QUESTION, (PrimitiveSchema) getSchema(BasicOntology.STRING));
			as.add(OPTIONS, (PrimitiveSchema) getSchema(BasicOntology.STRING));
			as.add(SCOPE, (PrimitiveSchema) getSchema(BasicOntology.STRING));
			as.add(TIMEOUT, (PrimitiveSchema) getSchema(BasicOntology.STRING));
			as.setResult((PrimitiveSchema) getSchema(BasicOntology.STRING));

			as = (AgentActionSchema) getSchema(MONITORING);
			as.add(QID, (PrimitiveSchema) getSchema(BasicOntology.STRING));
			as.setResult((PrimitiveSchema) getSchema(BasicOntology.STRING));

		} catch (OntologyException oe) {
			oe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
