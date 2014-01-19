/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.mod.tavana.toranj.consensus.agents;

import ir.mod.tavana.toranj.consensus.lib.Config;
import ir.mod.tavana.toranj.consensus.lib.Matrix;
import ir.mod.tavana.toranj.consensus.onto.CDMOntology;
import ir.mod.tavana.toranj.consensus.onto.CDSRequest;
import ir.mod.tavana.toranj.consensus.onto.CDSSessionDecisionResult;
import ir.mod.tavana.toranj.consensus.onto.CDSSessionFinalSettings;
import ir.mod.tavana.toranj.consensus.onto.CDSSessionInvitation;
import ir.mod.tavana.toranj.consensus.onto.CDSSessionPrivateMessage;
import ir.mod.tavana.toranj.consensus.onto.CDSSessionPublicMessage;
import ir.mod.tavana.toranj.consensus.onto.CDSSessionStatus;
import ir.mod.tavana.toranj.consensus.onto.CDSSessionUpdateStatus;
import ir.mod.tavana.toranj.consensus.onto.CDSSessionVote;
import ir.mod.tavana.toranj.consensus.onto.CDSSetting;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.*;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


import sun.reflect.LangReflectAccess;

/**
 *
 * @author Admin
 */
public class DecisionMaker extends Agent {
    private String role;
    
    @Override
    protected void setup() {
        getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL0);
        getContentManager().registerOntology(CDMOntology.getInstance());

        System.out.println("Hello my name is " + this.getLocalName());
        /*try {
        Thread.sleep(20000);
        } catch (InterruptedException ex) {
        Logger.getLogger(Judge.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType(Config.ROLE_DECISION_MAKER);
        sd.setName(getLocalName());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        System.out.println("!!Hint: Please send an initiation message to a decision maker");
        System.out.println("!!Use language = "+FIPANames.ContentLanguage.FIPA_SL0+" & ontology = "+
                CDMOntology.getInstance().getName());
        this.addBehaviour(new DecisionMakerCDMSResponder(this));
    }

    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }
}

//class GetMyRole extends SimpleBehaviour {
//
//    @Override
//    public void action() {
//        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
//        ACLMessage inc = myAgent.blockingReceive(mt);
//        String role = inc.getContent();
//        if (role.equalsIgnoreCase(Config.PROCESS_INITIATOR)) {
//            myAgent.addBehaviour(new TarheMozooInitiator((DecisionMaker) myAgent, 25000));
//        }
//    }
//
//    @Override
//    public boolean done() {
//        return true;
//    }
//}

//class TarheMozooInitiator extends TickerBehaviour {
//
//    private boolean trigger;
//    // Trigger to avoid adding multiple cyclic behaviours to my agent
//
//    TarheMozooInitiator(DecisionMaker aThis, int period) {
//        super(aThis, period);
//        trigger = false;
//    }
//
//    @Override
//    protected void onTick() {
//        try {
//            DFAgentDescription df = new DFAgentDescription();
//            ServiceDescription sd = new ServiceDescription();
//            sd.setType(Config.ROLE_JUDGE);
//            df.addServices(sd);
//            DFAgentDescription[] result = DFService.search(myAgent, df);
//            if (result.length > 0) {
//                AID judgeAgent = result[0].getName();
//                ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
//                msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
//                msg.setOntology(CDMOntology.ONTOLOGY_NAME);
//                msg.addReceiver(judgeAgent);
//                CDSRequest cdsReq = new CDSRequest();
//                msg.setInReplyTo(myAgent.getName());
//                // setting the broker
//                cdsReq.setBroker(myAgent.getAID().getName());
//                myAgent.getContentManager().fillContent(msg, cdsReq);
//                myAgent.send(msg);
//                if (!trigger) {
//                    myAgent.addBehaviour(new DecisionMakerCDMSResponder((DecisionMaker) myAgent));
//                } else {
//                    trigger = true;
//                }
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//            e.printStackTrace();
//        }
//    }
//}

class DecisionMakerCDMSResponder extends CyclicBehaviour {

    public DecisionMakerCDMSResponder(DecisionMaker aThis) {
        super(aThis);
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchLanguage(FIPANames.ContentLanguage.FIPA_SL0),
                MessageTemplate.MatchOntology(CDMOntology.ONTOLOGY_NAME));
        ACLMessage inc = myAgent.blockingReceive(mt);
        System.out.println(inc.getSender().getLocalName() + " just said: ");
        System.out.println(inc.getContent());
        System.out.println();
        Object incContent = null;

        try {
            if(!inc.getContent().equalsIgnoreCase(Config.PROCESS_INITIATOR) &&
                    !inc.getContent().equalsIgnoreCase(Config.PROCESS_SEND_PUBLIC_MESSAGE) )
                incContent = myAgent.getContentManager().extractContent(inc);
            else
                incContent = inc.getContent();
        } catch (CodecException ex) {
            Logger.getLogger(DecisionMakerCDMSResponder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UngroundedException ex) {
            Logger.getLogger(DecisionMakerCDMSResponder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OntologyException ex) {
            Logger.getLogger(DecisionMakerCDMSResponder.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (incContent.equals(Config.PROCESS_INITIATOR)) {
            try {
                DFAgentDescription df = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType(Config.ROLE_JUDGE);
                df.addServices(sd);
                DFAgentDescription[] result = DFService.search(myAgent, df);
                if (result.length > 0) {
                    AID judgeAgent = result[0].getName();
                    ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                    msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
                    msg.setOntology(CDMOntology.ONTOLOGY_NAME);
                    msg.addReceiver(judgeAgent);
                    CDSRequest cdsReq = new CDSRequest();
                    String coversationID = UUID.randomUUID()+"";
                    msg.setConversationId(coversationID);
                    // setting the broker
                    cdsReq.setBroker(myAgent.getAID().getName());
                    myAgent.getContentManager().fillContent(msg, cdsReq);
                    myAgent.send(msg);
                }
            } catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
            }
        } else if (incContent.equals(Config.PROCESS_SEND_PUBLIC_MESSAGE)) {
                try {
                    DFAgentDescription df = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType(Config.ROLE_JUDGE);
                    df.addServices(sd);
                    DFAgentDescription[] result = DFService.search(myAgent, df);
                    if (result.length > 0) {
                        AID judgeAgent = result[0].getName();
                        // initiate a new public message
                        ACLMessage msg = new ACLMessage(ACLMessage.PROPAGATE);
                        CDSSessionPublicMessage outPublicMsg = new CDSSessionPublicMessage();
                        msg.setConversationId(UUID.randomUUID()+"");
                        msg.addReceiver(judgeAgent);
                        msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
                        msg.setOntology(CDMOntology.getInstance().getName());
                        // ** NOTICE: all of the following SET methods must be done in order to complete the message tree
                        outPublicMsg.setInReplyTo("");
                        outPublicMsg.setMessage("new message");
                        outPublicMsg.setSessionID("123");
                        outPublicMsg.setSender(myAgent.getName());
                        outPublicMsg.setTag(msg.getPerformative()+"");
                        outPublicMsg.setMessageID(UUID.randomUUID()+"");
                        myAgent.getContentManager().fillContent(msg, outPublicMsg);
                        myAgent.send(msg);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
        } else if (incContent instanceof CDSRequest && incContent != null) {
            try {
                if(ACLMessage.AGREE == inc.getPerformative()){
                    // Fix the initial settings
                    CDSSetting cdsSet = new CDSSetting();
                    cdsSet.setMinDecisionMakers(Config.MIN_DECISION_MAKERS);
                    cdsSet.setTimeToStart("10000");
                    cdsSet.setType(Config.CDM_NAME);
                    ACLMessage reply = inc.createReply();
                    myAgent.getContentManager().fillContent(reply, cdsSet);
                    reply.setInReplyTo(Config.CDS_SETTING);
                    reply.setPerformative(ACLMessage.REQUEST);
                    myAgent.send(reply);

                    ((DecisionMaker)myAgent).setRole(Config.ROLE_BROKER);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (incContent instanceof CDSSetting && incContent != null) {
            try {
                if (inc.getPerformative() == ACLMessage.AGREE) {
                    //find the related decision makers and select them
                    //here we select all of the decision makers
                    //Inviting the decision makers
                    DFAgentDescription df = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType(Config.ROLE_DECISION_MAKER);
                    df.addServices(sd);
                    DFAgentDescription[] result = DFService.search(myAgent, df);
                    for (int i = 0; i < result.length; i++) {
                        //should avoid sending an invitation to myself?
                        //if(result[i].getName().equals(myAgent.getName())) continue;
                        //prepare the invitation
                        CDSSessionInvitation invitation = new CDSSessionInvitation();
                        invitation.setAccepted(Config.STATUS_NOT_CONFIRMED);
                        invitation.setName(result[i].getName().getLocalName());
                        invitation.setSession_title("SESSION TITLE");

                        // Set the role
                        String role = Config.ROLE_DECISION_MAKER;
                        if(myAgent.getAID().getLocalName().equals(result[i].getName().getLocalName()))
                            role = Config.ROLE_BROKER;
                        else
                        {
                            if(Math.random()<0.5)
                                role = Config.ROLE_DECISION_MAKER;
                        }
                        invitation.setRole(role);

                        //send the invitation to judge to be forwarded
                        ACLMessage msg = inc.createReply();
                        msg.setPerformative(ACLMessage.REQUEST);
                        msg.setLanguage(inc.getLanguage());
                        msg.setOntology(CDMOntology.ONTOLOGY_NAME);
                        myAgent.getContentManager().fillContent(msg, invitation);
                        myAgent.send(msg);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (incContent instanceof CDSSessionInvitation && incContent != null) {
            try {
                // Answer the invitation
                CDSSessionInvitation invitation = (CDSSessionInvitation) myAgent.getContentManager().extractContent(inc);
                ((DecisionMaker)myAgent).setRole(invitation.getRole());
                invitation.setAccepted(Config.STATUS_CONFIRMED);
                ACLMessage reply = inc.createReply();
                reply.setPerformative(ACLMessage.AGREE);
                myAgent.getContentManager().fillContent(reply, invitation);
                myAgent.send(reply);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (incContent instanceof CDSSessionStatus && incContent != null) {
            try {
                CDSSessionStatus status = (CDSSessionStatus) myAgent.getContentManager().extractContent(inc);
                if (status.getSessionStatus().equalsIgnoreCase(Config.SESSION_NOT_STARTED)) {
                } else if (status.getSessionStatus().equalsIgnoreCase(Config.SESSION_READY_TO_START)) {
                    // Set the final setting and start the session
                    ACLMessage reply = inc.createReply();
                    CDSSessionFinalSettings finalSettings = new CDSSessionFinalSettings();
                    finalSettings.addAlternative("a1");
                    finalSettings.addAlternative("a2");
                    finalSettings.addAlternative("a3");
                    finalSettings.addCriteria("c1");
                    finalSettings.addCriteria("c2");
//                    System.out.println("**********************************************************");
//                    System.out.println("**********************************************************");
//                    System.out.println("ALTERS = "+finalSettings.getAlternativesSet());
//                    System.out.println("CRIT = "+finalSettings.getCriteriaSet());
//                    System.out.println("**********************************************************");
//                    System.out.println("**********************************************************");
                    myAgent.getContentManager().fillContent(reply, finalSettings);
                    reply.setPerformative(ACLMessage.REQUEST);
                    myAgent.send(reply);
                } else if (status.getSessionStatus().equalsIgnoreCase(Config.SESSION_STARTED)) {
                    // Mozakerat mian agent ha
                    double rand = Math.random();
                    if(rand > 0.1){
                        // change the status of session to START_VOTING
                        System.out.println("SALAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAN");
                        if(Config.ROLE_BROKER.equals(((DecisionMaker)myAgent).getRole())){
                            CDSSessionUpdateStatus update = new CDSSessionUpdateStatus();
                            update.setSessionID(status.getSessionID());
                            update.setNewSessionStatus(Config.SESSION_START_VOTE);

                            ACLMessage reply = inc.createReply();
                            myAgent.getContentManager().fillContent(reply, update);
                            myAgent.send(reply);
                        }
                    } else {
                        // send a public message
                        DFAgentDescription df = new DFAgentDescription();
                        ServiceDescription sd = new ServiceDescription();
                        sd.setType(Config.ROLE_JUDGE);
                        df.addServices(sd);
                        DFAgentDescription[] result = DFService.search(myAgent, df);
                        if (result.length > 0) {
                            AID judgeAgent = result[0].getName();
                            // initiate a new public message
                            ACLMessage msg = new ACLMessage(ACLMessage.PROPAGATE);
                            CDSSessionPublicMessage outPublicMsg = new CDSSessionPublicMessage();
                            msg.setConversationId(UUID.randomUUID()+"");
                            msg.addReceiver(judgeAgent);
                            msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
                            msg.setOntology(CDMOntology.getInstance().getName());
                            // ** NOTICE: all of the following SET methods must be done in order to complete the message tree
                            outPublicMsg.setInReplyTo("");
                            outPublicMsg.setMessage("new message");
                            outPublicMsg.setSessionID("123");
                            outPublicMsg.setSender(myAgent.getName());
                            outPublicMsg.setTag(msg.getPerformative()+"");
                            outPublicMsg.setMessageID(UUID.randomUUID()+"");
                            myAgent.getContentManager().fillContent(msg, outPublicMsg);
                            myAgent.send(msg);
                        }
                    }
                } else if (status.getSessionStatus().equalsIgnoreCase(Config.SESSION_START_VOTE)) {
                    //Prepare and send votes
                    String alternativesSet = status.getAlternativesSet();
                    alternativesSet = alternativesSet.trim();
                    String criteriaSet = status.getCriteriaSet();
                    criteriaSet = criteriaSet.trim();
                    String[] alternatives = alternativesSet.split(Config.ALTERNATIVE_SEPERATOR);
                    String[] criteria = criteriaSet.split(Config.CRITERIA_SEPERATOR);

                    //just for an example: generate a set of random matrixes
                    // d : number of decision makers
                    // M : number of alternatives
                    // N : number of criteria
                    int d = 1, M = alternatives.length
                            , N = criteria.length;
                    Matrix m , w;
                    m = new Matrix<Float>(M, N);
                    w = new Matrix<Float>(N, 1);
                    for (int j = 0; j < M; j++) {
                        for (int k = 0; k < N; k++) {
                            m.setElement((float) Math.round((float) (Math.random() * 10)), j, k);
                            w.setElement((float) Math.round((float) (Math.random() * 10)), k, 0);
                        }
                    }
                    String decisionMatrixString = m.toString() , weightsMatrixString = w.toString();
                    CDSSessionVote myVote = new CDSSessionVote();
                    myVote.setDecisionMakerWeight(1);
                    myVote.setDecisionMatrix(decisionMatrixString);
                    myVote.setCriteriaWeightsMatrix(weightsMatrixString);
                    ACLMessage reply = inc.createReply();
                    myAgent.getContentManager().fillContent(reply, myVote);
                    myAgent.send(reply);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (incContent instanceof CDSSessionPrivateMessage && incContent != null) {
            try {
                // display message
            }catch (Exception e) {
                e.printStackTrace();
            }
        } else if (incContent instanceof CDSSessionPublicMessage && incContent != null) {
            try {
                // display message and to prepare the reply do as follows
                //GIVING REPLY TO A MESSAGE
                CDSSessionPublicMessage incPublicMsg = (CDSSessionPublicMessage) incContent;
                ACLMessage reply = inc.createReply();
                reply.setPerformative(ACLMessage.PROPAGATE);
                CDSSessionPublicMessage outPublicMsg = new CDSSessionPublicMessage();
                // ** NOTICE: all of the following SET methods must be done in order to complete the message tree
                outPublicMsg.setInReplyTo(incPublicMsg.getMessageID());
                outPublicMsg.setMessage("reply!!");
                outPublicMsg.setSessionID(incPublicMsg.getSessionID());
                outPublicMsg.setSender(myAgent.getName());
                outPublicMsg.setTag(reply.getPerformative()+"");
                outPublicMsg.setMessageID(UUID.randomUUID()+"");
                myAgent.getContentManager().fillContent(reply, outPublicMsg);
                if(Math.random() < 0.6)
                    myAgent.send(reply);
            }catch (Exception e) {
                e.printStackTrace();
            }
        } else if (incContent instanceof CDSSessionDecisionResult && incContent != null) {
            try {
                
                if(Config.ROLE_BROKER.equals(((DecisionMaker)myAgent).getRole())){
                    CDSSessionDecisionResult result = (CDSSessionDecisionResult) incContent;
                    CDSSessionUpdateStatus update = new CDSSessionUpdateStatus();
                    update.setSessionID(result.getSessionID());
                    if(result.isWasSuccessful())
                        update.setNewSessionStatus(Config.SESSION_FINISHED_SUCCESSFULLY);
                    else
                        update.setNewSessionStatus(Config.SESSION_FINISHED_UNSUCCESSFULLY);
                    ACLMessage reply = inc.createReply();
                    reply.setPerformative(ACLMessage.REQUEST);
                    myAgent.getContentManager().fillContent(reply, update);
                    myAgent.send(reply);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
        }
        //this.block();
    }
}

