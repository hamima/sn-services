package ir.mod.tavana.toranj.services.template;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FIPAManagementOntology;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.persistence.PersistenceOntology;
import jade.lang.acl.MessageTemplate;

public class TemplateAgent extends Agent implements TemplateVocabulary{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SLCodec codec = new SLCodec();
	
// 	In this method, the agent is setup and its initial configuration is performed
	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		
//		Here, the language used in this agent is added to the configuration of the agent
		getContentManager().registerLanguage(codec);
		
//		Here, the ontologies corresponding to this agent is registered 
		getContentManager().registerOntology(FIPAManagementOntology.getInstance());
		getContentManager().registerOntology(PersistenceOntology.getInstance());
		getContentManager().registerOntology(TemplateOntology.getInstance());
		
// 		Here the Service Description is defined and registered to the DF, This Service Description should be defined such that it can be discovered in the Directory Facility (DF) 

		DFAgentDescription dfad = new DFAgentDescription();
		dfad.setName(this.getAID());
		dfad.addLanguages(codec.getName());
		dfad.addProtocols(FIPANames.InteractionProtocol.FIPA_REQUEST);
		ServiceDescription sd;
		sd = new ServiceDescription();
		sd.addLanguages(codec.getName());
		sd.addProtocols(FIPANames.InteractionProtocol.FIPA_REQUEST);
		sd.setType(TEMPLATE_TYPE);
		sd.addOntologies(TemplateOntology.getInstance().getName());
		
		// If you want to be defiend as a wsig web service, this properties should be set;
/*		// WSIG properties
		
		Object[] args = getArguments();

		sd.addProperties(new Property(WSIG_FLAG, "true"));

		// Service name
		String wsigServiceName = WSIG_SERVICENAME ;
		if (args.length >= 1) {
			wsigServiceName = (String) args[0];
		}
		sd.setName(wsigServiceName);
*/		

		// If the mapper is needed in the service-agent relation, this codes should be added 
/*		boolean isMapperPresent = false;
		if (args.length >= 2) {
			isMapperPresent = Boolean.parseBoolean((String) args[1]);
		}
		if (isMapperPresent) {
			sd.addProperties(new Property(WSIG_MAPPER, "ir.mod.tavana.toranj.services.template.TemplateMapper"));
		}
*/

		// If you need to run several instances of the same agent, the prefix should be defined in order to distinguish among different instances
/*		String wsigPrefix = "";
		if (args.length >= 3) {
			wsigPrefix = (String) args[2];
		}
		if (wsigPrefix != null && !wsigPrefix.equals("")) {
			sd.addProperties(new Property(WSIG_PREFIX, wsigPrefix));
		}
*/
		dfad.addServices(sd);
		
		// DF registration
		try {
			DFService.register(this, dfad);
		} catch (Exception e) {
			e.printStackTrace();
			doDelete();
		}

		this.addBehaviour(new TemplateBehavior(this));		

	}
	
	public class TemplateBehavior extends CyclicBehaviour{
		
		private static final long serialVersionUID = 1L;
		
		// In this method the template of receiving messages are defined.
		private MessageTemplate msgTemplate = MessageTemplate.MatchOntology(ONTOLOGY_NAME);
		
		public TemplateBehavior(Agent agent) {
			// TODO Auto-generated constructor stub
			super(agent);
		}

		@Override
		public void action() {
			// TODO Auto-generated method stub
			
		}
		
	}

}
