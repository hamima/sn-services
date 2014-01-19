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
public class CDSSessionVote implements Predicate{

    private String decisionMatrix = "", criteriaWeightsMatrix = "" ;
    private float decisionMakerWeight = 1;

    /**
     * @return the decisionMatrix
     */
    public String getDecisionMatrix() {
        return decisionMatrix;
    }

    /**
     * @param decisionMatrix the decisionMatrix to set
     */
    public void setDecisionMatrix(String decisionMatrix) {
        this.decisionMatrix = decisionMatrix;
    }

    /**
     * @return the criteriaWeightsMatrix
     */
    public String getCriteriaWeightsMatrix() {
        return criteriaWeightsMatrix;
    }

    /**
     * @param criteriaWeightsMatrix the criteriaWeightsMatrix to set
     */
    public void setCriteriaWeightsMatrix(String criteriaWeightsMatrix) {
        this.criteriaWeightsMatrix = criteriaWeightsMatrix;
    }

    /**
     * @return the decisionMakerWeight
     */
    public float getDecisionMakerWeight() {
        return decisionMakerWeight;
    }

    /**
     * @param decisionMakerWeight the decisionMakerWeight to set
     */
    public void setDecisionMakerWeight(float decisionMakerWeight) {
        this.decisionMakerWeight = decisionMakerWeight;
    }


}
