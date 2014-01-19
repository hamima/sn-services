package ir.mod.tavana.toranj.services.search;

public interface SearchVocabulary {
	
	public static final String ONTOLOGY_NAME = "defa-ontology-search";
	public static final String SEARCH_SERVICE_TYPE = "SearchS-Agent";

	public static final String WSIG_SERVICENAME = "Search";

	public static final String AVATAR_TYPE = "AvatarS-Agent";

//	WSIG Vocabulary
	public static final String WSIG_FLAG = "wsig";
	public static final String WSIG_MAPPER = "wsig-mapper";
	public static final String WSIG_PREFIX = "wsig-prefix";
	
//	Actions
	public static final String SEARCH_FORWARD_REQUEST = "SearchForwardRequest";
	
	public static final String SEARCH_FORWARD_REQUEST_KEY = "key";
	public static final String SEARCH_FORWARD_REQUEST_VALUE = "value";
	public static final String SEARCH_FORWARD_REQUEST_OPERATOR = "operator";

}
