# Redis 避免缓存穿透与 BloomFilter

## Bloom Filter 概念

布隆过滤器（Bloom Filter）是1970年由一个叫布隆的小伙子提出的。它实际上是一个很长的二进制向量和一系列随机映射函数。布隆过滤器可以用于检索一个元素是否在一个集合中。它的优点是空间效率和查询时间都远远超过一般的算法，缺点是有一定的误识别率和删除困难。

## Bloom Filter 原理

布隆过滤器的原理是，当一个元素被加入集合时，通过K个散列函数将这个元素映射成一个位数组中的K个点，把它们置为1。检索时，我们只要看看这些点是不是都是1就（大约）知道集合中有没有它了：如果这些点有任何一个0，则被检元素一定不在；如果都是1，则被检元素很可能在。这就是布隆过滤器的基本思想。

Bloom Filter跟单哈希函数Bit-Map不同之处在于：Bloom Filter使用了k个哈希函数，每个字符串跟k个bit对应。从而降低了冲突的概率。

![img](https://user-gold-cdn.xitu.io/2019/10/28/16e112fbd031fe71?imageView2/0/w/1280/h/960/ignore-error/1)

## 缓存穿透

![img](https://user-gold-cdn.xitu.io/2019/10/30/16e1b6d07e758c4a?imageView2/0/w/1280/h/960/ignore-error/1)

那应用的场景在哪里呢？一般我们都会用来防止缓存击穿。在缓存里加上布隆过滤噐，若布隆过滤器不存在这个数据则说明数据库中没有这个数据，则直接返回。

## Bloom Filter 的缺点

bloom filter之所以能做到在时间和空间上的效率比较高，是因为牺牲了判断的准确率、删除的便利性

- 存在误判，可能要查到的元素并没有在容器中，但是hash之后得到的k个位置上值都是1。如果bloom filter中存储的是黑名单，那么可以通过建立一个白名单来存储可能会误判的元素。
- 删除困难。一个放入容器的元素映射到bit数组的k个位置上是1，删除的时候不能简单的直接置为0，可能会影响其他元素的判断。可以采用[Counting Bloom Filter](http://wiki.corp.qunar.com/confluence/download/attachments/199003276/US9740797.pdf?version=1&modificationDate=1526538500000&api=v2)

## 简易 Bloom Filter 

```java

public class BloomFilter {
    private byte[] data;

    public BloomFilter(int initSize){
        this.data = new byte[initSize * 2]; // 默认创建大小 * 2的空间
    }

    public void add(int key){
        int location1 = Math.abs(hash1(key)%data.length);
        int location2 = Math.abs(hash2(key)%data.length);
        int location3 = Math.abs(hash3(key)%data.length);

        data[location1]=data[location2]=data[location3]=1;
    }

    public boolean contains(int key){
        int location1 = Math.abs(hash1(key)%data.length);
        int location2 = Math.abs(hash2(key)%data.length);
        int location3 = Math.abs(hash3(key)%data.length);

        return data[location1]*data[location2]*data[location3]==1;
    }

    private int hash1(Integer key){
        return key.hashCode();
    }

    private int hash2(Integer key){
        int hashcode = key.hashCode();
        return hashcode ^ (hashcode>>>3);
    }

    private int hash3(Integer key){
        int hashcode = key.hashCode();
        return hashcode ^ (hashcode>>>16);
    }

    public static void main(String[] args) {
        BloomFilter bloomFilter = new BloomFilter(1500);
        int[] arr1 = {2,3,5,7,8,9};
        int[] arr2 = {3,6,9,100,7};
        for(int i=0;i<arr1.length;++i){
            bloomFilter.add(arr1[i]);
        }

        for(int i=0;i<arr2.length;++i){
            if(bloomFilter.contains(arr2[i])){
                System.out.print(arr2[i]+" ");
            }
        }

    }
}
```



## 参考

https://juejin.im/post/5db69365518825645656c0de