# [Partition Array](https://www.lintcode.com/problem/partition-array/description)

**Description**

给出一个整数数组 *nums* 和一个整数 *k*。划分数组（即移动数组 *nums* 中的元素），使得：

- 所有小于k的元素移到左边
- 所有大于等于k的元素移到右边

返回数组划分的位置，即数组中第一个位置 *i*，满足 *nums*[*i*] 大于等于 *k*。



> 你应该真正的划分数组 *nums*，而不仅仅只是计算比 *k* 小的整数数，如果数组 *nums* 中的所有元素都比 *k* 小，则返回 nums.length。

### Example

例1:

```
输入:
[],9
输出:
0
```

例2:

```
输入:
[3,2,2,1],2
输出:1
解释:
真实的数组为[1,2,2,3].所以返回 1
```

### Challenge

使用 O(n) 的时间复杂度在数组上进行划分。

```java
    public int partitionArray(int[] nums, int k) {
        int small = -1;
        for(int i=0;i<nums.length;){
            if(nums[i]<k) {
                swap(nums, ++small, i);
            }
            i++;
        }
        return small+1;
    }
    
        public static void swap(int[] nums ,int i,int j){
        int tmp=nums[i];
        nums[i]=nums[j];
        nums[j]=tmp;
    }
```

