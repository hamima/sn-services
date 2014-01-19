/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ir.mod.tavana.toranj.consensus.algo;

/**
 *
 * @author Admin
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Launcher.createInitialAgent("the_judge","consensus_decision_making_agents.Judge",null);
        Launcher.createOtherAgent("hamid","consensus_decision_making_agents.DecisionMaker",null);
        Launcher.createOtherAgent("saeid","consensus_decision_making_agents.DecisionMaker",null);
        Launcher.createOtherAgent("javad","consensus_decision_making_agents.DecisionMaker",null);
        Launcher.createOtherAgent("taghi","consensus_decision_making_agents.DecisionMaker",null);
    }

}

