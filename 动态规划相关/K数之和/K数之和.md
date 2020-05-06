### [89. k Sum](https://www.lintcode.com/problem/k-sum/note/217129)

Given *n* distinct positive integers, integer *k* (*k* <= *n*) and a number *target*.

Find *k* numbers where sum is target. Calculate how many solutions there are?

### Example

**Example 1**

```plain
Input:
List = [1,2,3,4]
k = 2
target = 5
Output: 2
Explanation: 1 + 4 = 2 + 3 = 5
```

**Example 2**

```plain
Input:
List = [1,2,3,4,5]
k = 3
target = 6
Output: 1
Explanation: There is only one method. 1 + 2 + 3 = 6
```



## 代码

```java
public class Solution {
    /**
     * @param A: An integer array
     * @param k: A positive integer (k <= length(A))
     * @param target: An integer
     * @return: An integer
     */
    public int kSum(int[] A, int k, int target) {
        int n = A.length;
        int[][][] f=new int[n+1][k+1][target+1];
        // f[i][j][t] 表示从数组的前i个数中选择j个数组成和为t的组合数
        // 正向扫描，需要用到已更新的值
        for(int i=0;i<=n;i++){
            for(int j=0;j<=k;j++){
                for(int t=0;t<=target;t++){
                    if(i==0&&j==0&&t==0){
                        f[i][j][t]=1;
                    }else if(i==0){
                        f[i][j][t]=0;
                    }else{
                        f[i][j][t]=f[i-1][j][t]; // 继承
                        if(j-1>=0&&t-A[i-1]>=0){
                          f[i][j][t] +=f[i-1][j-1][t-A[i-1]];  // 索取
                        }
                       
                    }
                }
            }
        }
        return f[n][k][target];
    }
}
```

