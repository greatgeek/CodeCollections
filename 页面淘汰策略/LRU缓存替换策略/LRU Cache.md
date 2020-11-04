# [146. LRU Cache](https://leetcode.com/problems/lru-cache/)

Design and implement a data structure for [Least Recently Used (LRU) cache](https://en.wikipedia.org/wiki/Cache_replacement_policies#LRU). It should support the following operations: `get` and `put`.

`get(key)` - Get the value (will always be positive) of the key if the key exists in the cache, otherwise return -1.
`put(key, value)` - Set or insert the value if the key is not already present. When the cache reached its capacity, it should invalidate the least recently used item before inserting a new item.

The cache is initialized with a **positive** capacity.

**Follow up:**
Could you do both operations in **O(1)** time complexity?

**Example:**

```
LRUCache cache = new LRUCache( 2 /* capacity */ );

cache.put(1, 1);
cache.put(2, 2);
cache.get(1);       // returns 1
cache.put(3, 3);    // evicts key 2
cache.get(2);       // returns -1 (not found)
cache.put(4, 4);    // evicts key 1
cache.get(1);       // returns -1 (not found)
cache.get(3);       // returns 3
cache.get(4);       // returns 4
```

 ## 分析

要求 `get`和`put`操作都要 **O(1) **时间复杂度来完成。`hashMap`天然可以在 **O(1)** 时间复杂度内获取到一个节点。所以使用**双向链表+`HashMap`** 来完成此此题。

## talk is cheap, show me the code

```java
public class LRUCache {
    class CacheNode{
        CacheNode pre;
        CacheNode next;
        int key;
        int value;

        public CacheNode(int key, int value){
            this.key=key;
            this.value=value;
        }
    }

    HashMap<Integer,CacheNode> map;
    CacheNode head,tail;
    int count=0;
    int capacity=0;

    public LRUCache(int capacity) {
        head=new CacheNode(Integer.MIN_VALUE,Integer.MIN_VALUE);
        tail=new CacheNode(Integer.MAX_VALUE,Integer.MAX_VALUE);
        head.next=tail;
        tail.pre=head;
        this.capacity=capacity;

        map=new HashMap<>();
    }

    public int get(int key) {
        if(head.next.key==key){
            return head.next.value;
        }

        CacheNode visNode = map.get(key);

        if(visNode==null) return -1;
        visNode.pre.next=visNode.next;// 从当前链表中断开
        visNode.next.pre=visNode.pre;

        visNode.next=head.next;// 接到头节点的下一个节点
        head.next.pre=visNode;
        head.next=visNode;
        visNode.pre=head;

        return visNode.value;
    }

    public void put(int key, int value) {
        CacheNode node = map.get(key);
        if(node!=null){
            node.value=value;
            get(key);
        }else {
            if(count>=capacity){
                delete();
            }
            CacheNode visNode=new CacheNode(key,value);
            visNode.next=head.next;
            head.next.pre=visNode;
            visNode.pre=head;
            head.next=visNode;
            count++;
            map.put(key,visNode);
        }
    }

    public void delete(){
        CacheNode delNode=tail.pre;
        tail.pre=delNode.pre;
        delNode.pre.next=tail;
        delNode.pre=null;
        delNode.next=null;
        map.remove(delNode.key);
        count--;
    }
}

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */
```



**仅用双向链表版：**

```java
class LRUCache {
    
    class CacheNode{
        CacheNode pre;
        CacheNode next;
        int key;
        int value;
        
        public CacheNode(int key, int value){
            this.key=key;
            this.value=value;
        }
    }
    
    CacheNode head,tail;
    int count=0;
    int capacity=0;

    public LRUCache(int capacity) {
        head=new CacheNode(Integer.MIN_VALUE,Integer.MIN_VALUE);
        tail=new CacheNode(Integer.MAX_VALUE,Integer.MAX_VALUE);
        head.next=tail;
        tail.pre=head;
        this.capacity=capacity;
    }
    
    public int get(int key) {
        if(head.next.key==key){
            return head.next.value;
        }
        CacheNode curNode = head;
        CacheNode visNode = null;
        while(curNode!=tail){
            if(curNode.key==key){
                visNode=curNode;
                break;
            }
            curNode=curNode.next;
        }
        if(visNode==null) return -1;
        visNode.pre.next=visNode.next;
        visNode.next.pre=visNode.pre;

        visNode.next=head.next;
        head.next.pre=visNode;
        head.next=visNode;
        visNode.pre=head;
        
        return visNode.value;
    }
    
    public void put(int key, int value) {
        CacheNode curNode = head;
        CacheNode node = null;
        while (curNode!=tail){
            if(curNode.key==key){
                node=curNode;
                break;
            }
            curNode=curNode.next;
        }
        if(node!=null){
            node.value=value;
            get(key);
        }else {
            if(count>=capacity){
                CacheNode delNode=tail.pre;
                tail.pre=delNode.pre;
                delNode.pre.next=tail;
                delNode.pre=null;
                delNode.next=null;
                count--;
            }
            CacheNode visNode=new CacheNode(key,value);
            visNode.next=head.next;
            head.next.pre=visNode;
            visNode.pre=head;
            head.next=visNode;
            count++;
        }
    }
}

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */
```

