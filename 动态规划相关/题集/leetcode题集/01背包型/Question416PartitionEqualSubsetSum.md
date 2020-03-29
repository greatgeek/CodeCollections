**01背包型**

#### 题目描述

Given a **non-empty** array containing **only positive integers**, find if the array can be partitioned into two subsets such that the sum of elements in both subsets is equal.

**Note:**

1. Each of the array element will not exceed 100.
2. The array size will not exceed 200.

 

**Example 1:**

```
Input: [1, 5, 11, 5]

Output: true

Explanation: The array can be partitioned as [1, 5, 5] and [11].
```

 

**Example 2:**

```
Input: [1, 2, 3, 5]

Output: false

Explanation: The array cannot be partitioned into equal sum subsets.
```

 #### 分析

这道题问是否能将数组分成和是一样的两组。其中每个数字只能用一次，若存在能够拼出总计和一半（$\frac{\sum{A_i}}{2}$）

则能够成功分组。

#### 01背包动态规划

每样固定重量的物品仅能放入一次或不放入，问能容纳有限重量的背包最多能放入多少重量。

思路用于此题就是每个数字只能放入一次或不放入，问是否能累计出总计和的一半（$\frac{\sum{A_i}}{2}$）。

#### 代码

```java
class Solution {
    public boolean canPartition(int[] nums) {
        int sum=Sum(nums);
        if(sum%2==1) return false;
        
        int half = sum/2;     
        int n=nums.length;
        boolean[][] dp = new boolean[n][sum+1];
        dp[0][0]=true;
        dp[0][nums[0]]=true;
        for(int i=1;i<dp.length;i++){
            for(int j=0;j<dp[0].length;j++){ // 2. 继承上一行  （新物品不使用的信息）
                if(dp[i-1][j]){
                    dp[i][j]=true;
                }
            }
            for(int j=0;j<dp[0].length;j++){ // 3. 使用新物品
                if(dp[i-1][j]){
                    dp[i][j+nums[i]]=true;
                }
            }
        }
        return dp[n-1][sum/2]; // 判断是否存在总计和的一半
        
    }
    
    int Sum(int[] nums){
        int sum=0;
        for(int x:nums){
            sum +=x;
        }
        return sum;
    }
}
```

