package ir.mod.tavana.toranj.services.mediator;

public interface MediatorVocabulary {
	
	public static final String ONTOLOGY_NAME = "defa-ontology-mediator";
	
	public static final String WSIG_SERVICENAME = "Mediator";
	
	public static final String AVATAR_ID = "AvatarId";
	public static final String AVATAR_TYPE = "AvatarS-Agent";


//	Actions
	public static final String REGISTER = "register";
	public static final String DEREGISTER = "deregister";
	public static final String LOGIN = "login";
	public static final String TEST = "test";

//  Concepts
	public static final String PROFILE = "profile";
	
// Primitives
	public static final String NAME = "name";
	public static final String QUEST = "quest";
	public static final String TYPE = "type";
	public static final String AGE = "age";
	
	public static final String AID = "aid";
	
	public static final String USERNAME = "username";

	
	
//	Service Types
	public static final String POLLING_TYPE = "PollingS-Agent";
	public static final String POLLING_ONTOLOGY_NAME = "defa-ontology-polling";	

	
}
