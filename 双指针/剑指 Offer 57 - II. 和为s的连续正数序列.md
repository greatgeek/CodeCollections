# [剑指 Offer 57 - II. 和为s的连续正数序列](https://leetcode-cn.com/problems/he-wei-sde-lian-xu-zheng-shu-xu-lie-lcof/)

输入一个正整数 target ，输出所有和为 target 的连续正整数序列（至少含有两个数）。

序列内的数字由小到大排列，不同序列按照首个数字从小到大排列。

 

示例 1：
```
输入：target = 9
输出：[[2,3,4],[4,5]]
```
示例 2：
```
输入：target = 15
输出：[[1,2,3,4,5],[4,5,6],[7,8]]
```


限制：

    1 <= target <= 10^5



## Approach#1 使用双端队列

```java
    public int[][] findContinuousSequence(int target) {
        Deque<Integer> deque = new LinkedList<>();
        LinkedList<LinkedList<Integer>> res = new LinkedList<>();
        for(int i=1;i<target;++i){
            if(!deque.isEmpty()){
                while((deque.peekFirst()+deque.peekLast())*deque.size()/2>target)
                    deque.removeFirst();
                int sum=(deque.peekFirst()+deque.peekLast())*deque.size()/2;
                if(sum==target){
                    res.add(new LinkedList<>(deque));
                    deque.removeFirst();
                }
            }
            deque.addLast(i);
        }
        int length=res.size();
        int[][] ans = new int[length][];
        int index=0;
        for(int i=0;i<length;++i){
            ans[index++]=transfer(res.get(i));
        }
        return ans;
    }

    private int[] transfer(List<Integer> list){
        int[] item = new int[list.size()];
        for(int i=0;i<list.size();++i){
            item[i]=list.get(i);
        }
        return item;
    }
```

## Approach#2 使用双指针

```java
    public int[][] findContinuousSequence(int target) {
        int L=1, R=1;
        int sum=0;
        List<int[]> list = new LinkedList<>();
        while(L<target && R<target){
            if(sum<target){
                sum +=R;
                R++;
            }else if(sum>target){
                sum -=L;
                L++;
            }else {
                int[] ans=new int[R-L];
                for(int i=L,index=0;i<R;++i){
                    ans[index++]=i;
                }
                list.add(ans);
                sum -=L;
                L++;
            }
        }

        int[][] res=new int[list.size()][];
        for(int i=0,index=0;i<list.size();++i){
            res[index++]=list.get(i);
        }
        return res;
    }
```

