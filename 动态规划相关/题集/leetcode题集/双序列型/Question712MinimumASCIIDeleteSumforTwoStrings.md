#### 712. Minimum ASCII Delete Sum for Two Strings

Given two strings `s1, s2`, find the lowest ASCII sum of deleted characters to make two strings equal.

**Example 1:**

```
Input: s1 = "sea", s2 = "eat"
Output: 231
Explanation: Deleting "s" from "sea" adds the ASCII value of "s" (115) to the sum.
Deleting "t" from "eat" adds 116 to the sum.
At the end, both strings are equal, and 115 + 116 = 231 is the minimum sum possible to achieve this.
```



**Example 2:**

```
Input: s1 = "delete", s2 = "leet"
Output: 403
Explanation: Deleting "dee" from "delete" to turn the string into "let",
adds 100[d]+101[e]+101[e] to the sum.  Deleting "e" from "leet" adds 101[e] to the sum.
At the end, both strings are equal to "let", and the answer is 100+101+101+101 = 403.
If instead we turned both strings into "lee" or "eet", we would get answers of 433 or 417, which are higher.
```



**Note:**

`0 < s1.length, s2.length <= 1000`.

All elements of each string will have an ASCII value in `[97, 122]`.

#### 一、确定状态

```f[i][j]```表示扫描到**str1**前```i```个字符，**str2**前```j```个字符所花费的最小删除代价。在此删除代价下，**str1**前```i```个字符与**str2**前```j```个字符各自组成的字符串是相等的。

#### 二、状态转移

$$
f[i][j]=
\left\{
		\begin{array}{l}
		f[i-1][j-1]; & str1[i]==str[j] & \\
		Min\{f[i][j-1]+str2[j],f[i-1][j]+str1[i]\};& str1[i]!=str2[j]
		\end{array}
\right.
$$

#### 三、初始条件

$f[0][j] =\sum_{j=0}^j str2[j]$;

```f[0][j]``` 表示扫描到```str1``` 前0个字符，```str2```前```j```个字符所花费的最小删除代价。即```str1```为空串时，每遇到一个来自```str2```的字符都要删除掉，并将代价累计入总删除代价

$f[i][0] =\sum_{i=0}^i str1[i]$;

```f[i][0]``` 表示扫描到```str2``` 前0个字符，```str1```前```i```个字符所花费的最小删除代价。即```str2```为空串时，每遇到一个来自```str1```的字符都要删除掉，并将代价累计入总删除代价

#### 代码

```java
class Solution {
    public int minimumDeleteSum(String s1, String s2) {
         int m=s1.length(),n=s2.length();
        int[][] f = new int[m+1][n+1];
        /**
         * f[i][j] 表示到str1 前i个字符，str2前j 个字符所花费的最小删除代价
         */

        // 初始条件
        /**
         * f[0][j] 表示到str1 前0个字符，str2前j个字符所花费的最小删除代价。即str1为空串时，每遇到一个来自str2的字符都要删除掉，并将代价累计入总删除代价
         */
        for(int j=1;j<=n;j++){
            f[0][j]=f[0][j-1]+s2.codePointAt(j-1);
        }
        /**
         * f[i][j] 表示到str2 前0个字符，str1前j个字符所花费的最小删除代价。即str2为空串时，每遇到一个来自str1的字符都要删除掉，并将代价累计入总删除代价
         */
        for(int i=1;i<=m;i++){
            f[i][0]=f[i-1][0]+s1.codePointAt(i-1);
        }

        // 开始动态规划计算
        for(int i=1;i<=m;i++){
            for(int j=1;j<=n;j++){
                if(s1.codePointAt(i-1)==s2.codePointAt(j-1)){
                    f[i][j]= f[i-1][j-1]; // 若str1[i-1]==str2[j-1],则不需要花费删除代价，直接继承
                }else{// 若str1[i-1]！=str2[j-1],选择一个删除代价小的删除，并累计入总删除代价
                    f[i][j] = Math.min(f[i][j-1]+s2.codePointAt(j-1),f[i-1][j]+s1.codePointAt(i-1));
                }
            }
        }
        return f[m][n];
    }
}
```

