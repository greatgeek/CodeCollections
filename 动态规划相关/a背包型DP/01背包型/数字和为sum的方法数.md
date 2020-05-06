# 数字和为sum的方法数

## 题目描述

给定一个有n个正整数的数组A和一个整数sum,求选择数组A中部分数字和为sum的方案数。
 当两种选取方案有一个数字的下标不一样,我们就认为是不同的组成方案。

## 输入描述:

```
输入为两行:
 第一行为两个正整数n(1 ≤ n ≤ 1000)，sum(1 ≤ sum ≤ 1000)
 第二行为n个正整数A[i](32位整数)，以空格隔开。
```

## 输出描述:

```
输出所求的方案数
```

示例1

## 输入

```mathematica
5 15 5 5 10 2 3
```

## 输出

```mathematica
4
```



```java
public static long dp(int[] nums,int sum){
        int n=nums.length;
        long[][] f=new long[n+1][sum+1];
        f[0][0]=1;
        for(int i=1;i<=n;i++){
            for(int j=0;j<=sum;j++){
                f[i][j]=f[i-1][j]; // 1. 继承
                // 索取法，可更新从上一行继承下来的值。此处若用贡献法，会被下一次从上一行继承下来的值给覆盖导致结果错误（需要特别注意）
                if(j-nums[i-1]>=0 && f[i-1][j-nums[i-1]]>0){
                    f[i][j] +=f[i-1][j-nums[i-1]]; // 2. 添加
                }
            }
        }
        return f[n][sum];
    }
```



## 可以将空间优化来一行数组

```java
public static long dp(int[] nums,int sum){
        int n=nums.length;
        long[] f=new long[sum+1];
        f[0]=1;
        for(int i=0;i<n;i++){
            for(int j=sum;j>=0;j--){ // 需要逆向遍历，防止值被覆盖
                if(j-nums[i]>=0 && f[j-nums[i]]>0){
                    f[j] +=f[j-nums[i]]; // 1. 继承 2. 添加
                }

            }
        }
        return f[sum];
    }
```

