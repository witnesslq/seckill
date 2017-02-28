package com.lionxxw.seckill.dao;

import com.lionxxw.seckill.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * @author wangjian@baofoo.com
 * @version 1.0.0
 * @description 配置spring和junit整合,junit启动时加载springIOC容器
 * spring-test,junit
 * @package com.lionxxw.seckill.dao
 * @project seckill
 * @company www.baofoo.com
 * @date 2017/2/28 14:32
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
    // 注入Dao实现类依赖
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void reduceNumber() throws Exception {
        long seckillId = 1000L;
        Date killTime = new Date();
        int i = seckillDao.reduceNumber(seckillId, killTime);
        System.out.println(i);
    }

    @Test
    public void queryById() throws Exception {
        long id = 1000L;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
    }

    /**
     * nested exception is org.apache.ibatis.binding.BindingException: Parameter 'offet' not found. Available parameters are [0, 1, param1, param2]
     * java 没有保存行参的记录:queryAll(int offet, int limit) ==> query(arg0,arg1)
     * @throws Exception
     */
    @Test
    public void queryAll() throws Exception {
        List<Seckill> seckills = seckillDao.queryAll(0, 4);
        for (Seckill s: seckills){
            System.out.println(s.getName());
        }
    }

}