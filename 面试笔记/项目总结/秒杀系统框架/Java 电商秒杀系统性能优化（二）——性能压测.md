# Java 电商秒杀系统性能优化（二）——性能压测

## jemeter 性能压测

采用jmeter进性能压测， KeepAlive采用长连接,向服务端发送请求之后，希望服务端不要立刻断开连接，而是等待复用连接；

- 线程组（组装线程池） 	
   启动多个线程，并发测试服务端的压力；
   线程组参数： 线程数：100； Ramp-Up（倾斜提升） 时间:10；循环次数:10
- Http请求 右键线程组添加HTTP请求，协议http
- 查看结果树,右键监听器，查看结果树;
- 聚合报告；

## 发现容量问题

采用 `pstree -p 进程号 | wc -l` 查看线程数；采用 `top -H`查看机器性能；

## Tomcat 性能优化

### Tomcat  简介

Tomcat是一个轻量级应用服务器，是支持**运行 Servlet/JSP 应用程序的容器**，运行在 jvm 上，绑定 IP 地址并监听TCP端口；运行时占用的系统资源小，扩展性好，支持负载平衡与邮件服务等开发应用系统常用的功能；

在做web项目时多数需要http协议，基于请求和响应，**servlet就是在服务器端运行的java程序**，通过配置文件拦截（**intercept**）你的请求，并进行相应处理，然后展示给你相应界面；Tomcat就可以创建servlet，去运行web项目，相当于一个应用服务器，作为servlet的容器；

### Tomcat 的调用过程

* tomcat要能够同时接收多个客户端发来的请求，那么就需要多线程；
* tomcat想要调用servlet就是得到这个servlet对象和类所在地址的映射关系；
* tomcat本身并不知道客户端会访问哪一个servlet，所以tomcat必须要能够动态的去调用servlet对象，那么就需要用到java的反射机制；
* 定位到具体的servlet并调用get或post方法并响应客户端；
  

### Tomcat 默认内嵌配置

打开Springboot项目中，双击shift,搜索spring-configuration-metadata.json文件，可以查看各个节点的配置；

tomcat默认参数是为开发环境制定，而非适合生产环境，尤其是内存和线程的配置，默认都很低，容易成为性能瓶颈；

* server.tomcat.accept-count:等待队列长度。默认100；**（线程池中的阻塞队列）**
* server.tomcat.max-connections:最大可被连接数，默认10000；
* server.tomcat.max-threads:最大工作线程数，默认200；（**线程池中的核心线程+非核心线程）**
* server.tomcat.min-spare-threads:最小线程数，默认10；
* 默认配置下，连接超过10000后出现拒绝连接情况；
* 默认配置下，触发的请求超过200+100后拒绝处理；（由线程池中的参数决定）

#### 定制内嵌 Tomcat 开发

* **keepAliveTimeOut**:多少毫秒后不响应的断开keepalive(设置在服务端上)；
* **maxKeepAliveRequests**:多少次请求后keepalive断开失效；
* 使用WebServerFactoryCustomizer<**ConfigurableServletWebServerFactory**>定制化内嵌tomcat配置

### Tomcat 调优

#### 1. 优化连接配置；**这里以tomcat7的参数配置**为例，需要修改conf/server.xml文件，修改连接数，关闭客户端dns查询
```xml
<Connector port="8080"
protocol="org.apache.coyote.http11.Http11NioProtocol"
connectionTimeout="20000"
redirectPort="8443"
maxThreads="500"
minSpareThreads="20"
acceptCount="100"
disableUploadTimeout="true"
enableLookups="false"
URIEncoding="UTF-8" />
```

#### 2. 利用缓存和压缩

对于静态页面最好是能够缓存起来，这样就不必每次从磁盘上读。这里我们采用了 Nginx 作为缓存服务器，将图片、css、js 文件都进行了缓存，有效的减少了后端 tomcat 的访问。

可以用 **gzip 压缩**，其实很多图片也可以用图像处理工具预先进行压缩，找到一个平衡点可以让画质损失很小而文件可以减小很多;

##### 内存调优: 设置 JVM 的一些参数

-Xmx3550m -最大可用内存；
-Xms3550m -JVM促使内存为3550m；
-Xmn2g 年轻代大小为2G；
-Xss128k -设置每个线程的堆栈大小；
-XX:NewRatio=4:设置年轻代（包括Eden和两个Survivor区）与年老代的比值（除去持久代）设置为4，则年轻代与年老代所占比值为1：4；
-XX:SurvivorRatio=4：设置年轻代中Eden区与Survivor区的大小比值。设置为4，则两个Survivor区与一个Eden区的比值为2:4；
-XX:MaxPermSize=16m:设置持久代大小为16m；
-XX:MaxTenuringThreshold=0：设置垃圾最大年龄；

## 发现容量问题

响应时间长，TPS也上不去；

* **单个Web容器有上限；**
  线程数量：4核cpu 8G内存单进程调度线程数800-1000以上后即花费巨大的时间在cpu调度上；等待队列长度：队列做缓冲池用，但也不能无限长，消耗内存，出队入队也耗cpu；
* **Mysql数据库QPS容量问题；**
  主键查询：千万级别数据 1-10毫秒
  唯一索引查询：千万级别数据=10-100毫秒
  非唯一索引查询：千万级别数据=100-1000毫秒
  无索引：百万条数据=1000毫秒+
* **Mysql数据库TPS容量问题	；**