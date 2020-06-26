# LRU 缓存替换策略

In computing, **cache algorithms**(also frequently called cache replacement algorithms or cache replacement ploicies) are optimizing instructions, or algorithms, that a computer program or a hardware-maintained structure can utilize in order to manage a cache of information stored on the computer. Caching improves performance by keeping recent or often-used data items in memory locations that are faster or computationally cheaper to access than normal memory stores. When the cache is full, the algorithm must choose which items to discard to make room for the new ones.



## Quick reference

A **Least Recently Used (LRU) Cache** organizes items in order of use, allowing you to quickly identify which item hasn't been used for the longest amount of time.

Picture a clothes rack, where clothes are always hung up on one side. To find the least-recently used item, look at the item on the other end of the rack.

Under the hood, an LRU cache is often implemented by pairing a doubly linked list with a hash map.

### Strengths:

* Super fast accesses. LRU caches store items in order from most-recently used to least-recently used. That means both can be accessed in O(1) time.
* Super fast updates. Each time an item is accessed, updating the cache take O(1) time.

### Weaknesses

* Space heavy. An LRU cache tracking n items requires a linked list of length n , and a hash map holding n items. That's O(n) space, but it's still two data structures(as opposed to one).

## Why Use A Cache?

**A cache is just fast storage.**  Reading data from a cache takes less time than reading it from something else (like a hard disk).

Here's the catch: caches are small. You can't fit everything in a cache, so you're still going to have to use larger, slower storage from to time to time.

## LRU Eviction

An **LRU cache** is an efficient cache data structure that can be used to figure out what we should evict when the cache is full. The goal is to always have the least-recently used item accessible in O(1) time.

## LRU Cache Implementation

- **实现LRU：**

1.用一个数组来存储数据，给每一个数据项标记一个访问时间戳，每次插入新数据项的时候，先把数组中存在的数据项的时间戳自增，并将新数据项的时间戳置为0并插入到数组中。每次访问数组中的数据项的时候，将被访问的数据项的时间戳置为0。当数组空间已满时，将时间戳最大的数据项淘汰。

2.利用一个链表来实现，每次新插入数据的时候将新数据插到链表的头部；每次缓存命中（即数据被访问），则将数据移到链表头部；那么当链表满的时候，就将链表尾部的数据丢弃。

3.利用链表和 hashmap 。当需要插入新的数据项的时候，如果新数据项在链表中存在（一般称为命中），则把该节点移到链表头部，如果不存在，则新建一个节点，放到链表头部，若缓存满了，则把链表最后一个节点删除即可。在访问数据的时候，如果数据项在链表中存在，则把该节点移到链表头部，否则返回-1。这样一来在链表尾部的节点就是最近最久未访问的数据项。

- **比较三种方法优劣：**

对于第一种方法，需要不停地维护数据项的访问时间戳，另外，在插入数据、删除数据以及访问数据时，时间复杂度都是O(n)。对于第二种方法，链表在定位数据的时候时间复杂度为O(n)。所以在一般使用第三种方式来是实现LRU算法。

## talk is cheap , show me the code

* 仅使用双向链表

```java
/**
 * 利用双向链表来实现 LRU 算法，要维护一个链表头（most-recently）和一个链表尾（lest-recently）
 * 每次访问一个节点，若存在于缓存中，则将其移至链表头，若不存在则新建该节点并添加至链表尾
 * 若缓存满了，即链表长度达到上限，则将链表尾的一个节点丢弃
 *
 * 仅使用双向链表来实现，删除与新增节点的时间复杂度都为O（1），但是定位节点的时间复杂度为 O（N）
 * 由于 hashMap 的定位时间复杂度为 O(1)，所以可以使用 双向链表 + hashMap 来进行优化定位
 */

public class LRUCache1 {
    class CacheNode {
        CacheNode pre;
        CacheNode next;
        int value;

        public CacheNode(int value){
            this.value=value;
        }
    }

    CacheNode head,tail;
    int capacity; // 初始化缓存容量
    int count; // 记录已存入多少个节点

    public LRUCache1(int capacity){
        head=new CacheNode(Integer.MIN_VALUE);
        tail=new CacheNode(Integer.MAX_VALUE);
        head.next=tail;
        tail.pre=head;
        count=0;

        this.capacity=capacity;
    }

    /**
     * 模拟访问该缓存块
     * @param value
     * @return
     */
    public CacheNode get(int value){
        if(head.next.value==value){ // 若头节点的第一个元素即为要访问的元素，则可以直接返回，这也是利用了 LRU 的设计思想
            return head.next;
        }
        CacheNode curNode = head;
        CacheNode visNode = null;
        while(curNode!=tail){
            if(curNode.value==value){
                visNode=curNode;
                break;
            }
            curNode=curNode.next;
        }

        if(visNode!=null){ // 若存在该节点，则将其移至头节点
            visNode.pre.next=visNode.next;
            visNode.next.pre=visNode.pre;

            visNode.next=head.next;
            head.next.pre=visNode;
            head.next=visNode;
            visNode.pre=head;
        }else { // 若不存在该节点
            if(count>=capacity){ // 模拟缓存已满，则删除尾部节点
               CacheNode delNode=tail.pre;
               tail.pre=delNode.pre;
               delNode.pre.next=tail;
               delNode.pre=null;
               delNode.next=null;
               count--;
            }
            // 新建该节点，并加入缓存
            visNode=new CacheNode(value);
            visNode.next=head.next;
            head.next.pre=visNode;
            visNode.pre=head;
            head.next=visNode;
            count++;
        }
        return visNode;
    }

    public void printLRUCache(){
        CacheNode curNode = head.next;
        while (curNode!=tail){
            System.out.print(curNode.value+" ");
            curNode=curNode.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        LRUCache1 list = new LRUCache1(5);
        int[] arr = {1,2,3,4,5};
        for(int x:arr){
            list.get(x);
        }
        list.printLRUCache();
        list.get(2);
        list.printLRUCache();
        list.get(1);
        list.printLRUCache();
        list.get(6);
        list.printLRUCache();
        list.get(8);
        list.printLRUCache();
        list.get(4);
        list.printLRUCache();
    }
}

```

* 使用双向链表 + HashMap

```java
import java.util.HashMap;

public class LRUCache2 {
    class CacheNode {
        CacheNode pre;
        CacheNode next;
        int value;

        public CacheNode(int value){
            this.value=value;
        }
    }

    int capacity;
    int count; // 记录已存入多少个节点
    CacheNode head,tail;
    HashMap<Integer,CacheNode> map;

    public LRUCache2(int capacity){
        head= new CacheNode(Integer.MIN_VALUE);
        tail=new CacheNode(Integer.MAX_VALUE);
        head.next=tail;
        tail.pre=head;
        count=0;
        this.capacity=capacity;
        map=new HashMap<>();
    }

    /**
     * 模拟访问该缓存块
     * @param value
     * @return
     */
    public CacheNode get(int value){
        if(head.next.value==value){ // 若头节点的第一个元素即为要访问的元素，则可以直接返回，这也是利用了 LRU 的设计思想
            return head.next;
        }
        CacheNode visNode = map.get(value);

        if(visNode!=null){ // 若存在该节点，则将其移至头节点
            visNode.pre.next=visNode.next;
            visNode.next.pre=visNode.pre;

            visNode.next=head.next;
            head.next.pre=visNode;
            head.next=visNode;
            visNode.pre=head;
        }else { // 若不存在该节点
            if(count>=capacity){ // 模拟缓存已满，则删除尾部节点
                CacheNode delNode=tail.pre;
                tail.pre=delNode.pre;
                delNode.pre.next=tail;
                delNode.pre=null;
                delNode.next=null;

                map.remove(delNode.value); // 删除时，也把 hashMap 中的记录给删除
                count--;
            }
            // 新建该节点，并加入缓存
            visNode=new CacheNode(value);
            visNode.next=head.next;
            head.next.pre=visNode;
            visNode.pre=head;
            head.next=visNode;
            count++;
            map.put(value,visNode);
        }

        return visNode;
    }

    public void printLRUCache(){
        CacheNode curNode = head.next;
        while (curNode!=tail){
            System.out.print(curNode.value+" ");
            curNode=curNode.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        LRUCache2 list = new LRUCache2(5);
        int[] arr = {1,2,3,4,5};
        for(int x:arr){
            list.get(x);
        }
        list.printLRUCache();
        list.get(2);
        list.printLRUCache();
        list.get(1);
        list.printLRUCache();
        list.get(6);
        list.printLRUCache();
        list.get(8);
        list.printLRUCache();
        list.get(5);
        list.printLRUCache();
        list.get(6);
        list.printLRUCache();
    }

}

```



## reference

[LRU Cache](https://www.interviewcake.com/concept/java/lru-cache) 

[LRU算法的Java实现](https://www.cnblogs.com/zlting/p/10775887.html)