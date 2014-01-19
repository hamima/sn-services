package ir.mod.tavana.toranj.services.buffer;

import ir.mod.tavana.toranj.services.mediator.MediatorOntology;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.PrimitiveSchema;

public class BufferOntology extends Ontology implements BufferVocabulary {

	private final static Ontology theInstance = new MediatorOntology();

	public final static Ontology getInstance() {
		return theInstance;
	}

	public BufferOntology() {
		// TODO Auto-generated constructor stub
		super(ONTOLOGY_NAME, BasicOntology.getInstance());
		
		// Inja method haye buffer tarif mishavad
		
		try {
			add(new AgentActionSchema(SET),ir.mod.tavana.toranj.services.buffer.Set.class);
			add(new AgentActionSchema(GET),ir.mod.tavana.toranj.services.buffer.Get.class);
			
			AgentActionSchema as;
			as = (AgentActionSchema) getSchema(SET);
			as.add(SET_AGENTID, (PrimitiveSchema)getSchema(BasicOntology.INTEGER));
			as.add(SET_VALUE, (PrimitiveSchema)getSchema(BasicOntology.INTEGER));

			as = (AgentActionSchema) getSchema(GET);
			as.add(GET_PREVEIOUSSTATE, (PrimitiveSchema)getSchema(BasicOntology.INTEGER));
			as.add(GET_TEMP, (PrimitiveSchema)getSchema(BasicOntology.INTEGER));
					
		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}
