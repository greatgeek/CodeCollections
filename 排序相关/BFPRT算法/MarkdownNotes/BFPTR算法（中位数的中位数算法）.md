通常需要在一大堆数中求前K大的数，比如在搜索引擎中求当天用户点击次数排名前1000的热词，这些都涉及一个核心问题，即**TOP-K**问题。

求TOP-K问题最简单的方式为快速排序后取前K大的数即可。但是这样做有两个问题：
1. 快速排序的平均时间复杂度为O(nlog(n)),但最坏时间复杂度为O(n^2).
2. 由于只需要前K大的数，而对其余不需要的数也进行了排序，浪费了大量的排序时间。而堆排序也是一个较好的方法，维护一个大小为K的堆，时间复杂度为O(nlog(k))。

## BFPRT 算法（中位数的中位数算法）
它是由Blum,Floyd,Pratt,Rivest,Tarjan提出。该算法的思想是修改快速选择算法的主元选取方法，提高算法在最坏情况下的时间复杂度。

## 一、快速排序原理
一趟快速排序的过程如下：
1. 先从序列中选取一个数作为基准数；（经典快排将最末尾的数作为基准）
2. 将比这个数大的数全部放到它的右边，把小于或者等于它的数全部放到它的左边；

一趟快速排序也叫**Partion**，即将序列划分为两个部分，一部分比基准数小，另一部分比基准数大，然后再进行分治，每一次Partion不一定都能保证划分得很均匀，所以最坏情况下的时间复杂度不能保证总是为O(nlog(n))。对于Partion过程，通过有两种方法：
1. 两个指针从首尾向中间扫描（双向扫描）

```C++
#include <iostream>
#include <string.h>
#include <stdio.h>
#include <algorithm>
#include <time.h>
 
using namespace std;
const int N = 10005;
 
int Partion(int a[], int l, int r)
{
    int i = l;
    int j = r;
    int pivot = a[l];
    while(i < j)
    {
        while(a[j] >= pivot && i < j)
            j--;
        a[i] = a[j];
        while(a[i] <= pivot && i < j)
            i++;
        a[j] = a[i];
    }
    a[i] = pivot;
    return i;
}
 
void QuickSort(int a[], int l, int r)
{
    if(l < r)
    {
        int k = Partion(a, l, r);
        QuickSort(a, l, k - 1);
        QuickSort(a, k + 1, r);
    }
}
 
int a[N];
 
int main()
{
    int n;
    while(cin >> n)
    {
        for(int i = 0; i < n; i++)
            cin >> a[i];
        QuickSort(a, 0, n - 1);
        for(int i = 0; i < n; i++)
            cout << a[i] << " ";
        cout << endl;
    }
    return 0;
}
```

2. 两个指针一前一后逐步向前扫描（单向扫描）

```C++
#include <iostream>
#include <string.h>
#include <stdio.h>
 
using namespace std;
const int N = 10005;
 
int Partion(int a[], int l, int r)
{
    int i = l - 1;
    int pivot = a[r];
    for(int j = l; j < r; j++)
    {
        if(a[j] <= pivot)
        {
            i++;
            swap(a[i], a[j]);
        }
    }
    swap(a[i + 1], a[r]);
    return i + 1;
}
 
void QuickSort(int a[], int l, int r)
{
    if(l < r)
    {
        int k = Partion(a, l, r);
        QuickSort(a, l, k - 1);
        QuickSort(a, k + 1, r);
    }
}
 
int a[N];
 
int main()
{
    int n;
    while(cin >> n)
    {
        for(int i = 0; i < n; i++)
            cin >> a[i];
        QuickSort(a, 0, n - 1);
        for(int i = 0; i < n; i++)
            cout << a[i] << " ";
        cout << endl;
    }
    return 0;
}
```
基于双向扫描的快速排序要比基于单向扫描的快速排序算法快很多。

## 二、 BFPRT 算法原理
在BFPRT算法中，仅仅是改变了快速排序Partion中的pivot值的选取，在快速排序中，我们始终选择第一个或最后一个元素作为pivot，而在BFPRT算法中，每次选择五分中位数的中位数作为pivot，这样做的目的就是使得划分比较合理，从而避免了最坏情况的发生。算法步骤如下：
1. 将n个元素划为$\lfloor n/5 \rfloor$组，每组5个，至多只有一组由n mod 5 个元素组成。
2. 寻找这$\lceil n/5 \rceil$个组中每一个组的中位数，这个过程可以用插入排序。
3. 对步骤2中的$\lfloor n/5 \rfloor$个中位数进行排序，再找出中位数。
4. 这个中位数即为pivot，把大于它的数全放左边，小于等于它的数全部放右边。
5. 判断pivot的位置与k的大小，有选择的对左边或右边递归。

求第K大就是求第n-k+1小，这两者是等价的。

```C++
#include <iostream>
#include <string.h>
#include <stdio.h>
#include <time.h>
#include <algorithm>
 
using namespace std;
const int N = 10005;
 
int a[N];
 
//插入排序
void InsertSort(int a[], int l, int r)
{
    for(int i = l + 1; i <= r; i++)
    {
        if(a[i - 1] > a[i])
        {
            int t = a[i];
            int j = i;
            while(j > l && a[j - 1] > t)
            {
                a[j] = a[j - 1];
                j--;
            }
            a[j] = t;
        }
    }
}
 
//寻找中位数的中位数
int FindMid(int a[], int l, int r)
{
    if(l == r) return l;
    int i = 0;
    int n = 0;
    for(i = l; i < r - 5; i += 5)
    {
        InsertSort(a, i, i + 4);
        n = i - l;
        swap(a[l + n / 5], a[i + 2]);
    }
 
    //处理剩余元素
    int num = r - i + 1;
    if(num > 0)
    {
        InsertSort(a, i, i + num - 1);
        n = i - l;
        swap(a[l + n / 5], a[i + num / 2]);
    }
    n /= 5;
    if(n == l) return l;
    return FindMid(a, l, l + n);
}
 
//进行划分过程
int Partion(int a[], int l, int r, int p)
{
    swap(a[p], a[l]);
    int i = l;
    int j = r;
    int pivot = a[l];
    while(i < j)
    {
        while(a[j] >= pivot && i < j)
            j--;
        a[i] = a[j];
        while(a[i] <= pivot && i < j)
            i++;
        a[j] = a[i];
    }
    a[i] = pivot;
    return i;
}
 
int BFPRT(int a[], int l, int r, int k)
{
    int p = FindMid(a, l, r);    //寻找中位数的中位数
    int i = Partion(a, l, r, p);
 
    int m = i - l + 1;
    if(m == k) return a[i];
    if(m > k)  return BFPRT(a, l, i - 1, k);
    return BFPRT(a, i + 1, r, k - m);
}
 
int main()
{
    int n, k;
    scanf("%d", &n);
    for(int i = 0; i < n; i++)
        scanf("%d", &a[i]);
    scanf("%d", &k);
    printf("The %d th number is : %d\n", k, BFPRT(a, 0, n - 1, k));
    for(int i = 0; i < n; i++)
        printf("%d ", a[i]);
    puts("");
    return 0;
}
 
/**
10
72 6 57 88 60 42 83 73 48 85
5
*/
```
