/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ir.mod.tavana.toranj.consensus.onto;

import jade.content.Predicate;

/**
 *
 * @author Admin
 */
public class CDSSessionAlternativeProposal implements Predicate{

    private String name  =  "", description = "" , messageID = "" , inReplyTo = "";

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
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
     * @return the inReplyTo
     */
    public String getInReplyTo() {
        return inReplyTo;
    }

    /**
     * @param inReplyTo the inReplyTo to set
     */
    public void setInReplyTo(String inReplyTo) {
        this.inReplyTo = inReplyTo;
    }

}
