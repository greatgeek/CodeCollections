# Volatile 的两个特性

## 1. volatile 的两个特性

当一个变量被定义成 volatile 之后，它将具备两项特性：第一项是**保证此变量对所有线程的可见性**，这里的“可见性”是指当一条线程修改了这个变量的值，新值对于其他线程来说是可以立即得知的。使用volatile 变量的第二个语义是**禁止指令重排优化**，普通的变量仅会保证在该方法的执行过程中所有依赖值结果的地方都能获取到正确的结果，而不能保证变量赋值操作的顺序与程序代码中的执行顺序一致。

**与 volatile 线程可见性相关的知识有 Java 内存模型（JMM，Java Memory Model）、缓存一致性问题、总线嗅探机制和 MESI 协议**。**与禁止指令重排优化相关的知识有内存屏障、happens-before 关系。**

## 2. 线程可见性

用 volatile 修饰的变量在转变成汇编代码时，会多出一行带有 lock 前缀的指令。这行带有 lock 前缀的指令在多核处理器下会引发两件事情。

1. 将当前处理器的缓存行的数据写回到系统内存。
2. 这个写回内存的操作会使在其他CPU里缓存了该内存地址的数据无效。

这两个操作都需要了解 JMM（Java Memory Model） 的概念，Java内存模型中每个线程都有私有的工作内存，共同享有主内存。与处理器、高速缓存与主内存之间的关系类似。

**将缓存行的数据写回系统内存发生的事：**在以前的处理器中 lock 前缀指令在执行过程中，会锁总线以便处理器可以独占任何共享内存。不过由于锁总线的开销过大，现在一般使用锁缓存而不是锁总线，此时处理器会锁定这块内存区域的缓存，并使用缓存一致机制来确保修改的原子性，也称为“缓存锁定”，缓存一致性机制会阻止同时修改由两个以上处理器缓存的内存区域数据。

**导致其他处理器的缓存无效发生的事：**处理器一般会使用嗅探技术来保证它的内部缓存、系统内存和其他处理器缓存的数据在总线上保持一致。例如 Intel 处理器使用的基于总线嗅探技术的MESI（Modifier/Exclusive/Shared/Invalid）协议。

## 3. 禁止指令重排优化

在 Java 内存模型中，如果一个操作执行的结果需要对另一个操作可见，那么这两个操作之间必须要存在 happens-before 关系，操作可以是在一个线程之内的，也可以是在不同线程之间的，happens-before还具有传递性。

编译器和处理器为了优化程序性能会对指令序列进行重新排序。而指令重排序会造成每个处理器所看到的执行顺序是不一样的，对存在依赖的关系的操作会产生错误的运行结果。此时需要提到 volatile 写-读建立的happens-before关系。为了实现volatile happens-before的内存语义，需要在指令序列中插入内存屏障来禁止特定类型的处理器重排序。**(写前不重排，读后不重排，写读不重排）**

## 参考

Java 并发编程的艺术

深入理解Java虚拟机：JVM高级特性与最佳实践（第3版）

深入浅出计算机组成原理（37讲/38讲）

 [缓存更新的套路](https://coolshell.cn/articles/17416.html)

[与程序员相关的CPU缓存知识](https://coolshell.cn/articles/20793.html#%E7%BC%93%E5%AD%98%E7%9A%84%E4%B8%80%E8%87%B4%E6%80%A7)

[Bus snooping](https://en.wikipedia.org/wiki/Bus_snooping)

[Happened-before](https://en.wikipedia.org/wiki/Happened-before)