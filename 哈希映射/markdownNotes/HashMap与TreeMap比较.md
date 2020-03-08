一、HashMap 与 TreeMap
**HashMap** 是基于哈希表的 **Map** 接口的实现。此实现提供所有可选的映射操作，并允许使用 **null** 值和 **null** 键。此类不保证映射的顺序，特别是它不保证该顺序恒久不变。

**TreeMap** 是基于红黑树（Red-Black tree） 的**NavigableMap** 实现。该映射可以根据其键的自然顺序进行排序，或者根据创建映射时提供的**Comparator** 进行排序，具体取决于使用的构造方法。


二、HashMap和TreeMap比较
1. HashMap 适用于在Map中插入、删除和定位元素；
2. TreeMap 适用于按自然顺序或自定义顺序遍历键（key）；
3. HashMap 通常比 TreeMap 快一点（哈希表和树的数据结构使然），建议多使用 HashMap, 在 Map 需要排序时才使用 TreeMap;
4. HashMap 非线程安全，TreeMap 线程安全；
5. HashMap 的结果是无序的，而 TreeMap 的输出结果是有序的；