package com.lionxxw.seckill.service;

import com.lionxxw.seckill.dto.Exposer;
import com.lionxxw.seckill.dto.SeckillExecution;
import com.lionxxw.seckill.entity.Seckill;
import com.lionxxw.seckill.exception.RepeatKillException;
import com.lionxxw.seckill.exception.SeckillCloseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author wangjian@baofoo.com
 * @version 1.0.0
 * @description 接口集成测试类
 * @package com.lionxxw.seckill.service
 * @project seckill
 * @company www.baofoo.com
 * @date 2017/3/1 9:48
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> seckillList = seckillService.getSeckillList();
        logger.info("seckillList={}", seckillList);
    }

    @Test
    public void getById() throws Exception {
        Long id = 1000L;
        Seckill seckill = seckillService.getById(id);
        logger.info("seckill={}", seckill);
    }

    /**
     * 测试整个秒杀业务逻辑
     * @throws Exception
     */
    @Test
    public void testSeckillLogic() throws Exception {
        Long id = 1000L;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if (exposer.getExposed()) {
            logger.info("exposer={}", exposer);
            String userPhone = "18721472364";
            String md5 = exposer.getMd5();
            SeckillExecution seckillExecution = null;
            try {
                seckillExecution = seckillService.executeSeckill(id, userPhone, md5);
                logger.info("seckillExecution={}", seckillExecution);
            } catch (SeckillCloseException e) {
                logger.error(e.getMessage());
            } catch (RepeatKillException e) {
                logger.error(e.getMessage());
            }
        } else {
            // 秒杀未开始
            logger.warn("exposer={}", exposer);
        }

    }

    /**
     * 测试存储过程
     */
    @Test
    public void testExecuteSeckillByProcedure(){
        Long id = 1001L;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if (exposer.getExposed()) {
            logger.info("exposer={}", exposer);
            String userPhone = "18721472364";
            String md5 = exposer.getMd5();
            SeckillExecution seckillExecution = null;
            try {
                seckillExecution = seckillService.executeSeckillByProcedure(id, userPhone, md5);
                logger.info("seckillExecution={}", seckillExecution);
            } catch (SeckillCloseException e) {
                logger.error(e.getMessage());
            } catch (RepeatKillException e) {
                logger.error(e.getMessage());
            }
        } else {
            // 秒杀未开始
            logger.warn("exposer={}", exposer);
        }
    }
}