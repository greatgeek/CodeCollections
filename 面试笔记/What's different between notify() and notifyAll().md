# What's difference between notify() and notifyAll() in Java

## **Differences between notify() and notifyAll()**

1. **Notification to number of threads:** We can use notify() method to give the notification **for only one thread** which is waiting for a particular object whereas by the help of notifyAll() methods we can give the notification to **all waiting threads** of a particular object.
2. **Notifying a thread by JVM :** If multiple threads are  waiting for the notification and we use notify() method then only one  thread get the notification and the remaining thread have to wait for  further notification. Which thread will get the notification we can’t  expect because it totally depends upon the JVM. But when we use  notifyAll() method then multiple threads got the notification but  execution of threads will be performed one by one because thread  requires lock and only one lock is available for one object.
3. **Interchangeability of threads :** We should go for  notify() if all your waiting threads are interchangeable (the order they wake up doesn’t matter). A common example is a thread pool. But we  should use notifyAll() for other cases where the waiting threads may  have different purposes and should be able to run concurrently. An  example is a maintenance operation on a shared resource, where multiple  threads are waiting for the operation to complete before accessing the  resource.

## **When to use notify() method and notifyAll()**

- In case of mutually exclusive locking, only one of the waiting  threads can do something useful after being notified (in this case  acquire the lock). In such a case, you would rather use notify().  Properly implemented, you could use notifyAll() in this situation as  well, but you would unnecessarily wake threads that can’t do anything  anyway.
- In some cases, all waiting threads can take useful action once the  wait finishes. An example would be a set of threads waiting for a  certain task to finish; once the task has finished, all waiting threads  can continue with their business. In such a case you would use  notifyAll() to wake up all waiting threads at the same time.

## **Applications of notify() and notifyAll()**

- A maintenance operation on a shared resource, where multiple threads are waiting for the operation to complete before accessing the  resource; for these we should go for notifyAll().
- Let’s say we have a producer thread and a consumer thread. Each  “packet” produced by the producer should be consumed by a consumer. The  consumer puts something in a queue and then calls notify().
- We want to have a notification when a lengthy process has finished.  You want a beep and a screen update. The process performs notifyAll() to notify both the beeping-thread and the screen-update-thread.



## References

[Difference between notify() and notifyAll() in Java](https://www.geeksforgeeks.org/difference-notify-notifyall-java/)

https://stackoverflow.com/questions/37026/java-notify-vs-notifyall-all-over-again