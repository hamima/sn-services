package ir.mod.tavana.toranj.services.avatar;

import ir.mod.tavana.toranj.services.avatar.polling.PollRequest;
import ir.mod.tavana.toranj.services.avatar.polling.PollRequestList;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ConceptSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PrimitiveSchema;

public class AvatarOntology extends Ontology implements AvatarVocabulary {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Ontology theInstance = new AvatarOntology();
	

	public final static Ontology getInstance() {
		return theInstance;
	}

	public AvatarOntology() {
		super(ONTOLOGY_NAME, BasicOntology.getInstance());
		
		try {
			add(new AgentActionSchema(POLL),ir.mod.tavana.toranj.services.avatar.polling.Poll.class);
			add(new AgentActionSchema(BROADCAST),ir.mod.tavana.toranj.services.avatar.BroadCast.class);
			add(new AgentActionSchema(SEARCH),ir.mod.tavana.toranj.services.avatar.search.Search.class);
			add(new AgentActionSchema(CONSENSUS),ir.mod.tavana.toranj.services.avatar.Consensus.class);
			add(new AgentActionSchema(LOGOFF),ir.mod.tavana.toranj.services.avatar.LogOff.class);
			add(new AgentActionSchema(ALL_AVATARS),ir.mod.tavana.toranj.services.avatar.ListOfAvatars.class);
			add(new AgentActionSchema(ALLPOLLREQUEST),ir.mod.tavana.toranj.services.avatar.polling.AllPollRequest.class);
			add(new AgentActionSchema(RESPONSEPOLL),ir.mod.tavana.toranj.services.avatar.polling.ResponsePoll.class);
			add(new AgentActionSchema(POLLREQUESTFORWARD),ir.mod.tavana.toranj.services.avatar.polling.PollRequestForward.class);
			add(new AgentActionSchema(ADD_PROPERTY),ir.mod.tavana.toranj.services.avatar.search.AddProperty.class);
			
//			add(new AgentActionSchema(ALL_AVATARS),ir.mod.tavana.toranj.services.avatar.ListOfAvatars.class);
			
			add(new ConceptSchema(POLLREQUEST),PollRequest.class);
			add(new ConceptSchema(POLLREQUESTLIST),PollRequestList.class);
			
/*			
  			add(new ConceptSchema(PROFILE),Profile.class);
			
			ConceptSchema cs = (ConceptSchema) getSchema(PROFILE);
			cs.add(NAME, (PrimitiveSchema)getSchema(BasicOntology.STRING));
			cs.add(AGE, (PrimitiveSchema)getSchema(BasicOntology.INTEGER));
			cs.add(TYPE, (PrimitiveSchema)getSchema(BasicOntology.STRING));
			cs.add(QUEST, (PrimitiveSchema)getSchema(BasicOntology.STRING));
			
			as = (AgentActionSchema) getSchema(POLL);
			as.add(PROF
			ILE, (ConceptSchema) getSchema(PROFILE));
			as.setResult((PrimitiveSchema) getSchema(BasicOntology.STRING));
			
			as = (AgentActionSchema) getSchema(BROADCAST);
			as.add(AID, (PrimitiveSchema)getSchema(BasicOntology.STRING));
			as.setResult((PrimitiveSchema) getSchema(BasicOntology.STRING));
*/
			AgentActionSchema as;
			ConceptSchema cs; 
			
			as = (AgentActionSchema) getSchema(ADD_PROPERTY);
			as.add(ADD_PROPERTY_KEY, (PrimitiveSchema)getSchema(BasicOntology.STRING));
			as.add(ADD_PROPERTY_VALUE, (PrimitiveSchema)getSchema(BasicOntology.STRING));
			
			as = (AgentActionSchema) getSchema(BROADCAST);
			as.add(RECEIVERS, (PrimitiveSchema)getSchema(BasicOntology.STRING));
			as.add(PERFORMANCE, (PrimitiveSchema)getSchema(BasicOntology.STRING));
			as.add(QUERY, (PrimitiveSchema)getSchema(BasicOntology.STRING));
			as.setResult((PrimitiveSchema)getSchema(BasicOntology.STRING));
			
			as = (AgentActionSchema) getSchema(POLL);
			as.add(QUESTION, (PrimitiveSchema)getSchema(BasicOntology.STRING));
			as.add(OPTIONS, (PrimitiveSchema)getSchema(BasicOntology.STRING));
			as.add(SCOPE, (PrimitiveSchema)getSchema(BasicOntology.STRING));
			as.add(TIMEOUT, (PrimitiveSchema)getSchema(BasicOntology.STRING));
			as.setResult((PrimitiveSchema)getSchema(BasicOntology.STRING));
			
			as = (AgentActionSchema) getSchema(ALL_AVATARS);
			as.setResult((PrimitiveSchema)getSchema(BasicOntology.STRING));
			
			cs = (ConceptSchema) getSchema(POLLREQUEST);
			cs.add(POLLREQUEST_QUESTION, (PrimitiveSchema)getSchema(BasicOntology.STRING));				
			cs.add(POLLREQUEST_OPTION, (PrimitiveSchema)getSchema(BasicOntology.STRING));
			
			as = (AgentActionSchema) getSchema(POLLREQUESTFORWARD);
			as.add(POLLREQUESTFORWARD_POLLREQUEST, (ConceptSchema) getSchema(POLLREQUEST));
			
			cs = (ConceptSchema) getSchema(POLLREQUESTLIST);
			cs.add(POLLREQUESTLIST_LIST, (ConceptSchema)getSchema(POLLREQUEST), 0, ObjectSchema.UNLIMITED);
			
			as = (AgentActionSchema) getSchema(ALLPOLLREQUEST);
//			as.setResult((PrimitiveSchema) getSchema(BasicOntology.STRING));
			as.setResult((ConceptSchema)getSchema(POLLREQUESTLIST));
			
			as = (AgentActionSchema) getSchema(RESPONSEPOLL);
			as.add(RESPONSEPOLL_QUESTION, (PrimitiveSchema) getSchema(BasicOntology.STRING)); 
			as.add(RESPONSEPOLL_OPTIONS, (PrimitiveSchema) getSchema(BasicOntology.STRING)); 
			as.add(RESPONSEPOLL_POLLAGENT, (ConceptSchema) getSchema(BasicOntology.AID)); 
			
			
			as = (AgentActionSchema) getSchema(SEARCH);
			
			as = (AgentActionSchema) getSchema(CONSENSUS);
			
//			as = (AgentActionSchema) getSchema(LOGOFF);

		} catch (OntologyException oe) {
			oe.printStackTrace();
		}
	}
}