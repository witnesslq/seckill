# seckill 简易版秒杀系统

#框架:
    spring + spring mvc + mybatis

#前端样式
    bootstrap+jquery

##前端插件
    --jquery.cookie
    --jquery.countdown
 
###项目运行前,请先根据sql/schema.sql 文件生成表结构

## 温故而知新,重复下spring mvc的运行流程
![image](https://raw.githubusercontent.com/fimi2008/seckill/master/images-folder/springmvc.png)
 
#业务接口:站在"使用者"角度设计接口

    三个方面:
     方法定义粒度,
     参数,
     返回类型(return 类型/异常)
  
#前端页面流程
![image](https://raw.githubusercontent.com/fimi2008/seckill/master/images-folder/lc.png)
![image](https://raw.githubusercontent.com/fimi2008/seckill/master/images-folder/lc2.png)

#Restful api 接口设计
![image](https://raw.githubusercontent.com/fimi2008/seckill/master/images-folder/restful.png)

#秒杀优化
##存在性能瓶颈的地方
![image](https://raw.githubusercontent.com/fimi2008/seckill/master/images-folder/problem.png)

    *红色部分代表可能出现高并发的点
  
##其他方案
![image](https://raw.githubusercontent.com/fimi2008/seckill/master/images-folder/other.png)

    成本分析:
        *运维成本和稳定型:NoSQL,MQ等
        *开发成本:数据一致性,回滚方案等
        *幂等性难保证:重复秒杀问题
        *不适合新手的架构
        
#优化处理
    1.使用redis缓存,减少获取秒杀地址接口对mysql数据库的压力
    2.更改业务流程,减少rowLock的占用时间(秒杀业务改用存储过程)
![image](https://raw.githubusercontent.com/fimi2008/seckill/master/images-folder/yhbefore.png)
![image](https://raw.githubusercontent.com/fimi2008/seckill/master/images-folder/yhafter.png)