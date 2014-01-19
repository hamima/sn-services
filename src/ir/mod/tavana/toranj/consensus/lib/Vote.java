/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ir.mod.tavana.toranj.consensus.lib;

/**
 *
 * @author You
 */
public class Vote {

    private float decisionMakerWeight;
    private Matrix<Float> decisionMatrix , criteriaWeightsMatrix;

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

    /**
     * @return the decisionMatrix
     */
    public Matrix<Float> getDecisionMatrix() {
        return decisionMatrix;
    }

    /**
     * @param decisionMatrix the decisionMatrix to set
     */
    public void setDecisionMatrix(Matrix<Float> decisionMatrix) {
        this.decisionMatrix = decisionMatrix;
    }

    /**
     * @return the criteriaWeightsMatrix
     */
    public Matrix<Float> getCriteriaWeightsMatrix() {
        return criteriaWeightsMatrix;
    }

    /**
     * @param criteriaWeightsMatrix the criteriaWeightsMatrix to set
     */
    public void setCriteriaWeightsMatrix(Matrix<Float> criteriaWeightsMatrix) {
        this.criteriaWeightsMatrix = criteriaWeightsMatrix;
    }



}
