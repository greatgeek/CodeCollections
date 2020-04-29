# N 皇后问题

## 题目描述

  请设计一种算法，解决著名的n皇后问题。这里的n皇后问题指在一个nxn的棋盘上放置n个棋子，使得每行每列和每条对角线上都只有一个棋子，求其摆放的方法数。 

  给定一个int **n**，请返回方法数，保证n小于等于15 

  测试样例： 

```
1
```
```
返回：1
```

# 解题过程

判断两点是否在同一对角线，```(x1,y1)```,```(x2,y2)```。

若 

```java
Math.abs(x1-x2)==Math.abs(y1-y2)
```

则表示在同一对角线。

## 代码

```java
import java.util.*;

public class Queens {
    public int nQueens(int n) {
        int[] row = new int[n];
        return process(row,n,0);
    }

    public int process(int[] row,int n, int rowNum){
        int sum = 0;
        if(rowNum==n){
            return 1;
        }
        for(int j=0;j<n;j++){
            if(isValid(row,rowNum,j)){
                row[rowNum]=j;
                sum +=process(row,n,rowNum+1);
            }
        }
        return sum;
    }

    public boolean isValid(int[] row,int x,int y){
        for(int rowNum=0;rowNum<x;rowNum++){
            int col = row[rowNum];
            if(col==y) return false;
            if(Math.abs(rowNum-x)==Math.abs(col-y)) return false;
        }
        return true;
    }
}
```

