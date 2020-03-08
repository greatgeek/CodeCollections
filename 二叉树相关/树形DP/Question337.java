/*
 * The thief has found himself a new place for his thievery again. There is only one entrance to this area, called the "root." Besides the root, each house has one and only one parent house. After a tour, the smart thief realized that "all houses in this place forms a binary tree". It will automatically contact the police if two directly-linked houses were broken into on the same night.

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
 * 
 * */

package leetcode;

public class Question337 {

	static class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;

		TreeNode(int x) {
			val = x;
		}
	}

	public static class ReturnType {
		public int robSum;
		public int noRobSum;

		public ReturnType(int robSum, int norobSum) {
			this.robSum = robSum;
			this.noRobSum = norobSum;
		}
	}
	
    public int rob(TreeNode root) {
        ReturnType data = process(root);
        return Math.max(data.robSum,data.noRobSum);
    }

	public static ReturnType process(TreeNode root) {
		if (root == null) {
			return new ReturnType(0,0);
		}

		int robSum = root.val; //  代表当前节点抢
		int norobSum = 0; // 代表当前节点不抢
		ReturnType dataL = new ReturnType(0, 0);
		ReturnType dataR = new ReturnType(0, 0);
		if (root.left != null) {
			dataL = process(root.left);
			robSum += dataL.noRobSum; // 当前节点抢了，下一层节点就不能抢了
			norobSum += Math.max(dataL.robSum, dataL.noRobSum);  // 当前节点抢了，下一层节点可选择抢或不抢，选择其中的最大值
		}

		if (root.right != null) {
			dataR = process(root.right);
			robSum += dataR.noRobSum;
			norobSum += Math.max(dataR.robSum, dataR.noRobSum);
		}

		return new ReturnType(robSum, norobSum);

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
