package leetcode;

import java.util.LinkedList;

public class AllLessNumSubArray {
	
	public static int getNum(int[] arr,int num) {
		if(arr == null || arr.length == 0) {
			return 0;
		}
		LinkedList<Integer> qmin = new LinkedList<>();
		LinkedList<Integer> qmax = new LinkedList<>();
		int L=0;
		int R=0;
		int res = 0;
		while(L<arr.length) {
			while(R<arr.length) {
				while(!qmin.isEmpty() && arr[qmin.peekLast()] >= arr[R]) {
					qmin.pollLast();
				}
				qmin.addLast(R);
				while(!qmax.isEmpty() && arr[qmax.peekLast()] <= arr[R]) {
					qmax.pollLast();
				}
				qmax.addLast(R);
				if(arr[qmax.getFirst()]-arr[qmin.getFirst()] > num) {
					break;
				}
				R++;
			}
			if(qmin.peekFirst() == L) { //判断下标是否过期
				qmin.pollFirst();
			}
			if(qmax.peekFirst() == L) {
				qmax.pollFirst();
			}
			res += R-L;
			L++;
		}
		return res;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
