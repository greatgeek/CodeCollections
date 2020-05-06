#### 474. Ones and Zeroes

In the computer world, use restricted resource you have to generate maximum benefit is what we always want to pursue.

For now, suppose you are a dominator of **m** `0s` and **n** `1s` respectively. On the other hand, there is an array with strings consisting of only `0s` and `1s`.

Now your task is to find the maximum number of strings that you can form with given **m** `0s` and **n** `1s`. Each `0` and `1` can be used at most **once**.

**Note:**

1. The given numbers of `0s` and `1s` will both not exceed `100`
2. The size of given string array won't exceed `600`.

 

**Example 1:**

```
Input: Array = {"10", "0001", "111001", "1", "0"}, m = 5, n = 3
Output: 4

Explanation: This are totally 4 strings can be formed by the using of 5 0s and 3 1s, which are “10,”0001”,”1”,”0”
```

 

**Example 2:**

```
Input: Array = {"10", "0", "1"}, m = 1, n = 1
Output: 2

Explanation: You could form "10", but then you'd have nothing left. Better form "0" and "1".
```

 

#### 摘自Discuss

For people finding this problem hard to understand:

Try and understand the basic knapsack problem and how it's solved in two different ways. We could either build the ```dp``` table top down or bottom up. The top down approach for knapsack with O(nW) runtime and O(nW) space is listed below:

```java
/**
     * 
     * @param val 物品的价值数组
     * @param wt  物品的重量数组
     * @param n   共有n件物品
     * @param W    背包最大承 W
     * @return 返回装下的最大价值
     */
    int knapsack(int[] val, int[] wt,int n,int W){
        int[][] dp=new int[n+1][W+1];
        // initial
        for(int i=0;i<dp.length;i++){
            for(int j=0;j<dp[i].length;j++){
                dp[i][j]=0;
            }
        }
        
        for(int i=1;i<=n;i++){
            for(int w=1;w<=W;w++){
                dp[i][w]=dp[i-1][w];// don't include the item
                if(w>=wt[i-1]){
                    dp[i][w]=Math.max(dp[i][w],val[i-1]+dp[i-1][w-wt[i-1]]);
                }
            }
        }
        return dp[n][W];
    }
```

The same knapsack problem could be solved with O(nW) runtime and O(W) space by building the table the bottom up as shown below:

```java
    int knapsack2(int[] val, int[] wt,int n,int W){
        int[] dp=new int[W+1];
        // initial
        for(int i=0;i<dp.length;i++){
            dp[i]=0;
        }

        for(int i=1;i<=n;i++){
            for(int w=W;w>=wt[i-1];w--){
                dp[w]=Math.max(dp[w],val[i-1]+dp[w-wt[i-1]]);
            }
        }
        return dp[W];
    }
```

Coming to this problem, if you could understand how above approaches work, this problem is pretty similar with two knapsacks. Though the first solution gives TLE for this problem, I'm posting the easy solution just so that you understand.

```java
public static int findMaxForm(String[] strs, int m, int n) {
        int N = strs.length;
        int[][][] dp = new int[N+1][m+1][n+1];
        for (int i = 1; i <= N; i++) {
            int zeros = 0;
            int ones = 0;
            for (char x : strs[i-1].toCharArray()) {
                if (x == '1') ones++;
                else zeros++;
            }

            for(int j=0;j<=m;j++){
                for(int k=0;k<=n;k++){
                    dp[i][j][k]=dp[i-1][j][k];
                    if(j>=zeros && k>=ones){
                        dp[i][j][k]=Math.max(1+dp[i-1][j-zeros][k-ones],dp[i-1][j][k]);
                    }
                }
            }
        }
        return dp[N][m][n];
    }
```

