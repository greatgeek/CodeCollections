# Java电商秒杀系统性能优化(九)——防刷限流技术—保护系统，免于过载

## 概述

本章介绍了常见的黄牛入侵手段，以及如何使用对应的防刷手段防止黄牛入侵。同时业务的发展预估永远可能高于系统可承载的能力，因此介绍了使用多种限流技术保证系统的稳定。

本章学习目标：

- 掌握验证码生成与验证技术；
- 掌握限流原理与实现；
- 掌握防黄牛技术；

## 一、验证码

- 包装秒杀令牌设置，需要验证码来错峰，分散用户的请求；
- 数学公式验证码生成器；

### 1.1 代码实现

```java
创建CodeUtil.java，创建好生成验证码的程序
OrderController.java
***
//生成秒杀令牌前，需要接收验证码
//生成验证码
    @RequestMapping(value = "/generateverifycode",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public void generateverifycode(HttpServletResponse response) throws BusinessException, IOException {
        String token = httpServletRequest.getParameterMap().get("token")[0];
        if(StringUtils.isEmpty(token)){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户还未登陆，不能生成验证码");
        }
        UserModel userModel = (UserModel) redisTemplate.opsForValue().get(token);
        if(userModel == null){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户还未登陆，不能生成验证码");
        }

        Map<String,Object> map = CodeUtil.generateCodeAndPic();

        redisTemplate.opsForValue().set("verify_code_"+userModel.getId(),map.get("code"));
        redisTemplate.expire("verify_code_"+userModel.getId(),10,TimeUnit.MINUTES);

        ImageIO.write((RenderedImage) map.get("codePic"), "jpeg", response.getOutputStream());


    }


    //生成秒杀令牌
    @RequestMapping(value = "/generatetoken",method = {RequestMethod.POST},consumes={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType generatetoken(@RequestParam(name="itemId")Integer itemId,
                                        @RequestParam(name="promoId")Integer promoId,
                                          @RequestParam(name="verifyCode")String verifyCode) throws BusinessException {
     //通过verifycode验证验证码的有效性
        String redisVerifyCode = (String) redisTemplate.opsForValue().get("verify_code_"+userModel.getId());
        if(StringUtils.isEmpty(redisVerifyCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请求非法");
        }
        if(!redisVerifyCode.equalsIgnoreCase(verifyCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请求非法，验证码错误");
        }
     } 
```

## 二、限流的目的

- 流量远比你想象的要多；
- 系统能运行或者总比挂了要好；
- 宁愿让少数人能用，也不要让所有人不能用；

## 三、限流方案

### 3.1 限并发

例如同一时间固定访问接口的线程数，利用全局计数器，当**ServerController**被唤醒某一个需要限制的接口，那我们就将下单接口Controller的入口处加一个全局计数器，并且要支持并发下的减和加的操作，当controller在入口的时候，将计数器减1，判断一下计数器的数字是否大于0，在controller出口的时候将计数器加1，就可以做到同一时间内对计数器的操作是固定的，一旦减到0或者变为负数，就要处理对应的问题；

### 3.2 令牌桶算法

假设有一个桶内放了许多令牌，假设用户要请求对应的实体，需要先获取一个令牌；初始状态下令牌桶内有10个令牌，客户端获取一个令牌，令牌数减一；设置一个定时器，每秒会往令牌桶内放置10个令牌，这样就可以做到客户端一秒可以访问10个对应的流量进去，下一秒就是下一个10个；可以限定某一时刻的最大值，应对突发流量；

### 3.3 漏桶算法原理

有一个桶，初始是满的，有10滴水，每秒流出一滴水；客户端请求的时候是往客户端里面加一滴水；

如果桶是满的这一滴水就加不进去；漏桶算法没有办法应对突发流量，其目的是用来**平滑网络流量**，固定的速度对应的操作。

## 四、限流力度

* **接口维度**
* **总维度**

假设系统有10个接口，分别是商品详情，下单列表、用户登录注册等，假设每个接口都可以承载5tps的流量，对应10个接口就是50tps,那我们的系统真的能承载50tps嘛，一般要比接口维度的总和要小20%左右；

**限流范围**

- 集群限流：依赖Redis或其它中间件技术做统一计数器，往往会产生性能瓶颈；
- 单机限流：负载均衡的前提下单机平均限流效果更好；

**限流代码实现**

```java
OrderController.java
***
   private RateLimiter orderCreateRateLimiter;
   @PostConstruct
    public void init(){
        executorService = Executors.newFixedThreadPool(20);

        orderCreateRateLimiter = RateLimiter.create(300);//tps为300，超过就要对其进行限制

    }
 
         //封装下单请求
    @RequestMapping(value = "/createorder",method = {RequestMethod.POST},consumes={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name="itemId")Integer itemId,
                                        @RequestParam(name="amount")Integer amount,
                                        @RequestParam(name="promoId",required = false)Integer promoId,
                                        @RequestParam(name="promoToken",required = false)String promoToken) throws BusinessException { 
         if(orderCreateRateLimiter.acquire() <= 0){
            throw new BusinessException(EmBusinessError.RATELIMIT);
        }                                
                                          
     }  
 RateLimiter没有实现令牌桶内定时器的功能，
 reserve方法是当前秒的令牌数，如果当前秒内还有令牌就直接返回；
 若没有令牌，需要计算下一秒是否有对应的令牌，有一个下一秒计算的提前量
 使得下一秒请求过来的时候，仍然不需要重复计算
 RateLimiter的设计思想比较超前，没有依赖于人为定时器的方式，而是将整个时间轴
 归一化到一个数组内，看对应的这一秒如果不够了，预支下一秒的令牌数，并且让当前的线程睡眠；
 如果当前线程睡眠成功，下一秒唤醒的时候令牌也会扣掉，程序也实现了限流

```

