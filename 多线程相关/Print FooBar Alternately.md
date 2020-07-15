# 1115. [Print FooBar Alternately](https://leetcode.com/problems/print-foobar-alternately/)

Suppose you are given the following code:

```
class FooBar {
  public void foo() {
    for (int i = 0; i < n; i++) {
      print("foo");
    }
  }

  public void bar() {
    for (int i = 0; i < n; i++) {
      print("bar");
    }
  }
}
```

The same instance of `FooBar` will be passed to two different threads. Thread A will call `foo()` while thread B will call `bar()`. Modify the given program to output "foobar" *n* times.

 

**Example 1:**

```
Input: n = 1
Output: "foobar"
Explanation: There are two threads being fired asynchronously. One of them calls foo(), while the other calls bar(). "foobar" is being output 1 time.
```

**Example 2:**

```
Input: n = 2
Output: "foobarfoobar"
Explanation: "foobar" is being output 2 times.
```

## Solution

### Approach#1 : using semaphore

```java
class FooBar {
    
    Semaphore fooS=new Semaphore(1);
    Semaphore barS=new Semaphore(0);
    
    private int n;

    public FooBar(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        
        for (int i = 0; i < n; i++) {
            fooS.acquire();
        	// printFoo.run() outputs "foo". Do not change or remove this line.
        	printFoo.run();
            barS.release();
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        
        for (int i = 0; i < n; i++) {
            barS.acquire();
            // printBar.run() outputs "bar". Do not change or remove this line.
        	printBar.run();
            fooS.release();
        }
    }
}
```

### Approach#2 using synchronized(wait and notify all)

```java
class FooBar {
    private int flag=0;
        
    private int n;

    public FooBar(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        
        for (int i = 0; i < n; i++) {
            synchronized(this){
                while(flag==1){
                    this.wait();
                }
        	    // printFoo.run() outputs "foo". Do not change or remove this line.
        	    printFoo.run();
                flag=1;
                this.notifyAll();
            }
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        
        for (int i = 0; i < n; i++) {
            synchronized(this){
                while(flag==0){
                    this.wait();
                }
            // printBar.run() outputs "bar". Do not change or remove this line.
        	printBar.run();
                flag=0;
                this.notifyAll();
           } 
        }
    }
}
```

### Approach#3 using Lock

```java
class FooBar {
    private ReentrantLock lock=new ReentrantLock();
    private Condition fooCondition=lock.newCondition();
    private Condition barCondition=lock.newCondition();
        
    private int flag=0;
    private int n;

    public FooBar(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        
        for (int i = 0; i < n; i++) {
            lock.lock();
            try{
                while(flag==1){
                    fooCondition.await();
                }
        	    // printFoo.run() outputs "foo". Do not change or remove this line.
        	    printFoo.run();
                flag=1;
                barCondition.signal();
            }finally{
                lock.unlock();
            }
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        
        for (int i = 0; i < n; i++) {
            lock.lock();
            try{
                while(flag==0){
                    barCondition.await();
                }
            // printBar.run() outputs "bar". Do not change or remove this line.
        	printBar.run();
                flag=0;
                fooCondition.signal();
           }finally{
                lock.unlock();
            }
        }
    }
}
```

### Approach#4 using volatile

```java
class FooBar {        
    private volatile int flag=0;
    private int n;

    public FooBar(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        
        for (int i = 0; i < n; i++) {
                while(flag==1);
        	    // printFoo.run() outputs "foo". Do not change or remove this line.
        	    printFoo.run();
                flag=1;
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        
        for (int i = 0; i < n; i++) {
                while(flag==0);
                // printBar.run() outputs "bar". Do not change or remove this line.
        	    printBar.run();
                flag=0;
        }
    }
}
```
