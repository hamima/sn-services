package ir.mod.tavana.toranj.services.task_allocation;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.ReflectiveIntrospector;
import jade.content.schema.ConceptSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PredicateSchema;
import jade.content.schema.TermSchema;

public class TaskAllocationOntology extends Ontology implements TaskAllocationVocabulary{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Ontology theInstance = new TaskAllocationOntology();
    private static ReflectiveIntrospector introspect = new ReflectiveIntrospector();

	
	public final static Ontology getInstance() {
		return theInstance;
	}

	public TaskAllocationOntology() {
		super(ONTOLOGY_NAME , BasicOntology.getInstance());

			try {
/*				ConceptSchema taskAllocationSchema = new ConceptSchema(TASK_ALLOCATION_AGENT);
				add(taskAllocationSchema, TaskAllocationAgent.class);
*/
	            ConceptSchema BidSchema = new ConceptSchema(Bid);
	            add(BidSchema, Bid.class);
	            
	            ConceptSchema RequestForBidSchema = new ConceptSchema(RequestForBid);
	            add(RequestForBidSchema, RequestForBid.class);
	            
	            BidSchema.add(Bid_My_Bid, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
	            BidSchema.add(Bid_TASK_ID, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
	            
	            BidSchema.add(RequestForBid_ID, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
	            BidSchema.add(RequestForBid_DESCRIPTION, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}