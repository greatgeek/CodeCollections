# [Partition List](https://www.lintcode.com/problem/partition-list/description)

**Description**

给定一个单链表和数值x，划分链表使得所有小于x的节点排在大于等于x的节点之前。

你应该保留两部分内链表节点原有的相对顺序。

### Example

**样例  1:**

```
输入: list = null, x = 0
输出: null	
样例解释: 空链表本身满足要求
```

**样例 2:**

```
输入: list = 1->4->3->2->5->2->null, x = 3
输出: 1->2->2->4->3->5->null	
样例解释: 要保持原有的相对顺序。
```

## 分析

有点类似荷兰国旗问题，用一个`small`指针指示，(0,small] 这段范围都是小于数值 x 的，遇到小于 x 的数则，将该节点加入到 `small`的下一个节点，`small`扩张至下一个节点。中心思想是找出小于数值x的节点，将其放到左边，将大于等于x的节点挤到右边。

## 代码

```java
    public static ListNode func(ListNode head,int x){
        ListNode dumb = new ListNode(-1);
        dumb.next=head;
        ListNode small=dumb;
        ListNode p=head,pre=dumb;
        ListNode pnext=null;
        while(p!=null){
            if(p.val<x){
                if(small.next==p){ // 若该节点是 small 的下一个节点，则不必操作，small 扩张即可
                    small=small.next;
                    pre=p;
                    p=p.next;
                }else{ // 若该节点不是 samll 的下一个节点，则需要操作，可以将大于等于 x 的节点挤到右边。
                    pnext=p.next;
                    pre.next=p.next;
                    p.next=small.next;
                    small.next=p;
                    small=small.next;
                    p=pnext;
                }
            }else{ 
                pre=p;
                p=p.next;
            }
        }
        return dumb.next;
    }
```

