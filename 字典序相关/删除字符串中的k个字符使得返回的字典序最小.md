# 删除字符串中的k个字符使得返回的字典序最小

题目说明：

给定一个字符串**M**，删除其中的**k**个字符返回，使得返回的字符串的字典序最小。

输入说明：
```
第一行输入是字符串 M
```
```
第二行输入是数字 k
```
输出说明：
```
输出一个字符串
```
例：

输入：
```
bacaa

1
```
输出：
```
 acaa
```
## 解题过程

要想返回的字典序最小，则删除字符过程中必须保留的字符从左到右都尽量小。**如果将字符的ASCII码比作山峰的高度，则字典序就是递增的山峰，在字典序中不会出现山峰的起伏。要想输出的字典序最小，所以山峰只能递增，而不能起伏。**

### 思路

想象一个这样的场景，你从左向右走在这个字符串的ASCII的山峰上，若是你只走平路或递增，则这个字符串就已经是最小字典序了。若是你走得起起伏伏，则每一次删除就删除遇到的第一个山峰，删除之后就会消除一个起伏，就会离最小字典序近一步。其实删除过程就是从左到右消除起伏的过程。

以```bacaa```举例

```java
  	bacaa

   |    
 | |
 |||||
```

从左向右走，遇到的```b```就是第一个山峰。需要删除，此时就会变成下图这样

```java
   |    
   |
  ||||
```

消除了一个起伏，在删除一个字符的情况下，这是最小字典序。

## 代码

```java
    public static String func(String M,int k){
        String str=M;
        for(int i=0;i<k;i++){
            str=proces(str);
        }
        return str;
    }

    public static String proces(String str){
        int N = str.length();
        char[] aa = str.toCharArray();
        boolean isIncre = false;
        String res = "";
        for(int i=0;i<N-1;i++){ // 可以判断出起伏和递减的情况. 起伏： abac, 递减： dcba。这两种情况，删除遇到的第一个峰
            if(aa[i]>aa[i+1]){
                res = str.substring(0,i)+str.substring(i+1,N);
                isIncre=true;
                break;
            }
        }
        if(!isIncre) return str.substring(0,N-1); // 若是递增的情况 : abcd ，这种情况，删除最后一个峰
        return res;
    }
```

