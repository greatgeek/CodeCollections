# CyclicBarrier 用法

回环栅栏，通过它可以实现让一组线程等待至某个状态之后再全部同时执行。叫做回环是因为当所有等待线程都被释放后，CyclicBarrier 可以被重用。当调用 await() 方法后，线程就处于等待 barrier 状态了。

## 构造器

CyclicBarrier 提供2个构造器

```java
CyclicBarrier (int parties) 	
Creates a new CyclicBarrier that will trip(触发) when the given number of parties (threads) are waiting upon it, and does not perform a predefined action when the barrier is tripped.
```

```java
CyclicBarrier (int parties, Runnable barrierAction) 	
Creates a new CyclicBarrier that will trip when the given number of parties (threads) are waiting upon it, and which will execute the given barrier action when the barrier is tripped, performed by the last thread entering the barrier.
```

参数 `parties` 指让多少个线程或者任务等待至 `barrier` 状态；参数 `barrierAction` 为当这些线程都达到 `barrier` 状态时会执行的内容。

## await 方法

CyclicBarrier 中最重要的方法就是 `await` 方法，它有 2 个重载版本：

```java
public int await() throws InterruptedException, BrokenBarrierException { };
```

```java
public int await(long timeout, TimeUnit unit)throws InterruptedException,BrokenBarrierException,TimeoutException { };
```

第一个版本比较常用，用来挂起当前线程，直至所有线程都到达barrier状态再同时执行后续任务；

第二个版本是让这些线程等待至一定的时间，如果还有线程没有到达barrier状态就直接让到达barrier的线程执行后续任务。

## 例子

```java
package multiThread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class MyCyclicBarrier {
    static class RunWriter implements Runnable{
        CyclicBarrier barrier;
        public RunWriter(CyclicBarrier barrier){
            this.barrier=barrier;
        }
        @Override
        public void run() {
            System.out.println("线程"+Thread.currentThread().getName());
            try {
                Thread.sleep(5000); // 以睡眠来模拟写入数据操作
                System.out.println("线程"+Thread.currentThread().getName()+"写入数据");
                barrier.await(); // 在 barrier 处等待
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

            System.out.println("所有线程写入完毕，继续处理其他任务...");
        }
    }

    /**
     * 所有线程都达到 barrier 状态后要执行的任务
     */
    static class AfterRun implements Runnable{

        @Override
        public void run() {
            System.out.println("当前线程"+Thread.currentThread().getName()+"所有线程都达到 barrier 状态后要执行的任务");
        }
    }

    public static void main(String[] args) {
        int N=5;
        CyclicBarrier barrier = new CyclicBarrier(N,new AfterRun());
        for (int i=0;i<N;++i){
            new Thread(new RunWriter(barrier)).start();
        }
    }
}

```

**运行结果：**

```java
线程Thread-0
线程Thread-3
线程Thread-4
线程Thread-2
线程Thread-1
线程Thread-3写入数据
线程Thread-2写入数据
线程Thread-0写入数据
线程Thread-1写入数据
线程Thread-4写入数据
当前线程Thread-4所有线程都达到 barrier 状态后要执行的任务
所有线程写入完毕，继续处理其他任务...
所有线程写入完毕，继续处理其他任务...
所有线程写入完毕，继续处理其他任务...
所有线程写入完毕，继续处理其他任务...
所有线程写入完毕，继续处理其他任务...
```

从结果可以看出，当五个线程都到达 barrier 后，会从四个线程中选择一个线程去执行 CyclicBarrier 的任务。

## 参考：

https://www.cnblogs.com/dolphin0520/p/3920397.html