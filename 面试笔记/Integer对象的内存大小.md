# 面试问题收集

## Integer 所占内存大小

### 引用类型的大小

若此时的 Integer 指的是引用类型，则需要知道在 java 中引用类型的内存大小。引用类型的作用是寻址，找到对象的位置，类似与C/C++中的指针。在32位 JVM中，是32 位 bit, 即4个字节，在64位的JVM中，是64位bit，即8个字节。但在64位JVM中，若开启了压缩指针选项：`-XX:+UseCompressedClassPointers`，也是32位 bit，即4个字节。

参考：

https://stackoverflow.com/questions/981073/how-big-is-an-object-reference-in-java-and-precisely-what-information-does-it-co

### Integer 对象所占内存大小

一个对象的内存布局由三部分组成：

```
+==============+
|   Mark word  |
+--------------+   Header
| class pointer|
+==============+
| Instance Data|
+==============+
|   Padding    |
+==============+
```

第一部分是 Header，由 Mark word 和 class pointer 组成。（class pointer 即 reference）。第二部分是 Instance Data。 第三部分是 Padding。

Mark word 在32位和64位虚拟机（未开启压缩指针）中分别为32 bit和64 bit, 即4个字节和8个字节。64位虚拟机开启压缩指针后，其Mark word 也会变为 4个字节。

class pointer 为4个字节。（计算对象的内存大小，这里一律按照即使在 64位虚拟机下也要开启压缩指针来计算）

Instance Data 就为一个 int ，所以是 4个字节。（int 是基础类型，无论是在32位虚拟机还是64位虚拟机下都是4个字节）

Padding ,此处的Padding 需要为4个字节。（**原因：由于HotSpot 虚拟机的自动内存管理系统要求对象起始地址必须是8字节的整数倍，换句话说任何对象的大小都必须是8字节的整数倍。**）

所以一个 Integer 对象的大小为 16个字节。

参考：

[1] https://stackoverflow.com/questions/8419860/integer-vs-int-with-regard-to-memory

[2] https://www.javamex.com/tutorials/memory/object_memory_usage.shtml

[3] 深入理解 Java 虚拟机。

## Integer 源码

由于类中的方法都存在方法区，并不会存在实例对象的堆内存中。只有实例字段才会存在于实例对象的堆内存中，也就是内存布局中的 `Instance Data`，实例数据，所以只需要关心类`Integer`中的字段即可。

查看源代码可以发现，Integer 类只有一个实例字段：

```
    /**
     * The value of the {@code Integer}.
     *
     * @serial
     */
    private final int value;
```

其余都是由 `static`修饰的类字段。类字段不会占用实例对象的内存。

所以实例数据部分只有 `int`类型4个字节。

