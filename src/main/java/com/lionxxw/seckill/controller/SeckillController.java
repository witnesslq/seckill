package com.lionxxw.seckill.controller;

import com.lionxxw.seckill.dto.Exposer;
import com.lionxxw.seckill.dto.SeckillExecution;
import com.lionxxw.seckill.dto.SeckillResult;
import com.lionxxw.seckill.entity.Seckill;
import com.lionxxw.seckill.enums.SeckillStatEnum;
import com.lionxxw.seckill.exception.RepeatKillException;
import com.lionxxw.seckill.exception.SeckillCloseException;
import com.lionxxw.seckill.exception.SeckillException;
import com.lionxxw.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 秒杀控制层
 * Package com.lionxxw.seckill.controller
 * Project seckill
 * Company www.baofoo.com
 * Author wangjian@baofoo.com
 * Created on 2017/3/1 14:09
 * version 1.0.0
 */
@Controller
@RequestMapping(value = "/seckill")  // url:/模块/资源/{id}/细分
public class SeckillController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    /**
     * 秒杀列表页
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        List<Seckill> seckills = seckillService.getSeckillList();
        model.addAttribute("list", seckills);
        return "list";
    }

    /**
     * 秒杀详情页
     *
     * @param seckillId
     * @param model
     * @return
     */
    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (null == seckillId) {
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getById(seckillId);
        if (null == seckill) {
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    /**
     * 秒杀入口请求
     * @param seckillId
     * @return
     */
    @RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
        SeckillResult<Exposer> result;
        if (null == seckillId) {
            result = new SeckillResult<Exposer>(false, "参数异常");
            return result;
        }
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = new SeckillResult<Exposer>(false, e.getMessage());
        }

        return result;
    }

    /**
     * 秒杀操作
     * @param seckillId
     * @param md5
     * @param userPhone
     * @return
     */
    @RequestMapping(value = "/{seckillId}/{md5}/execute", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "killPhone", required = false) String userPhone) {
        SeckillResult<SeckillExecution> result;
        if (null == seckillId || null == md5 ) {
            result = new SeckillResult<SeckillExecution>(false, "参数异常");
            return result;
        }
        if (null == userPhone){
            result = new SeckillResult<SeckillExecution>(false, "未登陆");
            return result;
        }
        SeckillExecution seckillExecution;
        try {
            seckillExecution = seckillService.executeSeckill(seckillId, userPhone, md5);
            result = new SeckillResult<SeckillExecution>(true, seckillExecution);
        } catch (SeckillCloseException e1) {
            seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.END);
            result = new SeckillResult<SeckillExecution>(true, seckillExecution);
        } catch (RepeatKillException e2) {
            seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT);
            result = new SeckillResult<SeckillExecution>(true, seckillExecution);
        } catch (SeckillException e) {
            logger.error(e.getMessage(), e);
            seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
            result = new SeckillResult<SeckillExecution>(true, seckillExecution);
        }
        return result;
    }

    /**
     * 请求服务器时间
     * @return
     */
    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time(){
        return new SeckillResult<Long>(true, System.currentTimeMillis());
    }
}