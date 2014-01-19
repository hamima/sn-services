package ir.mod.tavana.toranj.services.fusion;

import ir.mod.tavana.toranj.services.avatar.AvatarOntology;
import ir.mod.tavana.toranj.services.polling.PollingServicesOntology;
import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FIPAManagementOntology;
import jade.domain.persistence.PersistenceOntology;

public class FusionAgent extends Agent{

	
	private SLCodec codec = new SLCodec();

	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(FIPAManagementOntology.getInstance());
		getContentManager().registerOntology(PersistenceOntology.getInstance());
		getContentManager().registerOntology(FusionOntology.getInstance());
		
		
		
	}

}
