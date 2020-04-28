/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
class Solution {
    public int diameterOfBinaryTree(TreeNode root) {
        return process(root).max;
    }
    
    class Data{
        int max;
        int h;
        public Data(int max,int h){
            this.max=max;
            this.h=h;
        }
    }
    
    public Data process(TreeNode root){
        if(root==null){
            return new Data(0,0);
        }
        
        Data L = process(root.left);
        Data R = process(root.right);
        int h = Math.max(L.h,R.h)+1;
        int max = Math.max(L.h+R.h,Math.max(L.max,R.max));
        return new Data(max,h);
    }
}