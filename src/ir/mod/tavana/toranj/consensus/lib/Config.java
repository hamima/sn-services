/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ir.mod.tavana.toranj.consensus.lib;

/**
 *
 * @author Admin
 */
public abstract class Config {
    public static String
            MESSAGE_TREE_INDENT_SIGN = "-",
            MIN_DECISION_MAKERS = "4",
            MATRIX_ROWS_SEPERATOR = ";",
            MATRIX_ENTRIES_SEPERATOR = ",",
            CDS_REQUEST = "Tarhe-Mozoo" ,
            CDS_DM_SERVTYPE = "mooshak" ,
            CDM_NAME = "consensus-decision-making" ,
            CDS_SETTING = "Tanzimate-avalie" ,
            VIKOR_NAME = "vikor" , 
            STATUS_CONFIRMED = "Y" ,
            STATUS_NOT_CONFIRMED = "N",
            PROCESS_INITIATOR = "init" ,
            PROCESS_SEND_PUBLIC_MESSAGE = "public" ,
            ROLE_BROKER = "broker" ,
            ROLE_DECISION_MAKER = "decision-maker" ,
            ROLE_DECISION_MAKER_WITH_VETO_RIGHT = "decision-maker-with-veto-right" ,
            ROLE_JUDGE = "judge",
            CRITERIA_SEPERATOR=";" ,
            ALTERNATIVE_SEPERATOR=";" ,
            SESSION_NOT_STARTED="not-started-not-ready" ,
            SESSION_READY_TO_START="not-started-waiting-for-final-settings" ,
            SESSION_STARTED = "session-started",
            SESSION_START_VOTE = "session-start-vote",
            SESSION_FINISHED_UNSUCCESSFULLY = "session-finished-unsuccessfully",
            SESSION_FINISHED_SUCCESSFULLY = "session-finished-successfully";
    public static float VETO_PARAM = 0;
    
}
