/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.mod.tavana.toranj.consensus.agents;

import ir.mod.tavana.toranj.consensus.lib.CDMSession;
import ir.mod.tavana.toranj.consensus.lib.Config;
import ir.mod.tavana.toranj.consensus.lib.Matrix;
import ir.mod.tavana.toranj.consensus.lib.SessionManagementEngine;
import ir.mod.tavana.toranj.consensus.lib.TreeNode;
import ir.mod.tavana.toranj.consensus.lib.VIKOR;
import ir.mod.tavana.toranj.consensus.lib.Vote;
import ir.mod.tavana.toranj.consensus.onto.CDMOntology;
import ir.mod.tavana.toranj.consensus.onto.CDSRequest;
import ir.mod.tavana.toranj.consensus.onto.CDSSessionAlternativeProposal;
import ir.mod.tavana.toranj.consensus.onto.CDSSessionCriteriaProposal;
import ir.mod.tavana.toranj.consensus.onto.CDSSessionDecisionResult;
import ir.mod.tavana.toranj.consensus.onto.CDSSessionFinalSettings;
import ir.mod.tavana.toranj.consensus.onto.CDSSessionInvitation;
import ir.mod.tavana.toranj.consensus.onto.CDSSessionPublicMessage;
import ir.mod.tavana.toranj.consensus.onto.CDSSessionStatus;
import ir.mod.tavana.toranj.consensus.onto.CDSSessionUpdateStatus;
import ir.mod.tavana.toranj.consensus.onto.CDSSessionVote;
import ir.mod.tavana.toranj.consensus.onto.CDSSetting;
import jade.content.Predicate;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.*;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.rmi.server.UID;
import java.util.LinkedList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Admin
 */
public class Judge extends Agent {

    SessionManagementEngine sessionEngine;

    @Override
    protected void setup() {
        System.out.println("Hello my name is " + this.getLocalName());
        getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL0);
        getContentManager().registerOntology(CDMOntology.getInstance());
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType(Config.ROLE_JUDGE);
        sd.setName(Config.ROLE_JUDGE);
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        sessionEngine = new SessionManagementEngine();
//        this.addBehaviour(new TestVikorBehaviour(this));
        this.addBehaviour(new JudgeCDMSResponder(this));
    }

    @Override
    protected void takeDown() {
        super.takeDown();
    }
}

// Tests the implemented VIKOR algorithm
class TestVikorBehaviour extends Behaviour {

    public TestVikorBehaviour(Agent a) {
        super(a);
    }

    //this behaviour tests a sample decision making problem
    @Override
    public void action() {
        // d: number of decision makers
        // M: number of alternatives
        // N: number of criteria
        int d = 5, M = 10, N = 5;
        Matrix[] m = new Matrix[d], w = new Matrix[d];
        for (int i = 0; i < d; i++) {
            m[i] = new Matrix<Float>(M, N);
            w[i] = new Matrix<Float>(N, 1);
            for (int j = 0; j < M; j++) {
                for (int k = 0; k < N; k++) {
                    m[i].setElement((float) Math.round((float) (Math.random() * 10)), j, k);
                    w[i].setElement((float) Math.round((float) (Math.random() * 10)), k, 0);
                }
            }
            System.out.println("**M**");
            System.out.println(m[i].toString());
            System.out.println("**W**");
            System.out.println(w[i].toString());
        }
        VIKOR v = new VIKOR();
        v.setEcho(true);
        v.setDecisionMatrixes(m);
        v.setWeights(w);
        v.trace();
    }

    @Override
    public boolean done() {
        return true;
    }
}

class JudgeCDMSResponder extends CyclicBehaviour {

    public JudgeCDMSResponder(Judge aThis) {
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
            incContent = myAgent.getContentManager().extractContent(inc);
            System.out.println("AMOOOOOOOOOOOOOOOO RESID");
        } catch (CodecException ex) {
            Logger.getLogger(JudgeCDMSResponder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UngroundedException ex) {
            Logger.getLogger(JudgeCDMSResponder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OntologyException ex) {
            Logger.getLogger(JudgeCDMSResponder.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (incContent instanceof CDSRequest && incContent != null) {
            try {
                // process initial tarhe mozoo request
                String sessionCode = UUID.randomUUID() + "";
                ((Judge) myAgent).sessionEngine = new SessionManagementEngine();
                ((Judge) myAgent).sessionEngine.setSession(new CDMSession(inc.getSender()));
//                ((Judge)myAgent).sessionEngine.initiateSession(inc.getSender());
                ((Judge) myAgent).sessionEngine.getSession().setId(sessionCode);
                System.out.print("Session ID is " + ((Judge) myAgent).sessionEngine.getSession().getId());
                CDSRequest cdsReq = (CDSRequest) myAgent.getContentManager().extractContent(inc);
                cdsReq.setConfirmed(Config.STATUS_CONFIRMED);
                cdsReq.setCode(((Judge) myAgent).sessionEngine.getSession().getId());
                cdsReq.setTimeToStart("10000");
                ACLMessage reply = inc.createReply();
                reply.setPerformative(ACLMessage.AGREE);
                reply.setInReplyTo(Config.CDS_REQUEST);
                myAgent.getContentManager().fillContent(reply, cdsReq);
//                TreeNode incNode = new TreeNode(UUID.randomUUID()+"", "request", inc.getSender(), inc.getPerformative()+"");
//                ((Judge)myAgent).sessionEngine.getSession().addMessageToPool(incNode, inc.getConversationId(), "");
//                ((Judge)myAgent).sessionEngine.getSession().getMessagesPool().get(0).printMessageTree("***");
                myAgent.send(reply);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (incContent instanceof CDSSetting && incContent != null) {
            try {
                // process initial settings request
                CDSSetting cdsSet = (CDSSetting) myAgent.getContentManager().extractContent(inc);
                ((Judge) myAgent).sessionEngine.getSession().setMinimumNumberOfSessionMembers(Integer.parseInt(cdsSet.getMinDecisionMakers()));
                ACLMessage reply = inc.createReply();
                reply.setPerformative(ACLMessage.AGREE);
                reply.setInReplyTo(Config.CDS_REQUEST);
                myAgent.getContentManager().fillContent(reply, cdsSet);
                myAgent.send(reply);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (incContent instanceof CDSSessionInvitation && incContent != null) {
            try {
                if (inc.getPerformative() == ACLMessage.REQUEST) {
                    //process and send invitations
                    CDSSessionInvitation invitation = (CDSSessionInvitation) myAgent.getContentManager().extractContent(inc);
                    // invitations can be processed by a session or invitation engine here
                    ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                    msg.setLanguage(inc.getLanguage());
                    msg.setOntology(inc.getOntology());

                    //find the invited agent, prepare the invitation and send it
                    DFAgentDescription df = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType(Config.ROLE_DECISION_MAKER);
                    sd.setName(invitation.getName());
                    df.addServices(sd);
                    msg.setConversationId(inc.getConversationId());
                    DFAgentDescription[] result = DFService.search(myAgent, df);
                    for (int i = 0; i < result.length; i++) {
                        msg.addReceiver(result[i].getName());
                        myAgent.getContentManager().fillContent(msg, invitation);
                        myAgent.send(msg);
                        ((Judge) myAgent).sessionEngine.getSession().addInvitedMember(result[i].getName());
                    }
                } else if (inc.getPerformative() == ACLMessage.AGREE) {
                    CDSSessionInvitation invitation = (CDSSessionInvitation) incContent;
                    ((Judge) myAgent).sessionEngine.getSession().addMember(inc.getSender());
                    // you can change the start of project from here
                    if (((Judge) myAgent).sessionEngine.getSession().getSessionMemebers().size()
                            >= ((Judge) myAgent).sessionEngine.getSession().getMinimumNumberOfSessionMembers()) {
                        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                        msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
                        msg.setOntology(CDMOntology.getInstance().getName());
                        msg.setConversationId(inc.getConversationId());
                        CDSSessionStatus status = new CDSSessionStatus();
                        //msg.addReceiver(((Judge) myAgent).sessionEngine.getSession().getHeadMember());
                        msg.addReceiver(inc.getSender());

                        status.setSessionID(((Judge) myAgent).sessionEngine.getSession().getId());
                        status.setSessionStatus(Config.SESSION_READY_TO_START);
                        myAgent.getContentManager().fillContent(msg, status);
                        myAgent.send(msg);
                    }

                    //add the specified decision maker to session decision makers
                } else if (inc.getPerformative() == ACLMessage.REFUSE) {
                    //find another agent and invite them
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (incContent instanceof CDSSessionFinalSettings && incContent != null) {
            try {
                CDSSessionFinalSettings finalSettings = (CDSSessionFinalSettings) myAgent.getContentManager().extractContent(inc);
                ((Judge) myAgent).sessionEngine.getSession().importAlternatives(finalSettings.getAlternativesSet());
                ((Judge) myAgent).sessionEngine.getSession().importCritera(finalSettings.getCriteriaSet());
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setOntology(CDMOntology.getInstance().getName());
                msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
                msg.setPerformative(ACLMessage.INFORM);
                msg.setConversationId(inc.getConversationId());
                CDSSessionStatus status = new CDSSessionStatus();
                status.setSessionID(((Judge) myAgent).sessionEngine.getSession().getId());
                status.setSessionStatus(Config.SESSION_STARTED);
                status.setAlternativesSet(((Judge) myAgent).sessionEngine.getSession().getAlternativesSetInString());
                status.setCriteriaSet(((Judge) myAgent).sessionEngine.getSession().getCriteriaSetInString());
                myAgent.getContentManager().fillContent(msg, status);
                LinkedList<AID> sessionMembers = ((Judge) myAgent).sessionEngine.getSession().getSessionMemebers();
                for (int i = 0; i < sessionMembers.size(); i++) {
                    msg.addReceiver(sessionMembers.get(i));
                }
                myAgent.send(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (incContent instanceof CDSSessionCriteriaProposal && incContent != null) {
            try {
                if (inc.getPerformative() == ACLMessage.PROPOSE) {
                    CDSSessionCriteriaProposal criteria = (CDSSessionCriteriaProposal) myAgent.getContentManager().extractContent(inc);
                    if (criteria.getName().length() > 0) {
                        //build a tree node and store it in messages pool
                        TreeNode node = new TreeNode(criteria.getMessageID(), criteria.getName(), inc.getSender(), inc.getPerformative() + "", false, true, criteria.getInReplyTo());
                        ((Judge) myAgent).sessionEngine.getSession().addMessageToPool(node, inc.getConversationId(), criteria.getInReplyTo());

                        ACLMessage reply = inc.createReply();
                        reply.removeReceiver(inc.getSender());
                        reply.setPerformative(ACLMessage.PROPOSE);
                        myAgent.getContentManager().fillContent(reply, criteria);
                        LinkedList<AID> receivers =
                                ((Judge) myAgent).sessionEngine.getSession().getAuthorisedPublicMessageReceivers(inc.getSender());
                        for (int i = 0; i < receivers.size(); i++) {
                            reply.addReceiver(receivers.get(i));
                        }
                        myAgent.send(reply);
                    }
                } else if (inc.getPerformative() == ACLMessage.ACCEPT_PROPOSAL
                        && inc.getSender().equals(((Judge) myAgent).sessionEngine.getSession().getHeadMember())) {
                    CDSSessionCriteriaProposal criteria = (CDSSessionCriteriaProposal) myAgent.getContentManager().extractContent(inc);
                    if (criteria.getName().length() > 0) {
                        //build a tree node and store it in messages pool
//                        TreeNode node = new TreeNode(criteria.getMessageID(), criteria.getName(), inc.getSender(),inc.getPerformative()+"");
//                        ((Judge)myAgent).sessionEngine.getSession().addMessageToPool(node, inc.getConversationId(), criteria.getInReplyTo());

                        ACLMessage reply = inc.createReply();
                        reply.removeReceiver(inc.getSender());
                        reply.setPerformative(ACLMessage.PROPAGATE);
                        myAgent.getContentManager().fillContent(reply, criteria);
                        LinkedList<AID> receivers =
                                ((Judge) myAgent).sessionEngine.getSession().getAuthorisedPublicMessageReceivers(inc.getSender());
                        for (int i = 0; i < receivers.size(); i++) {
                            reply.addReceiver(receivers.get(i));
                        }
                        myAgent.send(reply);
                    }
                } else if (inc.getPerformative() == ACLMessage.REJECT_PROPOSAL
                        && inc.getSender().equals(((Judge) myAgent).sessionEngine.getSession().getHeadMember())
                        || ((Judge) myAgent).sessionEngine.getSession().isAMemberWithVetoRight(inc.getSender())) {
                    CDSSessionCriteriaProposal criteria = (CDSSessionCriteriaProposal) incContent;
                    ((Judge) myAgent).sessionEngine.getSession().removeCriteria(criteria.getName());

                    ACLMessage reply = inc.createReply();
                    reply.removeReceiver(inc.getSender());
                    reply.setPerformative(ACLMessage.PROPAGATE);
                    CDSSessionStatus status = new CDSSessionStatus();
                    status.setAlternativesSet(((Judge)myAgent).sessionEngine.getSession().getAlternativesSetInString());
                    status.setCriteriaSet(((Judge)myAgent).sessionEngine.getSession().getCriteriaSetInString());
                    status.setSessionID(((Judge)myAgent).sessionEngine.getSession().getId());
                    status.setSessionStatus(((Judge)myAgent).sessionEngine.getSession().getStatus());
                    myAgent.getContentManager().fillContent(reply, status);
                    LinkedList<AID> receivers =
                            ((Judge) myAgent).sessionEngine.getSession().getAuthorisedPublicMessageReceivers(inc.getSender());
                    for (int i = 0; i < receivers.size(); i++) {
                        reply.addReceiver(receivers.get(i));
                    }
                    myAgent.send(reply);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (incContent instanceof CDSSessionAlternativeProposal && incContent != null) {
            try {
                if (inc.getPerformative() == ACLMessage.PROPOSE) {
                    CDSSessionAlternativeProposal alternative = (CDSSessionAlternativeProposal) myAgent.getContentManager().extractContent(inc);
                    if (alternative.getName().length() > 0) {
                        //build a tree node and store it in messages pool
                        TreeNode node = new TreeNode(alternative.getMessageID(), alternative.getName(), inc.getSender(), inc.getPerformative() + "",
                                true, false, alternative.getInReplyTo());
                        ((Judge) myAgent).sessionEngine.getSession().addMessageToPool(node, inc.getConversationId(), alternative.getInReplyTo());

                        ACLMessage reply = inc.createReply();
                        reply.removeReceiver(inc.getSender());
                        reply.setPerformative(ACLMessage.PROPOSE);
                        myAgent.getContentManager().fillContent(reply, alternative);
                        LinkedList<AID> receivers =
                                ((Judge) myAgent).sessionEngine.getSession().getAuthorisedPublicMessageReceivers(inc.getSender());
                        for (int i = 0; i < receivers.size(); i++) {
                            reply.addReceiver(receivers.get(i));
                        }
                        myAgent.send(reply);
                    }
                } else if (inc.getPerformative() == ACLMessage.ACCEPT_PROPOSAL
                        && inc.getSender().equals(((Judge) myAgent).sessionEngine.getSession().getHeadMember())) {
                    CDSSessionAlternativeProposal alternative = (CDSSessionAlternativeProposal) myAgent.getContentManager().extractContent(inc);
                    if (alternative.getName().length() > 0) {
                        //build a tree node and store it in messages pool
//                        TreeNode node = new TreeNode(alternative.getMessageID(), alternative.getName(), inc.getSender(),inc.getPerformative()+"" , true , false);
//                        ((Judge)myAgent).sessionEngine.getSession().addMessageToPool(node, inc.getConversationId(), alternative.getInReplyTo());

                        ACLMessage reply = inc.createReply();
                        reply.removeReceiver(inc.getSender());
                        reply.setPerformative(ACLMessage.PROPAGATE);
                        myAgent.getContentManager().fillContent(reply, alternative);
                        LinkedList<AID> receivers =
                                ((Judge) myAgent).sessionEngine.getSession().getAuthorisedPublicMessageReceivers(inc.getSender());
                        for (int i = 0; i < receivers.size(); i++) {
                            reply.addReceiver(receivers.get(i));
                        }
                        myAgent.send(reply);
                    }
                } else if (inc.getPerformative() == ACLMessage.REJECT_PROPOSAL
                        && inc.getSender().equals(((Judge) myAgent).sessionEngine.getSession().getHeadMember())
                        || ((Judge) myAgent).sessionEngine.getSession().isAMemberWithVetoRight(inc.getSender())) {
                    CDSSessionAlternativeProposal alternative = (CDSSessionAlternativeProposal) incContent;
                    ((Judge) myAgent).sessionEngine.getSession().removeAlternative(alternative.getName());

                    ACLMessage reply = inc.createReply();
                    reply.removeReceiver(inc.getSender());
                    reply.setPerformative(ACLMessage.PROPAGATE);
                    CDSSessionStatus status = new CDSSessionStatus();
                    status.setAlternativesSet(((Judge)myAgent).sessionEngine.getSession().getAlternativesSetInString());
                    status.setCriteriaSet(((Judge)myAgent).sessionEngine.getSession().getCriteriaSetInString());
                    status.setSessionID(((Judge)myAgent).sessionEngine.getSession().getId());
                    status.setSessionStatus(((Judge)myAgent).sessionEngine.getSession().getStatus());
                    myAgent.getContentManager().fillContent(reply, status);
                    LinkedList<AID> receivers =
                            ((Judge) myAgent).sessionEngine.getSession().getAuthorisedPublicMessageReceivers(inc.getSender());
                    for (int i = 0; i < receivers.size(); i++) {
                        reply.addReceiver(receivers.get(i));
                    }
                    myAgent.send(reply);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (incContent instanceof CDSSessionPublicMessage && incContent != null) {
            try {
                CDSSessionPublicMessage publicMessage = (CDSSessionPublicMessage) myAgent.getContentManager().extractContent(inc);
                //build a tree node and store it in messages pool
                TreeNode node = new TreeNode(publicMessage.getMessageID(), publicMessage.getMessage(), inc.getSender(), inc.getPerformative() + "", false, false, publicMessage.getInReplyTo());
                ((Judge) myAgent).sessionEngine.getSession().addMessageToPool(node, inc.getConversationId(), publicMessage.getInReplyTo());
                ((Judge) myAgent).sessionEngine.getSession().printAllMessageTrees("****");
                publicMessage.setSender(inc.getSender().getName());
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
                msg.setOntology(CDMOntology.getInstance().getName());
                msg.setConversationId(inc.getConversationId());
                myAgent.getContentManager().fillContent(msg, publicMessage);
                LinkedList<AID> receivers =
                        ((Judge) myAgent).sessionEngine.getSession().getAuthorisedPublicMessageReceivers(inc.getSender());
                for (int i = 0; i < receivers.size(); i++) {
                    msg.addReceiver(receivers.get(i));
                }
                myAgent.send(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (incContent instanceof CDSSessionStatus && incContent != null) {
            try {
                if (inc.getPerformative() == ACLMessage.REQUEST) {
                    CDSSessionStatus status = (CDSSessionStatus) myAgent.getContentManager().extractContent(inc);
                    if (inc.getSender().equals(((Judge) myAgent).sessionEngine.getSession().getHeadMember())) {
                        ((Judge) myAgent).sessionEngine.getSession().setStatus(status.getSessionStatus());
                        status.setAlternativesSet(((Judge) myAgent).sessionEngine.getSession().getAlternativesSetInString());
                        status.setCriteriaSet(((Judge) myAgent).sessionEngine.getSession().getCriteriaSetInString());
                        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                        msg.setOntology(CDMOntology.getInstance().getName());
                        msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
                        msg.setPerformative(ACLMessage.INFORM);
                        msg.setConversationId(inc.getConversationId());
                        myAgent.getContentManager().fillContent(msg, status);
                        LinkedList<AID> sessionMembers = ((Judge) myAgent).sessionEngine.getSession().getSessionMemebers();
                        for (int i = 0; i < sessionMembers.size(); i++) {
                            msg.addReceiver(sessionMembers.get(i));
                        }
                        myAgent.send(msg);
                    } else {
                        ACLMessage msg = inc.createReply();
                        msg.setContent(inc.getContent());
                        msg.setPerformative(ACLMessage.REFUSE);
                        myAgent.send(msg);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (incContent instanceof CDSSessionVote && incContent != null) {
            try {
                // Handle all comming votes
                CDSSessionVote vote = (CDSSessionVote) myAgent.getContentManager().extractContent(inc);
                Vote v = new Vote();
                v.setCriteriaWeightsMatrix(new Matrix<Float>(vote.getCriteriaWeightsMatrix()));
                v.setDecisionMatrix(new Matrix<Float>(vote.getDecisionMatrix()));
                v.setDecisionMakerWeight(vote.getDecisionMakerWeight());
                ((Judge) myAgent).sessionEngine.getSession().addVote(v);
                if (((Judge) myAgent).sessionEngine.getSession().haveAllVotesBeenGathered()
                        || ((Judge) myAgent).sessionEngine.getSession().haveEnoughVotesBeenGathered()) {
                    Matrix[] w = new Matrix[((Judge) myAgent).sessionEngine.getSession().getVotes().size()];
                    Matrix[] m = new Matrix[w.length];
                    for (int i = 0; i < w.length; i++) {
                        w[i] = ((Judge) myAgent).sessionEngine.getSession().getVotes().get(i).getCriteriaWeightsMatrix();
                        m[i] = ((Judge) myAgent).sessionEngine.getSession().getVotes().get(i).getDecisionMatrix();
                    }
                    ((Judge) myAgent).sessionEngine.getSession().setVeto(Config.VETO_PARAM);
                    VIKOR vikor = new VIKOR();
                    vikor.setEcho(true);
                    vikor.setDecisionMatrixes(m);
                    vikor.setWeights(w);
                    vikor.setVeto(((Judge) myAgent).sessionEngine.getSession().getVeto());
                    LinkedList<Integer> selectedAlternatives = vikor.trace();
                    String selectedAlternativesString = "";
                    for (int i = 0; i < selectedAlternatives.size(); i++) {
                        selectedAlternativesString += selectedAlternatives.get(i) + Config.ALTERNATIVE_SEPERATOR;
                    }
                    boolean successful = false;
                    if (selectedAlternatives.size() == 1) {
                        successful = true;
                    }

                    //prepare the result
                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                    CDSSessionDecisionResult result = new CDSSessionDecisionResult();
                    result.setBestAlternatives(selectedAlternativesString);
                    result.setToBeContinued(Config.STATUS_NOT_CONFIRMED);
                    result.setSessionID(((Judge) myAgent).sessionEngine.getSession().getId());
                    result.setWasSuccessful(successful);

                    for (int i = 0; i < ((Judge) myAgent).sessionEngine.getSession().getSessionMemebers().size(); i++) {
                        msg.addReceiver(((Judge) myAgent).sessionEngine.getSession().getSessionMemebers().get(i));
                    }
                    msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
                    msg.setOntology(CDMOntology.getInstance().getName());
                    myAgent.getContentManager().fillContent(msg, result);
                    myAgent.send(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (incContent instanceof CDSSessionDecisionResult && incContent != null) {
            try {
                CDSSessionDecisionResult result = (CDSSessionDecisionResult) myAgent.getContentManager().extractContent(inc);
                if (result.getToBeContinued().equalsIgnoreCase(Config.STATUS_NOT_CONFIRMED)) {
                    if (result.isWasSuccessful()) {
                        ((Judge) myAgent).sessionEngine.getSession().setStatus(Config.SESSION_FINISHED_SUCCESSFULLY);
                    } else {
                        ((Judge) myAgent).sessionEngine.getSession().setStatus(Config.SESSION_FINISHED_UNSUCCESSFULLY);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (incContent instanceof CDSSessionUpdateStatus && incContent != null) {
            try {
                CDSSessionUpdateStatus update = (CDSSessionUpdateStatus) incContent;
                if (update.getSessionID().equals(((Judge) myAgent).sessionEngine.getSession().getId())) {
                    ((Judge) myAgent).sessionEngine.getSession().setStatus(update.getNewSessionStatus());
                }
                CDSSessionStatus status = ((Judge) myAgent).sessionEngine.getSession().exportCDSSessionStatus();                		
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
                msg.setOntology(CDMOntology.getInstance().getName());
                myAgent.getContentManager().fillContent(msg, status);
                LinkedList<AID> receivers = ((Judge) myAgent).sessionEngine.getSession().getAuthorisedPublicMessageReceivers(myAgent.getAID());
                for (int i = 0; i < receivers.size(); i++) {
                    msg.addReceiver(receivers.get(i));
                }
                myAgent.send(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
        }
    }
}
