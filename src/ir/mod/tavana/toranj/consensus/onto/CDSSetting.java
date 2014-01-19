/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ir.mod.tavana.toranj.consensus.onto;

import jade.content.Predicate;

/**
 *
 * @author Admin
 * type: indicates the type of decision making. is it a poll? is it voting?...
 * minDecisionMakers: indicates minimum number of the decision makers
 * timeToStart: indicates time to start since approval in milliseconds
 */
public class CDSSetting implements Predicate{
    private String type = "", minDecisionMakers = "",  timeToStart = "0";

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

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the min
     */
    public String getMinDecisionMakers() {
        return minDecisionMakers;
    }

    /**
     * @param min the min to set
     */
    public void setMinDecisionMakers(String min) {
        this.minDecisionMakers = min;
    }

}
