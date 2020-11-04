# K 个一组反转，不够K个也要反转



## Show me the code

```java
    public ListNode reverseKGroup(ListNode head, int k) {
      ListNode dummy = new ListNode(-1);
      dummy.next = head;

      ListNode pre = dummy;
      ListNode end = dummy;

      while(end.next!=null){
        for(int i=0;i<k && end!=null; ++i) end = end.next;
        if(end == null) break;
        ListNode start = pre.next;
        ListNode next = end.next;
        end.next=null;
        pre.next = reverse(start);
        start.next=next;
        pre = start;
        end = pre;
      }

      pre.next = reverse(pre.next); // 加了这一行就可以实现最后不够K个一组也会反转
      return dummy.next;
    }

    private ListNode reverse(ListNode head){
      ListNode pre = null;
      ListNode cur = head;
      while(cur!=null){
        ListNode next = cur.next;
        cur.next = pre;
        pre = cur;
        cur=next;
      }
      return pre;
    }
```

