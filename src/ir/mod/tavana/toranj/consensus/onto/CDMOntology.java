/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.mod.tavana.toranj.consensus.onto;

import jade.content.onto.*;
/**
 *
 * @author Admin
 */
import jade.content.onto.*;
import jade.content.schema.*;
// Private constructor private BookTradingOntology() {
public class CDMOntology extends Ontology {
    //NAME
    public static final String ONTOLOGY_NAME = "CDMOntology";
    // The singleton instance of this ontology
    private static ReflectiveIntrospector introspect = new ReflectiveIntrospector();
    private static Ontology theInstance = new CDMOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
    // VOCABULARY
    public static final String DECISION_MAKER = "DecisionMakerAgent";
    public static final String CDSSetting = "CDSSetting";
    public static final String CDSSetting_TYPE = "type";
    public static final String CDSSetting_MIN_DECISION_MAKERS = "minDecisionMakers";
    public static final String CDSSetting_TIMER = "timeToStart";
    public static final String CDSRequest = "CDSRequest";
    public static final String CDSRequest_CODE = "code";
    public static final String CDSRequest_CONFIRMED = "confirmed";
    public static final String CDSRequest_BROKER = "broker";
    public static final String CDSRequest_TIMER = "timeToStart";
    public static final String CDSSessionInvitation = "CDSSessionInvitation";
    public static final String CDSSessionInvitation_NAME = "name";
    public static final String CDSSessionInvitation_SESSION_TITLE = "session_title";
    public static final String CDSSessionInvitation_SESSION_DATE = "session_date";
    public static final String CDSSessionInvitation_DM_WEIGHT = "decision_maker_weight";
    public static final String CDSSessionInvitation_ROLE = "role";
    public static final String CDSSessionUpdateStatus = "CDSSessionUpdateStatus";
    public static final String CDSSessionUpdateStatus_SESSION_ID = "sessionID";
    public static final String CDSSessionUpdateStatus_NEW_SESSION_STATUS = "newSessionStatus";
    public static final String CDSSessionStatus = "CDSSessionStatus";
    public static final String CDSSessionStatus_SESSION_ID = "sessionID";
    public static final String CDSSessionStatus_SESSION_STATUS = "sessionStatus";
    public static final String CDSSessionStatus_CRITERIA_SET = "criteriaSet";
    public static final String CDSSessionStatus_ALTERNATIVES_SET = "alternativesSet";
    public static final String CDSSessionFinalSettings = "CDSSessionFinalSettings";
    public static final String CDSSessionFinalSettings_CRITERIA_SET = "criteriaSet";
    public static final String CDSSessionFinalSettings_ALTERNATIVES_SET = "alternativesSet";
    public static final String CDSSessionPrivateMessage = "CDSSessionPrivateMessage";
    public static final String CDSSessionPrivateMessage_SESSION_ID = "sessionID";
    public static final String CDSSessionPrivateMessage_TAG = "tag";
    public static final String CDSSessionPrivateMessage_MESSAGE = "message";
    public static final String CDSSessionPublicMessage = "CDSSessionPublicMessage";
    public static final String CDSSessionPublicMessage_SESSION_ID = "sessionID";
    public static final String CDSSessionPublicMessage_TAG = "tag";
    public static final String CDSSessionPublicMessage_MESSAGE = "message";
    public static final String CDSSessionPublicMessage_SENDER = "sender";
    public static final String CDSSessionPublicMessage_MESSAGE_ID = "messageID";
    public static final String CDSSessionPublicMessage_IN_REPLY_TO = "inReplyTo";
    public static final String CDSSessionVote = "CDSSessionVote";
    public static final String CDSSessionVote_DECISION_MATRIX = "decisionMatrix";
    public static final String CDSSessionVote_CRITERIA_WEIGHTS_MATRIX = "criteriaWeightsMatrix";
    public static final String CDSSessionVote_DECISION_MAKER_WEIGHT = "decisionMakerWeight";
    public static final String CDSSessionDecisionResult = "CDMSessionDecisionResult";
    public static final String CDSSessionDecisionResult_WAS_SUCCESSFUL = "wasSuccessful";
    public static final String CDSSessionDecisionResult_TO_BE_CONTINUED = "toBeContinued";
    public static final String CDSSessionDecisionResult_BEST_ALTERNATIVES = "bestAlternatives";
    public static final String CDSSessionDecisionResult_SESSION_ID = "sessionID";
    public static final String CDSSessionCriteriaProposal = "CDSSessionCriteriaProposal";
    public static final String CDSSessionCriteriaProposal_NAME = "name";
    public static final String CDSSessionCriteriaProposal_DESCRIPTION = "description";
    public static final String CDSSessionCriteriaProposal_MESSAGE_ID = "messageID";
    public static final String CDSSessionCriteriaProposal_IN_REPLY_TO = "inReplyTo";
    public static final String CDSSessionAlternativeProposal = "CDSSessionAlternativeProposal";
    public static final String CDSSessionAlternativeProposal_NAME = "name";
    public static final String CDSSessionAlternativeProposal_DESCRIPTION = "description";
    public static final String CDSSessionAlternativeProposal_MESSAGE_ID = "messageID";
    public static final String CDSSessionAlternativeProposal_IN_REPLY_TO = "inReplyTo";

    /**
     * Constructor
     */
    private CDMOntology() {
        super(ONTOLOGY_NAME, BasicOntology.getInstance());
        try {

            // adding Concept(s)

            // adding AgentAction(s)

            // adding AID(s)
            ConceptSchema decisionMakerSchema = new ConceptSchema(DECISION_MAKER);
            add(decisionMakerSchema, DecisionMakerAgent.class);

            // adding Predicate(s)
            PredicateSchema CDSRequesSchema = new PredicateSchema(CDSRequest);
            add(CDSRequesSchema, CDSRequest.class);
            PredicateSchema CDSSettingSchema = new PredicateSchema(CDSSetting);
            add(CDSSettingSchema, CDSSetting.class);
            PredicateSchema CDSSessionInvitationSchema = new PredicateSchema(CDSSessionInvitation);
            add(CDSSessionInvitationSchema, CDSSessionInvitation.class);
            PredicateSchema CDSSessionUpdateStatusSchema = new PredicateSchema(CDSSessionUpdateStatus);
            add(CDSSessionUpdateStatusSchema, CDSSessionUpdateStatus.class);
            PredicateSchema CDSSessionStatusSchema = new PredicateSchema(CDSSessionStatus);
            add(CDSSessionStatusSchema, CDSSessionStatus.class);
            PredicateSchema CDSSessionFinalSettingsSchema = new PredicateSchema(CDSSessionFinalSettings);
            add(CDSSessionFinalSettingsSchema, CDSSessionFinalSettings.class);
            PredicateSchema CDSSessionPrivateMessageSchema = new PredicateSchema(CDSSessionPrivateMessage);
            add(CDSSessionPrivateMessageSchema, CDSSessionPrivateMessage.class);
            PredicateSchema CDSSessionPublicMessageSchema = new PredicateSchema(CDSSessionPublicMessage);
            add(CDSSessionPublicMessageSchema, CDSSessionPublicMessage.class);
            PredicateSchema CDSSessionVoteSchema = new PredicateSchema(CDSSessionVote);
            add(CDSSessionVoteSchema, CDSSessionVote.class);
            PredicateSchema CDSSessionDecisionResultSchema = new PredicateSchema(CDSSessionDecisionResult);
            add(CDSSessionDecisionResultSchema, CDSSessionDecisionResult.class);
            PredicateSchema CDSSessionCriteriaProposalSchema = new PredicateSchema(CDSSessionCriteriaProposal);
            add(CDSSessionCriteriaProposalSchema, CDSSessionCriteriaProposal.class);
            PredicateSchema CDSSessionAlternativeProposalSchema = new PredicateSchema(CDSSessionAlternativeProposal);
            add(CDSSessionAlternativeProposalSchema, CDSSessionAlternativeProposal.class);

            // adding fields
            CDSRequesSchema.add(CDSRequest_CODE, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSRequesSchema.add(CDSRequest_BROKER, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSRequesSchema.add(CDSRequest_CONFIRMED, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSRequesSchema.add(CDSRequest_TIMER, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSettingSchema.add(CDSSetting_TYPE, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSettingSchema.add(CDSSetting_MIN_DECISION_MAKERS, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSettingSchema.add(CDSSetting_TIMER, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSessionInvitationSchema.add(CDSSessionInvitation_NAME, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSessionInvitationSchema.add(CDSSessionInvitation_SESSION_DATE, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSessionInvitationSchema.add(CDSSessionInvitation_SESSION_TITLE, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSessionInvitationSchema.add(CDSSessionInvitation_DM_WEIGHT, (TermSchema) getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL);
            CDSSessionInvitationSchema.add(CDSSessionInvitation_ROLE, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSessionUpdateStatusSchema.add(CDSSessionUpdateStatus_SESSION_ID, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSessionUpdateStatusSchema.add(CDSSessionUpdateStatus_NEW_SESSION_STATUS, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSessionStatusSchema.add(CDSSessionStatus_SESSION_ID, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSessionStatusSchema.add(CDSSessionStatus_SESSION_STATUS, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSessionStatusSchema.add(CDSSessionStatus_CRITERIA_SET, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSessionStatusSchema.add(CDSSessionStatus_ALTERNATIVES_SET, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSessionFinalSettingsSchema.add(CDSSessionFinalSettings_ALTERNATIVES_SET, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSessionFinalSettingsSchema.add(CDSSessionFinalSettings_CRITERIA_SET, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSessionPrivateMessageSchema.add(CDSSessionPrivateMessage_MESSAGE, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSessionPrivateMessageSchema.add(CDSSessionPrivateMessage_TAG, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSessionPrivateMessageSchema.add(CDSSessionPrivateMessage_SESSION_ID, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSessionPublicMessageSchema.add(CDSSessionPublicMessage_MESSAGE, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSessionPublicMessageSchema.add(CDSSessionPublicMessage_TAG, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSessionPublicMessageSchema.add(CDSSessionPublicMessage_SESSION_ID, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSessionPublicMessageSchema.add(CDSSessionPublicMessage_SENDER, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSessionPublicMessageSchema.add(CDSSessionPublicMessage_MESSAGE_ID, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSessionPublicMessageSchema.add(CDSSessionPublicMessage_IN_REPLY_TO, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSessionVoteSchema.add(CDSSessionVote_CRITERIA_WEIGHTS_MATRIX, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSessionVoteSchema.add(CDSSessionVote_DECISION_MAKER_WEIGHT, (TermSchema) getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL);
            CDSSessionVoteSchema.add(CDSSessionVote_DECISION_MATRIX, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSessionDecisionResultSchema.add(CDSSessionDecisionResult_BEST_ALTERNATIVES, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSessionDecisionResultSchema.add(CDSSessionDecisionResult_TO_BE_CONTINUED, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSessionDecisionResultSchema.add(CDSSessionDecisionResult_WAS_SUCCESSFUL, (TermSchema) getSchema(BasicOntology.BOOLEAN), ObjectSchema.OPTIONAL);
            CDSSessionDecisionResultSchema.add(CDSSessionDecisionResult_SESSION_ID, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSessionCriteriaProposalSchema.add(CDSSessionCriteriaProposal_NAME, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSessionCriteriaProposalSchema.add(CDSSessionCriteriaProposal_DESCRIPTION, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSessionAlternativeProposalSchema.add(CDSSessionAlternativeProposal_NAME, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
            CDSSessionAlternativeProposalSchema.add(CDSSessionAlternativeProposal_DESCRIPTION, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);

            // adding name mappings

            // adding inheritance
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }
}
