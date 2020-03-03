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
