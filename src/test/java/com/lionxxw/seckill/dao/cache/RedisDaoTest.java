package com.lionxxw.seckill.dao.cache;

import com.lionxxw.seckill.dao.SeckillDao;
import com.lionxxw.seckill.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * @author wangjian@baofoo.com
 * @version 1.0.0
 * @description redis集成测试
 * @package com.lionxxw.seckill.dao.cache
 * @project seckill
 * @company www.baofoo.com
 * @date 2017/3/2 13:56
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {
    @Autowired
    private RedisDao redisDao;
    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void testRedis(){
        Long id = 1000L;
        Seckill seckill1 = redisDao.getSeckill(id);
        if (null == seckill1){
            System.out.println("redis is not data");
            Seckill seckill = seckillDao.queryById(id);
            if (null != seckill){
                System.out.println("redis insert data");
                redisDao.putSeckill(seckill);
            }else{
                System.out.println("mysql is not data");
            }
        }else{
            System.out.println("redis have data");
        }
    }
}