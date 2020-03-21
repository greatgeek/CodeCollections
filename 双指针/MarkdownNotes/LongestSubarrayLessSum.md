![image-20200321114742125](I:\GreatGeek\CodeCollections\双指针\LongestSubarrayLessSum.assets\image-20200321114742125.png)

```java
	arr .... 		7,		5,		5,		-3,		-1
	index ...		n-5,	n-4,	n-3,	n-2,	n-1
	min_sum ... 	7,		5,		1,		-4,		-1
 min_sum_index ...	n-5,	n-4,	n-3,	n-1,	n-1
```



```java
 public static int maxLengthAwesome(int[] arr, int aim) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int[] sums = new int[arr.length];
        int[] ends = new int[arr.length];
        sums[arr.length-1] = arr[arr.length -1];
        ends[arr.length-1] = arr.length -1;

        for (int i = arr.length - 2; i >= 0; i--) {
            if (sums[i + 1] < 0) { // 有利可图
                sums[i] = arr[i] + sums[i + 1];
                ends[i] = ends[i+1];
            } else { // 无利可图
                sums[i] = arr[i];
                ends[i] = i;
            }
        }
        int R = 0;
        int sum = 0;
        int len = 0;
        for (int start = 0; start < arr.length; start++) {
            while (R < arr.length && sum + sums[R] <= aim) {
                sum += sums[R];
                R = ends[R] + 1;
            }
            sum -= R > start ? arr[start] : 0;
            len = Math.max(len, R - start);
            R = Math.max(R, start + 1);
        }
        return len;
    }
```

