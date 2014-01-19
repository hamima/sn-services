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
public class CDSRequest implements Predicate{
    private String code = "", broker = "", confirmed = Config.STATUS_NOT_CONFIRMED , timeToStart = "0";

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the requesterAID
     */
    public String getBroker() {
        return broker;
    }

    /**
     * @param requesterAID the requesterAID to set
     */
    public void setBroker(String requesterAID) {
        this.broker = requesterAID;
    }

    /**
     * @return the confirmed
     */
    public String getConfirmed() {
        return confirmed;
    }

    /**
     * @param confirmed the confirmed to set
     */
    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }

    /**
     * @return the timeToStart
     */
    public String getTimeToStart() {
        return timeToStart;
    }

    /**
     * @param timeToStart the timeToStart to set
     */
    public void setTimeToStart(String timeToStart) {
        this.timeToStart = timeToStart;
    }

}
