#### 题目描述

You are given `n` pairs of numbers. In every pair, the first number is always smaller than the second number.

Now, we define a pair `(c, d)` can follow another pair `(a, b)` if and only if `b < c`. Chain of pairs can be formed in this fashion.

Given a set of pairs, find the length longest chain which can be formed. You needn't use up all the given pairs. You can select pairs in any order.

**Example 1:**

```
Input: [[1,2], [2,3], [3,4]]
Output: 2
Explanation: The longest chain is [1,2] -> [3,4]
```



**Note:**

1. The number of given pairs will be in the range [1, 1000].

   

   ##### 动态规划解法

#### 一、确定状态

对```piars```的第```0```维进行排序，这样一来就有$ i<j \&\& piars[i][0] < piars[j][0]$成立

```f[j]```表示以第```j```对作为结尾时的最大长度链。

#### 二、确定依赖状态

```f[j]``` = $\{pairs[i][1]<pairs[j][0]\}\&\&Max_{0<=i<j}\{f[i]\}+1$

```f[j]```需要从```j```之前的每一个```i```中寻找最大值后加上1，也就是在以第```i```对作为结尾时的最大长度链时再加上当前的第```j```对。

#### 三、初始值

每一个对的最大长度链至少为其自身，所以初始值为```1```。

#### 四、计算顺序

从前向后算，即先算子问题，再算父问题。

时间复杂度$O(N^2)$, 空间复杂度$O(N)$

所以说并不见得，动态规划就是最快的。后面讲解本题的贪心算法的时间复杂度为$O(N)$.

#### 五、代码

```java
class Solution {
    public int findLongestChain(int[][] pairs) {
        Arrays.sort(pairs,(a,b)->(a[0]-b[0]));
        int N = pairs.length;
        int[] f = new int[N];
        
        for(int j=0;j<N;j++){
            f[j]=1;// 初始值至少为自身一个对，所以为1
            for(int i=0;i<j;i++){
                if(pairs[i][1]<pairs[j][0]){
                    f[j]=Math.max(f[i]+1,f[j]); // 找出在j 之前的i 个对的最大链长度，再加上当前第j对
                }
            }
        }
        
        int max=0;
        for(int x:f){
            if(x>max){
                max=x;
            }
        }
        return max;
    }
}
```

##### 贪心解法

本题与会议安排非常类似，将每一对看成是一场会议的开始赶时间与结束时间组成即可。

贪心算法与动态规划不同，贪心算法只追求局部最优，而动态规划追求全局最优。

#### 一、确定贪心的策略

尽可能挑选结束时间早的。也就是对```pairs```的第```1```进行排序，然后从前向后取出的每次都是当前结束时间最早的，若其开始时间大于前一场会议的结束时间，则可加入会议安排。

#### 二、代码

```java
class Solution {
    public int findLongestChain(int[][] pairs) {
        PriorityQueue<int[]> pq = new PriorityQueue<>((a,b)->(a[1]-b[1]));// 对第1维进行排序
        for(int i=0;i<pairs.length;i++){
            pq.offer(pairs[i]);
        }
        
        Deque<int[]> deque = new LinkedList<>();
        deque.offer(pq.poll());
        while(!pq.isEmpty()){
            int[] cur = deque.peekLast();
            int[] item = pq.poll();
            if(cur[1]<item[0]){ // 若取出的会议开始时间比前一场会议的结束时间大，则加入日程安排
                deque.offer(item);
            }
        }
        return deque.size();
    }
}
```



### 精简版

```java
class Solution {
    public int findLongestChain(int[][] pairs) {
        Arrays.sort(pairs, (a, b) -> a[1] - b[1]);
        int cur = Integer.MIN_VALUE, ans = 0;
        for (int[] pair: pairs) if (cur < pair[0]) {
            cur = pair[1];
            ans++;
        }
        return ans;
    }
}
```

