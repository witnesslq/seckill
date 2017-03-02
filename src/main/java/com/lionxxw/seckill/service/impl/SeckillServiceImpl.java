package com.lionxxw.seckill.service.impl;

import com.lionxxw.seckill.dao.SeckillDao;
import com.lionxxw.seckill.dao.SuccessKilledDao;
import com.lionxxw.seckill.dao.cache.RedisDao;
import com.lionxxw.seckill.dto.Exposer;
import com.lionxxw.seckill.dto.SeckillExecution;
import com.lionxxw.seckill.entity.Seckill;
import com.lionxxw.seckill.entity.SuccessKilled;
import com.lionxxw.seckill.enums.SeckillStatEnum;
import com.lionxxw.seckill.exception.RepeatKillException;
import com.lionxxw.seckill.exception.SeckillCloseException;
import com.lionxxw.seckill.exception.SeckillException;
import com.lionxxw.seckill.service.SeckillService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 秒杀业务接口实现
 * Package com.lionxxw.seckill.service.impl
 * Project seckill
 * Company www.baofoo.com
 * Author wangjian@baofoo.com
 * Created on 2017/2/28 17:04
 * version 1.0.0
 */
@Service
public class SeckillServiceImpl implements SeckillService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;
    @Autowired
    private RedisDao redisDao;
    /**
     * md5加盐字符串,用于混淆md5
     */
    private final String slat = "skjklj==14/jkjjk23/@s!##9j";

    @Override
    public List<Seckill> getSeckillList() {
        List<Seckill> seckills = seckillDao.queryAll(0, 4);
        return seckills;
    }

    @Override
    public Seckill getById(Long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);
        return seckill;
    }

    @Override
    public Exposer exportSeckillUrl(Long seckillId) {
        // 优化点:缓存优化
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (null == seckill) {
            seckill = seckillDao.queryById(seckillId);
            if (null == seckill) {
                return new Exposer(false, seckillId);
            } else {
                redisDao.putSeckill(seckill);
            }
        }

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();  // 当前系统时间
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        /**
         * 转化特定字符串
         */
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    @Override
    @Transactional
    /**
     * 使用注解控制事务方法的优点:
     *  1.开发团队达成一致约定,明确标注事务方法的编程风格;
     *  2.保证事务方法的执行时间尽可能短,不要穿插其他网络操作RPC/HTTP请求或者剥离到事务方法外部;
     *  3.不是所有的方法都需要事务,如只有一条修改操作,只读操作不需要事务控制.
     */
    public SeckillExecution executeSeckill(Long seckillId, String userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if (null == md5 || null == seckillId || !md5.equals(getMD5(seckillId))) {
//            throw new SeckillException("seckill data rewrite!");
            return new SeckillExecution(seckillId, SeckillStatEnum.DATA_REWRITE);
        }
        // 执行秒杀逻辑:减库存 + 记录购买行为
        Date nowTime = new Date();
        try {
           /* int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
            if (updateCount <= 0) {
                // 没有更新到记录,秒杀结束
                throw new SeckillCloseException("seckill is closed");
            } else {
                // 记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                if (insertCount <= 0) {
                    // 重复秒杀
                    throw new RepeatKillException("seckill repeated");
                } else {
                    // 秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }
            }*/
            /**
             * 优化处理,更改业务逻辑,减少rowLock时间
             * 1.记录购买行为
             * 2.减库存操作
             */
            // 记录购买行为
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            if (insertCount <= 0) {
                // 重复秒杀
                throw new RepeatKillException("seckill repeated");
            } else {
                // 减库存,热点商品竞争
                int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
                if (updateCount <= 0) {
                    // 没有更新到记录,秒杀结束 rollback
                    throw new SeckillCloseException("seckill is closed");
                }
                // 秒杀成功 commit
                SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // 所有编译期异常,转化为运行期异常
//            throw new SeckillException("seckill inner error:" + e.getMessage());
            return new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
        }
    }

    @Override
    public SeckillExecution executeSeckillByProcedure(Long seckillId, String userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if (null == md5 || null == seckillId || !md5.equals(getMD5(seckillId))) {
//            throw new SeckillException("seckill data rewrite!");
            return new SeckillExecution(seckillId, SeckillStatEnum.DATA_REWRITE);
        }
        // 执行秒杀逻辑:减库存 + 记录购买行为
        Date nowTime = new Date();
        Map<String, Object> map = new HashMap<String, Object>(4);
        map.put("seckillId", seckillId);
        map.put("killPhone", userPhone);
        map.put("killTime", nowTime);
        map.put("result", null);
        // 执行存储过程,result被赋值
        try {
            seckillDao.killByProcedure(map);
            Integer result = MapUtils.getInteger(map, "result", -2);
            if (1 == result) {
                // 秒杀成功
                // 秒杀成功 commit
                SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
            } else {
                return new SeckillExecution(seckillId, SeckillStatEnum.stateOf(result));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
        }
    }

    private String getMD5(Long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}