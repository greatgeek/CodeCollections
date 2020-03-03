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
