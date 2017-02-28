package com.lionxxw.seckill.dao;

import com.lionxxw.seckill.entity.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 秒杀库存dao
 * Package com.lionxxw.seckill.dao
 * Project seckill
 * Company www.baofoo.com
 * Author wangjian@baofoo.com
 * Created on 2017/2/28 11:32
 * version 1.0.0
 */
public interface SeckillDao {
    /**
     * 减库存操作
     * @param seckillId
     * @param killTime
     * @return
     */
    int reduceNumber(@Param("seckillId")Long seckillId, @Param("killTime")Date killTime);

    /**
     * 根据id查询秒杀库存
     * @param seckillId
     * @return
     */
    Seckill queryById(@Param("seckillId")Long seckillId);

    /**
     * 根据偏移量查询秒杀库存
     * @param offset
     * @param limit
     * @return
     */
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit")int limit);
}