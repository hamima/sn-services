package ir.mod.tavana.toranj.services.search;

import ir.mod.tavana.toranj.services.avatar.AvatarOntology;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.PrimitiveSchema;

public class SearchOntology extends Ontology implements SearchVocabulary{

	private static final long serialVersionUID = 1L;
	private final static Ontology theInstance = new AvatarOntology();
	

	public final static Ontology getInstance() {
		return theInstance;
	}

	
	public SearchOntology(){
		super(ONTOLOGY_NAME, BasicOntology.getInstance());
		
		try {
			
			add(new AgentActionSchema(SEARCH_FORWARD_REQUEST),ir.mod.tavana.toranj.services.avatar.search.SearchForwardRequest.class);
			
			AgentActionSchema as;
			as = (AgentActionSchema) getSchema(SEARCH_FORWARD_REQUEST);
			as.add(SEARCH_FORWARD_REQUEST_KEY, (PrimitiveSchema)getSchema(BasicOntology.STRING));
			as.add(SEARCH_FORWARD_REQUEST_VALUE, (PrimitiveSchema)getSchema(BasicOntology.STRING));
			as.add(SEARCH_FORWARD_REQUEST_OPERATOR, (PrimitiveSchema)getSchema(BasicOntology.STRING));
			as.setResult((PrimitiveSchema)getSchema(BasicOntology.STRING));

			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
}
