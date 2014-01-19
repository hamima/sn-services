/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ir.mod.tavana.toranj.consensus.lib;

import jade.core.AID;

/**
 *
 * This engine implements the management of decision making sessions between agents
 */
public class SessionManagementEngine {
    private CDMSession session;

    public SessionManagementEngine(){
    }

    /**
     * @return the session
     */
    public CDMSession getSession() {
        return this.session;
    }

    /**
     * @param session the session to set
     */
    public void setSession(CDMSession session) {
        this.session = session;
    }

    public void initiateSession(AID head){
        this.session = new CDMSession(head);
    }
}