package ir.mod.tavana.toranj.services.broadcasting;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.ReflectiveIntrospector;
import jade.content.schema.ConceptSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PredicateSchema;
import jade.content.schema.TermSchema;

public class BroadCastingServicesOntology extends Ontology implements BroadcastingServicesVocabulary {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Ontology theInstance = new BroadCastingServicesOntology();
    
	public final static Ontology getInstance() {
		return theInstance;
	}
	
	public BroadCastingServicesOntology() {

		super(ONTOLOGY_NAME , BasicOntology.getInstance(), new ReflectiveIntrospector());
	        try {

	            // adding Concept(s)

	            // adding AgentAction(s)

	            // adding AID(s)
	            ConceptSchema broadcastingSchema = new ConceptSchema(BROADCASTING_AGENT);
	            add(broadcastingSchema, BroadcastingServiceAgent.class);

	            // adding Predicate(s)
	            PredicateSchema BCRequestSchema = new PredicateSchema(BCRequest);
	            add(BCRequestSchema, BCRequest.class);
	            PredicateSchema BCDNSRequestSchema = new PredicateSchema(BCDNSRequest);
	            add(BCDNSRequestSchema, BCDNSRequest.class);
	            PredicateSchema BCDNSReplySchema = new PredicateSchema(BCDNSReply);
	            add(BCDNSReplySchema, BCDNSReply.class);
	            PredicateSchema BCReplySchema = new PredicateSchema(BCReply);
	            add(BCReplySchema, BCReply.class);
	            PredicateSchema BCForwardRequestSchema = new PredicateSchema(BCForwardRequest);
	            add(BCForwardRequestSchema, BCForwardRequest.class);
	            PredicateSchema BCForwardReplySchema = new PredicateSchema(BCForwardReply);
	            add(BCForwardReplySchema, BCForwardReply.class);
	            
	            BCRequestSchema.add(BCRequest_APPICATION, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
	            BCRequestSchema.add(BCRequest_BROADCAST, (TermSchema) getSchema(BasicOntology.BOOLEAN), ObjectSchema.OPTIONAL);
//	            BCRequestSchema.add(BCRequest_REAL_OBJ, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
	            BCRequestSchema.add(BCRequest_RECEIVER_QUERY, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
	            BCRequestSchema.add(BCRequest_RECEIVERS, (TermSchema) getSchema(BasicOntology.AID_ADDRESSES),ObjectSchema.OPTIONAL);

	            BCReplySchema.add(BCReply_BC_REPLY_MSG, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);

	            BCDNSRequestSchema.add(BCDNSRequest_RECEIVER_QUERY, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
	            
	            BCDNSReplySchema.add(BCDNSReply_DNS_RECEIVERS, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);

	            BCForwardRequestSchema.add(BCForwardRequest_CONTENT, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
	            BCForwardRequestSchema.add(BCForwardRequest_ACTUAL_SENDER, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);

	            BCForwardReplySchema.add(BCForwardReply_REPLY, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
	
	        } catch (java.lang.Exception e) {
	        	e.printStackTrace();	            
	        }		
	}
}
