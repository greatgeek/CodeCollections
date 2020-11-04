# [剑指 Offer 40. 最小的k个数](https://leetcode-cn.com/problems/zui-xiao-de-kge-shu-lcof/)

输入整数数组 arr ，找出其中最小的 k 个数。例如，输入4、5、1、6、2、7、3、8这8个数字，则最小的4个数字是1、2、3、4。

 

示例 1：
```
输入：arr = [3,2,1], k = 2
输出：[1,2] 或者 [2,1]
```
示例 2：
```
输入：arr = [0,1,2,1], k = 1
输出：[0]
```


限制：

* 0 <= k <= arr.length <= 10000
* 0 <= arr[i] <= 10000

## Show me the code

### Approach# 1 使用快排分区思想

使用快排分区思想，将最小的 K 个数作为一个区分出来。

```java
class Solution {
    public int[] getLeastNumbers(int[] arr, int k) {
        if(arr.length==0 || k==0) return new int[0];
        if(k==arr.length) return arr;
  
        int left=0,right=arr.length-1;
        int index=partition(arr,left,right);
        while(index!=k){
          if(index > k) right=index-1;
          else if(index < k) left=index+1;
          index = partition(arr,left,right);
        } 
        int[] res = new int[k];
        for(int i=0;i<k;++i){
            res[i]=arr[i];
        }
        return res;
    }

    private int partition(int[] arr,int left,int right){
        int pivot=arr[right];
        int start=left,end=right-1;
        while(start<=end){
            while(start<=end && arr[start]<=pivot) start++;
            while(start<=end && arr[end]>pivot) end--;
            if(start<end){
                swap(arr,start,end);
            }
        }
        swap(arr,start,right);
        return start;
    }

    private void swap(int[] arr, int left,int right){
        int temp=arr[left];
        arr[left]=arr[right];
        arr[right]=temp;
    }
}
```

### Approach#2 使用最大堆

```java
import java.util.*;

public class GetLeastNumbers {
    public int[] getLeastNumbers(int[] arr, int k) {
        if (k == 0) {
            return new int[0];
        }
        // 使用一个最大堆（大顶堆）
        // Java 的 PriorityQueue 默认是小顶堆，添加 comparator 参数使其变成最大堆
        Queue<Integer> heap = new PriorityQueue<>(k, (i1, i2) -> Integer.compare(i2, i1));

        for (int e : arr) {
            // 当前数字小于堆顶元素才会入堆
            if (heap.isEmpty() || heap.size() < k || e < heap.peek()) {
                heap.offer(e);
            }
            if (heap.size() > k) {
                heap.poll(); // 删除堆顶最大元素
            }
        }

        // 将堆中的元素存入数组
        int[] res = new int[heap.size()];
        int j = 0;
        for (int e : heap) {
            res[j++] = e;
        }
        return res;
    }
}
```

