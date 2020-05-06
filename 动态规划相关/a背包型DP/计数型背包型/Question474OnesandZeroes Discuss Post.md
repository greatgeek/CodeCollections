For people finding this problem hard to understand:
Try and understand the basic knapsack problem and how it's solved in two different ways. We could either build the dp table top down or bottom up. The top down approach for knapsack with O(nW) runtime and O(nW) space is listed below:



```c++
int knapsack(int val[], int wt[], int n, int W)
{
   int dp[n+1][W+1];
   memset(dp, 0, sizeof dp);
   for (int i = 1; i <= n; i++)
   {
       for (w = 1; w <= W; w++)
       {
           dp[i][w] = dp[i-1][w];  //don't include the item
           if (wt[i-1] <= w)
                 dp[i][w] = max(dp[i][w], 
                           val[i-1] + dp[i-1][w-wt[i-1]]);  
       }
   }
   return dp[n][W];
}
```



The same knapsack problem could be solved with O(nW) runtime and O(W) space by building the table bottom up as shown below:



```c++
int knapsack(int val[], int wt[], int n, int W)
{
    int dp[W+1];
    memset(dp, 0, sizeof(dp));
    for(int i=0; i < n; i++) 
        for(int j=W; j>=wt[i]; j--)
            dp[j] = max(dp[j] , val[i] + dp[j-wt[i]]);
    return dp[W];
}
```



Coming to this problem, if you could understand how above approaches work, this problem is pretty similar with two knapsacks. Though the first solution gives TLE for this problem, I'm posting the easy solution just so that you understand.



```c++
class Solution {
public:
    int findMaxForm(vector<string>& strs, int m, int n) {
        int len = strs.size();
        int dp[len+1][m+1][n+1];
        memset(dp, 0, sizeof dp);
        
        for (int i=1;i<=len;i++){
            for (int j=0;j<=m;j++){
                for (int k=0;k<=n;k++){
                    int ones = count(strs[i-1].begin(), strs[i-1].end(), '1');
                    int zeros = strs[i-1].size()-ones;
                    int res = dp[i-1][j][k];
                    if (zeros<=j && ones<=k) 
                       res = max(res, dp[i-1][j-zeros][k-ones]+1);
                    dp[i][j][k] = res;
                }
            }
        }
        
        return dp[len][m][n];
        
    }
};
```