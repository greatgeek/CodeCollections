# Redis和Memcache 的区别

## 集群

redis 和 memcache 都支持集群.

## 数据类型

Redis 支持的数据类型丰富的多, Redis 不仅支持简单的K/V类型的数据,同时还提供String, List, Set, Hash, Sorted Set, pub/sub, Transactions 数据结构的存储. 其中 Set 是HashMap实现的, value 永远为 null .

memcache 支持简单数据类型,需要客户端自己处理复杂对象.

## 持久性

Redis 支持数据持久化存储, 可以将内存中的数据保持在磁盘中,重启时可以再次加载进行使用.

memcache 不支持数据持久存储.

## 分布式存储

Redis 支持 master-slave 复制模式

memcache 可以使用一致性 hash 做分布式.

## value 大小不同

memcache 是一个内存缓存, key 的长度小于 256字符, 单个item 存储要小于 1M , 不适合虚拟机使用.

## 数据一致性不同

Redis 只使用单核, 而 memcache 可以使用多核,所以平均每一个核上 Redis 在存储小数据时比 memcached 性能更高.

Redis 使用的是单线程模型, 保证了数据按顺序提交.

memcache 需要使用 CAS 保证数据一致性. CAS 是一个确保并发一致性的机制,属于"乐观锁"范畴; 原理很简单: 拿版本号,操作,对比版本号,如果一致就操作, 不一致就放弃任何操作.

## CPU利用

Redis 单线程模型只能使用一个CPU,可以开启多个Redis 进程.