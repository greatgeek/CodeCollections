/*
Given an array of integers nums and an integer k. A subarray is called nice if there are k odd numbers on it.

Return the number of nice sub-arrays.

Example 1:

Input: nums = [1,1,2,1,1], k = 3
Output: 2
Explanation: The only sub-arrays with 3 odd numbers are [1,1,2,1] and [1,2,1,1].

Example 2:

Input: nums = [2,4,6], k = 1
Output: 0
Explanation: There is no odd numbers in the array.

Example 3:

Input: nums = [2,2,2,1,2,2,1,2,2,2], k = 2
Output: 16
*/


package leetcode;

public class Question1248CountNumberOfNiceSubarrays {
	
	class Solution {
	    public int numberOfSubarrays(int[] nums, int k) {
	        Window w1 = new Window();
	        Window w2 = new Window();
	        int L=0,R=0;
	        int ans=0;
	        for(int x:nums){
	            w1.add(x);
	            w2.add(x);
	            
	            while(w1.getCount()>k){
	                w1.remove(nums[L++]);
	            }
	                
	            while(w2.getCount()>=k){
	                w2.remove(nums[R++]);
	            }
	            
	            ans += R-L;
	        }
	        return ans;
	    }
	    
	    class Window{
	        int oddCount;
	        
	        public Window(){
	            oddCount=0;
	        }
	        
	        public void add(int x){
	            if(x%2==1) oddCount++;
	        }
	        
	        public void remove(int x){
	            if(x%2==1) oddCount--;
	        }
	        
	        public int getCount(){
	            return oddCount;
	        }
	    }
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
