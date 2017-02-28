package com.lionxxw.seckill.dao;

import com.lionxxw.seckill.entity.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author wangjian@baofoo.com
 * @version 1.0.0
 * @description TODO
 * @package com.lionxxw.seckill.dao
 * @project seckill
 * @company www.baofoo.com
 * @date 2017/2/28 14:41
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {
    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() throws Exception {
        int i = successKilledDao.insertSuccessKilled(1000L, "18721472363");
        System.out.println(i);
    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        long id = 1000L;
        String moblie = "18721472363";
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(id, moblie);
        System.out.println(successKilled);
    }

}