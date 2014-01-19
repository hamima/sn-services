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
public class CDSSessionFinalSettings implements Predicate{
    private String criteriaSet = "" , alternativesSet = "";

    public void addCriteria(String c){
        criteriaSet += c + Config.CRITERIA_SEPERATOR;
    }

    public void addAlternative(String a){
        setAlternativesSet(getAlternativesSet() + a + Config.ALTERNATIVE_SEPERATOR);
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
