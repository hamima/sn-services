/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ir.mod.tavana.toranj.consensus.lib;

/**
 *
 * @author You
 */
public class MessageTree {

    private TreeNode root;
    private String conversationID;

    public MessageTree(){
        root = new TreeNode();
        conversationID = "";
    }

    public MessageTree(TreeNode rootIn , String conversation_id){
        this.root = rootIn;
        conversationID = conversation_id;
    }

    /**
     * @return the root
     */
    public TreeNode getRoot() {
        return root;
    }

    /**
     * @param root the root to set
     */
    public void setRoot(TreeNode root) {
        this.root = root;
    }

    /**
     * @return the conversationID
     */
    public String getConversationID() {
        return conversationID;
    }

    /**
     * @param conversationID the conversationID to set
     */
    public void setConversationID(String conversationID) {
        this.conversationID = conversationID;
    }   

    public TreeNode searchByMessageID(String id){
        return searchByMessageID(id , this.root);
    }

    private TreeNode searchByMessageID(String id , TreeNode cur){
        TreeNode tmp;
        if(id.equals(cur.getMessageID())){
            System.out.println("HASSAN YAFT SHOD");
            return cur;
        }
        else if (cur.getChildren().size()>0){
            for(int i = 0 ; i < cur.getChildren().size() ; i++){
                tmp = searchByMessageID(id , cur.getChildren().get(i));
                if(tmp != null)return tmp;
            }
        }
        return null;
    }

    public void printMessageTree(String prefix){
        System.out.println("> Printing Message Tree with conv ID: "+this.conversationID);
        System.out.println(printSubMessageTree(prefix , this.root));
    }

    private String printSubMessageTree(String prefix , TreeNode cur){
        String str = prefix+cur.toString()+"\n";
        if (cur.getChildren().size()>0){
            for(int i = 0 ; i < cur.getChildren().size() ; i++){
                str += printSubMessageTree(prefix+Config.MESSAGE_TREE_INDENT_SIGN , cur.getChildren().get(i));
            }
        }
        return str;
    }

    public int countATagInAMessageTree(String tagIn){
        return countATagInASubset(tagIn, this.root);
    }

    private int countATagInASubset(String tagIn , TreeNode cur){
        int count = 0;
        if(tagIn != null && tagIn.equals(cur.getTag()))
            count++;
        if (cur.getChildren().size()>0){
            for(int i = 0 ; i < cur.getChildren().size() ; i++){
                count += countATagInASubset(tagIn , cur.getChildren().get(i));
            }
        }
        return count;
    }

    public TreeNode findParentProposal(String messageID){
        TreeNode cur = this.searchByMessageID(messageID);
        TreeNode altProposal = null;
        int altLvl = 0;
        if(cur.isIsAlternativeProposal()) return cur;
        else {
            while(!cur.equals(this.root))
            {
                altLvl++;
                cur = this.searchByMessageID(cur.getParentID());
                if(cur.isIsAlternativeProposal())
                    altProposal = cur;
                    break;
            }
        }

        cur = this.searchByMessageID(messageID);
        TreeNode crtProposal = null;
        int crtLvl = 0;
        if(cur.isIsCriteriaProposal()) return cur;
        else {
            while(!cur.equals(this.root))
            {
                crtLvl++;
                cur = this.searchByMessageID(cur.getParentID());
                if(cur.isIsCriteriaProposal())
                    crtProposal = cur;
                break;
            }
        }

        if(crtLvl > altLvl)
            return altProposal;
        else
            return crtProposal;
    }
}