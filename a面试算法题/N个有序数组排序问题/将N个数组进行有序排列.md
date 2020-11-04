# 将 N 个数组进行有序排列

```java
    static class Item{
        int val;
        int rowId;
        int index;
        public Item(int val,int row,int index){
            this.val=val;
            this.rowId =row;
            this.index=index;
        }
    }
    
    public int[] sort(int[][] A){
        int rowNum=A.length;
        int colNum = A[0].length;
        Map<Integer,Integer> map = new HashMap<>(); // [rowId, index]
        for(int i=0;i<rowNum;++i){
            map.put(i,0);
        }

        int[] res = new int[colNum*rowNum];

        PriorityQueue<Item> minHeap = new PriorityQueue<>((a,b)->(a.val-b.val));
        for(int i=0;i<rowNum;++i){
            if(map.get(i)>=A[i].length) continue;
            minHeap.add(new Item(A[i][map.get(i)],i,map.get(i)));
        }

        int index=0;
        while(index < colNum*rowNum){
            Item item = minHeap.poll();
            map.put(item.rowId,map.get(item.rowId)+1); // 将对应行的指针向后移一位

            if(map.get(item.rowId)<A[item.rowId].length)
                minHeap.add(new Item(A[item.rowId][map.get(item.rowId)],item.rowId,map.get(item.rowId)));
            res[index++]=item.val;
        }
        return res;
    }
```

