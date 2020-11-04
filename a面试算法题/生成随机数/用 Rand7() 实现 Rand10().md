# [470. 用 Rand7() 实现 Rand10()](https://leetcode-cn.com/problems/implement-rand10-using-rand7/)

已有方法 rand7 可生成 1 到 7 范围内的均匀随机整数，试写一个方法 rand10 生成 1 到 10 范围内的均匀随机整数。

不要使用系统的 Math.random() 方法。

 

示例 1:
```
输入: 1
输出: [7]
```
示例 2:
```
输入: 2
输出: [8,4]
```
示例 3:
```
输入: 3
输出: [8,1,10]
```


提示:

1. rand7 已定义。
2. 传入参数: n 表示 rand10 的调用次数。

进阶:

1. rand7()调用次数的 期望值 是多少 ?
2. 你能否尽量少调用 rand7() ?

## 解题思路

![image-20200818102712817](用 Rand7() 实现 Rand10().assets/image-20200818102712817.png)

![image-20200818102746898](用 Rand7() 实现 Rand10().assets/image-20200818102746898.png)

那么我们可以写出下面的代码

```java
class Solution extends SolBase {
    public int rand10() {
        // 首先得到一个数
        int num = (rand7() - 1) * 7 + rand7();
        // 只要它还大于10，那就给我不断生成，因为我只要范围在1-10的，最后直接返回就可以了
        while (num > 10){
            num = (rand7() - 1) * 7 + rand7();
        }
        return num;
    }
}
```

提交发现跑得很慢

![image-20200818102943239](用 Rand7() 实现 Rand10().assets/image-20200818102943239.png)

![image-20200818103011032](用 Rand7() 实现 Rand10().assets/image-20200818103011032.png)

```java
public int rand10() {
    // 首先得到一个数
    int num = (rand7() - 1) * 7 + rand7();
    // 只要它还大于40，那你就给我不断生成吧
    while (num > 40)
        num = (rand7() - 1) * 7 + rand7();
    // 返回结果，+1是为了解决 40%10为0的情况
    return 1 + num % 10;
}
```

![image-20200818103048323](用 Rand7() 实现 Rand10().assets/image-20200818103048323.png)

![image-20200818103115526](用 Rand7() 实现 Rand10().assets/image-20200818103115526.png)

```java
/**
 * The rand7() API is already defined in the parent class SolBase.
 * public int rand7();
 * @return a random integer in the range 1 to 7
 */
class Solution extends SolBase {
    public int rand10() {
        while (true){
            int num = (rand7() - 1) * 7 + rand7();
            // 如果在40以内，那就直接返回
            if(num <= 40) return 1 + num % 10;
            // 说明刚才生成的在41-49之间，利用随机数再操作一遍
            num = (num - 40 - 1) * 7 + rand7();
            if(num <= 60) return 1 + num % 10;
            // 说明刚才生成的在61-63之间，利用随机数再操作一遍
            num = (num - 60 - 1) * 7 + rand7();
            if(num <= 20) return 1 + num % 10;
        }
    }
}
```







## 参考：

https://leetcode-cn.com/problems/implement-rand10-using-rand7/solution/xiang-xi-si-lu-ji-you-hua-si-lu-fen-xi-zhu-xing-ji/