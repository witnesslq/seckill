package com.lionxxw.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.lionxxw.seckill.entity.Seckill;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;

/**
 * redis缓存访问
 * Package com.lionxxw.seckill.dao.cache
 * Project seckill
 * Company www.baofoo.com
 * Author wangjian@baofoo.com
 * Created on 2017/3/2 13:24
 * version 1.0.0
 */
public class RedisDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JedisPool jedisPool;

    public RedisDao(String host, int port, String password) {
//        this.jedisPool = new JedisPool(host, port);
        this.jedisPool = new JedisPool(new GenericObjectPoolConfig(), host,port, Protocol.DEFAULT_TIMEOUT, password);
    }

    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    public Seckill getSeckill(Long seckillId) {
        try {
            Jedis resource = jedisPool.getResource();
            try {
                String key = "seckillId:" + seckillId;
                // 并没有实现内部序列化操作
                // get->byte[]->反序列化->Object(Seckill)
                // 采用自定义序列化
                // protostuff : pojo
                byte[] bytes = resource.get(key.getBytes());
                // 获取到缓存
                if (null != bytes) {
                    // 空对象
                    Seckill seckill = schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
                    // seckill 被反序列化
                    return seckill;
                }
            } finally {
                resource.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    public String putSeckill(Seckill seckill) {
        // set Object(Seckill)->序列化->byte[]
        try {
            Jedis resource = jedisPool.getResource();
            try {
                String key = "seckillId:" + seckill.getSeckillId();
                byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                // 超时缓存
                int timeout = 60 * 60; // 单位秒
                String result = resource.setex(key.getBytes(), timeout, bytes);
                return result;
            } finally {
                resource.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }
}
