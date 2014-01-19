package ir.mod.tavana.toranj.services.broadcasting;

public interface BroadcastingServicesVocabulary {

	public static final String ONTOLOGY_NAME = "defa-broadcasting_ontology";
	
    public static final String BROADCASTING_AGENT = "BroadCastingAgent";
    
    public static final String BCRequest = "BCRequestMessage";
    public static final String BCRequest_APPICATION = "application";
    public static final String BCRequest_RECEIVERS = "receivers";
    public static final String BCRequest_BROADCAST = "broadcast";
    public static final String BCRequest_REAL_OBJ = "realObj";
    public static final String BCRequest_RECEIVER_QUERY = "receiverQuery";

    public static final String BCReply = "BCReplyMessage";
    public static final String BCReply_BC_REPLY_MSG = "bcReplyMsg";
    
    public static final String BCDNSRequest = "BCDNSRequest";
    public static final String BCDNSRequest_RECEIVER_QUERY = "receiverQuery";
    
    public static final String BCDNSReply = "BCDNSReply";
    public static final String BCDNSReply_DNS_RECEIVERS = "dnsReceivers"; 
        
    public static final String BCForwardRequest = "BCForwardRequest";
    public static final String BCForwardRequest_CONTENT = "content";
    public static final String BCForwardRequest_ACTUAL_SENDER = "actual_sender";
    
    public static final String BCForwardReply = "BCForwardReply"; 
    public static final String BCForwardReply_REPLY = "Reply"; 
    
	public static final String SEND_BROADCASTING = "SEND_BROADCASTING";
}
