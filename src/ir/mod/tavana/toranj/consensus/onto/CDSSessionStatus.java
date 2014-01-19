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
public class CDSSessionStatus implements Predicate {

    private String sessionID = "", sessionStatus = "" , criteriaSet = "" , alternativesSet = "";

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
     * @return the sessionStatus
     */
    public String getSessionStatus() {
        return sessionStatus;
    }

    /**
     * @param sessionStatus the sessionStatus to set
     */
    public void setSessionStatus(String sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    /**
     * @return the criteriaSet
     */
    public String getCriteriaSet() {
        return criteriaSet;
    }

    /**
     * @param criteriaSet the criteriaSet to set
     */
    public void setCriteriaSet(String criteriaSet) {
        this.criteriaSet = criteriaSet;
    }

    /**
     * @return the alternativesSet
     */
    public String getAlternativesSet() {
        return alternativesSet;
    }

    /**
     * @param alternativesSet the alternativesSet to set
     */
    public void setAlternativesSet(String alternativesSet) {
        this.alternativesSet = alternativesSet;
    }

}
