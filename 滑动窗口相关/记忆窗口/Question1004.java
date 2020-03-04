/*
Given an array A of 0s and 1s, we may change up to K values from 0 to 1.

Return the length of the longest (contiguous) subarray that contains only 1s. 

Example 1:

Input: A = [1,1,1,0,0,0,1,1,1,1,0], K = 2
Output: 6
Explanation: 
[1,1,1,0,0,1,1,1,1,1,1]
Bolded numbers were flipped from 0 to 1.  The longest subarray is underlined.

Example 2:

Input: A = [0,0,1,1,0,0,1,1,1,0,1,1,0,0,0,1,1,1,1], K = 3
Output: 10
Explanation: 
[0,0,1,1,1,1,1,1,1,1,1,1,0,0,0,1,1,1,1]
Bolded numbers were flipped from 0 to 1.  The longest subarray is underlined.
*/

package leetcode;

public class Question1004 {

	public static int longestOnes(int[] A, int K) {
		int i=0,j=0;
		while(j<A.length) {
			if(A[j]==0) K--;
			if(K<0 && A[i++]==0) K++;
			j++;
		}
		return j-i;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
