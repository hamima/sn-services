/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ir.mod.tavana.toranj.consensus.onto;

import jade.content.Predicate;

/**
 *
 * @author You
 */
public class CDSSessionUpdateStatus implements Predicate{

    private String sessionID = "", newSessionStatus = "";

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
     * @return the newSessionStatus
     */
    public String getNewSessionStatus() {
        return newSessionStatus;
    }

    /**
     * @param newSessionStatus the newSessionStatus to set
     */
    public void setNewSessionStatus(String newSessionStatus) {
        this.newSessionStatus = newSessionStatus;
    }

}
