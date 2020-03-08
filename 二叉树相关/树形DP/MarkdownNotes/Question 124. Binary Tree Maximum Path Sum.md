Question 124. Binary Tree Maximum Path Sum
```java
Given a non-empty binary tree, find the maximum path sum.

For this problem, a path is defined as any sequence of nodes from some starting node to any node in the tree along the parent-child connections. The path must contain at least one node and does not need to go through the root.

Example 1:

Input: [1,2,3]

       1
      / \
     2   3

Output: 6
Example 2:

Input: [-10,9,20,null,null,15,7]

   -10
   / \
  9  20
    /  \
   15   7

Output: 42
```

树形DP关键步骤：
1. 列可能性；
2. 整合信息（确定返回的结构体）；
3. 改递归；

本题的可能性：
1. 最大和来自左子树；
2. 最大和来自右子树；
3. 若左最大和延伸到左子树的头节点，且右最大和延伸到右子树的头节点，分别是当前节点的左右子树，则需要相加；

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
    public int maxPathSum(TreeNode root) {
        process(root);
        return maxSum;
    }
    
    class ReturnType{ //确定返回结构体
        public int max; // 最大值
        public TreeNode head; // 最大值延伸到的头部
        
        public ReturnType(int max, TreeNode head){
            this.max = max;
            this.head = head;
        }
    }
    
    public int maxSum=Integer.MIN_VALUE;
    
    public ReturnType process(TreeNode root){
        if(root==null){
            return new ReturnType(0,null);
        }
        
        ReturnType dataL = process(root.left); // 获取左子树的最大值与延伸到的头部
        ReturnType dataR = process(root.right); // 获取右子树的最大值与延伸到的头部
        if(root.left==dataL.head && root.right == dataR.head){
            maxSum = Math.max(maxSum, root.val + Math.max(0,dataL.max) + Math.max(0,dataR.max)); // 更新最大值
        }
        
        return new ReturnType(root.val+Math.max(0,Math.max(dataL.max,dataR.max)),root); // 若左右子树最大值均小于0，则只返回头节点的值与头节点
    }
}
```

后来发现并不用记录延伸到的头节点，因为每次情况下返回的都是延伸到最顶的头节点：
1. 左子树最大值小于0，右子树最大值大于0，则返回当前头节点与右子树最大值；
2. 右子树最大值小于0，左子树最大值大于0，则返回当前头节点与左子树最大值；
3. 左右子树最大值均小于0，则仅返回当前头节点；

如此分析来，每一次都会延伸到当前头节点。
所以返回结构体可以仅记录最大值即可，程序改写为：
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
    int maxSum = Integer.MIN_VALUE;
    public int maxPathSum(TreeNode root) {
        process(root);
        return maxSum;
    }
    
    public int process(TreeNode root){
        if(root==null){
            return 0;
        }
        
        int dataL = process(root.left);
        int dataR = process(root.right);
        
        maxSum=Math.max(maxSum,root.val+Math.max(0,dataL)+Math.max(0,dataR));
        
        return root.val+Math.max(0,Math.max(dataL,dataR)); // 返回时仅记录单侧数据
    }
    
}
```