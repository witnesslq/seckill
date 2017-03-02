package com.lionxxw.seckill.service;

import com.lionxxw.seckill.dto.Exposer;
import com.lionxxw.seckill.dto.SeckillExecution;
import com.lionxxw.seckill.entity.Seckill;
import com.lionxxw.seckill.exception.RepeatKillException;
import com.lionxxw.seckill.exception.SeckillCloseException;
import com.lionxxw.seckill.exception.SeckillException;

import java.util.List;

/**
 * 业务接口:站在"使用者"角度设计接口
 * 三个方面:
 *      方法定义粒度,
 *      参数,
 *      返回类型(return 类型/异常)
 * Package com.lionxxw.seckill.service
 * Project seckill
 * Company www.baofoo.com
 * Author wangjian@baofoo.com
 * Created on 2017/2/28 15:49
 * version 1.0.0
 */
public interface SeckillService {
    /**
     * 查询所有秒杀记录
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 查询单个秒杀记录
     * @param seckillId
     * @return
     */
    Seckill getById(Long seckillId);

    /**
     * 秒杀开启时输出秒杀接口地址,
     * 否则输出系统时间和秒杀时间
     * @param seckillId
     * @return
     */
    Exposer exportSeckillUrl(Long seckillId);

    /**
     * 执行秒杀操作
     * @param seckillId 秒杀商品id
     * @param userPhone 用户手机号码
     * @param md5       服务器返回的加密参数
     */
    SeckillExecution executeSeckill(Long seckillId, String userPhone, String md5) throws SeckillException,RepeatKillException,SeckillCloseException;

    /**
     * 执行秒杀操作 by 存储过程
     * @param seckillId 秒杀商品id
     * @param userPhone 用户手机号码
     * @param md5       服务器返回的加密参数
     */
    SeckillExecution executeSeckillByProcedure(Long seckillId, String userPhone, String md5) throws SeckillException,RepeatKillException,SeckillCloseException;
}