package com.lionxxw.seckill.dao;

import com.lionxxw.seckill.entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;

/**
 * 成功秒杀明细
 * Package com.lionxxw.seckill.dao
 * Project seckill
 * Company www.baofoo.com
 * Author wangjian@baofoo.com
 * Created on 2017/2/28 13:11
 * version 1.0.0
 */
public interface SuccessKilledDao {
    /**
     * 插入购买明细,可过滤重复
     * @param seckillId
     * @param userPhone
     * @return
     */
    int insertSuccessKilled(@Param("seckillId")Long seckillId, @Param("userPhone")String userPhone);

    /**
     * 根据id查询successKilled并携带秒杀产品对象实体
     * @param seckillId
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId")Long seckillId, @Param("userPhone")String userPhone);
}
