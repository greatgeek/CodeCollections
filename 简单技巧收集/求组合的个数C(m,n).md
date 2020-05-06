# 求组合的个数

**计算 C(m,n)**

```java
public static void main(String[] args) {
        // 计算 C (m,n)
        int m=5,n=2;
        int res=1;
        for(int i=1;i<=n;i++){
            res = res*(m-n+i)/i;
        }
        System.out.println(res);
    }
```

**注意：**

若将 ``` res = res*(m-n+i)/i```，写成``` res *=(m-n+i)/i```，会因为计算优先级的不同而导致计算过程中损失精度而得到错误的结果。