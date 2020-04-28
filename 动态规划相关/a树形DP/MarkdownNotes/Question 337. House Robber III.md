```java
The thief has found himself a new place for his thievery again. There is only one entrance to this area, called the "root." Besides the root, each house has one and only one parent house. After a tour, the smart thief realized that "all houses in this place forms a binary tree". It will automatically contact the police if two directly-linked houses were broken into on the same night.

Determine the maximum amount of money the thief can rob tonight without alerting the police.

Example 1:

Input: [3,2,3,null,3,null,1]

     3
    / \
   2   3
    \   \ 
     3   1

Output: 7 
Explanation: Maximum amount of money the thief can rob = 3 + 3 + 1 = 7.
Example 2:

Input: [3,4,5,1,3,null,1]

     3
    / \
   4   5
  / \   \ 
 1   3   1

Output: 9
Explanation: Maximum amount of money the thief can rob = 4 + 5 = 9.
```

可能性：
1. 当前节点抢，则下一层子节点就不能抢；
2. 当前节点不抢，则下一层子节点可以抢或不抢；

确定返回结构体：
1. 当前节点抢情况下的和；
2. 当前节点不抢情况下的和；

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
    public int rob(TreeNode root) {
        ReturnType data = process(root);
        return Math.max(data.robSum,data.noRobSum);
    }
    
    class ReturnType{
        public int robSum; // 当前节点抢情况下的和记录在robSum中
        public int noRobSum; // 当前节点不抢情况下的和记录在noRobSum中
        public ReturnType(int robSum,int noRobSum){
            this.robSum = robSum;
            this.noRobSum = noRobSum;
        }
    }
    
    public ReturnType process(TreeNode root){
        if(root == null){
            return new ReturnType(0,0);
        }
        
        int robSum = root.val;
        int noRobSum = 0;
        ReturnType dataL,dataR;
        if(root.left!=null){
            dataL = process(root.left);
            robSum += dataL.noRobSum; // 若当前节点抢情况下，只能累计下一层节点不抢情况下的和
            noRobSum += Math.max(dataL.robSum,dataL.noRobSum); // 若当前节点不抢，则可以选择下一层节点抢情况下与不抢情况下的和，选择其中最大值
        }
        
        if(root.right!=null){ // 同理
            dataR=process(root.right);
            robSum += dataR.noRobSum;
            noRobSum += Math.max(dataR.robSum,dataR.noRobSum);
        }
        
        return new ReturnType(robSum,noRobSum); // 返回当前节点，抢情况下与不抢情况下的结构体
    }
}
```