# [分割回文串II](https://leetcode-cn.com/problems/palindrome-partitioning-ii/)

**题目描述**

给定一个字符串 s，将 s 分割成一些子串，使每个子串都是回文串。

返回符合要求的最少分割次数。

示例:
```
输入: "aab"
输出: 1
解释: 进行一次分割就可将 s 分割成 ["aa","b"] 这样两个回文子串。
```

## 分析

回文串分两种

* 长度为奇数
* 长度为偶数

### **在字符串中找到所有回文串**

以字符吕的每个字符为中点，向两边扩展，找到所有回文串。

* 考虑奇数长度回文串和偶数长度回文串；
* 用 `isPalin[i][j]`表示`S[i..j]`是否是回文串；
* 时间复杂度$O(N^2)$

**show me the code**

```java
    public static boolean[][] isPalin(char[] ss){
        int n=ss.length;
        boolean[][] isPalin = new boolean[n][n];
        for (int i=0;i<n;i++){
            isPalin[i][i]=true;
            // 长度为奇数时
            int step=0;
            while (i-step>=0 && i+step<n){
                    if(ss[i-step]==ss[i+step] &&(i-step+1>i+step-1|| isPalin[i-step+1][i+step-1]) ){
                        isPalin[i-step][i+step]=true;
                    }
                step++;
            }

            // 长度为偶数时
            int j=i+1;
            step=0;
            while (i-step>=0 && j+step<n){
                    if(ss[i-step]==ss[j+step] && (i-step+1>j+step-1 || isPalin[i-step+1][j+step-1]) ){
                        isPalin[i-step][j+step]=true;
                    }

                step++;
            }
        }
        return isPalin;
    }
```

### 回到原题进行分析

* S 最少划分成多少个回文串；
* `f[i]=min(j=0,..,i-1){f[j]+1|S[j..i-1]是回文串}`
* `f[i]=min(j=0,..,i-1){f[j]+1|isPalin[j][i-1]==true}`
* 最后结果为`f[n]-1`（因为要求的是划分次数）

### talk is cheap ,show me the code

```java
    public static int minCut(String s) {
        char[] ss = s.toCharArray();
        int n=ss.length;
        if(n==0) return 0;
        boolean[][] isPalin=isPalin(ss);
        int[] f=new int[n+1];
        f[0]=0;
        for (int i=1;i<=n;i++){
            f[i]=Integer.MAX_VALUE;
            for(int j=0;j<i;j++){
                if(isPalin[j][i-1]){
                    f[i]=Math.min(f[i],f[j]+1);
                }
            }
        }
        return f[n]-1;
    }

    public static boolean[][] isPalin(char[] ss){
        int n=ss.length;
        boolean[][] isPalin = new boolean[n][n];
        for (int i=0;i<n;i++){
            isPalin[i][i]=true;
            // 长度为奇数时
            int step=0;
            while (i-step>=0 && i+step<n){
                    if(ss[i-step]==ss[i+step] &&(i-step+1>i+step-1|| isPalin[i-step+1][i+step-1]) ){
                        isPalin[i-step][i+step]=true;
                    }
                step++;
            }

            // 长度为偶数时
            int j=i+1;
            step=0;
            while (i-step>=0 && j+step<n){
                    if(ss[i-step]==ss[j+step] && (i-step+1>j+step-1 || isPalin[i-step+1][j+step-1]) ){
                        isPalin[i-step][j+step]=true;
                    }

                step++;
            }
        }
        return isPalin;
    }
```

