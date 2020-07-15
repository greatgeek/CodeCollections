# Floyd 最短路径算法

一个号称只有5行代码的算法，由1978年图灵奖获得者、斯坦福大学计算机科学教授罗伯特.弗洛伊德命名。在计算机科学中，```Floyd-Warshall```算法是一种在具有正或负边权重的加权图中找到最短路径的算法。该算法运用了动态规划的思想，算法的时间复杂度为$O(n^3)$，空间复杂度为$O(n^2)$

其核心思想是，在两个顶点之间插入一个或一个以上的中转点，比较经过与不经过中转点的距离哪个更短。

Floyd的核心5行代码：

```java
 for(k=0;k<n;k++)//中转站0~k
        for(i=0;i<n;i++) //i为起点
            for(j=0;j<n;j++) //j为终点
                if(d[i][j]>d[i][k]+d[k][j])//松弛操作 
                     d[i][j]=d[i][k]+d[k][j]; 
```



## 参考

[Floyd 算法](https://zhuanlan.zhihu.com/p/72248451)

[Floyd-Warshall算法](https://zh.wikipedia.org/wiki/Floyd-Warshall%E7%AE%97%E6%B3%95)