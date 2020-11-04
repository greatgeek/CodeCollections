### **105. Construct Binary Tree from Preorder and Inorder Traversal**

```java
Given preorder and inorder traversal of a tree, construct the binary tree.

Note:
You may assume that duplicates do not exist in the tree.

For example, given

preorder = [3,9,20,15,7]
inorder = [9,3,15,20,7]
Return the following binary tree:

    3
   / \
  9  20
    /  \
   15   7
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
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        return helper(preorder,0,preorder.length-1,inorder,0,inorder.length-1);
    }
    
    public TreeNode helper(int[] preorder, int pLeft,int pRight,int[] inorder,int iLeft,int iRight){
        TreeNode node=null;
        if(pLeft>pRight || iLeft>iRight) return null;
        int i=0;
        for(;i<inorder.length;i++){
            if(inorder[i]==preorder[pLeft]) break;
        }
        node = new TreeNode(inorder[i]);
        node.left=helper(preorder,pLeft+1,pLeft+i-iLeft,inorder,iLeft,i-1);
        node.right=helper(preorder,pLeft+i-iLeft+1,pRight,inorder,i+1,iRight);
        
        return node;
    }
}
```

### **106. Construct Binary Tree from Inorder and Postorder Traversal**

```java
Given inorder and postorder traversal of a tree, construct the binary tree.

Note:
You may assume that duplicates do not exist in the tree.

For example, given

inorder = [9,3,15,20,7]
postorder = [9,15,7,20,3]
Return the following binary tree:

    3
   / \
  9  20
    /  \
   15   7
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
    public TreeNode buildTree(int[] inorder, int[] postorder) {
        return helper(inorder,0,inorder.length-1,postorder,0,postorder.length-1);
    }
    
    public TreeNode helper(int[] inorder,int iLeft,int iRight,int[] postorder,int pLeft,int pRight){
        if(iLeft>iRight || pLeft>pRight) return null;
        int i=0;
        for(;i<inorder.length;i++){
            if(inorder[i]==postorder[pRight]) break;
        }
        TreeNode node=new TreeNode(inorder[i]);
        node.left=helper(inorder,iLeft,i-1,postorder,pLeft,pRight-iRight+i-1);
        node.right=helper(inorder,i+1,iRight,postorder,pLeft+i-iLeft,pRight-1);
        
        return node;
    }
}
```

### **889. Construct Binary Tree from Preorder and Postorder Traversal**
Return any binary tree that matches the given preorder and postorder traversals.

Values in the traversals pre and post are distinct positive integers.

 

Example 1:

Input: pre = [1,2,4,5,3,6,7], post = [4,5,2,6,7,3,1]
Output: [1,2,3,4,5,6,7]
 

Note:

* 1 <= pre.length == post.length <= 30
* pre[] and post[] are both permutations of 1, 2, ..., pre.length.
* It is guaranteed an answer exists. If there exists multiple answers, you can return any of them.

以前序和后序遍历来重建二叉树，结果并不唯一，给出合理解即可。

```java
class Solution {
    int[] pre, post;
    public TreeNode constructFromPrePost(int[] pre, int[] post) {
        this.pre = pre;
        this.post = post;
        return make(0, 0, pre.length);
    }

    public TreeNode make(int i0, int i1, int N) {
        if (N == 0) return null;
        TreeNode root = new TreeNode(pre[i0]);
        if (N == 1) return root;

        int L = 1;
        for (; L < N; ++L)
            if (post[i1 + L-1] == pre[i0 + 1])
                break;

        root.left = make(i0+1, i1, L);
        root.right = make(i0+L+1, i1+L, N-1-L);
        return root;
    }
}
```

### **1008. Construct Binary Search Tree from Preorder Traversal**

Return the root node of a binary search tree that matches the given preorder traversal.

(Recall that a binary search tree is a binary tree where for every node, any descendant of node.left has a value < node.val, and any descendant of node.right has a value > node.val.  Also recall that a preorder traversal displays the value of the node first, then traverses node.left, then traverses node.right.)

 

Example 1:

Input: [8,5,1,7,10,12]
Output: [8,5,10,1,7,null,12]

 

Note: 

1. 1 <= preorder.length <= 100
2. The values of preorder are distinct.

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
    public TreeNode bstFromPreorder(int[] preorder) {
        int[] inorder = Arrays.copyOf(preorder,preorder.length);
        Arrays.sort(inorder);
        return process(preorder,inorder,0,preorder.length-1,0,inorder.length-1);
    }
    
    public TreeNode process(int[] preorder,int[] inorder,int preL,int preR,int inL,int inR){
        if(preL>preR || inL>inR) return null;
        
        TreeNode root = new TreeNode(preorder[preL]);
        int len=0;
        while(inL+len<=inR && inorder[inL+len]!=preorder[preL]){
            len++;
        }
        root.left = process(preorder,inorder,preL+1,preL+len,inL,inL+len-1);
        root.right = process(preorder,inorder,preL+len+1,preR,inL+len+1,inR);
        return root;
    }
}
```