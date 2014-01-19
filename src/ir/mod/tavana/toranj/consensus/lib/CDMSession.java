/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ir.mod.tavana.toranj.consensus.lib;

import ir.mod.tavana.toranj.consensus.onto.CDSSessionStatus;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import java.util.LinkedList;
import java.util.UUID;


/**
 *
 * @author Admin
 */
public class CDMSession {
    private LinkedList<AID> sessionMemebers , invitedMembers , membersWithVetoRight;
    private LinkedList<String> alternativesSet , criteriaSet;
    private Matrix connectionGraph;
    private AID headMember;
    private int minimumNumberOfSessionMembers;
    private String id , status;
    private LinkedList<MessageTree> messagesPool;
    private LinkedList<Vote> votes;
    private float veto;

    public CDMSession(AID head){
        this.headMember = head;
        sessionMemebers = new LinkedList<AID>();
        invitedMembers = new LinkedList<AID>();
        alternativesSet = new LinkedList<String>();
        criteriaSet = new LinkedList<String>();
        messagesPool = new LinkedList<MessageTree>();
        votes = new LinkedList<Vote>();
        connectionGraph = new Matrix("");
        minimumNumberOfSessionMembers = 1;
        id = UUID.randomUUID()+"";
        status = Config.SESSION_NOT_STARTED;
        veto = 0;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    public void addMember(AID new_member){
        this.sessionMemebers.addLast(new_member);
    }

    public void addInvitedMember(AID new_member){
        this.invitedMembers.addLast(new_member);
    }

    public void removeMember(AID old_member){
        this.sessionMemebers.remove(old_member);
    }

    public void removeInvitedMember(AID old_member){
        this.invitedMembers.remove(old_member);
    }

    /**
     * @return the sessionMemebers
     */
    public LinkedList<AID> getSessionMemebers() {
        return sessionMemebers;
    }

    /**
     * @param sessionMemebers the sessionMemebers to set
     */
    public void setSessionMemebers(LinkedList<AID> sessionMemebers) {
        this.sessionMemebers = sessionMemebers;
    }

    /**
     * @return the invitedMembers
     */
    public LinkedList<AID> getInvitedMembers() {
        return invitedMembers;
    }

    /**
     * @param invitedMembers the invitedMembers to set
     */
    public void setInvitedMembers(LinkedList<AID> invitedMembers) {
        this.invitedMembers = invitedMembers;
    }

    /**
     * @return the connectionGraph
     */
    public Matrix getConnectionGraph() {
        return connectionGraph;
    }

    /**
     * @param connectionGraph the connectionGraph to set
     */
    public void setConnectionGraph(Matrix connectionGraph) {
        this.connectionGraph = connectionGraph;
    }

    /**
     * @return the headMember
     */
    public AID getHeadMember() {
        return headMember;
    }

    /**
     * @param headMember the headMember to set
     */
    public void setHeadMember(AID headMember) {
        this.headMember = headMember;
    }

    public String toString(){
        String str = "";
        str += ">> SESSION \n ID: "+id+
                "\n Number of invited : "+invitedMembers.size();
        str += "\n Number of Present : "+sessionMemebers.size()+"\n";
        str += " Head Member : "+headMember.getLocalName();
        return str;
    }

    /**
     * @return the minimumNumberOfSessionMembers
     */
    public int getMinimumNumberOfSessionMembers() {
        return minimumNumberOfSessionMembers;
    }

    /**
     * @param minimumNumberOfSessionMembers the minimumNumberOfSessionMembers to set
     */
    public void setMinimumNumberOfSessionMembers(int minimumNumberOfSessionMembers) {
        this.minimumNumberOfSessionMembers = minimumNumberOfSessionMembers;
    }

    /**
     * @return the alternativesSet
     */
    public LinkedList<String> getAlternativesSet() {
        return alternativesSet;
    }

    /**
     * @param alternativesSet the alternativesSet to set
     */
    public void setAlternativesSet(LinkedList<String> alternativesSet) {
        this.alternativesSet = alternativesSet;
    }

    /**
     * @return the criteriaSet
     */
    public LinkedList<String> getCriteriaSet() {
        return criteriaSet;
    }

    /**
     * @param criteriaSet the criteriaSet to set
     */
    public void setCriteriaSet(LinkedList<String> criteriaSet) {
        this.criteriaSet = criteriaSet;
    }

    public void importAlternatives (String str){
        str = str.trim();
        String[] alternatives = str.split(Config.ALTERNATIVE_SEPERATOR);
        for(int i = 0 ; i < alternatives.length ; i++)
        {
            if(alternatives[i].length() > 0)
            {
                System.out.println("ALTER["+i+"]="+alternatives[i]);
                this.alternativesSet.addLast(alternatives[i]);
            }
        }
    }

    public void importCritera (String str){
        str = str.trim();
        String[] criteria = str.split(Config.CRITERIA_SEPERATOR);
        for(int i = 0 ; i < criteria.length ; i++)
        {
            if(criteria[i].length() > 0)
                this.criteriaSet.addLast(criteria[i]);
        }
    }

    public LinkedList<AID> getAuthorisedPublicMessageReceivers(AID sender){

       // Dar hale hazer be hame miferestad. in kar badan mitavanad bar asase communicationGraph ijad shavad
        LinkedList<AID> authorisedReceivers = new LinkedList<AID>();
        for( int i = 0 ; i < sessionMemebers.size() ; i++ ){
            if(!sender.equals(sessionMemebers.get(i)))
                authorisedReceivers.addLast(sessionMemebers.get(i));
        }
        return authorisedReceivers;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getAlternativesSetInString(){
        String str = "";
        for(int i = 0 ; i < alternativesSet.size() ; i++)
            str += alternativesSet.get(i)+Config.ALTERNATIVE_SEPERATOR;
        return str;
    }

    public String getCriteriaSetInString(){
        String str = "";
        for(int i = 0 ; i < criteriaSet.size() ; i++)
            str += criteriaSet.get(i)+Config.CRITERIA_SEPERATOR;
        return str;
    }

    /**
     * @return the votes
     */
    public LinkedList<Vote> getVotes() {
        return votes;
    }

    /**
     * @param votes the votes to set
     */
    public void setVotes(LinkedList<Vote> votes) {
        this.votes = votes;
    }

    public void addVote(Vote vote){
        votes.addLast(vote);
    }

    public boolean haveEnoughVotesBeenGathered(){
        return minimumNumberOfSessionMembers <= votes.size();
    }

    public boolean haveAllVotesBeenGathered(){
        return sessionMemebers.size() <= votes.size();
    }

    /**
     * @return the veto
     */
    public float getVeto() {
        return veto;
    }

    /**
     * @param veto the veto to set
     */
    public void setVeto(float vetoIn) {
        if (veto < 0) this.veto = 0;
        else if(veto > 1) this.veto = 1;
        else this.veto = vetoIn;
    }

    /**
     * @return the publicMessagesPool
     */
    public LinkedList<MessageTree> getMessagesPool() {
        return messagesPool;
    }

    /**
     * @param publicMessagesPool the publicMessagesPool to set
     */
    public void setMessagesPool(LinkedList<MessageTree> publicMessagesPool) {
        this.messagesPool = publicMessagesPool;
    }

    
    public void addMessageToPool(TreeNode msg , String conversation_id , String inReplyTo){
        MessageTree tmp = null;
        for(int i = 0 ; i < this.messagesPool.size() ; i++){
            MessageTree cur = messagesPool.get(i);
            if(cur!= null && cur.getConversationID().equals(conversation_id) && conversation_id.length() > 0)
                tmp = cur;
        }
        if(tmp == null){
            System.out.println("TREE JADID");
            this.messagesPool.addLast(new MessageTree(msg, conversation_id));
        } else {
            System.out.println("OLD TREE");
            TreeNode tmpNode = null;
            tmpNode = tmp.searchByMessageID(inReplyTo);
            if(tmpNode == null)
                tmp.getRoot().getChildren().addLast(msg);
            else 
                tmpNode.getChildren().addLast(msg);
        }
    }

    public void printAllMessageTrees(String prefix){
        System.out.println(">> PRINTING ALL MESSAGE TREES: ");
        System.out.println("===========================================");
        for(int i = 0 ; i < this.messagesPool.size() ; i++)
            this.messagesPool.get(i).printMessageTree(prefix);
    }

    public CDSSessionStatus exportCDSSessionStatus(){
        CDSSessionStatus status  = new CDSSessionStatus();
        status.setSessionID(this.getId());
        status.setSessionStatus(this.getStatus());
        status.setAlternativesSet(this.getAlternativesSetInString());
        status.setCriteriaSet(this.getCriteriaSetInString());
        return status;
    }

    /**
     * @return the membersWithVetoRight
     */
    public LinkedList<AID> getMembersWithVetoRight() {
        return membersWithVetoRight;
    }

    /**
     * @param membersWithVetoRight the membersWithVetoRight to set
     */
    public void setMembersWithVetoRight(LinkedList<AID> membersWithVetoRight) {
        this.membersWithVetoRight = membersWithVetoRight;
    }

    public void addMemberWithVetoRight(AID newMember) {
        this.membersWithVetoRight.addLast(newMember);
    }

    public boolean isAMemberWithVetoRight(AID sessionMember) {
        int index = this.membersWithVetoRight.indexOf(sessionMember);
        return index > -1;
    }

    public void removeAlternative(String alternativeToRemove){
        for(int i = 0 ; i < this.alternativesSet.size() ; i++)
        {
            if(this.getAlternativesSet().get(i).equals(alternativeToRemove))
                this.alternativesSet.remove(i);
        }
    }

    public void removeCriteria(String criteriaToRemove){
        for(int i = 0 ; i < this.criteriaSet.size() ; i++)
        {
            if(this.getCriteriaSet().get(i).equals(criteriaToRemove))
                this.criteriaSet.remove(i);
        }
    }
}
