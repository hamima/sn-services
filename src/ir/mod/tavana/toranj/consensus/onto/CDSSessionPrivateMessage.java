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
public class CDSSessionPrivateMessage implements Predicate{

    private String sessionID = "" , tag = "" , message = "";

    /**
     * @return the sessionID
     */
    public String getSessionID() {
        return sessionID;
    }

    /**
     * @param sessionID the sessionID to set
     */
    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
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

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
