#### 题目描述

Given a **non-empty** string *s* and a dictionary *wordDict* containing a list of **non-empty** words, determine if *s* can be segmented into a space-separated sequence of one or more dictionary words.

**Note:**

- The same word in the dictionary may be reused multiple times in the segmentation.
- You may assume the dictionary does not contain duplicate words.

**Example 1:**

```
Input: s = "leetcode", wordDict = ["leet", "code"]
Output: true
Explanation: Return true because "leetcode" can be segmented as "leet code".
```

**Example 2:**

```
Input: s = "applepenapple", wordDict = ["apple", "pen"]
Output: true
Explanation: Return true because "applepenapple" can be segmented as "apple pen apple".
             Note that you are allowed to reuse a dictionary word.
```

**Example 3:**

```
Input: s = "catsandog", wordDict = ["cats", "dog", "sand", "and", "cat"]
Output: false
```

#### 一、确定状态

```f[0][j]``` 表示从0 到当前第 j 个字符可以被分解。这个状态依赖的状态有两个：```f[0][i]```和```f[i+1][j]```。

若 ```f[0][i]```和```f[i+1][j]``` 同时可以被分解，```f[0][j]```才可以被分解。

### 子问题

```f[0][i]```和```f[i+1][j]```同样依赖它们各自前面两个状态。

#### 二、转移方程

```f[0][j] = f[0][i] && f[i+1][j]```

#### 三、初始条件

```f[0][0] = true```表示从 0 到当前第 0 个字符可以被分解。

#### 四、计算顺序

从前向后算。

#### 五、代码

```java
class Solution {
    public boolean wordBreak(String s, List<String> wordDict) {
        Set<String> set = new HashSet<>();
        for(String str : wordDict){
            set.add(str);
        }
        int n=s.length();
        boolean[] f = new boolean[n+1];
        f[0]=true; // f[0] stands for substring(0,0)
        for(int i=1;i<=s.length();i++){
            for(int j=0;j<i;j++){
                if(f[j] && set.contains(s.substring(j,i))){
                    f[i]=true;
                    break;
                }
            }
        }
        return f[n];
    }
}
```





