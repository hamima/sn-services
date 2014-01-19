/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ir.mod.tavana.toranj.consensus.lib;

import jade.core.AID;
import java.util.LinkedList;

/**
 *
 * @author You
 */
public class TreeNode {

    private String messageID , //the id of the message
            parentID , // field inReplyTo
            content , // content of the message, if its a proposal then the title of the proposal
            tag;    // shows the Performative of the message
    private AID sender;
    private LinkedList<TreeNode> children;
    private boolean isAlternativeProposal, isCriteriaProposal;

    public TreeNode(){
        messageID = "";
        content = "";
        tag = "";
        sender = new AID();
        children = new LinkedList<TreeNode>();
        this.isAlternativeProposal = false;
        this.isCriteriaProposal = false;
        this.parentID = "";
    }

    public TreeNode(String message_id , String messageContent , AID senderAgent ,
            String tagIn , boolean alternativeProposal , boolean criteriaProposal , String inReplyTo){
        this.messageID = message_id;
        this.content = messageContent;
        this.sender = senderAgent;
        this.tag = tagIn;
        this.children = new LinkedList<TreeNode>();
        this.isAlternativeProposal = alternativeProposal;
        this.isCriteriaProposal = criteriaProposal;
        this.parentID = inReplyTo;
    }

    /**
     * @return the messageID
     */
    public String getMessageID() {
        return messageID;
    }

    /**
     * @param messageID the messageID to set
     */
    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the sender
     */
    public AID getSender() {
        return sender;
    }

    /**
     * @param sender the sender to set
     */
    public void setSender(AID sender) {
        this.sender = sender;
    }

    /**
     * @return the children
     */
    public LinkedList<TreeNode> getChildren() {
        return children;
    }

    /**
     * @param children the children to set
     */
    public void setChildren(LinkedList<TreeNode> children) {
        this.children = children;
    }

    public void addChild(TreeNode child){
        this.children.addLast(child);
    }

    /**
     * @return the tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * @param tag the tag to set
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    public String toString(){
        String str = "* Message: ";
        str += "sender: "+this.getSender().getLocalName()+" Tagged: "+this.getTag()+
                " Content: "+ this.getContent()+" Message ID: "+this.messageID+" In reply to: ";
        return str;
    }

    /**
     * @return the parentID
     */
    public String getParentID() {
        return parentID;
    }

    /**
     * @param parentID the parentID to set
     */
    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    /**
     * @return the isAlternativeProposal
     */
    public boolean isIsAlternativeProposal() {
        return isAlternativeProposal;
    }

    /**
     * @param isAlternativeProposal the isAlternativeProposal to set
     */
    public void setIsAlternativeProposal(boolean isAlternativeProposal) {
        this.isAlternativeProposal = isAlternativeProposal;
    }

    /**
     * @return the isCriteriaProposal
     */
    public boolean isIsCriteriaProposal() {
        return isCriteriaProposal;
    }

    /**
     * @param isCriteriaProposal the isCriteriaProposal to set
     */
    public void setIsCriteriaProposal(boolean isCriteriaProposal) {
        this.isCriteriaProposal = isCriteriaProposal;
    }
}
