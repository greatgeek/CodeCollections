# CountDownLatch 用法

倒计数门闩，利用它可以实现类似计数器的功能。比如有一个任务 A，它要等待其他 4 个任务执行完毕之后才能执行，此时就可以利用 `CountDownLatch` 来实现这功能。

## 构造器

```java
CountDownLatch (int count) 	
Constructs a CountDownLatch initialized with the given count.
```

## await

```java
void 	await() 	
Causes the current thread to wait until the latch has counted down to zero, unless the thread is interrupted.
```
调用await()方法的线程会被挂起，它会等待直到count值为0才继续执行。

```java
boolean 	await (long timeout, TimeUnit unit) 	
Causes the current thread to wait until the latch has counted down to zero, unless the thread is interrupted, or the specified waiting time elapses.
```
和await()类似，只不过等待一定的时间后count值还没变为0的话就会继续执行。

## countDown

```java
void 	countDown() 	
Decrements the count of the latch, releasing all waiting threads if the count reaches zero.
```

将 count 值减 1。

## 例子

```java
package multiThread;

import java.util.concurrent.CountDownLatch;

public class MyCountDownLatch {
    static class ARunner implements Runnable{
        CountDownLatch latch;
        public ARunner(CountDownLatch latch){
            this.latch=latch;
        }
        @Override
        public void run() {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("等待B系统任务执行完成，可以执行A任务");
        }
    }

    static class BRunner implements Runnable{
        CountDownLatch latch;
        public BRunner(CountDownLatch latch){
            this.latch=latch;
        }
        @Override
        public void run() {
            System.out.println("B任务"+Thread.currentThread().getName()+"正在执行");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
        }
    }

    public static void main(String[] args) {
        int N=4;
        CountDownLatch latch = new CountDownLatch(N);
        new Thread(new ARunner(latch)).start();
        for (int i=0;i<N;++i){
            new Thread(new BRunner(latch)).start();
        }
    }
}

```

运行结果：

```java
B任务Thread-1正在执行
B任务Thread-4正在执行
B任务Thread-3正在执行
B任务Thread-2正在执行
等待B系统任务执行完成，可以执行A任务
```




## 参考

https://www.cnblogs.com/dolphin0520/p/3920397.html