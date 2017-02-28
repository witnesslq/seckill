package com.lionxxw.seckill.dto;

import com.lionxxw.seckill.entity.SuccessKilled;
import com.lionxxw.seckill.enums.SeckillStatEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 封装秒杀执行后结果
 * Package com.lionxxw.seckill.dto
 * Project seckill
 * Company www.baofoo.com
 * Author wangjian@baofoo.com
 * Created on 2017/2/28 16:04
 * version 1.0.0
 */
@Getter
@Setter
public class SeckillExecution implements Serializable{

    private static final long serialVersionUID = 2032787759215856532L;

    private Long seckillId;                 // 秒杀商品id
    private Integer state;                  // 秒杀执行结果状态
    private String stateInfo;               // 状态表示
    private SuccessKilled successKilled;    // 秒杀成功对象

    public SeckillExecution(Long seckillId, SeckillStatEnum seckillStatEnum, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = seckillStatEnum.getState();
        this.stateInfo = seckillStatEnum.getStateInfo();
        this.successKilled = successKilled;
    }

    public SeckillExecution(Long seckillId, SeckillStatEnum seckillStatEnum) {
        this.seckillId = seckillId;
        this.state = seckillStatEnum.getState();
        this.stateInfo = seckillStatEnum.getStateInfo();
    }
}
