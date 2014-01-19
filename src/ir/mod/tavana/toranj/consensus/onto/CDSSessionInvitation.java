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
public class CDSSessionInvitation implements Predicate {
    // name parameter is the name of agent to be invited
    private String accepted = Config.STATUS_NOT_CONFIRMED , session_title = "", 
            session_date = "" , name = "" , role = "";
    private float decision_maker_weight = 0;

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
     * @return the accepted
     */
    public String getAccepted() {
        return accepted;
    }

    /**
     * @param accepted the accepted to set
     */
    public void setAccepted(String accepted) {
        this.accepted = accepted;
    }

    /**
     * @return the session_title
     */
    public String getSession_title() {
        return session_title;
    }

    /**
     * @param session_title the session_title to set
     */
    public void setSession_title(String session_title) {
        this.session_title = session_title;
    }

    /**
     * @return the session_date
     */
    public String getSession_date() {
        return session_date;
    }

    /**
     * @param session_date the session_date to set
     */
    public void setSession_date(String session_date) {
        this.session_date = session_date;
    }

    /**
     * @return the decision_maker_weight
     */
    public float getDecision_maker_weight() {
        return decision_maker_weight;
    }

    /**
     * @param decision_maker_weight the decision_maker_weight to set
     */
    public void setDecision_maker_weight(float decision_maker_weight) {
        this.decision_maker_weight = decision_maker_weight;
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
