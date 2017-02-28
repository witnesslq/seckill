package com.lionxxw.seckill.service.impl;

import com.lionxxw.seckill.dao.SeckillDao;
import com.lionxxw.seckill.dao.SuccessKilledDao;
import com.lionxxw.seckill.dto.Exposer;
import com.lionxxw.seckill.dto.SeckillExecution;
import com.lionxxw.seckill.entity.Seckill;
import com.lionxxw.seckill.entity.SuccessKilled;
import com.lionxxw.seckill.enums.SeckillStatEnum;
import com.lionxxw.seckill.exception.RepeatKillException;
import com.lionxxw.seckill.exception.SeckillCloseException;
import com.lionxxw.seckill.exception.SeckillException;
import com.lionxxw.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 秒杀业务接口实现
 * Package com.lionxxw.seckill.service.impl
 * Project seckill
 * Company www.baofoo.com
 * Author wangjian@baofoo.com
 * Created on 2017/2/28 17:04
 * version 1.0.0
 */
public class SeckillServiceImpl implements SeckillService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SeckillDao seckillDao;
    @Resource
    private SuccessKilledDao successKilledDao;
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
        Seckill seckill = seckillDao.queryById(seckillId);
        if (null == seckill) {
            return new Exposer(false, seckillId);
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
    public SeckillExecution executeSeckill(Long seckillId, String userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if (null == md5 || null == seckillId || md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite!");
        }
        // 执行秒杀逻辑:减库存 + 记录购买行为
        Date nowTime = new Date();
        try {
            int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
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
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // 所有编译期异常,转化为运行期异常
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }
    }

    private String getMD5(Long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}