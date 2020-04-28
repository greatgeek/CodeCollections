```java
Given a binary tree, return the sum of values of its deepest leaves.
 

Example 1:



Input: root = [1,2,3,4,5,null,6,7,null,null,null,null,8]
Output: 15
 

Constraints:

The number of nodes in the tree is between 1 and 10^4.
The value of nodes is between 1 and 100.
```

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
    public int deepestLeavesSum(TreeNode root) {
        process(root,1);
        return sum.value;
    }
    
    class ReturnType{
        public int value; // 节点值
        public int height; // 节点高度
        
        public ReturnType(int value, int height){
            this.value = value;
            this.height = height;
        }
    }
    
    public ReturnType sum = new ReturnType(0,0);
    
    // 先序遍历法
    public void process(TreeNode root,int h){
        if(root == null){
            return;
        }
        
        if(root.left == null && root.right == null){
            if (h > sum.height) {
				sum.height = h;
				sum.value = root.val;
			} else if (h == sum.height) {
				sum.value += root.val;
			}
        }
        
        process(root.left, h + 1);
		process(root.right, h + 1);
        
    }
}
```