package ir.mod.tavana.toranj.services.avatar;

public interface AvatarVocabulary {

	public static final String ONTOLOGY_NAME = "defa-ontology-avatar";

	public static final String WSIG_SERVICENAME = "Avatar";

	public static final String AVATAR_TYPE = "AvatarS-Agent";

//	WSIG Vocabulary
	public static final String WSIG_FLAG = "wsig";
	public static final String WSIG_MAPPER = "wsig-mapper";
	public static final String WSIG_PREFIX = "wsig-prefix";
	
//	Service Types
	public static final String POLLING_TYPE = "PollingS-Agent";
	public static final String SEARCH_TYPE = "SearchS-Agent";

//  Avatar Ontologies Needed
	public static final String BROADCASTING_ONTOLOGY_NAME = "defa-broadcasting_ontology";
	public static final String SEARCH_ONTOLOGY_NAME = "defa-ontology-search";
	public static final String POLLING_ONTOLOGY_NAME = "defa-ontology-polling";  
	
//	Actions
	public static final String POLL = "Poll";
	public static final String BROADCAST = "Broadcast";
	public static final String CONSENSUS = "Consensus";
	public static final String SEARCH = "Search";
	public static final String LOGOFF = "LogOff";
	public static final String ALL_AVATARS = "ListOfAvatars";
	public static final String ALLPOLLREQUEST = "AllPollRequest";
	public static final String RESPONSEPOLL = "ResponsePoll";
	public static final String POLLREQUESTFORWARD = "PollRequestForward";
	public static final String ADD_PROPERTY = "AddProperty";
	
//	Concepts
	public static final String POLLREQUEST = "PollRequest";
	public static final String POLLREQUEST_QUESTION = "question";
	public static final String POLLREQUEST_OPTION = "option";
	public static final String POLLREQUESTLIST = "PollRequestList";
	public static final String POLLREQUESTLIST_LIST = "PollRequestListList";
	
//	Broadcasting Fields	
	public static final String BROADCASTING_TYPE = "Broadcastings-Agent";
	public static final String RECEIVERS = "receivers";
	public static final String PERFORMANCE = "performance";
	public static final String QUERY = "query";
	
//	Add Property Fields
	public static final String ADD_PROPERTY_KEY = "key";
	public static final String ADD_PROPERTY_VALUE = "value";
	
//	Polling Fields	
	public static final String QUESTION = "question";
	public static final String OPTIONS = "options";
	public static final String SCOPE = "scope";
	public static final String TIMEOUT = "timeout";
	
//	PollResponse Fields
	public static final String RESPONSEPOLL_QUESTION = "question";
	public static final String RESPONSEPOLL_OPTIONS = "options";
	public static final String RESPONSEPOLL_POLLAGENT = "pollAgent";
	
//	PollRequestForward Fields
	public static final String POLLREQUESTFORWARD_POLLREQUEST = "pollRequest";
	
//	Search Fields
	public static final String SEARCH_KEY = "key";
	public static final String SEARCH_VALUE = "value";
	public static final String SEARCH_OPERATOR = "operator";
	
	public static final String AVATAR_LOCAL_NAME = "local_name";
}