1）若一个节点有右孩子，无左孩子，直接返回false；
2）在不违反1）的情况下，若当前节点左右孩子不全，则之后遇到的必须全为叶节点,开启叶节点模式；
3) 若开启叶节点模式后，发现当前节点不是叶节点，则返回false;

用boolean leaf = false; 来标记是否必须全为叶节点这个阶段的开启

```java
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
    public boolean isCompleteTree(TreeNode root) {
        if(root==null) return true;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        boolean leafMode=false;
        while(!queue.isEmpty()){
            TreeNode node = queue.poll();
            if(leafMode &&(node.left!=null || node.right!=null) ||// 若开启了 leafMode ，当前节点还有左或右孩子，则返回false
                node.right!=null && node.left==null){
                return false;
            }
            if(node.left!=null){
                queue.offer(node.left);
            }
            if(node.right!=null){
                queue.offer(node.right);
            }
            
            if(node.left==null || node.right==null){
                leafMode=true;
            }
        }
        return true;
    }
}
```