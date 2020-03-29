#### 题目一：198. House Robber

You are a professional robber planning to rob houses along a street. Each house has a certain amount of money stashed, the only constraint stopping you from robbing each of them is that adjacent houses have security system connected and **it will automatically contact the police if two adjacent houses were broken into on the same night**.

Given a list of non-negative integers representing the amount of money of each house, determine the maximum amount of money you can rob tonight **without alerting the police**.

**Example 1:**

```
Input: [1,2,3,1]
Output: 4
Explanation: Rob house 1 (money = 1) and then rob house 3 (money = 3).
             Total amount you can rob = 1 + 3 = 4.
```

**Example 2:**

```
Input: [2,7,9,3,1]
Output: 12
Explanation: Rob house 1 (money = 2), rob house 3 (money = 9) and rob house 5 (money = 1).
             Total amount you can rob = 2 + 9 + 1 = 12.
```

#### 序列型DP解法

```java
class Solution {
    public int rob(int[] nums) {
        if(nums.length==0) return 0;//如果没有人家则不能偷
        if(nums.length==1) return nums[0];//如果只有一家则只能偷一家
        int[] dp=new int[nums.length];
        dp[0]=nums[0];//偷前0家
        dp[1]=Math.max(nums[0],nums[1]);//偷前1家
        for(int i=2;i<nums.length;i++){
            dp[i]=Math.max(nums[i]+dp[i-2],dp[i-1]);//偷前i 家
        }
        return dp[dp.length-1];
    }
}
```



#### 树状DP解法

```java
class Solution {
    public static class ReturnType{
        int stolen;
        int noStolen;
        public ReturnType(int stolen,int noStolen){
            this.stolen=stolen;
            this.noStolen=noStolen;
        }
    }
    public static int rob(int[] nums) {
        if(nums.length==0) return 0;//如果没有人家则不能偷
        ReturnType data = process(nums,0);
        return Math.max(data.stolen,data.noStolen);
    }

    public static ReturnType process(int[] nums,int i){
        if(i==nums.length){
            return new ReturnType(0,0);
        }
        int stolen=nums[i];
        int noStolen=0;

        ReturnType data = process(nums,i+1);
        stolen +=data.noStolen; // 若偷了当前家，则下一家不能偷
        noStolen +=Math.max(data.stolen,data.noStolen); // 若不偷当前家，则可偷下一家或不偷下一家，选取其最大值
        return new ReturnType(stolen,noStolen);
    }
}
```





#### 题目二：740. Delete and Earn

Given an array `nums` of integers, you can perform operations on the array.

In each operation, you pick any `nums[i]` and delete it to earn `nums[i]` points. After, you must delete **every** element equal to `nums[i] - 1` or `nums[i] + 1`.

You start with 0 points. Return the maximum number of points you can earn by applying such operations.

**Example 1:**

```
Input: nums = [3, 4, 2]
Output: 6
Explanation: 
Delete 4 to earn 4 points, consequently 3 is also deleted.
Then, delete 2 to earn 2 points. 6 total points are earned.
```

 

**Example 2:**

```
Input: nums = [2, 2, 3, 3, 3, 4]
Output: 9
Explanation: 
Delete 3 to earn 3 points, deleting both 2's and the 4.
Then, delete 3 again to earn 3 points, and 3 again to earn 3 points.
9 total points are earned.
```

 

**Note:**

- The length of `nums` is at most `20000`.
- Each element `nums[i]` is an integer in the range `[1, 10000]`.

#### 序列型DP解法

```java
class Solution {
    public static int deleteAndEarn(int[] nums) {
        int[] bucket = new int[10001];
        for(int x:nums){
            bucket[x]++;
        }
        
        int N=bucket.length;
        int[] dp=new int[N];// dp[i] 表示偷前i家所能获取的最大值（偷或不偷第i家）
        dp[0]=bucket[0];
        dp[1]=Math.max(bucket[0],bucket[1]);
        for(int i=2;i<N;i++){
            dp[i]=Math.max(dp[i-1],dp[i-2]+bucket[i]*i);
        }
        return dp[N-1];
    }
}
```

#### 树状DP解法

```java
class Solution {
    public static int deleteAndEarn(int[] nums) {
        int[] bucket = new int[10001];
        for(int x:nums){
            bucket[x]++;
        }

        ReturnType data = process(bucket,0);
        return Math.max(data.stolen,data.noStolen);
    }

    public static class ReturnType{
        int stolen;
        int noStolen;
        public ReturnType(int stolen,int noStolen){
            this.stolen=stolen;
            this.noStolen=noStolen;
        }
    }

    public static ReturnType process(int[] bucket,int i){
        if(i==bucket.length){
            return new ReturnType(0,0);
        }
        int stolen=bucket[i]*i;
        int noStolen=0;

        ReturnType data = process(bucket,i+1);
        stolen +=data.noStolen;
        noStolen +=Math.max(data.stolen,data.noStolen);
        return new ReturnType(stolen,noStolen);
    }
}
```

#### 总结树状DP解题方法

解题步骤：

1. 列可能性
2. 根据可能性整合信息（确定返回的结构体）
3. 改递归

以 198. House Robber 为例进行说明。

* 列可能性

  由于不能偷相邻的房子，所以当前房子是否可偷受前面情况的影响，或者说当前房子偷不偷会影响后面房子是否可偷的影响；

  所以，考虑当前房子而言，就只有两种可能

  1. 偷
  2. 不偷

* 根据可能性整合信息（确定返回的结构体）

  由于只有两种可能性，所以确定结构体为：

  ```java
  public static class ReturnType{
          int stolen;
          int noStolen;
          public ReturnType(int stolen,int noStolen){
              this.stolen=stolen;
              this.noStolen=noStolen;
          }
      }
  ```

* 改递归

  若当前房子偷了，则只能取下一家房子不偷情况下的数据；

  若当前房子不偷，则有两种情况：偷或不偷下一家房子。取其最大值。

  ```java
  ReturnType data = process(nums,i+1);
          stolen +=data.noStolen; // 若偷了当前家，则下一家不能偷
          noStolen +=Math.max(data.stolen,data.noStolen); // 若不偷当前家，则可偷下一家或不偷下一家，选取其最大值
  ```

  