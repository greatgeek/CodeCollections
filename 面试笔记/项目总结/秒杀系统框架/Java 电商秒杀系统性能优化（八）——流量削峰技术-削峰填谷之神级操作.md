# Java电商秒杀系统性能优化(八)——流量削峰技术-削峰填谷之神级操作

## 概述

在之前的课程中经历了查询的优化技术，将单机查询效率提升到了4000 QPS
 对应的交易优化技术使用了缓存校验+异步扣减库存的方式，使得秒杀下单的方式有了明显的提升。

即便查询优化，交易优化技术用到极致后，只要外部的流量超过了系统可承载的范围就有拖垮系统的风险。本章通过**秒杀令牌**，**秒杀大闸**，**队列泄洪**等流量削峰技术解决全站的流量高性能运行效率。

项目缺陷：

* 秒杀下单接口会被脚本不停的刷新；
* 秒杀验证逻辑和秒杀下单接口强关联，代码冗余度高；
* 秒杀验证逻辑复杂，对交易系统产生无关联负载；

本章目标：

- **掌握秒杀令牌的原理和使用方式;**
- **掌握秒杀大闸的原理和使用方式;**
- **掌握队列泄洪的原理和使用方式.**

## 一、秒杀令牌

### 1.1 原理

* **秒杀接口需要依靠令牌才能进入**，对应的秒杀下单接口需要新增一个入参，表示对应前端用户获得传入的一个令牌，只有令牌处于合法之后，才能进入对应的秒杀下单的逻辑；
* **秒杀令牌由秒杀活动模块负责生成**，交易系统仅仅验证令牌的可靠性，以此来判断对应的秒杀接口是否可以被这次http的request进入；
* 秒杀活动模块对秒杀令牌生成全权处理，逻辑收口；
* 秒杀下单前需要获得秒杀令牌才能开始秒杀；

### 1.2 代码实现

```java
PromoService.java
***
//生成秒杀用的令牌
String generateSecondKillToken(Integer promoId,Integer itemId,Integer userId);
***
PromoServiceImpl.java
***
@Override
    public String generateSecondKillToken(Integer promoId,Integer itemId,Integer userId) {

        //判断是否库存已售罄，若对应的售罄key存在，则直接返回下单失败
        if(redisTemplate.hasKey("promo_item_stock_invalid_"+itemId)){
            return null;
        }
        PromoDO promoDO = promoDOMapper.selectByPrimaryKey(promoId);

        //dataobject->model
        PromoModel promoModel = convertFromDataObject(promoDO);
        if(promoModel == null){
            return null;
        }

        //判断当前时间是否秒杀活动即将开始或正在进行
        if(promoModel.getStartDate().isAfterNow()){
            promoModel.setStatus(1);
        }else if(promoModel.getEndDate().isBeforeNow()){
            promoModel.setStatus(3);
        }else{
            promoModel.setStatus(2);
        }
        //判断活动是否正在进行
        if(promoModel.getStatus().intValue() != 2){
            return null;
        }
        //判断item信息是否存在
        ItemModel itemModel = itemService.getItemByIdInCache(itemId);
        if(itemModel == null){
            return null;
        }
        //判断用户信息是否存在
        UserModel userModel = userService.getUserByIdInCache(userId);
        if(userModel == null){
            return null;
        }

        //获取秒杀大闸的count数量
        long result = redisTemplate.opsForValue().increment("promo_door_count_"+promoId,-1);
        if(result < 0){
            return null;
        }
        //生成token并且存入redis内并给一个5分钟的有效期
        String token = UUID.randomUUID().toString().replace("-","");

        redisTemplate.opsForValue().set("promo_token_"+promoId+"_userid_"+userId+"_itemid_"+itemId,token);
        redisTemplate.expire("promo_token_"+promoId+"_userid_"+userId+"_itemid_"+itemId,5, TimeUnit.MINUTES);

        return token;
    }
****
OrderController.java
***
@Autowired
private PromoService promoService;

/生成秒杀令牌
 @RequestMapping(value = "/generatetoken",method = {RequestMethod.POST},consumes={CONTENT_TYPE_FORMED}
 @ResponseBody
public CommonReturnType generatetoken(@RequestParam(name="itemId")Integer itemId,
                                        @RequestParam(name="promoId")Integer promoId) throws BusinessException {
    //根据token获取用户信息
     String token = httpServletRequest.getParameterMap().get("token")[0];
     if(StringUtils.isEmpty(token)){
          throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户还未登陆，不能下单");
     }
     //获取用户的登陆信息
     UserModel userModel = (UserModel) redisTemplate.opsForValue().get(token);
     if(userModel == null){
          throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户还未登陆，不能下单");
      }
      //获取秒杀访问令牌
      String promoToken = promoService.generateSecondKillToken(promoId,itemId,userModel.getId());
      if(promoToken == null){
          throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"生成令牌失败");
      }
      //返回对应的结果
      return CommonReturnType.create(promoToken);
} 

OrderServiceImpl.java
***
OrderController.java
***
//校验秒杀令牌是否正确
if(promoId != null){
    String inRedisPromoToken = (String) redisTemplate.opsForValue().get("promo_token_"+promoId+"_userid_"+userModel.getId()+"_itemid_"+itemId);
     if(inRedisPromoToken == null){
         throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"秒杀令牌校验失败");
       }
     if(!org.apache.commons.lang3.StringUtils.equals(promoToken,inRedisPromoToken)){
        throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"秒杀令牌校验失败");
      }
}  
 //修改前端代码
 getItem.html
 $.ajax({
		type:"POST",
		contentType:"application/x-www-form-urlencoded",
		url:"http://"+g_host+"/order/generatetoken?token="+token,
		data:{
			"itemId":g_itemVO.id,
			"promoId":g_itemVO.promoId
               },
{
		alert("获取令牌失败，原因为"+data.data.errMsg);
		if(data.data.errCode == 20003){
    		window.location.href="login.html";
		}
			
		},
		error:function(data){
		lert("获取令牌失败，原因为"+data.responseText);
		}
	}); 
```

**方案缺陷**
 **秒杀令牌活动一开始就无限制生成，影响系统性能；**

## 二、秒杀大闸

为了解决秒杀令牌在活动一开始无限制生成，影响系统的性能，提出了秒杀大闸的解决方案；

### 2.1 原理

- 依靠秒杀令牌的授权原理定制化发牌逻辑，解决用户对应流量问题，做到大闸功能；
- 根据秒杀商品初始化库存颁发对应数量令牌，控制大闸流量；
- 用户风控策略前置到秒杀令牌发放中；
- 库存售罄判断前置到秒杀令牌发放中。

### 2.2 代码实现

```java
PromoServiceImpl.java
***
 public void publishPromo(Integer promoId) {
//将大闸的限制数字设到redis内
 redisTemplate.opsForValue().set("promo_door_count_"+promoId,itemModel.getStock().intValue() * 5);
 }
@Override
public String generateSecondKillToken(Integer promoId,Integer itemId,Integer userId) {
         //判断是否库存已售罄，若对应的售罄key存在，则直接返回下单失败
        if(redisTemplate.hasKey("promo_item_stock_invalid_"+itemId)){
            return null;
        }
         //获取秒杀大闸的count数量
        long result = redisTemplate.opsForValue().increment("promo_door_count_"+promoId,-1);
        if(result < 0){
            return null;
        }
 }
 OrderController.java
 ***
```

**方案缺陷**

- 浪涌流量涌入后系统无法应对
- 多库存多商品等令牌限制能力弱；

## 三、队列泄洪

采用秒杀大闸之后，还是无法解决浪涌流量涌入后台系统，并且多库存多商品等令牌限制能力较弱；

### 3.1 原理

- 排队有些时候比并发更高效(例如redis单线程模型，innodb mutex key等)；
- 依靠排队去限制并发流量；
- 依靠排队和下游阻塞窗口程度调整队列释放流量大小；

以支付宝银行网关队列为例，支付宝需要对接许多银行网关，当你的支付宝绑定多张银行卡，那么支付宝对于这些银行都有不同的支付渠道。在大促活动时，支付宝的网关会有上亿级别的流量，银行的网关扛不住，支付宝就会将支付请求队列放到自己的消息队列中，依靠银行网关承诺可以处理的TPS流量去泄洪；

消息队列就像“水库”一样，拦蓄上游的洪水，削减进入下游河道的洪峰流量，从而达到减免洪水灾害的目的；

## 3.2 代码实现

```java
OrderController.java
***
private ExecutorService executorService;

 @PostConstruct
public void init(){
    //定义一个只有20个可工作线程的线程池
   executorService = Executors.newFixedThreadPool(20);
}
 //同步调用线程池的submit方法
//拥塞窗口为20的等待队列，用来队列化泄洪
 Future<Object> future = executorService.submit(new Callable<Object>() {
   @Override
   public Object call() throws Exception {
      //加入库存流水init状态
       String stockLogId = itemService.initStockLog(itemId,amount);
       //再去完成对应的下单事务型消息机制
       if(!mqProducer.transactionAsyncReduceStock(userModel.getId(),itemId,promoId,amount,stockLogId)){
           throw new BusinessException(EmBusinessError.UNKNOWN_ERROR,"下单失败");
          }
          return null;
        }
        });

        try {
            future.get();
        } catch (InterruptedException e) {
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR);
        } catch (ExecutionException e) {
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR);
        }
        return CommonReturnType.create(null);
}
```



## 四、本地 or 分布式

**本地：将队列维护在本地内存中；**
 **分布式：将队列设置到外部redis中**

比如说我们有100台机器，假设每台机器设置20个队列，那我们的拥塞窗口就是2000，但是由于负载均衡的关系，很难保证每台机器都能够平均收到对应的createOrder的请求，那如果将这2000个排队请求放入redis中，每次让redis去实现以及去获取对应拥塞窗口设置的大小，这种就是分布式队列；

**本地和分布式有利有弊：**

分布式队列最严重的就是性能问题，发送任何一次请求都会引起call网络的消耗，并且要对Redis产生对应的负载，Redis本身也是集中式的，虽然有扩展的余地。单点问题就是若Redis挂了，整个队列机制就失效了。

本地队列的好处就是完全维护在内存当中的，因此其对应的没有网络请求的消耗，只要JVM不挂，应用是存活的，那本地队列的功能就不会失效。因此企业级开发应用还是推荐使用本地队列，本地队列的性能以及高可用性对应的应用性和广泛性。可以使用外部的分布式集中队列，当外部集中队列不可用时或者请求时间超时，可以采用降级的策略，切回本地的内存队列。