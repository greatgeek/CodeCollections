# [K 数之和](https://www.lintcode.com/problem/k-sum/description)

## 描述

给定 *n* 个不同的正整数，整数 *k*（*k* <= *n*）以及一个目标数字 *target*。　
 在这 *n* 个数里面找出 *k* 个数，使得这 *k* 个数的和等于目标数字，求问有多少种方案？

### 样例

**样例1**

```plain
输入:
List = [1,2,3,4]
k = 2
target = 5
输出: 2
说明: 1 + 4 = 2 + 3 = 5
```

**样例2**

```plain
输入:
List = [1,2,3,4,5]
k = 3
target = 6
输出: 1
说明: 只有这一种方案。 1 + 2 + 3 = 6
```

**talk is cheap ,show me the code**

```java
    public int kSum(int[] A, int k, int target) {
        int n = A.length;
        int[][][] f=new int[n+1][k+1][target+1];
        // f[i][j][t] 表示从数组的前i个数中选择j个数组成和为t的组合数
        
        for(int i=0;i<=n;i++){
            for(int j=0;j<=k;j++){
                for(int t=0;t<=target;t++){
                    if(i==0&&j==0&&t==0){
                        f[i][j][t]=1;
                    }else if(i==0){
                        f[i][j][t]=0;
                    }else{
                        if(j-1>=0&&t-A[i-1]>=0){
                            f[i][j][t]=f[i-1][j][t]+f[i-1][j-1][t-A[i-1]];
                        }else{
                            f[i][j][t]=f[i-1][j][t];
                        }
                    }
                }
            }
        }
        return f[n][k][target];
    }
```



```java
    public int kSum(int[] A, int k, int target) {
        int n=A.length;
        int[][] f=new int [k+1][target+1];
        f[0][0]=1;
        for (int i=0;i<n;i++){
            for(int K=k;K>=0;K--){
                for(int T=target;T>=0;T--){
                    if(K-1>=0&&T-A[i]>=0){
                        f[K][T] +=f[K-1][T-A[i]];
                    }
                }
            }
        }
        return f[k][target];
    }
```

