/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ir.mod.tavana.toranj.consensus.onto;

import ir.mod.tavana.toranj.consensus.lib.Config;

import jade.content.Predicate;

/**
 *
 * @author Admin
 */
public class CDSSessionDecisionResult implements Predicate{

    private String bestAlternatives = "" , toBeContinued = Config.STATUS_NOT_CONFIRMED , sessionID = "";
    private boolean wasSuccessful = false;

    /**
     * @return the bestAlternatives
     */
    public String getBestAlternatives() {
        return bestAlternatives;
    }

    /**
     * @param bestAlternatives the bestAlternatives to set
     */
    public void setBestAlternatives(String bestAlternatives) {
        this.bestAlternatives = bestAlternatives;
    }

    /**
     * @return the wasSuccessful
     */
    public boolean isWasSuccessful() {
        return wasSuccessful;
    }

    public boolean getWasSuccessful() {
        return wasSuccessful;
    }

    /**
     * @param wasSuccessful the wasSuccessful to set
     */
    public void setWasSuccessful(boolean wasSuccessful) {
        this.wasSuccessful = wasSuccessful;
    }

    /**
     * @return the toBeContinued
     */
    public String getToBeContinued() {
        return toBeContinued;
    }

    /**
     * @param toBeContinued the toBeContinued to set
     */
    public void setToBeContinued(String toBeContinued) {
        this.toBeContinued = toBeContinued;
    }

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

}
