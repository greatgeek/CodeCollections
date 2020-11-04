# [剑指 Offer 54. 二叉搜索树的第k大节点](https://leetcode-cn.com/problems/er-cha-sou-suo-shu-de-di-kda-jie-dian-lcof/)

给定一棵二叉搜索树，请找出其中第k大的节点。

 

示例 1:
```
输入: root = [3,1,4,null,2], k = 1
   3
  / \
 1   4
  \
   2
输出: 4
```
示例 2:
```
输入: root = [5,3,6,2,4,null,null,1], k = 3
       5
      / \
     3   6
    / \
   2   4
  /
 1
输出: 4
```


限制：

1 ≤ k ≤ 二叉搜索树元素个数

## Show me the code

中序遍历（左，根，右）是递增序列，想要找出第 K 大节点，就必须降序排列，将中序遍历反过来即可（右，根，左）。再计数找出第 K 大即可。

```java
class Solution {
    public int kthLargest(TreeNode root, int k) {
        this.k=k;
        process(root);
        return res;
    }

    int k=0;
    int res=0;
    public void process(TreeNode root){
        if(root==null) return;
        process(root.right);
        if(k == 0) return;
        if(--k == 0) res = root.val;
        process(root.left);
    }
}
```

